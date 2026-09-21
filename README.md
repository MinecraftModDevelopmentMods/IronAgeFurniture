[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge](https://img.shields.io/badge/CurseForge-Iron%20Age%20Furniture-orange.svg)](https://www.curseforge.com/minecraft/mc-mods/iron-age-furniture)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml/badge.svg?branch=master-1.10)](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml?query=branch%3Amaster-1.10)

# IronAgeFurniture

IronAgeFurniture adds functional chairs, stools, benches, lamps, and sconces
to Minecraft. This branch provides the Phase 3 lighting release
`0.3.0.110021` for Minecraft 1.10.2 and Forge 12.18.3.2511.

Phase 3 contains the established seating catalog plus empty, torch,
redstone-torch, glow, lava, and redstone lighting, including throwable lava
lamps and obsidian chunks. The separate `feature/1.10-v1.0.0` line contains
later furniture development and is intentionally not part of this release.

Padded benches and padded back benches support all sixteen vanilla carpet
colours without consuming additional block or item IDs. Existing benches and
damage-zero items remain red, while newly crafted colours persist through
placement, drops, pick block, connected-shape changes, and chunk reloads. Each
coloured item also carries the stable `Color` NBT string used by later-version
migration; metadata remains present for native 1.10 rendering and compatibility.
Mixed-colour bench runs may connect normally; colour does not participate in
the established joining algorithm.

## Optional integrations

Matching furniture and Java-side recipes are registered when these mods are
loaded:

- Biomes O' Plenty
- Natura
- Forestry
- Immersive Engineering

All integrations are optional. A normal installation requires none of them,
and furniture for an absent integration is not registered. Minecraft 1.10.2
predates the recipe-book advancement format, so this target registers recipes
in Java rather than packaging later-version recipe or advancement JSON.

## Compatibility

The persistent mod ID, registry names, resource paths, configuration names,
entities, and saved-world identity remain under `ironagefurniture`. Existing
Phase 2 worlds therefore retain their surviving furniture identities.

The supported Java namespace is `zone.moddev.mc.ironagefurniture`. Add-ons
compiled against `com.mcmoddev.ironagefurniture` must update their imports.
The Maven coordinate is
`zone.moddev.mc:iron-age-furniture:0.3.0.110021`.

See [CHANGELOG.md](CHANGELOG.md) for release notes and
[docs/VERSIONS.md](docs/VERSIONS.md) for the versioning scheme. Report bugs
through the [MMD issue tracker](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/issues).

## Building

Use the checked-in Gradle wrapper with Java 17 or newer for Gradle and a
Temurin Java 8 compiler toolchain. CI pins Java 17 for Gradle and Java
8.0.502+7 for compilation; Eclipse may run Buildship on a newer JVM and may
use a newer Temurin Java 8 patch release for the compiler toolchain.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

Release publication is initiated manually from the protected default-branch
dispatcher after the exact target commit passes hosted CI. Local builds do not
tag or publish a release.

IronAgeFurniture is licensed under LGPL-2.1.
