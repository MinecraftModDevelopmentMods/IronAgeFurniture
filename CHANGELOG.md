# IronAgeFurniture 0.3.0.115021

Phase 3 lighting release for Minecraft 1.15.2 and Forge 31.2.57.

## Player-visible changes

- Add the complete Phase 3 lighting set: empty, torch, unlit-torch,
  redstone-torch, glow, lava, and redstone lamps and sconces, obsidian chunks,
  and hidden redstone-light states. Soul-torch sconces remain exclusive to
  Minecraft 1.16 and newer, where soul torches exist.
- Restore lava-lamp throwing. Thrown and falling lamps shatter with a glass
  sound, ignite dry landing positions, damage struck entities, and become a
  waterlogged obsidian chunk on contact with water.
- Fix waterlogging lava-filled floor and wall sconces so they become matching
  waterlogged empty sconces, preserve their supports, and drop one obsidian chunk.
- Let placed lava lamps melt ordinary ice directly beneath them. Packed ice
  and blue ice are intentionally unaffected.
- Correct falling and underwater lamp placement, survival and Creative drops,
  and redstone-light transitions.
- Give wooden chairs and benches normal wood flammability.
- Correct every recipe advancement to use Minecraft 1.15's native ingredient
  predicate and its matching recipe-unlocked criterion. Obtaining a crafting
  table or common oak planks no longer unlocks the entire furniture catalog or
  unrelated lighting and optional-mod recipes.
- Keep each wood and furniture form as its own recipe-book entry instead of
  cycling unrelated furniture through one shared recipe cell.
- Synchronise and render thrown lava lamps visibly in flight, show the
  obsidian chunk at a useful angle in inventories, and allow wall sconces to
  use narrow centre supports such as fences and walls.
- Load Biomes O' Plenty, Immersive Engineering, and Oh The Biomes You'll Go
  recipes and advancements only when their mod is installed. Missing optional
  mods no longer produce ignored-advancement or missing-recipe log spam.
- Stop Forge from attempting to inject thousands of optional and obsolete
  connected-bench object holders at startup. Optional furniture is resolved
  only after its blocks have actually been registered, keeping clean installs
  quiet without changing any block or item IDs.
- Remove stale Immersive Engineering recipes and the unreachable log-bench
  advancement for furniture IDs that were never registered, and correct the
  retained IE texture paths against the published 1.15.2 jar.
- Match the latest published BYG 1.15.2 registry by excluding the unregistered
  bulbis, embur, glacial-oak, and ironwood furniture families and by using its
  legacy `byg:blocks/...` texture paths.

## Legacy world upgrades

- Add a pre-flattening migration path for supported IronAgeFurniture content
  from Forge 1.10.2 and 1.12.2 worlds.
- Preserve furniture facing, connected-bench shape, separate light states,
  and all sixteen padded-bench colours. The earlier colourless format becomes
  red, including items in player storage, nested containers, and item entities.
- Rename legacy `big_oak` furniture to `dark_oak`.
- Import furniture from the 1.12.2 Iron Age Furniture Oh The Biomes Add-On.
  Surviving woods keep their names; Frozen Oak maps to Aspen, Great Oak to
  vanilla Oak, Hawthorn to Cherry, Ironwood to Ebony, Palm to Baobab, and Rowan
  to Maple as visual fallbacks for woods retired before BYG 1.15.2.

## Compatibility and tooling

- Move the supported Java API namespace to
  `zone.moddev.mc.ironagefurniture`; the Maven coordinate is
  `zone.moddev.mc:iron-age-furniture:0.3.0.115021`.
- Preserve the `ironagefurniture` mod ID and all surviving registry, resource,
  configuration, entity, NBT, and saved-world identities.
- Modernize the build to ForgeGradle 7.0.34, Gradle 9.6.1, a pinned Temurin
  Java 8 compiler, and Java 17 for Gradle while allowing Eclipse to use a newer
  Temurin Java 8 patch.
- Add a deterministic furniture catalog, conditional-data validation, exact
  optional-mod SHA-256 manifests, and positive packaged-server probes.
- Produce deterministic main, sources, and Javadoc jars with SHA-256 checksums
  and reproducible Eclipse/Buildship metadata.

Publication remains gated by MMD's protected release workflow after manual
acceptance and hosted CI.
