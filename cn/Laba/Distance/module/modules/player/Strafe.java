
package cn.Laba.Distance.module.modules.player;

import java.awt.Color;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.World.EventPreUpdate;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.entity.PlayerUtil;


public class Strafe
extends Module {

    public Strafe() {
        super("Strafe", new String[]{"Strafe"}, ModuleType.Movement);
        this.setColor(new Color(208, 30, 142).getRGB());
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (PlayerUtil.MovementInput()) {
            PlayerUtil.setSpeed((double)PlayerUtil.getSpeed());
        }
    }
    }


