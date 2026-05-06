package com.daqem.coldcase.client.entity.renderer;

import com.daqem.coldcase.client.entity.model.DeadBodyModel;
import com.daqem.coldcase.entity.DeadBodyEntity;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;

public class DeadBodyRenderer extends LivingEntityRenderer<DeadBodyEntity, DeadBodyModel> {

    public DeadBodyRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DeadBodyModel(ctx.bakeLayer(ModelLayers.PLAYER_SLIM), true), 0.5F);
    }

    @Override
    protected void setupRotations(DeadBodyEntity entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks, float f) {
        super.setupRotations(entity, stack, ageInTicks, rotationYaw, partialTicks, f);
        stack.mulPose(Axis.XP.rotationDegrees(90.0F));
        stack.translate(0.0D, -1.0D, -0.15D);
    }

    @Override
    protected boolean shouldShowName(DeadBodyEntity entity) {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(DeadBodyEntity entity) {
        GameProfile deceasedProfile = entity.getDeceasedProfile();
        if (deceasedProfile != null) {
            PlayerSkin skin = Minecraft.getInstance()
                    .getSkinManager()
                    .getInsecureSkin(deceasedProfile);
            if (skin != null) {
                return skin.texture();
            }
        }
        return DefaultPlayerSkin.get(entity.getUUID()).texture();
    }
}
