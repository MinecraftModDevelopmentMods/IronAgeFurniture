[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge](https://img.shields.io/badge/CurseForge-Iron%20Age%20Furniture-orange.svg)](https://www.curseforge.com/minecraft/mc-mods/iron-age-furniture)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml/badge.svg?branch=master-1.20.1)](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml?query=branch%3Amaster-1.20.1)

# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lamps, sconces, and other ye
olde-style furniture to Minecraft. Seating is functional, furniture supports
the appropriate vanilla and optional-mod materials, and lighting includes
falling and throwable lava lamps.

This branch provides IronAgeFurniture `0.3.0.120011` for Minecraft 1.20.1 and
Forge 47.4.10. It requires Java 17 and can be installed on both clients and
dedicated servers.

## Optional integrations

IronAgeFurniture adds matching furniture and recipes when these mods are
installed:

- Biomes O' Plenty
- Immersive Engineering
- Oh The Biomes We've Gone

They are optional. Their recipes and recipe advancements are conditionally
loaded, so a normal installation does not need any of them.

Biomes O' Plenty 18 and 19 both receive furniture for their shared wood set.
When BOP 19.0.0.96 or newer is detected, IronAgeFurniture additionally enables
Empyreal, Maple, and Pine furniture. This does not impose a minimum BOP version.
Back up a world before downgrading from BOP 19 to BOP 18: those three source
woods, and furniture made from them, are not available in BOP 18.

## Compatibility

The persistent mod ID, core registry names, resource paths, configuration
names, entities, and saved-world identity remain under `ironagefurniture`.
The 1.20.1 port also remaps IronAgeFurniture's former Biomes O' Plenty cherry
furniture to vanilla cherry and supported BYG furniture names to their BWG
replacements. This scoped remapping does not provide a general BYG world
migration.

The supported Java source namespace is
`zone.moddev.mc.ironagefurniture`. The Maven coordinate is
`zone.moddev.mc:iron-age-furniture:0.3.0.120011`.

See [CHANGELOG.md](CHANGELOG.md) for the release notes and
[docs/VERSIONS.md](docs/VERSIONS.md) for the versioning scheme. Bugs can be
reported through the
[MMD issue tracker](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/issues).

## Building

Use the checked-in Gradle wrapper with Temurin Java 17.0.1+12. ForgeGradle's
Renamer uses Temurin Java 8.0.502+7, while the checksum-sealed Mavenizer
compatibility fixture runs with Temurin Java 25.0.3+9.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Before importing the nested project into its Eclipse workspace, generate the
reproducible Buildship metadata and Forge launch profiles with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Release publication is initiated manually from the protected default-branch
dispatcher after the exact target commit has passed hosted CI. Building
locally does not tag or publish a release.

Furniture registrations and their matching blockstates, models, recipes,
advancements, and translations are generated from
`gradle/furniture-catalog.json`. Normal builds verify committed output without
changing it; run `gradlew.bat updateFurnitureCatalog` only when intentionally
changing the catalog.

IronAgeFurniture is licensed under LGPL-2.1.
