package cn.Laba.Distance.module.modules.move.speedmode.speed;

import cn.Laba.Distance.api.events.World.*;

import cn.Laba.Distance.module.modules.move.Speed;
import cn.Laba.Distance.module.modules.move.speedmode.SpeedModule;
import cn.Laba.Distance.util.entity.MovementUtils;
import cn.Laba.Distance.util.time.MSTimer;

public class HypixelSpeed extends SpeedModule {
    private boolean stage = false;
    private final MSTimer timer = new MSTimer();

    @Override
    public void onMotion(EventMotionUpdate e) {
        if (MovementUtils.isMoving() && mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && mc.thePlayer.jumpTicks == 0) {
            mc.thePlayer.jump();
            mc.thePlayer.jumpTicks = 10;
        }
        if (MovementUtils.isMoving() && !mc.thePlayer.onGround && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava()) {
            MovementUtils.strafe();
        }
        if (this.stage) {
            mc.timer.timerSpeed = Speed.mintimerValue.getValue().floatValue();
            if (this.timer.hasTimePassed(Speed.mintimerMisValue.get().longValue())) {
                this.timer.reset();
                this.stage = !this.stage;
            }
        } else {
            mc.timer.timerSpeed = Speed.maxtimerValue.get().floatValue();
            if (this.timer.hasTimePassed(Speed.maxtimerMisValue.get().longValue())) {
                this.timer.reset();
                this.stage = !this.stage;
            }
        }
    }

    @Override
    public void onPacketSend(EventPacketSend e) {

    }

    @Override
    public void onStep(EventStep e) {
    }

    @Override
    public void onPre(EventPreUpdate e) {
    }

    double y;
    @Override
    public void onMove(EventMove event) {
        if (MovementUtils.isMoving() && !mc.thePlayer.isInWeb && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava()) {
            double moveSpeed = Math.max(MovementUtils.getBaseMoveSpeed(), MovementUtils.getSpeed());
//            TargetStrafe targetStrafe = (TargetStrafe) ModuleManager.getModuleByClass(TargetStrafe.class);
//            if (!targetStrafe.doStrafeAtSpeed(event,moveSpeed)) {
            MovementUtils.setSpeed(event, moveSpeed);
//            }
            if (MovementUtils.isMoving() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && !mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround && Speed.sendJumpValue.get()) {
                mc.thePlayer.jump();
            }
        }
    }

    @Override
    public void onPost(EventPostUpdate e) {
    }

    @Override
    public void onEnabled() {
    }



    @Override
    public void onDisabled() {
    }
}
