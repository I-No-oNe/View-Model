package net.i_no_am.viewmodel.client;

import io.github.itzispyder.improperui.util.ChatUtils;
import net.i_no_am.viewmodel.utils.Utils;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ModVersionChecker implements Global {

    public static boolean checked = false;
    private static final String REPO_URL = "https://api.github.com/repos/I-No-oNe/View-Model/releases/latest";

    public static void updateChecker() {
        if (!checked) {
            try {
                String latestVersion = getLatestVersion();
                if (latestVersion != null && Utils.isUpdateAvailable(latestVersion)) {
                    ChatUtils.sendMessage(PREFIX + "Download the new version of View Model from Modrinth!");
                    Text literal = Text.literal(  "§a https://modrinth.com/mod/no-ones-view-model");
                    ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/no-ones-view-model");
                    MutableText text = literal.copy();
                    Utils.sendText(text.fillStyle(text.getStyle().withClickEvent(event)));



                }
                checked = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static String getLatestVersion() throws Exception {
        URL url = new URL(REPO_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        conn.disconnect();

        String jsonString = sb.toString();

        String versionTag = "\"tag_name\":\"";
        int startIndex = jsonString.indexOf(versionTag);
        if (startIndex != -1) {
            int endIndex = jsonString.indexOf('"', startIndex + versionTag.length());
            if (endIndex != -1) {
                return jsonString.substring(startIndex + versionTag.length(), endIndex);
            }
        }

        return null;
    }

}
