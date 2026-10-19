# IronAgeFurniture 0.4.0.2602002

- Bring the released NeoForge 26.3 Phase 4 furniture and lighting changes to Minecraft 26.2.
- Add translated names for every registered block and item in all seventeen language files, including hidden state blocks shown by HUD mods.
- Add sixteen-colour wingbacks, thrones, wooden beds and canopy beds, including doubles, with matching construction and recolouring recipes.
- Preserve complete shield items in shield chairs, render the shield plate without its handle, and show an empty frame after sneak retrieval.
- Add four ordered vanilla candle slots to sconces, mixed candle colours, compact three-dimensional rendering and shared 12–15 lighting.
- Preserve individual sconce metal identities and historical placed or stored furniture data, including absent-provider recovery.
- Carry forward torch, water, redstone, support-loss, mining, fire, Silk Touch, Creative and lava lifecycle corrections.
- Separate the core's Chairs, Benches, Beds and Lights tabs, synchronize gameplay settings through the native 26.2 server configuration, and retain locale fallbacks.
- Move Biomes O' Plenty furniture out of the core into its own add-on project.
- Adapt beds, sleeping height, shield rendering and released-lava interpolation to the actual Minecraft 26.2 APIs, including preserving bed direction on repeated world loads.

## Earlier release: 0.3.0.2602002

- Port IronAgeFurniture to Minecraft 26.2 and NeoForge 26.2.0.45-beta on Java 25.
- Retain Pale Oak and the complete contemporary Biomes O' Plenty furniture family, including Origin Oak.
- Validate built-in texture paths and every optional wood model semantically against the exact pinned Biomes O' Plenty jar.
- Omit Oh The Biomes We've Gone and Immersive Engineering because neither integration supports this target matrix.
- Generate NeoForge item definitions and target-native `neoforge:conditions` recipe and advancement data.
- Carry forward the deterministic checked-in furniture catalog, scoped BOP cherry alias, and positive BOP runtime probe.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 25 toolchain, processed resources, and a duplicate-free NeoForge classpath.
