# IronAgeFurniture 0.3.0.120062

- Translate IronAgeFurniture to Minecraft 1.20.6 and NeoForge 20.6.139 on Java 21.
- Replace Forge conditional wrappers with native `neoforge:conditions` and use NeoForge registry aliases for legacy BOP cherry furniture.
- Retain the complete contemporary Biomes O' Plenty furniture family and its conditional recipes and advancements.
- Correct every Biomes O' Plenty model to use its matching wood textures and validate the mappings against the pinned integration jar.
- Update legacy vanilla texture references, including iron-sconce models, to their current paths.
- Omit Biomes We've Gone and Immersive Engineering furniture because neither integration has a compatible release for this target.
- Carry forward the deterministic checked-in furniture catalog, scoped BOP cherry remap, and positive optional-integration runtime probe.
- Fix recipe advancements so furniture unlocks only when its actual ingredient is obtained, including conditional optional-mod recipes.
- Restore normal wooden-furniture fire behaviour while keeping Crimson and Warped furniture non-flammable.
- Restore throwable lava lamps, water shattering with the glass sound and obsidean-chunk drop, and safe creative-mode removal.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 21 toolchain, processed resources, and a duplicate-free NeoForge classpath.

This is a release candidate changelog. Publication requires the protected default-branch release workflow.
