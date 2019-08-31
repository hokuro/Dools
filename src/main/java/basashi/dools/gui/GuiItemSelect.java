package basashi.dools.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import basashi.dools.container.ContainerItemSelect;
import basashi.dools.core.log.ModLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiItemSelect extends GuiContainer {

	protected static final ResourceLocation ftexGui = new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tab_items.png");
	protected static final ResourceLocation ftexTab = new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tabs.png");

	public static InventoryBasic inventory = new InventoryBasic(new TextComponentTranslation("tmp"), 45);
	public static InventoryBasic inventoryItem = new InventoryBasic(new TextComponentTranslation("sel"), 9);

	private static int selectedTabIndex = ItemGroup.BUILDING_BLOCKS.getIndex();

	private float currentScroll = 0.0F;

	private boolean isScrolling = false;

	private boolean wasClicking;
	private GuiTextField searchField;

	private List backupContainerSlots;
	private Slot field_74235_v = null;
	private boolean field_74234_w = false;
	private CreativeCrafting field_82324_x;
	protected GuiScreen ownerScreen;
	protected EntityLivingBase target;
	public static boolean isChange = false;


	public GuiItemSelect(GuiScreen pOwner, EntityLivingBase pTarget, EntityPlayer pPlayer) {
		super(new ContainerItemSelect(pPlayer));
		this.allowUserInput = true;
		this.ySize = 136;
		this.xSize = 195;
		ownerScreen = pOwner;
		target = pTarget;
	}

	@Override
    protected void handleMouseClick(Slot par1Slot, int par2, int par3, ClickType par4){
	//protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4) {
		this.field_74234_w = true;
		boolean var5 = par4 == ClickType.QUICK_MOVE; // Shift
		InventoryPlayer var6 = this.mc.player.inventory;
		ItemStack var7 = var6.getItemStack();
		ModLog.log().debug("handolMouse.(" + par2 + par3 + par4.toString() +")");

		if (par1Slot != null) {
			if (var5) {
				// Shiftが押されている
				if (!var7.isEmpty()) {
					var6.setItemStack(par1Slot.getStack());
					par1Slot.putStack(var7);
				} else {
					var7 = par1Slot.getStack();
					if (!var7.isEmpty()) {
						var7 = var7.split(1);
					}
				}
			} else if (par1Slot.inventory == inventory) {
				// Creativeエリアの処理
				ItemStack var8 = par1Slot.getStack();
				ItemStack var9;

				if (par4 == ClickType.SWAP) {
					return;
				}

				if (par4 == ClickType.CLONE) {
					if (var6.getItemStack().isEmpty() && par1Slot.getHasStack()) {
						var9 = par1Slot.getStack().copy();
						var9.setCount(var9.getMaxStackSize());
						var6.setItemStack(var9);
					}

					return;
				}

				if (!var7.isEmpty() && !var8.isEmpty() && var7.isItemEqual(var8)) {
					if (par3 == 0) {
						if (var5) {
							var7.setCount(var7.getMaxStackSize());
						} else if (var7.getCount() < var7.getMaxStackSize()) {
							var7.grow(1);;
						}
					} else if (var7.getCount() <= 1) {
						var6.setItemStack(ItemStack.EMPTY);
					} else {
						var7.grow(1);
					}
				} else if (!var8.isEmpty() && var7.isEmpty()) {
					var6.setItemStack(var8.copy());
					var7 = var6.getItemStack();

					if (var5) {
						var7.setCount(var7.getMaxStackSize());
					}
				} else {
					var6.setItemStack(ItemStack.EMPTY);
				}
			} else {
				// 装備品欄
				//this.inventorySlots.retrySlotClick(par1Slot == null ? par2 : par1Slot.slotNumber, par3, true, this.mc.thePlayer);
				inventorySlots.slotClick(par1Slot == null ? par2 : par1Slot.slotNumber, par3, par4, this.mc.player);
			}
		} else {
			// スロットをクリックしなかった
			var6.setItemStack(ItemStack.EMPTY);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttons.clear();
		this.mc.keyboardListener.enableRepeatEvents(true);
		this.searchField = new GuiTextField(0, this.fontRenderer,
				this.guiLeft + 82, this.guiTop + 6, 89, this.fontRenderer.FONT_HEIGHT);
		this.searchField.setMaxStringLength(15);
		this.searchField.setEnableBackgroundDrawing(false);
		this.searchField.setVisible(false);
		this.searchField.setTextColor(16777215);
		int var1 = selectedTabIndex;
		selectedTabIndex = -1;
		this.setCurrentCreativeTab(ItemGroup.GROUPS[var1]);
		this.field_82324_x = new CreativeCrafting(this.mc);
		this.mc.player.inventoryContainer.addListener(this.field_82324_x);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		if (this.mc.player != null && this.mc.player.inventory != null) {
			this.mc.player.inventoryContainer.removeListener(this.field_82324_x);
		}
		isChange = true;
		target.setItemStackToSlot(EntityEquipmentSlot.MAINHAND,
				inventoryItem.getStackInSlot(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.MAINHAND)));
		target.setItemStackToSlot(EntityEquipmentSlot.OFFHAND,
				inventoryItem.getStackInSlot(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.OFFHAND)));
		target.setItemStackToSlot(EntityEquipmentSlot.FEET,
				 inventoryItem.getStackInSlot(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.FEET)));
		target.setItemStackToSlot(EntityEquipmentSlot.LEGS,
				inventoryItem.getStackInSlot(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.LEGS)));
		target.setItemStackToSlot(EntityEquipmentSlot.CHEST,
				 inventoryItem.getStackInSlot(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.CHEST)));
		target.setItemStackToSlot(EntityEquipmentSlot.HEAD,
				 inventoryItem.getStackInSlot(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.HEAD)));

		this.mc.keyboardListener.enableRepeatEvents(true);
	}

	@Override
	public boolean charTyped(char par1, int par2) {
		if (selectedTabIndex != ItemGroup.SEARCH.getColumn()) {
			if (this.mc.gameSettings.keyBindChat.isPressed()) {
				this.setCurrentCreativeTab(ItemGroup.SEARCH);
			} else {
				if (par2 == 1) {
					mc.displayGuiScreen(ownerScreen);
				}
			}
		} else {
			if (this.field_74234_w) {
				this.field_74234_w = false;
				this.searchField.setText("");
			}

			if (!this.func_195363_d(par1,par2)) {
				if (this.searchField.charTyped(par1, par2)) {
					this.updateCreativeSearch();
				} else {
					super.charTyped(par1, par2);
				}
			}
		}
		return true;
	}

	private void updateCreativeSearch() {
		ContainerItemSelect lcontainer = (ContainerItemSelect)inventorySlots;
		lcontainer.itemList.clear();
		List<Item> list = new ArrayList<Item>();
		IRegistry.field_212630_s.forEach((itm)->{
			list.add(itm);
		});
		Object[] var2 = list.toArray();
		int var3 = var2.length;
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			Item var5 = (Item)var2[var4];

			NonNullList<ItemStack> stacks =  NonNullList.withSize(lcontainer.itemList.size(),ItemStack.EMPTY);
			stacks.addAll(lcontainer.itemList);
//			if (var5 != null && var5.getGroup() != null) {
//				var5.getSubItems((ItemGroup) null,  stacks);
//			}
		}

