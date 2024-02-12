package tech.zmario.coraloverlay.utils;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ClientUtils {

    public static Path detectClient() {
        String homedir = System.getProperty("user.home");

        // directories from https://github.com/Chit132/abyss-overlay/blob/2fbcf85518185a83b9f7857217df679165ca7df6/src/clients.js#L6

        List<File> files = Arrays.asList(
                new File(homedir + "/Library/Application Support/minecraft/logs/blclient/minecraft/latest.log"),
                new File(homedir + "/Library/Application Support/minecraft/logs/latest.log"),
                new File(homedir + "/Library/Application Support/.pvplounge/logs/latest.log"),
                new File(homedir + "/Library/Application Support/minecraft/logs/fml-client-latest.log"),
                new File(homedir + "/Library/Application Support/minecraft/logs/latest.log"),
                new File(homedir + "/AppData/Roaming/.minecraft/logs/blclient/minecraft/latest.log"),
                new File(homedir + "/AppData/Roaming/.minecraft/logs/latest.log"),
                new File(homedir + "/AppData/Roaming/.pvplounge/logs/latest.log"),
                new File(homedir + "/AppData/Roaming/.minecraft/logs/fml-client-latest.log"),
                new File(homedir + "/AppData/Roaming/.minecraft/logs/latest.log"),
                new File(homedir + "/.lunarclient/offline/1.7/logs/latest.log"),
                new File(homedir + "/.lunarclient/offline/1.8/logs/latest.log"),
                new File(homedir + "/.lunarclient/offline/1.8.9/logs/latest.log"),
                new File(homedir + "/.lunarclient/offline/multiver/logs/latest.log")
        );

        files.sort((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

        Path path = files.get(0) == null ? null : files.get(0).toPath();

        System.out.println("Detected client path: " + path);
        return path;
    }
}
