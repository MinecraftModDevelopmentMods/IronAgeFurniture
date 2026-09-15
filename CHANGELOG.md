# IronAgeFurniture 0.3.0.2601021

- Port IronAgeFurniture to Minecraft 26.1.2 and Forge 64.0.9 on Java 25.
- Retain Pale Oak and the complete contemporary Biomes O' Plenty furniture family, including Origin Oak.
- Validate built-in texture paths and every optional wood model semantically against the exact pinned Biomes O' Plenty jar.
- Omit Oh The Biomes We've Gone and Immersive Engineering because neither integration supports this target matrix.
- Generate Forge 64 item definitions and target-native conditional recipe and advancement data.
- Carry forward the deterministic checked-in furniture catalog, scoped BOP cherry remap, and positive BOP runtime probe.
- Fix recipe advancements so furniture unlocks only when its actual ingredient is obtained, including conditional BOP recipes.
- Restore normal wooden-furniture fire behaviour while keeping Crimson and Warped furniture non-flammable.
- Restore throwable lava lamps, water shattering with the glass sound and obsidean-chunk drop, and safe creative-mode removal.
- Preserve transparent glass and visible contents while glow, lava, and red lamps are falling.
