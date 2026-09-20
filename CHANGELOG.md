# IronAgeFurniture 0.3.0.117011

Phase 3 lighting release for Minecraft 1.17.1 and Forge 37.1.1.

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
- Load Biomes O' Plenty recipes and advancements only when the mod is
  installed. Its absence no longer produces ignored-advancement or
  missing-recipe log spam.
- Omit Immersive Engineering integration because its 1.17.1 port never had a
  publicly released build that can be pinned and tested.

## Compatibility and tooling

- Move the supported Java API namespace to
  `zone.moddev.mc.ironagefurniture`; the Maven coordinate is
  `zone.moddev.mc:iron-age-furniture:0.3.0.117011`.
- Preserve the `ironagefurniture` mod ID and all surviving registry, resource,
  configuration, entity, NBT, and saved-world identities.
- Modernize the build to ForgeGradle 7.0.34, Gradle 9.6.1, and pinned Java
  toolchains while allowing Eclipse to use a newer Temurin 16 patch.
- Add a deterministic furniture catalog, conditional-data validation, exact
  optional-mod SHA-256 manifests, and positive packaged-server probes.
- Produce deterministic main, sources, and Javadoc jars with SHA-256 checksums
  and reproducible Eclipse/Buildship metadata.

Publication remains gated by MMD's protected release workflow after manual
acceptance and hosted CI.
