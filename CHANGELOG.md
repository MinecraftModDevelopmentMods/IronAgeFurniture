# IronAgeFurniture 0.4.0.114041

Phase 4 for Minecraft 1.14.4 and Forge 28.2.26. Requires Java 8 on clients and servers.

## New furniture

- Add wooden and canopy beds, including doubles, plus wingback chairs and
  thrones in all sixteen upholstery colours for supported vanilla, Biomes O'
  Plenty and Immersive Engineering woods.
- Craft a wooden bed from a vanilla bed and a matching plank, keeping its
  colour. Add another matching plank and carpet to make a canopy bed. Combine
  two matching singles for a double bed.
- Make chair upgrades shapeless: a classic chair, matching plank and chosen
  carpet make a wingback; a wingback, matching plank and matching carpet make
  a throne.
- Give coloured beds and tall chairs separate inventory items so the recipe
  book selects the right ingredients. Bed recolouring remains available by
  hand but stays out of the recipe book, including previously learned recipes.
- Discover matching throne, canopy and double-bed recipes as soon as their
  input furniture is crafted, without closing the crafting table. Other woods
  and colours are not unlocked.
- Split Creative inventory into Chairs, Benches, Beds and Lights.
- Place wingbacks and thrones facing the player, like classic chairs. Existing
  placed chairs keep their direction. Wooden beds burn like wooden seating.

## Shields and lighting

- Remove and refit chair shields without losing damage, banner designs,
  enchantments, names or other item data. Shield-chair crafting keeps the
  supplied shield; breaking a chair returns its frame and shield separately.
- Add floor and wall candles, small flames and held-candle smoke. Smelt meat
  or rotten flesh into tallow; one tallow and one string make eight candles.
  Held effects are cosmetic unless a separate dynamic-light feature is enabled.
- Fit up to four candles or two torches into a sconce. Water extinguishes them.
  Redstone lights waterlogged twin torches while powered; they go out again
  when the signal stops.
- Recover sconce frames and their contents with bare hands or any tool,
  including unlit and waterlogged states and broken supports. Suitable
  pickaxes still mine faster; dropped frames keep their metal.
- Add gold and 21 optional Base Metals sconce materials with matching textures,
  mining toughness and blast resistance. Five matching nuggets make four
  sconces for every supported metal.
- Add optional Mineralogy rock-salt sconces, which stay lit underwater and
  return their lamp and frame when removed or broken.
- Recover an intact lava lamp from a sconce with Silk Touch. Without it, the
  frame drops and the lamp shatters into fire. Creative breaking creates
  neither drops nor fire.
- Powered wall lava sconces release their lamp when the space below is clear.
  The frame stays on the wall; the falling lamp shatters into fire on dry ground
  or an obsidian chunk in water.

## Existing worlds and integrations

- Preserve supported furniture from 1.10.2, 1.12.2 and older 1.14.4 releases:
  facing, connected states, upholstery, metals, beds, stored shields and items
  in player inventories, Ender Chests, containers, nested containers and drops.
- Convert legacy colour and metal item data into the matching inventory
  identities without changing surviving block IDs. Missing colour defaults to
  red; missing metal defaults to iron. Known Base Metals names survive while
  that mod is absent. Legacy `big_oak` furniture becomes `dark_oak`.
- Add one-for-one recipes for CFM's six vanilla-wood chairs. Existing CFM
  chairs remain unchanged unless forced conversion is enabled; supported
  chairs are recovered automatically when CFM is removed. Other CFM furniture
  is not converted.
- Retain the 1.12 language choices and translations, with English names for
  entries still awaiting translation. Hide unavailable optional woods and
  metals without missing-recipe or object-holder debug spam.
- Verify the published Base Metals 3.0.1.114041 and OreSpawn 4.1.0.114041
  integration, including recipes, textures, drops and legacy metal saves.
- Split the build scripts by responsibility while retaining the Forge 1.14.4
  toolchain, reproducible release jars, Eclipse setup and guarded publication.

Back up your world before upgrading. Supported upgrade fixtures load directly
and retain their converted data on a second load, but unsupported mods and
containers may need preparation in the old version. See
[the upgrade guide](docs/UPGRADING.md) for supported formats and limitations.

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
- Fix waterlogging lava-filled floor and wall sconces so they become matching
  waterlogged empty sconces, preserve their supports, and drop one obsidian chunk.
- Let placed lava lamps melt ordinary ice directly beneath them. Packed ice
  and blue ice are intentionally unaffected.
- Correct falling and underwater lamp placement, survival and Creative drops,
  and redstone-light transitions.
- Give wooden chairs and benches normal wood flammability.
- Correct every recipe advancement to use its own ingredient and matching
  recipe-unlocked criterion. Common oak planks no longer unlock unrelated
  lighting or optional-mod recipes, and use the correct Minecraft 1.14 item
  predicate format so ordinary inventory changes cannot unlock everything.
- Keep each wood and furniture form as its own recipe-book entry instead of
  cycling unrelated furniture through one shared recipe cell.
- Synchronise and render thrown lava lamps visibly in flight, and show the
  obsidian chunk at a useful angle in inventories.
- Allow floor sconces to stand on narrow centre supports such as fences and
  walls, and correct wall-sconce support-face checks.
- Load Biomes O' Plenty and Immersive Engineering
  recipes and advancements only when their mod is installed. Missing optional
  mods no longer produce ignored-advancement or missing-recipe log spam.
- Remove stale Immersive Engineering recipes and the unreachable log-bench
  advancement for furniture IDs that were never registered, and correct the
  retained IE texture paths against the published 1.14.4 jar.

## Legacy world upgrades

- Add a pre-flattening migration path for supported IronAgeFurniture blocks
  and items from Forge 1.10.2 and 1.12.2 worlds.
- Preserve chair, stool, connected-bench, and Phase 3 light facing and state
  while Minecraft converts the world to 1.14.4.
- Convert the original red-only padded benches to red and preserve all sixteen
  colours used by the later 1.10.2 and 1.12.2 padded-bench format, including
  items in player and Ender Chest inventories, nested containers, and dropped
  item entities.
- Rename legacy `big_oak` furniture to the matching `dark_oak` furniture.
- Leave unsupported Natura, Forestry, and retired Biomes O' Plenty woods
  unmapped because this release has no equivalent furniture for them.

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
