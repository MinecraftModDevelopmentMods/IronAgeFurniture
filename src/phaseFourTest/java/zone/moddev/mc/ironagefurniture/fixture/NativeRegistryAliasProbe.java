package zone.moddev.mc.ironagefurniture.fixture;

import java.util.function.BiConsumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

/** Missing-mapping callbacks alone do not qualify ordinary old-ID lookups. */
final class NativeRegistryAliasProbe {
    static void run(net.minecraft.server.MinecraftServer server, BiConsumer<Boolean, String> check) {
        same("chair_wood_ironage_classic_big_oak", "chair_wood_ironage_classic_dark_oak", check);
        same("chair_wood_ironage_bench_padded_single_oak", "chair_wood_ironage_bench_padded_red_single_oak", check);
        check.accept(BuiltInRegistries.ITEM.getValue(id("light_metal_ironage_candle_floor")) == Items.CANDLE,
                "Standalone candle item aliases work without a missing-mapping event");
        if (net.minecraftforge.fml.ModList.isLoaded("iafbopaddon")) {
            same("chair_wood_ironage_bench_padded_single_biomesoplenty_willow",
                    "chair_wood_ironage_bench_padded_red_single_biomesoplenty_willow", check);
            same("chair_wood_ironage_bench_padded_single_biomesoplenty_cherry",
                    "chair_wood_ironage_bench_padded_red_single_cherry", check);
        }
        statistics(server, check);
        savedSnapshotAliases(check);
        System.out.println("IAF_FORGE_ALIASES_PASS");
    }
    private static void savedSnapshotAliases(BiConsumer<Boolean, String> check) {
        var root = new net.minecraft.nbt.CompoundTag();
        var fml = new net.minecraft.nbt.CompoundTag(); root.put("fml", fml);
        var registries = new net.minecraft.nbt.CompoundTag(); fml.put("Registries", registries);
        for (String registry : new String[]{"minecraft:block", "minecraft:item"}) {
            var snapshot = new net.minecraft.nbt.CompoundTag(); registries.put(registry, snapshot);
            var aliases = new net.minecraft.nbt.ListTag(); snapshot.put("aliases", aliases);
            for (String key : new String[]{"ironagefurniture:chair_wood_ironage_classic_oak",
                    "ironagefurniture:chair_wood_ironage_classic_big_oak", "minecraft:stone"}) {
                var alias = new net.minecraft.nbt.CompoundTag(); alias.putString("K", key);
                alias.putString("V", "ironagefurniture:chair_wood_ironage_classic_dark_oak"); aliases.add(alias);
            }
            snapshot.putString("Unrelated", "retain");
        }
        check.accept(zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook.sanitizeForgeSnapshotAliases(root) == 2,
                "Saved aliases cannot hide real furniture installed after a core-only world was saved");
        for (String registry : new String[]{"minecraft:block", "minecraft:item"}) {
            var snapshot = registries.getCompoundOrEmpty(registry);
            var aliases = snapshot.getListOrEmpty("aliases");
            check.accept(aliases.size() == 2 && aliases.getCompoundOrEmpty(0).getStringOr("K", "").endsWith("_big_oak")
                    && aliases.getCompoundOrEmpty(1).getStringOr("K", "").equals("minecraft:stone"),
                    "Genuine virtual aliases and other mods' mappings remain untouched");
            check.accept(snapshot.getStringOr("Unrelated", "").equals("retain"), "Unrelated saved registry data is retained");
        }
        check.accept(zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook.sanitizeForgeSnapshotAliases(root) == 0,
                "Saved alias repair is idempotent");
    }
    private static void same(String old, String target, BiConsumer<Boolean, String> check) {
        var expected = BuiltInRegistries.ITEM.getValue(id(target));
        check.accept(expected != Items.AIR && BuiltInRegistries.ITEM.getValue(id(old)) == expected,
                "Live registry alias retains old item/statistic lookup: " + old);
    }
    private static Identifier id(String path) { return Identifier.fromNamespaceAndPath("ironagefurniture", path); }
    private static void statistics(net.minecraft.server.MinecraftServer server, BiConsumer<Boolean, String> check) {
        var source = com.google.gson.JsonParser.parseString("""
                {"stats":{"minecraft:mined":{"ironagefurniture:chair_wood_ironage_shield_big_oak":3,
                "ironagefurniture:chair_wood_ironage_shield_dark_oak":4},
                "minecraft:crafted":{"ironagefurniture:chair_wood_ironage_bench_padded_single_oak":9,
                "ironagefurniture:chair_wood_ironage_bench_padded_red_single_oak":5,"minecraft:stick":27}},
                "Unrelated":{"keep":123}}
                """);
        var original = source.deepCopy();
        var dynamic = net.minecraft.util.datafix.DataFixTypes.STATS.updateToCurrentVersion(
                net.minecraft.util.datafix.DataFixers.getDataFixer(),
                new com.mojang.serialization.Dynamic<>(com.mojang.serialization.JsonOps.INSTANCE, source),
                net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version());
        var converted = dynamic.getValue().getAsJsonObject();
        var stats = converted.getAsJsonObject("stats");
        check.accept(source.equals(original), "Statistics conversion does not mutate its input");
        check.accept(stats.getAsJsonObject("minecraft:mined").get("ironagefurniture:chair_wood_ironage_shield_dark_oak").getAsInt() == 7,
                "Mined statistics combine old and canonical chair IDs without losing counts");
        check.accept(stats.getAsJsonObject("minecraft:crafted").get("ironagefurniture:chair_wood_ironage_bench_padded_red_single_oak").getAsInt() == 14,
                "Crafted statistics combine old and canonical upholstered items");
        check.accept(stats.getAsJsonObject("minecraft:crafted").get("minecraft:stick").getAsInt() == 27
                && converted.getAsJsonObject("Unrelated").equals(original.getAsJsonObject().get("Unrelated")), "Unrelated statistics and data are retained");
        check.accept(converted.equals(zone.moddev.mc.ironagefurniture.migration.LegacyStatisticsMigration.convert(converted)),
                "Statistics conversion is idempotent");
        var path = java.nio.file.Path.of("unused-native-statistics-probe.json");
        check.accept(!java.nio.file.Files.exists(path), "Statistics probe does not replace an existing file");
        var counter = new net.minecraft.stats.ServerStatsCounter(server, path);
        var current = source.deepCopy().getAsJsonObject();
        current.addProperty("DataVersion", net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version());
        counter.parse(net.minecraft.util.datafix.DataFixers.getDataFixer(), current);
        check.accept(counter.getValue(net.minecraft.stats.Stats.ITEM_CRAFTED.get(BuiltInRegistries.ITEM.getValue(
                id("chair_wood_ironage_bench_padded_red_single_oak")))) == 14,
                "The actual native statistics codec retains the merged crafted count");
        check.accept(counter.getValue(net.minecraft.stats.Stats.BLOCK_MINED.get(BuiltInRegistries.BLOCK.getValue(
                id("chair_wood_ironage_shield_dark_oak")))) == 7,
                "The actual native statistics codec retains the merged mined count");
    }
    private NativeRegistryAliasProbe() { }
}
