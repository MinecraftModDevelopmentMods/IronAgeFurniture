# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This branch targets Minecraft 1.20.6 with NeoForge 20.6.139. Its current complete version is `0.3.0.120062` and its Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.3.0.120062`.

## Optional integrations

Biomes O' Plenty furniture is enabled only when Biomes O' Plenty is present; it is not a hard dependency. Its recipes and recipe advancements are loaded conditionally with the integration, and existing IronAgeFurniture BOP Cherry furniture remaps to vanilla Cherry.

Oh The Biomes We've Gone and Immersive Engineering furniture are intentionally unavailable because neither integration has a compatible release for this target.

## Build

Use the checked-in Gradle wrapper with Temurin Java 21.0.7+6. This NeoForge target uses NeoGradle directly and does not need the Forge Mavenizer preparation fixture.

```text
gradlew.bat clean
gradlew.bat check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Run the clean and verification commands separately because NeoGradle resolves model inputs under `build/tmp` while configuring the verification build.

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Release publication is initiated manually from the protected default-branch dispatcher after the exact target commit has passed hosted CI. Building locally does not tag or publish a release.

Furniture registrations and their matching blockstates, models, recipes, advancements, and translations are generated from `gradle/furniture-catalog.json`. Normal builds verify committed output without changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally changing the catalog.

The Java API namespace is `zone.moddev.mc.ironagefurniture`. The persistent runtime mod, registry, resource, data, configuration, and saved-world namespace remains `ironagefurniture`.
