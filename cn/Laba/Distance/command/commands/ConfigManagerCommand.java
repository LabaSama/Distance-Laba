package cn.Laba.Distance.command.commands;

import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.manager.ConfigManager;
import cn.Laba.Distance.util.misc.Helper;

public class ConfigManagerCommand extends Command {
    public ConfigManagerCommand(){
        super("ConfigManager", new String[] { "cm" }, "", "����/���汾������(Ҳ������\".cm\"��ָ��)");
    }

    @Override
    public String execute(String[] args) {
        if(args.length == 2 && args[0].equalsIgnoreCase("save")){
            ConfigManager.saveConfig(args[1]);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("load")){
            ConfigManager.loadConfig(args[1]);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("remove")){
            ConfigManager.removeConfig(args[1]);
        }
        if (args.length != 2){
            Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l==================================");
            Helper.sendMessageWithoutPrefix("\u00a7b\u00a7lDistance ConfigManager");
            Helper.sendMessageWithoutPrefix("\u00a7b.cm save <������> :\u00a77 ����һ������");
            Helper.sendMessageWithoutPrefix("\u00a7b.cm load <������> :\u00a77 ����һ������");
            Helper.sendMessageWithoutPrefix("\u00a7b.cm remove <������> :\u00a77 �Ƴ�һ������");
            Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l==================================");
        }
        return null;
    }
}
