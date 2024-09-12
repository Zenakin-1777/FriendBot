package com.zenakin.friendbot.config;

import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.*;
import com.zenakin.friendbot.FriendBot;
import com.zenakin.friendbot.config.pages.PageTBD;
import com.zenakin.friendbot.util.AudioManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.*;

public class FriendBotConfig extends Config {
    public static FriendBotConfig instance;
    public static Set<String> nameList = new LinkedHashSet<>();

    @Switch(
            name = "Main Toggle",
            description = "Enable/Dissable most features of the mod",
            size = OptionSize.DUAL
    )
    public static boolean isModEnabled = true;

    @Text(
            name = "Custom Message Contents",
            placeholder = "Paste your message here",
            multiline = true
    )
    public static String customMessage = "";

    @Page(
            name = "IGN list",
            location = PageLocation.TOP,
            description = "Page where you can edit the list of usernames"
    )
    public PageTBD pageTBD = new PageTBD();

    @Text(
            name = "IGN of the player",
            placeholder = "Paste their username here",
            size = OptionSize.SINGLE,
            category = "Name List"
    )
    public static String name = "";

    @Button(
            name = "List IGNs",
            description = "Display the list of currently stored IGNs",
            text = "LIST",
            category = "Name List"
    )
    public static void listNames() {
        for (String IGN : nameList) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(IGN));
        }
    }

    @Button(
            name = "Add IGN",
            description = "Add the IGN of the player to the list",
            text = "ADD",
            category = "Name List"
    )
    public static void addName() {
        // Play sound when adding a name
        // OLD --> AudioManager.getInstance().playSound("friendbot:notification_ping.ogg");
        // STILL NOT WORKING!!! --> AudioManager.playPingSound();
        //TODO: SOUND TESTING CAN BE DONE HERE

        // Only add the name if it is not already in the set
        if (!nameList.contains(name)) {
            nameList.add(name);  // Automatically prevents duplicates
            sortNameList();  // Sort the list alphabetically
        }
    }

    @Button(
            name = "Remove IGN",
            description = "Remove the IGN of the player from the list",
            text = "REMOVE",
            category = "Name List"
    )
    public static void removeName() {
        nameList.remove(name);  // Remove the name from the set
    }

    /* TODO: make a backup button that will save lists as a restore point
    @Button(
            name = "List IGNs",
            description = "Display the list of currently stored IGNs",
            text = "LIST"
    )
     */

    @Info(
            text = "THE FOLLOWING SETTINGS WILL ERASE ANY AND ALL NAMES FROM THE LIST!!!",
            size = OptionSize.DUAL,
            type = InfoType.ERROR,
            category = "Name List",
            subcategory = "CLEAR"
    )
    public static boolean separator;

    @Text(
            name = "CLEAR CONFIRMATION",
            placeholder = "Type 'Clear' before clicking the button to confirm..",
            size = OptionSize.DUAL,
            category = "Name List",
            subcategory = "CLEAR"
    )
    public static String clearCode = "";

    @Button(
            name = "Clear List",
            description = "CAUTION - WILL DELETE ALL NAMES IN THE LIST!!!",
            text = "CLEAR",
            category = "Name List",
            subcategory = "CLEAR"
    )
    public static void clearNameList() {
        if (!nameList.isEmpty() && Objects.equals(clearCode, "Clear") && toggleClearErase){
            nameList.clear();
            clearCode = "";
        } else if (!nameList.isEmpty() && Objects.equals(clearCode, "Clear") && !toggleClearErase){
            nameList.clear();
        }
    }

    @Switch(
            name = "Erase Clear Text",
            description = "Toggle erasing the clear confirmation text box",
            category = "Name List",
            subcategory = "CLEAR"
    )
    public static boolean toggleClearErase = true;

    // Method to sort the list alphabetically
    private static void sortNameList() {
        List<String> sortedList = new ArrayList<>(nameList);
        Collections.sort(sortedList);  // Sorts alphabetically
        nameList.clear();
        nameList.addAll(sortedList);  // Add the sorted names back to the set
    }

    public FriendBotConfig() {
        super(new Mod(FriendBot.NAME, ModType.UTIL_QOL, "/assets/friendbot/logo.png"), FriendBot.MODID + ".json");
        initialize();
    }
}
