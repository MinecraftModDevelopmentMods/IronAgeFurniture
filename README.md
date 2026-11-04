[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge](https://img.shields.io/badge/CurseForge-Iron%20Age%20Furniture-orange.svg)](https://www.curseforge.com/minecraft/mc-mods/ironagefurniture)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml/badge.svg?branch=master-1.17)](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml?query=branch%3Amaster-1.17)

# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lamps, sconces, and other ye
olde-style furniture to Minecraft. Seating is functional, furniture supports
the appropriate vanilla and optional-mod materials, and lighting includes
falling and throwable lava lamps.

This branch provides IronAgeFurniture `0.4.1.117011` for Minecraft 1.17.1 and
Forge 37.1.1. It requires Java 16 and can be installed on both clients and
dedicated servers.

## Phase 4 furniture and lighting

The four creative tabs are Chairs, Benches, Beds and Lights. Wooden and canopy
beds, matching doubles, wingbacks and thrones use all 19 existing woods and all
sixteen upholstery colours. Shield-chair backs are removable real shields,
preserving designs, damage, enchantments, names and other item data.

- Vanilla bed + matching plank makes a wooden bed of the same colour.
- Wooden bed + matching plank + matching carpet makes a canopy bed.
- Two identical singles make a matching double bed.
- Classic chair + matching plank + carpet makes a wingback.
- Wingback + matching plank + matching carpet makes a throne.
- Five matching nuggets make four sconces. Without copper nuggets, five copper
  ingots make thirty-two copper sconces.

Chair upgrades are shapeless. Owning or crafting the matching single bed or
wingback reveals its upgrade while the table remains open. Manual bed
recolouring remains available with carpet, but stays out of the recipe book.

Candles, twin-torch and multi-candle sconces, rock-salt sconces and metal frames
join the existing lights. Survival mining and support removal recover the
frame and contents, except for intentional lava-lamp shattering. Silk Touch
recovers an intact lamp; ordinary wall-sconce mining releases the lamp to fall
and shatter. Right-click removal is safe.

Water buckets waterlog candle and torch sconces and extinguish their flames.
Powered wet candle and torch sconces relight, then go out again when power stops.
Glass lamps break quickly, while frames retain their metal's toughness.

Candle sconces hold up to four vanilla candles, including their saved item data.
Sneak-click removes the newest candle first. Empty frames and the metal of each
frame are preserved when the contents change. Held lights have no flame effects
or dynamic lighting.

## Optional integrations

IronAgeFurniture adds matching furniture and recipes when these mods are
installed:

- Biomes O' Plenty adds its supported woods.
- Base Metals supplies additional sconce metals.
- Mineralogy supplies rock-salt lamps.
- MrCrayfish's Furniture Mod adds supported chair-conversion recipes.

None is required. Unavailable recipes and creative entries stay hidden. Known
metals remain saved if Base Metals is temporarily removed. Forced CFM world
conversion is off by default; removed-mod recovery covers supported wooden
chairs, not every CFM block.

## Compatibility

The persistent mod ID, registry names, resource paths, configuration names,
entities, and saved-world identity remain under `ironagefurniture`. Existing
1.17.1 worlds therefore retain the same runtime identities.

This release can also upgrade supported IronAgeFurniture furniture and Phase 3
lights saved by the Forge 1.10.2 and 1.12.2 editions across Minecraft's
flattening. Facing, connected-bench shape, lighting state, and padded-bench
colour are preserved. The original red-only padded benches become red, while
the later sixteen-colour format keeps its saved `Color` value in placed blocks,
inventories, containers, and dropped items.

Only furniture with a matching 1.17.1 implementation can be carried forward.
In particular, the separate 1.12.2 Oh The Biomes Add-On does not have a 1.17.1
target catalog and is not imported by this branch. Older Immersive Engineering
furniture is also unsupported here. See [docs/UPGRADING.md](docs/UPGRADING.md)
for the tested upgrade paths and limitations.

Always back up a world before moving it to a newer Minecraft version. Once the
world has been saved by 1.17.1 it cannot safely be reopened in an older version.

The supported Java source namespace is now
`zone.moddev.mc.ironagefurniture`. Add-ons compiled against the former
`com.mcmoddev.ironagefurniture` packages must update their imports. The Maven
coordinate is
`zone.moddev.mc:iron-age-furniture:0.4.1.117011`.

See [CHANGELOG.md](CHANGELOG.md) for the release notes and
[docs/VERSIONS.md](docs/VERSIONS.md) for the versioning scheme. Bugs can be
reported through the
[MMD issue tracker](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/issues).

## Building

Use the checked-in Gradle wrapper with a Java 17 runtime and a Temurin Java 16
compiler toolchain. The compiler is pinned to Temurin 16.0.2+7 while Buildship runs Gradle on
Java 17 or newer. ForgeGradle preparation uses Java 25.0.3+9.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

In Eclipse, add the Temurin 16 JDK under **Java > Installed JREs** and name it
`JavaSE-16`. The game launches select that installation explicitly. Eclipse
itself and Gradle can use newer Java versions; Minecraft 1.17.1 must use Java 16.

`runClient` uses a core-only folder; `runClientCompatibility` uses the separate
optional-mod folder, preserving existing development saves. Restart the client
after changing classes or resources.

Release publication is initiated manually from the protected default-branch
dispatcher after the exact target commit has passed hosted CI. Building
locally does not tag or publish a release.

IronAgeFurniture is licensed under LGPL-2.1.
