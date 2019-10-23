package basashi.dools.render;

import com.mojang.blaze3d.platform.GlStateManager;

import basashi.dools.entity.EntityDoolPlayer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EntityPlayerRender extends LivingRenderer<EntityDoolPlayer,PlayerModel<EntityDoolPlayer>>
{
    /** this field is used to indicate the 3-pixel wide arms */
    private boolean smallArms;
    private HeadLayer customHed;

    public EntityPlayerRender(EntityRendererManager renderManager) {
        this(renderManager, false);
    }

    public EntityPlayerRender(EntityRendererManager renderManager, boolean useSmallArms)  {
        super(renderManager, new PlayerModel<>(0.0F, useSmallArms), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ArrowLayer<>(this));
        this.addLayer(new LayerDool5Head(this));
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new LayerDoolParrotVariant<EntityDoolPlayer>(this));
        this.addLayer(new SpinAttackEffectLayer<>(this));
    }

    public PlayerModel getMainModel() {
        return (PlayerModel)super.getEntityModel();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityDoolPlayer entity, double x, double y, double z, float entityYaw, float partialTicks)  {
        double d0 = y;
        if (entity.isSneaking()) {
            d0 = y - 0.125D;
        }
        if ( entity.isSlim() != smallArms){
        	smallArms = entity.isSlim();
        	this.entityModel = new PlayerModel(0.0F, smallArms);
        }

        this.setModelVisibilities(entity);
        super.doRender(entity, x, d0, z, entityYaw, partialTicks);
   }

    private void setModelVisibilities(EntityDoolPlayer clientPlayer) {
        PlayerModel<EntityDoolPlayer> modelplayer = this.getMainModel();

        if (clientPlayer.isSpectator) {
            modelplayer.setVisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        } else {
        	ItemStack itemstack = clientPlayer.getHeldItemMainhand();
        	ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
            modelplayer.setVisible(true);
            modelplayer.bipedHeadwear.showModel = clientPlayer.isHat;
            modelplayer.bipedBodyWear.showModel = clientPlayer.isJacket;
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isLeftLeg;
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isRightLeg;
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isLeftSleeve;
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isRightSleeve;
            modelplayer.isSneak = clientPlayer.isSneaking();
            BipedModel.ArmPose bipedmodel$armpose = this.func_217766_a(clientPlayer, itemstack, itemstack1, Hand.MAIN_HAND);
            BipedModel.ArmPose bipedmodel$armpose1 = this.func_217766_a(clientPlayer, itemstack, itemstack1, Hand.OFF_HAND);

            if (clientPlayer.getPrimaryHand() == HandSide.RIGHT) {
            	modelplayer.rightArmPose = bipedmodel$armpose;
            	modelplayer.leftArmPose = bipedmodel$armpose1;
             } else {
            	 modelplayer.rightArmPose = bipedmodel$armpose1;
            	 modelplayer.leftArmPose = bipedmodel$armpose;
             }
        }
    }

    private BipedModel.ArmPose func_217766_a(EntityDoolPlayer p_217766_1_, ItemStack p_217766_2_, ItemStack p_217766_3_, Hand p_217766_4_) {
        BipedModel.ArmPose bipedmodel$armpose = BipedModel.ArmPose.EMPTY;
        ItemStack itemstack = p_217766_4_ == Hand.MAIN_HAND ? p_217766_2_ : p_217766_3_;
        if (!itemstack.isEmpty()) {
           bipedmodel$armpose = BipedModel.ArmPose.ITEM;
           if (p_217766_1_.getItemInUseCount() > 0) {
              UseAction useaction = itemstack.getUseAction();
              if (useaction == UseAction.BLOCK) {
                 bipedmodel$armpose = BipedModel.ArmPose.BLOCK;
              } else if (useaction == UseAction.BOW) {
                 bipedmodel$armpose = BipedModel.ArmPose.BOW_AND_ARROW;
              } else if (useaction == UseAction.SPEAR) {
                 bipedmodel$armpose = BipedModel.ArmPose.THROW_SPEAR;
              } else if (useaction == UseAction.CROSSBOW && p_217766_4_ == p_217766_1_.getActiveHand()) {
                 bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_CHARGE;
              }
           } else {
              boolean flag3 = p_217766_2_.getItem() == Items.CROSSBOW;
              boolean flag = CrossbowItem.isCharged(p_217766_2_);
              boolean flag1 = p_217766_3_.getItem() == Items.CROSSBOW;
              boolean flag2 = CrossbowItem.isCharged(p_217766_3_);
              if (flag3 && flag) {
                 bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
              }

              if (flag1 && flag2 && p_217766_2_.getItem().getUseAction(p_217766_2_) == UseAction.NONE) {
                 bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
              }
           }
        }

        return bipedmodel$armpose;
     }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(LivingEntity entity) {
        return ((EntityDoolPlayer)entity).getLocationSkin();
    }


    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityDoolPlayer entitylivingbaseIn, float partialTickTime) {
    	super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        float f = 0.9375F;
        GlStateManager.scalef(f, f, f);
    }

    public void renderRightArm(EntityDoolPlayer clientPlayer) {
        float f = 1.0F;
        GlStateManager.color3f(f, f, f);
        PlayerModel modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        modelplayer.swingProgress = 0.0F;
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(clientPlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        modelplayer.postRenderArm(0.0625F,HandSide.LEFT);
        modelplayer.postRenderArm(0.0625F,HandSide.RIGHT);
    }

    public void renderLeftArm(EntityDoolPlayer clientPlayer)  {
        float f = 1.0F;
        GlStateManager.color3f(f, f, f);
        PlayerModel modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        modelplayer.isSneak = false;
        modelplayer.swingProgress = 0.0F;
        modelplayer.setRotationAngles(clientPlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        modelplayer.postRenderArm(0.0625F,HandSide.LEFT);
        modelplayer.postRenderArm(0.0625F,HandSide.RIGHT);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityDoolPlayer entity) {
        return ((EntityDoolPlayer)entity).getLocationSkin();
	}

	@Override
	public void renderName(EntityDoolPlayer entity, double x, double y, double z)
    {

    }
}
