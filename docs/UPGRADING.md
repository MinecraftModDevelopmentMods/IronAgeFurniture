# Upgrading to Phase 4 on Minecraft 1.15.2

Back up the whole world before upgrading, including `level.dat`, player data,
regions and the `data` folder. Keep the backup outside the installation you
will launch. Never reopen the upgraded world in an older Minecraft version.

Use Forge 31.2.57 and Java 8, and replace optional mods with their compatible
1.15.2 editions. An IAF furniture upgrade does not make an old modpack compatible
with a new Minecraft version.

## What is preserved

The upgrade bridge keeps supported furniture identities, facing, connected
bench states, bed and tall-chair parts, lamp states, stack counts and unrelated
item data. It handles player inventories, Ender Chests, ordinary and nested
containers, dropped stacks and the embedded single-player inventory.

Legacy `Color` and `Metal` values select the corresponding flattened items.
Missing or invalid colour becomes red; missing metal becomes iron. Known Base
Metals identities remain saved when their provider is absent, even though those
variants are hidden from Creative and cannot be newly crafted. Reinstalling a
compatible provider restores their material appearance. Stored chair shields
keep their complete item data, not just their banner pattern.

Learned legacy furniture recipe names are updated too. Manual bed recolouring
still works, but both new and previously learned recolouring entries are kept
out of the recipe book.

Conversion happens as data is loaded. The world sidecar records the old
registry information needed by later chunks; keep it with the save. A second
load should not repeat conversions or migration warnings.

## The 1.12 Oh The Biomes add-on

This edition has embedded BYG support. Furniture from the released Phase 4
1.12 add-on is imported from `iafbygaddon` into its matching `ironagefurniture`
identity. Twenty-one BYG woods retain their own wood family. Six retired woods
use these visual substitutes without changing the furniture form or colour:

| Old wood | Replacement |
| --- | --- |
| Frozen Oak | BYG Aspen |
| Great Oak | Vanilla Oak |
| Hawthorn | BYG Cherry |
| Ironwood | BYG Ebony |
| Palm | BYG Baobab |
| Rowan | BYG Maple |

Install the compatible BYG edition before opening a world with BYG furniture.
The 1.12 add-on jar itself must not be installed on Minecraft 1.15.2.

## Crayfish chair conversion

When both mods are installed, CFM conversion recipes are available but existing
CFM chairs stay unchanged by default. The `forceCfmChairConversion` option is
an explicit, default-off choice to replace supported wooden chairs throughout
loaded data. Try it on a backup first.

Recovery also handles the supported oak, spruce, birch, jungle, acacia and dark
oak chairs when CFM has been removed. This is not a converter for every CFM
block or item. Forge may still report missing mappings for unsupported CFM
content; do not accept a removal prompt on a valuable world without checking
what would be lost.

## Local verification for this candidate

Disposable, fingerprinted copies were loaded directly into 1.15.2 and then
loaded again. No intermediate Minecraft edition was needed for the tested
furniture fixtures:

- 1.10.2 and 1.12.2 Phase 2 red-only, Phase 3 multicolour and Phase 4 saves.
- Released 1.14.4 Phase 3 and Phase 4 saves, and an older 1.15.2 save.
- The released 1.12 Phase 4 BYG add-on, including all 27 woods, both bed sizes,
  canopy beds, wingbacks, thrones, all colours, stored shields and nested items.
- Legacy metals with Base Metals both absent and installed; supported CFM
  chairs with default-off, forced and removed-mod conversion.
- A furniture-only disposable extraction from the Sylvester save. Its original
  world was left untouched. This does not qualify the entire old modpack.

Packaged clients and servers were checked core-only, with each available
integration separately, with the full stack, and with integrations disabled.
Checks include actual recipe-book autofill and connected-player discovery,
all chair facings, hand/tool harvesting, support loss, waterlogging and
redstone, Silk Touch, multiblock fire removal, Creative behaviour and reloads.

The optional-mod stack used for these checks was:

| Mod | Version |
| --- | --- |
| Biomes O' Plenty | 10.0.0.366 |
| Oh The Biomes You'll Go | 1.0.11 |
| Immersive Engineering | 4.1.1-125 |
| Base Metals | 3.0.1.115021 |
| OreSpawn | 4.1.0.115021 |
| Mineralogy | 6.0.1.115021 |
| MrCrayfish's Furniture Mod | 7.0.0-pre16 |

Two external-mod limitations are worth noting:

- The 1.14-to-1.15 full-stack fixture reports BOP mappings removed upstream,
  including ethereal wood content and retired biomes. The IAF furniture checks
  pass, but this does not promise preservation of those removed BOP objects.
- Immersive Engineering 4.1.1-125 reports a missing `revolver_einhorn.png`
  vanity texture in the client log. IAF's treated-wood models pass their checks;
  this error is not an IAF furniture texture.

## Before using your main world

Restart the client after replacing the jar or updating Eclipse output. On a
disposable copy, check a few familiar rooms and stored items first:

- Craft matching-colour wooden, canopy and double beds, then wingbacks and
  thrones. Check normal and shift-click discovery while the table stays open.
- Place tall chairs facing you, and check all four creative tabs.
- Mine a coloured bench or bed and a non-iron sconce. Check the returned colour,
  metal and contents, including support removal and underwater twin torches.
- Remove and reattach a decorated shield. Check its name, damage and pattern.
- Check ordinary lava-lamp shattering separately from Silk Touch recovery;
  shattering and the redstone release trap can intentionally start fires.
- Save, close and reopen. Check furniture parts, colours, metals and inventories
  again, and review any missing-mod prompt before proceeding.
