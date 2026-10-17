# Upgrading to Forge 26.3 Phase 4

Keep the original world and its mod installation. Test the upgrade on a copy, and never reopen a world saved by 26.3 in an older Minecraft version.

Version `0.4.0.2603001` brings the NeoForge Phase 4 furniture and lighting changes to Forge. Use Forge jars throughout the installation; a matching Minecraft version does not make a NeoForge dependency usable on Forge.

## Furniture and stored items

The upgrade bridge handles supported placed furniture, multiblock parts, upholstery, sconce metals and stored shields. It also visits player and Ender Chest storage, containers, nested inventories and dropped stacks before obsolete IDs and subtype data can be discarded.

Missing old colour information defaults to red; missing metal information defaults to iron. Known Base Metals materials retain their identity when the provider is absent. Reopening an upgraded world must not repeat the conversion.

Sconce frame materials now have separate placed-block IDs. Existing frame item IDs and the original iron block IDs are retained. Legacy subtype numbers, `Metal` data and shared-block `metal` properties are converted while preserving the installed light, facing and water state.

## Candles

- Old floor candles become uncoloured vanilla candles, preserving their count and applicable lighting and water state.
- Old wall candles become iron candle sconces in place, preserving their facing. Migration deliberately supplies a frame.
- Old candle-sconce count variants become one to four stored vanilla candles, preserving their frame material and lighting.
- Mixed-colour sconces save the original candle stacks and their complete components.

The old IAF candle recipes and creative entries are hidden. Standalone candles use vanilla behaviour. A sconce holds up to four candles: insert with a candle in hand and remove newest-first with an empty hand. Breaking it or removing its support releases the frame and stored contents.

## Optional mods

Install the matching Forge BOP add-on before opening a world containing its furniture. Its fourteen supported wood families and documented retired-wood substitutions remain separate from the core.

This target does not provide old BYG or Immersive Engineering furniture. Keep the original installation for worlds containing unsupported furniture or other missing third-party content. The supported legacy CFM chair conversion is not a whole-modpack converter and does not add Refurbished Furniture support. Forced conversion while CFM is installed remains off by default.

## Tested upgrades

The Forge port reads older worlds' registry records before Minecraft updates their level data. This preserves numeric furniture identities from 1.10 and 1.12. Known old furniture names in crafting and mining statistics are also converted, combining counts where necessary.

A world first played with the core alone can later load with the BOP add-on. Its saved fallback names are reconciled with the newly installed furniture, including when you leave and reopen the world in the same client session.

Packaged Forge clients and servers are tested separately from the NeoForge reference. The disposable upgrade fixtures include 1.10 and 1.12 Phase 2–4 furniture, later released worlds, the current 1.17 candidate, existing 26.3 furniture and the supported Sylvester furniture subset. First and second loads are compared, and original world fingerprints are checked. A separate NeoForge-to-Forge fixture covers 5,104 saved furniture states and stored contents. These are furniture checks, not a promise that an entire old modpack can be upgraded.

This release was tested on Forge `66.0.2` and `66.0.13`, with stable BOP `26.3.0.0.7` and newer BOP `26.3.0.0.15`. The BOP tests used GlitchCore `26.3.0.0.5` and TerraBlender `26.3.0.0.9`. Base Metals and legacy CFM were checked as absent providers only; this is not a claim of live compatibility with an unpublished 26.3 version of either mod.

Always keep the backup and any existing migration sidecar until your own copied world passes the [manual checklist](../MANUAL_TESTING.md).
