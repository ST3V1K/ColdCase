package com.daqem.coldcase.item.criminal;

import com.daqem.coldcase.config.ColdCaseCustomConfig;
import com.daqem.coldcase.effect.ColdCaseMobEffects;
import com.daqem.coldcase.effect.IdentityTheftAccessor;
import com.daqem.coldcase.item.ColdCaseItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class IdentityTheft extends ColdCaseItem {

    public IdentityTheft(Properties props) {
        super(props);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player
                && !level.isClientSide()
                && stack.has(DataComponents.CUSTOM_NAME)) {
            setEffect(player, stack.getHoverName());
            return super.finishUsingItem(stack, level, entity);
        }
        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    public String getLoreKeyFormat() {
        return "item.coldcase.identity_theft.lore.%d";
    }

    private void setEffect(Player player, Component name) {
        if (player.level().isClientSide()) {
            return;
        }

        Holder<MobEffect> holder = ColdCaseMobEffects.identityTheftHolder();
        MobEffectInstance oldIdentityTheft = player.getEffect(holder);
        int duration = ColdCaseCustomConfig.identityTheftDuration.get();
        MobEffectInstance instance = new MobEffectInstance(holder, duration, 0, true, false, true, oldIdentityTheft);
        player.addEffect(instance);

        if (player instanceof IdentityTheftAccessor accessor) {
            accessor.coldcase$setStolenIdentity(name);
        }
    }
}
