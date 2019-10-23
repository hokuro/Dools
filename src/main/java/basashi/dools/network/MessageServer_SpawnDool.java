package basashi.dools.network;

import java.util.Random;
import java.util.function.Supplier;

import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageServer_SpawnDool {

	private double x;
	private double y;
	private double z;
	private float yaw;
	private String mobString;

	public MessageServer_SpawnDool(double posX, double posY, double posZ, float yaw, String mob){
		this.x = posX;
		this.y = posY;
		this.z = posZ;
		this.yaw = yaw;
		this.mobString = mob;
	}

	public static void encode(MessageServer_SpawnDool pkt, PacketBuffer buf) {
		buf.writeInt(pkt.mobString.length());
		buf.writeDouble(pkt.x);
		buf.writeDouble(pkt.y);
		buf.writeDouble(pkt.z);
		buf.writeFloat(pkt.yaw);
		buf.writeString(pkt.mobString);
	}

	public static MessageServer_SpawnDool decode(PacketBuffer buf) {
		int len = buf.readInt();
		return new MessageServer_SpawnDool(
				buf.readDouble(),
				buf.readDouble(),
				buf.readDouble(),
				buf.readFloat(),
				buf.readString(len)
				);
	}

	public static class Handler {
		public static void handle(final MessageServer_SpawnDool pkt, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				try{
					ServerWorld lworld = (ServerWorld) ctx.get().getSender().world;
					Entity lentity = null;
					EntityDool ldool = null;
					ServerDool lserver = null;
					int leid = 0;
					double lx = pkt.x;
					double ly = pkt.y;
					double lz = pkt.z;
					float lyaw = pkt.yaw;
					if (pkt.mobString =="") {
						// i未選択でGUI閉じたのでアイテムをドロップ
						ItemEntity leitem = new ItemEntity(lworld, lx, ly + 0.25D, lz, new ItemStack(Dools.item_dool));
						leitem.setDefaultPickupDelay();
						lworld.addEntity(leitem);
						ModLog.log().debug("SpawnItem:" +lx+"," +ly+"," +lz+ "Server.");
					} else {
						// i指定値にフィギュアをスポーン
						String lname = pkt.mobString;
						try {
							lentity = Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(lname)).create(lworld);
							ldool = new EntityDool(lworld, lentity);
							ldool.setPositionAndRotation(lx, ly, lz, lyaw, 0F);
							lworld.addEntity(ldool);
							PlayerEntity pl = ctx.get().getSender();
							lworld.playSound(pl, new BlockPos(pl.posX,pl.posY,pl.posZ), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS ,0.5F, 0.4F / ((new Random()).nextFloat() * 0.4F + 0.8F));
							ModLog.log().debug("SpawnFigure: "+lname+", "+lx+", "+ly+", "+lz+" Server.");
						} catch (Exception e) {
							ModLog.log().debug("SpawnFigure: failed.");
						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
