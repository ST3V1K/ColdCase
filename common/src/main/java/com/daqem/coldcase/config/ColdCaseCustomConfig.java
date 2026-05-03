package com.daqem.coldcase.config;

import com.daqem.coldcase.ColdCase;
import com.supermartijn642.configlib.api.ConfigBuilders;
import com.supermartijn642.configlib.api.IConfigBuilder;

import java.util.function.Supplier;

public class ColdCaseCustomConfig {

    public static void init() {
    }

    public static final Supplier<Double> timeRevealChance;
    public static final Supplier<Double> userRevealChance;
    public static final Supplier<Double> partialUserRevealChance;
    public static final Supplier<Double> skinColorRevealChance;
    public static final Supplier<Double> toolRevealChance;
    public static final Supplier<Double> timeInaccuracy;

    public static final Supplier<Double> cleanworkHelmetHideChance;
    public static final Supplier<Double> cleanworkChestplateHideChance;
    public static final Supplier<Double> cleanworkPantsHideChance;
    public static final Supplier<Double> cleanworkBootsHideChance;

    public static final Supplier<String> timeFormatYears;
    public static final Supplier<String> timeFormatDays;
    public static final Supplier<String> timeFormatHours;
    public static final Supplier<String> timeFormatMinutes;
    public static final Supplier<String> timeFormatSeconds;

    public static final Supplier<String> clueNotFound;
    public static final Supplier<String> clueFoundHeader;
    public static final Supplier<String> clueTime;
    public static final Supplier<String> clueTimeUnknown;
    public static final Supplier<String> clueUser;
    public static final Supplier<String> clueUserStartWith;
    public static final Supplier<String> clueUserUnknown;
    public static final Supplier<String> clueSkinColor;
    public static final Supplier<String> clueTool;
    public static final Supplier<String> clueUnknown;
    public static final Supplier<String> clueHardToTell;


    static {
        IConfigBuilder config = ConfigBuilders.newTomlConfig(ColdCase.MOD_ID, "coldcase-custom", true);

        config.push("evidence");
        timeRevealChance = config.comment("The chance to reveal the time of an action.")
                .onlyOnServer()
                .define("timeRevealChance", 0.8, 0.0, 1.0);
        userRevealChance = config.comment("The chance to reveal the user who performed an action.")
                .onlyOnServer()
                .define("userRevealChance", 0.8, 0.0, 1.0);
        partialUserRevealChance = config.comment("The chance to reveal a partial username (e.g. the first letter).")
                .onlyOnServer()
                .define("partialUserRevealChance", 0.8, 0.0, 1.0);
        skinColorRevealChance = config.comment("The chance to reveal the skin color of the user.")
                .onlyOnServer()
                .define("skinColorRevealChance", 0.8, 0.0, 1.0);
        toolRevealChance = config.comment("The chance to reveal the tool used to perform an action.")
                .onlyOnServer()
                .define("toolRevealChance", 0.8, 0.0, 1.0);
        timeInaccuracy = config.comment("The maximum inaccuracy of the time of an action (e.g. 0.1 for 10% inaccuracy).")
                .onlyOnServer()
                .define("timeInaccuracy", 0.01, 0.0, 1.0);
        config.pop();

        config.push("cleanwork");
        cleanworkHelmetHideChance = config.comment("The chance for a Cleanwork helmet to hide evidence.")
                .onlyOnServer()
                .define("cleanworkHelmetHideChance", 0.15, 0.0, 1.0);
        cleanworkChestplateHideChance = config.comment("The chance for a Cleanwork chestplate to hide evidence.")
                .onlyOnServer()
                .define("cleanworkChestplateHideChance", 0.15, 0.0, 1.0);
        cleanworkPantsHideChance = config.comment("The chance for a Cleanwork pants to hide evidence.")
                .onlyOnServer()
                .define("cleanworkPantsHideChance", 0.15, 0.0, 1.0);
        cleanworkBootsHideChance = config.comment("The chance for a Cleanwork boots to hide evidence.")
                .onlyOnServer()
                .define("cleanworkBootsHideChance", 0.15, 0.0, 1.0);
        config.pop();

        config.push("messages");
        timeFormatYears = config.comment("The format for the time in messages when the largest unit is years. Placeholders: {years}, {days}")
                .onlyOnServer()
                .define("timeFormatYears", "{years} years, {days} days", 1, 100);
        timeFormatDays = config.comment("The format for the time in messages when the largest unit is days. Placeholders: {days}, {hours}")
                .onlyOnServer()
                .define("timeFormatDays", "{days} days, {hours} hours", 1, 100);
        timeFormatHours = config.comment("The format for the time in messages when the largest unit is hours. Placeholders: {hours}, {minutes}")
                .onlyOnServer()
                .define("timeFormatHours", "{hours} hours, {minutes} minutes", 1, 100);
        timeFormatMinutes = config.comment("The format for the time in messages when the largest unit is minutes. Placeholders: {minutes}, {seconds}")
                .onlyOnServer()
                .define("timeFormatMinutes", "{minutes} minutes, {seconds} seconds", 1, 100);
        timeFormatSeconds = config.comment("The format for the time in messages when the largest unit is seconds. Placeholders: {seconds}")
                .onlyOnServer()
                .define("timeFormatSeconds", "{seconds} seconds", 1, 100);

        clueNotFound = config.comment("Message when no clues are found.")
                .onlyOnServer()
                .define("clueNotFound", "No clues found.", 1, 100);
        clueFoundHeader = config.comment("Header for when a clue is found.")
                .onlyOnServer()
                .define("clueFoundHeader", "§eYou found a clue!", 1, 100);
        clueTime = config.comment("Message for the time of the clue. Placeholder: {time}")
                .onlyOnServer()
                .define("clueTime", "§aHappened around {time} ago.", 1, 100);
        clueTimeUnknown = config.comment("Message for when the time of the clue is unknown.")
                .onlyOnServer()
                .define("clueTimeUnknown", "§aIt is unknown when this happened.", 1, 100);
        clueUser = config.comment("Message for the user of the clue. Placeholder: {user}")
                .onlyOnServer()
                .define("clueUser", "§aThe culprit was {user}.", 1, 100);
        clueUserStartWith = config.comment("Message for when the user of the clue is partially revealed. Placeholder: {user}")
                .onlyOnServer()
                .define("clueUserStartWith", "§aThe culprit's name starts with '{user}'.", 1, 100);
        clueUserUnknown = config.comment("Message for when the user of the clue is unknown.")
                .onlyOnServer()
                .define("clueUserUnknown", "§aThe culprit is unknown.", 1, 100);
        clueSkinColor = config.comment("Message for the skin color of the user. Placeholder: {skin_color}")
                .onlyOnServer()
                .define("clueSkinColor", "§aTheir skin color is {skin_color}.", 1, 100);
        clueTool = config.comment("Message for the tool used. Placeholder: {tool}")
                .onlyOnServer()
                .define("clueTool", "§aThe tool used was {tool}.", 1, 100);
        clueUnknown = config.comment("Message for when a clue is unknown.")
                .onlyOnServer()
                .define("clueUnknown", "unknown", 1, 100);
        clueHardToTell = config.comment("Message for when a clue is hard to tell.")
                .onlyOnServer()
                .define("clueHardToTell", "hard to tell", 1, 100);
        config.pop();

        config.build();
    }
}
