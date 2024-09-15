package com.zenakin.friendbot.utils;

import com.zenakin.friendbot.config.FriendBotConfig;
import cc.polyfrost.oneconfig.config.core.OneColor; // Import OneColor class
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.Objects;

public class DiscordWebhookUtils {

    public static void sendWebhookMessage(
            String content,
            String webhookURL,
            String webhookUsername,
            String webhookAvatarURL,
            String embedTitle,
            String embedDescription,
            OneColor embedColor
    ) {
        if (Objects.equals(webhookURL, "")) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("No webhook URL detected!"));
            return;
        }
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(webhookURL);
            post.setHeader("Content-Type", "application/json");

            // Escape the content for JSON
            String escapedContent = escapeJson(content);

            // Convert OneColor to hex and then to an integer
            int colorInt = embedColor.getRGB() & 0xFFFFFF; // Get the RGB value and ignore alpha

            // Create the payload for Discord
            String jsonPayload = "{" +
                    "\"username\": \"" + escapeJson(webhookUsername) + "\"," +
                    "\"avatar_url\": \"" + escapeJson(webhookAvatarURL) + "\"," +
                    "\"content\": \"" + escapedContent + "\"," +
                    "\"embeds\": [{" +
                    "\"title\": \"" + escapeJson(embedTitle) + "\"," +
                    "\"description\": \"" + escapeJson(embedDescription) + "\"," +
                    "\"color\": " + colorInt +
                    "}]" +
                    "}";

            post.setEntity(new StringEntity(jsonPayload));

            // Execute the request and get the response
            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                System.out.println("Webhook sent successfully!");
            } else {
                System.err.println("Failed to send webhook. Status code: " + statusCode);
                System.err.println("Response: " + EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String escapeJson(String value) {
        return value.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}