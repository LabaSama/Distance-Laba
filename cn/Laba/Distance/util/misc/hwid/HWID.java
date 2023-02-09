package cn.Laba.Distance.util.misc.hwid;


import cms.mortalchen.chimera.irc.MyBufferedReader;
import cms.mortalchen.chimera.irc.MyPrintWriter;
import cms.mortalchen.chimera.irc.utils.IRCUtils;
import cms.mortalchen.chimera.irc.utils.packets.IRCPacket;
import cms.mortalchen.chimera.irc.utils.packets.IRCType;
import cms.mortalchen.chimera.irc.utils.packets.clientside.ClientHandShakePacket;
import cms.mortalchen.chimera.irc.utils.packets.clientside.ClientVerifyPacket;
import cms.mortalchen.chimera.irc.utils.packets.serverside.ServerHandShakePacket;
import cms.mortalchen.chimera.irc.utils.packets.serverside.ServerVerifyResultPacket;
import cms.mortalchen.encryption.RSA;
import cn.Laba.Distance.Client;
import net.minecraft.client.main.Main;

import javax.swing.*;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Socket;


public class HWID {
    public static void genHWID() throws Exception {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        Socket socket = new Socket("disirc.casodo.cc", 44413);
        MyPrintWriter pw = new MyPrintWriter(socket.getOutputStream(), true);
        MyBufferedReader br = new MyBufferedReader(new InputStreamReader(socket.getInputStream()));
        RSA.genKey();
        pw.println(new ClientHandShakePacket(System.currentTimeMillis(),RSA.PUBLIC_KEY).toJson());
        String message = br.readLine();
        IRCPacket packet = IRCUtils.coverToPacket(message);
        if (packet.type.equals(IRCType.HANDSHAKE)){
            ServerHandShakePacket handShakePacket = (ServerHandShakePacket) packet;
            RSA.SERVER_PUBLIC_KEY = handShakePacket.content;
            pw.publicKey = RSA.SERVER_PUBLIC_KEY;
            br.privateKey = RSA.PRIVATE_KEY;
        }else {
            JOptionPane.showMessageDialog(null,"���������ݰ��쳣","Distance",JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        pw.println(new ClientVerifyPacket(System.currentTimeMillis(),"",HWIDUtil.getHWID(), Client.releaseVersion,Main.isbeta).toJson());

        message = br.readLine();
        packet = IRCUtils.coverToPacket(message);
        if (packet.type.equals(IRCType.VERIFY)) {
            ServerVerifyResultPacket verifyResultPacket = (ServerVerifyResultPacket) IRCUtils.coverToPacket(message);
            if (verifyResultPacket.content.startsWith("false")) {
                String[] str = verifyResultPacket.content.split(":");
                if (str[1].equalsIgnoreCase("version")) {
                    JOptionPane.showInputDialog(null,
                            Main.isbeta ?  "��Beta�ͻ����ѹ�ʱ" : "@��@��@��@��@��@��@��@ʱ@,@��@��@��@��@��@��@".replace("@","")
                            , Client.releaseVersion);
                    Runtime.getRuntime().exec("taskkill /f /pid " + Integer.parseInt(runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@"))));
                    Runtime.getRuntime().exit(0);
                } else if (str[1].equalsIgnoreCase("hwid")) {
                    JOptionPane.showInputDialog(null, Main.isbeta ? "������Beta�û�" : "��û��ע��nacs".replace("n","H").replace("a","W").replace("c","I").replace("s","D")
                            , HWIDUtil.getHWID());
                    Runtime.getRuntime().exec("taskkill /f /pid " + Integer.parseInt(runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@"))));
                    Runtime.getRuntime().exit(0);
                } else {
                    JOptionPane.showMessageDialog(null, "�޷��жϴ���"
                            , "Distance", JOptionPane.ERROR_MESSAGE);
                    Runtime.getRuntime().exec("taskkill /f /pid " + Integer.parseInt(runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@"))));
                    Runtime.getRuntime().exit(0);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "�޷��жϴ���"
                    , "Distance", JOptionPane.ERROR_MESSAGE);
            Runtime.getRuntime().exec("taskkill /f /pid " + Integer.parseInt(runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@"))));
            Runtime.getRuntime().exit(0);
        }
        socket.close();
    }
}
