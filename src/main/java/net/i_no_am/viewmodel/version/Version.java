package net.i_no_am.viewmodel.version;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.ViewModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Version implements Global {

    private static final String REPO = "https://api.github.com/repos/I-No-oNe/View-Model/releases/latest";

    public static void checkUpdates() {
        try {
            String latestVersion = getLatestVersionFromGitHub();
            if (!latestVersion.equals(getModVersion())) {
                ViewModel.isOutdated = true;
            }
        } catch (Exception ignored) {}
    }

    private static String getLatestVersionFromGitHub() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(Version.REPO).openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        return jsonResponse.get("tag_name").getAsString();
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
