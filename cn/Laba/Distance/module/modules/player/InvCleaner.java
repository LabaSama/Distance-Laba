package cn.Laba.Distance.module.modules.player;

import java.util.ArrayList;

import cn.Laba.Distance.api.EventHandler;
import cn.Laba.Distance.api.events.World.EventPreUpdate;
import cn.Laba.Distance.api.value.Mode;
import cn.Laba.Distance.api.value.Numbers;
import cn.Laba.Distance.api.value.Option;
import cn.Laba.Distance.manager.ModuleManager;
import cn.Laba.Distance.module.Module;
import cn.Laba.Distance.module.ModuleType;
import cn.Laba.Distance.util.entity.InventoryUtils;
import cn.Laba.Distance.util.time.TimerUtil;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/*
 * Made by LeakedPvP
 */
public class InvCleaner extends Module {
	private final Numbers<Double> BlockCap = new Numbers<>("BlockCap", 128.0D, 0.0D, 256.0D, 8.0D);
	private final Numbers<Double> Delay = new Numbers<>("Delay", 1.0D, 0.0D, 10.0D, 1.0D);
	private final Option Food = new Option("Food", true);
	private final Option sort = new Option("sort", true);
	private final Option Archery = new Option("Archery", true);
	private final Option Sword = new Option("Sword", true);
	private final cn.Laba.Distance.api.value.Mode Mode = new Mode("Mode", "Mode", EMode.values(), EMode.Basic);
	private final Option JSword = new Option("JustKeepSword", true);

	private final Option InvCleaner = new Option("InvCleaner", true);
	private final Option UHC = new Option("UHC", "UHC", false);
	private final Option toogle = new Option("AutoDisable", false);


	public InvCleaner() {
		super("InvManager", new String[]{"InvCleaner"}, ModuleType.Player);
		this.addValues(BlockCap, Delay, Food, Archery, Sword, JSword, Mode, InvCleaner, sort, UHC, toogle);
	}

	public static int weaponSlot = 36, pickaxeSlot = 37, axeSlot = 38, shovelSlot = 39;
	TimerUtil timer = new TimerUtil();
	ArrayList<Integer> whitelistedItems = new ArrayList<>();

	public void onEnable() {
		super.onEnable();
	}

