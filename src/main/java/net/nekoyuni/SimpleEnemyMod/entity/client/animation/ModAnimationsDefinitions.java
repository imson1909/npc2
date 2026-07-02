package net.nekoyuni.SimpleEnemyMod.entity.client.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.animation.AnimationChannel.Interpolations;
import net.minecraft.client.animation.AnimationChannel.Targets;
import net.minecraft.client.animation.AnimationDefinition.Builder;

public class ModAnimationsDefinitions {
    public static final AnimationDefinition UNIT_WALK = Builder.withLength(0.9F)
            .looping()
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.15F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.15F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, 2.5F, -3.1F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.6F, KeyframeAnimations.scaleVec(1.1F, 0.97F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(28.05F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(37.93F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.15F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), Interpolations.LINEAR),
                                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 2.0F, -0.56F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.scaleVec(1.1F, 0.97F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.15F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, -0.58F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.02F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, -32.5F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.posVec(0.0F, -0.5F, -0.5F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.33F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.55F, KeyframeAnimations.posVec(0.0F, -0.53F, -0.47F), Interpolations.LINEAR),
                                    new Keyframe(0.7F, KeyframeAnimations.posVec(0.0F, 0.48F, -0.28F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.scaleVec(0.95F, 1.05F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.7F, KeyframeAnimations.scaleVec(0.95F, 1.05F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(1.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.7F, KeyframeAnimations.degreeVec(1.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.35F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.05F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.8F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.05F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(1.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.7F, KeyframeAnimations.degreeVec(1.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.35F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.05F), Interpolations.LINEAR),
                                    new Keyframe(0.8F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.05F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.65F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.posVec(0.0F, -0.39F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.2F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, 0.57F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, -0.2F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.55F, KeyframeAnimations.posVec(0.0F, -0.54F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.65F, KeyframeAnimations.posVec(0.0F, 0.2F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.15F, KeyframeAnimations.scaleVec(1.0, 0.975F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.scaleVec(1.0, 1.0125F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.6F, KeyframeAnimations.scaleVec(1.0, 0.975F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0, 1.0125F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.9F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .build();
    public static final AnimationDefinition UNIT_IDLE = Builder.withLength(2.0F)
            .looping()
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0, 1.025F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0, 1.025F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-1.5693F, 0.1902F, -2.9585F), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .build();
    public static final AnimationDefinition UNIT_HURT = Builder.withLength(1.0F)
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.15F, KeyframeAnimations.degreeVec(6.0F, 20.0F, 3.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.25F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.scaleVec(1.05F, 1.025F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.4F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), Interpolations.LINEAR),
                                    new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(36.9656F, -36.9448F, -36.1963F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.5F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(-16.1284F, 2.4019F, -12.3162F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.25F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.scaleVec(1.0, 1.025F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(49.2474F, 23.6019F, 26.7408F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.75F, -0.5F, -1.5F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .build();
    public static final AnimationDefinition UNIT_HURT_1 = Builder.withLength(1.0F)
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(2.5F, 64.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(54.7909F, 63.0219F, 18.8208F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(30.0F, 57.5F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(2.0F, 0.0F, 4.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(110.2224F, 42.922F, 85.3298F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(-3.0F, 0.0F, -4.0F), Interpolations.LINEAR),
                                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .build();
    public static final AnimationDefinition UNIT_DEATH = Builder.withLength(1.0F)
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(5.0F, 30.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(3.7169F, -4.907F, -2.8908F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.25F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.5F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.scaleVec(1.05F, 1.025F, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.4F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-43.5557F, 15.815F, -7.59F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(-61.0557F, 15.815F, -7.59F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(-11.0557F, 15.815F, -7.59F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.25F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, 0.25F, 3.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-33.4276F, -8.5373F, 12.3796F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(-48.4276F, -8.5373F, 12.3796F), Interpolations.LINEAR),
                                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(34.9309F, -0.2351F, -29.4674F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, 0.75F, 2.25F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(-100.0F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, -16.0F, 19.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(Targets.SCALE, new Keyframe[]{new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)})
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.degreeVec(36.4115F, 1.5586F, 0.0665F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.35F, KeyframeAnimations.degreeVec(5.1766F, -32.4718F, -1.3846F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.25F), Interpolations.LINEAR),
                                    new Keyframe(0.35F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.5F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(17.0198F, 51.2398F, 8.727F), Interpolations.LINEAR),
                                    new Keyframe(0.35F, KeyframeAnimations.degreeVec(117.8067F, 80.5987F, 118.1374F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.25F), Interpolations.LINEAR),
                                    new Keyframe(0.35F, KeyframeAnimations.posVec(-0.75F, 0.0F, 1.25F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(Targets.SCALE, new Keyframe[]{new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)})
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(22.4167F, -8.7669F, -5.4641F), Interpolations.LINEAR),
                                    new Keyframe(0.35F, KeyframeAnimations.degreeVec(152.9738F, -110.8276F, -185.9508F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.35F, KeyframeAnimations.posVec(0.75F, 0.0F, 2.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .build();
    public static final AnimationDefinition UNIT_DEATH_BACK = Builder.withLength(1.0F)
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(82.5F, 0.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "unit",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.posVec(0.0F, 0.0F, -10.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, -8.0F, -20.12F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, -20.0F, -23.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.35F, KeyframeAnimations.degreeVec(10.6946F, -34.7029F, -4.6745F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(Targets.POSITION, new Keyframe[]{new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)})
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.degreeVec(-5.0514F, -27.3983F, 2.3867F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(-2.5514F, -27.3983F, 2.3867F), Interpolations.LINEAR),
                                    new Keyframe(0.35F, KeyframeAnimations.degreeVec(-2.4226F, -19.9266F, 1.7226F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(Targets.POSITION, new Keyframe[]{new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)})
            )
            .addAnimation(
                    "body",
                    new AnimationChannel(
                            Targets.SCALE,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.scaleVec(1.175F, 1.0, 0.725F), Interpolations.LINEAR),
                                    new Keyframe(0.4F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.05F, KeyframeAnimations.degreeVec(0.0F, 122.5F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(0.0F, 143.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 147.5F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(0.0F, 92.5F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightArm",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.4F, KeyframeAnimations.posVec(-1.0F, 0.0F, -2.25F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.degreeVec(147.5F, -117.2953F, -180.0F), Interpolations.LINEAR),
                                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(158.5723F, -83.6427F, -192.7307F), Interpolations.LINEAR),
                                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(-25.0F, -115.0F, 0.0F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftArm",
                    new AnimationChannel(Targets.POSITION, new Keyframe[]{new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)})
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.degreeVec(31.8218F, -9.5438F, 8.1102F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(41.8218F, -9.5438F, 8.1102F), Interpolations.LINEAR),
                                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(-18.1782F, -9.5438F, 8.1102F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "rightLeg",
                    new AnimationChannel(
                            Targets.POSITION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.posVec(-0.75F, 0.0F, -1.75F), Interpolations.LINEAR),
                                    new Keyframe(0.3F, KeyframeAnimations.posVec(-0.75F, 0.0F, -0.75F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(
                            Targets.ROTATION,
                            new Keyframe[]{
                                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                                    new Keyframe(0.1F, KeyframeAnimations.degreeVec(54.1689F, -11.135F, -6.3422F), Interpolations.LINEAR),
                                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(94.9162F, 1.2988F, -7.3873F), Interpolations.LINEAR),
                                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(24.6541F, 1.6985F, -17.3828F), Interpolations.LINEAR)
                            }
                    )
            )
            .addAnimation(
                    "leftLeg",
                    new AnimationChannel(Targets.POSITION, new Keyframe[]{new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)})
            )
            .build();
    public static final AnimationDefinition[] UNIT_HURT_VARIANTS = new AnimationDefinition[]{UNIT_HURT, UNIT_HURT_1};
}