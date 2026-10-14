# Upgrading to Phase 4 on Minecraft 1.16.5

Back up the whole world, including `level.dat`, player data, regions and the
`data` folder. Keep that backup outside the installation you will launch.
Never reopen an upgraded world in an older Minecraft version.

Use Forge 36.2.34 and Java 8. Replace optional mods with compatible 1.16.5
editions; an IAF furniture upgrade cannot make an old modpack compatible.

## Furniture and saved items

The bridge imports supported furniture from older IAF releases, preserving
facing, connected benches, bed and tall-chair parts, upholstery, sconce metal
and complete stored shields. Player and Ender Chest inventories, ordinary and
nested containers, dropped items and embedded single-player data are included.

Missing or invalid colour becomes red; missing metal becomes iron. Known
Base Metals identities stay saved while their provider is absent. Those
variants hide from Creative and crafting until a compatible provider returns.
Keep the migration sidecar with the save: later chunks may still need its old
numeric registry mapping. A second load should not repeat conversion.

## The 1.12 Oh The Biomes add-on

This version has embedded BYG support. Furniture from `iafbygaddon` becomes
its matching `ironagefurniture` entry. Twenty-two surviving woods keep their
family, including Palm. The five retired woods use these visual substitutes:

| Old wood | Replacement |
| --- | --- |
| Frozen Oak | BYG Aspen |
| Great Oak | Vanilla Oak |
| Hawthorn | BYG Cherry |
| Ironwood | BYG Ebony |
| Rowan | BYG Maple |

Install compatible BYG before opening a world containing its furniture. Do not
install the old 1.12 add-on jar in a 1.16.5 profile.

## CFM conversion

When both mods are installed, conversion recipes are available but placed CFM
chairs stay unchanged by default. `forceCfmChairConversion` opts into replacing
supported wooden chairs throughout loaded data. Test it on a backup first.

Removed-mod recovery covers oak, spruce, birch, jungle, acacia, dark oak,
crimson and warped chairs. It does not convert stone or stripped-wood chairs,
or other CFM furniture. Check any missing-mapping prompt before accepting it.

## Tested upgrade routes and limits

Disposable worlds from 1.10 and 1.12 Phase 2, 3 and 4, released 1.14 Phase 3
and 4, released 1.15 Phase 4, and the previous 1.16 Phase 3 release have been
checked through Minecraft's normal loading path. The released 1.12 BYG add-on
fixture also covers its surviving and retired woods. These furniture fixtures
load directly in 1.16.5; no intermediate Minecraft version was needed.

The checks compare furniture identities, facing, connected states, colours,
metals, structure parts, shields and saved stacks against the source, then
repeat after saving and reopening. They include player and Ender Chest data,
ordinary and nested containers, and dropped items. A furniture-only extraction
from an older modpack provides an additional regression; this is not a test
of converting the whole modpack.

One older 1.15 Phase 3 world keeps its furniture but logs vanilla village-piece
errors for obsolete tree and flower configuration data. The same errors occur
without IAF, including on the second load. This world is not an error-free
whole-world upgrade, and IAF does not rewrite its vanilla village structures.
Older BOP, BYG and CFM releases also have retired non-IAF entries; review their
missing-mapping prompts rather than assuming IAF converts those mods' content.

The pinned IE release has a missing optional revolver-skin texture, and CFM
logs warnings for its own recipe-book categories. Their isolated profiles
reproduce these messages; neither indicates missing IAF models or recipes.

## Manual checks before using your main world

Restart the client after replacing the jar or synchronising Eclipse output.
On a disposable world, check familiar rooms and stored items first:

- Craft coloured wooden, canopy and double beds, then wingbacks and thrones.
  Check recipe-book autofill and normal/shift-click discovery with the table open.
- Mine coloured benches and beds, then iron, gold and Base Metals sconces by
  hand and with tools. Remove their supports too, including underwater cases.
- Power wet candle and twin-torch sconces, remove the signal and repeat.
  Bucket clicks should extinguish them without pouring water or losing contents.
- Test ordinary lava shattering separately from Silk Touch and safe right-click
  removal. Wall-sconce lamps should fall before shattering; this can start fires.
- Detach and reattach a decorated shield, and inspect all four creative tabs.
- Check soul-torch sconces and crimson/warped fire resistance, then save,
  close and reopen to check parts, colours, metals and stored items again.
