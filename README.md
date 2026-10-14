[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge](https://img.shields.io/badge/CurseForge-Iron%20Age%20Furniture-orange.svg)](https://www.curseforge.com/minecraft/mc-mods/iron-age-furniture)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml/badge.svg?branch=master-1.15)](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml?query=branch%3Amaster-1.15)

# IronAgeFurniture

IronAgeFurniture adds functional chairs, stools, benches, beds, lamps and
sconces in an old-fashioned style. Furniture supports
the appropriate vanilla and optional-mod materials, and lighting includes
falling and throwable lava lamps.

This branch provides IronAgeFurniture `0.4.0.115021` for Minecraft 1.15.2 and
Forge 31.2.57. It requires Java 8 and can be installed on both clients and
dedicated servers.

## Phase 4 furniture and lighting

Choose from four creative tabs: Chairs, Benches, Beds and Lights. This edition
keeps all 39 wood families, including the optional biome-mod woods.

- Make a wooden bed with a vanilla bed and a matching plank. Its colour stays
  the same. Add another matching plank and matching carpet for a canopy bed.
  Two identical single beds make a double; carpet can deliberately recolour
  an IAF bed, but those recolouring recipes stay out of the recipe book.
- Upgrade a classic chair with a matching plank and carpet to make a wingback.
  A wingback, another matching plank and matching carpet make a throne. Both
  upgrades are shapeless, and their recipe-book entries use the exact colour.
- Remove and reattach a shield without losing its banner design, damage,
  enchantments, name or other saved item data.
- Light the room with candles, twin-torch sconces and optional Mineralogy
  rock-salt lamps. Five matching metal nuggets make four sconces.
- Carrying a candle shows a small flame and smoke effect. Moving light still
  needs OptiFine or a compatible dynamic-light mod; IAF does not add its own
  dynamic-light engine.

Sconces keep their metal and contents when broken by hand or a tool. A lava
lamp is the intentional exception: Silk Touch recovers it intact. Ordinary
breaking shatters a floor lamp; mining a filled wall sconce releases the vial
to fall and shatter on landing, or turn into obsidian if it lands in water.
Right-clicking to remove the lamp from its sconce is safe. Powering a wall
lava-lamp sconce also releases the lamp as a falling trap.

Click a candle or torch sconce with a water bucket to put its flames out without
pouring water or emptying the bucket. This also works for standalone candles.
Water placed around the holder still waterlogs and extinguishes it normally.

Waterlogged candle and torch sconces burn while powered by redstone and go
out again when the signal stops. Dry sconces stay lit after losing power.

## Optional integrations

IronAgeFurniture adds matching furniture and recipes when these mods are
installed:

- Biomes O' Plenty
- Immersive Engineering
- Oh The Biomes You'll Go
- Base Metals (additional sconce metals)
- Mineralogy (rock-salt sconces)
- MrCrayfish's Furniture Mod (chair conversion recipes)

They are optional. Their recipes and recipe advancements are conditionally
loaded, so a normal installation does not need any of them.

CFM world conversion is off by default while both mods are installed. Enable
the conversion option only when you want supported CFM chairs replaced with
IAF chairs. Removed-mod recovery is also provided for supported old chairs.

## Compatibility

The persistent mod ID, registry names, resource paths, configuration names,
entities, and saved-world identity remain under `ironagefurniture`. Existing
1.15.2 worlds therefore retain the same runtime identities.

The upgrade bridge imports supported Phase 2, 3 and 4 furniture from Forge
1.10.2 and 1.12.2, as well as older 1.14.4 and 1.15.2 saves. It preserves
facing, connected benches, bed and chair parts, upholstery, sconce metals and
stored shields. The original red-only benches become red; later colours and
metals become the matching current items in inventories, containers and drops.
New coloured furniture uses distinct item IDs rather than recipe-selection NBT.

Worlds using the Iron Age Furniture Oh The Biomes Add-On for 1.12.2 are also
supported. The twenty-one woods still present in BYG 1.15.2 keep their matching
furniture. Retired woods use documented visual substitutes: Frozen Oak becomes
Aspen, Great Oak becomes vanilla Oak, Hawthorn becomes Cherry, Ironwood becomes
Ebony, Palm becomes Baobab, and Rowan becomes Maple.

Always back up a world before moving it to a newer Minecraft version. Once the
world has been saved by 1.15.2 it cannot safely be reopened in an older version.
See [the upgrade guide](docs/UPGRADING.md) for tested paths, optional-mod limits
and a short checklist before using a valuable world.

The supported Java source namespace is now
`zone.moddev.mc.ironagefurniture`. Add-ons compiled against the former
`com.mcmoddev.ironagefurniture` packages must update their imports. The Maven
coordinate is
`zone.moddev.mc:iron-age-furniture:0.4.0.115021`.

See [CHANGELOG.md](CHANGELOG.md) for the release notes and
[docs/VERSIONS.md](docs/VERSIONS.md) for the versioning scheme. Bugs can be
reported through the
[MMD issue tracker](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/issues).

## Building

Use the checked-in Gradle wrapper with a Java 17 runtime and a Temurin Java 8
compiler toolchain. CI pins the compiler to 8.0.502+7; Eclipse may use a newer
Temurin Java 8 patch release for the project JRE while Buildship runs Gradle on
Java 17. The sealed ForgeGradle preparation fixture uses Java 25.0.3+9.0.LTS.
ForgeGradle prepares the Forge 31 dependency from the checked-in wrapper
configuration without downloading a separate build tool.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

For development testing, choose `runClient` for IAF alone or
`runClientCompatibility` for IAF with the optional mods. These profiles work
in Eclipse and Gradle. The core-only profile uses `run-core`; the compatibility
profile keeps the existing `run` folder and its `mods`, saves and settings.
Their worlds and configuration are separate, and no existing saves are moved.
Keep optional jars in `run/mods`, not `run-core/mods`, and prepare Eclipse again
after changing them. See [the build guide](gradle/README.md) for the development
copies used by optional mods.

Release publication is initiated manually from the protected default-branch
dispatcher after the exact target commit has passed hosted CI. Building
locally does not tag or publish a release.

IronAgeFurniture is licensed under LGPL-2.1.
