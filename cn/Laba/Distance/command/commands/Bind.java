package cn.Laba.Distance.command.commands;

import cn.Laba.Distance.Client;
import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.util.misc.Helper;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

public class Bind extends Command {
	public Bind() {
		super("Bind", new String[] { "b" }, "", "��ģ�鵽ָ������");
	}

	@Override
	public String execute(String[] args) {
		if (args.length >= 2) {
			Module m = Client.instance.getModuleManager().getAlias(args[0]);
			if (m != null) {
				int k = Keyboard.getKeyIndex((String) args[1].toUpperCase());
				m.setKey(k);
				Object[] arrobject = new Object[2];
				arrobject[0] = m.getName();
				arrobject[1] = k == 0 ? "none" : args[1].toUpperCase();
				Helper.sendMessage(String.format("��ģ�� %s �� %s", arrobject));
			} else {
				Helper.sendMessage("ģ��: " + (Object) ((Object) EnumChatFormatting.RED) + args[0]
						+ (Object) ((Object) EnumChatFormatting.GRAY) + " ������");
			}
		} else {
			Helper.sendMessage("�﷨���� .bind <module> <key>");
		}
		return null;
	}
}
