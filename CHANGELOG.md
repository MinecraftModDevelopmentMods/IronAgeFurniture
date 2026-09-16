# IronAgeFurniture 0.3.0.2603002

- Port IronAgeFurniture to Minecraft 26.3 and NeoForge 26.3.0.1-beta on Java 25.
- Retain the complete vanilla wood catalog, including Pale Oak, plus the existing furniture, lighting, flammability, lava-lamp, and save-compatibility behavior.
- Omit Biomes O' Plenty, Oh The Biomes We've Gone, and Immersive Engineering furniture because none currently provides a compatible NeoForge 26.3 release.
- Generate Minecraft 26.3 item definitions and target-native recipe and advancement data from the deterministic checked-in furniture catalog.
- Retain the scoped BOP-cherry registry aliases so existing IronAgeFurniture IDs continue to map to vanilla Cherry furniture.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 25 toolchain, processed resources, and a duplicate-free NeoForge classpath.
