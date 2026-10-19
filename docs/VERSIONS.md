# IronAgeFurniture Versioning

IronAgeFurniture releases use `Major.Minor.Bug.Target`. The first three components describe the functional release, and the final numeric component identifies the Minecraft and loader target.

The target is calculated as `MmmppL`: Minecraft major without padding, two-digit minor, two-digit patch, and a one-digit loader code. Forge uses loader code `1`; NeoForge uses `2`.

For Minecraft 26.1.2 NeoForge, the target is `2601022`, so the Phase 4 version is `0.4.0.2601022`. A release tag, when published, is exactly that complete version.

Equivalent ports retain their functional version and change only the target. A player-visible feature or fix changes Major, Minor, or Bug independently of the target. Biomes O' Plenty integration remains part of the core on this branch and shares its version.

The complete version must agree with `minecraft_version`, `loader_name`, and `loader_code` in `gradle.properties`. CI build numbers are never appended.
