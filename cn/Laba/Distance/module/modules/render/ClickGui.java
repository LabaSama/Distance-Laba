package cn.Laba.Distance.module.modules.render;

import cn.Laba.Distance.api.value.Mode;
import cn.Laba.Distance.api.value.Option;
import cn.Laba.Distance.api.verify.SHWID;
import cn.Laba.Distance.manager.Fucker;
import cn.Laba.Distance.manager.RenderManager;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.sound.SoundFxPlayer;
import cn.Laba.Distance.ui.gui.clikguis.ClickUi.ClickUi;
import cn.Laba.Distance.ui.gui.clikguis.clickgui3.ClientClickGui;

public class ClickGui extends Module {
	private static final Mode mode = new Mode("Mode", "mode", ClickGui.modes.values(), modes.Distance);
	public static final Option CustomColor = new Option("CustomColor",false);
	public ClickGui() {
		super("ClickGui", new String[] { "clickui" }, ModuleType.Render);
		this.addValues(mode,CustomColor);
	}

	@Override
	public void onEnable() {
        if (SHWID.BITCH != 0){
            RenderManager.doRender();
        }
        if (SHWID.hahaha != 1){
            Fucker.dofuck();
        }
        if (SHWID.id != 1){
            RenderManager.doRender();
            Fucker.dofuck();
        }
        if (SHWID.id2 != 2){
            RenderManager.doRender();
            Fucker.dofuck();
        }
        new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.ClickGuiOpen,-4);
	    switch ((modes)mode.getValue()){
            case Nov:{
                mc.displayGuiScreen(new ClickUi());
                break;
            }
            case Azlips:{
                mc.displayGuiScreen(new cn.Laba.Distance.ui.gui.clikguis.clickgui4.ClickGui());
                break;
            }
            case Distance:{
                mc.displayGuiScreen(new ClientClickGui());
                break;
            }
        }
		this.setEnabled(false);
	}
    enum modes{
    	Nov,
        Distance,
		Azlips
    }
}
