package com.zenakin.friendbot.config;

import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Color;
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

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class FriendBotConfig extends Config {
    public static FriendBotConfig instance;
    public static List<String> nameList;

    @Info(
            text = "BACKING UP MORE THAN ONCE TO THE SAME DIRECTORY(PATH) WILL OVERWRITE ANY PREVIOUS BACKUPS!!!",
            type = InfoType.WARNING,
            size = 2
    )
    public static boolean backupWarning;

    /*TODO: not implemented yet...
    @Switch(
            name = "Main Toggle",
            description = "Enable/Dissable most features of the mod",
            size = OptionSize.DUAL,
            category = "General"
    )
    public static boolean isModEnabled = true;
     */

    /* TODO: FUTURE FEATURE??
    @Number(
            name = "Ping",
            description = "Your ping in milliseconds",
            min = 5,
            max = 5000,
            step = 10,
            category = "General"
    )
    public static int ping = 100;
     */

    @Text(
            name = "Backup Path",
            description = "DEFAULT: OneConfig\\ConfigBackup | EX: ConfigBackup | EX2: C:\\Users\\User\\Downloads\\ConfigBackup",
            placeholder = "Paste the path here (no extension)",
            category = "General"
    )
    public static String localBackupPath = "OneConfig\\ConfigBackup" + ".txt";

    @Button(
            name = "Backup Config",
            description = "Save the current settings to another file",
            text = "BACKUP",
            category = "General"
    )
    public static void backupConfig() {
        backupConfigToFile(localBackupPath);
    }

    /* TODO: make this work
    @Button(
            name = "Restore Config",
            description = "Restore settings to the last backed-up state",
            text = "RESTORE",
            category = "General"
    )
    public static void restoreConfig() {
        restoreConfigFromFile(localBackupPath);
    }
     */

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

    @Color(
            name = "Start Color",
            description = "Color of the embed when messages start sending",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static OneColor webhookColorStart = new OneColor(206, 56, 216);

    @Color(
            name = "Done Color",
            description = "Color of the embed when all messages have been sent",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static OneColor webhookColorDone = new OneColor(206, 56, 216);

    @Color(
            name = "Reply Color",
            description = "Color of the embed when someone replies",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static OneColor webhookColorElse = new OneColor(200, 200, 0);

    /* TODO: FUTURE FEATURE
    @Color(
            name = "Failed Color",
            description = "Color of the embed when messages fail to fully send",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static OneColor webhookColorFailed = new OneColor(200, 0, 0);
     */

    @Switch(
            name = "Mention Someone",
            description = "Mention a role or individual whenever the webhook is sent",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static boolean mentionToggled = false;

    @Text(
            name = "Mention Role Name",
            description = "The role or person to be @mentioned. You don't need to add the @",
            placeholder = "everyone",
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static String mentionRoleName = "everyone";

    @Button(
            name = "Reset",
            text = "RESET",
            description = "Return webhook settings to defaul values",
            size = 2,
            category = "Discord Integration",
            subcategory = "Webhook"
    )
    public static void resetWebhookSettings() {
        mentionToggled = false;
        mentionRoleName = "everyone";
        webhookUsername = "FriendBot";
        webhookAvatarURL = "https://freobot.vercel.app/assets/img/freo.png";
        webhookColorStart = new OneColor(0, 200, 0);
        webhookColorDone = new OneColor(206, 56, 216);
        webhookColorElse = new OneColor(200, 200, 0);
        //TODO: FUTURE FEATURE
        // webhookColorFailed = new OneColor(200, 0, 0);
    }

    @Info(
            text = "For better reply features when playing Hypixel, use the ChatTriggers module 'OurTools' which has an AFK feature called 'OurAFK' that is much better than this single reply. 'OurAFK' can even autojoin parties, send a custom AFK message in party chat, and leave.",
            type = InfoType.INFO,
            size = 2,
            category = "Message Settings"
    )
    public static boolean replyInfo;

    @Text(
            name = "Custom Message",
            placeholder = "Paste your message here",
            multiline = true,
            category = "Message Settings"
    )
    public static String customMessage = "";

    @Text(
            name = "Custom Reply",
            placeholder = "Paste your reply here",
            multiline = true,
            category = "Message Settings"
    )
    public static String customReply = "Sorry, can't talk rn. Plz reach out via Discord";

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

    @Text(
            name = "File Path",
            description = "DEFAULT: OneConfig\\ListBackup | EX: ListBackup | EX2: C:\\Users\\User\\Downloads\\ListBackup",
            placeholder = "Paste the path here (no extension)",
            size = OptionSize.DUAL,
            category = "Name List",
            subcategory = "Backup/Restore"
    )
    public static String localListPath = "OneConfig\\ListBackup" + ".txt";


    @Button(
            name = "Save IGN List",
            description = "Save the current list of usernames to a local file",
            text = "BACKUP",
            category = "Name List",
            subcategory = "Backup/Restore"
    )
    public static void saveList() {
        saveListToFile(nameList, localListPath);
    }

    @Button(
            name = "Load IGN List",
            description = "Import the list of usernames from the local file. (if it has been backed up before)",
            text = "RESTORE",
            category = "Name List",
            subcategory = "Backup/Restore"
    )
    public static void loadList() {
        loadListFromFile(nameList, localListPath);
    }

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
            name = "Add IGN",
            description = "Add the IGN of the player to the list",
            text = "ADD",
            category = "Name List",
            subcategory = "EDIT",
            size = 2
    )
    public static void addName() {
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
            category = "Name List",
            subcategory = "EDIT",
            size = 2
    )
    public static void removeName() {
        nameList.remove(name);
    }

    public static void removeNameExternally(String externalName) {
        nameList.remove(externalName);
    }

    @Info(
            text = "THE FOLLOWING SETTINGS WILL ERASE ANY AND ALL NAMES FROM THE LIST!!! BACKING UP THE LIST BEFOREHAND IS RECOMMENDED",
            size = OptionSize.DUAL,
            type = InfoType.WARNING,
            category = "Name List",
            subcategory = "CLEAR"
    )
    public static boolean separator;

    @Switch(
            name = "Erase Clear Text",
            description = "Toggle erasing the clear confirmation text box after clicking the clear button",
            category = "Name List",
            subcategory = "CLEAR"
    )
    public static boolean toggleClearErase = true;

    @Text(
            name = "CLEAR CONFIRMATION",
            placeholder = "Type 'Clear' to confirm..",
            category = "Name List",
            subcategory = "CLEAR"
    )
    public static String clearCode = "";

    @Button(
            name = "Clear List",
            description = "CAUTION - WILL DELETE ALL NAMES IN THE LIST!!!",
            text = "CLEAR",
            size = 2,
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

    /* TODO: FUTURE FEATURE?
    @Page(
            name = "Page TBD",
            location = PageLocation.BOTTOM,
            description = "A page that has yet to be used for anything..",
            category = "General",
            subcategory = "Pages"
    )
    public PageTBD pageTBD = new PageTBD();
     */

    public static void loadListFromFile(List<String> list, String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!list.contains(line)) {
                        list.add(line);
                        sortNameList();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle exceptions
            }
        } else {
            FriendBot.notifyViaOneConfig(
                    "File not found: " + filePath,
                    () -> { });
        }
    }

    public static void saveListToFile(List<String> list, String filepath) {
        File filepath2 = new File(filepath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (String item : list) {
                writer.write(item);
                writer.newLine(); // Add a new line after each item
            }
            String notificationContent = "List has been saved to " + filepath + ". Click to open";
            FriendBot.notifyViaOneConfig(
                    notificationContent,
                    () -> {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().open(filepath2);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void backupConfigToFile(String exportPath) {
        File filepath3 = new File("OneConfig\\profiles\\Default Profile\\friendbot.json");
        File filepath2 = new File(exportPath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath3));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filepath2))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            String notificationContent = "Config has been saved to " + filepath2 + ". Click to open";
            FriendBot.notifyViaOneConfig(
                    notificationContent,
                    () -> {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().open(filepath2);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void restoreConfigFromFile(String exportPath) {
        File filepath2 = new File("OneConfig\\profiles\\Default Profile\\friendbot.json");
        File filepath3 = new File(exportPath);

        if (filepath3.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filepath3));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(filepath2))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }

                String notificationContent = "Config has been restored";
                FriendBot.notifyViaOneConfig(notificationContent, () -> { });
            } catch (IOException e) {
                e.printStackTrace(); // Handle exceptions
            }
        } else {
            FriendBot.notifyViaOneConfig("File not found: " + exportPath, () -> { });
        }
    }

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
