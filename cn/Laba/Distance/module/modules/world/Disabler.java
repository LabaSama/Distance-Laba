package cn.Laba.Distance.module.modules.world;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.Render.EventRender2D;
import cn.Laba.Distance.api.events.World.*;
import cn.Laba.Distance.api.value.Mode;
import cn.Laba.Distance.api.value.Option;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.module.modules.world.dis.disablers.*;
import cn.Laba.Distance.util.time.MSTimer;
import cn.Laba.Distance.module.modules.world.dis.DisablerModule;
import cn.Laba.Distance.module.modules.world.dis.disablers.*;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Disabler extends Module {
    private final Mode mode = new Mode("Mode", Modes.values(), Modes.NewSpoof);
    public static Option lowerTimer = new Option("Lower timer on Lag",false);

    private final MSTimer lagTimer = new MSTimer();
    //    public static final Numbers<Double> delay = new Numbers<>("Delay",500d, 300d, 2000d, 100d);

    public Disabler() {
        super("Disabler", new String[]{"Bypass", "Patcher"}, ModuleType.World);
        addValues(mode,lowerTimer);
    }

    @Override
    public void onEnable() {
        ((Modes) mode.getValue()).get().onEnabled();
    }

    @Override
    public void onDisable() {
        ((Modes) mode.getValue()).get().onDisable();
    }

    @EventHandler
    public void onMotionUpdate(EventMotionUpdate e){
        ((Modes) mode.getValue()).get().onMotionUpdate(e);
    }

    @EventHandler
    public void onRender2d(EventRender2D e){
        ((Modes) mode.getValue()).get().onRender2d(e);

    }

    @EventHandler
    public void onPre(EventPreUpdate e) {
        setSuffix(mode.getValue());
        ((Modes) mode.getValue()).get().onUpdate(e);
        if (lowerTimer.getValue()) {
            if (!lagTimer.hasTimePassed(1000)) {
                mc.timer.timerSpeed = 0.7f;
            } else {
                mc.timer.timerSpeed = 1f;
            }
        }
    }

    @EventHandler
    public void onPacket(EventPacket e){
        ((Modes) mode.getValue()).get().onPacket(e);
        if (e.packet instanceof S08PacketPlayerPosLook) {
            lagTimer.reset();
        }
    }

    @EventHandler
    public void onPacket(EventPacketSend event) {
        ((Modes) mode.getValue()).get().onPacket(event);
    }

    @EventHandler
    public void onPacketRE(EventPacketReceive e) {
        ((Modes) mode.getValue()).get().onPacket(e);
    }

    @EventHandler
    public void onRespawn(EventWorldChanged e) {
        ((Modes) mode.getValue()).get().onWorldChange(e);
    }

    enum Modes {
        Hypxiel(new DisablerHypixelDisabler()),
        NewSpoof(new NewSpoofDisabler()),
        AAC4LessFlag(new AAC4LessFlagDisabler()),
        AAC5Test(new AAC5TestDisabler()),
        VulcanCombat(new VulcanCombatDisabler());

        final DisablerModule disablerModule;

        Modes(DisablerModule disabler) {
            disablerModule = disabler;
        }

        public DisablerModule get() {
            return disablerModule;
        }
    }
}
