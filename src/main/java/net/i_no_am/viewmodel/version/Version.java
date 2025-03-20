package net.i_no_am.viewmodel.version;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.viewmodel.Global;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Version implements Global {

    private static final Map<String, Double> versionCache = new HashMap<>();
    private final String api;
    private final String download;
    private static boolean bl = false;
    private final double version;

    /**
     @param api The link to the github repo api.
     @param download The link to the download page.
     ***/

    public Version(String api, String download) throws Exception {
        this.api = api;
        this.download = download;
        this.version = getVApi();
    }

    public static Version create(String apiLink, String downloadLink) {
        try {
            return new Version(apiLink, downloadLink);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyUpdate(boolean printVersions) {
        if (!bl && mc.currentScreen == null && mc.player != null && !isUpdated()) {
            if (printVersions) {
                System.out.println("Versions: \nCurrent Version: " + getSelf() + "\n" + "Online Version: " + getApi());
            }
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

    private static double parseVersion(String version) {
        String[] parts = version.split("-");

        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+\\.\\d+")) {
                String[] versionNumbers = part.split("\\.");
                double parsedVersion = Double.parseDouble(versionNumbers[0] + "." + versionNumbers[1]);
                return parsedVersion * 10;
            } else if (part.matches("\\d+\\.\\d+")) return Double.parseDouble(part);
        }

        return 0.0;
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
}