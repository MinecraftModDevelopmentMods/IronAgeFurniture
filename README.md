# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This branch is for Minecraft 1.21.11 with NeoForge 21.11.45 and Java 21. This release is `0.4.0.121112`. The Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.4.0.121112`.

## Furniture and lighting

Wooden beds, double beds, canopy beds, wingback chairs and thrones come in all sixteen upholstery colours. Recipes use the matching wood and carpet. Shield chairs keep the shield's name, patterns, damage and other components; sneak-click to retrieve it and leave an empty frame.

Sconces retain their metal when filled, emptied, broken or reloaded. Candle sconces hold four individual vanilla candles, including mixed colours and custom components, and return the newest candle first. Their light rises from 12 to 15 as candles are added. Carried candles do not produce flames or dynamic light.

Chairs, Benches, Beds and Lights have separate creative tabs. The server controls which furniture can be crafted and shown without removing existing furniture from saves. All seventeen language files include block and item names for HUDs, including hidden lighting states; new names use English fallbacks where a translation is not available.

## Optional integrations

Biomes O' Plenty and Oh The Biomes We've Gone furniture is built into the core on this branch and enabled only when the corresponding mod is present. Neither mod is a hard dependency and no separate furniture add-on is required. BOP supplies fourteen wood families, including Origin Oak, and BWG supplies twenty-five, including Spirit wood.

The pinned optional stack is BOP 21.11.0.31, BWG 4.4.0, TerraBlender 21.11.0.0, GlitchCore 21.11.0.4, Corgilib 9.0.0.0, Oh The Trees You'll Grow 9.1.0 and GeckoLib 5.4.5. Exact filenames, checksums and official download links are in `gradle/verification/optional-integration-mods.json`.

Mineralogy rock-salt lamps can be used when a compatible Mineralogy release and its OreSpawn dependency are installed. This release doesn't support installed versions of Base Metals or the original MrCrayfish's Furniture Mod on NeoForge 1.21.11. Refurbished Furniture is a different mod and isn't included as a replacement. Immersive Engineering furniture isn't available on this branch.

Recipes and recipe advancements for optional furniture are loaded conditionally with their integration. Existing IronAgeFurniture BOP Cherry furniture remaps to vanilla Cherry, while supported legacy BYG furniture names remap to their BWG replacements when BWG is installed.

Back up a world before upgrading and test a copy first. These mappings concern IronAgeFurniture's furniture, not every block added by a biome or furniture provider. Missing-provider item payloads are retained for later recovery where possible. A world saved by Minecraft 26.x cannot be downgraded into 1.21.11.

## Build

Use the checked-in Gradle wrapper with Temurin Java 21.0.7+6. This is a NeoGradle project; no Forge Mavenizer step is needed.

```text
gradlew.bat clean
gradlew.bat check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Manual production launches are `runClientBase`, `runClientWithBop`, `runClientWithBwg` and `runClientWithIntegrations`. Their separate worlds and logs default to `../manual-runs/1.21.11-phase4`, outside the checkout. Supply the pinned jars with `-PbopRuntimeDirectory=/path/to/optional-jars`; the launch tasks reject unexpected or changed jars rather than replacing them. `verifyManualClientProfiles verifyOptionalIntegrationTextures` checks the launch profiles and provider textures without starting Minecraft.

Release publication is initiated manually from the protected default-branch dispatcher after the exact target commit has passed hosted CI. Building locally does not tag or publish a release.

Furniture registrations and their matching blockstates, models, recipes, advancements, and translations are generated from `gradle/furniture-catalog.json`. Normal builds verify committed output without changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally changing the catalog.

The Java API namespace is `zone.moddev.mc.ironagefurniture`. The persistent runtime mod, registry, resource, data, configuration, and saved-world namespace remains `ironagefurniture`.
