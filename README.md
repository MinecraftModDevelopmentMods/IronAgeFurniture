# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This branch provides Phase 4, version `0.4.0.2602002`, for Minecraft 26.2 with NeoForge 26.2.0.45-beta and Java 25.

## Furniture and lighting

Every registered block and item has a readable name in all seventeen supported language files, including filled sconces, lamp brightness states and hidden parts shown by HUD mods.

Phase 4 adds wingback chairs, thrones, wooden beds and canopy beds, including doubles, in all sixteen upholstery colours. Shield chairs store the complete shield item, including its name, damage, enchantments and banner design. Sneak-click with an empty hand to retrieve the shield and leave a bare wooden frame.

Furniture is grouped into Chairs, Benches, Beds and Lights tabs. Chair upgrades use the matching chair, plank and carpet. A vanilla bed and matching plank make a wooden bed; add a matching plank and carpet for a canopy bed. Two matching single beds make a double bed. Construction recipes appear in the recipe book; carpet recolouring remains available without filling the book with colour-change recipes.

Sconces accept up to four vanilla candles, including mixed colours. Each candle keeps its item components, and empty-hand removal returns the newest candle first. The candles share their lighting state and emit light levels 12, 13, 14 and 15. They render as small three-dimensional candles on metal dishes. Carried items do not emit flames or dynamic light.

The lighting range also includes single and twin torches, redstone torches and glass lamps. Water, redstone, mining, support loss and lava-water reactions retain their separate behaviours. Iron, gold and copper frames do not require another mod.

## Add-ons and settings

Biomes O' Plenty furniture belongs in the separate matching Biomes O' Plenty add-on, not in the core mod. Install the core, add-on, biome mod and its required dependencies for the same Minecraft version. Do not use a Forge or Minecraft 26.3 jar in this NeoForge 26.2 installation.

Gameplay settings use NeoForge 26.2's server configuration and are synchronized to clients. Set instance defaults in `config/ironagefurniture-server.toml`. To override them for one world, put that file in the world's `serverconfig` folder. Restart the server after editing. Disabling a feature hides its crafting and creative entries without removing saved block identities.

Base Metals and MrCrayfish's original Furniture Mod have no published NeoForge 26.2 build in the checked official catalogs. Their historical migration paths do not imply that those providers can be installed on this target. Mineralogy compatibility must use the matching NeoForge 26.2 Mineralogy and OreSpawn releases.

## Backups and upgrades

Back up a world before upgrading. The compatibility code recognises historical furniture identities, upholstery, sconce metals and stored shield or candle items. Missing providers must not silently turn their saved furniture into another material.

The tested 1.10/1.12 Phase 2–4 and 1.14–1.17 furniture fixtures passed direct first and second loads in 26.2, with their source backups unchanged. This checks furniture preservation and repeat-load behaviour, not conversion of an entire old modpack. Missing biome terrain, blocks or items from unrelated mods are outside that scope.

Other old saves may need intermediate Minecraft versions before 26.2 can open them. Upgrade only disposable copies until the result has been checked. This is a downward code port: it does not make Minecraft 26.3 worlds safe to open in 26.2.

## Build

Use the checked-in Gradle wrapper with the exact Temurin Java 25.0.3+9 toolchain. This NeoForge target uses NeoGradle directly and does not need the Forge Mavenizer preparation fixture.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Use `runClientBase` for the core alone and `runClientWithBop` for the matching BOP add-on and dependencies. They use separate `run-core-only-26.2` and `run-bop-26.2-phase4` folders, without changing existing saves. For the BOP profile, set `bopRuntimeDirectory` in ignored `local.properties`, or pass `-PbopRuntimeDirectory=<candidate bundle folder>`. The profile checks the exact jar hashes and refuses unexpected or different existing jars. `verifyManualClientProfiles` checks both profiles without starting Minecraft.

Release publication is initiated manually from the protected default-branch dispatcher after the exact target commit has passed hosted CI. Building locally does not tag or publish a release.

Furniture registrations and their matching blockstates, models, recipes, advancements, and translations are generated from `gradle/furniture-catalog.json`. Normal builds verify committed output without changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally changing the catalog.

The core has no biome-mod dependency. Its Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.4.0.2602002`. A successful build does not by itself confirm gameplay, world migration or publication.

The Java API namespace is `zone.moddev.mc.ironagefurniture`. The persistent runtime mod, registry, resource, data, configuration, and saved-world namespace remains `ironagefurniture`.
