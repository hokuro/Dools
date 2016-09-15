package basashi.dools.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import basashi.dools.container.ContainerItemSelect;
import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiItemSelect extends GuiContainer {

	protected static final ResourceLocation ftexGui = new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tab_items.png");
	protected static final ResourceLocation ftexTab = new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tabs.png");

	public static InventoryBasic inventory = new InventoryBasic("tmp", false, 45);
	public static InventoryBasic inventoryItem = new InventoryBasic("sel", false, 9);

	private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();

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
		InventoryPlayer var6 = this.mc.thePlayer.inventory;
		ItemStack var7 = var6.getItemStack();
		ModLog.log().debug("handolMouse.(" + par2 + par3 + par4.toString() +")");

		if (par1Slot != null) {
			if (var5) {
				// Shiftが押されている
				if (var7 != null) {
					var6.setItemStack(par1Slot.getStack());
					par1Slot.putStack(var7);
				} else {
					var7 = par1Slot.getStack();
					if (var7 != null) {
						var7 = var7.splitStack(1);
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
					if (var6.getItemStack() == null && par1Slot.getHasStack()) {
						var9 = par1Slot.getStack().copy();
						var9.stackSize = var9.getMaxStackSize();
						var6.setItemStack(var9);
					}

					return;
				}

				if (var7 != null && var8 != null && var7.isItemEqual(var8)) {
					if (par3 == 0) {
						if (var5) {
							var7.stackSize = var7.getMaxStackSize();
						} else if (var7.stackSize < var7.getMaxStackSize()) {
							++var7.stackSize;
						}
					} else if (var7.stackSize <= 1) {
						var6.setItemStack((ItemStack) null);
					} else {
						--var7.stackSize;
					}
				} else if (var8 != null && var7 == null) {
					var6.setItemStack(ItemStack.copyItemStack(var8));
					var7 = var6.getItemStack();

					if (var5) {
						var7.stackSize = var7.getMaxStackSize();
					}
				} else {
					var6.setItemStack((ItemStack) null);
				}
			} else {
				// 装備品欄
				//this.inventorySlots.retrySlotClick(par1Slot == null ? par2 : par1Slot.slotNumber, par3, true, this.mc.thePlayer);
				this.inventorySlots.func_184996_a(par1Slot == null ? par2 : par1Slot.slotNumber, par3, par4, this.mc.thePlayer);
			}
		} else {
			// スロットをクリックしなかった
			var6.setItemStack((ItemStack) null);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.searchField = new GuiTextField(0, this.fontRendererObj,
				this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
		this.searchField.setMaxStringLength(15);
		this.searchField.setEnableBackgroundDrawing(false);
		this.searchField.setVisible(false);
		this.searchField.setTextColor(16777215);
		int var1 = selectedTabIndex;
		selectedTabIndex = -1;
		this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[var1]);
		this.field_82324_x = new CreativeCrafting(this.mc);
		this.mc.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_82324_x);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null) {
			this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_82324_x);
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
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
			if (Keyboard.isKeyDown(this.mc.gameSettings.keyBindChat.getKeyCode())) {
				this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
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

			if (!this.checkHotbarKeys(par2)) {
				if (this.searchField.textboxKeyTyped(par1, par2)) {
					this.updateCreativeSearch();
				} else {
					try {
						super.keyTyped(par1, par2);
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void updateCreativeSearch() {
		ContainerItemSelect lcontainer = (ContainerItemSelect)inventorySlots;
		lcontainer.itemList.clear();
		List<Item> list = new ArrayList<Item>();
		for (ResourceLocation key : Item.itemRegistry.getKeys()){
			list.add(Item.itemRegistry.getObject(key));
		}
		Object[] var2 = list.toArray();
		int var3 = var2.length;
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			Item var5 = (Item)var2[var4];

			if (var5 != null && var5.getCreativeTab() != null) {
				var5.getSubItems(var5, (CreativeTabs) null, lcontainer.itemList);
			}
		}

//		Enchantment[] var8 = Enchantment.enchantmentsBookList;
//		var3 = var8.length;
		Iterator<Enchantment> ent = Enchantment.enchantmentRegistry.iterator();
		while(ent.hasNext()){
			Enchantment var12 = ent.next();
			if (var12 != null && var12.type != null) {
				Items.enchanted_book.getAll(var12, lcontainer.itemList);
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
			Iterator var6 = var11.getTooltip(this.mc.thePlayer,
					this.mc.gameSettings.advancedItemTooltips).iterator();

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
		CreativeTabs lct = CreativeTabs.creativeTabArray[selectedTabIndex];

		if (lct.drawInForegroundOfTab()) {
			this.fontRendererObj.drawString(I18n.format(lct.getTranslatedTabLabel()), 8, 6, 4210752);
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 0) {
			int var4 = par1 - this.guiLeft;
			int var5 = par2 - this.guiTop;
			CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
			int var7 = var6.length;

			for (int var8 = 0; var8 < var7; ++var8) {
				CreativeTabs var9 = var6[var8];
				if (var9 == CreativeTabs.tabInventory) {
					continue;
				}
				if (this.func_147049_a(var9, var4, var5)) {
					this.setCurrentCreativeTab(var9);
					return;
				}
			}
		}

		try {
			super.mouseClicked(par1, par2, par3);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private boolean needsScrollBars() {
		return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()
				&& CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory()
				&& ((ContainerItemSelect) this.inventorySlots).hasMoreThan1PageOfItemsInList();
	}

	private void setCurrentCreativeTab(CreativeTabs par1CreativeTabs) {
		int var2 = selectedTabIndex;
		selectedTabIndex = par1CreativeTabs.getTabIndex();
		ContainerItemSelect var3 = (ContainerItemSelect) this.inventorySlots;
		var3.itemList.clear();
		par1CreativeTabs.displayAllRelevantItems(var3.itemList);  //displayAllReleventItems(var3.itemList);

		if (this.searchField != null) {
			if (par1CreativeTabs == CreativeTabs.tabAllSearch) {
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
	public void handleMouseInput() {
		try {
			super.handleMouseInput();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		int var1 = Mouse.getEventDWheel();

		if (var1 != 0 && this.needsScrollBars()) {
			int var2 = ((ContainerItemSelect) this.inventorySlots).itemList.size() / 9 - 5 + 1;

			if (var1 > 0) {
				var1 = 1;
			}

			if (var1 < 0) {
				var1 = -1;
			}

			this.currentScroll = (float) ((double) this.currentScroll - (double) var1
					/ (double) var2);

			if (this.currentScroll < 0.0F) {
				this.currentScroll = 0.0F;
			}

			if (this.currentScroll > 1.0F) {
				this.currentScroll = 1.0F;
			}

			((ContainerItemSelect) this.inventorySlots).scrollTo(this.currentScroll);
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		boolean var4 = Mouse.isButtonDown(0);
		int var5 = this.guiLeft;
		int var6 = this.guiTop;
		int var7 = var5 + 175;
		int var8 = var6 + 18;
		int var9 = var7 + 14;
		int var10 = var8 + 112;

		if (!this.wasClicking && var4 && par1 >= var7 && par2 >= var8
				&& par1 < var9 && par2 < var10) {
			this.isScrolling = this.needsScrollBars();
		}

		if (!var4) {
			this.isScrolling = false;
		}

		this.wasClicking = var4;

		if (this.isScrolling) {
			this.currentScroll = ((float) (par2 - var8) - 7.5F)
					/ ((float) (var10 - var8) - 15.0F);

			if (this.currentScroll < 0.0F) {
				this.currentScroll = 0.0F;
			}

			if (this.currentScroll > 1.0F) {
				this.currentScroll = 1.0F;
			}

			((ContainerItemSelect) this.inventorySlots).scrollTo(this.currentScroll);
		}

		super.drawScreen(par1, par2, par3);
		CreativeTabs[] var11 = CreativeTabs.creativeTabArray;
		int var12 = var11.length;

		for (int var13 = 0; var13 < var12; ++var13) {
			CreativeTabs lct = var11[var13];
			if (lct == CreativeTabs.tabInventory) {
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
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		CreativeTabs var5 = CreativeTabs.creativeTabArray[selectedTabIndex];
		CreativeTabs[] var7 = CreativeTabs.creativeTabArray;
		int var8 = var7.length;
		int var9;

		for (var9 = 0; var9 < var8; ++var9) {
			CreativeTabs var10 = var7[var9];
			if (var10 == CreativeTabs.tabInventory) {
				continue;
			}
			Minecraft.getMinecraft().getTextureManager().bindTexture(ftexTab);

			if (var10.getTabIndex() != selectedTabIndex) {
				this.renderCreativeTab(var10);
			}
		}

		Minecraft.getMinecraft().getTextureManager().bindTexture(ftexGui);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		this.searchField.drawTextBox();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var11 = this.guiLeft + 175;
		var8 = this.guiTop + 18;
		var9 = var8 + 112;
		Minecraft.getMinecraft().getTextureManager().bindTexture(ftexTab);

		if (var5.shouldHidePlayerInventory()) {
			this.drawTexturedModalRect(var11, var8
					+ (int) ((float) (var9 - var8 - 17) * this.currentScroll),
					232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		}

		this.renderCreativeTab(var5);
	}

	protected boolean func_147049_a(CreativeTabs par1CreativeTabs, int par2, int par3) {
		int var4 = par1CreativeTabs.getTabColumn();
		int var5 = 28 * var4;
		byte var6 = 0;

		if (var4 == 5) {
			var5 = this.xSize - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (par1CreativeTabs.isTabInFirstRow()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + this.ySize;
		}

		return par2 >= var5 && par2 <= var5 + 28 && par3 >= var7
				&& par3 <= var7 + 32;
	}

	protected boolean renderCreativeInventoryHoveringText(CreativeTabs par1CreativeTabs, int par2, int par3) {
		int var4 = par1CreativeTabs.getTabColumn();
		int var5 = 28 * var4;
		byte var6 = 0;

		if (var4 == 5) {
			var5 = this.xSize - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (par1CreativeTabs.isTabInFirstRow()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + this.ySize;
		}

		if (this.isPointInRegion(var5 + 3, var7 + 3, 23, 27, par2, par3)) {
			this.drawCreativeTabHoveringText(
					par1CreativeTabs.getTranslatedTabLabel(), par2, par3);
			return true;
		} else {
			return false;
		}
	}

	protected void renderCreativeTab(CreativeTabs par1CreativeTabs) {
		boolean var2 = par1CreativeTabs.getTabIndex() == selectedTabIndex;
		boolean var3 = par1CreativeTabs.isTabInFirstRow();
		int var4 = par1CreativeTabs.getTabColumn();
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
		RenderItem itemRenderer = Dools.getPrivateValue(GuiScreen.class, this, "itemRender");
		itemRenderer.zLevel = 100.0F;
		var7 += 6;
		var8 += 8 + (var3 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		ItemStack var10 = new ItemStack(par1CreativeTabs.getTabIconItem());
		itemRenderer.renderItemAndEffectIntoGUI(var10, var7, var8);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj,var10, var7, var8,"");


		GL11.glDisable(GL11.GL_LIGHTING);
		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(new GuiAchievements(this,this.mc.thePlayer.getStatFileWriter()));
		}

		if (par1GuiButton.id == 1) {
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
		}
	}

	public int getSelectedTabIndex() {
		return selectedTabIndex;
	}

	/**
	 * 選択用のインベントリをクリア
	 */
	public static void clearInventory() {
		for (int li = 0; li < inventoryItem.getSizeInventory(); li++) {
			inventoryItem.setInventorySlotContents(li, null);
		}
	}

}
