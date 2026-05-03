package com.daqem.coldcase.mixin;

import com.daqem.coldcase.effect.IdentityTheftAccessor;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @ModifyVariable(method = "renderNameTag", at = @At("HEAD"), argsOnly = true)
    private Component onRenderNameTag(Component oldName, Entity entity) {
        if (entity instanceof IdentityTheftAccessor accessor) {
            Optional<Component> customName = accessor.coldcase$getStolenIdentity();
            return customName.orElse(oldName);
        }
        return oldName;
    }
}
