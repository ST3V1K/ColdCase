package com.daqem.coldcase;

import com.daqem.coldcase.config.ColdCaseConfig;
import com.daqem.coldcase.config.ColdCaseCustomConfig;
import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.effect.ColdCaseMobEffects;
import com.daqem.coldcase.event.ChatEvent;
import com.daqem.coldcase.event.CommandEvent;
import com.daqem.coldcase.event.EntityEvents;
import com.daqem.coldcase.event.LevelLoadEvent;
import com.daqem.coldcase.event.PlayerJoinEvent;
import com.daqem.coldcase.event.PlayerQuitEvent;
import com.daqem.coldcase.event.RegisterCommandEvent;
import com.daqem.coldcase.event.TickEvents;
import com.daqem.coldcase.event.block.BlockEvents;
import com.daqem.coldcase.event.item.ItemEvents;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class ColdCase {
    public static final String MOD_ID = "coldcase";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static Database DATABASE;

    public static void init() {
        ColdCaseMobEffects.init();
    }

    public static void initServer() {
        initConfigs();
        boolean databaseReady = prepareDatabase();
        if (!databaseReady) {
            return;
        }
        registerEvents();
    }

    private static void initConfigs() {
        ColdCaseConfig.init();
        ColdCaseCustomConfig.init();
    }

    private static void registerEvents() {
        BlockEvents.registerEvents();
        TickEvents.registerEvents();
        EntityEvents.registerEvents();
        ItemEvents.registerEvents();

        PlayerJoinEvent.registerEvent();
        PlayerQuitEvent.registerEvent();
        LevelLoadEvent.registerEvent();
        RegisterCommandEvent.registerEvent();

        ChatEvent.registerEvent();
        CommandEvent.registerEvent();
    }

    private static boolean prepareDatabase() {
        LOGGER.info("Preparing ColdCase database...");
        long start = System.currentTimeMillis();
        try {
            DATABASE = new Database();
            boolean connected = DATABASE.createConnection();
            if (!connected) {
                LOGGER.error("Failed to connect to database, disabling ColdCase...");
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to connect to database, disabling ColdCase...", e);
            return false;
        }

        Services.MATERIAL.createTable();
        Services.USER.createTable();
        Services.USERNAME.createTable();
        Services.LEVEL.createTable();
        Services.ENTITY.createTable();
        Services.TOOL.createTable();
        Services.SKIN_COLOR.createTable();
        Services.BLOCK.createTable();
        Services.CONTAINER.createTable();
        Services.SESSION.createTable();
        Services.CHAT.createTable();
        Services.COMMAND.createTable();
        Services.ITEM.createTable();
        Services.DAMAGE.createTable();

        if (ColdCaseConfig.useIndexes.get()) {
            Services.BLOCK.createIndexes();
            Services.CHAT.createIndexes();
            Services.COMMAND.createIndexes();
            Services.CONTAINER.createIndexes();
            Services.ITEM.createIndexes();
            Services.SESSION.createIndexes();
            Services.DAMAGE.createIndexes();
        }

        long end = System.currentTimeMillis();
        LOGGER.info("Database prepared in {}ms.", end - start);
        return true;
    }

    public static Database getDatabase() {
        return DATABASE;
    }

    public static MutableComponent translate(String str) {
        MutableComponent component = translate(str, TranslatableContents.NO_ARGS);
        if (ColdCaseConfig.serverSideOnlyMode.get()) {
            component = Component.literal(component.getString()).withStyle(component.getStyle());
        }
        return component;
    }

    public static MutableComponent translate(String str, Object... args) {
        MutableComponent component = Component.translatable(MOD_ID + "." + str, args);
        if (ColdCaseConfig.serverSideOnlyMode.get()) {
            component = Component.literal(component.getString()).withStyle(component.getStyle());
        }
        return component;
    }

    public static MutableComponent literal(String str) {
        return Component.literal(str);
    }

    public static MutableComponent themedTranslate(String str) {
        MutableComponent component = themedTranslate(str, TranslatableContents.NO_ARGS);
        if (ColdCaseConfig.serverSideOnlyMode.get()) {
            component = Component.literal(component.getString()).withStyle(component.getStyle());
        }
        return component;
    }

    public static MutableComponent themedTranslate(String str, Object... args) {
        MutableComponent component = Component.translatable(MOD_ID + "." + str, args)
                .withStyle(getTheme());
        if (ColdCaseConfig.serverSideOnlyMode.get()) {
            component = Component.literal(component.getString()).withStyle(component.getStyle());
        }
        return component;
    }

    public static MutableComponent themedLiteral(String str) {
        MutableComponent component = Component.literal(str).withStyle(getTheme());
        if (ColdCaseConfig.serverSideOnlyMode.get()) {
            component = Component.literal(component.getString()).withStyle(component.getStyle());
        }
        return component;
    }

    public static Component getName() {
        Component component = translate("name").withStyle(getTheme());
        if (ColdCaseConfig.serverSideOnlyMode.get()) {
            component = Component.literal(component.getString()).withStyle(component.getStyle());
        }
        return component;
    }

    public static Style getTheme() {
        return Style.EMPTY.withColor(0xFCBA03);
    }

    public static ResourceLocation getId(String id) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }
}