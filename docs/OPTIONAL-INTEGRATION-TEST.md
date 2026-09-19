# Optional integration status

IronAgeFurniture `0.3.0.2603001` targets Minecraft 26.3 with Forge 66.0.2.

No Biomes O' Plenty, Oh The Biomes We've Gone, or Immersive Engineering build is currently published for this target, so this branch deliberately ships no optional furniture catalog, metadata declaration, embedded resource pack, pinned third-party manifest, or positive runtime probe.

The base mod must start cleanly without those mods and must not emit missing optional recipe, advancement, registry, model, or texture warnings. The scoped aliases for retired IronAgeFurniture Biomes O' Plenty Cherry IDs remain because those IDs map to vanilla Cherry furniture and do not require Biomes O' Plenty to be installed.

When a compatible integration is published, inspect its real jar and dependency metadata before restoring any catalog. Pin every test jar and dependency by SHA-256, verify actual registry and texture paths, keep registration and data conditional, and test the integration both separately and in the complete compatible stack.
