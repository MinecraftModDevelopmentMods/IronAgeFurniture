# Upgrading worlds to Minecraft 1.17.1

Keep a backup before upgrading Minecraft or Iron Age Furniture. Test the copied
world first and retain the old mod installation until the result is accepted.
Do not reopen a world saved by 1.17.1 in an older Minecraft version.

The upgrade bridge preserves supported furniture identities, facing and structure
parts, upholstery colours, sconce metals and stored shield data. It also handles
items in player storage, Ender Chests, containers, nested inventories and drops.
Old colourless upholstery defaults to red; sconces without metal data default
to iron. Importing the same saved data again must not repeat a conversion.

This target retains its 19 vanilla/Biomes O' Plenty woods. BYG, Immersive
Engineering and the separate 1.12 Oh The Biomes furniture catalog are outside
its supported scope. Do not assume that an entire older modpack can run here
just because its supported IAF furniture can be upgraded.

## Tested upgrade paths

Disposable copies were checked on their first load and after saving and
reopening. The original worlds were fingerprinted and left unchanged.

| Source | Furniture checks per load |
| --- | ---: |
| 1.10.2 Phase 2, red-only benches | 26 |
| 1.10.2 Phase 3, multicolour benches and lights | 104 |
| 1.12.2 Phase 2 | 4 |
| 1.12.2 Phase 3 | 92 |
| 1.10.2 Phase 4 | 61 |
| 1.12.2 Phase 4 | 114 |
| Released 1.14.4 Phase 3 and Phase 4 | 104 and 114 |
| Released 1.15.2 Phase 3 and Phase 4 | 25 and 246 |
| Released 1.16.5 Phase 3 and final Phase 4 game code | 31 and 2,468 |
| An existing 1.17.1 world saved by 0.3.0.117011 | 24 |
| Furniture extracted from the current Sylvester world | 444 |

These furniture fixtures loaded directly in 1.17.1; no intermediate Minecraft
version was needed. Checks include facing, connected states, structure parts,
colours, metals, stored shields and items in the applicable inventories,
containers, nested storage and dropped stacks. Reopening did not repeat the
conversions or change the legacy-registry sidecar.

The 1.15 Phase 4 fixture also contains 22 placed BYG/Immersive Engineering
chairs outside this edition's catalog. They are excluded from the 246 supported
checks, not treated as successful imports. Those woods and the separate 1.12
BYG add-on need their own compatible implementation before an entire world
using them can be carried forward safely.

Sylvester was tested as a furniture-only extraction: unsupported mod blocks
were replaced in the disposable copy and IAF items were retained from old
containers. This does not establish compatibility with the whole old modpack.

## Warnings from older worlds

Forge can report old vanilla registry names, removed optional mods and retired
third-party entries during a version change. These reports were reviewed
separately from furniture preservation; they are not a promise that other mods'
content can be converted.

The older 1.16 Phase 3 fixture also produces 24 vanilla village/tree structure
errors. A Forge-only control without IAF produced exactly the same messages.
Its furniture passed both loads, but this is not an error-free whole-world
upgrade: keep the backup and inspect affected villages in a copied world.

One older 1.12 CFM fixture reports a missing spawner entity after reopening.
The same position and message were reproduced without IAF. Its converted chairs
passed both loads; the unrelated spawner warning should still be investigated
before keeping an upgraded copy of that world.

## Crayfish furniture

Forced conversion remains off by default when CFM is installed. Enabling it
converts supported wooden chairs, not every kind of Crayfish furniture. Removed-
mod recovery uses the same chair mappings. Forge still reports the removed CFM
namespace in its old registry snapshot; check the converted furniture before
accepting that warning on a world you intend to keep.

The packaged conversion tests check placed chairs in every facing, direct and
nested stacks, player and Ender Chest storage and dropped chairs. They preserve
counts, names, enchantments and other item data. In 1.17, dropped stacks are
migrated through the separate entity-region reader before item decoding.
Default-off, forced and removed-mod cases each pass 88 checks and a second load
without repeated conversion.

Separate 1.10 and 1.12 CFM fixtures each passed eight legacy chair checks on both
loads, including their saved item data.

## Before using a regular world

Keep both the original backup and a copy saved by the old mod. Try the
[manual-test checklist](MANUAL_TESTING.md) with core-only and optional-mod
profiles, then inspect familiar furniture in a copied world. IAF candles are
retained in this build; replacing them with vanilla candles is a separate change.
