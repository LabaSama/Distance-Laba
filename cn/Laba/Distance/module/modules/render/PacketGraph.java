package cn.Laba.Distance.module.modules.render;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.Render.EventRender3D;
import cn.Laba.Distance.api.events.World.EventPacketSend;
import cn.Laba.Distance.api.value.Numbers;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.render.RenderUtil;
import de.gerrygames.viarewind.utils.PacketUtil;
import cn.Laba.Distance.util.world.BlockObject;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.util.time.TimeHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;


public class PacketGraph extends Module {
    public final Numbers<Double> xValue = new Numbers<>("PacketGraph","X", 2.0d, 0.0d, 2000.0d, 1.0d);
    public final Numbers<Double> yValue = new Numbers<>("PacketGraph","Y", 10.0d, 0.0d, 2000.0d, 1.0d);
    private List<BlockObject> clientBlocks = new CopyOnWriteArrayList();
    private List<BlockObject> serverBlocks = new CopyOnWriteArrayList();
    private TimeHelper timerUtil = new TimeHelper();
    private TimeHelper secTimerUtil = new TimeHelper();
    private static int clientPackets;
    private static int serverPackets;
    private static int secClientPackets;
    private static int secServerPackets;
    private int renderSecClientPackets;
    private int renderSecServerPackets;

    public PacketGraph(){
        super("PacketGraph", new String[]{"gui"},ModuleType.Render);
        this.addValues(xValue,yValue);
    }


    public void clear() {
        clientPackets = 0;
        serverPackets = 0;
        secClientPackets = 0;
        secServerPackets = 0;
        this.renderSecClientPackets = 0;
        this.renderSecServerPackets = 0;
        this.clientBlocks.clear();
        this.serverBlocks.clear();
        this.timerUtil.reset();
        this.secTimerUtil.reset();
    }

    @EventHandler
    public void onPacket(EventPacketSend e) {
        if (e.getPacket().getClass().getSimpleName().toLowerCase().startsWith("c")) {
            clientPackets++;
            secClientPackets++;
        } else if (e.getPacket().getClass().getSimpleName().toLowerCase().startsWith("s")) {
            serverPackets++;
            secServerPackets++;
        }
    }

    @EventHandler
    public void onRender(EventRender3D event) {
        int x = this.xValue.get().intValue();
        int y = this.yValue.get().intValue();
        RenderUtil.drawBorderedRect((float) (x - 3), (float) (y - 68), (float) (x + Opcodes.IFEQ), (float) (y + 74), 1.0f, new Color(0, 0, 255).getRGB(), new Color(0, 0, 0, 0).getRGB());
        if (this.timerUtil.hasReached(50L)) {
            this.clientBlocks.forEach(blockObject -> {
                blockObject.f168x--;
            });
            this.clientBlocks.add(new BlockObject(x + Opcodes.FCMPG, Math.min(clientPackets, 55)));
            clientPackets = 0;
            this.serverBlocks.forEach(blockObject -> {
                blockObject.f168x--;
            });
            this.serverBlocks.add(new BlockObject(x + Opcodes.FCMPG, Math.min(serverPackets, 55)));
            serverPackets = 0;
            this.timerUtil.reset();
        }
        if (this.secTimerUtil.hasReached(1000L)) {
            this.renderSecClientPackets = secClientPackets;
            this.renderSecServerPackets = secServerPackets;
            secClientPackets = 0;
            secServerPackets = 0;
            this.secTimerUtil.reset();
        }
        int graphY = y;
        for (int i = 0; i < 2; i++) {
            drawGraph(i, x, graphY);
            graphY += 68;
        }
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        if (!this.clientBlocks.isEmpty()) {
            BlockObject firstBlock = this.clientBlocks.get(this.clientBlocks.size() - 1);

        }
        if (!this.serverBlocks.isEmpty()) {
            BlockObject firstBlock2 = this.serverBlocks.get(this.serverBlocks.size() - 1);

        }
        GL11.glPopMatrix();
        this.clientBlocks.removeIf(block -> {
            return block.f168x < x;
        });
        this.serverBlocks.removeIf(block -> {
            return block.f168x < x;
        });
    }

    private void drawGraph(int mode, int x, int y) {
        boolean isClient = mode == 0;
        RenderUtil.drawRect((double) x, ((double) y) + 0.5d, (double) (x + Opcodes.FCMPG), (double) (y - 60), new Color(0, 0, 0, 80).getRGB());
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        String secString = isClient ? this.renderSecClientPackets + " packets/sec" : this.renderSecServerPackets + " packets/sec";
        GL11.glPopMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glLineWidth(1.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBegin(1);
        int rainbowTicks = 0;
        List<BlockObject> list = isClient ? this.clientBlocks : this.serverBlocks;
        for (BlockObject block : list) {
            RenderUtil.glColor(1,1,1,1);
            GL11.glVertex2d((double) block.f168x, (double) (y - block.height));
            try {
                GL11.glVertex2d((double) (block.f168x + 1), (double) (y - list.get(list.indexOf(block) + 1).height));
            } catch (Exception e) {
            }
            rainbowTicks += 300;
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);

    }
}
