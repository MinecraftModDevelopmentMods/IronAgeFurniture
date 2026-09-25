# IronAgeFurniture Versioning

IronAgeFurniture releases use `Major.Minor.Bug.Target`. The first three components describe the functional release, and the final numeric component identifies the Minecraft and loader target.

The target is calculated as `MmmppL`: Minecraft major without padding, two-digit minor, two-digit patch, and a one-digit loader code. Forge uses loader code `1`; NeoForge uses `2`.

For Minecraft 1.18.2 Forge, the target is `118021`, so this Phase 3 release is `0.3.0.118021`. The release tag is exactly that complete version.

Equivalent forward ports retain `0.3.0` and change only the target. A player-visible feature or fix changes Major, Minor, or Bug independently of the target.

The 1.18.2 release contains the upgrade bridge for supported furniture and
lighting saved by the 1.10.2 and 1.12.2 core releases. It also recognises the
1.12.2 Oh The Biomes Add-On namespace and carries supported furniture into the
embedded 1.18.2 BYG catalog. Back up a world before crossing the flattening
boundary; a world saved by 1.18.2 cannot safely return to an older game version.

The complete version must agree with `minecraft_version`, `loader_name`, and `loader_code` in `gradle.properties`. CI build numbers are never appended.
