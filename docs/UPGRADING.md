# Upgrading a world to the 1.14.4 Phase 4 candidate

Make a backup first and test a separate copy. Minecraft's flattening changes
how blocks and items are stored; after saving a world in 1.14.4, do not reopen
that copy in 1.10.2 or 1.12.2.

## What Iron Age Furniture preserves

Supported chairs, stools, benches and lights retain their registry identities,
facing and connected states. The old `big_oak` name becomes `dark_oak` for both
placed furniture and stored items. Original padded benches with no colour data
remain red; later multicolour benches retain their saved `Color` value.

Phase 4 beds and tall chairs retain their colour and structure parts. Sconce
metals move from legacy item metadata and tile data into the modern `metal`
block property and `Metal` item field. A missing colour means red; a missing
metal means iron. Known Base Metals names survive even when the supplying mod
is absent, although their appearance falls back to iron.

The upgrade also handles player and Ender Chest inventories, ordinary and
nested containers, dropped items and stored chair shields. Minecraft's own
item data fixer upgrades a shield's old durability and banner format; IAF
keeps the complete shield stack rather than rebuilding it from selected fields.

The world keeps a small `ironagefurniture_legacy_registry.dat` sidecar for
chunks that have not yet been loaded. Keep this file with the world and include
it in backups. The upgrade is designed to be repeatable: converted furniture
does not convert again on later loads.

## Tested upgrade paths

The local saved-world checks use disposable, hash-checked copies and load each
successful copy twice. No intermediate Minecraft version was needed for these
supported furniture fixtures.

| Source | Coverage |
| --- | --- |
| 1.10.2 Phase 2 | Red-only benches, ordinary seating, old wood names and stored items. |
| 1.10.2 Phase 3 | All sixteen padded colours, both bench forms, facing and connected states, lights and stored or dropped items. |
| 1.12.2 Phase 2 | Original red-only padded furniture. |
| 1.12.2 Phase 3 | Multicolour benches, connected states, lighting and stored items. |
| 1.10.2 and 1.12.2 Phase 4 | Saved beds, tall chairs, shields, candles, lamps and player items; a separate 1.12 fixture covers all sixteen bed colours. |
| 1.12.2 Phase 4 metal fixture | All 23 metal names across 66 sconce states and four facings, plus player, Ender Chest, container, nested and dropped frame items. |
| 1.14.4 Phase 3 | Existing flattened furniture and inventory data. |
| CFM chair fixtures | 1.10.2 CFM 4.1.2, 1.12.2 CFM 6.3.2 and 1.14.4 CFM 7.0.0-pre15: all six supported woods, facing and stored chair data. |

These checks are not a promise that every old modpack can upgrade intact. The
1.14.4 furniture catalog contains vanilla woods, eleven supported Biomes O'
Plenty woods and Immersive Engineering's treated wood. Natura, Forestry,
retired BOP woods and the 1.12 Oh The Biomes add-on have no equivalent catalog
in this target. Remove or replace unsupported furniture in the old version
before upgrading, or wait for a target that supports it.

## Crayfish Furniture and other old mods

IAF recovers supported wooden CFM chairs when CFM has been removed. When both
mods are installed, existing chairs remain unchanged unless
`forceCfmChairConversion` is enabled. This only converts the six vanilla-wood
chairs, not tables, appliances, crates or every item inside those containers.

A direct test of a large, long-running 1.10.2 modpack world stopped in
Minecraft's chunk loader because a removed CFM crate had the invalid legacy
tile ID `cfmCrate`. That is not a successful whole-world upgrade. For the IAF
regression test, a separate furniture-only copy retained the IAF blocks and
items while removing unsupported mod data; items held by unsupported
containers were moved into test chests. This preparation is test coverage,
not an automatic conversion supplied by IAF. An equivalent real-world upgrade
needs its own backup and a decision about the other mods' content.

If Forge presents a missing-registry prompt, read it carefully. IAF cannot
recover entries outside its documented furniture mappings. Do not accept an
unexpected removal list on your only copy of a world.

## Base Metals compatibility

The 1.14.4 candidate's Base Metals integration is optional and has been tested
with the published Base Metals 3.0.1.114041 and OreSpawn 4.1.0.114041 builds.
An independently saved 1.12 fixture covering all 23 sconce metals, four facings,
66 historical sconce IDs and stored, nested and dropped items passes both its
initial upgrade and a second load with these mods installed. This complements
the absent-mod migration tests; it does not certify upgrades of unrelated
Base Metals machinery or an entire old modpack.
