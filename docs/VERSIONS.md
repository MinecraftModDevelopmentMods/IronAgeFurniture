# IronAgeFurniture Versioning

IronAgeFurniture releases use `Major.Minor.Bug.Target`. The first three components describe the functional release, and the final numeric component identifies the Minecraft and loader target.

The target is calculated as `MmmppL`: Minecraft major without padding, two-digit minor, two-digit patch, and a one-digit loader code. Forge uses loader code `1`; NeoForge uses `2`.

For Minecraft 1.20.6 NeoForge, the target is `120062`, so the Phase 4 version is `0.4.0.120062`. The release tag is the complete version, without a prefix. Local builds do not create tags or publish releases.

Equivalent Phase 4 ports retain `0.4.0` and change only the target. A player-visible feature or fix changes Major, Minor, or Bug independently of the target. The target does not imply compatibility with saves from newer Minecraft releases.

The complete version must agree with `minecraft_version`, `loader_name`, and `loader_code` in `gradle.properties`. CI build numbers are never appended.
