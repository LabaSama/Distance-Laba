package cn.Laba.Distance.module.modules.player.Nofalls.impl;

import cn.Laba.Distance.module.modules.player.Nofalls.NofallModule;
import cn.Laba.Distance.api.events.World.EventMotionUpdate;
import cn.Laba.Distance.api.events.World.EventPacketSend;
import cn.Laba.Distance.api.events.World.EventPreUpdate;

public class SpoofGroundNoFall implements NofallModule {
    @Override
    public void onEnable() {

    }

    @Override
    public void onUpdate(EventPreUpdate e) {
        if (mc.thePlayer.fallDistance > 2.5) {
            e.setOnground(true);
        }
    }

    @Override
    public void onPacketSend(EventPacketSend e) {

    }

    @Override
    public void onUpdateMotion(EventMotionUpdate e) {

    }
}
