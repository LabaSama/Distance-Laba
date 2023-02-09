package cn.Laba.Distance.module.modules.world;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.World.EventTick;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;

import java.awt.Color;

public class FastPlace extends Module {
	public FastPlace() {
		super("FastPlace", new String[] { "fplace", "fc" }, ModuleType.World);
		this.setColor(new Color(226, 197, 78).getRGB());
	}

	@EventHandler
	private void onTick(EventTick e) {
		this.mc.rightClickDelayTimer = 0;
	}
}
