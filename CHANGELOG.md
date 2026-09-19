# IronAgeFurniture 0.3.0.110021

Phase 3 lighting release for Minecraft 1.10.2 and Forge 12.18.3.2511.

## Player-visible changes

- Add the Phase 3 lighting set: empty, torch, unlit-torch, redstone-torch,
  glow, lava, and redstone lamps and sconces, obsidian chunks, and hidden
  redstone-light states.
- Restore throwable lava lamps. Thrown and falling lamps shatter with a glass
  sound, ignite dry landing positions, damage struck entities, and become an
  obsidian chunk on contact with water.
- Let placed lava lamps melt ordinary ice directly beneath them. Other ice
  variants are intentionally unaffected.
- Correct falling-lamp handling, survival and Creative drops, and redstone
  transitions.
- Give ordinary wooden furniture normal wood flammability.
- Register Biomes O' Plenty, Natura, Forestry, and Immersive Engineering
  furniture and Java recipes only when the relevant optional mod is loaded.
  Minecraft 1.10.2 does not use recipe advancements.

## Compatibility and tooling

- Preserve all surviving `ironagefurniture` registry, resource,
  configuration, entity, NBT, and saved-world identities from Phase 2.
- Keep later 1.10-only candles, chandeliers, hanging signs, beds, tables,
  shelves, cabinets, machinery, and other 1.0 work on
  `feature/1.10-v1.0.0`; those features are not in this release.
- Move the supported Java namespace to `zone.moddev.mc.ironagefurniture` and
  publish as `zone.moddev.mc:iron-age-furniture:0.3.0.110021`.
- Modernize the build to ForgeGradle 7.0.34 and Gradle 9.6.1, pin Java 17 for
  Gradle in CI, and use a Temurin Java 8 compiler while allowing Eclipse to run
  Buildship on newer JVMs and use newer Java 8 patches.
- Add a deterministic furniture catalog, optional-integration audits,
  deterministic main/sources/Javadoc jars, checksums, and reproducible
  Eclipse/Buildship metadata.

Publication remains gated by MMD's protected release workflow after manual
acceptance and hosted CI.
