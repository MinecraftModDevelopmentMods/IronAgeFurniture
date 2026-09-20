# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This branch targets Minecraft 26.3 with Forge 66.0.2. Its current complete version is `0.3.0.2603001` and its Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.3.0.2603001`.

## Optional integrations

No optional biome or engineering integration currently has a compatible Forge 26.3 release, so this target contains the complete vanilla wood catalog only. Biomes O' Plenty, Oh The Biomes We've Gone, and Immersive Engineering furniture can be reintroduced after their owning mods publish compatible builds that can be pinned and tested.

The scoped missing-mapping aliases for retired IronAgeFurniture Biomes O' Plenty Cherry IDs remain so those IDs continue to resolve to the equivalent vanilla Cherry furniture.

## Build

Use the checked-in Gradle wrapper and exact Temurin Java 25.0.3+9 toolchain. The checksum-sealed Mavenizer fixture also runs under Java 25 and prepares Forge without downloading build tools dynamically in CI.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts writeReleaseChecksums verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Release publication is initiated manually from the protected default-branch dispatcher after the exact target commit has passed hosted CI. Building locally does not tag or publish a release.

Furniture registrations and their matching blockstates, models, recipes, advancements, item definitions, and translations are generated from `gradle/furniture-catalog.json`. Normal builds verify committed output without changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally changing the catalog.

The Java API namespace is `zone.moddev.mc.ironagefurniture`. The persistent Forge mod, registry, resource, data, configuration, and saved-world namespace remains `ironagefurniture`.
