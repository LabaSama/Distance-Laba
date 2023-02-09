package cn.Laba.Distance.api.events.World;

import cn.Laba.Distance.api.Event;
import net.minecraft.client.audio.ISound;

public class EventSound extends Event {
    public ISound sound;
    public EventSound(ISound iSound){
        sound = iSound;
    }
}
