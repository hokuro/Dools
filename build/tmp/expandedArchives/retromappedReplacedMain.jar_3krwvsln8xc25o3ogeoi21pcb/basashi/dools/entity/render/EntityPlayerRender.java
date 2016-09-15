package basashi.dools.entity.render;

import basashi.dools.entity.EntityDoolPlayer;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraft.client.model.ModelBiped.ArmPose;
@SideOnly(Side.CLIENT)
public class EntityPlayerRender extends RenderLivingBase<EntityDoolPlayer>
{
    /** this field is used to indicate the 3-pixel wide arms */
    private boolean smallArms;
    private LayerCustomHead customHed;

    public EntityPlayerRender(RenderManager renderManager)
    {
        this(renderManager, false);
    }

    public EntityPlayerRender(RenderManager renderManager, boolean useSmallArms)
    {
        super(renderManager, new ModelPlayer(0.0F, useSmallArms), 0.5F);
        this.smallArms = useSmallArms;
        this.func_177094_a(new LayerBipedArmor(this));
        this.func_177094_a(new LayerHeldItem(this));
        this.func_177094_a(new LayerArrow(this));
        this.func_177094_a(new LayerDool5Head(this));
        //this.addLayer(new LayerDoolCape(this)); // マント
        customHed=new LayerCustomHead(this.func_177087_b().field_78116_c);
        this.func_177094_a(customHed);
        //this.renderOutlines = true;
    }

    public ModelPlayer func_177087_b()
    {
        return (ModelPlayer)super.func_177087_b();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void func_76986_a(EntityDoolPlayer entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        double d0 = y;
        if (entity.func_70093_af())
        {
            d0 = y - 0.125D;
        }
        if ( entity.isSlim() != smallArms){
        	smallArms = entity.isSlim();
        	this.field_77045_g = new ModelPlayer(0.0F, smallArms);
        	this.func_177089_b(customHed);
        	customHed = new LayerCustomHead(this.func_177087_b().field_78116_c);
            this.func_177094_a(customHed);
        }


        this.setModelVisibilities(entity);
        super.func_76986_a(entity, x, d0, z, entityYaw, partialTicks);
   }

    private void setModelVisibilities(EntityDoolPlayer clientPlayer)
    {
        ModelPlayer modelplayer = this.func_177087_b();

        if (clientPlayer.isSpectator)
        {
            modelplayer.func_178719_a(false);
            modelplayer.field_78116_c.field_78806_j = true;
            modelplayer.field_178720_f.field_78806_j = true;
        }
        else
        {
            ItemStack itemstack = clientPlayer.func_184586_b(EnumHand.MAIN_HAND);
            modelplayer.func_178719_a(true);
            modelplayer.field_178720_f.field_78806_j = clientPlayer.isHat;
            modelplayer.field_178730_v.field_78806_j = clientPlayer.isJacket;
            modelplayer.field_178733_c.field_78806_j = clientPlayer.isLeftLeg;
            modelplayer.field_178731_d.field_78806_j = clientPlayer.isRightLeg;
            modelplayer.field_178734_a.field_78806_j = clientPlayer.isLeftSleeve;
            modelplayer.field_178732_b.field_78806_j = clientPlayer.isRightSleeve;
            modelplayer.field_187075_l = ArmPose.EMPTY;
            modelplayer.field_187076_m = ArmPose.EMPTY;
            //modelplayer.aimedBow = false;
            modelplayer.field_78117_n = clientPlayer.func_70093_af();

            if (itemstack == null)
            {
            	modelplayer.field_187075_l = ArmPose.EMPTY;
                modelplayer.field_187076_m = ArmPose.EMPTY;
            }
            else
            {
            	modelplayer.field_187075_l = ArmPose.ITEM;
                modelplayer.field_187076_m = ArmPose.EMPTY;
                EnumAction enumaction = itemstack.func_77975_n();
                if (enumaction == EnumAction.BLOCK)
                {
                    modelplayer.field_187075_l = ArmPose.BLOCK;
                }
                else if (enumaction == EnumAction.BOW)
                {
                	modelplayer.field_187075_l = ArmPose.BOW_AND_ARROW;
                    modelplayer.field_187076_m = ArmPose.BOW_AND_ARROW;
                }
            }

        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityLiving entity)
    {
        return ((EntityDoolPlayer)entity).getLocationSkin();
    }


    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void func_77041_b(EntityDoolPlayer entitylivingbaseIn, float partialTickTime)
    {
    	super.func_77041_b(entitylivingbaseIn, partialTickTime);
        float f = 0.9375F;
        GlStateManager.func_179152_a(f, f, f);
    }

    public void renderRightArm(EntityDoolPlayer clientPlayer)
    {
        float f = 1.0F;
        GlStateManager.func_179124_c(f, f, f);
        ModelPlayer modelplayer = this.func_177087_b();
        this.setModelVisibilities(clientPlayer);
        modelplayer.field_78095_p = 0.0F;
        modelplayer.field_78117_n = false;
        modelplayer.func_78087_a(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
        modelplayer.func_187073_a(0.0625F,EnumHandSide.LEFT);
        modelplayer.func_187073_a(0.0625F,EnumHandSide.RIGHT);
    }

    public void renderLeftArm(EntityDoolPlayer clientPlayer)
    {
        float f = 1.0F;
        GlStateManager.func_179124_c(f, f, f);
        ModelPlayer modelplayer = this.func_177087_b();
        this.setModelVisibilities(clientPlayer);
        modelplayer.field_78117_n = false;
        modelplayer.field_78095_p = 0.0F;
        modelplayer.func_78087_a(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
        modelplayer.func_187073_a(0.0625F,EnumHandSide.LEFT);
        modelplayer.func_187073_a(0.0625F,EnumHandSide.RIGHT);
    }

	@Override
	protected ResourceLocation func_110775_a(EntityDoolPlayer entity) {
        return ((EntityDoolPlayer)entity).getLocationSkin();
	}

	@Override
	public void func_177067_a(EntityDoolPlayer entity, double x, double y, double z)
    {

    }

//    /**
//     * Sets a simple glTranslate on a LivingEntity.
//     */
//    protected void renderLivingAt(EntityDoolPlayer entityLivingBaseIn, double x, double y, double z)
//    {
//        if (entityLivingBaseIn.isEntityAlive() && entityLivingBaseIn.isPlayerSleeping())
//        {
//            super.renderLivingAt(entityLivingBaseIn, x + (double)entityLivingBaseIn.renderOffsetX, y + (double)entityLivingBaseIn.renderOffsetY, z + (double)entityLivingBaseIn.renderOffsetZ);
//        }
//        else
//        {
//            super.renderLivingAt(entityLivingBaseIn, x, y, z);
//        }
//    }

//    protected void rotateCorpse(EntityDoolPlayer bat, float p_77043_2_, float p_77043_3_, float partialTicks)
//    {
//        if (bat.isEntityAlive() && bat.isPlayerSleeping())
//        {
//            GlStateManager.rotate(bat.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
//            GlStateManager.rotate(this.getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
//            GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
//        }
//        else
//        {
//            super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
//        }
//    }
}