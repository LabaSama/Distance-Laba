package cn.Laba.Distance.command.commands;


import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.util.misc.Helper;
import cn.Laba.Distance.ui.notifications.user.Notifications;

public class NotificationTest extends Command {
    public NotificationTest() {
        super("NotificationTest",new String[]{"noti"}, "", "����֪ͨϵͳ");
    }
    @Override
    public String execute(String[] args) {
        if (args.length == 0) {
            Helper.sendMessage(".noti [notify/warning/info/f]");
        } else {
            Notifications not = Notifications.getManager();
            if (args[0].equalsIgnoreCase("notify")) {
                not.post("Speed", "��⵽���أ�", 2500L, Notifications.Type.NOTIFY);
            } else if (args[0].equalsIgnoreCase("warning")) {
                not.post("Bypass", "��⵽Hypixel!����Disablerû������", 2500L, Notifications.Type.WARNING);
            } else if (args[0].equalsIgnoreCase("info")) {
                not.post("AutoFish", "ò���ж����Ϲ���( >��< )", 2500L, Notifications.Type.INFO);
            } else if (args[0].equalsIgnoreCase("f")) {
                not.post("FFFFFF", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 2500L, Notifications.Type.INFO);
            }else {
                Helper.sendMessage(" ???ʲô��");
            }

        }
        return null;
    }

    public String getUsage() {
        return null;
    }
}
