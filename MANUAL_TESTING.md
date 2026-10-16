# Phase 4 manual testing

Use a disposable world or a complete copy of an existing world. Keep the original installation and backup untouched. Do not reopen a world saved by 26.3 in an older Minecraft version.

Test the core alone first, then add the matching BOP add-on and its dependencies. Both versions are `0.4.0.2603002`. The BOP stack is Biomes O' Plenty `26.3.0.0.15`, GlitchCore `26.3.0.0.5` and TerraBlender `26.3.0.0.9`. Java 25 and NeoForge `.77` or `.83` are the tested runtime targets.

Restart the client after refreshing Eclipse output or changing jars. Normal launches must not include the development probe mod.

## Focused checks

- Browse Chairs, Benches, Beds and Lights. Check core and BOP woods, the sixteen upholstery colours, and iron/gold/copper frames. Missing Base Metals variants should not clutter the tabs.
- Craft a coloured wooden bed, canopy, double bed, wingback and throne from the recipe book. Try normal and shift-click crafting. Matching upgrades should appear while the table remains open; obtaining a crafting table must not unlock everything.
- Place beds and chairs in every direction. Sleep in singles and doubles, including canopies, then check the respawn point. Break different structure parts and confirm one correctly coloured item returns.
- Attach and remove a named, damaged and patterned shield. Check that its design faces the right way, fills the original chair back and has no hand grip. Drop or let the chair fall, save and reopen, and confirm its appearance and item data are retained.
- Remove a shield by sneak-right-clicking with an empty hand, including while holding a torch in the other hand. Pick up and replace the empty chair: its inventory model and outline should show only the wooden frame. Reattach the same shield and check all four facings.
- Check that the obsidian chunk is centred and angled in its inventory slot, rather than looking like a flat square.
- Fill floor and wall sconces with one to four different vanilla candles. Check the smaller candles sit in their matching-metal dish, with flame and smoke at the wicks in every facing. The first starts unlit; later additions keep the flame state. A fifth must not be consumed or placed through the holder.
- Retrieve candles newest first, including with a full inventory and in Creative. Break the holder and its support: recover the original candles once, with the correct metal frame in Survival and no duplicate frame in Creative.
- Test buckets, waterlogging and redstone on single torches, twin torches and candle sconces. Underwater power should relight them; removing that power should extinguish them again.
- Mine lamps and sconces with hands, ordinary tools and Silk Touch. Check metal appearance, digging speed, content drops and support loss. Lava-lamp falling, shattering and possible fire are intentional; interacting with the sconce remains the safe way to remove the lamp.
- Break a floor lava sconce without Silk Touch and leave its dropped frame in the fire for several seconds: the frame must survive. Break the support under floor and wall lava sconces and watch the lamps fall without a client crash.
- Mine empty gold and copper wall frames, both dry and waterlogged. Each must return its original metal, not iron.
- Open a copy of an older world with metal sconces, including any earlier Phase 4 test world. Check placed frames and stored items before and after a second load. Gold, copper and known Base Metals materials must not turn into iron; a missing provider may change the fallback appearance, not the saved material.
- Carry each candle colour in first and third person. IAF should add no flame, smoke or dynamic lighting while a candle is held.
- Save and reopen the copied world. Check old furniture, inventories and containers again, and look for repeated migration or missing IAF content in the log.

## Limits to keep in mind

The tested older furniture fixtures can load directly into 26.3, but this does not convert an entire historical modpack. Install the BOP add-on before loading its furniture. Old BYG and Immersive Engineering furniture is not supplied by these candidates. Base Metals and legacy CFM do not have a verified live 26.3 integration; their absence and migration contracts are tested, not a future provider release.

See [the upgrade guide](docs/UPGRADING.md) for the tested migration scope and the documented retired-wood choices.
