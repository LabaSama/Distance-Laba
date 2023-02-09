package cn.Laba.Distance.api.events.Misc;

import cn.Laba.Distance.api.Event;

//EntityPlayerSP
public class EventPushBlock extends Event {
    boolean isPre;
    public void fire(boolean pre) {
        this.isPre = pre;
    }
    public boolean isPre() {
        return isPre;
    }

}
