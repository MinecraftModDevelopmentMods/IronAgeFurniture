# IronAgeFurniture 0.3.0.2603001

- Port IronAgeFurniture to Minecraft 26.3 and Forge 66.0.2 on Java 25.
- Retain the complete vanilla wood catalog, including Pale Oak, plus the existing furniture, lighting, flammability, lava-lamp, and save-compatibility behavior.
- Preserve lava-filled floor and wall sconces when they become waterlogged by converting them to the matching waterlogged empty sconce and dropping exactly one obsidian chunk without replacing the supporting block.
- Omit Biomes O' Plenty, Oh The Biomes We've Gone, and Immersive Engineering furniture because none currently provides a compatible Forge 26.3 release.
- Generate Minecraft 26.3 item definitions and target-native recipe and advancement data from the deterministic checked-in furniture catalog.
- Retain the scoped BOP-cherry registry mappings so retired IronAgeFurniture IDs continue to map to vanilla Cherry furniture.
- Update redstone connectivity overrides and recipe-unlock criteria for the Minecraft 26.3 APIs and data schema.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 25 toolchain, processed resources, and a production-only Forge classpath.
