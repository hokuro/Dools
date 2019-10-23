package basashi.dools.render;

import com.mojang.blaze3d.platform.GlStateManager;

import basashi.dools.entity.EntityDoolPlayer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerDool5Head extends LayerRenderer<EntityDoolPlayer, PlayerModel<EntityDoolPlayer>>
{
    private final IEntityRenderer playerRenderer;

    public LayerDool5Head(IEntityRenderer<EntityDoolPlayer, PlayerModel<EntityDoolPlayer>> p_i50945_1_) {
        super(p_i50945_1_);
        playerRenderer = p_i50945_1_;
     }

    @Override
    public void render(EntityDoolPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        if ("deadmau5".equals(entitylivingbaseIn.getName().getString()) && !entitylivingbaseIn.isInvisible()) {
            this.bindTexture(entitylivingbaseIn.getLocationSkin());

            for(int i = 0; i < 2; ++i) {
               float f = MathHelper.lerp(partialTicks, entitylivingbaseIn.prevRotationYaw, entitylivingbaseIn.rotationYaw) - MathHelper.lerp(partialTicks, entitylivingbaseIn.prevRenderYawOffset, entitylivingbaseIn.renderYawOffset);
               float f1 = MathHelper.lerp(partialTicks, entitylivingbaseIn.prevRotationPitch, entitylivingbaseIn.rotationPitch);
               GlStateManager.pushMatrix();
               GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);
               GlStateManager.rotatef(f1, 1.0F, 0.0F, 0.0F);
               GlStateManager.translatef(0.375F * (float)(i * 2 - 1), 0.0F, 0.0F);
               GlStateManager.translatef(0.0F, -0.375F, 0.0F);
               GlStateManager.rotatef(-f1, 1.0F, 0.0F, 0.0F);
               GlStateManager.rotatef(-f, 0.0F, 1.0F, 0.0F);
               float f2 = 1.3333334F;
               GlStateManager.scalef(1.3333334F, 1.3333334F, 1.3333334F);
               this.getEntityModel().renderDeadmau5Head(0.0625F);
               GlStateManager.popMatrix();
            }

         }
    }

    public boolean shouldCombineTextures() {
        return true;
    }

}