	@EventHandler
	public void onEvent(EventPreUpdate event) {
		if (mc.thePlayer.openContainer instanceof ContainerChest && mc.currentScreen instanceof GuiContainer) return;
		InvCleaner i3 = (InvCleaner) ModuleManager.getModuleByClass(InvCleaner.class);
		long delay = this.Delay.getValue().longValue() * 50;
		this.setSuffix(this.Mode.getValue());
		long Adelay = AutoArmor.DELAY.getValue().longValue() * 50;

		if (timer.hasReached(Adelay) && i3.isEnabled()) {
			if (!(i3.Mode.getValue() == AutoArmor.EMode.OpenInv) || mc.currentScreen instanceof GuiInventory) {
				if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat) {
					getBestArmor();
				}
			}
		}
		if (i3.isEnabled())
			for (int type = 1; type < 5; type++) {
				if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
					ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
					if (!AutoArmor.isBestArmor(is, type)) {
						return;
					}
				} else if (invContainsType(type - 1)) {
					return;
				}
			}
		if (this.Mode.getValue() == EMode.OpenInv && !(mc.currentScreen instanceof GuiInventory)) {
			return;
		}

		if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat) {
			if (timer.hasReached(delay) && weaponSlot >= 36) {

				if (!mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
					getBestWeapon(weaponSlot);

				} else {
					if (!isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) {
						getBestWeapon(weaponSlot);
					}
				}
			}
			if (sort.getValue()) {
				if (timer.hasReached(delay) && pickaxeSlot >= 36) {
					getBestPickaxe(pickaxeSlot);
				}
				if (timer.hasReached(delay) && shovelSlot >= 36) {
					getBestShovel(shovelSlot);
				}
				if (timer.hasReached(delay) && axeSlot >= 36) {
					getBestAxe(axeSlot);
				}
			}

			if (timer.hasReached(delay) && this.InvCleaner.getValue())
				for (int i = 9; i < 45; i++) {
					if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
						ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
						if (shouldDrop(is, i)) {
							drop(i);
							timer.reset();
							if (delay > 0)
								break;
						}
					}
				}
		}
		if (toogle.getValue()) setEnabled(false);

	}

	public void shiftClick(int slot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
	}

	public void swap(int slot1, int hotbarSlot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
	}

	public void drop(int slot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
	}

	public boolean isBestWeapon(ItemStack stack) {
		float damage = getDamage(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getDamage(is) > damage && (is.getItem() instanceof ItemSword || !this.Sword.getValue()))
					return false;
			}
		}
		return stack.getItem() instanceof ItemSword || !this.Sword.getValue();

	}

	public void getBestWeapon(int slot) {
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (isBestWeapon(is) && getDamage(is) > 0 && (is.getItem() instanceof ItemSword || !this.Sword.getValue())) {
					swap(i, slot - 36);
					timer.reset();
					break;
				}
			}
		}
	}

	private float getDamage(ItemStack stack) {
		float damage = 0;
		Item item = stack.getItem();
		if (item instanceof ItemTool) {
			ItemTool tool = (ItemTool) item;
			damage += tool.getMaxDamage();
		}
		if (item instanceof ItemSword) {
			ItemSword sword = (ItemSword) item;
			damage += sword.getDamageVsEntity();
		}
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f +
				EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
		return damage;
	}

	public boolean shouldDrop(ItemStack stack, int slot) {
		if (stack.getItem() instanceof ItemSword && JSword.getValue()) {
			return false;
		}
		if (stack.getDisplayName().contains("点击")) {
			return false;
		}
		if (stack.getDisplayName().contains("右键")) {
			return false;
		}
		if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
			return false;
		}
		if (UHC.getValue()) {
			if (stack.getItem() instanceof ItemBucket) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().startsWith("and") && stack.getDisplayName().toLowerCase().endsWith("ril")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("�?")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("apple")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("head")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("gold")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("crafting table")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("stick")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("and") && stack.getDisplayName().toLowerCase().contains("ril")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("axe of perun")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("barbarian")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("bloodlust")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("dragonchest")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("dragon sword")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("dragon armor")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("excalibur")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("exodus")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("fusion armor")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("hermes boots")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("hide of leviathan")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("scythe")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("seven-league boots")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("shoes of vidar")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("apprentice")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("master")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("vorpal")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("enchanted")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("spiked")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("tarnhelm")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("philosopher")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("anvil")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("panacea")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("fusion")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("excalibur")) {
				return false;
			}


			//中文
			if (stack.getDisplayName().toLowerCase().contains("学徒")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("大师罗盘")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("斩首之剑")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("附魔")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("巨龙之剑")) {
				return false;
			}

			if (stack.getDisplayName().toLowerCase().contains("巨龙之甲")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("刃甲")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("七国战靴")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("冰斗�?")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("哲人")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("铁砧")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("苹果")) {
				return false;
			}

			if (stack.getDisplayName().toLowerCase().contains("�?")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("永生之酒")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("丘比特之�?")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("锻炉")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("backpack")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("聚变之甲")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("背包")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("月神")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("永生")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("潮汐")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("雷斧")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("王�?�之�?")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("安都瑞尔")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("死神镰刀")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("丰饶之角")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("维达战靴")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("夺魂之刃")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("蛮人之甲")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("窃贼之靴")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("hermes")) {
				return false;
			}
			if (stack.getDisplayName().toLowerCase().contains("barbarian")) {
				return false;
			}
		}


		if ((slot == weaponSlot && isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) ||
				(slot == pickaxeSlot && isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack()) && pickaxeSlot >= 0) ||
				(slot == axeSlot && isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack()) && axeSlot >= 0) ||
				(slot == shovelSlot && isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack()) && shovelSlot >= 0)) {
			return false;
		}
		if (stack.getItem() instanceof ItemArmor) {
			for (int type = 1; type < 5; type++) {
				if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
					ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
					if (AutoArmor.isBestArmor(is, type)) {
						continue;
					}
				}
				if (AutoArmor.isBestArmor(stack, type)) {
					return false;
				}
			}
		}
		if (this.BlockCap.getValue().intValue() != 0 && stack.getItem() instanceof ItemBlock &&
				(getBlockCount() > this.BlockCap.getValue().intValue() ||
						InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock) stack.getItem()).getBlock()))) {
			return true;
		}
		if (stack.getItem() instanceof ItemPotion) {
			if (isBadPotion(stack)) {
				return true;
			}
		}
		if (stack.getItem() instanceof ItemFood && this.Food.getValue() && !(stack.getItem() instanceof ItemAppleGold)) {
			return true;
		}
		if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor) {
			return true;
		}
		if ((stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("arrow")) && (Boolean) this.Archery.getValue()) {
			return true;
		}

		return (stack.getItem().getUnlocalizedName().contains("tnt")) ||
				(stack.getItem().getUnlocalizedName().contains("stick")) ||
				(stack.getItem().getUnlocalizedName().contains("egg")) ||
				(stack.getItem().getUnlocalizedName().contains("string")) ||
				(stack.getItem().getUnlocalizedName().contains("cake")) ||
				(stack.getItem().getUnlocalizedName().contains("mushroom")) ||
				(stack.getItem().getUnlocalizedName().contains("flint")) ||
				(stack.getItem().getUnlocalizedName().contains("compass")) ||
				(stack.getItem().getUnlocalizedName().contains("dyePowder")) ||
				(stack.getItem().getUnlocalizedName().contains("feather")) ||
				(stack.getItem().getUnlocalizedName().contains("bucket")) ||
				(stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) ||
				(stack.getItem().getUnlocalizedName().contains("snow")) ||
				(stack.getItem().getUnlocalizedName().contains("fish")) ||
				(stack.getItem().getUnlocalizedName().contains("enchant")) ||
				(stack.getItem().getUnlocalizedName().contains("exp")) ||
				(stack.getItem().getUnlocalizedName().contains("shears")) ||
				(stack.getItem().getUnlocalizedName().contains("anvil")) ||
				(stack.getItem().getUnlocalizedName().contains("torch")) ||
				(stack.getItem().getUnlocalizedName().contains("seeds")) ||
				(stack.getItem().getUnlocalizedName().contains("leather")) ||
				(stack.getItem().getUnlocalizedName().contains("reeds")) ||
				(stack.getItem().getUnlocalizedName().contains("skull")) ||
				(stack.getItem().getUnlocalizedName().contains("record")) ||
				(stack.getItem().getUnlocalizedName().contains("snowball")) ||
				(stack.getItem() instanceof ItemGlassBottle) ||
				(stack.getItem().getUnlocalizedName().contains("piston"));
	}

	public ArrayList<Integer> getWhitelistedItem() {
		return whitelistedItems;
	}

	private int getBlockCount() {
		int blockCount = 0;
		for (int i = 0; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = is.getItem();
				if (is.getItem() instanceof ItemBlock && !InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock) item).getBlock())) {
					blockCount += is.stackSize;
				}
			}
		}
		return blockCount;
	}

	private void getBestPickaxe(int slot) {
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if (isBestPickaxe(is) && pickaxeSlot != i) {
					if (!isBestWeapon(is))
						if (!mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
							swap(i, pickaxeSlot - 36);
							timer.reset();
							if (this.Delay.getValue().longValue() > 0)
								return;
						} else if (!isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())) {
							swap(i, pickaxeSlot - 36);
							timer.reset();
							if (this.Delay.getValue().longValue() > 0)
								return;
						}

				}
			}
		}
	}

	private void getBestShovel(int slot) {
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if (isBestShovel(is) && shovelSlot != i) {
					if (!isBestWeapon(is))
						if (!mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
							swap(i, shovelSlot - 36);
							timer.reset();
							if (this.Delay.getValue().longValue() > 0)
								return;
						} else if (!isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())) {
							swap(i, shovelSlot - 36);
							timer.reset();
							if (this.Delay.getValue().longValue() > 0)
								return;
						}

				}
			}
		}
	}

	private void getBestAxe(int slot) {

		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if (isBestAxe(is) && axeSlot != i) {
					if (!isBestWeapon(is))
						if (!mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
							swap(i, axeSlot - 36);
							timer.reset();
							if (this.Delay.getValue().longValue() > 0)
								return;
						} else if (!isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())) {
							swap(i, axeSlot - 36);
							timer.reset();
							if (this.Delay.getValue().longValue() > 0)
								return;
						}

				}
			}
		}
	}

	private boolean isBestPickaxe(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemPickaxe))
			return false;
		float value = getToolEffect(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
					return false;
				}

			}
		}
		return true;
	}

	private boolean isBestShovel(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemSpade))
			return false;
		float value = getToolEffect(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
					return false;
				}

			}
		}
		return true;
	}

	private boolean isBestAxe(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemAxe))
			return false;
		float value = getToolEffect(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
					return false;
				}

			}
		}
		return true;
	}

	private float getToolEffect(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemTool))
			return 0;
		String name = item.getUnlocalizedName();
		ItemTool tool = (ItemTool) item;
		float value = 1;
		if (item instanceof ItemPickaxe) {
			value = tool.getStrVsBlock(stack, Blocks.stone);
			if (name.toLowerCase().contains("gold")) {
				value -= 5;
			}
		} else if (item instanceof ItemSpade) {
			value = tool.getStrVsBlock(stack, Blocks.dirt);
			if (name.toLowerCase().contains("gold")) {
				value -= 5;
			}
		} else if (item instanceof ItemAxe) {
			value = tool.getStrVsBlock(stack, Blocks.log);
			if (name.toLowerCase().contains("gold")) {
				value -= 5;
			}
		} else
			return 1f;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100d;
		return value;
	}

	private boolean isBadPotion(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ItemPotion) {
			final ItemPotion potion = (ItemPotion) stack.getItem();
			if (potion.getEffects(stack) == null)
				return true;
			for (final Object o : potion.getEffects(stack)) {
				final PotionEffect effect = (PotionEffect) o;
				if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	boolean invContainsType(int type) {

		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = is.getItem();
				if (item instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) item;
					if (type == armor.armorType) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void getBestArmor() {
		for (int type = 1; type < 5; type++) {
			if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
				if (AutoArmor.isBestArmor(is, type)) {
					continue;
				} else {
					drop(4 + type);
				}
			}
			for (int i = 9; i < 45; i++) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
					ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
					if (AutoArmor.isBestArmor(is, type) && AutoArmor.getProtection(is) > 0) {
						shiftClick(i);
						timer.reset();
						if (this.Delay.getValue().longValue() > 0)
							return;
					}
				}
			}
		}
	}

	enum EMode {
		Basic, OpenInv
	}
}
