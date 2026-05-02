package com.daqem.coldcase.util;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.thread.ThreadManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import net.minecraft.world.entity.player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinColorManager {

    private static final Map<UUID, String> SKIN_COLOR_CACHE = new HashMap<>();
    private static final Map<String, Color> PALETTE = new HashMap<>();

    static {
        PALETTE.put("White", new Color(255, 255, 255));
        PALETTE.put("Orange", new Color(216, 127, 51));
        PALETTE.put("Magenta", new Color(178, 76, 216));
        PALETTE.put("Light Blue", new Color(102, 153, 216));
        PALETTE.put("Yellow", new Color(229, 229, 51));
        PALETTE.put("Lime", new Color(127, 204, 25));
        PALETTE.put("Pink", new Color(242, 127, 165));
        PALETTE.put("Gray", new Color(76, 76, 76));
        PALETTE.put("Light Gray", new Color(153, 153, 153));
        PALETTE.put("Cyan", new Color(0, 183, 235));
        PALETTE.put("Purple", new Color(127, 63, 178));
        PALETTE.put("Blue", new Color(51, 76, 255));
        PALETTE.put("Brown", new Color(102, 76, 51));
        PALETTE.put("Green", new Color(0, 160, 0));
        PALETTE.put("Red", new Color(200, 51, 51));
        PALETTE.put("Black", new Color(10, 10, 10));

        PALETTE.put("Light Skin", new Color(255, 219, 172));
        PALETTE.put("Medium Skin", new Color(224, 172, 105));
        PALETTE.put("Dark Skin", new Color(141, 85, 36));
    }

    public static String getSkinColor(Player player) {
        return SKIN_COLOR_CACHE.getOrDefault(player.getUUID(), "unknown");
    }

    public static void fetchSkinColorAsync(Player player) {
        UUID uuid = player.getUUID();
        if (SKIN_COLOR_CACHE.containsKey(uuid)) return;

        ThreadManager.submit(() -> {
            try {
                Property textures = player.getGameProfile()
                        .getProperties()
                        .get("textures")
                        .stream()
                        .findFirst()
                        .orElse(null);
                if (textures != null) {
                    String value = textures.value();
                    String decoded = new String(Base64.getDecoder().decode(value));
                    JsonObject texturesJson = JsonParser.parseString(decoded).getAsJsonObject();

                    if (texturesJson.has("textures") && texturesJson.getAsJsonObject("textures")
                            .has("SKIN")) {
                        String skinUrl = texturesJson.getAsJsonObject("textures")
                                .getAsJsonObject("SKIN")
                                .get("url")
                                .getAsString();

                        try (InputStream in = URI.create(skinUrl).toURL().openStream()) {
                            BufferedImage image = ImageIO.read(in);
                            String prominentColor = calculateProminentColor(image);
                            SKIN_COLOR_CACHE.put(uuid, prominentColor);
                        }
                        return null;
                    }
                }
            } catch (Exception e) {
                ColdCase.LOGGER.error("Failed to fetch skin color for {}",
                        player.getName().getString(), e);
            }
            SKIN_COLOR_CACHE.put(uuid, "unknown");
            return null;
        }, result -> {});
    }

    private static String calculateProminentColor(BufferedImage image) {
        Map<String, Integer> colorCounts = new HashMap<>();

        // Head: 8,8 to 16,16 (Front)
        tallyRegion(image, 8, 8, 16, 16, colorCounts);
        // Torso: 20,20 to 28,32 (Front)
        tallyRegion(image, 20, 20, 28, 32, colorCounts);

        String bestColor = "unknown";
        int maxCount = -1;
        for (Map.Entry<String, Integer> entry : colorCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                bestColor = entry.getKey();
            }
        }
        return bestColor;
    }

    private static void tallyRegion(BufferedImage image, int startX, int startY, int endX, int endY, Map<String, Integer> colorCounts) {
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int rgb = image.getRGB(x, y);
                if ((rgb >> 24) == 0x00)
                    continue;

                Color pixelColor = new Color(rgb, true);
                String closest = getClosestColor(pixelColor);
                colorCounts.put(closest, colorCounts.getOrDefault(closest, 0) + 1);
            }
        }
    }

    private static String getClosestColor(Color target) {
        String closestName = "unknown";
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<String, Color> entry : PALETTE.entrySet()) {
            Color p = entry.getValue();
            double distance = Math.pow(target.getRed() - p.getRed(), 2) +
                    Math.pow(target.getGreen() - p.getGreen(), 2) +
                    Math.pow(target.getBlue() - p.getBlue(), 2);
            if (distance < minDistance) {
                minDistance = distance;
                closestName = entry.getKey();
            }
        }
        return closestName;
    }
}
