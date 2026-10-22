# IronAgeFurniture 0.4.0.121012

- Bring Phase 4 to Minecraft 1.21.1, including single and double beds, canopies, wingback chairs and thrones.
- Keep the existing vanilla, Biomes O' Plenty, Oh The Biomes We've Gone and Immersive Engineering furniture.
- Add removable shield displays and sconces that hold up to four candles. Removed shields and candles keep their names and other saved details; sneak to remove the newest candle first.
- Add four creative tabs and server-controlled gameplay settings, with all 17 translations updated.
- Give different furniture its own recipe-book entry, while keeping alternative recipes for the same item together. Recipes unlock when their ingredients are collected.
- Add a copper-ingot fallback when copper nuggets are unavailable: five ingots make 32 empty copper sconces. The nugget recipe still makes four sconces from five nuggets.
- Keep waterlogging, redstone control, safe lava-lamp retrieval and glass-lamp breakage, and fix missing lamp particle animations.
- Update older furniture, recipes, statistics and advancements while keeping recoverable lamp data from missing mods.
- Fix Eclipse launch setup and keep its classes and resources in sync with the build.

Back up your world and try upgrades on a copy first. Some retired woods and third-party blocks have no replacement. Do not downgrade worlds from newer Minecraft versions.

# IronAgeFurniture 0.3.0.121012

- Advance IronAgeFurniture to Minecraft 1.21.1 and NeoForge 21.1.247 on Java 21.
- Move recipes and recipe advancements to Minecraft 1.21's singular data directories and retain native `neoforge:conditions`.
- Retain contemporary Biomes O' Plenty furniture, add 25 Oh The Biomes We've Gone wood families, and restore Immersive Engineering treated-wood furniture.
- Correct every BOP and BWG model to use its matching texture and update the retained Immersive Engineering models for the `wooden_decoration` texture layout.
- Update legacy vanilla texture paths for iron sconces and treated-wood bench upholstery.
- Convert older BOP cherry furniture to vanilla cherry, and supported BYG furniture to its BWG replacement.
- Expand recipe and resource checks for BOP, BWG and Immersive Engineering.
- Fix recipe advancements so furniture unlocks only when its actual ingredient is obtained, including conditional optional-mod recipes.
- Restore normal wooden-furniture fire behaviour while keeping Crimson and Warped furniture non-flammable.
- Restore throwable lava lamps, water shattering with the glass sound and obsidean-chunk drop, and safe creative-mode removal.
- Preserve waterlogging when lamps and other waterloggable furniture are placed or fall into water.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 21 toolchain, processed resources, and a duplicate-free NeoForge classpath.
