package basashi.dools.network;

import java.util.Random;

import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServer_SpawnDool implements IMessage, IMessageHandler<MessageServer_SpawnDool, IMessage> {

	private double x;
	private double y;
	private double z;
	private float yaw;
	private String mobString;

	public MessageServer_SpawnDool(){}
	public MessageServer_SpawnDool(double posX, double posY, double posZ, float yaw, String mob){
		this.x = posX;
		this.y = posY;
		this.z = posZ;
		this.yaw = yaw;
		this.mobString = mob;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		yaw = buf.readFloat();
		int len = buf.readInt();
		if (len!=0){
			byte[] st = new byte[len];
			buf.readBytes(st);
			mobString = new String(st);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(yaw);
		buf.writeInt(mobString.length());
		buf.writeBytes(mobString.getBytes());
	}

	@Override
	public IMessage onMessage(MessageServer_SpawnDool message, MessageContext ctx) {
		try{
			WorldServer lworld = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
			Entity lentity = null;
			EntityDool ldool = null;
			ServerDool lserver = null;
			int leid = 0;
			double lx = message.x;
			double ly = message.y;
			double lz = message.z;
			float lyaw = message.yaw;
			if (message.mobString =="") {
				// 未選択でGUI閉じたのでアイテムをドロップ
				EntityItem leitem = new EntityItem(lworld, lx, ly + 0.25D, lz, new ItemStack(Dools.dool));
				leitem.setDefaultPickupDelay();
				lworld.spawnEntityInWorld(leitem);
				ModLog.log().debug("SpawnItem:" +lx+"," +ly+"," +lz+ "Server.");
			} else {
				// 指定値にフィギュアをスポーン
				String lname = message.mobString;
				try {
					ldool = Dools.getEntityMob(lworld);
					lentity = EntityList.createEntityByName(lname, lworld);
					ldool.setRenderEntity((EntityLivingBase)lentity);
					ldool.setPositionAndRotation(lx, ly, lz, lyaw, 0F);
					lworld.spawnEntityInWorld(ldool);
					EntityPlayer pl = ctx.getServerHandler().playerEntity;
					lworld.playSound(pl, new BlockPos(pl.posX,pl.posY,pl.posZ), SoundEvents.block_stone_place, SoundCategory.BLOCKS ,0.5F, 0.4F / ((new Random()).nextFloat() * 0.4F + 0.8F));
					//lworld.playSoundAtEntity(ctx.getServerHandler().playerEntity, "step.wood",0.5F, 0.4F / ((new Random()).nextFloat() * 0.4F + 0.8F));
					ModLog.log().debug("SpawnFigure: "+lname+", "+lx+", "+ly+", "+lz+" Server.");
				} catch (Exception e) {
					ModLog.log().debug("SpawnFigure: failed.");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

}
