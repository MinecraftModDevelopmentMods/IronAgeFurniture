# Iron Age Furniture

Iron Age Furniture adds wooden chairs, stools, benches, beds and lighting with a simple, old-fashioned style.

This branch provides Phase 4, version **0.4.0.2603002**, for Minecraft **26.3**, NeoForge **26.3** (minimum `26.3.0.77`), and Java **25**. The supported NeoForge range is `[26.3.0.77,26.3.1)`; builds use `26.3.0.83`.

## Furniture and lighting

Phase 4 adds wingback chairs, thrones, single and double wooden beds, and canopy beds in all sixteen upholstery colours. Furniture is organised into four creative tabs: Chairs, Benches, Beds and Lights.

Shield chairs can carry a real shield. Sneak-right-click with an empty hand to take it off, even while the other hand holds an item; right-click with a shield to put it back. Damage, banner patterns, enchantments, names and other item components stay with that shield. The shield forms the chair back at its original size, without the handheld grip. Without a shield, both the placed chair and its inventory item show the open wooden frame.

Sconces support torches, twin torches, lamps and candles. Iron, gold and non-weathering copper frames are available without another mod. Additional Base Metals frames are conditional on a compatible provider; their saved material is retained if that provider is temporarily absent.

Each sconce metal now has its own placed-block identity, just as different coloured benches do. Existing frame items keep their IDs. Older placed sconces are converted on loading, retaining their metal, facing and contents; this also covers saves made with earlier Phase 4 test builds.

### Candle sconces

Use any of the seventeen vanilla candle types. Right-click an empty frame to insert one candle, then add up to three more in any colour combination. The first candle starts unlit; adding another keeps the existing flame state. A full holder consumes nothing.

Empty-hand right-click takes out one candle, newest first. All candles share a flame state and emit light levels 12, 13, 14 and 15 as the holder fills. Flint and steel, a torch or redstone can light them. Water extinguishes them, while redstone can keep a waterlogged holder lit until the signal disappears.

Breaking the holder or its support returns the matching metal frame and the original candles. Stored item components are retained. In Creative, the contents are recovered without duplicating the frame.

Sconce candles sit in a matching metal dish and are smaller than freestanding candles. Their colour and stored item data stay unchanged.

Standalone candles use vanilla placement, recipes and light levels. Tallow and string provide an alternative recipe for eight uncoloured candles. Candles held in your hand do not produce flame or smoke; IAF does not add dynamic lighting.

Lava lamps deliberately break and may start fires when shattered. Remove a lamp by interacting with its sconce, or use Silk Touch to recover it intact. Ordinary mining of a wall-mounted lava sconce releases the lamp to fall. Metal sconce frames resist fire and lava, so a shattered lamp cannot burn away its dropped frame.

## Crafting upgrades

- Vanilla bed + matching plank → wooden bed in the same colour.
- Wooden bed + matching plank + matching carpet → canopy bed.
- Two identical single beds → the matching double bed.
- Classic chair + matching plank + carpet → wingback chair.
- Wingback + matching plank + matching carpet → throne.
- Five matching nuggets → four sconces.

Chair upgrades are shapeless. Beds can still be deliberately recoloured with carpet, but those recolouring recipes do not clutter the recipe book.

## Optional integrations and older worlds

The core contains twelve vanilla wood families. Biomes O' Plenty furniture belongs to the separate matching BOP add-on, not this jar. Mineralogy rock-salt lamps can be used in sconces when a compatible Mineralogy release is installed. Base Metals and legacy Crayfish Furniture migration remain optional; Refurbished Furniture is not treated as the old CFM mod.

Back up worlds before changing Minecraft or mod versions. Old upholstery defaults to red when no colour was saved; old frames default to iron when no metal was saved. Old floor candles become vanilla candles. Old wall candles become iron candle sconces at the same position, deliberately receiving a frame during conversion.

See [upgrading worlds](docs/UPGRADING.md) for the migration scope and current verification status. Never test an upgrade on your only copy of a world.

Gameplay choices are synchronised from the server. Disabling a furniture family hides its new construction options without removing its persistent registrations or deleting existing furniture.

## Building and contributing

Use the checked-in Gradle wrapper and Temurin Java `25.0.3+9`. This target uses NeoGradle directly; it does not require the legacy Forge Mavenizer.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Build settings are split under `gradle/`: toolchains and runs, artifact packaging, verification, release validation, Eclipse and the resource catalogs each have their own file. Normal builds verify the committed catalog output. To intentionally refresh it, run `gradlew.bat updateFurnitureCatalog`; this also refreshes translations and English fallbacks.

### Manual clients in Eclipse's Gradle Tasks view

Refresh the Gradle Tasks view, then expand `neogradle/runs` in the core project:

- `runClientBase` starts the core alone, using the existing `run` folder. Its `mods` folder must contain no jars.
- `runClientWithBop` starts the core with the tested BOP add-on, BOP `26.3.0.0.15`, GlitchCore `26.3.0.0.5` and TerraBlender `26.3.0.0.9`. It uses `run-bop`, with separate worlds and settings.

Both launchers use this project's current core source. The BOP launcher uses the packaged add-on candidate, not the add-on project's source. Neither launcher includes development probes. The original `runClient` remains available.

For the BOP launcher, set `bopRuntimeDirectory` in an ignored `local.properties` file to the manual-testing bundle's `mods` folder, or pass `-PbopRuntimeDirectory=<folder>`. The launcher checks all four dependency hashes before copying them into `run-bop/mods`; it does not copy the packaged core or optional Mineralogy/OreSpawn jars. Another checkout can instead put those four tested jars in `libs/bop-runtime`. Missing, changed or unexpected runtime jars stop the launch rather than silently changing the test setup. Existing worlds and extra jars are never deleted.

Run `gradlew.bat verifyManualClientProfiles` to check both setups without opening Minecraft. When switching profiles, close the previous client first; worlds are not shared automatically.

The Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.4.0.2603002`. The Java API lives under `zone.moddev.mc.ironagefurniture`; persistent mod, registry, resource and save identities remain under `ironagefurniture`. Building locally does not publish or tag a release.
