package com.daqem.coldcase.config;

import com.daqem.coldcase.ColdCase;
import com.supermartijn642.configlib.api.ConfigBuilders;
import com.supermartijn642.configlib.api.IConfigBuilder;

import java.util.function.Supplier;

public class ColdCaseCustomConfig {

    public static void init() {
    }

    public static final Supplier<Integer> damageLogWindow;
    public static final Supplier<Integer> injuriesToShowDecay;
    public static final Supplier<Integer> suspectNameMaxTime;
    public static final Supplier<Integer> suspectNamePartialTime;
    public static final Supplier<Integer> suspectSkinMaxTime;
    public static final Supplier<Integer> suspectSkinRevealTime;
    public static final Supplier<Integer> weaponNameMaxTime;

    public static final Supplier<Double> timeRevealChance;
    public static final Supplier<Double> userRevealChance;
    public static final Supplier<Double> userNameLetterRevealChance;
    public static final Supplier<Double> skinColorRevealChance;
    public static final Supplier<Double> toolRevealChance;
    public static final Supplier<Double> clueBaseRevealChance;
    public static final Supplier<Double> itemRevealFalloff;
    public static final Supplier<Integer> itemRevealFullChanceTime;
    public static final Supplier<Integer> itemRevealZeroChanceTime;
    public static final Supplier<Double> timeInaccuracy;

    public static final Supplier<Double> minItemTransactionFactor;
    public static final Supplier<Double> maxItemTransactionFactor;
    public static final Supplier<Integer> minItemsForFactor;
    public static final Supplier<Integer> maxItemsForFactor;

    public static final Supplier<Double> cleanworkHelmetHideChance;
    public static final Supplier<Double> cleanworkChestplateHideChance;
    public static final Supplier<Double> cleanworkPantsHideChance;
    public static final Supplier<Double> cleanworkBootsHideChance;

    public static final Supplier<Integer> identityTheftDuration;

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
    public static final Supplier<String> clueUsernameContainsLetter;
    public static final Supplier<String> clueUserUnknown;
    public static final Supplier<String> clueSkinColor;
    public static final Supplier<String> clueTool;
    public static final Supplier<String> clueBlockAction;
    public static final Supplier<String> clueItem;
    public static final Supplier<String> clueItemTook;
    public static final Supplier<String> clueItemPut;
    public static final Supplier<String> clueUnknown;
    public static final Supplier<String> clueHardToTell;


