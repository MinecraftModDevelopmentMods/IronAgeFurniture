# Upgrading worlds to NeoForge 26.3

Keep the original world and its mod installation. Test an upgrade on a disposable copy, and do not reopen a world saved by 26.3 in an older Minecraft version.

The Phase 4 candidate is `0.4.0.2603002`. Its upgrade bridge covers supported placed furniture, structure parts, upholstery, sconce metals, stored shields and saved item stacks. It runs before obsolete item IDs or subtype data can be discarded. Item handling includes player and Ender Chest storage, containers, nested inventories and dropped stacks.

Old colourless furniture defaults to red and sconces without metal data default to iron. Known Base Metals materials remain recoverable while their provider is absent. A repeated load must not repeat a conversion.

## Candles

- Old floor candles become vanilla uncoloured candles, retaining their count and applicable lighting/water state.
- Old wall candles become iron candle sconces in place, preserving their facing. This deliberately supplies a frame.
- Old sconce count variants become one to four stored vanilla candles, with the frame material and light state retained.
- New mixed-colour sconces save the original item stacks and their complete components.

Obsolete IAF candles are not exposed through recipes or the creative inventory.

## Optional content

Install the matching BOP add-on before opening a world that needs its woods. Retired BOP wood mappings remain part of that add-on's compatibility contract. The core alone does not supply missing biome furniture, and this port does not promise conversion of an entire older modpack.

Older BYG and Immersive Engineering furniture is not supplied by this target. In particular, a 1.15 world containing their shield chairs is not a lossless whole-world upgrade candidate. Keep its original installation and do not open your only copy with those providers missing.

Legacy CFM conversion is limited to the supported wooden chairs. Recovery is available after the old mod is removed. Forced conversion while both mods are present remains off by default. This does not add compatibility with Refurbished Furniture.

## Automated verification

Packaged server tests pass on NeoForge `26.3.0.77` and `26.3.0.83`, including the current BOP dependency stack. They cover mixed candle storage, real player interactions, lighting transitions, support-loss drops, native sleeping and respawn, falling shields and migration regressions. The clients check the visible item catalog and shared candle/shield renderers. Two separate clients also receive and render changes made after both have joined a dedicated server.

First and second loads pass for the following disposable furniture fixtures:

| Source | Tested scope |
|---|---|
| 1.10 and 1.12 | Phase 2 red-only, Phase 3 multicolour and Phase 4 furniture |
| 1.14 | Phase 3 and Phase 4 furniture; missing old third-party terrain is not certified |
| 1.15 | Released Phase 3 furniture |
| 1.16 | Released Phase 3 and final Phase 4 furniture |
| 1.17 | Released Phase 3 and the local Phase 4 candidate |
| 26.3 | Existing core/BOP saves and new mixed-candle/shield storage |
| Sylvester | A furniture-only extraction, not the entire old modpack |

The original sources were hash-checked and left unchanged. The full 1.15 Phase 4 source fixture was deliberately not launched: it contains 44 unsupported BYG/Immersive Engineering shield-chair blocks. That is an upgrade limitation, not a passing whole-world result.

Those fixture tests loaded directly into 26.3; they did not need an intermediate Minecraft save. Keep the backup and any existing migration sidecar with a real world, as its other mods and world history may impose different upgrade requirements.

Testing caught and corrected legacy numeric candle lighting, old canopy-part metadata, 1.10 shield wrappers and an early palette conversion that could corrupt dense block-state storage. The palette regression now checks every cell. A separate-process save test also preserves 5,104 placed state/storage records across two restarts, including named mixed-colour candles, shields, metals and multiblock beds.

All 2,496 core/add-on Phase 4 construction recipes pass native recipe-book placement and normal/shift crafting, including immediate matching-upgrade discovery. Obtaining a crafting table does not unlock unrelated recipes, and old learned bed-recolouring entries are removed without removing unrelated recipes.

The fourteen supported legacy CFM chair IDs pass removed-provider, nested-item, count, facing and idempotence contracts, including native player datafixing. The installed-provider conversion gate remains opt-in. There is no live CFM or Base Metals 26.3 compatibility claim.

Both projects pass their build, catalog, resource, locale, metadata, hygiene, workflow, artifact and Eclipse gates. Main, sources and Javadoc hashes match across two clean builds for each project. The add-on pins the exact tested core artifact.

These are local candidates for [hands-on testing](../MANUAL_TESTING.md), not published releases. Keep the source backup until you have checked your own copied world.
