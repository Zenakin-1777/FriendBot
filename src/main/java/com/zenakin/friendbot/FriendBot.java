package com.zenakin.friendbot;

import cc.polyfrost.oneconfig.config.core.OneColor;
import com.zenakin.friendbot.config.FriendBotConfig;
import com.zenakin.friendbot.utils.AudioManager;
import com.zenakin.friendbot.utils.DiscordWebhookUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.*;

@Mod(modid = FriendBot.MODID, name = FriendBot.NAME, version = FriendBot.VERSION)
public class FriendBot {

    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    @Mod.Instance
    public static FriendBot instance;
    public FriendBotConfig config;
    public static String lastPart;
    public static String extractedName = "";
    public static String messagedPlayer = "";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register event handler
        config = new FriendBotConfig();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ChatEventHandler());
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        // Server setup code
    }

    public static class ChatEventHandler {
        @SubscribeEvent(priority = EventPriority.HIGH)
        public void onClientChatReceived(ClientChatReceivedEvent event) {
            // Get the chat message
            String message = event.message.getUnformattedText();
            extractContent(message);

            if (!Objects.equals(extractedName, "") && isOnList(extractedName)) {
                messagedPlayer = extractedName;
                extractedName = "";
                AudioManager.playLoudSound("friendbot:notification_ping", FriendBotConfig.customVolume, FriendBotConfig.customPitch, Minecraft.getMinecraft().thePlayer.getPositionVector());
                // Schedule message sending with a delay
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        sendMessagesInChunks(messagedPlayer, FriendBotConfig.customMessage);
                        notifyViaWebhook("FriendBot Update","Listed player detected!", "Sending messages to player: `" + messagedPlayer + "`",FriendBotConfig.webhookColorStart);
                    }
                }, FriendBotConfig.initialMessageDelay);
            }

            //DEBUGGIN: Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(lastPart));
            if (message.startsWith("To") && message.endsWith(lastPart)) {
                AudioManager.playLoudSound("friendbot:notification_ping", FriendBotConfig.customVolume, FriendBotConfig.customPitch, Minecraft.getMinecraft().thePlayer.getPositionVector());
                notifyViaWebhook("@everyone FriendBot Update","Finished sending messages", "Target player: `" + messagedPlayer + "`",FriendBotConfig.webhookColorDone);
                FriendBotConfig.removeNameExternally(messagedPlayer);
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(messagedPlayer + " has been removed from list!!!"));
                messagedPlayer = "";
            }
            /* TODO: Failed check triggering prematurely
            else if (!Objects.equals(messagedPlayer, "")) {
                DiscordWebhookUtils.sendWebhookMessage("@everyone ERROR: Didn't finish sending message to [" + messagedPlayer + "]..");
                messagedPlayer = "";
            }
            */
        }

        public void notifyViaWebhook(String content, String embedTittle, String embedDescription, OneColor embedColor) {
            DiscordWebhookUtils.sendWebhookMessage(
                    content,
                    FriendBotConfig.webhookURL,
                    FriendBotConfig.webhookUsername,
                    FriendBotConfig.webhookAvatarURL,
                    embedTittle,
                    embedDescription,
                    embedColor
            );
        }

        private void sendMessagesInChunks(String name1, String fullMessage) {
            List<String> messages = splitMessage(fullMessage, FriendBotConfig.messageLength);
            lastPart = messages.get(messages.size() - 1);

            Timer timer = new Timer();
            for (int i = 0; i < messages.size(); i++) {
                final String message = messages.get(i);
                // Schedule each message with a delay
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/t " + name1 + " " + message);
                    }
                }, (long) FriendBotConfig.ping + ((long) i * FriendBotConfig.timeBetweenMessages));
            }
        }

        private List<String> splitMessage(String message, int maxLength) {
            List<String> parts = new ArrayList<>();
            String[] words = message.split(" "); // Split message into words
            StringBuilder currentPart = new StringBuilder();

            for (String word : words) {
                // Check if adding this word and a space would exceed the max length
                if (currentPart.length() + word.length() + 1 > maxLength) {
                    // Add the current part to the list and start a new part
                    parts.add(currentPart.toString().trim());
                    currentPart = new StringBuilder();
                }
                // Add the word to the current part
                if (currentPart.length() > 0) {
                    currentPart.append(" "); // Add a space before adding the word
                }
                currentPart.append(word);
            }
            // Add the last part if it's not empty
            if (currentPart.length() > 0) {
                parts.add(currentPart.toString().trim());
            }
            return parts;
        }

        public void extractContent(String message) {
            if (message.startsWith("Friend > ") && message.endsWith(" joined.")) {
                extractedName = message.substring(9, message.length() - 8);
            }
        }

        public boolean isOnList(String nameOnList) {
            return FriendBotConfig.nameList.contains(nameOnList);
        }
    }


}
