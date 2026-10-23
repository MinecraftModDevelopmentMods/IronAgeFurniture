# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This branch is for Minecraft 1.20.6 with NeoForge 20.6.139. The Phase 4 version is `0.4.0.120062`.

Phase 4 adds single and double beds, canopies, wingback chairs, thrones, removable shield displays and sconces that hold up to four candles. Shields and candles keep their names and other saved details when removed. Furniture is organised into four creative tabs, with gameplay settings controlled by the server. All 17 translations are included.

Different chairs and stools have their own recipe-book entries. Five copper nuggets make four empty copper sconces. If no copper nuggets are available, five copper ingots make 32 instead.

## Optional integrations

Biomes O' Plenty furniture is enabled only when Biomes O' Plenty is present; it is not a hard dependency. Its recipes and recipe advancements are loaded conditionally with the integration, and existing IronAgeFurniture BOP Cherry furniture remaps to vanilla Cherry.

Oh The Biomes We've Gone and Immersive Engineering furniture are intentionally unavailable because neither integration has a compatible release for this target.

Back up your world and try upgrades on a copy first. Worlds from newer Minecraft versions cannot be downgraded. Some retired woods and third-party blocks have no replacement; this does not make every old modpack compatible.

## Build

Use the included Gradle wrapper with Temurin Java 21.0.7+6.

```text
gradlew.bat clean
gradlew.bat check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Run the clean and verification commands separately because NeoGradle resolves model inputs under `build/tmp` while configuring the verification build.

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Releases use the manual publication workflow after CI passes. A local build does not publish anything. The Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.4.0.120062`.

Furniture registrations and their matching blockstates, models, recipes, advancements, and translations are generated from `gradle/furniture-catalog.json`. Normal builds verify committed output without changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally changing the catalog.

The Java package is `zone.moddev.mc.ironagefurniture`. The mod ID and saved-data namespace remain `ironagefurniture`.
