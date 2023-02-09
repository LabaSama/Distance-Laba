package cn.Laba.Distance.util;

import cn.Laba.Distance.api.value.Mode;
import cn.Laba.Distance.api.value.Numbers;
import cn.Laba.Distance.api.value.Option;
import cn.Laba.Distance.manager.FileManager;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.misc.Helper;

import java.util.List;

/**
 * ClientSetting
 * �ͻ����Զ�������
 * �൱�ڸ��Ի������ñ�ֽ����ɫ�õ�
 */
public class ClientSetting extends Module {
    public static int id = 1;
    public static int id2 = 0;
    public static Numbers<Double> soundFxVolume = new Numbers<>("SoundFx Volume",100d,0d,100d,1d);

    public static final Option shaderBG = new Option("Shader BackGround", false);
    public static final Mode backGround = new Mode("BackGround",backgrounds.values(),backgrounds.BG13);
    public static final Mode betterFPS = new Mode("BetterFPS",betterfps.values(),betterfps.JavaMath);
    public static final Option resetBF = new Option("Reset BetterFPS", false);

    public static final Option backPackAnimation = new Option("BackPack Animation", true);
    public static final Option soundFx = new Option("Module SoundFx", true);

    public static final Option clMode = new Option("AutoCL(Netease Login)", false);
    public static final Option fakeForge = new Option("FakeForge", false);
    public static final Option keepWorld = new Option("Keep world on disconnect", false);
    public static final Option enableBlur = new Option("Enable Blur", true);

    public ClientSetting(){
        super("ClientSetting",new String[]{"ClientSetting"}, ModuleType.World);
        addValues(shaderBG,backGround,backPackAnimation,soundFx,betterFPS,resetBF,clMode,fakeForge,keepWorld,enableBlur);
        enableBlur.setValue(isBlurConfig());
        setRemoved(true);
    }
    @Override
    public void onEnable(){
        Helper.sendMessage("���ģ�鲻��Ҫ������");
        setEnabled(false);
    }

    public enum betterfps {
        Rivens("rivens"),
        LibGDX("libgdx"),
        RivensFull("rivens-full"),
        RivensHalf("rivens-half"),
        JavaMath("java"),
        Random("random"),
        Distance("distance"),
        Vanilla("vanilla");
        final String fileName;

        betterfps(String name) {
            this.fileName = name;
        }
        public String get(){
            return fileName;
        }

    }
    public enum backgrounds {
        BG1("bg.jpg"),
        BG2("bg2.jpg"),
        BG3("bg3.jpg"),
        BG4("bg4.jpg"),
        BG5("bg5.jpg"),
        BG6("bg6.jpg"),
        BG7("bg7.jpg"),
        BG8("bg8.jpg"),
        BG9("bg9.jpg"),
        BG10("bg10.jpg"),
        BG11("bg11.jpg"),
        BG12("bg12.jpg"),
        BG13("bg13.jpg");
        final String fileName;

        backgrounds(String name) {
            this.fileName = name;
        }
        public String getFileName(){
            return fileName;
        }
    }
    private boolean isBlurConfig(){
        List<String> names = FileManager.read("NeedBlur.txt");
        if (names.isEmpty()){
            return false;
        }
        for (String v : names) {
            if (v.contains("true")){
                return true;
            }else if(v.contains("false")){
                return false;
            }
        }
        return false;
    }
}
