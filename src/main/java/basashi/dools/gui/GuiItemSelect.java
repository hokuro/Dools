package basashi.dools.gui;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;

import basashi.dools.container.ContainerItemSelect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeCraftingListener;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.client.util.ISearchTree;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiItemSelect extends ContainerScreen<ContainerItemSelect> {

	protected static final ResourceLocation ftexGui = new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tab_items.png");
	protected static final ResourceLocation ftexTab = new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tabs.png");

	private static int selectedTabIndex = ItemGroup.BUILDING_BLOCKS.getIndex();
	private static final Inventory TMP_INVENTORY = new Inventory(45);
	private static final Inventory ITM_INVENTORY = new Inventory(9);

	private float currentScroll = 0.0F;
	private boolean isScrolling = false;

	private boolean wasClicking;
	private TextFieldWidget searchField;
	private List<Slot> originalSlots;
	private Slot destroyItemSlot;
	private CreativeCraftingListener listener;
	private boolean field_195377_F;
	private boolean field_199506_G;
	private final Map<ResourceLocation, Tag<Item>> tagSearchResults = Maps.newTreeMap();
	private static int tabPage = 0;
	private int maxPages = 0;

	private List backupContainerSlots;
	private Slot field_74235_v = null;
	private boolean field_74234_w = false;
	private CreativeCraftingListener field_82324_x;
	protected LivingEntity target;
	public static boolean isChange = false;


	public GuiItemSelect(PlayerInventory playerInv, LivingEntity target) {
		super(new ContainerItemSelect(playerInv, target), playerInv, new StringTextComponent(""));
		playerInv.player.openContainer = this.container;
		this.passEvents = true;
		this.ySize = 136;
		this.xSize = 195;
	}

	@Override
	public void tick() {
		if (!this.minecraft.playerController.isInCreativeMode()) {
			this.minecraft.displayGuiScreen(new InventoryScreen(this.minecraft.player));
		} else if (this.searchField != null) {
			this.searchField.tick();
		}
	}

	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type){
		if (this.hasTmpInventory(slotIn)) {
			this.searchField.setCursorPositionEnd();
			this.searchField.setSelectionPos(0);
		}
		boolean flag = type == ClickType.QUICK_MOVE;
		type = slotId == -999 && type == ClickType.PICKUP ? ClickType.THROW : type;
		if (slotIn == null && selectedTabIndex != ItemGroup.INVENTORY.getIndex() && type != ClickType.QUICK_CRAFT) {
			PlayerInventory playerinventory1 = this.minecraft.player.inventory;
			if (!playerinventory1.getItemStack().isEmpty() && this.field_199506_G) {
				if (mouseButton == 0) {
					playerinventory1.setItemStack(ItemStack.EMPTY);
				}

				if (mouseButton == 1) {
					ItemStack itemstack6 = playerinventory1.getItemStack().split(1);
				}
			}
		} else {
			if (slotIn != null && !slotIn.canTakeStack(this.minecraft.player)) {
				 return;
			}

			if (slotIn == this.destroyItemSlot && flag) {
				for(int j = 0; j < this.minecraft.player.container.getInventory().size(); ++j) {
					this.minecraft.playerController.sendSlotPacket(ItemStack.EMPTY, j);
				}
			} else if (selectedTabIndex == ItemGroup.INVENTORY.getIndex()) {
				// iプレイヤーインベントリ
			} else if (type != ClickType.QUICK_CRAFT && slotIn.inventory == TMP_INVENTORY) {
				PlayerInventory playerinventory = this.minecraft.player.inventory;
				ItemStack itemstack5 = playerinventory.getItemStack();
				ItemStack itemstack7 = slotIn.getStack();
				if (type == ClickType.SWAP) {
					   if (!itemstack7.isEmpty() && mouseButton >= 0 && mouseButton < 9) {
						  ItemStack itemstack10 = itemstack7.copy();
						  itemstack10.setCount(itemstack10.getMaxStackSize());
						  ITM_INVENTORY.setInventorySlotContents(mouseButton, itemstack10);
						  this.container.detectAndSendChanges();
					   }
					   return;
				}

				if (type == ClickType.CLONE) {
					if (playerinventory.getItemStack().isEmpty() && slotIn.getHasStack()) {
						ItemStack itemstack9 = slotIn.getStack().copy();
						itemstack9.setCount(itemstack9.getMaxStackSize());
						playerinventory.setItemStack(itemstack9);
					}
					return;
				}

				if (type == ClickType.THROW) {
					if (!itemstack7.isEmpty()) {
						ItemStack itemstack8 = itemstack7.copy();
						itemstack8.setCount(mouseButton == 0 ? 1 : itemstack8.getMaxStackSize());
						this.minecraft.playerController.sendPacketDropItem(itemstack8);
					}
					return;
				}
				if (!itemstack5.isEmpty() && !itemstack7.isEmpty() && itemstack5.isItemEqual(itemstack7) && ItemStack.areItemStackTagsEqual(itemstack5, itemstack7)) {
					if (mouseButton == 0) {
						if (flag) {
							itemstack5.setCount(itemstack5.getMaxStackSize());
						} else if (itemstack5.getCount() < itemstack5.getMaxStackSize()) {
							itemstack5.grow(1);
						}
					} else {
						itemstack5.shrink(1);
					}
				} else if (!itemstack7.isEmpty() && itemstack5.isEmpty()) {
					playerinventory.setItemStack(itemstack7.copy());
					itemstack5 = playerinventory.getItemStack();
					if (flag) {
						itemstack5.setCount(itemstack5.getMaxStackSize());
					}
				} else if (mouseButton == 0) {
					playerinventory.setItemStack(ItemStack.EMPTY);
				} else {
					playerinventory.getItemStack().shrink(1);
				}
			} else if (this.container != null) {
				ItemStack itemstack3 = slotIn == null ? ItemStack.EMPTY : this.container.getSlot(slotIn.slotNumber).getStack();
				this.container.slotClick(slotIn == null ? slotId : slotIn.slotNumber, mouseButton, type, this.minecraft.player);
				if (Container.getDragEvent(mouseButton) == 2) {
					for(int k = 0; k < 9; ++k) {
						this.minecraft.playerController.sendSlotPacket(this.container.getSlot(45 + k).getStack(), 36 + k);
					}
				} else if (slotIn != null) {
					ItemStack itemstack4 = this.container.getSlot(slotIn.slotNumber).getStack();
					this.minecraft.playerController.sendSlotPacket(itemstack4, slotIn.slotNumber - (this.container).inventorySlots.size() + 9 + 36);
					int i = 45 + mouseButton;
					if (type == ClickType.SWAP) {
						this.minecraft.playerController.sendSlotPacket(itemstack3, i - (this.container).inventorySlots.size() + 9 + 36);
					} else if (type == ClickType.THROW && !itemstack3.isEmpty()) {
						ItemStack itemstack2 = itemstack3.copy();
						itemstack2.setCount(mouseButton == 0 ? 1 : itemstack2.getMaxStackSize());
						this.minecraft.playerController.sendPacketDropItem(itemstack2);
					}

					this.container.detectAndSendChanges();
				}
			}
		}
	}

	private boolean hasTmpInventory(@Nullable Slot slotIn) {
		return slotIn != null && slotIn.inventory == getTmpInventory();
	}

	@Override
	public void init() {
		super.init();
		this.minecraft.keyboardListener.enableRepeatEvents(true);
		int tabCount = ItemGroup.GROUPS.length;
		if (tabCount > 12) {
			addButton(new net.minecraft.client.gui.widget.button.Button(guiLeft,              guiTop - 50, 20, 20, "<", b -> tabPage = Math.max(tabPage - 1, 0       )));
			addButton(new net.minecraft.client.gui.widget.button.Button(guiLeft + xSize - 20, guiTop - 50, 20, 20, ">", b -> tabPage = Math.min(tabPage + 1, maxPages)));
			maxPages = (int) Math.ceil((tabCount - 12) / 10D);
		}
		this.buttons.clear();
		this.searchField = new TextFieldWidget(this.font, this.guiLeft + 82, this.guiTop + 6, 80, 9, I18n.format("itemGroup.search"));
		this.searchField.setMaxStringLength(50);
		this.searchField.setEnableBackgroundDrawing(false);
		this.searchField.setVisible(false);
		this.searchField.setTextColor(16777215);
		int var1 = selectedTabIndex;
		selectedTabIndex = -1;
		this.setCurrentCreativeTab(ItemGroup.GROUPS[var1]);
		this.minecraft.player.container.removeListener(this.listener);
		this.listener = new CreativeCraftingListener(this.minecraft);
		this.minecraft.player.container.addListener(this.listener);
	}

	@Override
	public void resize(Minecraft p_resize_1_, int p_resize_2_, int p_resize_3_) {
		String s = this.searchField.getText();
		this.init(p_resize_1_, p_resize_2_, p_resize_3_);
		this.searchField.setText(s);
		if (!this.searchField.getText().isEmpty()) {
			this.updateCreativeSearch();
		}
	}

	@Override
	public void removed() {
		super.removed();
		if (this.minecraft.player != null && this.minecraft.player.inventory != null) {
			this.minecraft.player.container.removeListener(this.listener);
		}
		this.minecraft.keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		if (this.field_195377_F) {
			return false;
		} else if (!ItemGroup.GROUPS[selectedTabIndex].hasSearchBar()) {
			return false;
		} else {
			String s = this.searchField.getText();
			if (this.searchField.charTyped(p_charTyped_1_, p_charTyped_2_)) {
				if (!Objects.equals(s, this.searchField.getText())) {
					this.updateCreativeSearch();
				}
				return true;
			} else {
				return false;
			}
		}
	}


	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		this.field_195377_F = false;
		if (!ItemGroup.GROUPS[selectedTabIndex].hasSearchBar()) {
			if (this.minecraft.gameSettings.keyBindChat.matchesKey(p_keyPressed_1_, p_keyPressed_2_)) {
				this.field_195377_F = true;
				this.setCurrentCreativeTab(ItemGroup.SEARCH);
				return true;
			} else {
				return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
			}
		} else {
			boolean flag = !this.hasTmpInventory(this.hoveredSlot) || this.hoveredSlot != null && this.hoveredSlot.getHasStack();
			if (flag && this.func_195363_d(p_keyPressed_1_, p_keyPressed_2_)) {
				this.field_195377_F = true;
				return true;
			} else {
				String s = this.searchField.getText();
				if (this.searchField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)) {
					if (!Objects.equals(s, this.searchField.getText())) {
						this.updateCreativeSearch();
					}
					return true;
				} else {
					return this.searchField.isFocused() && this.searchField.getVisible() && p_keyPressed_1_ != 256 ? true : super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
				}
			}
		}
	}

	@Override
	public boolean keyReleased(int p_223281_1_, int p_223281_2_, int p_223281_3_) {
		this.field_195377_F = false;
		return super.keyReleased(p_223281_1_, p_223281_2_, p_223281_3_);
	}

	private void updateCreativeSearch() {
		(this.container).itemList.clear();
		this.tagSearchResults.clear();
		ItemGroup tab = ItemGroup.GROUPS[selectedTabIndex];
		if (tab.hasSearchBar() && tab != ItemGroup.SEARCH) {
			tab.fill(container.itemList);
			if (!this.searchField.getText().isEmpty()) {
				String search = this.searchField.getText().toLowerCase(Locale.ROOT);
				java.util.Iterator<ItemStack> itr = container.itemList.iterator();
				while (itr.hasNext()) {
					ItemStack stack = itr.next();
					boolean matches = false;
					for (ITextComponent line : stack.getTooltip(this.minecraft.player, this.minecraft.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL)) {
						if (TextFormatting.getTextWithoutFormattingCodes(line.getString()).toLowerCase(Locale.ROOT).contains(search)) {
							matches = true;
							break;
						}
					}
					if (!matches) {
						itr.remove();
					}
				}
			}

			this.currentScroll = 0.0F;
			container.scrollTo(0.0F);
			return;
		}

		String s = this.searchField.getText();
		if (s.isEmpty()) {
			for(Item item : Registry.ITEM) {
				item.fillItemGroup(ItemGroup.SEARCH, (this.container).itemList);
			}
		} else {
			ISearchTree<ItemStack> isearchtree;
			if (s.startsWith("#")) {
				s = s.substring(1);
				isearchtree = this.minecraft.func_213253_a(SearchTreeManager.field_215360_b);
				this.searchTags(s);
			} else {
				isearchtree = this.minecraft.func_213253_a(SearchTreeManager.field_215359_a);
			}
			(this.container).itemList.addAll(isearchtree.search(s.toLowerCase(Locale.ROOT)));
		}
		this.currentScroll = 0.0F;
		this.container.scrollTo(0.0F);
	}

	private void searchTags(String search) {
		int i = search.indexOf(58);
		Predicate<ResourceLocation> predicate;
		if (i == -1) {
			predicate = (p_214084_1_) -> {
				return p_214084_1_.getPath().contains(search);
			};
		} else {
			String s = search.substring(0, i).trim();
			String s1 = search.substring(i + 1).trim();
			predicate = (p_214081_2_) -> {
				return p_214081_2_.getNamespace().contains(s) && p_214081_2_.getPath().contains(s1);
			};
		}
		TagCollection<Item> tagcollection = ItemTags.getCollection();
		tagcollection.getRegisteredTags().stream().filter(predicate).forEach((p_214082_2_) -> {
			Tag tag = this.tagSearchResults.put(p_214082_2_, tagcollection.get(p_214082_2_));
		});
	}

	@Override
	public void onClose() {
		super.onClose();

		if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.inventory != null) {
			Minecraft.getInstance().player.container.removeListener(this.field_82324_x);
		}
		isChange = true;
		target.setItemStackToSlot(EquipmentSlotType.MAINHAND,
				ITM_INVENTORY.getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.MAINHAND)));
		target.setItemStackToSlot(EquipmentSlotType.OFFHAND,
				ITM_INVENTORY.getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.OFFHAND)));
		target.setItemStackToSlot(EquipmentSlotType.FEET,
				ITM_INVENTORY.getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.FEET)));
		target.setItemStackToSlot(EquipmentSlotType.LEGS,
				ITM_INVENTORY.getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.LEGS)));
		target.setItemStackToSlot(EquipmentSlotType.CHEST,
				ITM_INVENTORY.getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.CHEST)));
		target.setItemStackToSlot(EquipmentSlotType.HEAD,
				ITM_INVENTORY.getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.HEAD)));

		Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		ItemGroup itemgroup = ItemGroup.GROUPS[selectedTabIndex];
		if (itemgroup != null && itemgroup.drawInForegroundOfTab()) {
			GlStateManager.disableBlend();
			this.font.drawString(I18n.format(itemgroup.getTranslationKey()), 8.0F, 6.0F, itemgroup.getLabelColor());
		}
	}

	private boolean needsScrollBars() {
		if (ItemGroup.GROUPS[selectedTabIndex] == null) return false;
		return selectedTabIndex != ItemGroup.INVENTORY.getIndex() && ItemGroup.GROUPS[selectedTabIndex].hasScrollbar() && this.container.canScroll();
	}

	private void setCurrentCreativeTab(ItemGroup tab) {
		if (tab == null) return;
		int var2 = selectedTabIndex;
		selectedTabIndex = tab.getIndex();
		ContainerItemSelect var3 = (ContainerItemSelect) this.container;
		var3.itemList.clear();
		this.dragSplittingSlots.clear();
		if (tab == ItemGroup.HOTBAR) {
			CreativeSettings creativesettings = this.minecraft.getCreativeSettings();
			for(int j = 0; j < 9; ++j) {
				HotbarSnapshot hotbarsnapshot = creativesettings.getHotbarSnapshot(j);
				if (hotbarsnapshot.isEmpty()) {
					for(int k = 0; k < 9; ++k) {
						if (k == j) {
							ItemStack itemstack = new ItemStack(Items.PAPER);
							itemstack.getOrCreateChildTag("CustomCreativeLock");
							String s = this.minecraft.gameSettings.keyBindsHotbar[j].getLocalizedName();
							String s1 = this.minecraft.gameSettings.keyBindSaveToolbar.getLocalizedName();
							itemstack.setDisplayName(new TranslationTextComponent("inventory.hotbarInfo", s1, s));
							(this.container).itemList.add(itemstack);
						} else {
							(this.container).itemList.add(ItemStack.EMPTY);
						}
					}
				} else {
					(this.container).itemList.addAll(hotbarsnapshot);
				}
			}
		} else if (tab != ItemGroup.SEARCH) {
			tab.fill((this.container).itemList);
		}

		if (this.searchField != null) {
			if (tab.hasSearchBar()) {
				this.searchField.setVisible(true);
				this.searchField.setCanLoseFocus(false);
				this.searchField.setFocused2(true);
				if (var2 != tab.getIndex()) {
					this.searchField.setText("");
				}
				this.searchField.setWidth(tab.getSearchbarWidth());
				this.searchField.x = this.guiLeft + (82 + 89) - this.searchField.getWidth();
				this.updateCreativeSearch();
			} else {
				this.searchField.setVisible(false);
				this.searchField.setCanLoseFocus(true);
				this.searchField.setFocused2(false);
				this.searchField.setText("");
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

			for(ItemGroup itemgroup : ItemGroup.GROUPS) {
	            if (itemgroup != null && this.isMouseOverGroup(itemgroup, var4, var5)) {
	            	 return true;
				}
			}
			if (selectedTabIndex != ItemGroup.INVENTORY.getIndex() && this.func_195376_a(par1, par2)) {
	            this.isScrolling = this.needsScrollBars();
	            return true;
	         }
		}
		return super.mouseClicked(par1, par2, par3);
	}

	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		if (p_mouseReleased_5_ == 0) {
			double d0 = p_mouseReleased_1_ - (double)this.guiLeft;
			double d1 = p_mouseReleased_3_ - (double)this.guiTop;
			this.isScrolling = false;
			for(ItemGroup itemgroup : ItemGroup.GROUPS) {
				if (itemgroup != null && this.isMouseOverGroup(itemgroup, d0, d1)) {
					this.setCurrentCreativeTab(itemgroup);
					return true;
				}
			}
		}
		return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
	}


	protected boolean isMouseOverGroup(ItemGroup p_195375_1_, double p_195375_2_, double p_195375_4_) {
		if ((p_195375_1_.getTabPage() != tabPage && p_195375_1_ != ItemGroup.SEARCH) || p_195375_1_ == ItemGroup.INVENTORY) return false;
		int i = p_195375_1_.getColumn();
		int j = 28 * i;
		int k = 0;
		if (p_195375_1_.isAlignedRight()) {
			j = this.xSize - 28 * (6 - i) + 2;
		} else if (i > 0) {
			j += i;
		}
		if (p_195375_1_.isOnTopRow()) {
			k = k - 32;
		} else {
			k = k + this.ySize;
		}
		return p_195375_2_ >= (double)j && p_195375_2_ <= (double)(j + 28) && p_195375_4_ >= (double)k && p_195375_4_ <= (double)(k + 32);
	}

	protected boolean func_195376_a(double p_195376_1_, double p_195376_3_) {
		int i = this.guiLeft;
		int j = this.guiTop;
		int k = i + 175;
		int l = j + 18;
		int i1 = k + 14;
		int j1 = l + 112;
		return p_195376_1_ >= (double)k && p_195376_3_ >= (double)l && p_195376_1_ < (double)i1 && p_195376_3_ < (double)j1;
	}

	@Override
	public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
		if (!this.needsScrollBars()) {
			return false;
		} else {
			int i = ((this.container).itemList.size() + 9 - 1) / 9 - 5;
			this.currentScroll = (float)((double)this.currentScroll - p_mouseScrolled_5_ / (double)i);
			this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
			this.container.scrollTo(this.currentScroll);
			return true;
		}
	}

	@Override
	protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_, int p_195361_7_) {
		boolean flag = p_195361_1_ < (double)p_195361_5_ || p_195361_3_ < (double)p_195361_6_ || p_195361_1_ >= (double)(p_195361_5_ + this.xSize) || p_195361_3_ >= (double)(p_195361_6_ + this.ySize);
		this.field_199506_G = flag && !this.isMouseOverGroup(ItemGroup.GROUPS[selectedTabIndex], p_195361_1_, p_195361_3_);
		return this.field_199506_G;
	}

	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (this.isScrolling) {
			int i = this.guiTop + 18;
			int j = i + 112;
			this.currentScroll = ((float)p_mouseDragged_3_ - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
			this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
			this.container.scrollTo(this.currentScroll);
			return true;
		} else {
			return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
		}
	}

	@Override
	public void render(int par1, int par2, float par3) {
		this.renderBackground();
		super.render(par1, par2, par3);

		int start = tabPage * 10;
		int end = Math.min(ItemGroup.GROUPS.length, ((tabPage + 1) * 10) + 2);
		if (tabPage != 0) start += 2;
		boolean rendered = false;

		for (int x = start; x < end; x++) {
			ItemGroup itemgroup = ItemGroup.GROUPS[x];
			if (itemgroup != null && this.renderCreativeInventoryHoveringText(itemgroup, par1, par2)) {
				rendered = true;
				break;
			}
		}

		if (!rendered && !renderCreativeInventoryHoveringText(ItemGroup.SEARCH, par1, par2))
			renderCreativeInventoryHoveringText(ItemGroup.INVENTORY, par1, par2);

		if (this.destroyItemSlot != null && selectedTabIndex == ItemGroup.INVENTORY.getIndex() && this.isPointInRegion(this.destroyItemSlot.xPos, this.destroyItemSlot.yPos, 16, 16, (double)par1, (double)par2)) {
			this.renderTooltip(I18n.format("inventory.binSlot"), par1, par2);
		}

		if (maxPages != 0) {
			String page = String.format("%d / %d", tabPage + 1, maxPages + 1);
			GlStateManager.disableLighting();
			this.blitOffset = 300;
			this.itemRenderer.zLevel = 300.0F;
			font.drawString(page, guiLeft + (xSize / 2) - (font.getStringWidth(page) / 2), guiTop - 44, -1);
			this.blitOffset = 0;
			this.itemRenderer.zLevel = 0.0F;
		}

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
		this.renderHoveredToolTip(par1, par2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		ItemGroup var5 = ItemGroup.GROUPS[selectedTabIndex];
		ItemGroup itemgroup = ItemGroup.GROUPS[selectedTabIndex];

		int start = tabPage * 10;
		int end = Math.min(ItemGroup.GROUPS.length, ((tabPage + 1) * 10 + 2));
		if (tabPage != 0) start += 2;

		for (int idx = start; idx < end; idx++) {
			ItemGroup itemgroup1 = ItemGroup.GROUPS[idx];
			if (itemgroup1 != null && itemgroup1.getIndex() != selectedTabIndex && itemgroup1 != ItemGroup.INVENTORY && itemgroup1 != ItemGroup.HOTBAR) {
				this.minecraft.getTextureManager().bindTexture(itemgroup1.getTabsImage());
				this.drawTab(itemgroup1);
			}
		}

		this.minecraft.getTextureManager().bindTexture(itemgroup.getBackgroundImage());
		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.searchField.render(par2, par3, par1);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		int i = this.guiLeft + 175;
		int j = this.guiTop + 18;
		int k = j + 112;
		this.minecraft.getTextureManager().bindTexture(itemgroup.getTabsImage());
		if (itemgroup.hasScrollbar()) {
			this.blit(i, j + (int)((float)(k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		}

		if ((itemgroup == null || itemgroup.getTabPage() != tabPage) && (itemgroup != ItemGroup.SEARCH && itemgroup != ItemGroup.INVENTORY)) {
			return;
		}
		this.drawTab(itemgroup);
		if (itemgroup == ItemGroup.INVENTORY) {

		}
	}

	protected void renderTooltip(ItemStack p_renderTooltip_1_, int p_renderTooltip_2_, int p_renderTooltip_3_) {
		if (selectedTabIndex == ItemGroup.SEARCH.getIndex()) {
			List<ITextComponent> list = p_renderTooltip_1_.getTooltip(this.minecraft.player, this.minecraft.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
			List<String> list1 = Lists.newArrayListWithCapacity(list.size());

			for(ITextComponent itextcomponent : list) {
				list1.add(itextcomponent.getFormattedText());
			}

			Item item = p_renderTooltip_1_.getItem();
			ItemGroup itemgroup1 = item.getGroup();
			if (itemgroup1 == null && item == Items.ENCHANTED_BOOK) {
				Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(p_renderTooltip_1_);
				if (map.size() == 1) {
					Enchantment enchantment = map.keySet().iterator().next();

					for(ItemGroup itemgroup : ItemGroup.GROUPS) {
						if (itemgroup.hasRelevantEnchantmentType(enchantment.type)) {
							itemgroup1 = itemgroup;
							break;
						}
					}
				}
			}

			this.tagSearchResults.forEach((p_214083_2_, p_214083_3_) -> {
				if (p_214083_3_.contains(item)) {
					list1.add(1, "" + TextFormatting.BOLD + TextFormatting.DARK_PURPLE + "#" + p_214083_2_);
				}
			});
			if (itemgroup1 != null) {
				list1.add(1, "" + TextFormatting.BOLD + TextFormatting.BLUE + I18n.format(itemgroup1.getTranslationKey()));
			}

			for(int i = 0; i < list1.size(); ++i) {
				if (i == 0) {
					list1.set(i, p_renderTooltip_1_.getRarity().color + (String)list1.get(i));
				} else {
					list1.set(i, TextFormatting.GRAY + (String)list1.get(i));
				}
			}

			net.minecraft.client.gui.FontRenderer font = p_renderTooltip_1_.getItem().getFontRenderer(p_renderTooltip_1_);
			net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(p_renderTooltip_1_);
			this.renderTooltip(list1, p_renderTooltip_2_, p_renderTooltip_3_, (font == null ? this.font : font));
			net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
		} else {
			super.renderTooltip(p_renderTooltip_1_, p_renderTooltip_2_, p_renderTooltip_3_);
		}
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

	protected boolean renderCreativeInventoryHoveringText(ItemGroup tab, int mouseX, int mouseY) {
		if (tab == ItemGroup.HOTBAR || tab == ItemGroup.INVENTORY) {return false;}
		int i = tab.getColumn();
		int j = 28 * i;
		int k = 0;
		if (tab.isAlignedRight()) {
			j = this.xSize - 28 * (6 - i) + 2;
		} else if (i > 0) {
			j += i;
		}

		if (tab.isOnTopRow()) {
			k = k - 32;
		} else {
			k = k + this.ySize;
		}

		if (this.isPointInRegion(j + 3, k + 3, 23, 27, (double)mouseX, (double)mouseY)) {
			this.renderTooltip(I18n.format(tab.getTranslationKey()), mouseX, mouseY);
			return true;
		} else {
			return false;
		}
	}

	protected void drawTab(ItemGroup tab) {
		boolean flag = tab.getIndex() == selectedTabIndex;
		boolean flag1 = tab.isOnTopRow();
		int i = tab.getColumn();
		int j = i * 28;
		int k = 0;
		int l = this.guiLeft + 28 * i;
		int i1 = this.guiTop;
		int j1 = 32;
		if (flag) {
			k += 32;
		}

		if (tab.isAlignedRight()) {
			l = this.guiLeft + this.xSize - 28 * (6 - i);
		} else if (i > 0) {
			l += i;
		}

		if (flag1) {
			i1 = i1 - 28;
		} else {
			k += 64;
			i1 = i1 + (this.ySize - 4);
		}

		GlStateManager.disableLighting();
		GlStateManager.color3f(1F, 1F, 1F);
		GlStateManager.enableBlend();
		this.blit(l, i1, j, k, 28, 32);
		this.blitOffset = 100;
		this.itemRenderer.zLevel = 100.0F;
		l = l + 6;
		i1 = i1 + 8 + (flag1 ? 1 : -1);
		GlStateManager.enableLighting();
		GlStateManager.enableRescaleNormal();
		ItemStack itemstack = tab.getIcon();
		this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, l, i1);
		this.itemRenderer.renderItemOverlays(this.font, itemstack, l, i1);
		GlStateManager.disableLighting();
		this.itemRenderer.zLevel = 0.0F;
		this.blitOffset = 0;
	}

	public int getSelectedTabIndex() {
		return selectedTabIndex;
	}

	/**
	 * 選択用のインベントリをクリア
	 */
	public static void clearInventory() {
		for (int li = 0; li < ITM_INVENTORY.getSizeInventory(); li++) {
			ITM_INVENTORY.setInventorySlotContents(li, ItemStack.EMPTY);
		}
	}

	public static Inventory getTmpInventory() {
		return TMP_INVENTORY;
	}

	public static Inventory getItmInventory() {
		return ITM_INVENTORY;
	}

}