    static {
        IConfigBuilder config = ConfigBuilders.newTomlConfig(ColdCase.MOD_ID, "coldcase-custom", true);

        config.push("autopsy");
        damageLogWindow = config.comment("The time in milliseconds to look back for damage logs.")
                .onlyOnServer()
                .define("damageLogWindow", 60000, 0, Integer.MAX_VALUE);
        injuriesToShowDecay = config.comment("Time in milliseconds to lose one revealed injury log.")
                .onlyOnServer()
                .define("injuriesToShowDecay", 7200, 0, Integer.MAX_VALUE);
        suspectNameMaxTime = config.comment("Time in milliseconds after which the suspect's name is 'Unknown'.")
                .onlyOnServer()
                .define("suspectNameMaxTime", 144000, 0, Integer.MAX_VALUE);
        suspectNamePartialTime = config.comment("Time in milliseconds within which a partial suspect name is revealed.")
                .onlyOnServer()
                .define("suspectNamePartialTime", 12000, 0, Integer.MAX_VALUE);
        suspectSkinMaxTime = config.comment("Time in milliseconds after which the suspect's skin color is 'Unknown'.")
                .onlyOnServer()
                .define("suspectSkinMaxTime", 72000, 0, Integer.MAX_VALUE);
        suspectSkinRevealTime = config.comment("Time in milliseconds within which the suspect's skin color is revealed.")
                .onlyOnServer()
                .define("suspectSkinRevealTime", 24000, 0, Integer.MAX_VALUE);
        weaponNameMaxTime = config.comment("Time in milliseconds after which the weapon name is unrecoverable.")
                .onlyOnServer()
                .define("weaponNameMaxTime", 144000, 0, Integer.MAX_VALUE);
        config.pop();

        config.push("evidence");
        timeRevealChance = config.comment("The chance to reveal the time of an action.")
                .onlyOnServer()
                .define("timeRevealChance", 0.8, 0.0, 1.0);
        userRevealChance = config.comment("The chance to reveal the user who performed an action.")
                .onlyOnServer()
                .define("userRevealChance", 0.8, 0.0, 1.0);
        userNameLetterRevealChance = config.comment("The chance to reveal a letter from username.")
                .onlyOnServer()
                .define("partialUserRevealChance", 0.8, 0.0, 1.0);
        skinColorRevealChance = config.comment("The chance to reveal the skin color of the user.")
                .onlyOnServer()
                .define("skinColorRevealChance", 0.8, 0.0, 1.0);
        toolRevealChance = config.comment("The chance to reveal the tool used to perform an action.")
                .onlyOnServer()
                .define("toolRevealChance", 0.8, 0.0, 1.0);
        clueBaseRevealChance = config.comment("The base chance (in percent) to reveal the first clue.")
                .onlyOnServer()
                .define("itemRevealBaseChance", 100.0, 0.0, 100.0);
        itemRevealFalloff = config.comment("The multiplier (0.0-1.0) by which the reveal chance decreases for each subsequent item interaction.")
                .onlyOnServer()
                .define("itemRevealFalloff", 0.5, 0.0, 1.0);
        itemRevealFullChanceTime = config.comment("Time in milliseconds after which item reveal chance starts to decay.")
                .onlyOnServer()
                .define("itemRevealFullChanceTime", 60000, 0, Integer.MAX_VALUE); // 1 minute
        itemRevealZeroChanceTime = config.comment("Time in milliseconds after which item reveal chance becomes zero.")
                .onlyOnServer()
                .define("itemRevealZeroChanceTime", 3600000, 0, Integer.MAX_VALUE); // 1 hour
        timeInaccuracy = config.comment("The maximum inaccuracy of the time of an action (e.g. 0.1 for 10% inaccuracy).")
                .onlyOnServer()
                .define("timeInaccuracy", 0.01, 0.0, 1.0);

        minItemTransactionFactor = config.comment("The minimum multiplier (e.g., 0.15 for 15%) for the reveal chance of a container transaction.")
                .onlyOnServer()
                .define("minItemTransactionFactor", 0.15, 0.0, 1.0);
        maxItemTransactionFactor = config.comment("The maximum multiplier (e.g., 0.8 for 80%) for the reveal chance of a container transaction.")
                .onlyOnServer()
                .define("maxItemTransactionFactor", 0.8, 0.0, 1.0);
        minItemsForFactor = config.comment("The number of items moved to trigger the minimum reveal chance multiplier.")
                .onlyOnServer()
                .define("minItemsForFactor", 1, 1, Integer.MAX_VALUE);
        maxItemsForFactor = config.comment("The number of items moved to trigger the maximum reveal chance multiplier.")
                .onlyOnServer()
                .define("maxItemsForFactor", 64, 1, Integer.MAX_VALUE);
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

        config.push("other");
        identityTheftDuration = config.comment("The duration of the Identity Theft effect in ticks.")
                .onlyOnServer()
                .define("identityTheftDuration", 20 * 60 * 2, 0, Integer.MAX_VALUE);
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
                .define("clueNotFound", "§7No clues found.", 1, 100);
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
        clueUsernameContainsLetter = config.comment("Message for when a letter from the user of the clue is revealed. Placeholder: {letter}")
                .onlyOnServer()
                .define("clueUserStartWith", "§aThe culprit's name contains the letter '{letter}'.", 1, 100);
        clueUserUnknown = config.comment("Message for when the user of the clue is unknown.")
                .onlyOnServer()
                .define("clueUserUnknown", "§aThe culprit is unknown.", 1, 100);
        clueSkinColor = config.comment("Message for the skin color of the user. Placeholder: {skin_color}")
                .onlyOnServer()
                .define("clueSkinColor", "§aTheir skin color is {skin_color}.", 1, 100);
        clueTool = config.comment("Message for the tool used. Placeholder: {tool}")
                .onlyOnServer()
                .define("clueTool", "§aThe tool used was {tool}.", 1, 100);
        clueBlockAction = config.comment("Message for the block action. Placeholders: {action}, {block}")
                .onlyOnServer()
                .define("clueBlockAction", "§a{action} {block}.", 1, 100);
        clueItem = config.comment("Message for the item that was taken/placed. Placeholder: {item}")
                .onlyOnServer()
                .define("clueItem", "§aThe item was {item}.", 1, 100);
        clueItemTook = config.comment("Message for when an item was taken. Placeholder: {item}")
                .onlyOnServer()
                .define("clueItemTook", "§aTook {item}.", 1, 100);
        clueItemPut = config.comment("Message for when an item was put. Placeholder: {item}")
                .onlyOnServer()
                .define("clueItemPut", "§aPut {item}.", 1, 100);
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