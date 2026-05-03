package com.daqem.coldcase.item.detective;

import com.daqem.coldcase.util.RandomisedLookupUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class MagnifyingGlass extends Item {

    String LORE_KEY_FORMAT = "item.coldcase.magnifying_glass.lore.%d";

    public MagnifyingGlass(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = player.getOnPos();
        stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        RandomisedLookupUtils.performLookup(player, level, pos, Direction.UP);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        Direction direction = ctx.getClickedFace();

        if (player != null) {
            ctx.getItemInHand().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            RandomisedLookupUtils.performLookup(player, level, pos, direction);
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        Language language = Language.getInstance();
        for (int i = 1; language.has(LORE_KEY_FORMAT.formatted(i)); i++) {
            tooltip.add(Component.translatable(LORE_KEY_FORMAT.formatted(i)));
        }
    }
}
