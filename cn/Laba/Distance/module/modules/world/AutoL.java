package cn.Laba.Distance.module.modules.world;

import cn.Laba.Distance.Client;
import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.Misc.EventChat;
import cn.Laba.Distance.api.events.Render.EventRender2D;
import cn.Laba.Distance.api.value.Mode;
import cn.Laba.Distance.api.value.Option;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.math.RandomUtil;
import cn.Laba.Distance.util.time.TimeHelper;
import cn.Laba.Distance.util.world.SpammerUtils;
import cn.Laba.Distance.module.modules.combat.KillAura;

import java.awt.*;
import java.util.Random;

public class AutoL 
extends Module {
	static int Totals = 0;
	TimeHelper delay = new TimeHelper();
	static final String[] Hypixel = {
			"Do you know a little client...",
			"I am desperate, why am I banned...",
			"Why am I like this...",
	};
	private static final Mode mode = new Mode("Mode", LMode.values(), LMode.Hypixel);
	public static final Option sendL = new Option("SendL",false);

	public static final Option ad = new Option("AD",true);
	public static final Option randomString = new Option("RandomString",true);
	public static final Option head = new Option("Prefix",true);

	public AutoL() {
		super("AutoL", new String[]{"L"}, ModuleType.World);
		this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
		this.addValues(mode);
	}

	@EventHandler
	public void onRender2D(EventRender2D e){
		this.setSuffix(mode.getValue());
	}

	@EventHandler
	private void handleRequest(EventChat e) {
		if (e.getMessage().contains(mc.thePlayer.getName()) && e.getMessage().contains(KillAura.currentTarget.getName())){
			sayL();
		}
	}
	static int i = 0;
	public static void sayL() {
		Random r = new Random();

		switch ((LMode) mode.getValue()) {
			case Hypixel: {
				sendL(" " + Hypixel[r.nextInt(Hypixel.length)]);
				break;
			}
			case PenShen: {
				sendL(SpammerUtils.intcihui[r.nextInt(SpammerUtils.intcihui.length)]);
				break;
			}
			case Killcount: {
				Totals++;
				sendL(", " + "������ɱ�ĵ�" + Totals + "������");
				break;
			}
			case CXK: {
				sendL(SpammerUtils.CXK[random.nextInt(SpammerUtils.CXK.length)]);
				break;
			}
			case TCC: {
				if (i > SpammerUtils.TCC.length - 1)i = 0;
				sendL(SpammerUtils.TCC[i] + getTail());
				i++;
				break;
			}
			case HHAF: {
				sendL(SpammerUtils.HHAF[random.nextInt(SpammerUtils.HHAF.length)]);
				break;
			}
			case Math: {
				if (i > SpammerUtils.Maths.length - 1)i = 0;
				sendL(SpammerUtils.Maths[i]);
				i++;
				break;
			}
			case Politics: {
				if (i > SpammerUtils.Politics.length - 1)i = 0;
				sendL(SpammerUtils.Politics[i]);
				i++;
				break;
			}
		}
	}

	public static void sendL(String string){
		mc.thePlayer.sendChatMessage(
				getHead() +
				KillAura.currentTarget.getName()
						+ (sendL.getValue()?" L ":" ") + string
						+ getTail());
	}

	//TODO �ȴ���վ����
	private static String getTail(){
		return (ad.getValue()?" | get Distance -> Distance dot cool " : "") + (randomString.getValue() ? RandomUtil.randomString(5 + new Random().nextInt(5)):"");
	}
	private static String getHead(){
		return head.getValue()?"["+ Client.name +"] " :"";
	}

	enum LMode {
		Hypixel,
		PenShen,
		Killcount,
		Math,
		CXK,
		Politics,
		TCC,
		HHAF,
	}

	public static boolean isHypixelKilled(String message) {
		return message.toLowerCase().contains("was killed by " + mc.thePlayer.getName().toLowerCase() + ".") ||
				message.toLowerCase().contains("was thrown into the void by " + mc.thePlayer.getName().toLowerCase() + ".") ||
				message.toLowerCase().contains("was thrown off a cliff by " + mc.thePlayer.getName().toLowerCase() + ".");
	}
}

