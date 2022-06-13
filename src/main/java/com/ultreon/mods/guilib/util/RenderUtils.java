package com.ultreon.mods.guilib.util;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class RenderUtils {
    public static void renderEntityInGui(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity) {
        float yRot = (float) Math.atan(mouseX / 40.0F);
        float xRot = (float) Math.atan(mouseY / 40.0F);

        renderEntityInGui(posX, posY, xRot, yRot, (float) scale, livingEntity);
    }

    private static void renderEntityInGui(int posX, int posY, float xRot, float yRot, float scale, LivingEntity livingEntity) {
        PoseStack modelView = RenderSystem.getModelViewStack();
        modelView.pushPose();
        modelView.translate(posX, posY, 1050.0D);
        modelView.scale(1.0F, 1.0F, -1.0F);

        RenderSystem.applyModelViewMatrix();
        PoseStack pose = new PoseStack();
        pose.translate(0.0D, 0.0D, 1000.0D);
        pose.scale(scale, scale, scale);

        Quaternion flipped = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion = Vector3f.XP.rotationDegrees(xRot * 20.0F);
        flipped.mul(quaternion);
        pose.mulPose(flipped);

        float bodyRot = livingEntity.yBodyRot;
        float oldYRot = livingEntity.getYRot();
        float oldXRot = livingEntity.getXRot();
        float headYRot0 = livingEntity.yHeadRotO;
        float headYRot = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 180.0F + yRot * 20.0F;
        livingEntity.setYRot(180.0F + yRot * 40.0F);
        livingEntity.setXRot(-xRot * 20.0F);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();

        Lighting.setupForEntityInInventory();

        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion.conj();
        dispatcher.overrideCameraOrientation(quaternion);
        dispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            dispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, pose, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        dispatcher.setRenderShadow(true);

        livingEntity.yBodyRot = bodyRot;
        livingEntity.setYRot(oldYRot);
        livingEntity.setXRot(oldXRot);
        livingEntity.yHeadRotO = headYRot0;
        livingEntity.yHeadRot = headYRot;

        modelView.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    public static void renderEntityInGui(int posX, int posY, int scale, float mouseX, float mouseY, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            renderEntityInGui(posX, posY, scale, mouseX, mouseY, livingEntity);
            return;
        }

        float yRot = (float) Math.atan(mouseX / 40.0F);
        float xRot = (float) Math.atan(mouseY / 40.0F);

        renderEntityInGui(posX, posY, xRot, yRot, (float) scale, entity);
    }

    public static void renderEntityInGui(int posX, int posY, float xRot, float yRot, float scale, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            renderEntityInGui(posX, posY, xRot, yRot, scale, livingEntity);
            return;
        }

        PoseStack modelView = RenderSystem.getModelViewStack();
        modelView.pushPose();
        modelView.translate(posX, posY, 1050.0D);
        modelView.scale(1.0F, 1.0F, -1.0F);

        RenderSystem.applyModelViewMatrix();
        PoseStack pose = new PoseStack();
        pose.translate(0.0D, 0.0D, 1000.0D);
        pose.scale(scale, scale, scale);

        Quaternion flipped = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion = Vector3f.XP.rotationDegrees(xRot * 20.0F);
        flipped.mul(quaternion);
        pose.mulPose(flipped);

        float oldYRot = entity.getYRot();
        float oldXRot = entity.getXRot();
        entity.setYRot(180.0F + yRot * 40.0F);
        entity.setXRot(-xRot * 20.0F);

        Lighting.setupForEntityInInventory();

        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion.conj();
        dispatcher.overrideCameraOrientation(quaternion);
        dispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, pose, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        dispatcher.setRenderShadow(true);

        entity.setYRot(oldYRot);
        entity.setXRot(oldXRot);

        modelView.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }
}
