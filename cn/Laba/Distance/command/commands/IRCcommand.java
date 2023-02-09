package cn.Laba.Distance.command.commands;
import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.manager.ModuleManager;
import cn.Laba.Distance.util.misc.Helper;
import cn.Laba.Distance.module.modules.world.IRC;


import net.minecraft.util.EnumChatFormatting;

public class IRCcommand
        extends Command {
    public IRCcommand() {
        super("IRC", new String[]{"c"}, "", "发送一个IRC消息");
    }

    @Override
    public String execute(String[] args) {
        String msg;
        if (args.length == 0) {
            Helper.sendMessageWithoutPrefix(EnumChatFormatting.GREEN+"[DistanceIRC]"+EnumChatFormatting.WHITE+".irc <text>");
            return null;
        } else {
            StringBuilder stringBuilder = null;
            boolean a = false;
            for (String s : args) {
                if (!a) {
                    stringBuilder = new StringBuilder(s);
                    a = true;
                    continue;
                }
                stringBuilder.append(" ").append(s);
            }
            msg = stringBuilder.toString();
            if (ModuleManager.getModuleByClass(IRC.class).isEnabled()) {
                IRC.sendIRCMessage(msg);
            }
        }

        return null;
    }
}





