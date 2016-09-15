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

    public void func_177141_a(EntityDoolPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.func_70005_c_().equals("deadmau5") && !entitylivingbaseIn.func_82150_aj())
        {
            this.playerRenderer.func_110776_a(entitylivingbaseIn.getLocationSkin());

            for (int i = 0; i < 2; ++i)
            {
                float f = entitylivingbaseIn.field_70126_B + (entitylivingbaseIn.field_70177_z - entitylivingbaseIn.field_70126_B) * partialTicks - (entitylivingbaseIn.field_70760_ar + (entitylivingbaseIn.field_70761_aq - entitylivingbaseIn.field_70760_ar) * partialTicks);
                float f1 = entitylivingbaseIn.field_70127_C + (entitylivingbaseIn.field_70125_A - entitylivingbaseIn.field_70127_C) * partialTicks;
                GlStateManager.func_179094_E();
                GlStateManager.func_179114_b(f, 0.0F, 1.0F, 0.0F);
                GlStateManager.func_179114_b(f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.func_179109_b(0.375F * (float)(i * 2 - 1), 0.0F, 0.0F);
                GlStateManager.func_179109_b(0.0F, -0.375F, 0.0F);
                GlStateManager.func_179114_b(-f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.func_179114_b(-f, 0.0F, 1.0F, 0.0F);
                float f2 = 1.3333334F;
                GlStateManager.func_179152_a(f2, f2, f2);
                this.playerRenderer.func_177087_b().func_178727_b(0.0625F);
                GlStateManager.func_179121_F();
            }
        }
    }

    public boolean func_177142_b()
    {
        return true;
    }
}