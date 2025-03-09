package net.i_no_am.viewmodel.version;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.ViewModel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Version implements Global {

        private static final String SOURCE = "https://raw.githubusercontent.com/I-No-oNe/View-Model/refs/heads/1.21.4-midnight-lib/version";

    public static void checkUpdates() {
        try {
            String latestVersion = getLatestVersionFromGitHub();
            if (!latestVersion.equals(getModVersion())) {
                ViewModel.isOutdated = true;
                System.out.println("Oh no, you are using an outdated version of ViewModel! The latest version is " + latestVersion);
            }
        } catch (Exception ignored) {}
    }

    private static String getLatestVersionFromGitHub() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SOURCE))
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch latest version: " + response.statusCode());
        }

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        return jsonResponse.get("version").getAsString();
    }


    public static String getModVersion() {
        String fullVersionString = FabricLoader.getInstance().getModContainer(modId).get().getMetadata().getVersion().getFriendlyString();
        String[] parts = fullVersionString.split("-");
        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+")) {
                return part;
            }
        }
        return "Unknown";
    }
}
