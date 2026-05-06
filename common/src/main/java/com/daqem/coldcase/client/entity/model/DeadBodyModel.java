package com.daqem.coldcase.client.entity.model;

import com.daqem.coldcase.entity.DeadBodyEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;

public class DeadBodyModel extends PlayerModel<DeadBodyEntity> {

    public DeadBodyModel(ModelPart root, boolean slim) {
        super(root, slim);
    }

    @Override
    public void setupAnim(DeadBodyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // We do not call super.setupAnim() to prevent any idle or movement animations.
        // Instead, we ensure all parts are at a neutral, "dead" pose.
        
        this.head.xRot = 0;
        this.head.yRot = 0;
        this.head.zRot = 0;

        this.hat.copyFrom(this.head);

        this.body.xRot = 0;
        this.body.yRot = 0;
        this.body.zRot = 0;

        this.rightArm.xRot = 0;
        this.rightArm.yRot = 0;
        this.rightArm.zRot = 0;

        this.leftArm.xRot = 0;
        this.leftArm.yRot = 0;
        this.leftArm.zRot = 0;

        this.rightLeg.xRot = 0;
        this.rightLeg.yRot = 0;
        this.rightLeg.zRot = 0;

        this.leftLeg.xRot = 0;
        this.leftLeg.yRot = 0;
        this.leftLeg.zRot = 0;
    }
}
