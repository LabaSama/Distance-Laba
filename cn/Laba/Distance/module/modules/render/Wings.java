package cn.Laba.Distance.module.modules.render;

import java.awt.Color;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.Render.EventRender3D;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.render.RenderWings;

public class Wings
extends Module {
	//public static Option<Boolean> Rainbow = new Option<Boolean>("Rainbow", "Rainbow", false);
    public Wings() {
        super("Wings", new String[]{"Wings"}, ModuleType.Render);
        this.setColor(new Color(208, 30, 142).getRGB());
        //super.addValues(Rainbow);
    }
    @EventHandler
    public void onRenderPlayer(EventRender3D event) {
        RenderWings renderWings = new RenderWings();
        renderWings.renderWings(event.getPartialTicks());
}

    }


