# MMD Version Policy

Iron Age Furniture uses the MMD four-component version form:

`Major.Minor.Bug.Target`

- `Major.Minor.Bug` is the functional mod version. It changes when the mod's own behavior or compatibility changes.
- `Target` identifies the Minecraft version and loader without changing the functional version.
- The target is `MmmppL`: Minecraft major (`M`), zero-padded minor (`mm`), zero-padded patch (`pp`), then loader code (`L`).
- Loader code `1` means Forge.

For Minecraft 1.10.2 on Forge, the target is `110021`, so this candidate is `0.3.0.110021`.

The maintained Java package is `zone.moddev.mc.ironagefurniture`. Maven publications use group `zone.moddev.mc`, giving this candidate the coordinate `zone.moddev.mc:iron-age-furniture:0.3.0.110021`. The persistent mod and resource namespace remains `ironagefurniture`.

The build validates that the fourth component matches `minecraft_version` and `loader_code` in `gradle.properties`. `gradle.properties` is authoritative for release and target metadata; source code, `mcmod.info`, the manifest, README, and changelog must agree with it.
