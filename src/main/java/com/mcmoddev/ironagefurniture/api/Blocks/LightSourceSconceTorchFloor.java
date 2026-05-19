package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class LightSourceSconceTorchFloor extends LightHolderSconceFloor {

    private static final AxisAlignedBB AABB = new AxisAlignedBB(
        6.0 / 16.0, 0.0,    6.0 / 16.0,
        10.0 / 16.0, 13.0 / 16.0, 10.0 / 16.0
    );

    private boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        BlockPos backPos = pos.offset(facing.getOpposite());
        return worldIn.isSidePowered(backPos, facing.getOpposite());
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos,
                                      AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
        super.addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
    }

    @Override
    public boolean isFullCube(IBlockState bs)   { return false; }
    
    @Override
    public boolean isOpaqueCube(IBlockState bs) { return false; }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    
    public LightSourceSconceTorchFloor(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setLightLevel(14.0F / 15.0F);
    }

    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = Lists.newArrayList();
        drops.add(new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, 1));
        drops.add(new ItemStack(LightDrop(), 1));
        return drops;
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        boolean hasFlame = true; // TODO: HasFlame() logic
        
        if (hasFlame) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 0.9D;
            double z = pos.getZ() + 0.5D;
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME,       x, y, z, 0.0D, 0.0D, 0.0D);
        }
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
                Block unlit = GetUnlitTorchVariant();
                worldIn.setBlockState(pos, unlit.getDefaultState() .withProperty(FACING, state.getValue(FACING)), 3 /*UPDATE_ALL*/);
            }
            
            return true;
        }
        

        if (isBlockedFilledSconceItem(heldItem)) {
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    protected boolean tryTakeLightOut(World worldIn, BlockPos pos, IBlockState state,
                                      EntityPlayer playerIn, EnumHand hand, ItemStack heldItem) {
        Item lightItem = Item.getItemFromBlock(LightDrop());

        if (heldItem == null || heldItem.stackSize <= 0) {
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos,
                    DropVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)),
                    3);

                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.setHeldItem(hand, new ItemStack(LightDrop(), 1));
                }
            }

            return true;
        }

        if (heldItem.getItem() != lightItem) {
            return false;
        }

        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos,
                DropVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)),
                3);

            if (!playerIn.capabilities.isCreativeMode) {
                if (heldItem.stackSize < heldItem.getMaxStackSize()) {
                    heldItem.stackSize++;
                } else {
                    playerIn.inventory.addItemStackToInventory(new ItemStack(LightDrop(), 1));
                }
            }
        }

        return true;
    }

    protected boolean isSconceLightSourceItem(ItemStack heldItem) {
        if (heldItem == null || heldItem.stackSize <= 0) {
            return false;
        }

        Item item = heldItem.getItem();
        return item == Item.getItemFromBlock(Blocks.TORCH)
            || item == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)
            || isItemFromBlock(item, BlockObjectHolder.light_metal_ironage_candle_floor)
            || isItemFromBlock(item, BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)
            || isItemFromBlock(item, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)
            || isItemFromBlock(item, BlockObjectHolder.light_metal_ironage_block_floor_red_clear);
    }

    protected boolean isBlockedFilledSconceItem(ItemStack heldItem) {
        return isSconceLightSourceItem(heldItem)
            || isEmptySconceItem(heldItem);
    }

    protected boolean isEmptySconceItem(ItemStack heldItem) {
        if (heldItem == null || heldItem.stackSize <= 0) {
            return false;
        }

        Item item = heldItem.getItem();
        return isItemFromBlock(item, BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron)
            || isItemFromBlock(item, BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron);
    }

    protected boolean isItemFromBlock(Item item, Block block) {
        return block != null && item == Item.getItemFromBlock(block);
    }

    protected Block UnlitVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit;
    }

    protected Block DropVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
    }

    protected Block LightDrop() {
        return Blocks.TORCH;
    }

    protected boolean CanEx() {
        return true;
    }

    protected boolean HasFlame() {
        return true;
    }

    protected boolean InvertDirection() {
        return false;
    }

    @Override
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron;
    }
    @Override
    protected Block GetGlowVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron;
    }
    @Override
    protected Block GetTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron;
    }
    @Override
    protected Block GetLavaVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron;
    }
    @Override
    protected Block GetRedTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron;
    }
    @Override
    protected Block GetSoulTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_soultorch_iron;
    }
    @Override
    protected Block GetRedVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron;
    }
    @Override
    protected Block GetSoulVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_soultorch_iron;
    }
    @Override
    protected Block GetUnlitTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit;
    }
}
