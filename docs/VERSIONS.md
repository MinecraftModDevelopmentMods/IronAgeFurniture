# IronAgeFurniture Versioning

IronAgeFurniture releases use `Major.Minor.Bug.Target`. The first three components describe the functional release, and the final numeric component identifies the Minecraft and loader target.

The target is calculated as `MmmppL`: Minecraft major without padding, two-digit minor, two-digit patch, and a one-digit loader code. Forge uses loader code `1`; NeoForge uses `2`.

For Minecraft 1.14.4 Forge, the target is `114041`. The current development
version is `0.4.0.114041`; the stable Phase 3 release is `0.3.0.114041`. A release
tag uses the complete version, but this development branch has not been released.

Equivalent Phase 4 forward ports retain `0.4.0` and change only the target. A player-visible feature or fix changes Major, Minor, or Bug independently of the target.

The 1.14.4 release is the first version after Minecraft's flattening and
contains the upgrade bridge for supported IronAgeFurniture content saved by
the 1.10.2 and 1.12.2 releases. Back up a world before moving it across this
boundary; once Minecraft has saved it as 1.14.4, it cannot be opened safely by
the older game versions.

The 1.10/1.12 versions store upholstery under `Color` and sconce metal under
`Metal`. In 1.14, colours and metals use block properties and distinct inventory
items. Existing red/iron item IDs and every surviving block ID are retained;
new variant item IDs append a colour or replace the iron metal suffix.
The upgrade bridge converts legacy metadata, tile data and tagged items from
earlier 1.14 candidates. An absent Base Metals installation
does not erase a known metal; it only changes availability and rendering until
that material is supplied again.

The complete version must agree with `minecraft_version`, `loader_name`, and `loader_code` in `gradle.properties`. CI build numbers are never appended.
