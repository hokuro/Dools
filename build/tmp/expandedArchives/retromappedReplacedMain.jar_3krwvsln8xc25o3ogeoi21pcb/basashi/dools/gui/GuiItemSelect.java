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

	private static int selectedTabIndex = CreativeTabs.field_78030_b.func_78021_a();

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
		this.field_146291_p = true;
		this.field_147000_g = 136;
		this.field_146999_f = 195;
		ownerScreen = pOwner;
		target = pTarget;
	}

	@Override
    protected void func_184098_a(Slot par1Slot, int par2, int par3, ClickType par4){
	//protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4) {
		this.field_74234_w = true;
		boolean var5 = par4 == ClickType.QUICK_MOVE; // Shift
		InventoryPlayer var6 = this.field_146297_k.field_71439_g.field_71071_by;
		ItemStack var7 = var6.func_70445_o();
		ModLog.log().debug("handolMouse.(" + par2 + par3 + par4.toString() +")");

		if (par1Slot != null) {
			if (var5) {
				// Shiftが押されている
				if (var7 != null) {
					var6.func_70437_b(par1Slot.func_75211_c());
					par1Slot.func_75215_d(var7);
				} else {
					var7 = par1Slot.func_75211_c();
					if (var7 != null) {
						var7 = var7.func_77979_a(1);
					}
				}
			} else if (par1Slot.field_75224_c == inventory) {
				// Creativeエリアの処理
				ItemStack var8 = par1Slot.func_75211_c();
				ItemStack var9;

				if (par4 == ClickType.SWAP) {
					return;
				}

				if (par4 == ClickType.CLONE) {
					if (var6.func_70445_o() == null && par1Slot.func_75216_d()) {
						var9 = par1Slot.func_75211_c().func_77946_l();
						var9.field_77994_a = var9.func_77976_d();
						var6.func_70437_b(var9);
					}

					return;
				}

				if (var7 != null && var8 != null && var7.func_77969_a(var8)) {
					if (par3 == 0) {
						if (var5) {
							var7.field_77994_a = var7.func_77976_d();
						} else if (var7.field_77994_a < var7.func_77976_d()) {
							++var7.field_77994_a;
						}
					} else if (var7.field_77994_a <= 1) {
						var6.func_70437_b((ItemStack) null);
					} else {
						--var7.field_77994_a;
					}
				} else if (var8 != null && var7 == null) {
					var6.func_70437_b(ItemStack.func_77944_b(var8));
					var7 = var6.func_70445_o();

					if (var5) {
						var7.field_77994_a = var7.func_77976_d();
					}
				} else {
					var6.func_70437_b((ItemStack) null);
				}
			} else {
				// 装備品欄
				//this.inventorySlots.retrySlotClick(par1Slot == null ? par2 : par1Slot.slotNumber, par3, true, this.mc.thePlayer);
				this.field_147002_h.func_184996_a(par1Slot == null ? par2 : par1Slot.field_75222_d, par3, par4, this.field_146297_k.field_71439_g);
			}
		} else {
			// スロットをクリックしなかった
			var6.func_70437_b((ItemStack) null);
		}
	}

	@Override
	public void func_73866_w_() {
		super.func_73866_w_();
		this.field_146292_n.clear();
		Keyboard.enableRepeatEvents(true);
		this.searchField = new GuiTextField(0, this.field_146289_q,
				this.field_147003_i + 82, this.field_147009_r + 6, 89, this.field_146289_q.field_78288_b);
		this.searchField.func_146203_f(15);
		this.searchField.func_146185_a(false);
		this.searchField.func_146189_e(false);
		this.searchField.func_146193_g(16777215);
		int var1 = selectedTabIndex;
		selectedTabIndex = -1;
		this.setCurrentCreativeTab(CreativeTabs.field_78032_a[var1]);
		this.field_82324_x = new CreativeCrafting(this.field_146297_k);
		this.field_146297_k.field_71439_g.field_71069_bz.func_75132_a(this.field_82324_x);
	}

	@Override
	public void func_146281_b() {
		super.func_146281_b();

		if (this.field_146297_k.field_71439_g != null && this.field_146297_k.field_71439_g.field_71071_by != null) {
			this.field_146297_k.field_71439_g.field_71069_bz.func_82847_b(this.field_82324_x);
		}
		isChange = true;
		target.func_184201_a(EntityEquipmentSlot.MAINHAND,
				inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.MAINHAND)));
		target.func_184201_a(EntityEquipmentSlot.OFFHAND,
				inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.OFFHAND)));
		target.func_184201_a(EntityEquipmentSlot.FEET,
				 inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.FEET)));
		target.func_184201_a(EntityEquipmentSlot.LEGS,
				inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.LEGS)));
		target.func_184201_a(EntityEquipmentSlot.CHEST,
				 inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.CHEST)));
		target.func_184201_a(EntityEquipmentSlot.HEAD,
				 inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.HEAD)));
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void func_73869_a(char par1, int par2) {
		if (selectedTabIndex != CreativeTabs.field_78027_g.func_78021_a()) {
			if (Keyboard.isKeyDown(this.field_146297_k.field_71474_y.field_74310_D.func_151463_i())) {
				this.setCurrentCreativeTab(CreativeTabs.field_78027_g);
			} else {
				if (par2 == 1) {
					field_146297_k.func_147108_a(ownerScreen);
				}
			}
		} else {
			if (this.field_74234_w) {
				this.field_74234_w = false;
				this.searchField.func_146180_a("");
			}

			if (!this.func_146983_a(par2)) {
				if (this.searchField.func_146201_a(par1, par2)) {
					this.updateCreativeSearch();
				} else {
					try {
						super.func_73869_a(par1, par2);
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void updateCreativeSearch() {
		ContainerItemSelect lcontainer = (ContainerItemSelect)field_147002_h;
		lcontainer.itemList.clear();
		List<Item> list = new ArrayList<Item>();
		for (ResourceLocation key : Item.field_150901_e.func_148742_b()){
			list.add(Item.field_150901_e.func_82594_a(key));
		}
		Object[] var2 = list.toArray();
		int var3 = var2.length;
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			Item var5 = (Item)var2[var4];

			if (var5 != null && var5.func_77640_w() != null) {
				var5.func_150895_a(var5, (CreativeTabs) null, lcontainer.itemList);
			}
		}

//		Enchantment[] var8 = Enchantment.enchantmentsBookList;
//		var3 = var8.length;
		Iterator<Enchantment> ent = Enchantment.field_185264_b.iterator();
		while(ent.hasNext()){
			Enchantment var12 = ent.next();
			if (var12 != null && var12.field_77351_y != null) {
				Items.field_151134_bR.func_92113_a(var12, lcontainer.itemList);
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
		String var10 = this.searchField.func_146179_b().toLowerCase();

		while (var9.hasNext()) {
			ItemStack var11 = (ItemStack) var9.next();
			boolean var13 = false;
			Iterator var6 = var11.func_82840_a(this.field_146297_k.field_71439_g,
					this.field_146297_k.field_71474_y.field_82882_x).iterator();

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
	protected void func_146979_b(int par1, int par2) {
		CreativeTabs lct = CreativeTabs.field_78032_a[selectedTabIndex];

		if (lct.func_78019_g()) {
			this.field_146289_q.func_78276_b(I18n.func_135052_a(lct.func_78024_c()), 8, 6, 4210752);
		}
	}

	@Override
	protected void func_73864_a(int par1, int par2, int par3) {
		if (par3 == 0) {
			int var4 = par1 - this.field_147003_i;
			int var5 = par2 - this.field_147009_r;
			CreativeTabs[] var6 = CreativeTabs.field_78032_a;
			int var7 = var6.length;

			for (int var8 = 0; var8 < var7; ++var8) {
				CreativeTabs var9 = var6[var8];
				if (var9 == CreativeTabs.field_78036_m) {
					continue;
				}
				if (this.func_147049_a(var9, var4, var5)) {
					this.setCurrentCreativeTab(var9);
					return;
				}
			}
		}

		try {
			super.func_73864_a(par1, par2, par3);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private boolean needsScrollBars() {
		return selectedTabIndex != CreativeTabs.field_78036_m.func_78021_a()
				&& CreativeTabs.field_78032_a[selectedTabIndex].func_78017_i()
				&& ((ContainerItemSelect) this.field_147002_h).hasMoreThan1PageOfItemsInList();
	}

	private void setCurrentCreativeTab(CreativeTabs par1CreativeTabs) {
		int var2 = selectedTabIndex;
		selectedTabIndex = par1CreativeTabs.func_78021_a();
		ContainerItemSelect var3 = (ContainerItemSelect) this.field_147002_h;
		var3.itemList.clear();
		par1CreativeTabs.func_78018_a(var3.itemList);  //displayAllReleventItems(var3.itemList);

		if (this.searchField != null) {
			if (par1CreativeTabs == CreativeTabs.field_78027_g) {
				this.searchField.func_146189_e(true);
				this.searchField.func_146205_d(false);
				this.searchField.func_146195_b(true);
				this.searchField.func_146180_a("");
				this.updateCreativeSearch();
			} else {
				this.searchField.func_146189_e(false);
				this.searchField.func_146205_d(true);
				this.searchField.func_146195_b(false);
			}
		}

		this.currentScroll = 0.0F;
		var3.scrollTo(0.0F);
	}

	@Override
	public void func_146274_d() {
		try {
			super.func_146274_d();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		int var1 = Mouse.getEventDWheel();

		if (var1 != 0 && this.needsScrollBars()) {
			int var2 = ((ContainerItemSelect) this.field_147002_h).itemList.size() / 9 - 5 + 1;

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

			((ContainerItemSelect) this.field_147002_h).scrollTo(this.currentScroll);
		}
	}

	@Override
	public void func_73863_a(int par1, int par2, float par3) {
		boolean var4 = Mouse.isButtonDown(0);
		int var5 = this.field_147003_i;
		int var6 = this.field_147009_r;
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

			((ContainerItemSelect) this.field_147002_h).scrollTo(this.currentScroll);
		}

		super.func_73863_a(par1, par2, par3);
		CreativeTabs[] var11 = CreativeTabs.field_78032_a;
		int var12 = var11.length;

		for (int var13 = 0; var13 < var12; ++var13) {
			CreativeTabs lct = var11[var13];
			if (lct == CreativeTabs.field_78036_m) {
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
	protected void func_146976_a(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.func_74520_c();
		CreativeTabs var5 = CreativeTabs.field_78032_a[selectedTabIndex];
		CreativeTabs[] var7 = CreativeTabs.field_78032_a;
		int var8 = var7.length;
		int var9;

		for (var9 = 0; var9 < var8; ++var9) {
			CreativeTabs var10 = var7[var9];
			if (var10 == CreativeTabs.field_78036_m) {
				continue;
			}
			Minecraft.func_71410_x().func_110434_K().func_110577_a(ftexTab);

			if (var10.func_78021_a() != selectedTabIndex) {
				this.renderCreativeTab(var10);
			}
		}

		Minecraft.func_71410_x().func_110434_K().func_110577_a(ftexGui);
		this.func_73729_b(field_147003_i, field_147009_r, 0, 0, field_146999_f, field_147000_g);
		this.searchField.func_146194_f();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var11 = this.field_147003_i + 175;
		var8 = this.field_147009_r + 18;
		var9 = var8 + 112;
		Minecraft.func_71410_x().func_110434_K().func_110577_a(ftexTab);

		if (var5.func_78017_i()) {
			this.func_73729_b(var11, var8
					+ (int) ((float) (var9 - var8 - 17) * this.currentScroll),
					232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		}

		this.renderCreativeTab(var5);
	}

	protected boolean func_147049_a(CreativeTabs par1CreativeTabs, int par2, int par3) {
		int var4 = par1CreativeTabs.func_78020_k();
		int var5 = 28 * var4;
		byte var6 = 0;

		if (var4 == 5) {
			var5 = this.field_146999_f - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (par1CreativeTabs.func_78023_l()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + this.field_147000_g;
		}

		return par2 >= var5 && par2 <= var5 + 28 && par3 >= var7
				&& par3 <= var7 + 32;
	}

	protected boolean renderCreativeInventoryHoveringText(CreativeTabs par1CreativeTabs, int par2, int par3) {
		int var4 = par1CreativeTabs.func_78020_k();
		int var5 = 28 * var4;
		byte var6 = 0;

		if (var4 == 5) {
			var5 = this.field_146999_f - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (par1CreativeTabs.func_78023_l()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + this.field_147000_g;
		}

		if (this.func_146978_c(var5 + 3, var7 + 3, 23, 27, par2, par3)) {
			this.func_146279_a(
					par1CreativeTabs.func_78024_c(), par2, par3);
			return true;
		} else {
			return false;
		}
	}

	protected void renderCreativeTab(CreativeTabs par1CreativeTabs) {
		boolean var2 = par1CreativeTabs.func_78021_a() == selectedTabIndex;
		boolean var3 = par1CreativeTabs.func_78023_l();
		int var4 = par1CreativeTabs.func_78020_k();
		int var5 = var4 * 28;
		int var6 = 0;
		int var7 = this.field_147003_i + 28 * var4;
		int var8 = this.field_147009_r;
		byte var9 = 32;

		if (var2) {
			var6 += 32;
		}

		if (var4 == 5) {
			var7 = this.field_147003_i + this.field_146999_f - 28;
		} else if (var4 > 0) {
			var7 += var4;
		}

		if (var3) {
			var8 -= 28;
		} else {
			var6 += 64;
			var8 += this.field_147000_g - 4;
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		this.func_73729_b(var7, var8, var5, var6, 28, var9);
		this.field_73735_i = 100.0F;
		RenderItem itemRenderer = Dools.getPrivateValue(GuiScreen.class, this, "itemRender");
		itemRenderer.field_77023_b = 100.0F;
		var7 += 6;
		var8 += 8 + (var3 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		ItemStack var10 = new ItemStack(par1CreativeTabs.func_78016_d());
		itemRenderer.func_180450_b(var10, var7, var8);
		itemRenderer.func_180453_a(this.field_146289_q,var10, var7, var8,"");


		GL11.glDisable(GL11.GL_LIGHTING);
		itemRenderer.field_77023_b = 0.0F;
		this.field_73735_i = 0.0F;
	}

	@Override
	protected void func_146284_a(GuiButton par1GuiButton) {
		if (par1GuiButton.field_146127_k == 0) {
			this.field_146297_k.func_147108_a(new GuiAchievements(this,this.field_146297_k.field_71439_g.func_146107_m()));
		}

		if (par1GuiButton.field_146127_k == 1) {
			this.field_146297_k.func_147108_a(new GuiStats(this, this.field_146297_k.field_71439_g.func_146107_m()));
		}
	}

	public int getSelectedTabIndex() {
		return selectedTabIndex;
	}

	/**
	 * 選択用のインベントリをクリア
	 */
	public static void clearInventory() {
		for (int li = 0; li < inventoryItem.func_70302_i_(); li++) {
			inventoryItem.func_70299_a(li, null);
		}
	}

}
