
package cn.Laba.Distance.module.modules.world;

import java.awt.Color;

import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;


public class NoCommand
extends Module {
    public NoCommand() {
        super("NoCommand", new String[]{"No Command", "Commnand"}, ModuleType.World);
        this.setColor(new Color(223, 233, 233).getRGB());
    }
}
