package tech.zmario.coraloverlay.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tech.zmario.coraloverlay.objects.BedWarsUser;
import tech.zmario.coraloverlay.objects.StatsUser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UserManager {

    private static final String USER_ENDPOINT = "https://api.coralmc.it/api/user/%s";
    private static final String INFO_ENDPOINT = "https://api.coralmc.it/api/user/%s/infos/";
    private static final Gson GSON = new Gson();

    private final HttpClient httpClient;
    private final List<String> loadedPlayers = new ArrayList<>();

    private final Cache<String, BedWarsUser> userCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();

    public UserManager() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public CompletableFuture<BedWarsUser> getUser(String username) {
        userCache.cleanUp();

        System.out.println("Getting user " + username);

        if (userCache.asMap().containsKey(username))
            return CompletableFuture.completedFuture(userCache.getIfPresent(username));

        return makeRequest(String.format(USER_ENDPOINT, username)).thenApply(response -> {
            System.out.println("Got response for " + username);
            BedWarsUser user = response.statusCode() != 200 ?
                    new BedWarsUser() :
                    GSON.fromJson(response.body(), StatsUser.class).getBedWarsUser();

            userCache.put(username, user);
            return user;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    private CompletableFuture<HttpResponse<String>> makeRequest(String format) {
        return httpClient.sendAsync(HttpRequest.newBuilder().uri(URI.create(format)).build(),
                HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<String> getPrefix(String userName) {
        return makeRequest(String.format(INFO_ENDPOINT, userName))
                .thenApply(response -> {
                    if (response.statusCode() != 200) return "disguised";
                    JsonElement jsonElement = JsonParser.parseString(response.body());

                    if (!jsonElement.isJsonObject()) return "disguised";
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    JsonElement prefix;
                    if (!jsonObject.has("vipBedwars"))
                        prefix = jsonObject.get("globalRank");
                    else
                        prefix = jsonObject.get("vipBedwars");

                    return prefix.getAsString().split("\\.", 3)[2];
                });
    }

    public List<String> getPlayerNames() {
        return loadedPlayers;
    }
}
