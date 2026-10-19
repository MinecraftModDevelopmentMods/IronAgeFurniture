# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lighting, and other ye olde-style furniture to Minecraft.

This branch targets Minecraft 26.1.2 with NeoForge 26.1.2.94. Its current complete version is `0.4.0.2601022` and its Maven coordinate is `zone.moddev.mc:iron-age-furniture:0.4.0.2601022`.

Phase 4 adds wingback chairs, thrones, wooden and canopy beds, double beds, removable shield-chair shields, and candle sconces with four individually stored vanilla candles. Chairs, benches, beds and lights have separate creative tabs. Gameplay settings are synchronized from the server, and placed lighting variants have readable HUD names in all seventeen bundled languages.

Back up worlds before upgrading. Furniture migration preserves supported historical registry identities, colours and stored item components. Open only worlds from Minecraft 26.1.2 or earlier; this downward code port does not make newer Minecraft worlds safe to downgrade.

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

Biomes O' Plenty furniture remains built into this branch, not a separate add-on. It is optional and registers only when compatible BOP is loaded; its models are exposed through a conditional resource pack and its recipes and advancements require both the provider and the relevant furniture feature. The pinned runtime is BOP 26.1.2.0.22, GlitchCore 26.1.2.0.2 and TerraBlender 26.1.2.0.3. Upstream textures are referenced, not redistributed. Oh The Biomes We've Gone and Immersive Engineering furniture are not registered on this target.

For isolated development clients, use `runClientBase` for core-only play or `runClientWithBop` for the built-in BOP integration. Set `bopRuntimeDirectory` in ignored `local.properties` or with `-P` to a directory containing those three exact dependency jars. These profiles use production output and separate save directories.

The Java API namespace is `zone.moddev.mc.ironagefurniture`. The persistent runtime mod, registry, resource, data, configuration, and saved-world namespace remains `ironagefurniture`.
