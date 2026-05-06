package com.daqem.coldcase.client.entity.renderer;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.client.entity.model.DeadBodyModel;
import com.daqem.coldcase.entity.DeadBodyEntity;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DeadBodyRenderer extends LivingEntityRenderer<DeadBodyEntity, DeadBodyModel> {

    private static final ResourceLocation FLY_TEXTURE = ColdCase.getId("textures/particle/fly.png");

    public DeadBodyRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DeadBodyModel(ctx.bakeLayer(ModelLayers.PLAYER_SLIM), true), 0.5F);
    }

    @Override
    public void render(DeadBodyEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        renderFlies(entity, poseStack, buffer, partialTicks, packedLight);
    }

    private void renderFlies(DeadBodyEntity entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int packedLight) {
        long timeSinceDeath = System.currentTimeMillis() - entity.getDeathTime();
        if (timeSinceDeath <= 20) return;

        poseStack.pushPose();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucentCull(FLY_TEXTURE));

        int flyCount = entity.getFlyCount();
        for (int i = 0; i < flyCount; i++) {
            poseStack.pushPose();

            Vector3f offset = getRandomOffset(entity, partialTicks, i);
            Vector3f nextOffset = getRandomOffset(entity, partialTicks + 1.0f, i);

            poseStack.translate(offset.x, offset.y, offset.z);

            Quaternionf camRot = this.entityRenderDispatcher.camera.rotation();
            Quaternionf camRotInv = new Quaternionf(camRot).invert();

            poseStack.mulPose(camRot);

            Vector3f screenVel = new Vector3f(
                    nextOffset.x - offset.x,
                    nextOffset.y - offset.y,
                    nextOffset.z - offset.z
            ).rotate(camRotInv);

            if (screenVel.x * screenVel.x + screenVel.y * screenVel.y > 1e-8f) {
                float angleRad = Math.atan2(screenVel.y, screenVel.x) - Mth.PI * 0.5f;
                poseStack.mulPose(Axis.ZP.rotation(angleRad));
            }

            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

            Matrix4f matrix4f = poseStack.last().pose();
            float size = 0.025F;
            vertexConsumer.addVertex(matrix4f, size, size, 0)
                    .setColor(255, 255, 255, 255)
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(packedLight)
                    .setNormal(0, 0, 1);
            vertexConsumer.addVertex(matrix4f, size, -size, 0)
                    .setColor(255, 255, 255, 255)
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(packedLight)
                    .setNormal(0, 0, 1);
            vertexConsumer.addVertex(matrix4f, -size, -size, 0)
                    .setColor(255, 255, 255, 255)
                    .setUv(1, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(packedLight)
                    .setNormal(0, 0, 1);
            vertexConsumer.addVertex(matrix4f, -size, size, 0)
                    .setColor(255, 255, 255, 255)
                    .setUv(1, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(packedLight)
                    .setNormal(0, 0, 1);

            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private Vector3f getRandomOffset(DeadBodyEntity entity, float partialTicks, int i) {
        float phase = i * 2.399f;
        float t = entity.getId() + entity.tickCount + partialTicks;

        float x = Math.sin(t * 0.031f + phase) * 0.50f
                + Math.sin(t * 0.079f + phase * 1.3f) * 0.25f
                + Math.sin(t * 0.131f + phase * 0.7f) * 0.12f;
        float z = Math.sin(t * 0.041f + phase * 1.6f) * 0.50f
                + Math.sin(t * 0.071f + phase * 0.5f) * 0.25f
                + Math.sin(t * 0.109f + phase * 2.3f) * 0.12f;
        float y = Math.sin(t * 0.047f + phase * 2.1f) * 0.25f
                + Math.sin(t * 0.083f + phase * 0.9f) * 0.12f
                + 0.5f;

        return new Vector3f(x, y, z);
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
            return skin.texture();
        }
        return DefaultPlayerSkin.get(entity.getUUID()).texture();
    }
}
