package net.i_no_am.viewmodel.version;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Version implements Global {

    private static final Map<String, Double> versionCache = new HashMap<>();
    @Nullable private final String api;
    @Nullable private final String download;
    private static boolean bl = false;
    private final double version;

    /**
     @param api The link to the GITHUB repo api.
     @param download The link to the download page.
     ***/

    private Version(@Nullable String api, @Nullable String download) throws Exception {
        this.api = api;
        this.download = download;
        this.version = Config.shouldCheck ? getVApi() : 0.0;
    }

    public static Version create(String apiLink, String downloadLink) {
        try {
            return new Version(apiLink, downloadLink);
        } catch (Exception e) {
            Utils.log(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void notifyUpdate(boolean printVersions) {
        if (!bl && mc.currentScreen == null && mc.player != null && !isUpdated() && Config.shouldCheck) {
            if (printVersions) Utils.log("Versions: \nCurrent Version: " + getSelf() + "\n" + "Online Version: " + getApi() + "\n" + "isUpdated() state: " + isUpdated());
            mc.setScreen(new ConfirmScreen(confirmed -> {
                if (confirmed) {
                    Util.getOperatingSystem().open(URI.create(download));
                    mc.player.closeScreen();
                }
                mc.player.closeScreen();
            }, Text.of(Formatting.RED + "You are using an outdated version of View Model"), Text.of("Please download the latest version from " + Formatting.GREEN + "Modrinth"), Text.of("Download"), Text.of("Continue playing")));
            bl = true;
        }
    }

    private boolean isUpdated() {
        return getSelf() >= getApi();
    }

    private double getApi() {
        return version;
    }

    private static double getSelf() {
        String versionString = FabricLoader.getInstance().getModContainer(modId).orElseThrow(() -> new RuntimeException(modId + " Isn't loaded")).getMetadata().getVersion().getFriendlyString();
        return parseVersion(versionString);
    }

    /**
     * Parses a version string into a composite integer.
     * For example, "1.0.1" becomes 10001 and "1.0.2" becomes 10002.
     *
     * @param version the version string that is taking from the mod version from gradle.properties
     * @return the composite version as an integer
     */
    private static int parseVersion(String version) {
        String[] parts = version.split("-");
        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+\\.\\d+")) {
                String[] versionNumbers = part.split("\\.");
                int major = Integer.parseInt(versionNumbers[0]);
                int minor = Integer.parseInt(versionNumbers[1]);
                int patch = Integer.parseInt(versionNumbers[2]);
                return major * 10000 + minor * 100 + patch;
            } else if (part.matches("\\d+\\.\\d+")) {
                String[] versionNumbers = part.split("\\.");
                int major = Integer.parseInt(versionNumbers[0]);
                int minor = Integer.parseInt(versionNumbers[1]);
                return major * 10000 + minor * 100;
            }
        }
        return 0;
    }

    private double getVApi() throws Exception {
        if (versionCache.containsKey(api)) {
            return versionCache.get(api);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(api)).timeout(Duration.ofSeconds(600)).header("Accept", "application/vnd.github.v3+json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch latest version: " + response.statusCode());
        }

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        String versionString = jsonResponse.get("tag_name").getAsString();

        double parsedVersion = parseVersion(versionString);
        versionCache.put(api, parsedVersion);
        return parsedVersion;
    }

    public static class Utils {
        private static final File logFile = new File(mc.runDirectory + "/logs", "view-model.log");
        private static boolean wroteLaunchHeader = false;
        static {
            try {
                logFile.getParentFile().mkdirs();
                if (logFile.exists()) {
                    logFile.delete();
                }
            } catch (Exception e) {
                System.err.println("[View-Model] Failed to prepare log file: " + e.getMessage());
            }
        }

        public static void log(String message) {
            if (isDev) {
                System.out.println("[ViewModel] " + message);
            }

            try (FileWriter writer = new FileWriter(logFile, true)) {
                if (!wroteLaunchHeader) {
                    writer.write("=== Launch at " + getTimestamp(true) + " ===\n");
                    wroteLaunchHeader = true;
                }
                writer.write(getTimestamp(false) + " " + message + "\n");
            } catch (IOException e) {
                System.err.println("Failed to write to view-model.log: " + e.getMessage());
            }
        }

        private static String getTimestamp(boolean includeFullDate) {
            DateTimeFormatter fullDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
            DateTimeFormatter timeOnly = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
            return LocalDateTime.now().format(includeFullDate ? fullDate : timeOnly);
        }
    }
}