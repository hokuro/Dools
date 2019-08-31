package basashi.dools.entity.render;

import basashi.dools.entity.EntityDoolPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.entity.model.ModelPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerDool5Head(this));
        //this.addLayer(new LayerDoolCape(this)); // マント
        customHed=new LayerCustomHead(this.getMainModel().bipedHead);
        this.addLayer(customHed);
        //this.renderOutlines = true;
    }

    public ModelPlayer getMainModel()
    {
        return (ModelPlayer)super.getMainModel();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityDoolPlayer entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        double d0 = y;
        if (entity.isSneaking())
        {
            d0 = y - 0.125D;
        }
        if ( entity.isSlim() != smallArms){
        	smallArms = entity.isSlim();
        	this.mainModel = new ModelPlayer(0.0F, smallArms);
        	this.layerRenderers.remove(customHed);
        	customHed = new LayerCustomHead(this.getMainModel().bipedHead);
            this.addLayer(customHed);
        }


        this.setModelVisibilities(entity);
        super.doRender(entity, x, d0, z, entityYaw, partialTicks);
   }

    private void setModelVisibilities(EntityDoolPlayer clientPlayer)
    {
        ModelPlayer modelplayer = this.getMainModel();

        if (clientPlayer.isSpectator)
        {
            modelplayer.setVisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        }
        else
        {
            ItemStack itemstack = clientPlayer.getHeldItem(EnumHand.MAIN_HAND);
            modelplayer.setVisible(true);
            modelplayer.bipedHeadwear.showModel = clientPlayer.isHat;
            modelplayer.bipedBodyWear.showModel = clientPlayer.isJacket;
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isLeftLeg;
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isRightLeg;
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isLeftSleeve;
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isRightSleeve;
            modelplayer.leftArmPose = ArmPose.EMPTY;
            modelplayer.rightArmPose = ArmPose.EMPTY;
            //modelplayer.aimedBow = false;
            modelplayer.isSneak = clientPlayer.isSneaking();

            if (itemstack.isEmpty())
            {
            	modelplayer.leftArmPose = ArmPose.EMPTY;
                modelplayer.rightArmPose = ArmPose.EMPTY;
            }
            else
            {
            	modelplayer.leftArmPose = ArmPose.ITEM;
                modelplayer.rightArmPose = ArmPose.EMPTY;
                EnumAction enumaction = itemstack.getUseAction();
                if (enumaction == EnumAction.BLOCK)
                {
                    modelplayer.leftArmPose = ArmPose.BLOCK;
                }
                else if (enumaction == EnumAction.BOW)
                {
                	modelplayer.leftArmPose = ArmPose.BOW_AND_ARROW;
                    modelplayer.rightArmPose = ArmPose.BOW_AND_ARROW;
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
    protected void preRenderCallback(EntityDoolPlayer entitylivingbaseIn, float partialTickTime)
    {
    	super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        float f = 0.9375F;
        GlStateManager.scalef(f, f, f);
    }

    public void renderRightArm(EntityDoolPlayer clientPlayer)
    {
        float f = 1.0F;
        GlStateManager.color3f(f, f, f);
        ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        modelplayer.swingProgress = 0.0F;
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
        modelplayer.postRenderArm(0.0625F,EnumHandSide.LEFT);
        modelplayer.postRenderArm(0.0625F,EnumHandSide.RIGHT);
    }

    public void renderLeftArm(EntityDoolPlayer clientPlayer)
    {
        float f = 1.0F;
        GlStateManager.color3f(f, f, f);
        ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        modelplayer.isSneak = false;
        modelplayer.swingProgress = 0.0F;
        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
        modelplayer.postRenderArm(0.0625F,EnumHandSide.LEFT);
        modelplayer.postRenderArm(0.0625F,EnumHandSide.RIGHT);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityDoolPlayer entity) {
        return ((EntityDoolPlayer)entity).getLocationSkin();
	}

	@Override
	public void renderName(EntityDoolPlayer entity, double x, double y, double z)
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