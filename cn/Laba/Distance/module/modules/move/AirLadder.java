package cn.Laba.Distance.module.modules.move;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.World.EventMotionUpdate;
import cn.Laba.Distance.api.value.Option;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.time.TimerUtil;

public class AirLadder extends Module {
    public Option hytBypass = new Option("HYTAntiFlag",false);
    public final TimerUtil timer = new TimerUtil();
    public AirLadder(){
        super("AirLadder",new String[]{"airladder"}, ModuleType.Movement);
        addValues(hytBypass);
    }
    boolean enable = true;
    @EventHandler
    public void onUpdate(EventMotionUpdate e){
        if (hytBypass.getValue()) {
            setSuffix("HuaYuTing" + " " + (enable ? "On" : "Off"));
            if (timer.hasReached(2000)) {
                enable = !enable;
                timer.reset();
            }
        }else {
            enable = true;
            setSuffix("");
        }
        if (enable&&(mc.thePlayer.isOnLadder()&&mc.gameSettings.keyBindJump.pressed))mc.thePlayer.motionY=0.11;
    }
}
