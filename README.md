# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This branch targets Minecraft 1.20.1 with Forge 47.4.10. Its current complete version is `0.3.0.120011` and its Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.3.0.120011`.

## Build

Use the checked-in Gradle wrapper with Temurin Java 17.0.1+12. ForgeGradle's Renamer uses Temurin Java 8.0.502+7, while the checked-in Mavenizer compatibility fixture uses Temurin Java 25.0.3+9.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Release publication is initiated manually from the protected default-branch dispatcher after the exact target commit has passed hosted CI. Building locally does not tag or publish a release.

Furniture registrations and their matching blockstates, models, recipes, advancements, and translations are generated from `gradle/furniture-catalog.json`. Normal builds verify committed output without changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally changing the catalog.

The Java API namespace is `zone.moddev.mc.ironagefurniture`. The persistent Forge mod, registry, resource, data, configuration, and saved-world namespace remains `ironagefurniture`.
