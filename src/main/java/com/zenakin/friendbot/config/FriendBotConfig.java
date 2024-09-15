package com.zenakin.friendbot.config;

import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.*;
import com.zenakin.friendbot.FriendBot;
import com.zenakin.friendbot.config.pages.PageTBD;
import com.zenakin.friendbot.utils.AudioManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.*;

public class FriendBotConfig extends Config {
    public static FriendBotConfig instance;
    public static List<String> nameList;

    @Switch(
            name = "Main Toggle",
            description = "Enable/Dissable most features of the mod",
            size = OptionSize.DUAL,
            category = "General"
    )
    public static boolean isModEnabled = true;

    @Number(
            name = "Ping",
            description = "Your ping in milliseconds",
            min = 5,
            max = 5000,
            step = 10,
            category = "General"
    )
    public static int ping = 100;

    @Slider(
            name = "Sound Volume",
            description = "The volume of the sounds the mod will play",
            min = 0,
            max = 1.0f,
            category = "General",
            subcategory = "Sounds"
    )
    public static float customVolume = 1.0f;

    @Slider(
            name = "Sound Pitch",
            description = "The pitch of the sounds the mod will play",
            min = 0,
            max = 2.0f,
            category = "General",
            subcategory = "Sounds"
    )
    public static float customPitch = 1.0f;

    @Button(
            name = "Test Sound",
            description = "Play the sound",
            text = "PLAY",
            subcategory = "Sounds",
            size = OptionSize.DUAL
    )
    public static void playSound() {
        AudioManager.playLoudSound("friendbot:notification_ping", customVolume, customPitch, Minecraft.getMinecraft().thePlayer.getPositionVector());
    }

    @Info(
            text = "You must first create your own Discord server and webhook before changing these settings.",
            type = InfoType.INFO,
            size = 2,
            category = "Discord Integration"
    )
    public static boolean webhookInfoLine;

    @Text(
            name = "Webhook URL",
            placeholder = "https://discord.com/api/webhooks/...",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static String webhookURL = "";

    @Text(
            name = "Username",
            placeholder = "FriendBot",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static String webhookUsername = "FriendBot";

    @Text(
            name = "Avatar URL",
            placeholder = "https://image.site.com/assets/img/icon.png",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static String webhookAvatarURL = "https://freobot.vercel.app/assets/img/freo.png";

    @Text(
            name = "Title",
            placeholder = "Cool Bot 420",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static String webhookTitle = "FriendBot Updates";

    @Text(
            name = "Description",
            placeholder = "Info on FriendBot",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static String webhookDescription = "Update on FriendBot's status";

    @Color(
            name = "Color",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static OneColor webhookColor = new OneColor(206, 56, 216);

    @Text(
            name = "Custom Message Contents",
            placeholder = "Paste your message here",
            multiline = true,
            category = "Message Settings"
    )
    public static String customMessage = "";

    @Info(
            text = "The timing settings are all in milliseconds. (More units will be added soon)",
            type = InfoType.INFO,
            size = OptionSize.DUAL,
            category = "Message Settings",
            subcategory = "Message Timing"
    )
    public static boolean timingInfo;

    /* FUTURE FEATURE
    @Dropdown(
            name = "Time Unit",
            description = "Unit used between milliseconds, seconds and minutes",
            options = {"Milliseconds", "Seconds", "Minutes", "Hours"},
            category = "Message Settings",
            subcategory = "Message Timing"
    )
    public static int timeUnit = 1;
     */

    @Number(
            name = "Initial Delay",
            description = "The delay before starting to send messages (max 1hr)",
            min = 1000,
            max = 3600000,
            step = 1000,
            category = "Message Settings",
            subcategory = "Message Timing"
    )
    public static int initialMessageDelay = 4000;

    @Number(
            name = "Subsequent Message Delay",
            description = "The time to wait before sending the next part of a message. (if the message is longer than the set Message Length",
            min = 1000,
            max = 10000,
            step = 500,
            category = "Message Settings",
            subcategory = "Message Timing"
    )
    public static int timeBetweenMessages = 3500;

    @Slider(
            name = "Message Length",
            description = "The number of characters each message can have. (depending on the length of the person's name, length may vary, but on average, there is around an 80 character limit",
            min = 5,
            max = 200,
            step = 5,
            category = "Message Settings",
            subcategory = "Message Timing"
    )
    public static int messageLength = 80;

    @Text(
            name = "IGN of the player",
            placeholder = "Put a username here",
            category = "Name List",
            subcategory = "EDIT"
    )
    public static String name = "";

    @Button(
            name = "List IGNs",
            description = "Display the list of currently stored IGNs in chat",
            text = "LIST",
            category = "Name List",
            subcategory = "EDIT",
            size = 1
    )
    public static void listNames() {
        for (String IGN : nameList) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(IGN));
        }
    }

    @Button(
            name = "Remove IGN",
            description = "Remove the IGN of the player from the list",
            text = "REMOVE",
            category = "Name List",
            subcategory = "EDIT",
            size = 1
    )
    public static void removeName() {
        nameList.remove(name);
    }

    public static void removeNameExternally(String externalName) {
        nameList.remove(externalName);
    }

    @Button(
            name = "Add IGN",
            description = "Add the IGN of the player to the list",
            text = "ADD",
            category = "Name List",
            subcategory = "EDIT",
            size = 1
    )
    public static void addName() {
        // Play sound when adding a name
        // OLD --> AudioManager.getInstance().playSound("friendbot:notification_ping.ogg");
        // STILL NOT WORKING!!! --> AudioManager.playPingSound();
        // This one worked: AudioManager.playLoudSound("friendbot:notification_ping", customVolume, customPitch, Minecraft.getMinecraft().thePlayer.getPositionVector());

        // Only add the name if it is not already in the set
        if (!nameList.contains(name)) {
            nameList.add(name);  // Automatically prevents duplicates
            sortNameList();  // Sort the list alphabetically
        }
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

    @Text(
            name = "CLEAR CONFIRMATION",
            placeholder = "Type 'Clear' to confirm..",
            category = "Name List",
            subcategory = "CLEAR"
    )
    public static String clearCode = "";

    @Switch(
            name = "Erase Clear Text",
            description = "Toggle erasing the clear confirmation text box",
            category = "Name List",
            subcategory = "CLEAR"
    )
    public static boolean toggleClearErase = true;

    @Page(
            name = "Page TBD",
            location = PageLocation.BOTTOM,
            description = "A page that has yet to be used for anything..",
            category = "General",
            subcategory = "Pages"
    )
    public PageTBD pageTBD = new PageTBD();

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
