# IronAgeFurniture 0.4.0.112021

Phase 4 furniture and lighting update for Minecraft 1.12.2 and Forge
14.23.5.2859.

- Add multiblock wooden and canopy beds, wingback chairs, and thrones, with
  sixteen upholstery colours preserved through placement, drops, and reloads.
- Let wooden and canopy beds burn like chairs. Fire consuming any bed part
  removes the whole bed, without leaving stray parts or dropping an intact bed.
- Let shield chairs release and reattach their shields without losing the
  shield's damage, banner design, enchantments, name, or other item data.
- Add twin-torch sconces, standalone and mounted candles, Mineralogy rock-salt
  sconces, and gold and Base Metals sconce materials. Older iron sconces remain
  iron; metal choices survive mining and pick block.
- Show a small flame and smoke when a candle is held. OptiFine Dynamic Lights
  recognises IAF's emitting block items automatically. The README includes
  AtomicStryker Dynamic Lights 1.12.2 settings for candles, glow lamps and lava
  lamps, while keeping unpowered red lamps dark. IAF adds no lighting engine.
- Raise first-person candle smoke above the flame and keep it beside the candle
  when looking up or down, rather than letting it start near the holder.
- Release a wall-sconce lava lamp on a redstone signal when air is available
  below it. Falling lamps shatter and can start a fire outside Creative mode.
- Add the seventeen locale choices used across the Phase 4 furniture set.
- Add conditional conversion recipes for six MrCrayfish's Furniture Mod
  wooden chairs. Recover their old placed and stored identities when that mod
  is absent; conversion while it remains installed is optional and off by
  default.
- Preserve the Phase 3 registry identities and existing padded-bench colours.
  Older furniture with no `Color` value remains red, and sconces with no
  `Metal` value remain iron.
- Keep all sixteen padded-bench colours when either bench style is mined;
  previously a coloured bench could drop as red.
- Give each furniture design, wood and upholstery colour its own recipe-book
  entry instead of cycling all seating through one cell. Alternate recipes
  for the same chair remain together; bed combining and recolouring recipes
  also stay separate for each wood and bed style.
- Keep the original sconce metal when mining a lava-lamp sconce. Silk Touch
  returns the empty sconce and intact lamp; ordinary mining drops the empty
  sconce and shatters the lamp into fire.
- Restore Natura redwood furniture under its historical registry names, so
  worlds that used that wood can retain their chairs and benches.
- Organize the build checks, Eclipse setup, and release tasks into smaller
  Gradle files without changing the Forge 1.12 toolchain or publication gate.
- Refresh artifact checksums when jars change during an incremental build.
- Preserve all sixteen vanilla bed colours when crafting wooden beds. Adding
  another matching plank upgrades a wooden bed to a canopy bed without changing
  its colour; carpet is now used only for deliberate bed recolouring.

# IronAgeFurniture 0.3.0.112021

Phase 3 lighting release for Minecraft 1.12.2 and Forge 14.23.5.2859.

- Add all sixteen carpet colours to padded benches and padded back benches
  across vanilla and every supported optional wood integration, without adding
  registry IDs or changing bench joining behaviour.
- Preserve upholstery through a non-ticking tile entity, the shared historical
  `ironagefurniture:padded_bench_colour` ID, legacy item metadata and a stable
  `Color` NBT string suitable for later flattening migration.
- Render placed and inventory upholstery with resource-pack-overridable vanilla
  wool textures through a client-only custom baked model.
- Package English locale files under Minecraft 1.12's lowercase resource
  paths, restoring translated furniture names and the creative-tab title.
- Register every padded-bench item variant during Forge 1.12's model-registry
  event and select its inventory model from the stack's stable colour data,
  fixing missing-model icons for padded back benches and later entries.

## Player-visible changes

- Add the complete Phase 3 lighting set: empty, torch, unlit-torch,
  redstone-torch, glow, lava, and redstone lamps and sconces, obsidian chunks,
  and hidden redstone-light states. Soul-torch sconces remain exclusive to
  Minecraft 1.16 and newer, where soul torches exist.
- Restore lava-lamp throwing. Thrown and falling lamps shatter with a glass
  sound, ignite dry landing positions, damage struck entities, and become an
  obsidian chunk on contact with water.
- Render thrown lava lamps with their translucent lava-lamp item model instead
  of the unbound grey projectile fallback.
- Let placed lava lamps melt ordinary ice directly beneath them. Packed ice
  and blue ice are intentionally unaffected.
- Correct falling and underwater lamp placement, survival and Creative drops,
  and redstone-light transitions.
- Give wooden chairs and benches normal wood flammability.
- Correct every recipe advancement to use its own ingredient and matching
  recipe-unlocked criterion. Common oak planks no longer unlock unrelated
  lighting or optional-mod recipes.
- Load Biomes O' Plenty, Natura, Forestry, and Immersive Engineering
  recipes and advancements only when their mod is installed. Missing optional
  mods no longer produce ignored-advancement or missing-recipe log spam.
- Preserve the established 1.12.2 optional wood catalogs while preventing
  absent integrations from registering furniture or loading their data.
## Compatibility and tooling

- Move the supported Java API namespace to
  `zone.moddev.mc.ironagefurniture`; the Maven coordinate is
  `zone.moddev.mc:iron-age-furniture:0.3.0.112021`.
- Preserve the `ironagefurniture` mod ID and all surviving registry, resource,
  configuration, entity, NBT, and saved-world identities.
- Allow furniture add-ons to use their own registry namespace while retaining
  the established connected-bench behaviour.
- Expose client registration for add-on padded-bench namespaces and items so
  their placed and inventory models can use the shared sixteen-colour renderer.
- Modernize the build to ForgeGradle 7.0.34, Gradle 9.6.1, a pinned Temurin
  Java 8 compiler, and Java 17 for Gradle while allowing Eclipse to use a newer
  Temurin Java 8 patch.
- Add a deterministic furniture catalog, conditional-data validation, exact
  optional-mod SHA-256 manifests, and positive packaged-server probes.
- Produce deterministic main, sources, and Javadoc jars with SHA-256 checksums
  and reproducible Eclipse/Buildship metadata.

Publication remains gated by MMD's protected release workflow after manual
acceptance and hosted CI.
