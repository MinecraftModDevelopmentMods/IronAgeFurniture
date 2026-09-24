[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge](https://img.shields.io/badge/CurseForge-Iron%20Age%20Furniture-orange.svg)](https://www.curseforge.com/minecraft/mc-mods/iron-age-furniture)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml/badge.svg?branch=master-1.16)](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml?query=branch%3Amaster-1.16)

# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lamps, sconces, and other ye
olde-style furniture to Minecraft. Seating is functional, furniture supports
the appropriate vanilla and optional-mod materials, and lighting includes
falling and throwable lava lamps.

This branch provides IronAgeFurniture `0.3.0.116051` for Minecraft 1.16.5 and
Forge 36.2.34. It requires Java 8 and can be installed on both clients and
dedicated servers.

## Optional integrations

IronAgeFurniture adds matching furniture and recipes when these mods are
installed:

- Biomes O' Plenty
- Immersive Engineering
- Oh The Biomes You'll Go

They are optional. Their recipes and recipe advancements are conditionally
loaded, so a normal installation does not need any of them.

## Compatibility

The persistent mod ID, registry names, resource paths, configuration names,
entities, and saved-world identity remain under `ironagefurniture`. Existing
1.16.5 worlds therefore retain the same runtime identities.

This release can also upgrade supported IronAgeFurniture furniture and Phase 3
lights saved by the Forge 1.10.2 and 1.12.2 editions across Minecraft's
flattening. Facing, connected-bench shape, lighting state, and padded-bench
colour are preserved. The original red-only padded benches become red, while
the later sixteen-colour format keeps its saved `Color` value in placed blocks,
inventories, containers, and dropped items.

Worlds using the Iron Age Furniture Oh The Biomes Add-On for 1.12.2 are also
supported. The twenty-two woods still present in BYG 1.16.5 keep their matching
furniture. Retired woods use documented visual substitutes: Frozen Oak becomes
Aspen, Great Oak becomes vanilla Oak, Hawthorn becomes Cherry, Ironwood becomes
Ebony, and Rowan becomes Maple. Palm remains Palm in this version.

Always back up a world before moving it to a newer Minecraft version. Once the
world has been saved by 1.16.5 it cannot safely be reopened in an older version.

The supported Java source namespace is now
`zone.moddev.mc.ironagefurniture`. Add-ons compiled against the former
`com.mcmoddev.ironagefurniture` packages must update their imports. The Maven
coordinate is
`zone.moddev.mc:iron-age-furniture:0.3.0.116051`.

See [CHANGELOG.md](CHANGELOG.md) for the release notes and
[docs/VERSIONS.md](docs/VERSIONS.md) for the versioning scheme. Bugs can be
reported through the
[MMD issue tracker](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/issues).

## Building

Use the checked-in Gradle wrapper with a Java 17 runtime and a Temurin Java 8
compiler toolchain. CI pins the compiler to 8.0.502+7; Eclipse may use a newer
Temurin Java 8 patch release for the project JRE while Buildship runs Gradle on
Java 17. The sealed ForgeGradle preparation fixture uses Java 25.0.3+9.0.LTS.
ForgeGradle prepares the Forge 36 dependency from the checked-in wrapper
configuration without downloading a separate build tool.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Release publication is initiated manually from the protected default-branch
dispatcher after the exact target commit has passed hosted CI. Building
locally does not tag or publish a release.

IronAgeFurniture is licensed under LGPL-2.1.
