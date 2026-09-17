# IronAgeFurniture 0.3.0.121112

- Advance IronAgeFurniture to Minecraft 1.21.11 and NeoForge 21.11.45 on Java 21.
- Move recipes and recipe advancements to Minecraft 1.21's singular data directories and retain native `neoforge:conditions`.
- Retain contemporary Biomes O' Plenty furniture and add 25 Oh The Biomes We've Gone wood families, including spirit wood.
- Validate built-in texture paths and every optional wood model semantically against the exact pinned BOP and BWG jars.
- Remap IronAgeFurniture's legacy BOP cherry IDs to vanilla cherry, and defensible BYG furniture IDs to their BWG replacements.
- Expand the deterministic catalog and positive optional-integration runtime probe across BOP and BWG.
- Fix recipe advancements so furniture unlocks only when its actual ingredient is obtained, including conditional optional-mod recipes.
- Restore normal wooden-furniture fire behaviour while keeping Crimson and Warped furniture non-flammable.
- Restore throwable lava lamps, water shattering with the glass sound and obsidean-chunk drop, and safe creative-mode removal.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 21 toolchain, processed resources, and a duplicate-free NeoForge classpath.
