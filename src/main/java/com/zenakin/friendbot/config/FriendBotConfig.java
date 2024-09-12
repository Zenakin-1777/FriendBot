package com.zenakin.friendbot.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import com.zenakin.friendbot.FriendBot;
import com.zenakin.friendbot.config.pages.NameListPage;

public class FriendBotConfig extends Config {
    public static FriendBotConfig instance;

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
    public NameListPage nameListPage = new NameListPage();

    public FriendBotConfig() {
        super(new Mod(FriendBot.NAME, ModType.UTIL_QOL, "/assets.friendbot/logo.png"), FriendBot.MODID + ".json");
        initialize();
    }
}
