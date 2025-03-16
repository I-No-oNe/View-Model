package net.i_no_am.viewmodel.version;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.ViewModel;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Version implements Global {

    private final String apiLink;
    private final String downloadLink;
    private final double selfVersion;
    private double apiVersion;

    public Version(String apiLink, String downloadLink) {
        this.apiLink = apiLink;
        this.downloadLink = downloadLink;
        this.selfVersion = getSelfVersion();
        try {
            this.apiVersion = getApiVersion();
        } catch (Exception e) {
            this.apiVersion = 0.0;
            ViewModel.Log("Error fetching API version: " + e.getMessage());
        }
    }

    public boolean isUpdated() {
        return getVSelf() >= getVApi();
    }

    public double getVSelf() {
        return selfVersion;
    }

    public double getVApi() {
        return apiVersion;
    }

    private double getApiVersion() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiLink)).timeout(Duration.ofSeconds(31)).header("Accept", "application/vnd.github.v3+json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch latest version: " + response.statusCode());
        }

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        String versionString = jsonResponse.get("tag_name").getAsString();

        return parseVersion(versionString);
    }

    private static double getSelfVersion() {
        String versionString = FabricLoader.getInstance().getModContainer(modId).orElseThrow(() -> new RuntimeException("Mod not found!")).getMetadata().getVersion().getFriendlyString();
        return parseVersion(versionString);
    }

    private static double parseVersion(String version) {
        String[] parts = version.split("-");
        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+")) {
                return Double.parseDouble(part);
            }
        }
        return 0.0;
    }

    public void notifyV() {
        boolean did = false;
        if (!did && mc.currentScreen == null && mc.player != null && !isUpdated()) {
            mc.setScreen(new ConfirmScreen(confirmed -> {
                if (confirmed) {
                    Util.getOperatingSystem().open(URI.create(downloadLink));
                    mc.player.closeScreen();
                }
                mc.player.closeScreen();
            }, Text.of(Formatting.RED + "You are using an outdated version of View Model"), Text.of("Please download the latest version from " + Formatting.GREEN + "modrinth"), Text.of("Download"), Text.of("Continue playing")));
            did = true;
        }
    }
}