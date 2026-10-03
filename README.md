[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge](https://img.shields.io/badge/CurseForge-Iron%20Age%20Furniture-orange.svg)](https://www.curseforge.com/minecraft/mc-mods/iron-age-furniture)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml/badge.svg?branch=master-1.14)](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/actions/workflows/ci.yml?query=branch%3Amaster-1.14)

# IronAgeFurniture

IronAgeFurniture adds chairs, stools, benches, lamps, sconces, and other ye
olde-style furniture to Minecraft. Seating is functional, furniture supports
the appropriate vanilla and optional-mod materials, and lighting includes
falling and throwable lava lamps.

This branch is the in-development Phase 4 port, `0.4.0.114041`, for Minecraft
1.14.4 and Forge 28.2.26. It requires Java 8 on clients and dedicated servers.
The Phase 4 furniture and migration work is not yet complete; the latest stable
1.14.4 release remains `0.3.0.114041`. Use a disposable test world with this branch.

## Removable chair shields

Sneak-right-click a shield chair with an empty main hand to take its shield off.
Right-click the empty frame with a shield to fit it again. Damage, banner designs,
enchantments, custom names and other item data stay with the shield. Breaking the
chair returns the empty frame and its shield separately; pick block copies the
fitted shield as part of the chair item. Existing chairs keep their plain shield.

## Beds and tall chairs

Wooden beds, canopy beds, wingback chairs and thrones come in all sixteen wool
colours. Craft a vanilla bed with one matching plank to make a wooden bed of the
same colour. Add another matching plank to turn it into a canopy bed, or combine
two matching single beds to make a double. A carpet deliberately recolours an
IAF bed. Different colours cannot be combined into one double bed.

To make a wingback chair, put a carpet above a matching plank and a classic chair
in a vertical crafting column. Use a wingback chair in the same arrangement to
make a throne. These tall chairs and beds are single pieces of furniture:
breaking any part removes the whole structure and returns one correctly coloured
item. Wooden beds burn like the wooden seating.

## Candles

Smelt cooked meat or rotten flesh into tallow, then craft one tallow with one
string to make eight candles. Cooked pork gives three tallow; beef and mutton
give two; chicken, rabbit and rotten flesh give one.

Candles can stand on a suitable surface or attach to a wall. Water extinguishes
them, including when placed underwater. Drain the water and use a torch or flint
and steel to relight them. An empty sconce holds up to four candles; use an empty
hand to remove them, or add a candle to a full sconce to take them all back.
Adding a second torch to a lit torch sconce makes a brighter twin-torch sconce.
These sconces also extinguish underwater and retain their contents until removed.
Their small flame and occasional smoke are cosmetic;
carrying a candle does not alter world lighting without a separate dynamic-light
feature such as OptiFine's Dynamic Lights option.

## Recovering lava lamps

Mining a lava-filled sconce with a suitable pick returns its empty sconce in Survival. A Silk
Touch tool also returns the intact lamp. Without Silk Touch the lamp breaks and
starts a fire, whether the sconce was standing on the floor or mounted on a wall.
Creative-mode breaking produces neither drops nor fire.

## Optional integrations

IronAgeFurniture adds matching furniture and recipes when these mods are
installed:

- Biomes O' Plenty
- Immersive Engineering
- Minecraft Mineralogy (rock-salt lamps in sconces)

They are optional. Their recipes and recipe advancements are conditionally
loaded, so a normal installation does not need any of them.
With Mineralogy installed, use its rock-salt lamp on an empty sconce to fit it.
The lamp stays lit underwater and can be removed intact with an empty hand.

## Compatibility

The persistent mod ID, registry names, resource paths, configuration names,
entities, and saved-world identity remain under `ironagefurniture`. Existing
1.14.4 worlds therefore retain the same runtime identities.

This release can also upgrade supported IronAgeFurniture blocks and items from
Forge 1.10.2 and 1.12.2 worlds across Minecraft's 1.13 flattening. Chairs,
stools, connected benches, and Phase 3 lights retain their facing and block
state. Padded benches from the original red-only releases become red, while
the later 16-colour padded benches retain the colour stored in their item or
block data. This includes player and Ender Chest inventories, nested containers,
and dropped items. The old `big_oak` furniture names are migrated to the
equivalent 1.14.4 `dark_oak` names.

Make a backup before upgrading a world. Only furniture whose wood is supported
by this 1.14.4 release can be migrated. Natura, Forestry, and retired Biomes
O' Plenty woods have no matching 1.14.4 furniture and should be removed from
the old world before upgrading.

The supported Java source namespace is now
`zone.moddev.mc.ironagefurniture`. Add-ons compiled against the former
`com.mcmoddev.ironagefurniture` packages must update their imports. The Maven
coordinate is
`zone.moddev.mc:iron-age-furniture:0.4.0.114041`.

See [CHANGELOG.md](CHANGELOG.md) for the release notes and
[docs/VERSIONS.md](docs/VERSIONS.md) for the versioning scheme. Bugs can be
reported through the
[MMD issue tracker](https://github.com/MinecraftModDevelopmentMods/IronAgeFurniture/issues).

## Building

Use the checked-in Gradle wrapper with a Java 17 runtime and a Temurin Java 8
compiler toolchain. CI pins the compiler to 8.0.502+7; Eclipse may use a newer
Temurin Java 8 patch release for the project JRE while Buildship runs Gradle on
Java 17. ForgeGradle prepares the Forge 28 dependency from the checked-in
wrapper configuration without downloading a separate build tool.

```text
gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyReleaseChecksums
```

Generate reproducible Eclipse/Buildship metadata with:

```text
gradlew.bat prepareEclipse verifyEclipseProductionClasspath
```

The longer verification, Eclipse and publication tasks are grouped in smaller
Gradle scripts. [gradle/README.md](gradle/README.md) explains where each part of
the build lives.

Release publication is initiated manually from the protected default-branch
dispatcher after the exact target commit has passed hosted CI. Building
locally does not tag or publish a release.

IronAgeFurniture is licensed under LGPL-2.1.
