# IronAgeFurniture 0.4.0.120062

- Bring Phase 4 to Minecraft 1.20.6: single and double beds, canopies, wingback chairs and thrones in all supported vanilla and BOP woods and colours.
- Add removable shield displays and sconces that hold up to four candles. Removed shields and candles keep their names and other saved details.
- Add four creative tabs, server-controlled gameplay settings and updated names in all 17 translations.
- Give different furniture its own recipe-book entry and unlock recipes when their ingredients are collected.
- Add a copper-ingot fallback when copper nuggets are unavailable: five ingots make 32 empty copper sconces. The nugget recipe still makes four from five nuggets.
- Keep the waterlogging, redstone, lava-lamp retrieval and glass-lamp fixes, including particle animations.
- Carry forward support for older furniture, recipes, statistics and advancements. Missing third-party woods are not replaced with unrelated ones.
- Update Eclipse launch setup and keep its classes and resources in sync with the build.

Back up your world and try upgrades on a copy first. Do not downgrade worlds from newer Minecraft versions. BWG and Immersive Engineering remain unavailable on this target.

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
