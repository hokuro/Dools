package basashi.dools.core;

import basashi.dools.entity.EntityDool;
import basashi.dools.gui.GuiDoolPause;
import basashi.dools.render.IItemRenderManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CommonProxy implements IItemRenderManager  {
	public static void setZoomRate() {
	}

	public void clientCustomPayload(NetHandlerPlayClient var1, CPacketCustomPayload var2) {
	}

	public void sendToServer(Packet pPacket) {

	}

	/**
	 * 独自のGUIを獲得する。
	 */
	public GuiDoolPause getGui(EntityDool pEntity) {
		return null;
	}

	public boolean callAfterRender(EntityDool pEntity) {
		return false;
	}

	public void initEntitys() {

	}

	/**
	 * サーバーにフィギュア固有データを要求する。
	 */
	public void getDoolData(Entity pEntity) {
	}

	public void openGuiSelect(EntityPlayer pEntity, World pWorld) {
	}

	public void openGuiPause(EntityPlayer pPlayer, EntityDool pFigure,World pWorld) {
	}

	@Override
	public ResourceLocation getRenderTexture(ItemStack pItemStack) {
		return null;
	}


	public ThreadDownloadImageData getDownloadImageSkin(ResourceLocation par0ResourceLocation, String par1Str, int kind) {
		return null;
	}

	private ThreadDownloadImageData func_110301_a(ResourceLocation par0ResourceLocation, String par1Str,ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer) {
		return null;
	}

	public String func_110300_d(String par0Str) {
		return null;
	}

	public ResourceLocation func_110311_f(String par0Str) {
		return null;
	}

	@Override
	public boolean renderItemWorld(ItemStack pItemStack) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isRenderItemWorld(ItemStack pItemStack) {
		return pItemStack.func_77952_i() != 0;
	}

	@Override
	public boolean isRenderItem(ItemStack pItemStack) {
		// TODO Auto-generated method stub
		return pItemStack.func_77952_i() != 0;
	}

	@Override
	public boolean renderItem(Entity pEntity, ItemStack pItemStack, int pIndex) {
		return true;
	}

	@Override
	public boolean renderItemInFirstPerson(Entity pEntity, ItemStack pItemStack, float pDeltaTimepRenderPhatialTick) {
		return false;
	}

	@Override
	public boolean isRenderItemInFirstPerson(ItemStack pItemStack) {
		return false;
	}

	public void registerRender() {
		// TODO 自動生成されたメソッド・スタブ

	}


}
