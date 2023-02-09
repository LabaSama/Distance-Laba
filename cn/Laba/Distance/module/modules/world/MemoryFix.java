package cn.Laba.Distance.module.modules.world;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.World.EventTick;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.time.TimerUtil;

public class MemoryFix extends Module {
    private final TimerUtil mftimer = new TimerUtil();

    public MemoryFix() {
        super("MemoryFix", new String[]{"memoryfix"}, ModuleType.World);
    }

    @Override
    public void onEnable() {
        Runtime.getRuntime().gc();
        mftimer.reset();
    }

    @EventHandler
    public void onTick(EventTick e) {
        double mflimit = 10.0;
        if(mftimer.hasReached(120000) && mflimit <= ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100f / Runtime.getRuntime().maxMemory())) {
            Runtime.getRuntime().gc();
            mftimer.reset();
        }
    }
}
