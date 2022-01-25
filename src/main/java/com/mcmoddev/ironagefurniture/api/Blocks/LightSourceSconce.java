/*package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.Random;
import com.mcmoddev.ironagefurniture.api.util.Coordinates;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public abstract class LightSourceSconce extends LightSource {

	private Coordinates _flameCoordinate;
	
	public LightSourceSconce(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn, name, resistance, hardness);
		
		
		_flameCoordinate = new Coordinates(0.5D, 0.7D, 0.5D);
	}

	protected void SetFlameCoords(double x, double y, double z) {
		_flameCoordinate.X = x;
		_flameCoordinate.Y = y;
		_flameCoordinate.Z = z;
	}
	
	protected void SetFlameCoords(Coordinates coordinates) {
		_flameCoordinate = coordinates;
	}
	
	protected Coordinates GetFlameCoords() {
		return _flameCoordinate;
	}
	
	protected boolean ShowFlame() {
		return true;
	}
	
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
		if (ShowFlame()) {
			EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
			Coordinates currentFlameCoordinates = _flameCoordinate;
			
			if (enumfacing == EnumFacing.EAST)
				currentFlameCoordinates = getEast(_flameCoordinate);
			
			if (enumfacing == EnumFacing.SOUTH)
				currentFlameCoordinates = getSouth(_flameCoordinate);
			
			if (enumfacing == EnumFacing.WEST)
				currentFlameCoordinates = getWest(_flameCoordinate);
			
	        double x = (double)pos.getX() + currentFlameCoordinates.X;
	        double y = (double)pos.getY() + currentFlameCoordinates.Y;
	        double z = (double)pos.getZ() + currentFlameCoordinates.Z;
	        
	        if (enumfacing.getAxis().isHorizontal())
	        {
	            EnumFacing enumfacing1 = enumfacing.getOpposite();
	            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.1D * (double)enumfacing1.getFrontOffsetX(), y + 0.33D, z - 0.1D * (double)enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
	            worldIn.spawnParticle(EnumParticleTypes.FLAME, x - 0.1D * (double)enumfacing1.getFrontOffsetX(), y + 0.33D, z - 0.1D * (double)enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
	        }
	        else
	        {
	            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
	            worldIn.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
	        }      
		}
    }
}
*/