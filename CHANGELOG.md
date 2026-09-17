# IronAgeFurniture 0.3.0.121012

- Advance IronAgeFurniture to Minecraft 1.21.1 and NeoForge 21.1.247 on Java 21.
- Move recipes and recipe advancements to Minecraft 1.21's singular data directories and retain native `neoforge:conditions`.
- Retain contemporary Biomes O' Plenty furniture, add 25 Oh The Biomes We've Gone wood families, and restore Immersive Engineering treated-wood furniture.
- Correct every BOP and BWG model to use its matching texture and update the retained Immersive Engineering models for the `wooden_decoration` texture layout.
- Update legacy vanilla texture paths for iron sconces and treated-wood bench upholstery.
- Remap IronAgeFurniture's legacy BOP cherry IDs to vanilla cherry, and defensible BYG furniture IDs to their BWG replacements.
- Expand the deterministic catalog and positive optional-integration runtime probe across BOP, BWG, and Immersive Engineering.
- Fix recipe advancements so furniture unlocks only when its actual ingredient is obtained, including conditional optional-mod recipes.
- Restore normal wooden-furniture fire behaviour while keeping Crimson and Warped furniture non-flammable.
- Restore throwable lava lamps, water shattering with the glass sound and obsidean-chunk drop, and safe creative-mode removal.
- Preserve waterlogging when lamps and other waterloggable furniture are placed or fall into water.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 21 toolchain, processed resources, and a duplicate-free NeoForge classpath.

This is a release candidate changelog. Publication requires the protected default-branch release workflow.
