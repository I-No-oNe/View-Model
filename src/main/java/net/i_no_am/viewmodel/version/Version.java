package net.i_no_am.viewmodel.version;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.ViewModel;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class Version implements Global {

    private static final String SOURCE = "https://raw.githubusercontent.com/I-No-oNe/View-Model/refs/heads/1.21.4-midnight-lib/version";
    private static boolean sent = false;

    private static boolean isUpdated() throws Exception {
        return getLatestVersionFromGitHub().equals(getModVersion());
    }

    private static String getLatestVersionFromGitHub() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SOURCE))
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "*/*")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch latest version: " + response.statusCode());
        }

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        return jsonResponse.get("version").getAsString();
    }


    private static String getModVersion() {
        String fullVersionString = FabricLoader.getInstance().getModContainer(modId).get().getMetadata().getVersion().getFriendlyString();
        String[] parts = fullVersionString.split("-");
        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+")) {
                return part;
            }
        }
        return "Unknown";
    }

    public static void sendUpdate() throws Exception {
        if (!isUpdated() && mc.player != null && !sent) {
            mc.player.sendMessage(Text.of(PREFIX + "§cYou are using an outdated version of View Model!"), false);
            Text clickableMessage = Text.literal(PREFIX + "§aClick here to update!")
                    .setStyle(Style.EMPTY
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/I-No-oNe/View-Model/releases/latest"))
                            .withUnderline(true)
                            .withColor(0x00FF00)
                    );
            mc.player.sendMessage(clickableMessage, false);
        } else {
            ViewModel.Log("View Model is up to date!" + "\n View Model Version -> "+ getLatestVersionFromGitHub());
        }
        sent = true;
    }
}
