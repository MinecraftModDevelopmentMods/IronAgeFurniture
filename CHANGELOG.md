# IronAgeFurniture 0.3.0.110021

Phase 3 lighting release for Minecraft 1.10.2 and Forge 12.18.3.2511.

## Player-visible changes

- Add the Phase 3 lighting set: empty, torch, unlit-torch, redstone-torch,
  glow, lava, and redstone lamps and sconces, obsidian chunks, and hidden
  redstone-light states.
- Restore throwable lava lamps. Thrown and falling lamps shatter with a glass
  sound, ignite dry landing positions, damage struck entities, and become an
  obsidian chunk on contact with water.
- Render thrown lava lamps with their translucent lava-lamp item model instead
  of the unbound grey projectile fallback.
- Let placed lava lamps melt ordinary ice directly beneath them. Other ice
  variants are intentionally unaffected.
- Correct falling-lamp handling, survival and Creative drops, and redstone
  transitions.
- Keep internal lamp and sconce state blocks out of the creative inventory;
  only the player-placeable base items have item forms.
- Give ordinary wooden furniture normal wood flammability.
- Add all sixteen carpet colours to padded benches and padded back benches
  without adding registry IDs. Existing benches remain red, every segment in
  a connected mixed-colour run retains its own upholstery, and colours survive
  placement, drops, pick block, shape changes, and reloads.
- Use the vanilla wool textures for padded upholstery, allowing compatible
  resource packs to change the fabric used by benches.
- Select padded bench inventory models from item metadata during model baking,
  so every coloured subtype renders with its matching upholstery in GUIs.
- Preserve upholstery choices on crafted, Creative, dropped, picked, and
  copied benches, including compatibility data for future Minecraft upgrades.
- Register Biomes O' Plenty, Natura, Forestry, and Immersive Engineering
  furniture and Java recipes only when the relevant optional mod is loaded.
  Minecraft 1.10.2 does not use recipe advancements.

## Compatibility and development

- Preserve all surviving `ironagefurniture` registry, resource,
  configuration, entity, NBT, and saved-world identities from Phase 2.
- Move the supported Java namespace to `zone.moddev.mc.ironagefurniture` and
  publish as `zone.moddev.mc:iron-age-furniture:0.3.0.110021`.
- Modernize the build to ForgeGradle 7.0.34 and Gradle 9.6.1, pin Java 17 for
  Gradle in CI, and use a Temurin Java 8 compiler while allowing Eclipse to run
  Buildship on newer JVMs and use newer Java 8 patches.
- Add a deterministic furniture catalog and optional-integration checks.
- Produce reproducible main, sources, and Javadoc jars with SHA-256 checksums.
- Provide reproducible Eclipse/Buildship project metadata for contributors.
