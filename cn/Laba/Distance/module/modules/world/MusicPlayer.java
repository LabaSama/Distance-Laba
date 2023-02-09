package cn.Laba.Distance.module.modules.world;

import cn.Laba.Distance.api.value.Numbers;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.ui.cloudmusic.ui.GuiCloudMusic;

public class MusicPlayer extends Module {
    public static final Numbers<Double> musicPosYlyr = new Numbers<>("MusicPlayerLyricY", 120d, 0d, 200d, 1d);
    public MusicPlayer() {
        super("MusicPlayer",new String[]{"neteasemusicplayer","music"}, ModuleType.World);
        addValues(musicPosYlyr);
    }

    @Override
    public void onEnable(){
        mc.displayGuiScreen(new GuiCloudMusic());
        this.setEnabled(false);
    }
}
