package tech.zmario.coraloverlay.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tech.zmario.coraloverlay.CoralOverlay;
import tech.zmario.coraloverlay.objects.BedWarsUser;
import tech.zmario.coraloverlay.objects.StatsUser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class UserManager {

    private static final String USER_ENDPOINT = "https://api.coralmc.it/api/user/%s";
    private static final String INFO_ENDPOINT = USER_ENDPOINT + "/infos/";
    private static final Gson GSON = new Gson();

    private final HttpClient httpClient;
    private final List<String> loadedPlayers = new ArrayList<>();

    private final Cache<String, BedWarsUser> userCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();

    public UserManager() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public CompletableFuture<BedWarsUser> getUser(String username) {
        userCache.cleanUp();

        CoralOverlay.LOGGER.info(() -> "Getting user " + username);

        if (userCache.asMap().containsKey(username)) {
            CoralOverlay.LOGGER.info(() -> "User " + username + " found in cache");
            return CompletableFuture.completedFuture(userCache.getIfPresent(username));
        }

        CoralOverlay.LOGGER.info(() -> "User " + username + " not found in cache");

        return makeRequest(String.format(USER_ENDPOINT, username))
                .exceptionally(throwable -> {
                    CoralOverlay.LOGGER.log(Level.SEVERE, String.format("Error while getting user %s", username), throwable);
                    return null;
                })
                .thenApply(response -> {
                    CoralOverlay.LOGGER.info(() -> "Got user " + username + " with status code " + response.statusCode());

                    BedWarsUser user = response.statusCode() != 200 ?
                            new BedWarsUser() :
                            GSON.fromJson(response.body(), StatsUser.class).getBedWarsUser();

                    userCache.put(username, user);
                    return user;
                });
    }

    private CompletableFuture<HttpResponse<String>> makeRequest(String format) {
        CoralOverlay.LOGGER.info(() -> "Making request to " + format);

        // check if the http client is alive

        return httpClient.sendAsync(HttpRequest.newBuilder().uri(URI.create(format))
                        .timeout(Duration.ofSeconds(5)).build(),
                HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<String> getPrefix(String userName) {
        return makeRequest(String.format(INFO_ENDPOINT, userName)).thenApply(response -> {
            if (response.statusCode() != 200) return "disguised";
            JsonElement jsonElement = JsonParser.parseString(response.body());

            if (!jsonElement.isJsonObject()) return "disguised";
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            JsonElement prefix;
            String globalRank = jsonObject.get("globalRank").getAsString();
            JsonElement bedwarsVipPrefix = jsonObject.get("vipBedwars");

            if (globalRank.equals("prefix.0.&7") && bedwarsVipPrefix != null) {
                prefix = bedwarsVipPrefix;
            } else {
                prefix = jsonObject.get("globalRank");
            }

            CoralOverlay.LOGGER.info(() -> "Got prefix " + prefix.getAsString() + " for " + userName);
            return prefix.getAsString().split("\\.", 3)[2];
        });
    }

    public List<String> getPlayerNames() {
        return loadedPlayers;
    }
}
