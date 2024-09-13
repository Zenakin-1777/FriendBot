package com.zenakin.friendbot.utils;

import com.zenakin.friendbot.config.FriendBotConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class DiscordWebhookUtils {

    public static void sendWebhookMessage(String content) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(FriendBotConfig.webhookURL);
            post.setHeader("Content-Type", "application/json");

            // Create the payload for Discord
            String jsonPayload = "{" +
                    "\"username\": \"" + FriendBotConfig.webhookUsername + "\"," +
                    "\"avatar_url\": \"" + FriendBotConfig.webhookAvatarURL + "\"," +
                    "\"embeds\": [{" +
                    "\"title\": \"" + FriendBotConfig.webhookTitle + "\"," +
                    "\"description\": \"" + FriendBotConfig.webhookDescription + "\"," +
                    "\"color\": " + FriendBotConfig.webhookColor +
                    "}]" +
                    "}";

            post.setEntity(new StringEntity(jsonPayload));

            // Execute the request
            client.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
