package cn.Laba.Distance.ui.gui;


import cn.Laba.Distance.Client;
import cn.Laba.Distance.fastuni.FontLoader;
import cn.Laba.Distance.manager.FileManager;
import cn.Laba.Distance.util.ClientSetting;
import cn.Laba.Distance.ui.font.FontLoaders;
import cn.Laba.Distance.ui.gui.verify.GuiLogin;
import cn.Laba.Distance.ui.jelloparticle.ParticleEngine;
import cn.Laba.Distance.ui.buttons.UIFlatButton;
import cn.Laba.Distance.util.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.List;


public class GuiNeedBlur extends GuiScreen {
    public ParticleEngine pe = new ParticleEngine();
    public GuiButton yesButton;
    public GuiButton noButton;
    private static boolean isBlurEnabled;

    @Override
    public void initGui(){
        int h = new ScaledResolution(this.mc).getScaledHeight();
        int w = new ScaledResolution(this.mc).getScaledWidth();
        this.yesButton = new UIFlatButton(1, (int) (w / 2f) - 20 - 25, (int) (h / 2f) + 15, 40, 20, "�ǵ�", new Color(25,25,25).getRGB());
        this.noButton = new UIFlatButton(3, (int) (w / 2f) - 20 + 25, (int) (h / 2f) + 15, 40, 20, "��Ҫ", new Color(25,25,25).getRGB());
        this.buttonList.add(this.yesButton);
        this.buttonList.add(this.noButton);
    }

    @Override
    protected void keyTyped(char var1, int var2) {

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                isBlurEnabled = true;
                FileManager.save("NeedBlur.txt","true",false);
                ClientSetting.enableBlur.setValue(true);
                mc.displayGuiScreen(new GuiLogin());
                break;
            case 3:
                isBlurEnabled = false;
                FileManager.save("NeedBlur.txt","false",false);
                ClientSetting.enableBlur.setValue(false);
                mc.displayGuiScreen(new GuiLogin());
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int h = new ScaledResolution(this.mc).getScaledHeight();
        int w = new ScaledResolution(this.mc).getScaledWidth();

        GlStateManager.translate(0, 0, 0);
        Gui.drawRect(-20, -20, w + 20, h + 20, new Color(0, 0, 0).getRGB());
        RenderUtil.drawRect(w / 2f - 30, h / 2f + 9, w / 2f - 30 + 60, h / 2f + 10, new Color(255, 255, 255).getRGB());

        if (isBlurConfiged()) {
            isBlurEnabled = setBlurEnabled();
            mc.displayGuiScreen(new GuiLogin());
            return;
        }
        pe.render(0, 0);
        Client.FontLoaders.Chinese18.drawCenteredString("����ģ��Ч��?", w / 2f, h / 2f - 25 + 4, -1);
        //(���ֵ��Բ�֧��ģ��Ч���ᵼ�º���)
        FontLoader.msFont13.drawCenteredString("���ֵ��Բ�֧��ģ��Ч���ᵼ�º����������������Ƿ�֧�ֱ�Ч���벻Ҫ����", w / 2f, h / 2f - 15 + 4, -1);
        FontLoader.msFont13.drawCenteredString("������Ǻ�����ɾ��\".minecraft/Distance/NeedBlur.txt\"���ٴη��ʱ�����", w / 2f, h / 2f - 7 + 4, -1);

        FontLoaders.GoogleSans16.drawCenteredString(Client.name+" made by \u00a7oLaba\u00a7r(Laba Team)", width / 2f, height - FontLoaders.GoogleSans16.getHeight() - 6f, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    private boolean setBlurEnabled() {
        List<String> names = FileManager.read("NeedBlur.txt");
        for (String v : names) {
            return v.contains("true");
        }
        return false;
    }
    public static boolean isBlurEnabled() {
        return ClientSetting.enableBlur.get();
    }
    private boolean isBlurConfiged(){
        List<String> names = FileManager.read("NeedBlur.txt");
        return !names.isEmpty();
    }
}
