package com.zenakin.friendbot.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;
import java.util.Queue;

public class ChatUtils {

    private static final long MESSAGE_DELAY = 300L; // Delay in milliseconds

    private static SimpleTimeMark lastMessageSent = SimpleTimeMark.farPast();
    private static final Queue<String> sendQueue = new LinkedList<>();

    private static boolean internalChat(String message) {
        return chat(new ChatComponentText(message));
    }

    public static boolean chat(IChatComponent message) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null || minecraft.thePlayer == null) {
            return false;
        }

        minecraft.thePlayer.addChatMessage(message);
        return true;
    }

    public static void sendMessageToServer(String message) {
        sendQueue.add(message);
    }

    @SubscribeEvent
    public void onTick(LorenzTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null) {
            sendQueue.clear();
            return;
        }

        if (lastMessageSent.passedSince() > MESSAGE_DELAY) {
            String message = sendQueue.poll();
            if (message != null) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
                lastMessageSent = SimpleTimeMark.now();
            }
        }
    }
}
