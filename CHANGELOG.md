# Iron Age Furniture 0.4.1.2603002

- Adds translated names for every registered block and item in all seventeen language files, including filled sconces, lamp brightness states and hidden furniture parts shown by HUD mods.
- Keeps existing furniture, recipes, registry identities and world-upgrade behaviour unchanged.

## Iron Age Furniture 0.4.0.2603002

- Removes flame and smoke from held candles. Placed lighting keeps its existing behaviour.
- Restores the matching-metal dish beneath sconce candles and makes the candles smaller, with particles aligned to their wicks.
- Restores the angled obsidian-chunk inventory model and the bare-frame item model for shield chairs with no shield attached.
- Makes an empty shield chair's outline and collision match its open wooden frame, and restores sneak-right-click shield removal even when the other hand holds an item.
- Adds wingback chairs, thrones, wooden and canopy beds, and matching double beds across the twelve vanilla wood families.
- Preserves all sixteen upholstery colours through crafting, placement, mining and reloads, with matching upgrade discovery and four creative tabs.
- Lets shield chairs retain a removable shield with its full item components.
- Adds mixed-colour vanilla candle sconces, twin torches, tallow, optional rock-salt lamps, and iron, gold and non-weathering copper frames.
- Retains optional Base Metals identities without making the provider mandatory.
- Gives every sconce metal its own placed-block identity, including Base Metals frames. Existing item IDs stay unchanged, and older placed sconces keep their material, orientation and contents when upgraded.
- Prevents old sconce data from being lost during world upgrades, including updates made without changing Minecraft versions.
- Carries forward the mining, support-loss, waterlogging, redstone, Silk Touch and lava-lamp corrections from the earlier Phase 4 ports.
- Imports legacy furniture, colours, metals, shields and candles before obsolete save data is discarded. Old wall candles receive an iron frame during conversion.
- Recognises the original 1.10 metal storage ID as well as its later names, so gold and Base Metals sconces retain their material when an older world is upgraded.
- Keeps recovered candle and shield items safe when the Creative inventory is full, returning them to a slot or dropping them rather than discarding them.
- Fixes startup on newer NeoForge/FML versions and keeps gameplay settings synchronised on servers.
- Updates recipes, language fallbacks and the build layout. Biomes O' Plenty furniture remains in its separate add-on.
- Adds separate core-only and BOP/add-on client tasks to Eclipse's Gradle Tasks view, with isolated worlds and checked dependency jars.
- Restores the shield-chair back's intended size and orientation, without the handheld shield grip. Colours, patterns and enchantment glint also render correctly while the chair falls.
- Keeps dropped metal sconce frames safe from a shattered lava lamp's fire and fixes a client crash when a released lamp starts falling.
- Preserves the metal when an empty wall sconce is broken, instead of returning an iron frame.

Back up existing worlds before upgrading. The [upgrade guide](docs/UPGRADING.md) explains what is preserved and which older third-party content is not supported.

## 0.3.0.2603002

- Port IronAgeFurniture to Minecraft 26.3 and NeoForge 26.3.0.1-beta on Java 25.
- Retain the complete vanilla wood catalog, including Pale Oak, plus the existing furniture, lighting, flammability, lava-lamp, and save-compatibility behavior.
- Omit Biomes O' Plenty, Oh The Biomes We've Gone, and Immersive Engineering furniture because none currently provides a compatible NeoForge 26.3 release.
- Generate Minecraft 26.3 item definitions and target-native recipe and advancement data from the deterministic checked-in furniture catalog.
- Retain the scoped BOP-cherry registry aliases so existing IronAgeFurniture IDs continue to map to vanilla Cherry furniture.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 25 toolchain, processed resources, and a duplicate-free NeoForge classpath.
