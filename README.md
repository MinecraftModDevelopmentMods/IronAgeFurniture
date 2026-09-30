[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge](https://img.shields.io/badge/CurseForge-Iron%20Age%20Furniture-orange.svg)](https://www.curseforge.com/minecraft/mc-mods/iron-age-furniture)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml/badge.svg?branch=master-1.10)](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml?query=branch%3Amaster-1.10)

# IronAgeFurniture

IronAgeFurniture adds functional chairs, stools, benches, beds, lamps, and
sconces to Minecraft. The unreleased `0.4.0.110021` candidate adds Phase 4
furniture and lighting for Minecraft 1.10.2 with Forge 12.18.3.2511.

Phase 3 contains the established seating collection plus empty, torch,
redstone-torch, glow, lava, and redstone lighting, including throwable lava
lamps and obsidian chunks.

Phase 4 adds multiblock wooden and canopy beds, wingback and throne chairs,
removable shield chairs, twin-torch sconces, candles, Mineralogy rock-salt
sconces, and gold/Base Metals sconce materials. Wall lava-lamp sconces can
release their lamp on redstone, landing intact in Creative but shattering and
igniting the landing area in other modes. Seventeen locale choices and CFM
wooden-chair migration support are included.

Carried candles show a small animated flame and a little smoke without any
extra mod. With OptiFine's Dynamic Lights enabled, a held candle can also light
the area around you; that illumination is an OptiFine visual effect, not a
placed light block.

## Requirements

- Minecraft 1.10.2
- Forge 12.18.3.2511
- Java 8 for the game

Padded benches and padded back benches support all sixteen vanilla carpet
colours without consuming additional block or item IDs. Existing benches and
damage-zero items remain red, while newly crafted colours persist through
placement, drops, pick block, connected-shape changes, and chunk reloads.
Items retain a stable colour name for compatibility with future Minecraft
versions as well as the metadata used by Minecraft 1.10. Mixed-colour bench
runs may connect normally.

## Optional integrations

Matching furniture and Java-side recipes are registered when these mods are
loaded:

- Biomes O' Plenty
- Natura
- Forestry
- Immersive Engineering
- Base Metals (extra sconce materials)
- Mineralogy (rock-salt lamps)
- MrCrayfish's Furniture Mod (chair conversion recipes)

All integrations are optional. A normal installation requires none of them,
and furniture for an absent integration is not registered. Minecraft 1.10.2
predates the recipe-book advancement format, so this target registers recipes
in Java rather than packaging later-version recipe or advancement JSON.
The available Base Metals sconce materials follow the installed Base Metals
build; older 1.10 builds do not provide Antimony, Bismuth, or Pewter. Sconce
mining hardness and blast resistance also vary with the chosen metal.

## Compatibility

The persistent mod ID, registry names, resource paths, configuration names,
entities, and saved-world identity remain under `ironagefurniture`. Existing
Phase 2 and Phase 3 worlds therefore retain their surviving furniture
identities. Untagged older furniture uses red upholstery or a plain shield;
older sconces with no metal data remain iron. When CFM is absent, its six
wooden chair block and saved item identities are recovered as classic chairs;
IAF records the old numeric IDs in the world so unopened chunks still migrate
after a restart. Forced conversion while CFM remains installed is a separate,
default-off option. Back up older worlds before first opening them in Phase 4.

The supported Java namespace is `zone.moddev.mc.ironagefurniture`. Add-ons
compiled against the former `com.mcmoddev.ironagefurniture` packages must
update their imports. The Maven coordinate is
`zone.moddev.mc:iron-age-furniture:0.4.0.110021`.

See [CHANGELOG.md](CHANGELOG.md) for release notes and
[docs/VERSIONS.md](docs/VERSIONS.md) for the versioning scheme. Report bugs
through the [MMD issue tracker](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/issues).

## Building from source

Use the checked-in Gradle wrapper with Java 17 or newer for Gradle and a
Temurin Java 8 compiler toolchain. CI pins Java 17 for Gradle and Java
8.0.502+7 for compilation; Eclipse may run Buildship on a newer JVM and may
use a newer Temurin Java 8 patch release for the compiler toolchain.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

The build tasks and their scripts are described in [gradle/README.md](gradle/README.md).

The release process only publishes commits that have passed the hosted build,
test, artifact, and security checks. Running the Gradle commands above creates
local artifacts only.

IronAgeFurniture is licensed under LGPL-2.1.
