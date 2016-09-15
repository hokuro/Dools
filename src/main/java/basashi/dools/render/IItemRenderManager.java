package basashi.dools.render;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IItemRenderManager {

	public static final int VM_FIRST_PERSON		= 0;
	public static final int VM_THERD_PERSON		= 1;
	public static final int VM_THERD_PERSON_INV	= 2;


	/**
	 * アイテムの描画のみ、位置補正はしない。
	 * @param pEntity
	 * @param pItemStack
	 * @param pIndex
	 * @return
	 */
	public boolean renderItem(Entity pEntity, ItemStack pItemStack, int pIndex);
	public boolean renderItemInFirstPerson(Entity pEntity, ItemStack pItemStack, float pDeltaTimepRenderPhatialTick);
	public boolean renderItemWorld(ItemStack pItemStack);
	public ResourceLocation getRenderTexture(ItemStack pItemStack);
	public boolean isRenderItem(ItemStack pItemStack);
	public boolean isRenderItemInFirstPerson(ItemStack pItemStack);
	public boolean isRenderItemWorld(ItemStack pItemStack);

}