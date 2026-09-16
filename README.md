# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This branch targets Minecraft 26.3 with NeoForge 26.3.0.1-beta. Its current complete version is `0.3.0.2603002` and its Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.3.0.2603002`.

## Build

Use the checked-in Gradle wrapper with the exact Temurin Java 25.0.3+9 toolchain. This NeoForge target uses NeoGradle directly and does not need the Forge Mavenizer preparation fixture.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Release publication is initiated manually from the protected default-branch dispatcher after the exact target commit has passed hosted CI. Building locally does not tag or publish a release.

Furniture registrations and their matching blockstates, models, recipes, advancements, and translations are generated from `gradle/furniture-catalog.json`. Normal builds verify committed output without changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally changing the catalog.

No optional biome or engineering integration currently has a compatible NeoForge 26.3 release, so this target contains the complete vanilla wood catalog only. Integrations can be reintroduced when their owning mods publish compatible builds that can be pinned and tested.

The Java API namespace is `zone.moddev.mc.ironagefurniture`. The persistent runtime mod, registry, resource, data, configuration, and saved-world namespace remains `ironagefurniture`.
