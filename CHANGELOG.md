# Iron Age Furniture 0.4.0.2603001

- Brings the complete Phase 4 furniture and lighting range to Forge 26.3, including the final NeoForge shield, candle and sconce corrections.
- Adds wingbacks, thrones, wooden and canopy beds, and doubles across all twelve vanilla wood families and sixteen upholstery colours.
- Preserves removable shield items, including their damage, names, patterns, enchantments and other components. Empty shield chairs show the bare wooden frame.
- Adds four-slot mixed-colour vanilla candle sconces with matching-metal support dishes. Insertion, removal, mining and support loss preserve the original candle items.
- Gives every sconce metal its own placed-block identity, while keeping existing item IDs and recovering older saved metal data.
- Carries forward redstone, waterlogging, bucket, hand-mining, support-loss, Silk Touch, lava-lamp shattering and fire-resistant frame drops.
- Keeps held candles free of flame and smoke effects. IAF does not add dynamic lighting.
- Preserves the matching bed and chair upgrade recipes, immediate recipe discovery and separate Chairs, Benches, Beds and Lights tabs.
- Adds server-synchronised gameplay settings, complete language fallbacks, and build scripts organised by responsibility.
- Keeps Biomes O' Plenty furniture in the separate matching add-on.

Back up worlds before upgrading; the [upgrade guide](docs/UPGRADING.md) explains the supported scope.

# IronAgeFurniture 0.3.0.2603001

- Port IronAgeFurniture to Minecraft 26.3 and Forge 66.0.2 on Java 25.
- Retain the complete vanilla wood catalog, including Pale Oak, plus the existing furniture, lighting, flammability, lava-lamp, and save-compatibility behavior.
- Preserve lava-filled floor and wall sconces when they become waterlogged by converting them to the matching waterlogged empty sconce and dropping exactly one obsidian chunk without replacing the supporting block.
- Omit Biomes O' Plenty, Oh The Biomes We've Gone, and Immersive Engineering furniture because none currently provides a compatible Forge 26.3 release.
- Generate Minecraft 26.3 item definitions and target-native recipe and advancement data from the deterministic checked-in furniture catalog.
- Retain the scoped BOP-cherry registry mappings so retired IronAgeFurniture IDs continue to map to vanilla Cherry furniture.
- Update redstone connectivity overrides and recipe-unlock criteria for the Minecraft 26.3 APIs and data schema.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 25 toolchain, processed resources, and a production-only Forge classpath.
