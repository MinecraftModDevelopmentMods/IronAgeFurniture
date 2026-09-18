[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge](https://img.shields.io/badge/CurseForge-Iron%20Age%20Furniture-orange.svg)](https://www.curseforge.com/minecraft/mc-mods/iron-age-furniture)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml/badge.svg?branch=master-1.20.1-neo)](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml?query=branch%3Amaster-1.20.1-neo)

# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lamps, sconces, and other ye
olde-style furniture to Minecraft. Seating is functional, furniture supports
the appropriate vanilla and optional-mod materials, and lighting includes
falling and throwable lava lamps.

This branch provides IronAgeFurniture `0.3.0.120012` for Minecraft 1.20.1 and
early NeoForge 47.1.99 or newer in the 47.x line. It is built against NeoForge
47.1.106, requires Java 17, and can be installed on both clients and dedicated
servers.

NeoForge 1.20.1 retained the Forge-era Java API, `mods.toml`, `forge:*` data
conditions, and ForgeGradle user-development contract. Those names are
therefore intentional on this branch and do not indicate a Forge loader build.

## Optional integrations

IronAgeFurniture adds matching furniture and recipes when these mods are
installed:

- Biomes O' Plenty
- Immersive Engineering
- Oh The Biomes We've Gone

They are optional. Their recipes and recipe advancements are conditionally
loaded, so a normal installation does not need any of them.

The positive-path compatibility set pins BWG 1.5.11, the newest published
1.20.1 build explicitly marked for NeoForge. Later Forge-only BWG releases
require Forge 47.4+ and are not compatible with this early NeoForge line.

The tested Biomes O' Plenty target is 18.0.0.598, which receives furniture for
its complete 1.20.1 wood set. BOP 19.0.0.96 was also inspected, but it and its
GlitchCore dependency require Forge 47.3+ and cannot start on the early
NeoForge 47.1 line. Its Empyreal, Maple, and Pine tier is therefore not
advertised as supported by this branch.

## Compatibility

The persistent mod ID, core registry names, resource paths, configuration
names, entities, and saved-world identity remain under `ironagefurniture`.
The 1.20.1 port also remaps IronAgeFurniture's former Biomes O' Plenty cherry
furniture to vanilla cherry and supported BYG furniture names to their BWG
replacements. This scoped remapping does not provide a general BYG world
migration.

The supported Java source namespace is
`zone.moddev.mc.ironagefurniture`. The Maven coordinate is
`zone.moddev.mc:iron-age-furniture:0.3.0.120012`.

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
reproducible Buildship metadata and early-NeoForge launch profiles with:

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