//		Enchantment[] var8 = Enchantment.enchantmentsBookList;
//		var3 = var8.length;

		Iterator<Enchantment> ent = IRegistry.field_212628_q.iterator();
		while(ent.hasNext()){
			Enchantment enchantment = ent.next();
			if (enchantment != null && enchantment.type != null) {
		        for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i)
		        {
		        	lcontainer.itemList.add( ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment, i)));
		        }
			}
		}
//		for (var4 = 0; var4 < var3; ++var4) {
//			Enchantment var12 = var8[var4];
//
//			if (var12 != null && var12.type != null) {
//				Items.enchanted_book.getAll(var12, lcontainer.itemList);
//			}
//		}

		Iterator var9 = lcontainer.itemList.iterator();
		String var10 = this.searchField.getText().toLowerCase();

		while (var9.hasNext()) {
			ItemStack var11 = (ItemStack) var9.next();
			boolean var13 = false;
			Iterator var6 = var11.getTooltip(this.mc.player,
					this.mc.gameSettings.advancedItemTooltips?TooltipFlags.ADVANCED:TooltipFlags.NORMAL).iterator();

			while (true) {
				if (var6.hasNext()) {
					String var7 = (String) var6.next();

					if (!var7.toLowerCase().contains(var10)) {
						continue;
					}

					var13 = true;
				}

				if (!var13) {
					var9.remove();
				}

				break;
			}
		}

		this.currentScroll = 0.0F;
		lcontainer.scrollTo(0.0F);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		ItemGroup lct = ItemGroup.GROUPS[selectedTabIndex];

		if (lct.drawInForegroundOfTab()) {
			this.fontRenderer.drawString(I18n.format(I18n.format(lct.getTranslationKey())), 8, 6, 4210752);
		}
	}



	private boolean needsScrollBars() {
		return selectedTabIndex != ItemGroup.INVENTORY.getIndex()
				&& ItemGroup.GROUPS[selectedTabIndex].hasScrollbar()
				&& ((ContainerItemSelect) this.inventorySlots).hasMoreThan1PageOfItemsInList();
	}

	private void setCurrentCreativeTab(ItemGroup par1ItemGroup) {
		int var2 = selectedTabIndex;
		selectedTabIndex = par1ItemGroup.getIndex();
		ContainerItemSelect var3 = (ContainerItemSelect) this.inventorySlots;
		var3.itemList.clear();
        NonNullList<ItemStack> stacks = NonNullList.<ItemStack>create();
		par1ItemGroup.fill(stacks);  //displayAllReleventItems(var3.itemList);
		for (ItemStack st : stacks){
			var3.itemList.add(st);
		}

		if (this.searchField != null) {
			if (par1ItemGroup == ItemGroup.SEARCH) {
				this.searchField.setVisible(true);
				this.searchField.setCanLoseFocus(false);
				this.searchField.setFocused(true);
				this.searchField.setText("");
				this.updateCreativeSearch();
			} else {
				this.searchField.setVisible(false);
				this.searchField.setCanLoseFocus(true);
				this.searchField.setFocused(false);
			}
		}

		this.currentScroll = 0.0F;
		var3.scrollTo(0.0F);
	}

	@Override
	 public boolean mouseClicked(double par1, double par2, int par3) {
		if (par3 == 0) {
			double var4 = par1 - this.guiLeft;
			double var5 = par2 - this.guiTop;
			ItemGroup[] var6 = ItemGroup.GROUPS;
			int var7 = var6.length;

			for (int var8 = 0; var8 < var7; ++var8) {
				ItemGroup var9 = var6[var8];
				if (var9 == ItemGroup.INVENTORY) {
					continue;
				}
				if (this.func_147049_a(var9, var4, var5)) {
					this.setCurrentCreativeTab(var9);
				}
			}
		}
		int var5 = this.guiLeft;
		int var6 = this.guiTop;
		int var7 = var5 + 175;
		int var8 = var6 + 18;
		int var9 = var7 + 14;
		int var10 = var8 + 112;

		if (!this.wasClicking && par1 >= var7 && par2 >= var8 && par1 < var9 && par2 < var10) {
			this.isScrolling = this.needsScrollBars();
		}
		return super.mouseClicked(par1, par2, par3);
	}

	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (this.isScrolling) {
			int i = this.guiTop + 18;
			int j = i + 112;
            this.currentScroll = ((float)(p_mouseDragged_3_ - i) - 7.5F) / ((float)(j - i) - 15.0F);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
			((ContainerItemSelect) this.inventorySlots).scrollTo(this.currentScroll);
			return true;
		} else {
			return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
		}
	}

	@Override
	public void render(int par1, int par2, float par3) {
		//boolean var4 = Mouse.isButtonDown(0);
		int var5 = this.guiLeft;
		int var6 = this.guiTop;
		int var7 = var5 + 175;
		int var8 = var6 + 18;
		int var9 = var7 + 14;
		int var10 = var8 + 112;



		super.render(par1, par2, par3);
		ItemGroup[] var11 = ItemGroup.GROUPS;
		int var12 = var11.length;

		for (int var13 = 0; var13 < var12; ++var13) {
			ItemGroup lct = var11[var13];
			if (lct == ItemGroup.INVENTORY) {
				continue;
			}

			if (this.renderCreativeInventoryHoveringText(lct, par1, par2)) {
				break;
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		if (p_mouseReleased_5_ == 0) {
			double d0 = p_mouseReleased_1_ - (double)this.guiLeft;
			double d1 = p_mouseReleased_3_ - (double)this.guiTop;
			this.isScrolling = false;
			return  super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);//((InventoryOriginalMenu)((ContainerOriginalWorkBench)this.inventorySlots).getMenu()).needScroll();
		}
		return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		ItemGroup var5 = ItemGroup.GROUPS[selectedTabIndex];
		ItemGroup[] var7 = ItemGroup.GROUPS;
		int var8 = var7.length;
		int var9;

		for (var9 = 0; var9 < var8; ++var9) {
			ItemGroup var10 = var7[var9];
			if (var10 == ItemGroup.INVENTORY) {
				continue;
			}
			Minecraft.getInstance().getTextureManager().bindTexture(ftexTab);

			if (var10.getIndex() != selectedTabIndex) {
				this.renderCreativeTab(var10);
			}
		}

		Minecraft.getInstance().getTextureManager().bindTexture(ftexGui);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		this.searchField.drawTextField(par2,par3,par1);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var11 = this.guiLeft + 175;
		var8 = this.guiTop + 18;
		var9 = var8 + 112;
		Minecraft.getInstance().getTextureManager().bindTexture(ftexTab);

		if (var5.hasSearchBar()) {
			this.drawTexturedModalRect(var11, var8
					+ (int) ((float) (var9 - var8 - 17) * this.currentScroll),
					232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		}

		this.renderCreativeTab(var5);
	}

	protected boolean func_147049_a(ItemGroup par1ItemGroup, double par2, double par3) {
		int var4 = par1ItemGroup.getColumn();
		int var5 = 28 * var4;
		byte var6 = 0;

		if (var4 == 5) {
			var5 = this.xSize - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (par1ItemGroup.isOnTopRow()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + this.ySize;
		}

		return par2 >= var5 && par2 <= var5 + 28 && par3 >= var7
				&& par3 <= var7 + 32;
	}

	protected boolean renderCreativeInventoryHoveringText(ItemGroup par1ItemGroup, int par2, int par3) {
		int var4 = par1ItemGroup.getColumn();
		int var5 = 28 * var4;
		byte var6 = 0;

		if (var4 == 5) {
			var5 = this.xSize - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (par1ItemGroup.isOnTopRow()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + this.ySize;
		}

		if (this.isPointInRegion(var5 + 3, var7 + 3, 23, 27, par2, par3)) {
			this.drawHoveringText(
					I18n.format(par1ItemGroup.getTranslationKey()), par2, par3);
			return true;
		} else {
			return false;
		}
	}

	protected void renderCreativeTab(ItemGroup par1ItemGroup) {
		boolean var2 = par1ItemGroup.getIndex() == selectedTabIndex;
		boolean var3 = par1ItemGroup.isOnTopRow();
		int var4 = par1ItemGroup.getColumn();
		int var5 = var4 * 28;
		int var6 = 0;
		int var7 = this.guiLeft + 28 * var4;
		int var8 = this.guiTop;
		byte var9 = 32;

		if (var2) {
			var6 += 32;
		}

		if (var4 == 5) {
			var7 = this.guiLeft + this.xSize - 28;
		} else if (var4 > 0) {
			var7 += var4;
		}

		if (var3) {
			var8 -= 28;
		} else {
			var6 += 64;
			var8 += this.ySize - 4;
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		this.drawTexturedModalRect(var7, var8, var5, var6, 28, var9);
		this.zLevel = 100.0F;
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		itemRenderer.zLevel = 100.0F;
		var7 += 6;
		var8 += 8 + (var3 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		ItemStack var10 = par1ItemGroup.getIcon().copy();
		itemRenderer.renderItemAndEffectIntoGUI(var10, var7, var8);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer,var10, var7, var8,"");


		GL11.glDisable(GL11.GL_LIGHTING);
		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
//		if (par1GuiButton.id == 0) {
//			this.mc.displayGuiScreen(new GuiAchievements(this,this.mc.player.getStatFileWriter()));
//		}
//
//		if (par1GuiButton.id == 1) {
//			this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
//		}
	}

	public int getSelectedTabIndex() {
		return selectedTabIndex;
	}

	/**
	 * 選択用のインベントリをクリア
	 */
	public static void clearInventory() {
		for (int li = 0; li < inventoryItem.getSizeInventory(); li++) {
			inventoryItem.setInventorySlotContents(li, ItemStack.EMPTY);
		}
	}

}
