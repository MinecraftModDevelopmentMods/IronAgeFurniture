# IronAgeFurniture Versioning

IronAgeFurniture releases use `Major.Minor.Bug.Target`. The first three components describe the functional release, and the final numeric component identifies the Minecraft and loader target.

The target is calculated as `MmmppL`: Minecraft major without padding, two-digit minor, two-digit patch, and a one-digit loader code. Forge uses loader code `1`; NeoForge uses `2`.

For Minecraft 1.15.2 Forge, the target is `115021`, so the Phase 4 version is `0.4.0.115021`. A release tag uses exactly that complete version.

Equivalent Phase 4 forward ports retain `0.4.0` and change only the target. A player-visible feature or fix changes Major, Minor, or Bug independently of the target.

The Phase 4 edition contains the upgrade bridge for supported Phase 2–4
furniture and lighting from 1.10.2 and 1.12.2, including the released 1.12.2
Oh The Biomes add-on, and preserves older 1.14.4 and 1.15.2 furniture saves.
See [UPGRADING.md](UPGRADING.md) for the tested paths and limits. Back up a world
before crossing the flattening boundary; a world saved by 1.15.2 cannot safely
return to an older game version.

The complete version must agree with `minecraft_version`, `loader_name`, and `loader_code` in `gradle.properties`. CI build numbers are never appended.
