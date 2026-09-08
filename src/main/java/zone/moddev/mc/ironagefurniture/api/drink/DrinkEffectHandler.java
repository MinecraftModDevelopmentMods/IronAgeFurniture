package zone.moddev.mc.ironagefurniture.api.drink;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class DrinkEffectHandler {
	private static final ResourceLocation KEY = new ResourceLocation(Ironagefurniture.MODID, "drink_effect");
	private static final int HANGOVER_TICKS = 1200;
	private static final double HEAVY_THRESHOLD = 10.0D;

	@CapabilityInject(IDrinkEffectData.class)
	public static final Capability<IDrinkEffectData> CAPABILITY = null;

	private DrinkEffectHandler() {
	}

	public static void init() {
		CapabilityManager.INSTANCE.register(IDrinkEffectData.class, new DrinkEffectProvider.Storage(),
			new Callable<IDrinkEffectData>() {
				@Override public IDrinkEffectData call() { return new DrinkEffectData(); }
			});
		MinecraftForge.EVENT_BUS.register(new DrinkEffectHandler());
	}

	public static void addServings(EntityPlayer player, double servings) {
		IDrinkEffectData data = get(player);
		if (data == null || servings <= 0.0D) return;
		data.addServings(servings);
		data.setRecoveryTicks(0);
		if (data.getServings() >= HEAVY_THRESHOLD) data.setReachedHeavyEffect(true);
	}

	@Nullable
	public static IDrinkEffectData get(EntityPlayer player) {
		return player == null || CAPABILITY == null ? null : player.getCapability(CAPABILITY, null);
	}

	@SubscribeEvent
	public void attach(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(KEY, new DrinkEffectProvider());
		}
	}

	@SubscribeEvent
	public void clone(PlayerEvent.Clone event) {
		IDrinkEffectData oldData = get(event.getOriginal());
		IDrinkEffectData newData = get(event.getEntityPlayer());
		if (!event.isWasDeath() && oldData != null && newData != null) {
			newData.deserializeNBT(oldData.serializeNBT());
		}
	}

	@SubscribeEvent
	public void wake(PlayerWakeUpEvent event) {
		if (!event.getEntityPlayer().getEntityWorld().isRemote) clearToHangover(event.getEntityPlayer());
	}

	@SubscribeEvent
	public void finishUsing(LivingEntityUseItemEvent.Finish event) {
		if (event.getEntityLiving() instanceof EntityPlayer && event.getItem() != null
				&& event.getItem().getItem() == Items.MILK_BUCKET && !event.getEntityLiving().getEntityWorld().isRemote) {
			clearToHangover((EntityPlayer)event.getEntityLiving());
		}
	}

	@SubscribeEvent
	public void tick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END || event.player.getEntityWorld().isRemote
				|| !IronAgeFurnitureConfiguration.ENABLE_DRINK_EFFECTS) return;
		IDrinkEffectData data = get(event.player);
		if (data == null) return;

		if (data.getHangoverTicks() > 0) {
			data.setHangoverTicks(data.getHangoverTicks() - 1);
			if (event.player.ticksExisted % 20 == 0) {
				apply(event.player, MobEffects.WEAKNESS, 80, 0);
				apply(event.player, MobEffects.MINING_FATIGUE, 80, 0);
				apply(event.player, MobEffects.NAUSEA, 80, 0);
			}
			return;
		}

		if (data.getServings() <= 0.0D) return;
		data.setRecoveryTicks(data.getRecoveryTicks() + 1);
		if (data.getRecoveryTicks() >= IronAgeFurnitureConfiguration.DRINK_EFFECT_RECOVERY_TICKS) {
			data.setRecoveryTicks(0);
			data.setServings(data.getServings() - 1.0D);
			if (data.getServings() <= 0.0D && data.hasReachedHeavyEffect()) beginHangover(data);
		}
		if (event.player.ticksExisted % 20 == 0) applyCurrentEffects(event.player, data.getServings());
	}

	private static void applyCurrentEffects(EntityPlayer player, double servings) {
		if (servings >= 1.0D) apply(player, MobEffects.SLOWNESS, 80, servings >= 10.0D ? 2 : servings >= 6.0D ? 1 : 0);
		if (servings >= 3.0D) apply(player, MobEffects.WEAKNESS, 80, servings >= 10.0D ? 1 : 0);
		if (servings >= 6.0D) apply(player, MobEffects.MINING_FATIGUE, 80, servings >= 10.0D ? 1 : 0);
		if (servings >= 10.0D) apply(player, MobEffects.NAUSEA, 80, 0);
	}

	private static void apply(EntityPlayer player, Potion potion, int duration, int amplifier) {
		player.addPotionEffect(new PotionEffect(potion, duration, amplifier, true, false));
	}

	private static void clearToHangover(EntityPlayer player) {
		IDrinkEffectData data = get(player);
		if (data == null) return;
		boolean heavy = data.hasReachedHeavyEffect();
		data.setServings(0.0D);
		data.setRecoveryTicks(0);
		if (heavy) beginHangover(data);
	}

	private static void beginHangover(IDrinkEffectData data) {
		data.setHangoverTicks(HANGOVER_TICKS);
		data.setReachedHeavyEffect(false);
	}
}
