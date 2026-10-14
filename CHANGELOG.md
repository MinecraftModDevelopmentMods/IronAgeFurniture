# IronAgeFurniture 0.4.0.116051

Phase 4 furniture and lighting for Minecraft 1.16.5 and Forge 36.2.34.
Requires Java 8 on clients and servers.

- Add wooden and canopy beds, matching doubles, wingbacks and thrones across
  all 47 existing wood families, with sixteen colour-specific items per form.
- Keep crimson and warped furniture fireproof, and retain soul-torch sconces.
- Start soul-torch sconces dry, preserving their iron default and blue flame.
- Preserve the complete shield item when detaching, reattaching, mining or
  dropping a shield chair, including designs, damage, enchantments and names.
- Add carried candle effects, standalone candles, twin-torch and multi-candle
  sconces, Mineralogy rock-salt sconces, and gold and Base Metals frames.
- Keep frame metals and contents through hand/tool mining and support removal.
  Silk Touch recovers intact lava lamps; mining wall sconces releases their
  lamps to fall and shatter. Right-click removal remains safe.
- Keep lava-lamp mining prediction free of fire blocks, preventing the empty
  particle-shape crash. Glass lamps break as quickly as glass, not metal frames.
- Let water buckets extinguish candles without placing water or consuming the
  bucket. Powered wet sconces relight; they go out again when power stops.
- Apply single-torch redstone relighting on the server, not in client particle
  updates, so its saved light state agrees with candles and twin torches.
- Separate Chairs, Benches, Beds and Lights tabs. Keep manual bed recolouring
  out of the recipe book, including recolouring learned in earlier releases.
- Use matching-colour bed upgrades and shapeless wingback/throne recipes.
  Reveal matching upgrades while the crafting table is still open.
- Make all frame recipes return four sconces from five matching nuggets.
- Load Base Metals recipes before 1.16's tag synchronisation, then require
  live ingredients at crafting time. Empty or disabled tags cannot make frames.
- Read the world's Base Metals recipe option before datapacks load, without
  rewriting the config, so disabled integrations stay out of the recipe catalog.
- Add seventeen locales with English fallbacks and smaller build scripts
  explaining the game, development, verification and publication setup.
- Extend legacy imports to Phase 4 beds, chairs, shields and metals, including
  the released 1.12 Oh The Biomes add-on. Read both pre-1.16 and native chunk
  palettes without losing upholstery colours.
- Capture old numeric registries through Forge 36's world reader before
  Minecraft flattens the chunks, preserving direct 1.10 and 1.12 imports.
- Convert old padded benches even when a red-only save has no colour tile;
  preserve saved colours, facing and connected-back shapes when tiles exist.
- Preserve old container chunks holding unchanged furniture item IDs too,
  preventing stored items from being lost during Minecraft's chunk upgrade.
- Keep inventories in the oldest vanilla chests when another mod renames
  their tile IDs before Minecraft's legacy datafixer runs.
- Reuse bed and tall-chair collision shapes during world entry, reducing
  the repeated geometry work without changing their shapes or facing.
- Add eight optional CFM chair recipes, including crimson and warped. Forced
  world conversion remains off by default; removed-mod recovery stays limited
  to supported wooden chairs.
- Separate core-only and optional-mod development launches while preserving
  existing test saves and verified original dependency downloads.

## Previous release: 0.3.0.116051

Phase 3 lighting release for Minecraft 1.16.5 and Forge 36.2.34.

## Player-visible changes

- Add the complete Phase 3 lighting set: empty, torch, unlit-torch,
  redstone-torch, glow, lava, and redstone lamps and sconces, obsidian chunks,
  hidden redstone-light states, and soul-torch sconces.
- Restore lava-lamp throwing. Thrown and falling lamps shatter with a glass
  sound, ignite dry landing positions, damage struck entities, and become a
  waterlogged obsidian chunk on contact with water.
- Fix waterlogging lava-filled floor and wall sconces so they become matching
  waterlogged empty sconces, preserve their supports, and drop one obsidian chunk.
- Let placed lava lamps melt ordinary ice directly beneath them. Packed ice
  and blue ice are intentionally unaffected.
- Correct falling and underwater lamp placement, survival and Creative drops,
  and redstone-light transitions.
- Give ordinary wooden chairs and benches normal wood flammability while
  keeping crimson and warped furniture fireproof.
- Correct every recipe advancement to use its own ingredient and matching
  recipe-unlocked criterion. Common oak planks no longer unlock unrelated
  lighting or optional-mod recipes.
- Keep each wood and furniture form as its own recipe-book entry instead of
  cycling unrelated furniture through one shared recipe cell.
- Synchronise and render thrown lava lamps visibly in flight, show the
  obsidian chunk at a useful angle in inventories, and allow wall sconces to
  use narrow centre supports such as fences and walls.
- Load Biomes O' Plenty, Immersive Engineering, and Oh The Biomes You'll Go
  recipes and advancements only when their mod is installed. Missing optional
  mods no longer produce ignored-advancement or missing-recipe log spam.
- Remove stale Immersive Engineering recipes and the unreachable log-bench
  advancement for furniture IDs that were never registered, and correct the
  retained IE texture paths against the published 1.16.5 jar.
- Match the published BYG registry by excluding its retired glacial-oak and
  ironwood families, and use its actual bulbis-stem and embur-pedu tags.

## Legacy world upgrades

- Add a pre-flattening migration path for supported IronAgeFurniture content
  from Forge 1.10.2 and 1.12.2 worlds.
- Preserve furniture facing, connected-bench shape, separate light states,
  and all sixteen padded-bench colours. The earlier colourless format becomes
  red, including items in player storage, nested containers, and item entities.
- Rename legacy `big_oak` furniture to `dark_oak`.
- Import furniture from the 1.12.2 Iron Age Furniture Oh The Biomes Add-On.
  Surviving woods keep their names; Frozen Oak maps to Aspen, Great Oak to
  vanilla Oak, Hawthorn to Cherry, Ironwood to Ebony, and Rowan to Maple as
  visual fallbacks for woods retired before BYG 1.16.5. Palm remains Palm.

## Compatibility and tooling

- Move the supported Java API namespace to
  `zone.moddev.mc.ironagefurniture`; the Maven coordinate is
  `zone.moddev.mc:iron-age-furniture:0.3.0.116051`.
- Preserve the `ironagefurniture` mod ID and all surviving registry, resource,
  configuration, entity, NBT, and saved-world identities.
- Modernize the build to ForgeGradle 7.0.34, Gradle 9.6.1, a pinned Temurin
  Java 8 compiler, and Java 17 for Gradle while allowing Eclipse to use a newer
  Temurin Java 8 patch.
- Add a deterministic furniture catalog, conditional-data validation, exact
  optional-mod SHA-256 manifests, and positive packaged-server probes.
- Resolve optional furniture from the registered catalog instead of declaring
  absent integration blocks as object holders, reducing harmless startup-log
  noise when optional mods are not installed.
- Produce deterministic main, sources, and Javadoc jars with SHA-256 checksums
  and reproducible Eclipse/Buildship metadata.

Publication remains gated by MMD's protected release workflow after manual
acceptance and hosted CI.
