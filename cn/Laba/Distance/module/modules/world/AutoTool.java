package cn.Laba.Distance.module.modules.world;


import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.World.EventPacketSend;
import cn.Laba.Distance.api.events.World.EventTick;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.world.BlockUtils;
import net.minecraft.util.BlockPos;

public class AutoTool extends Module {
	public AutoTool() {
		super("AutoTool", new String[] {"AutoTool"}, ModuleType.Player);
    }
	public Class type() {
        return EventPacketSend.class;
    }

	@EventHandler
	    public void onEvent(EventTick event) {
	        if (!mc.gameSettings.keyBindAttack.isKeyDown()) {
	            return;
	        }
	        if (mc.objectMouseOver == null) {
	            return;
	        }
	        BlockPos pos = mc.objectMouseOver.getBlockPos();
	        if (pos == null) {
	            return;
	        }
	        BlockUtils.updateTool(pos);
	    }
	}
