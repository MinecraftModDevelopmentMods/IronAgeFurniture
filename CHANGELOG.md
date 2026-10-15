# Iron Age Furniture 0.4.0.2603002

- Adds wingback chairs, thrones, wooden and canopy beds, and matching double beds across the twelve vanilla wood families.
- Preserves all sixteen upholstery colours through crafting, placement, mining and reloads, with matching upgrade discovery and four creative tabs.
- Lets shield chairs retain a removable shield with its full item components.
- Adds mixed-colour vanilla candle sconces, twin torches, tallow, optional rock-salt lamps, and iron, gold and non-weathering copper frames.
- Retains optional Base Metals identities without making the provider mandatory.
- Carries forward the mining, support-loss, waterlogging, redstone, Silk Touch and lava-lamp corrections from the earlier Phase 4 ports.
- Imports legacy furniture, colours, metals, shields and candles before obsolete save data is discarded. Old wall candles receive an iron frame during conversion.
- Keeps recovered candle and shield items safe when the Creative inventory is full, returning them to a slot or dropping them rather than discarding them.
- Fixes startup on newer NeoForge/FML versions and separates server-synchronised gameplay settings from client effects.
- Updates recipes, language fallbacks and the build layout. Biomes O' Plenty furniture remains in its separate add-on.

This is a local candidate; publication is a separate step. The upgrade guide records verification results and remaining limitations.

## 0.3.0.2603002

- Port IronAgeFurniture to Minecraft 26.3 and NeoForge 26.3.0.1-beta on Java 25.
- Retain the complete vanilla wood catalog, including Pale Oak, plus the existing furniture, lighting, flammability, lava-lamp, and save-compatibility behavior.
- Omit Biomes O' Plenty, Oh The Biomes We've Gone, and Immersive Engineering furniture because none currently provides a compatible NeoForge 26.3 release.
- Generate Minecraft 26.3 item definitions and target-native recipe and advancement data from the deterministic checked-in furniture catalog.
- Retain the scoped BOP-cherry registry aliases so existing IronAgeFurniture IDs continue to map to vanilla Cherry furniture.
- Provide reproducible Eclipse/Buildship setup with the exact Temurin Java 25 toolchain, processed resources, and a duplicate-free NeoForge classpath.
