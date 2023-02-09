package cn.Laba.Distance.command.commands;

import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.ui.ClientNotification;
import cn.Laba.Distance.util.misc.Helper;
import cn.Laba.Distance.module.modules.player.AutoSay;

public class Spammer extends Command {
    public Spammer() {
        super("Spammer", new String[]{"spam"}, "", "更改CustomSpammer的内容");
    }


    @Override
    public String execute(String[] args) {
        if (args.length == 0) {
            Helper.sendClientMessage(".spam <Text>", ClientNotification.Type.warning);
        } else {
            int i = 0;
            String msg = "";
            while (i < args.length) {
                msg = msg + args[i] + " ";
                i++;
            }
            msg = msg.substring(0, msg.length() - 1);
            AutoSay.CustomString = msg;
        }
        return null;
    }
}
