package cn.Laba.Distance.module.modules.player.Nofalls;

import cn.Laba.Distance.api.events.World.EventMotionUpdate;
import cn.Laba.Distance.api.events.World.EventPacketSend;
import cn.Laba.Distance.api.events.World.EventPreUpdate;
import net.minecraft.client.Minecraft;

public interface NofallModule {
    Minecraft mc = Minecraft.getMinecraft();

    void onEnable();
    void onUpdate(EventPreUpdate e);
    void onPacketSend(EventPacketSend e);
    void onUpdateMotion(EventMotionUpdate e);
}
