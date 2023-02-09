package cn.Laba.Distance.command.commands;

import java.util.ArrayList;
import java.util.List;

import cn.Laba.Distance.Client;
import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.manager.ModuleManager;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.util.misc.Helper;
import net.minecraft.util.EnumChatFormatting;



public class Hidden extends Command {
	public static List<String> list = new ArrayList();

	public Hidden() {
		super("hidden", new String[]{"h", "hide"}, "", "����ָ��ģ��");
	}

	@Override
	public String execute(String[] args) {
		if (args.length == 0) {
			Helper.sendMessage("��ȷ�÷�: .h <module>");
			return null;
		}

		if (args[0].equalsIgnoreCase("clear")){
			for (Module m: ModuleManager.getModules()){
				if (m.wasRemoved()) {
					m.setRemoved(false);
				}
			}
			Helper.sendMessage("������������ص�ģ��");
			return null;
		}

		for (String s : args) {
			boolean found = false;
			Module m = Client.instance.getModuleManager().getAlias(s);
			if (m != null) {
				found = true;
				if (!m.wasRemoved()) {
					m.setRemoved(true);
					Helper.sendMessage(m.getName() + EnumChatFormatting.GRAY + " �ѱ�"
							+ EnumChatFormatting.RED + "����");
				} else {
					m.setRemoved(false);
					Helper.sendMessage(m.getName() + EnumChatFormatting.GRAY + " �ѻָ�"
							+ EnumChatFormatting.GREEN + "��ʾ");
				}
			}
			if (!found) {
				Helper.sendMessage("ģ��:" + EnumChatFormatting.RED + s
						+ EnumChatFormatting.GRAY + "������");
			}
		}
		return null;
	}
}
