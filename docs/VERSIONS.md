# IronAgeFurniture Versioning

IronAgeFurniture releases use `Major.Minor.Bug.Target`. The first three components describe the functional release, and the final numeric component identifies the Minecraft and loader target.

The target is calculated as `MmmppL`: Minecraft major without padding, two-digit minor, two-digit patch, and a one-digit loader code. Forge uses loader code `1`; NeoForge uses `2`.

For Minecraft 26.3 Forge, the target is `2603001`. Phase 4 began at `0.4.0.2603001`; the translated HUD-name update is `0.4.1.2603001`. Release tags use the complete version.

Equivalent ports retain the functional version and change only the target. A player-visible feature or fix changes Major, Minor, or Bug independently of the target.

The complete version must agree with `minecraft_version`, `loader_name`, and `loader_code` in `gradle.properties`. CI build numbers are never appended.
