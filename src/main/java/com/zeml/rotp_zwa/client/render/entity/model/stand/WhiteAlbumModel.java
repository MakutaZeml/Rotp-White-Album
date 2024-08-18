package com.zeml.rotp_zwa.client.render.entity.model.stand;

import com.github.standobyte.jojo.entity.stand.stands.StarPlatinumEntity;
import com.zeml.rotp_zwa.entity.stand.stands.WhiteAlbumEntity;
import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import com.github.standobyte.jojo.client.render.entity.pose.ModelPose;
import com.github.standobyte.jojo.client.render.entity.pose.RotationAngle;
import net.minecraft.client.renderer.model.ModelRenderer;

public class WhiteAlbumModel extends HumanoidStandModel<WhiteAlbumEntity> {

	final ModelRenderer spine_r1;

	public WhiteAlbumModel() {
		super();

		addHumanoidBaseBoxes(null);
		texWidth = 128;
		texHeight = 128;

		rightForeArm.texOffs(37, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.2F, false);


		spine_r1 = new ModelRenderer(this);
		rightForeArm.addChild(spine_r1);
		setRotationAngle(spine_r1, 0.0F, -0.5672F, 0.0F);
		spine_r1.texOffs(59, 8).addBox(-2.0F, 4.0F, -2.0F, 1.0F, 0.0F, 1.0F, 0.0F, false);
		spine_r1.texOffs(59, 8).addBox(-3.0F, 3.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, false);
		spine_r1.texOffs(59, 8).addBox(2.0F, 2.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, false);

	}


	@Override
	protected ModelPose initIdlePose() {
		return new ModelPose<>(new RotationAngle[] {
				RotationAngle.fromDegrees(body,14.03f, -26.8f, -6.43f),
				RotationAngle.fromDegrees(leftArm,39.71f, 16.59f, -53.97f),
				RotationAngle.fromDegrees(leftForeArm,-67.5f, 0f, 0f),
				RotationAngle.fromDegrees(rightArm,-52.5f, 0f, 75f),
				RotationAngle.fromDegrees(rightForeArm,-57.5f, 0f, 0f),
				RotationAngle.fromDegrees(leftLeg,-10.47f, 17.23f, -3.13f),
				RotationAngle.fromDegrees(leftLowerLeg,32.5f, 0f, 0f),
				RotationAngle.fromDegrees(rightLeg,45f, 0f, 0f),
				RotationAngle.fromDegrees(rightLegJoint, 30f, 0f, 0f)
		});
	}



}