package basashi.dools.entity.render;

import basashi.dools.entity.EntityDoolPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerDool5Head implements LayerRenderer<EntityDoolPlayer>
{
    private final EntityPlayerRender playerRenderer;

    public LayerDool5Head(EntityPlayerRender playerRendererIn)
    {
        this.playerRenderer = playerRendererIn;
    }

    public void doRenderLayer(EntityDoolPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.getName().equals("deadmau5") && !entitylivingbaseIn.isInvisible())
        {
            this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationSkin());

            for (int i = 0; i < 2; ++i)
            {
                float f = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
                float f1 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
                GlStateManager.pushMatrix();
                GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.375F * (float)(i * 2 - 1), 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -0.375F, 0.0F);
                GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
                float f2 = 1.3333334F;
                GlStateManager.scale(f2, f2, f2);
                this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
                GlStateManager.popMatrix();
            }
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}