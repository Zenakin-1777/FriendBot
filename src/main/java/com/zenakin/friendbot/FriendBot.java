package com.zenakin.friendbot;

import com.zenakin.friendbot.config.FriendBotConfig;
import com.zenakin.friendbot.config.pages.NameListPage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.EventBus;

@Mod(modid = com.zenakin.friendbot.FriendBot.MODID, name = com.zenakin.friendbot.FriendBot.NAME, version = com.zenakin.friendbot.FriendBot.VERSION)
public class FriendBot {

    // Sets the variables from `gradle.properties`. See the `blossom` config in `build.gradle.kts`.
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Some common setup code
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register event handler
        MinecraftForge.EVENT_BUS.register(new ChatEventHandler());
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        // Server setup code
    }

    public static class ChatEventHandler {

        @SubscribeEvent
        public void onServerChat(ServerChatEvent event) {
            String message = event.message; // Get the chat message as String

            String name = extractName(message);

            if (name == null || !isOnList(name)) {
                //TODO: logic to abort safely
            } else {
                // Send a response to chat
                event.player.addChatMessage(new ChatComponentText("/t " + name + FriendBotConfig.customMessage));
            }
        }

        public String extractName(String message) {
            if (message.startsWith("Friend ") && (message.endsWith(" joined") || message.endsWith(" left"))) {
                String name = message.substring(7, message.length() - 6);
                return name;
            } else {
                return null;
            }
        }

        public boolean isOnList(String name) {
            if (NameListPage.nameList.contains(name)){
                return  true;
            }
            else return false;
        }
    }
}
