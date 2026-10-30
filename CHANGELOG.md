# IronAgeFurniture 0.4.1.120011

- Bring Phase 4 to Minecraft 1.20.1 and Forge 47.4.10 on Java 17.
- Add wingback chairs, thrones, single and double wooden beds, and canopy beds in all sixteen upholstery colours.
- Add removable shields with their saved details, empty wooden frames, mixed-colour candle sconces and fixed-metal frames.
- Keep separate recipe-book entries, ingredient discovery, four creative tabs and server-controlled gameplay settings.
- Add the copper ingot fallback: five ingots make 32 sconces when copper nuggets are unavailable; five nuggets still make four.
- Keep glass lamps and lamp sconces transparent in Fast and Fancy graphics.
- Retain all eleven vanilla, thirteen BOP and twenty-four BWG wood families, plus IE treated wood and the existing BOP 18/19 version checks.
- Include all seventeen language files and hidden HUD names, with native 1.20.1 item NBT and older furniture recovery.
- Fix loading saved player advancement progress and keep older recipe history when saving.
- Speed up Eclipse sync by updating changed files instead of recopying the whole furniture catalog.

# IronAgeFurniture 0.3.0.120011

First IronAgeFurniture release for Minecraft 1.20.1 and Forge 47.4.10.

## Player-visible changes

- Add furniture for the vanilla mangrove, cherry, and bamboo wood families.
- Support the Biomes O' Plenty wood set shared by its compatible 1.20.1
  releases, and conditionally add Empyreal, Maple, and Pine furniture when BOP
  19.0.0.96 or newer is installed, without imposing a minimum BOP version.
- Replace the retired Oh The Biomes You'll Go integration with Oh The Biomes
  We've Gone furniture, while retaining Immersive Engineering treated-wood
  furniture.
- Remap IronAgeFurniture's former Biomes O' Plenty cherry furniture to vanilla
  cherry. On installations with BWG, remap supported BYG furniture names to
  matching BWG woods, including BYG cherry to sakura and BYG mangrove to white
  mangrove. This is intentionally scoped to IronAgeFurniture entries and is
  not a general BYG world migration.
- Restore lava-lamp throwing: use a lava lamp in the air to throw it, consuming
  one lamp unless the player is in Creative mode.
- Restore lava-lamp impact behavior. Lamps shatter with a glass sound, ignite
  dry landing positions, burn struck entities, and turn into an obsidian chunk
  after touching water.
- Let placed lava lamps melt ordinary ice directly beneath them. Packed ice and
  blue ice are intentionally unaffected.
- Restore normal wood-like burning for ordinary wooden furniture while keeping
  crimson and warped furniture fireproof.
- Correct recipe advancements so obtaining a common ingredient no longer
  unlocks unrelated lamp and furniture recipes.
- Conditionally load Biomes O' Plenty, Immersive Engineering, and Oh The Biomes
  We've Gone recipes and advancements. Missing optional mods no longer produce
  ignored-advancement or missing-recipe log spam.

## Compatibility and tooling

- Preserve the `ironagefurniture` mod ID and persistent core registry,
  resource, configuration, entity, NBT, and saved-world identities.
- Keep the supported Java API namespace at
  `zone.moddev.mc.ironagefurniture`. The Maven coordinate is
  `zone.moddev.mc:iron-age-furniture:0.3.0.120011`.
- Add a deterministic, checked-in furniture catalog covering registrations,
  blockstates, models, recipes, advancements, and translations.
- Resolve each generated optional wood model to that wood's actual texture
  layout, including BWG's nested texture paths and current Immersive Engineering
  treated-wood paths.
- Verify the supported optional integrations with pinned positive-path runtime
  probes while keeping their test fixtures out of release artifacts.
- Build with ForgeGradle 7.0.34, Gradle 9.6.1, and pinned Java toolchains.
- Produce deterministic main, sources, and Javadoc jars with SHA-256 checksums.
- Provide reproducible Eclipse/Buildship setup and guarded CI, validation, and
  release workflows.

Publication is performed from MMD's protected release workflow after the exact
tagged commit passes hosted CI.
