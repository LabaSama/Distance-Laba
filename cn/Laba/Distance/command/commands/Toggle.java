package cn.Laba.Distance.command.commands;

import cn.Laba.Distance.Client;
import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.util.misc.Helper;
import net.minecraft.util.EnumChatFormatting;

public class Toggle extends Command {
	public Toggle() {
		super("t", new String[] { "toggle", "togl", "turnon", "enable" }, "", "�л�ָ��ģ��Ŀ���");
	}

	@Override
	public String execute(String[] args) {
		if (args.length == 0) {
			Helper.sendMessage("Correct usage .t <module>");
			return null;
		}
		for(String s : args)
		{
		boolean found = false;
		Module m = Client.instance.getModuleManager().getAlias(s);
		if (m != null) {
			if (!m.isEnabled()) {
				m.setEnabled(true);
			} else {
				m.setEnabled(false);
			}
			found = true;
			if (m.isEnabled()) {
				Helper.sendMessage(m.getName() + (Object) ((Object) EnumChatFormatting.GRAY) + " was"
						+ (Object) ((Object) EnumChatFormatting.GREEN) + " enabled");
			} else {
				Helper.sendMessage(m.getName() + (Object) ((Object) EnumChatFormatting.GRAY) + " was"
						+ (Object) ((Object) EnumChatFormatting.RED) + " disabled");
			}
		}
		if (!found) {
			Helper.sendMessage("Module name " + (Object) ((Object) EnumChatFormatting.RED) + s
					+ (Object) ((Object) EnumChatFormatting.GRAY) + " is invalid");
		}
		
	}
		return null;
	}
}
