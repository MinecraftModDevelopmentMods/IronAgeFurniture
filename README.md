# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This version brings Phase 4 to Minecraft 26.1.2 with Forge 64.0.9 and Java 25. The version is `0.4.1.2601021`.

## Furniture and lighting

Phase 4 adds wingback chairs, thrones, single and double wooden beds, and canopy beds in all sixteen upholstery colours. Furniture is organised into four creative tabs: Chairs, Benches, Beds and Lights. Gameplay settings are controlled by the server without deleting existing furniture.

Shield chairs hold a real shield, including its damage, banner patterns, enchantments, name and other saved details. Sneak-right-click with an empty hand to remove it. Empty chairs show the bare wooden frame, both placed and in your inventory.

Sconces hold torches, twin torches, lamps or up to four vanilla candles in any colour combination. Empty-hand clicks remove candles newest first, keeping their saved item details. They share a flame state and give light levels 12, 13, 14 and 15 as the holder fills. Iron, gold and copper frames work without another mod; other materials require their matching provider.

Water, redstone and buckets work with the lighting as before. Lava lamps can shatter and start fires; retrieve one from its sconce, or use Silk Touch to recover it intact. Metal frames resist fire and lava. Held candles do not emit flame or smoke, and IAF does not add dynamic lighting.

Matching bed and chair upgrades retain their wood and upholstery colour. Different furniture has its own recipe-book entry, and upgrades become available as their ingredients are collected. All seventeen language files include the new names and hidden states used by HUD mods, with English fallbacks where a translation is not yet available.

## Optional integrations

Biomes O' Plenty furniture is enabled only when Biomes O' Plenty is present. It is not required to run IronAgeFurniture. All fourteen supported BOP wood families are included, including Origin Oak. Oh The Biomes We've Gone and Immersive Engineering furniture are not supported in this version.

Recipes and recipe advancements for BOP furniture are loaded conditionally with the integration. Existing IronAgeFurniture BOP Cherry furniture remaps to vanilla Cherry.

Back up worlds before changing Minecraft or mod versions and test upgrades on a copy. The port carries forward older furniture, shield, candle, recipe and advancement recovery, but missing third-party blocks do not automatically gain a replacement. Never downgrade a world from a newer Minecraft version.

## Build

Use the checked-in Gradle wrapper and Temurin Java 25.0.3+9. The bundled Mavenizer build tool prepares the Forge development files and is checked against its recorded checksums.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Client launches reuse the installed Minecraft assets when available. For an installation elsewhere, add `-PminecraftAssetsDir=<assets-directory>` when preparing Eclipse.

Release publication is initiated manually from the protected default-branch dispatcher after the exact target commit has passed hosted CI. Building locally does not tag or publish a release.

Furniture registrations and their matching blockstates, models, recipes, advancements, and translations are generated from `gradle/furniture-catalog.json`. Normal builds verify committed output without changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally changing the catalog.

The Java API namespace is `zone.moddev.mc.ironagefurniture`. The persistent Forge mod, registry, resource, data, configuration, and saved-world namespace remains `ironagefurniture`.

The Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.4.1.2601021`. Local builds do not publish or tag releases.
