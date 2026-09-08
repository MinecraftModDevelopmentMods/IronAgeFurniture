package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import com.google.common.collect.Lists;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LightSourceSconceRedTorchWall extends LightSourceSconceTorchWall {
    protected static final Map<World, List<Toggle>> RECENT_TOGGLES = new WeakHashMap<World, List<Toggle>>();
    protected static final int RECENT_TOGGLE_TIMER = 60;
    protected static final int MAX_RECENT_TOGGLES = 8;
    protected static final int RESTART_DELAY = 160;

    public LightSourceSconceRedTorchWall(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
        this.setLightLevel(8.0F / 15.0F);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = Lists.newArrayList();
        drops.add(MetalVariantHelper.getDrop(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, world, pos));
        drops.add(new ItemStack(Blocks.REDSTONE_TORCH, 1));
        return drops;
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (!IsLit()) {
            return;
        }

        EnumFacing facing = state.getValue(FACING);
        double baseX;
        double baseZ;

        switch (facing) {
            case WEST:
                baseX = 0.4D;
                baseZ = 0.5D;
                break;
            case NORTH:
                baseX = 0.5D;
                baseZ = 0.4D;
                break;
            case SOUTH:
                baseX = 0.5D;
                baseZ = 0.6D;
                break;
            default:
                baseX = 0.6D;
                baseZ = 0.5D;
                break;
        }

        EnumFacing opposite = facing.getOpposite();
        double x = pos.getX() + baseX + (0.27D * opposite.getFrontOffsetX());
        double y = pos.getY() + 1.02D;
        double z = pos.getZ() + baseZ + (0.27D * opposite.getFrontOffsetZ());

        world.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
                                    EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (tryTakeLightOut(worldIn, pos, state, playerIn, hand, heldItem)) {
            return true;
        }

        if (heldItem.getItem() == Items.WATER_BUCKET) {
            if (!worldIn.isRemote) {
                MetalVariantHelper.replaceBlockPreservingMetal(worldIn, pos,
                    GetUnlitTorchVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)),
                    3);
            }

            return true;
        }

        if (isBlockedFilledSconceItem(heldItem)) {
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (IsLit()) {
            notifyNeighbors(worldIn, pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (IsLit()) {
            notifyNeighbors(worldIn, pos);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        super.neighborChanged(state, worldIn, pos, blockIn);

        if (worldIn.getBlockState(pos).getBlock() != this) {
            return;
        }

        if (IsLit() == hasNeighborSignal(worldIn, pos, state) && !worldIn.isUpdateScheduled(pos, this)) {
            worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean hasSignal = hasNeighborSignal(worldIn, pos, state);
        pruneRecentToggles(worldIn);

        if (IsLit()) {
            if (hasSignal) {
                MetalVariantHelper.replaceBlockPreservingMetal(worldIn, pos,
                    GetUnlitTorchVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)),
                    3);

                if (isToggledTooFrequently(worldIn, pos, true)) {
                    worldIn.playEvent(1502, pos, 0);
                    worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), RESTART_DELAY);
                }
            }
        } else if (!hasSignal && !isToggledTooFrequently(worldIn, pos, false)) {
            MetalVariantHelper.replaceBlockPreservingMetal(worldIn, pos,
                GetLitVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)),
                3);
        }
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return IsLit() && state.getValue(FACING) != side ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? this.getWeakPower(state, blockAccess, pos, side) : 0;
    }

    @Override
    public int tickRate(World worldIn) {
        return 2;
    }

    protected boolean hasNeighborSignal(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing direction = state.getValue(FACING).getOpposite();
        return worldIn.isSidePowered(pos.offset(direction), direction);
    }

    protected void pruneRecentToggles(World worldIn) {
        List<Toggle> list = RECENT_TOGGLES.get(worldIn);

        while (list != null && !list.isEmpty() && worldIn.getTotalWorldTime() - list.get(0).when > RECENT_TOGGLE_TIMER) {
            list.remove(0);
        }
    }

    protected boolean isToggledTooFrequently(World worldIn, BlockPos pos, boolean addNewToggle) {
        List<Toggle> list = RECENT_TOGGLES.get(worldIn);

        if (list == null) {
            list = Lists.newArrayList();
            RECENT_TOGGLES.put(worldIn, list);
        }

        if (addNewToggle) {
            list.add(new Toggle(pos, worldIn.getTotalWorldTime()));
        }

        int toggleCount = 0;

        for (Toggle toggle : list) {
            if (!toggle.pos.equals(pos)) {
                continue;
            }

            toggleCount++;

            if (toggleCount >= MAX_RECENT_TOGGLES) {
                return true;
            }
        }

        return false;
    }

    protected void notifyNeighbors(World worldIn, BlockPos pos) {
        for (EnumFacing direction : EnumFacing.values()) {
            worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
        }
    }

    @Override
    protected Block LightDrop() {
        return Blocks.REDSTONE_TORCH;
    }

    @Override
    protected boolean CanEx() {
        return false;
    }

    protected boolean IsLit() {
        return true;
    }

    protected Block GetLitVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron;
    }

    @Override
    protected Block DropVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
    }

    @Override
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron;
    }

    @Override
    protected Block GetTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron;
    }

    @Override
    protected Block GetGlowVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron;
    }

    @Override
    protected Block GetLavaVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron;
    }

    @Override
    protected Block GetRedTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron;
    }

    @Override
    protected Block GetRedVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron;
    }

    @Override
    protected Block GetUnlitTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron_unlit;
    }

    protected static class Toggle {
        final BlockPos pos;
        final long when;

        Toggle(BlockPos pos, long when) {
            this.pos = pos;
            this.when = when;
        }
    }
}
