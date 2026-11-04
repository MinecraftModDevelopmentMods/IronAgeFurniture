# Testing Phase 4 on Minecraft 1.17.1

Restart the client after rebuilding or refreshing Eclipse output. Use Java 16
for the game; Buildship runs Gradle on Java 17.

`runClient` is core-only and uses `run-core`. `runClientCompatibility` uses
`run-phase4-compat`, with its separate optional-mod installation and saves. Test only copied worlds when upgrading.

The compatibility stack used for automated tests is:

- Biomes O' Plenty 14.0.0.43 for 1.17.1.
- Base Metals 3.1.0.117011 and OreSpawn 4.1.0.117011.
- Mineralogy 6.1.4.117011.
- MrCrayfish's Furniture Mod 7.0.0-pre25 for 1.17.1.

Their published downloads and checksums are pinned in the build's integration
manifests. Eclipse uses development-only remapped copies; packaged-game tests
use the original downloaded jars.

## Furniture and recipes

- Check all four creative tabs, including stools under Chairs and crafting
  materials under Lights. Optional furniture should disappear when disabled.
- Make a coloured wooden bed, add matching wood and carpet for a canopy, then
  combine matching singles into doubles. Check colour, facing, sleeping,
  breaking either half, drops and reopening the world.
- Craft a wingback and throne with matching wood and carpet. Check immediate
  recipe discovery while the table stays open, including shift-click crafting.
  Carpet recolouring should work manually without cluttering the recipe book.
- Attach a blue or patterned shield, save and reopen the world, then check
  that the chair shows the same design. Remove the shield and check its name,
  damage and enchantments.

## Lights and sconces

- Mine empty and filled floor/wall sconces with hands and tools. Break their
  supports too. Check frame metal, contents and no duplicate Creative drops.
- Try single/twin torches and one to four candles in every facing. Water
  buckets should extinguish them without spilling water. Power wet sconces,
  remove power and confirm they go out again.
- Mine floor lamps by hand. Check selection/hit particles and the short break
  time. Mine wall lava sconces normally and with Silk Touch: ordinary mining
  releases a falling, shattering lamp; Silk Touch returns an intact lamp.
  Right-click removal should stay safe.
- Compare iron, gold and Base Metals frame textures and mining toughness.
  Include a gold frame and a hard metal such as adamantine or star steel.
- Check rock-salt lamps with Mineralogy and reopen a world with mixed-colour
  candle sconces. Held lights should have no flame or smoke effects.

Legacy IAF candles keep their saved block identities. New multi-candle sconces
hold vanilla candles.

See [UPGRADING.md](UPGRADING.md) for the tested legacy paths, excluded wood
families and vanilla structure warnings.
