package cn.Laba.Distance.command.commands;

import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.manager.CommandManager;
import cn.Laba.Distance.util.misc.Helper;

public class Help extends Command {
	public Help() {
		super("Help", new String[] { "list" }, "", "�г����п���ָ��");
	}

	@Override
	public String execute(String[] args) {
		if (args.length == 0) {
			Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
			Helper.sendMessageWithoutPrefix("\u00a7b\u00a7lDistance");
            for (Command c : CommandManager.commands){
            	if (c.getHelp().contains("���ô�ģ��"))continue;
				Helper.sendMessageWithoutPrefix("\u00a7b."+c.getName().toLowerCase()+" >\u00a77 "+c.getHelp());
			}
			Helper.sendMessageWithoutPrefix("\u00a7b.(ģ����) >\u00a77 ����ģ�����");

			Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
		} else {
			Helper.sendMessage("Correct usage .help");
		}
		return null;
	}
}
