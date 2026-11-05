# IronAgeFurniture Versioning

IronAgeFurniture releases use `Major.Minor.Bug.Target`. The first three components describe the functional release, and the final numeric component identifies the Minecraft and loader target.

The target is calculated as `MmmppL`: Minecraft major without padding, two-digit minor, two-digit patch, and a one-digit loader code. Forge uses loader code `1`; NeoForge uses `2`.

For Minecraft 1.14.4 Forge, the target is `114041`. Phase 4 shipped as
`0.4.0.114041`, following Phase 3's `0.3.0.114041`. The Phase 4 bugfix release
is `0.4.1.114041`. It fixes lamp mining and updates how placed sconces save their
metal. A release tag uses the complete version.

Equivalent Phase 4 forward ports retain `0.4.0` and change only the target. A player-visible feature or fix changes Major, Minor, or Bug independently of the target.

The 1.14.4 release is the first version after Minecraft's flattening and
contains the upgrade bridge for supported IronAgeFurniture content saved by
the 1.10.2 and 1.12.2 releases. Back up a world before moving it across this
boundary; once Minecraft has saved it as 1.14.4, it cannot be opened safely by
the older game versions.

The 1.10/1.12 versions store upholstery under `Color` and sconce metal under
`Metal`. In 1.14.4, upholstery uses block properties and distinct inventory
items. Starting with `0.4.1.114041`, placed sconces use a separate block ID for
each metal. Existing sconce item IDs and the iron sconce block IDs remain valid.
The upgrade bridge converts legacy metadata, tile data and tagged items from
earlier 1.14 candidates. An absent Base Metals installation
does not erase a known metal; it only changes availability and rendering until
that material is supplied again.

The complete version must agree with `minecraft_version`, `loader_name`, and `loader_code` in `gradle.properties`. CI build numbers are never appended.
