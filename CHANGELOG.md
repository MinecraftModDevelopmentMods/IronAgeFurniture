# IronAgeFurniture 0.4.0.121112

- Added wooden beds, double beds, canopy beds, wingbacks and thrones to NeoForge 1.21.11, with sixteen upholstery colours and matching wood recipes.
- Added the new furniture for all fourteen BOP woods and twenty-five BWG woods without changing existing furniture IDs.
- Shield chairs now keep the shield's name, patterns, damage and other components when crafted, placed, retrieved or broken. Empty frames have their own item appearance and snug shape.
- Candle sconces now hold up to four vanilla candles, with mixed colours and individual components. Candles are removed newest first, and the light level rises from 12 to 15. Held candles don't produce flames.
- Sconces keep their metal when filled, emptied, broken or reloaded. Older furniture data is preserved, including recoverable item data when an optional mod is missing.
- Kept water-bucket and redstone behaviour for wet candles and torches, support-loss drops, safe lava removal, falling-lamp shattering and lava-water conversion.
- Fixed the red lamp turning grey when powered. Its glass now stays transparent at every signal strength.
- Added separate Chairs, Benches, Beds and Lights tabs, server-controlled furniture availability and immediate recipe discovery.
- Added HUD names for every block and item, including hidden lighting variants, in all seventeen language files. New names use English where a translation isn't available.
- Updated rendering and save handling for 1.21.11, kept Minecraft's shared asset cache intact, and added separate core-only, BOP, BWG and combined development launches.

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
