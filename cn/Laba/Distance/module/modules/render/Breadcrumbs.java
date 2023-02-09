package cn.Laba.Distance.module.modules.render;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.Render.EventRender3D;
import cn.Laba.Distance.api.events.World.EventPreUpdate;
import cn.Laba.Distance.api.value.Numbers;
import cn.Laba.Distance.api.value.Option;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.render.gl.GLUtils;

import java.awt.*;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;

public class Breadcrumbs extends Module {
    public final Numbers<Double> colorRedValue = new Numbers<>("R", 255d, 0d, 255d, 1d);
    public final Numbers<Double> colorGreenValue = new Numbers<>("G", 179d, 0d, 255d, 1d);
    public final Numbers<Double> colorBlueValue = new Numbers<>("B", 72d, 0d, 255d, 1d);
    public final Option colorRainbow = new Option("Rainbow", false);
    private final LinkedList<double[]> positions = new LinkedList<>();

    public Breadcrumbs() {
        super("Breadcrumbs", new String[]{"Breadcrumb"}, ModuleType.Render);
        this.addValues(colorRedValue,colorGreenValue,colorBlueValue,colorRainbow);
    }

    @EventHandler
    public void onRender3D(EventRender3D event) {
        final Color color = colorRainbow.getValue() ? HUD.RainbowColor : new Color(colorRedValue.getValue().intValue(), colorGreenValue.getValue().intValue(), colorBlueValue.getValue().intValue());

        synchronized (positions) {
            glPushMatrix();

            glDisable(GL_TEXTURE_2D);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_LINE_SMOOTH);
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
            mc.entityRenderer.disableLightmap();
            glBegin(GL_LINE_STRIP);

            final double renderPosX = mc.getRenderManager().viewerPosX;
            final double renderPosY = mc.getRenderManager().viewerPosY;
            final double renderPosZ = mc.getRenderManager().viewerPosZ;

            for (final double[] pos : positions)
                glVertex3d(pos[0] - renderPosX, pos[1] - renderPosY, pos[2] - renderPosZ);

            glColor4d(1, 1, 1, 1);
            glEnd();
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_LINE_SMOOTH);
            glDisable(GL_BLEND);
            glEnable(GL_TEXTURE_2D);
            glPopMatrix();
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        synchronized (positions) {
            positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null)
            return;

        synchronized (positions) {
            positions.add(new double[]{mc.thePlayer.posX,
                    mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEyeHeight() * 0.5f),
                    mc.thePlayer.posZ});
            positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        synchronized (positions) {
            positions.clear();
        }
        super.onDisable();
    }
}
