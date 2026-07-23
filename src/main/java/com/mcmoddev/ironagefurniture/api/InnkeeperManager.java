package com.mcmoddev.ironagefurniture.api;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityHangingInnSign;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public final class InnkeeperManager {
	private static final String ROLE_TAG = "IronAgeInnkeeper";
	private static final String SIGN_X_TAG = "IronAgeInnSignX";
	private static final String SIGN_Y_TAG = "IronAgeInnSignY";
	private static final String SIGN_Z_TAG = "IronAgeInnSignZ";
	private static final String SIGN_DIM_TAG = "IronAgeInnSignDimension";
	private static final String PREVIOUS_PROFESSION_TAG = "IronAgePreviousProfession";
	private static final String PREVIOUS_PROFESSION_NAME_TAG = "IronAgePreviousProfessionName";
	private static VillagerProfession profession;

	private InnkeeperManager() {
	}

	public static void init() {
		if (!IronAgeFurnitureConfiguration.GENERATE_INNKEEPERS) return;
		profession = new VillagerProfession(Ironagefurniture.MODID + ":innkeeper",
			"minecraft:textures/entity/villager/butcher.png",
			"minecraft:textures/entity/zombie_villager/zombie_villager.png");
		VillagerRegistry.instance().register(profession);
		new VillagerCareer(profession, "innkeeper").addTrade(1,
			new EntityVillager.ListItemForEmeralds(Items.GLASS_BOTTLE,
				new EntityVillager.PriceInfo(-8, -8)));
		MinecraftForge.EVENT_BUS.register(new InnkeeperManager());
	}

	public static boolean assignNearest(TileEntityHangingInnSign sign, EntityPlayer owner) {
		if (sign == null || owner == null || sign.getWorld() == null) return false;
		World world = sign.getWorld();
		BlockPos pos = sign.getPos();
		int horizontal = IronAgeFurnitureConfiguration.INN_RADIUS_HORIZONTAL;
		int vertical = IronAgeFurnitureConfiguration.INN_RADIUS_VERTICAL;
		AxisAlignedBB area = new AxisAlignedBB(pos.getX() - horizontal, pos.getY() - vertical,
			pos.getZ() - horizontal, pos.getX() + horizontal + 1, pos.getY() + vertical + 1,
			pos.getZ() + horizontal + 1);
		List<EntityVillager> villagers = world.getEntitiesWithinAABB(EntityVillager.class, area);
		EntityVillager nearest = null;
		double distance = Double.MAX_VALUE;
		for (EntityVillager villager : villagers) {
			if (villager.isChild() || (!IronAgeFurnitureConfiguration.INTEGRATION_MCA && isMca(villager))) continue;
			if (isAssigned(villager) && getAssignedSign(villager) != null) continue;
			double candidate = villager.getDistanceSq(pos);
			if (candidate < distance) { nearest = villager; distance = candidate; }
		}
		if (nearest == null) return false;

		int previous = isMca(nearest) ? -1 : nearest.getProfession();
		String previousName = !isMca(nearest) && nearest.getProfessionForge() != null
			? nearest.getProfessionForge().getRegistryName().toString() : "";
		if (!isMca(nearest)) nearest.setProfession(profession);
		NBTTagCompound data = nearest.getEntityData();
		data.setBoolean(ROLE_TAG, true);
		data.setInteger(SIGN_X_TAG, pos.getX());
		data.setInteger(SIGN_Y_TAG, pos.getY());
		data.setInteger(SIGN_Z_TAG, pos.getZ());
		data.setInteger(SIGN_DIM_TAG, world.provider.getDimension());
		data.setInteger(PREVIOUS_PROFESSION_TAG, previous);
		if (!previousName.isEmpty()) data.setString(PREVIOUS_PROFESSION_NAME_TAG, previousName);
		String keeperName = nearest.hasCustomName() || isMca(nearest) ? nearest.getName() : "Innkeeper";
		sign.setKeeper(nearest.getUniqueID(), previous, keeperName);
		return true;
	}

	public static void release(TileEntityHangingInnSign sign) {
		if (sign == null || sign.getWorld() == null || !sign.hasKeeper()) return;
		Entity entity = findEntity(sign.getWorld(), sign.getKeeperUuid());
		if (entity instanceof EntityVillager) {
			EntityVillager villager = (EntityVillager)entity;
			if (!isMca(villager)) restoreProfession(villager, sign.getKeeperPreviousProfession());
			clearRole(villager);
		}
		sign.setKeeper(null, -1, null);
	}

	public static boolean isAssigned(Entity entity) {
		return entity != null && entity.getEntityData().getBoolean(ROLE_TAG);
	}

	public static boolean isMca(Entity entity) {
		String name = entity == null ? "" : entity.getClass().getName().toLowerCase(Locale.ROOT);
		return name.contains("radixcore") || name.contains("minecraftcomesalive") || name.contains("mca.entity");
	}

	@Nullable
	public static TileEntityHangingInnSign getAssignedSign(Entity entity) {
		if (!isAssigned(entity) || entity.getEntityWorld().provider.getDimension()
				!= entity.getEntityData().getInteger(SIGN_DIM_TAG)) return null;
		NBTTagCompound data = entity.getEntityData();
		TileEntity tile = entity.getEntityWorld().getTileEntity(new BlockPos(data.getInteger(SIGN_X_TAG),
			data.getInteger(SIGN_Y_TAG), data.getInteger(SIGN_Z_TAG)));
		if (tile instanceof TileEntityHangingInnSign) {
			TileEntityHangingInnSign sign = (TileEntityHangingInnSign)tile;
			if (entity.getUniqueID().equals(sign.getKeeperUuid())) return sign;
		}
		if (!entity.getEntityWorld().isRemote && entity instanceof EntityVillager) {
			EntityVillager villager = (EntityVillager)entity;
			if (!isMca(villager)) restoreProfession(villager, data.getInteger(PREVIOUS_PROFESSION_TAG));
			clearRole(villager);
		}
		return null;
	}

	public static boolean isInsideInn(Entity entity, TileEntityHangingInnSign sign) {
		if (entity == null || sign == null || entity.getEntityWorld() != sign.getWorld()) return false;
		BlockPos pos = sign.getPos();
		return Math.abs(entity.posX - (pos.getX() + 0.5D)) <= IronAgeFurnitureConfiguration.INN_RADIUS_HORIZONTAL
			&& Math.abs(entity.posY - (pos.getY() + 0.5D)) <= IronAgeFurnitureConfiguration.INN_RADIUS_VERTICAL
			&& Math.abs(entity.posZ - (pos.getZ() + 0.5D)) <= IronAgeFurnitureConfiguration.INN_RADIUS_HORIZONTAL;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void interact(PlayerInteractEvent.EntityInteract event) {
		Entity target = event.getTarget();
		if (!isAssigned(target)) return;
		if (isMca(target) && !event.getEntityPlayer().isSneaking()) return;
		TileEntityHangingInnSign sign = getAssignedSign(target);
		if (sign == null || !isInsideInn(target, sign) || !isInsideInn(event.getEntityPlayer(), sign)) return;
		event.setCanceled(true);
		if (!event.getEntityPlayer().getEntityWorld().isRemote) {
			BlockPos pos = sign.getPos();
			event.getEntityPlayer().openGui(Ironagefurniture.instance, Ironagefurniture.GUI_INNKEEPER,
				sign.getWorld(), pos.getX(), pos.getY(), pos.getZ());
		}
	}

	private static void clearRole(EntityVillager villager) {
		NBTTagCompound data = villager.getEntityData();
		data.removeTag(ROLE_TAG);
		data.removeTag(SIGN_X_TAG);
		data.removeTag(SIGN_Y_TAG);
		data.removeTag(SIGN_Z_TAG);
		data.removeTag(SIGN_DIM_TAG);
		data.removeTag(PREVIOUS_PROFESSION_TAG);
		data.removeTag(PREVIOUS_PROFESSION_NAME_TAG);
	}

	private static void restoreProfession(EntityVillager villager, int fallback) {
		NBTTagCompound data = villager.getEntityData();
		if (data.hasKey(PREVIOUS_PROFESSION_NAME_TAG, 8)) {
			VillagerProfession previous = VillagerRegistry.instance().getRegistry().getValue(
				new ResourceLocation(data.getString(PREVIOUS_PROFESSION_NAME_TAG)));
			if (previous != null) {
				villager.setProfession(previous);
				return;
			}
		}
		if (fallback >= 0) villager.setProfession(fallback);
	}

	@Nullable
	private static Entity findEntity(World world, @Nullable UUID id) {
		if (id == null) return null;
		for (Entity entity : world.loadedEntityList) if (id.equals(entity.getUniqueID())) return entity;
		return null;
	}
}
