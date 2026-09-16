# Optional integration runtime tests

This Minecraft 26.3 NeoForge target has no optional-integration runtime probe because
Biomes O' Plenty, Oh The Biomes We've Gone, and Immersive Engineering do not currently
publish compatible NeoForge 26.3 builds. Their furniture registrations and resources
are deliberately absent rather than left untestable.

When a compatible integration is released, add it to `gradle/furniture-catalog.json`,
pin the exact third-party jars and SHA-256 checksums, restore the positive packaged-
server probe, and verify all conditional recipes, advancements, registrations, and
external texture paths before enabling it for this target.
