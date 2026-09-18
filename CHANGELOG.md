# IronAgeFurniture 0.3.0.114041

Phase 3 lighting release for Minecraft 1.14.4 and Forge 28.2.26.

## Player-visible changes

- Add the complete Phase 3 lighting set: empty, torch, unlit-torch,
  redstone-torch, glow, lava, and redstone lamps and sconces, obsidian chunks,
  and hidden redstone-light states. Soul-torch sconces remain exclusive to
  Minecraft 1.16 and newer, where soul torches exist.
- Restore lava-lamp throwing. Thrown and falling lamps shatter with a glass
  sound, ignite dry landing positions, damage struck entities, and become a
  waterlogged obsidian chunk on contact with water.
- Let placed lava lamps melt ordinary ice directly beneath them. Packed ice
  and blue ice are intentionally unaffected.
- Correct falling and underwater lamp placement, survival and Creative drops,
  and redstone-light transitions.
- Give wooden chairs and benches normal wood flammability.
- Correct every recipe advancement to use its own ingredient and matching
  recipe-unlocked criterion. Common oak planks no longer unlock unrelated
  lighting or optional-mod recipes.
- Load Biomes O' Plenty and Immersive Engineering
  recipes and advancements only when their mod is installed. Missing optional
  mods no longer produce ignored-advancement or missing-recipe log spam.
- Remove stale Immersive Engineering recipes and the unreachable log-bench
  advancement for furniture IDs that were never registered, and correct the
  retained IE texture paths against the published 1.14.4 jar.
## Compatibility and tooling

- Move the supported Java API namespace to
  `zone.moddev.mc.ironagefurniture`; the Maven coordinate is
  `zone.moddev.mc:iron-age-furniture:0.3.0.114041`.
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
