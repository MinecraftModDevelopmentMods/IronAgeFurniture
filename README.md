# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This branch is for Minecraft 1.21.1 with NeoForge 21.1.247. The version is `0.4.0.121012`.

Phase 4 adds single and double beds, canopies, wingback chairs, thrones, removable shield displays and sconces that hold up to four candles. Shields and candles keep their names and other saved details when removed. Furniture is organised into four creative tabs, with gameplay settings controlled by the server. All 17 translations are included.

Different chairs and stools have their own recipe-book entries. If no copper nuggets are available, five copper ingots can be used to make 32 empty copper sconces instead.

## Optional integrations

Biomes O' Plenty, Oh The Biomes We've Gone, and Immersive Engineering furniture is enabled only when the corresponding mod is present. None of these mods is a hard dependency. Recipes and recipe advancements for optional furniture are loaded conditionally with their integration.

Existing IronAgeFurniture BOP Cherry furniture is converted to vanilla Cherry. Supported older BYG furniture is converted to its BWG equivalent when BWG is installed.

Back up your world and try upgrades on a copy first. Worlds from newer Minecraft versions cannot be downgraded. Some retired woods and third-party blocks have no replacement. Lamp data from missing mods and older advancement progress are kept for later recovery; this does not make every old modpack compatible.

## Build

Use the included Gradle wrapper with Temurin Java 21.0.7+6:

```text
gradlew.bat clean
gradlew.bat check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate Eclipse/Buildship metadata and synchronize production-only classes and processed resources with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Run this command from a terminal before the first Eclipse import or Gradle refresh. It records the exact Temurin 21.0.7+6 installation in the machine-local Buildship preferences; otherwise Eclipse may select a different runtime that is rejected by the pinned-toolchain check.

Releases use the manual publication workflow after CI passes. A local build does not publish anything. The Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.4.0.121012`.

Furniture registrations and their matching blockstates, models, recipes, advancements, and translations are generated from `gradle/furniture-catalog.json`. Normal builds verify committed output without changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally changing the catalog.

The Java package is `zone.moddev.mc.ironagefurniture`. The mod ID and saved-data namespace remain `ironagefurniture`.
