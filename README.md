# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This branch targets Minecraft 1.21.1 with NeoForge 21.1.247. Its current complete version is `0.3.0.121012` and its Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.3.0.121012`.

## Optional integrations

Biomes O' Plenty, Oh The Biomes We've Gone, and Immersive Engineering furniture is enabled only when the corresponding mod is present. None of these mods is a hard dependency. Recipes and recipe advancements for optional furniture are loaded conditionally with their integration.

Existing IronAgeFurniture BOP Cherry furniture remaps to vanilla Cherry, while supported legacy BYG furniture names remap to their BWG replacements when BWG is installed.

## Build

Use the checked-in Gradle wrapper with Temurin Java 21.0.7+6. This NeoForge target uses NeoGradle directly and does not need the Forge Mavenizer preparation fixture.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Run this command from a terminal before the first Eclipse import or Gradle refresh. It records the exact Temurin 21.0.7+6 installation in the machine-local Buildship preferences; otherwise Eclipse may select a different runtime that is rejected by the pinned-toolchain check.

Release publication is initiated manually from the protected default-branch dispatcher after the exact target commit has passed hosted CI. Building locally does not tag or publish a release.

Furniture registrations and their matching blockstates, models, recipes, advancements, and translations are generated from `gradle/furniture-catalog.json`. Normal builds verify committed output without changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally changing the catalog.

The Java API namespace is `zone.moddev.mc.ironagefurniture`. The persistent Forge mod, registry, resource, data, configuration, and saved-world namespace remains `ironagefurniture`.
