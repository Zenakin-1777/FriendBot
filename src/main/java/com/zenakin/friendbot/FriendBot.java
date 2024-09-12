package com.zenakin.friendbot;

import com.zenakin.friendbot.config.FriendBotConfig;
import com.zenakin.friendbot.utils.AudioManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;

@Mod(modid = com.zenakin.friendbot.FriendBot.MODID, name = com.zenakin.friendbot.FriendBot.NAME, version = com.zenakin.friendbot.FriendBot.VERSION)
public class FriendBot {

    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    @Mod.Instance
    public static FriendBot instance;
    public FriendBotConfig config;
    public static String lastPart;

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
            String name = extractContent(message);

            if (name != null && isOnList(name)) {
                AudioManager.playLoudSound("friendbot:notification_ping", FriendBotConfig.customVolume, FriendBotConfig.customPitch, Minecraft.getMinecraft().thePlayer.getPositionVector());
                // Schedule message sending with a delay
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        sendMessagesInChunks(name, FriendBotConfig.customMessage);
                    }
                }, FriendBotConfig.initialMessageDelay);
            }

            //DEBUGGIN: Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(lastPart));
            if (message.startsWith("To") && message.endsWith(lastPart)) {
                FriendBotConfig.nameList.remove(name);
                AudioManager.playLoudSound("friendbot:notification_ping", FriendBotConfig.customVolume, FriendBotConfig.customPitch, Minecraft.getMinecraft().thePlayer.getPositionVector());
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(name + " has been removed from list!!!"));
            }
        }

        private void sendMessagesInChunks(String name, String fullMessage) {
            List<String> messages = splitMessage(fullMessage, FriendBotConfig.messageLength);
            lastPart = messages.get(messages.size() - 1);

            Timer timer = new Timer();
            for (int i = 0; i < messages.size(); i++) {
                final String message = messages.get(i);
                // Schedule each message with a delay
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/t " + name + " " + message);
                    }
                }, (long) i * FriendBotConfig.timeBetweenMessages);
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

        public String extractContent(String message) {
            if (message.startsWith("Friend > ") && message.endsWith(" joined.")) {
                return message.substring(9, message.length() - 8);
            } else {
                return null;
            }
        }

        public boolean isOnList(String name) {
            return FriendBotConfig.nameList.contains(name);
        }
    }
}
