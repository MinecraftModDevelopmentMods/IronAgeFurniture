package zone.moddev.mc.ironagefurniture.api.Blocks;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;

import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class GoldBars extends BlockPane {
	public GoldBars() {
		super(Material.IRON, true);
		this.setHardness(3.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
	}
}
