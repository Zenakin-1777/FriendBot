package com.zenakin.friendbot.config;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.OptionSize;

public class FriendBotConfig {

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
}
