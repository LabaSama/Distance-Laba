package cn.Laba.Distance.ui.cloudmusic.utils;

import cn.Laba.Distance.api.EventBus;
import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.util.anim.AnimationUtil;
import cn.Laba.Distance.api.events.Render.EventRender2D;
import net.minecraft.client.Minecraft;

public class SpectrumUtil {

    public SpectrumUtil(){
        EventBus.getInstance().register(this);
    }
    float[] spectrum;

    public void updateSpectrum(float[] spectrum) {
        if (this.spectrum != null){
            for (int i = 0; i < spectrum.length; i++) {
                // ������ڸ���ֵ�͸���
                if (this.spectrum[i] < spectrum[i])
                this.spectrum[i] = AnimationUtil.moveUD(this.spectrum[i],spectrum[i],30f / Minecraft.getDebugFPS(),28f / Minecraft.getDebugFPS());
            }
        }else {
            this.spectrum = spectrum;
        }
    }

    // ��ȡƵ��
    public float[] getSpectrum() {
        if (spectrum != null) {
            return spectrum;
        } else {
            return new float[]{0f};
        }
    }

    @EventHandler
    public void onRender(EventRender2D e){
        if (spectrum != null) {
            for (int i = 0; i < spectrum.length; i++) {
                // �����İ�Ƶ�׽���0
                spectrum[i] = AnimationUtil.moveUD(spectrum[i], 0, 2f / Minecraft.getDebugFPS(), 1f / Minecraft.getDebugFPS());
            }
        }
    }
}
