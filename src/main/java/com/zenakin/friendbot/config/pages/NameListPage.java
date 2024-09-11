package com.zenakin.friendbot.config.pages;

import java.util.ArrayList;
import java.util.List;

import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import com.zenakin.friendbot.util.AudioManager;

public class NameListPage {
    public static List<String> nameList = new ArrayList<>();

    @Text(
            name = "IGN of the player",
            placeholder = "Paste their username here",
            size = OptionSize.SINGLE
    )
    public static String name = "";

    @Button(
            name = "Add IGN",
            description = "Add the IGN of the player to the list",
            text = "ADD"
    )
    public static void addName() {
        AudioManager.getInstance().playSound("/assets/friendbot/sounds/notification_ping.ogg");
        nameList.add(name);
    }

    @Button(
            name = "Remove IGN",
            description = "Remove the IGN of the player from the list",
            text = "REMOVE"
    )
    public static void removeName(String name) {
        nameList.remove(name);
    }

    @Button(
            name = "List IGNs",
            description = "Display the list of currently stored IGNs",
            text = "LIST"
    )
    public static void listNames() {
        for (String IGN : nameList) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(IGN));
        }
    }

}
