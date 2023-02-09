package cn.Laba.Distance.command.commands;

import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.util.math.MathUtil;
import cn.Laba.Distance.util.misc.Helper;
import cn.Laba.Distance.util.time.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.EnumChatFormatting;

public class VClip extends Command {
	private TimerUtil timer = new TimerUtil();

	public VClip() {
		super("Vc", new String[] { "Vclip", "clip", "verticalclip", "clip" }, "", "���´���ָ������");
	}

	@Override
	public String execute(String[] args) {
		if (args.length > 0) {
			if (MathUtil.parsable(args[0], (byte) 4)) {
				float distance = Float.parseFloat(args[0]);
				Minecraft.getMinecraft().getNetHandler().addToSendQueue(
						new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX,
								Minecraft.getMinecraft().thePlayer.posY + (double) distance,
								Minecraft.getMinecraft().thePlayer.posZ, false));
				Helper.mc.getMinecraft().thePlayer.setPosition(Helper.mc.getMinecraft().thePlayer.posX,
						Helper.mc.getMinecraft().thePlayer.posY + (double) distance,
						Helper.mc.getMinecraft().thePlayer.posZ);
				Helper.sendMessage("Vclipped " + distance + " blocks");
			} else {
				Helper.sendMessage((Object) ((Object) EnumChatFormatting.GRAY) + args[0] + " is not a valid number");
			}
		} else {
			Helper.sendMessage((Object) ((Object) EnumChatFormatting.GRAY) + "Correct usage .vclip <number>");
		}
		return null;
	}
}
