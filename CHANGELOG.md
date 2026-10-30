# IronAgeFurniture 0.4.1.119041

- Bring Phase 4 to Minecraft 1.19.4 and Forge 45.4.0 on Java 17.
- Add wingback chairs, thrones, single and double wooden beds, and canopy beds in all sixteen upholstery colours.
- Add removable shields with their saved details, empty wooden frames, mixed-colour candle sconces and fixed-metal frames.
- Keep separate recipe-book entries, ingredient discovery, four creative tabs and server-controlled gameplay settings.
- Add the copper ingot fallback: five ingots make 32 sconces when copper nuggets are unavailable; five nuggets still make four.
- Keep glass lamps and lamp sconces transparent in Fast and Fancy graphics.
- Add vanilla mangrove furniture and retain all eleven BOP and twenty-seven BYG wood families, plus IE treated wood.
- Include all seventeen language files and hidden HUD names, with native 1.19.4 item NBT and older furniture recovery.
- Keep saved player advancement history when loading and saving worlds.
- Speed up Eclipse sync by updating changed files, keeping raw resources out of the runtime folder and leaving launch generation to the setup task.

# IronAgeFurniture 0.3.0.119041

First modernized release for Minecraft 1.19.4 and Forge 45.4.0.

## Player-visible changes

- Restore lava-lamp throwing: use a lava lamp in the air to throw it, consuming
  one lamp unless the player is in Creative mode.
- Restore lava-lamp impact behavior. Lamps shatter with a glass sound, ignite
  dry landing positions, burn struck entities, and turn into an obsidian chunk
  after touching water.
- Let placed lava lamps melt ordinary ice directly beneath them. Packed ice and
  blue ice are intentionally unaffected.
- Restore normal wood-like burning for ordinary wooden chairs and benches while
  keeping crimson and warped furniture fireproof.
- Correct special recipe advancements so obtaining a common ingredient no
  longer unlocks unrelated lamp and furniture recipes.
- Conditionally load Biomes O' Plenty, Immersive Engineering, and Oh The Biomes
  You'll Go recipes and advancements. Missing optional mods no longer produce
  ignored-advancement or missing-recipe log spam.

## Compatibility and tooling

- Move the supported Java API namespace to
  `zone.moddev.mc.ironagefurniture`. The Maven coordinate is now
  `zone.moddev.mc:iron-age-furniture:0.3.0.119041`.
- Preserve the `ironagefurniture` mod ID and all persistent registry, resource,
  configuration, entity, NBT, and saved-world identities.
- Modernize the build to ForgeGradle 7.0.34, Gradle 9.6.1, and pinned Java
  toolchains.
- Produce deterministic main, sources, and Javadoc jars with SHA-256 checksums.
- Add reproducible Eclipse/Buildship setup and guarded CI, validation, and
  release workflows.

Publication is performed from MMD's protected release workflow after the exact
tagged commit passes hosted CI.
