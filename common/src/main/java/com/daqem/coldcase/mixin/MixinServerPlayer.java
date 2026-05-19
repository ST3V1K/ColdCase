package com.daqem.coldcase.mixin;

import com.daqem.coldcase.block.container.ContainerHandler;
import com.daqem.coldcase.block.container.ContainerTransactionManager;
import com.daqem.coldcase.block.container.ContainersTransactionManager;
import com.daqem.coldcase.block.container.IContainerTransactionManager;
import com.daqem.coldcase.command.ClueNavigationCommand;
import com.daqem.coldcase.command.page.Page;
import com.daqem.coldcase.config.ColdCaseCustomConfig;
import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.event.item.DropItemEvent;
import com.daqem.coldcase.model.SimpleItemStack;
import com.daqem.coldcase.model.action.ItemAction;
import com.daqem.coldcase.model.history.IHistory;
import com.daqem.coldcase.model.history.UnreliableBlockHistory;
import com.daqem.coldcase.model.history.UnreliableContainerHistory;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import com.mojang.authlib.GameProfile;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements ColdCaseServerPlayer {

    @Shadow
    public ServerGamePacketListenerImpl connection;
    @Unique
    private boolean coldcase$inspecting = false;
    @Unique
    private IContainerTransactionManager coldcase$containerTransactionManager;
    @Unique
    private final Map<ItemAction, List<SimpleItemStack>> coldcase$itemQueue = new HashMap<>();
    @Unique
    private final List<Page> coldcase$pages = new ArrayList<>();

    public MixinServerPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Unique
    public boolean coldcase$isInspecting() {
        return coldcase$inspecting;
    }

    @Unique
    public void coldcase$setInspecting(boolean inspecting) {
        this.coldcase$inspecting = inspecting;
    }

    @Unique
    public void coldcase$sendInspectMessage(List<IHistory> historyList) {
        if (historyList.isEmpty()) {
            if (((Player) this) instanceof ServerPlayer serverPlayer)
                serverPlayer.sendSystemMessage(com.daqem.coldcase.ColdCase.translate("lookup.no_history", com.daqem.coldcase.ColdCase.getName()));
        } else {
            List<Page> pages = Page.convertToPages(historyList, true);
            coldcase$setPages(pages);
            Page pageToDisplay = pages.get(0);
            pageToDisplay.sendToPlayer(coldcase$asServerPlayer());
        }
    }

    @Unique
    public void coldcase$sendMagnifyingGlassMessage(List<IHistory> historyList) {
        ServerPlayer serverPlayer = coldcase$asServerPlayer();
        if (!historyList.isEmpty()) {
            List<IHistory> revealedHistories = new ArrayList<>();
            double currentRevealChance = ColdCaseCustomConfig.itemRevealBaseChance.get() / 100.0;
            
            for (IHistory history : historyList) {
                if (history.shouldReveal(currentRevealChance)) {
                    revealedHistories.add(history);
                    currentRevealChance *= ColdCaseCustomConfig.itemRevealFalloff.get();
                } else {
                    break; // Stop revealing if chance fails
                }
            }

            if (!revealedHistories.isEmpty()) {
                // Store the revealed list for navigation
                BlockPos locationPos = revealedHistories.get(0).getPosition().toBlockPos();
                ClueNavigationCommand.storePlayerHistoryList(serverPlayer.getUUID(), locationPos, revealedHistories);

                // Send the header once
                serverPlayer.sendSystemMessage(Component.literal(ColdCaseCustomConfig.clueFoundHeader.get()));
                
                // Send the first (latest) revealed clue with interactive footer
                IHistory firstClue = revealedHistories.get(0);
                if (firstClue instanceof UnreliableContainerHistory unreliableContainerHistory) {
                    serverPlayer.sendSystemMessage(unreliableContainerHistory.getInteractiveClueComponent(0, revealedHistories.size(), firstClue.getPosition()));
                } else {
                    // For block history, we'll just send the clue component for now.
                    // If interactive navigation is desired for block history too, this would need a similar interactive component.
                    serverPlayer.sendSystemMessage(firstClue.getClueComponent());
                }
                return;
            }
        }

        serverPlayer.sendSystemMessage(Component.literal(ColdCaseCustomConfig.clueNotFound.get()));
    }

    @Unique
    public ServerPlayer coldcase$asServerPlayer() {
        return (ServerPlayer) (Object) this;
    }

    @Override
    public List<Page> coldcase$getPages() {
        return coldcase$pages;
    }

    @Override
    public void coldcase$setPages(List<Page> pages) {
        coldcase$pages.clear();
        coldcase$pages.addAll(pages);
    }

    @Inject(at = @At("HEAD"), method = "openMenu")
    public void openMenu(MenuProvider menuProvider, CallbackInfoReturnable<OptionalInt> cir) {
        Optional<BaseContainerBlockEntity> container = ContainerHandler.getContainer(menuProvider);
        if (container.isPresent()) {
            this.coldcase$containerTransactionManager = new ContainerTransactionManager(container.get());
        } else {
            ContainerHandler.getContainers(menuProvider).ifPresent(containers -> {
                this.coldcase$containerTransactionManager = new ContainersTransactionManager(containers);
            });
        }
    }

    @Inject(at = @At("HEAD"), method = "doCloseContainer()V")
    public void coldcase$doCloseContainer(CallbackInfo ci) {
        EnvExecutor.getInEnv(EnvType.SERVER, () -> () -> {
            if (this.coldcase$containerTransactionManager != null) {
                this.coldcase$containerTransactionManager.finalize(coldcase$asServerPlayer());
                this.coldcase$containerTransactionManager = null;
            }
            return null;
        });
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void coldcase$tick(CallbackInfo ci) {
        EnvExecutor.getInEnv(EnvType.SERVER, () -> () -> {
            if (!coldcase$itemQueue.isEmpty()) {
                Services.ITEM.insertMap(this, blockPosition(), new HashMap<>(coldcase$itemQueue));
                coldcase$itemQueue.clear();
            }
            return null;
        });
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("RETURN"))
    private void drop(ItemStack itemStack, boolean bl, boolean bl2, CallbackInfoReturnable<ItemEntity> cir) {
        EnvExecutor.getInEnv(EnvType.SERVER, () -> () -> {
            if (cir.getReturnValue() != null) {
                DropItemEvent.onDropItem(this, cir.getReturnValue());
            }
            return null;
        });
    }

    public void coldcase$addItemToQueue(ItemAction action, SimpleItemStack itemStack) {
        List<SimpleItemStack> itemStacks = coldcase$itemQueue.get(action);
        if (itemStacks != null) {
            SimpleItemStack existingItemStack = itemStacks.stream()
                    .filter(itemStack::equals)
                    .findFirst()
                    .orElse(null);
            if (existingItemStack != null) {
                existingItemStack.setCount(existingItemStack.getCount() + itemStack.getCount());
                return;
            }
        }
        coldcase$itemQueue.computeIfAbsent(action, k -> new ArrayList<>()).add(itemStack);
    }
}
