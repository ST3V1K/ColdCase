package com.daqem.coldcase.util;

import com.daqem.coldcase.config.ColdCaseCustomConfig;
import com.daqem.coldcase.model.DamageLog;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class InvestigationUtils {

    private static final Random RANDOM = new Random();

    public static String getTimeWindow(long timeSinceDeath) {
        long seconds = timeSinceDeath / 1000;
        if (seconds < 60) return "Within the last minute";
        if (seconds < 300) return "1-5 minutes ago";
        if (seconds < 600) return "5-10 minutes ago";
        if (seconds < 1800) return "10-30 minutes ago";
        if (seconds < 3600) return "30-60 minutes ago";
        if (seconds < 7200) return "1-2 hours ago";
        return "Over 2 hours ago";
    }

    public static String getDamageCause(DamageLog log) {
        if (log.type().equals("player")) {
            ItemStack weapon = log.attackerEquipment()
                    .get(net.minecraft.world.entity.EquipmentSlot.MAINHAND);
            if (weapon != null && !weapon.isEmpty()) {
                return "Cut by " + weapon.getHoverName().getString();
            } else {
                return "Fist hit";
            }
        }
        return log.type();
    }

    public static List<DamageLog> getRevealedDamageLogs(List<DamageLog> logs, long timeSinceDeath) {
        int injuriesToShow = Math.max(1, 10 - (int) (timeSinceDeath / ColdCaseCustomConfig.injuriesToShowDecay.get())); // Lose one log every 6 minutes
        return logs.subList(0, Math.min(injuriesToShow, logs.size()));
    }

    public static String getSuspectNameInfo(String attackerName, long timeSinceDeath) {
        if (attackerName == null) {
            return "Unknown";
        }
        long maxTime = ColdCaseCustomConfig.suspectNameMaxTime.get();
        if (timeSinceDeath > maxTime) {
            return "Unknown";
        }

        float decayFactor = 1.0F - (timeSinceDeath / (float) maxTime);

        double fullNameChance = ColdCaseCustomConfig.userRevealChance.get() * decayFactor;
        if (RANDOM.nextDouble() < fullNameChance) {
            return "Suspect identified as " + attackerName + ".";
        }

        double partialNameChance = ColdCaseCustomConfig.partialUserRevealChance.get() * decayFactor;
        if (RANDOM.nextDouble() < partialNameChance) {
            char randomChar = attackerName.charAt(RANDOM.nextInt(attackerName.length()));
            return "Name may contain the letter '" + randomChar + "'.";
        }

        return "Too decomposed to determine.";
    }

    public static String getSuspectSkinInfo(String attackerSkinColor, long timeSinceDeath) {
        if (attackerSkinColor == null) {
            return "Unknown";
        }
        long maxTime = ColdCaseCustomConfig.suspectSkinMaxTime.get();
        if (timeSinceDeath > maxTime) {
            return "Unknown";
        }

        float decayFactor = 1.0F - (timeSinceDeath / (float) maxTime);
        double skinChance = ColdCaseCustomConfig.skinColorRevealChance.get() * decayFactor;

        if (RANDOM.nextDouble() < skinChance) {
            return "Skin color appears to be " + attackerSkinColor + ".";
        }

        return "Skin is too decomposed to determine color.";
    }

    public static String getWeaponName(ItemStack weapon, long timeSinceDeath) {
        if (weapon == null || weapon.isEmpty() || !weapon.has(DataComponents.CUSTOM_NAME)) {
            return "No special weapon detected.";
        }
        long maxTime = ColdCaseCustomConfig.weaponNameMaxTime.get();
        if (timeSinceDeath > maxTime) {
            return "Traces of a custom weapon were found, but the name is unrecoverable.";
        }

        float decayFactor = 1.0F - (timeSinceDeath / (float) maxTime);
        double chance = ColdCaseCustomConfig.toolRevealChance.get() * decayFactor;

        if (RANDOM.nextDouble() < chance) {
            return "Weapon was named: " + weapon.getHoverName().getString();
        }
        return "Traces of a custom weapon were found, but the name is unrecoverable.";
    }
}
