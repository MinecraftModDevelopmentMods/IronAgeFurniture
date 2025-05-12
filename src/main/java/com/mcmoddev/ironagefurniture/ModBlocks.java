//package com.mcmoddev.ironagefurniture;
//
//import java.util.function.Supplier;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.material.Material;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;
//
//public class ModBlocks {
//    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Ironagefurniture.MODID);
//
//    // Example: Registering a single block
//    public static final RegistryObject<Block> CHAIR_WOOD_IRONAGE_BENCH_PADDED_PURPLE_LEFT_BYG_ETHER = registerBlock(
//            "chair_wood_ironage_bench_padded_purple_left_byg_ether",
//            () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0f))
//    );
//
//    // Add more blocks here...
//
//    private static RegistryObject<Block> registerBlock(String name, Supplier<? extends Block> block) {
//        return BLOCKS.register(name, block);
//    }
//
//}
