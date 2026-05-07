package com.daqem.coldcase.item.detective;

import com.daqem.coldcase.config.ColdCaseCustomConfig;
import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.entity.DeadBodyEntity;
import com.daqem.coldcase.item.ColdCaseItem;
import com.daqem.coldcase.model.DamageLog;
import com.daqem.coldcase.util.InvestigationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class AutopsyKit extends ColdCaseItem {

    public AutopsyKit(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        Level level = entity.level();
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        if (entity instanceof DeadBodyEntity deadBody) {
            long deathTime = deadBody.getDeathTime();
            long timeSinceDeath = System.currentTimeMillis() - deathTime;

            deadBody.getDeceasedUuid().ifPresent(deceasedUuid -> {
                List<DamageLog> damageLogs = Services.DAMAGE.getDamageHistory(
                        level.registryAccess(),
                        deceasedUuid,
                        deathTime - ColdCaseCustomConfig.damageLogWindow.get(),
                        deathTime
                );

                if (damageLogs == null || damageLogs.isEmpty()) {
                    player.sendSystemMessage(Component.literal("No damage logs found for this body.").withStyle(ChatFormatting.RED));
                    return;
                }

                generateAutopsyReport(player, timeSinceDeath, damageLogs, deadBody.getAttackerSkinColor());
            });

            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private void generateAutopsyReport(Player player, long timeSinceDeath, List<DamageLog> damageLogs, String attackerSkinColor) {
        player.sendSystemMessage(Component.literal("--- Autopsy Report ---").withStyle(ChatFormatting.GOLD));

        player.sendSystemMessage(Component.literal("Estimated Time of Death: " + InvestigationUtils.getTimeWindow(timeSinceDeath)).withStyle(ChatFormatting.GRAY));

        DamageLog lastDamage = damageLogs.getFirst();
        player.sendSystemMessage(Component.literal("Cause of Death: " + InvestigationUtils.getDamageCause(lastDamage)).withStyle(ChatFormatting.RED));

        List<DamageLog> revealedLogs = InvestigationUtils.getRevealedDamageLogs(damageLogs, timeSinceDeath);
        if (!revealedLogs.isEmpty()) {
            player.sendSystemMessage(Component.literal("Recent Injuries:").withStyle(ChatFormatting.YELLOW));
            for (DamageLog log : revealedLogs) {
                player.sendSystemMessage(Component.literal(" - " + InvestigationUtils.getDamageCause(log)).withStyle(ChatFormatting.GRAY));
            }
        }

        if (lastDamage.attackerName() != null) {
            player.sendSystemMessage(Component.literal("Suspect Information:").withStyle(ChatFormatting.YELLOW));
            player.sendSystemMessage(Component.literal(" - " + InvestigationUtils.getSuspectNameInfo(lastDamage.attackerName(), timeSinceDeath)).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(" - " + InvestigationUtils.getSuspectSkinInfo(attackerSkinColor, timeSinceDeath)).withStyle(ChatFormatting.DARK_PURPLE));
            
            ItemStack weapon = lastDamage.attackerEquipment().get(net.minecraft.world.entity.EquipmentSlot.MAINHAND);
            player.sendSystemMessage(Component.literal(" - " + InvestigationUtils.getWeaponName(weapon, timeSinceDeath)).withStyle(ChatFormatting.DARK_AQUA));
        }
        
        player.sendSystemMessage(Component.literal("--------------------").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public String getLoreKeyFormat() {
        return "item.coldcase.autopsy_kit.lore.%d";
    }
}
