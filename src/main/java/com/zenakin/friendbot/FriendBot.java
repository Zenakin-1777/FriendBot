package com.zenakin.friendbot;

import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.utils.Notifications;
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

import java.io.File;
import java.util.*;
import java.util.List;

@Mod(modid = FriendBot.MODID, name = FriendBot.NAME, version = FriendBot.VERSION)
public class FriendBot {

    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    @Mod.Instance
    public static FriendBot instance;
    public FriendBotConfig config;
    public static String lastPart;
    public static String lastSentMessage;
    public static String extractedName = "";
    public static String messagedPlayer = "";
    public static String customConfirmMessage = FriendBotConfig.customConfirmationMessage;
    public static boolean doneSendingMessage;

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
            if (FriendBotConfig.isModEnabled) {
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
                            notifyViaWebhook(mentionEveryone() + " FriendBot Update","Listed player detected!", "Sending messages to player: \n`" + messagedPlayer + "`",embedColor("webhookColorStart"));
                        }
                    }, FriendBotConfig.initialMessageDelay);
                }

                //DEBUGGIN: Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(lastPart));
                if (message.startsWith("To") && message.endsWith(lastPart)) {
                    Timer timer2 = new Timer();
                    Timer timer3 = new Timer();
                    AudioManager.playLoudSound("friendbot:notification_ping", FriendBotConfig.customVolume, FriendBotConfig.customPitch + 0.5f, Minecraft.getMinecraft().thePlayer.getPositionVector());
                    notifyViaWebhook(mentionEveryone() + " FriendBot Update","Finished sending messages", "Target player: `" + messagedPlayer + "`",embedColor("webhookColorDone"));

                    String formattedMessage2 = message.toLowerCase();
                    if (FriendBotConfig.toggleMustConfirm) {
                        doneSendingMessage = false;
                        timer3.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Minecraft.getMinecraft().thePlayer.sendChatMessage("/t " + messagedPlayer + " Please reply ('/r message' to reply) with '" + customConfirmMessage + "' to confirm that you have read the previous messages..");
                                lastSentMessage = "/t " + messagedPlayer + " Please reply ('/r message' to reply) with '" + customConfirmMessage + "' to confirm that you have read the previous messages..";
                                doneSendingMessage = true;
                            }
                        }, 800);
                    }
                    // DEBUGGING:
                    notifyViaWebhook("FriendBot Update","Debugging Info","Last Part of the split messages:\n'" + lastPart + "'\n\nLast sent message: \n'" + lastSentMessage + "'\n\nReceived reply in lower case: \n'" + formattedMessage2 + "'",embedColor(null));
                    if (!FriendBotConfig.toggleMustConfirm || formattedMessage2.contains(customConfirmMessage)) {
                        FriendBotConfig.removeNameExternally(messagedPlayer);
                        notifyViaOneConfig(messagedPlayer + " has been removed from list!!!", () -> { });
                        notifyViaWebhook(mentionEveryone() + " FriendBot Update","List Updated", "Player removed from list: \n`" + messagedPlayer + "`",embedColor("webhookColorRemovedFromList"));
                    }

                    timer2.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            messagedPlayer = "";
                        }
                    }, 300000);
                }
            /* TODO: Failed check triggering prematurely
            else if (!Objects.equals(messagedPlayer, "")) {
                DiscordWebhookUtils.sendWebhookMessage("@everyone ERROR: Didn't finish sending message to [" + messagedPlayer + "]..");
                messagedPlayer = "";
            }
            */

                String formattedMessage = message.toLowerCase();

                if (message.startsWith("From") && formattedMessage.contains(messagedPlayer) && !messagedPlayer.isEmpty()) {
                    AudioManager.playLoudSound("friendbot:notification_ping", FriendBotConfig.customVolume, FriendBotConfig.customPitch - 0.5f, Minecraft.getMinecraft().thePlayer.getPositionVector());
                    notifyViaWebhook(mentionEveryone() + " FriendBot Update","Someone Replied!!!", "Message: \n`" + message + "`",embedColor("webhookColorReply"));

                    if (FriendBotConfig.toggleReplies) {
                        while (!doneSendingMessage) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/t " + messagedPlayer + " " + FriendBotConfig.customReply);
                        lastSentMessage = "/t " + messagedPlayer + " " + FriendBotConfig.customReply;
                    }
                }
            }
        }

        public OneColor embedColor(String situation) {
            if (FriendBotConfig.webhookCustomColorsToggled) {
                if (Objects.equals(situation, "webhookColorRemovedFromList")) return FriendBotConfig.webhookColorRemovedFromList;
                else if (Objects.equals(situation, "webhookColorReply")) return FriendBotConfig.webhookColorReply;
                else if (Objects.equals(situation, "webhookColorDone")) return FriendBotConfig.webhookColorDone;
                else if (Objects.equals(situation, "webhookColorStart")) return FriendBotConfig.webhookColorStart;
                else return new OneColor(255,255,255);
            } else {
                if (Objects.equals(situation, "webhookColorRemovedFromList")) return new OneColor(64, 224, 208);
                else if (Objects.equals(situation, "webhookColorReply")) return new OneColor(200, 200, 0);
                else if (Objects.equals(situation, "webhookColorDone")) return new OneColor(206, 56, 216);
                else if (Objects.equals(situation, "webhookColorStart")) return new OneColor(0, 200, 0);
                else return new OneColor(255,255,255);
            }
        }

        public String mentionEveryone() {
            if (FriendBotConfig.mentionToggled) {
                return "@" + FriendBotConfig.mentionRoleName;
            } else {
                return "";
            }
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
            if (!Objects.equals(lastPart, messages.get(messages.size() - 1))) lastPart = messages.get(messages.size() - 1);

            Timer timer = new Timer();
            Timer timer1 = new Timer();
            doneSendingMessage = false;

            for (int i = 0; i < messages.size(); i++) {
                final String message = messages.get(i);
                // Schedule each message with a delay
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/t " + name1 + " " + message);
                        lastSentMessage = message;
                    }
                }, /*TODO: FUTURE FEATURE???(long) FriendBotConfig.ping + */((long) i * FriendBotConfig.timeBetweenMessages));
            }

            timer1.schedule(new TimerTask() {
                @Override
                public void run() {
                    doneSendingMessage = true;
                }
            }, ((long) messages.size() * FriendBotConfig.timeBetweenMessages));

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
                extractedName = message.substring(9, message.length() - 8).toLowerCase();
            }
        }

        public boolean isOnList(String nameOnList) {
            return FriendBotConfig.nameList.contains(nameOnList);
        }
    }
    public static void notifyViaOneConfig(String notificationContent, Runnable action) {
        File fileToOpen = new File(FriendBotConfig.localListPath);
        Notifications.INSTANCE.send(
                "FriendBot",
                notificationContent,
                null,
                5000,
                action);
    }
}
