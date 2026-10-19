# IronAgeFurniture 0.4.0.2601022

- Bring Phase 4 from NeoForge 26.2 to Minecraft 26.1.2 while retaining the built-in optional Biomes O' Plenty integration.
- Add wingback chairs, thrones, wooden and canopy beds, and matching double beds across vanilla and supported BOP woods.
- Preserve removable shield components, empty frames and sneak retrieval, plus four ordered vanilla candle slots with compact metal dishes and shared lighting.
- Carry forward fixed-metal sconces, wet redstone and bucket handling, safe lava removal and water conversion, support-loss drops and Creative non-duplication.
- Add separate Chairs, Benches, Beds and Lights tabs, server-synchronized settings, recipe discovery and readable block/item HUD names across all seventeen bundled languages.
- Adapt native furniture migration to Minecraft 26.1.2's data version so current beds retain their facing on subsequent loads.

## 0.3.0.2601022

- Port IronAgeFurniture to Minecraft 26.1.2 and NeoForge 26.1.2.94 on Java 25.
- Retain Pale Oak and the complete contemporary Biomes O' Plenty furniture family, including Origin Oak.
- Validate built-in texture paths and every optional wood model semantically against the exact pinned Biomes O' Plenty jar.
- Omit Oh The Biomes We've Gone and Immersive Engineering because neither integration supports this target matrix.
- Generate NeoForge item definitions and target-native `neoforge:conditions` recipe and advancement data.
- Carry forward the deterministic checked-in furniture catalog, scoped BOP cherry alias, and positive BOP runtime probe.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 25 toolchain, processed resources, and a duplicate-free NeoForge classpath.
