package com.zenakin.friendbot;

import com.zenakin.friendbot.config.FriendBotConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

@Mod(modid = com.zenakin.friendbot.FriendBot.MODID, name = com.zenakin.friendbot.FriendBot.NAME, version = com.zenakin.friendbot.FriendBot.VERSION)
public class FriendBot {

    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    @Mod.Instance
    public static FriendBot instance;
    public FriendBotConfig config;

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

            String name = extractName(message);

            // DEBUGGING: Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Name: " + name + " | From: " + message));

            if (name != null && isOnList(name)) {
                MinecraftServer server = MinecraftServer.getServer();
                if (server != null) {
                    // DEBUGGING: Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Attempting command: " + "/t " + name + " " + FriendBotConfig.customMessage + ".."));
                    MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "/t " + name + " " + FriendBotConfig.customMessage);
                }
            }
        }

        public String extractName(String message) {
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
