package cn.Laba.Distance.module.modules.player;

import java.awt.Color;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.Misc.EventChat;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;

public class AutoReconnect
extends Module {
    private float old;

    public AutoReconnect() {
        super("AutoReconnect", new String[]{"AutoReconnect", "AutoReconnect", "AutoReconnect"}, ModuleType.Player);
        this.setColor(new Color(244, 255, 149).getRGB());
    }

    @EventHandler
    private void onChat(EventChat e) {
        if(e.getMessage().contains("Flying or related."))mc.thePlayer.sendChatMessage("/back");
    }

}

