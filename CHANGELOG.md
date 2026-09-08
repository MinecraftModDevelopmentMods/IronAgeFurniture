# Changelog

## 0.3.0.110021 - Unreleased

- Added the Phase 3 Iron Age Furniture gameplay line, including candle surface lighting and particle behavior.
- Reworked hanging inn signs, support behavior, placement, drops, rendering, and compatibility details.
- Migrated the Minecraft 1.10.2 development build to ForgeGradle 7 and target-qualified MMD versioning.
- Moved the Java API and implementation from `com.mcmoddev.ironagefurniture` to `zone.moddev.mc.ironagefurniture`, and changed the Maven group to `zone.moddev.mc`.
- Fixed dedicated-server startup by isolating client-only item model and color registration.
- Replaced reflective Power Advantage fluid-pipe integration with its pinned public `FluidNetworkApi`, while keeping Power Advantage optional at runtime.
- Preserved compact resource generation while making release archives deterministic.

## 0.2.0.5 - Published

- Latest published Iron Age Furniture release for Minecraft 1.10.2.
