package cn.Laba.Distance.command.commands;
import cn.Laba.Distance.command.Command;
import cn.Laba.Distance.ui.ClientNotification;
import cn.Laba.Distance.util.misc.Helper;
import net.minecraft.network.play.client.C01PacketChatMessage;


public class Say
        extends Command {
    boolean sending = false;
    public Say() {
        super("say", new String[]{"say"}, "", "����������Ϣ(�������¼�������)");
    }


    @Override
    public String execute(String[] args) {
        if (args.length == 0) {
            sending = false;
            Helper.sendClientMessage(".say <Text>", ClientNotification.Type.warning);
        } else {
            String msg = "";
            sending = true;
            int i = 0;
            while (i < args.length) {
                msg = String.valueOf(String.valueOf(String.valueOf(msg))) + args[i] + " ";
                i++;
            }
            msg = msg.substring(0, msg.length() - 1);

            mc.getNetHandler().addToSendQueueSilent(new C01PacketChatMessage(msg));
        }

        if (sending)Helper.sendClientMessage("��Ϣ�ѷ��͵���Ϸ����", ClientNotification.Type.info);
        return null;
    }
}

