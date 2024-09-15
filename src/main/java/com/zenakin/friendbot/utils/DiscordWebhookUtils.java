package com.zenakin.friendbot.utils;

import com.zenakin.friendbot.config.FriendBotConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Objects;

public class DiscordWebhookUtils {

    public static void sendWebhookMessage(String content) {
        if (Objects.equals(FriendBotConfig.webhookURL, "")) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("No webhook URL detected!"));
            return;
        }
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(FriendBotConfig.webhookURL);
            post.setHeader("Content-Type", "application/json");

            // Escape the content for JSON
            String escapedContent = content.replace("\"", "\\\"");

            // Create the payload for Discord
            String jsonPayload = "{" +
                    "\"content\": \"" + escapedContent + "\"" +
                    "}";

            post.setEntity(new StringEntity(jsonPayload));

            // Execute the request
            client.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}