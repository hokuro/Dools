package basashi.dools.event;

import basashi.dools.core.Dools;
import basashi.dools.gui.GuiDoolSelect;
import basashi.dools.item.ItemDool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiEventHandler implements IGuiHandler {

	public GuiEventHandler(){

	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
//		BlockPos pos = new BlockPos(x,y,z);
//		if (id == 1){
//			TileEntity ent = world.getTileEntity(pos);
//			if((ent instanceof TileEntityExpBank)){
//				return new ContainerExpBank(player, world,x,y,z);
//			}
//		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		if (id == 0){
			if ((ItemDool.entDool != null)){
				return new GuiDoolSelect(world,ItemDool.entDool);
			}
		}else if (id == 1){
			if ((Dools.guiDool  != null)){
				return Dools.proxy.getGui(Dools.guiDool);
			}
		}
		Dools.guiDool = null;
		return null;
	}

}