# IronAgeFurniture 0.3.0.118021

Phase 3 lighting release for Minecraft 1.18.2 and Forge 40.3.0.

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
  retained IE texture paths against the published 1.18.2 jar.
- Match the published BYG registry by excluding its retired glacial-oak and
  ironwood families, and use its actual bulbis-stem and embur-pedu tags.

## Legacy world upgrades

- Add a pre-flattening migration path for supported IronAgeFurniture content
  from Forge 1.10.2 and 1.12.2 worlds.
- Preserve furniture facing, connected-bench shape, separate light states,
  and all sixteen padded-bench colours. The earlier colourless format becomes
  red, including items in player storage, nested containers, and item entities.
- Rename legacy `big_oak` furniture to `dark_oak`.
- Import the 1.12.2 Oh The Biomes Add-On namespace into the embedded BYG
  catalog, retaining surviving woods and applying documented visual fallbacks
  for Frozen Oak, Great Oak, Hawthorn, Ironwood, Palm, and Rowan.

## Compatibility and tooling

- Move the supported Java API namespace to
  `zone.moddev.mc.ironagefurniture`; the Maven coordinate is
  `zone.moddev.mc:iron-age-furniture:0.3.0.118021`.
- Preserve the `ironagefurniture` mod ID and all surviving registry, resource,
  configuration, entity, NBT, and saved-world identities.
- Modernize the build to ForgeGradle 7.0.34, Gradle 9.6.1, and pinned Java
  toolchains while allowing Eclipse to use a newer Temurin 17 patch.
- Add a deterministic furniture catalog, conditional-data validation, exact
  optional-mod SHA-256 manifests, and positive packaged-server probes.
- Resolve optional furniture from the registered catalog instead of declaring
  absent integration blocks as object holders, reducing harmless startup-log
  noise when optional mods are not installed.
- Produce deterministic main, sources, and Javadoc jars with SHA-256 checksums
  and reproducible Eclipse/Buildship metadata.

Publication remains gated by MMD's protected release workflow after manual
acceptance and hosted CI.
