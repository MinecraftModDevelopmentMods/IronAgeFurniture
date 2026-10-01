[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge](https://img.shields.io/badge/CurseForge-Iron%20Age%20Furniture-orange.svg)](https://www.curseforge.com/minecraft/mc-mods/iron-age-furniture)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml/badge.svg?branch=master-1.12)](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml?query=branch%3Amaster-1.12)

# IronAgeFurniture

IronAgeFurniture adds functional chairs, stools, benches, beds, lamps, and
sconces in an old-fashioned style. Furniture uses vanilla woods and, when
installed, matching materials from supported mods.

This branch is the unreleased `0.4.0.112021` Phase 4 candidate for Minecraft
1.12.2 and Forge 14.23.5.2859. The game requires Java 8; the mod runs on
clients and dedicated servers.

Phase 4 adds multiblock wooden and canopy beds, wingback and throne chairs,
removable shields on shield chairs, twin-torch sconces, candles, rock-salt
sconces, and metal sconce choices. A carried candle shows a small flame and
smoke. With OptiFine Dynamic Lights enabled, it may also light the surrounding
area while held. A powered wall lava-lamp sconce releases its lamp when there
is room below; it can fall, shatter, and start a fire.

Mining a lava-lamp sconce with a suitable Silk Touch pickaxe returns the empty
sconce and intact lamp separately. Without Silk Touch, the sconce still drops
in its original metal, but the lamp shatters and starts a fire.

Padded benches and padded back benches support all sixteen vanilla carpet
colours without adding block or item IDs. Existing benches and data-zero items
remain red. New stacks retain both their legacy metadata and a stable `Color`
NBT string through crafting, Creative inventory, placement, drops, pick block,
connected-shape changes and chunk reloads. Mixed-colour runs remain governed by
the unchanged bench joining algorithm.

The recipe book lists furniture designs, woods and upholstery colours
separately, so you can find the piece you want without cycling through the
whole seating collection. Alternate recipes for the same chair share an entry.

Beds keep their colour as you build them up. Craft a vanilla bed with one plank
of your chosen wood to make a wooden bed, then add another matching plank to
make a canopy bed. Combine two single beds of the same wood, style and colour
to make a double bed. To change any IAF bed's colour deliberately, craft it
with a carpet in the colour you want.

## Optional integrations

IronAgeFurniture adds matching furniture and recipes when these mods are
installed:

- Biomes O' Plenty
- Natura
- Forestry
- Immersive Engineering
- Mineralogy (rock-salt lighting)
- Base Metals (extra sconce materials)
- MrCrayfish's Furniture Mod (chair conversion recipes)

They are optional. Their recipes and advancements load only when the relevant
mod is installed. The available Base Metals sconce variants follow the
installed Base Metals build.

## Compatibility

The persistent mod ID, surviving registry names, resource paths,
configuration names, and saved-world identity remain under
`ironagefurniture`. Older padded benches without colour data stay red; older
sconces without metal data stay iron. Beds and other upholstered furniture
keep their colour when placed, moved, broken, and reloaded. Shield chairs
retain the complete attached shield item, including its wear, design, name,
and enchantments.

If MrCrayfish's Furniture Mod is removed from an older world, this build can
recover its six wooden chair types as IAF classic chairs. Conversion while
both mods remain installed is a separate option, off by default. Back up any
world before opening it with a new mod version.

The supported Java source namespace is now
`zone.moddev.mc.ironagefurniture`. Add-ons compiled against the former
`com.mcmoddev.ironagefurniture` packages must update their imports. The Maven
coordinate is
`zone.moddev.mc:iron-age-furniture:0.4.0.112021`.

Furniture add-ons can register subclasses of the public IAF furniture blocks
under their own mod namespace and place their items in the IAF creative tab.
Connected bench subclasses participate in the normal joining behaviour, and
the client API can register an add-on namespace and item with the shared
sixteen-colour padded-bench renderer. Add-ons should retain ownership of their
own registry and resource IDs rather than registering new content as
`ironagefurniture`.

See [CHANGELOG.md](CHANGELOG.md) for the release notes,
[docs/UPGRADING.md](docs/UPGRADING.md) for tested old-world upgrades, and
[docs/VERSIONS.md](docs/VERSIONS.md) for the versioning scheme. Bugs can be
reported through the
[MMD issue tracker](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/issues).

## Building

Use the checked-in Gradle wrapper with a Java 17 runtime and a Temurin Java 8
compiler toolchain. CI pins the compiler to 8.0.502+7; Eclipse may use a newer
Temurin Java 8 patch release for the project JRE while Buildship runs Gradle on
Java 17. ForgeGradle prepares the Forge 1.12.2 dependency from the checked-in
wrapper configuration without downloading a separate build tool.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

The build tasks are described in [gradle/README.md](gradle/README.md).

Release publication is initiated manually from the protected default-branch
dispatcher after the exact target commit has passed hosted CI. Building
locally does not tag or publish a release.

IronAgeFurniture is licensed under LGPL-2.1.
