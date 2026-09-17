# Optional integration runtime test

This positive-path test starts NeoForge 1.21.11 with Iron Age Furniture and the supported
Biomes O' Plenty and Oh The Biomes We've Gone integrations. A test-only NeoForge mod then checks the live server
registries for every conditional recipe and recipe advancement and stops the server.

The third-party mod jars are deliberately not committed or redistributed. Their
exact filenames, versions, SHA-256 checksums, roles, and source pages are pinned in
`gradle/verification/optional-integration-mods.json`.

## Setup

1. Install the NeoForge `21.11.45` server into the ignored
   `run-optional-integrations` directory and accept its EULA.
2. Download every jar listed in the manifest into
   `run-optional-integrations/mods`. This directory may also contain the generated
   probe and Iron Age Furniture jars.
3. Run `gradlew.bat runOptionalIntegrationServer` with the project's pinned Java
   installations available as described in `gradle.properties`.

An existing disposable NeoForge server can be used without moving it by adding
`-PoptionalIntegrationServerDir=<server-directory>` to the command. The manifest
jars must be in that server's `mods` directory. The task stages only the current
packaged Iron Age Furniture jar and generated probe; the server installation and
third-party jars remain local.

The default probes the combined BOP and BWG stack. Each
integration can also be selected independently; dependency jars may remain installed:

```text
gradlew.bat runOptionalIntegrationServer -PoptionalIntegrationProbeMods=biomesoplenty
gradlew.bat runOptionalIntegrationServer -PoptionalIntegrationProbeMods=biomeswevegone
```

The Gradle task verifies every supplied jar against the pinned checksum before the
server starts. It builds and stages
`ironagefurniture-optional-integration-probe.jar`, removes any stale result, and
accepts success only when the probe writes
`optional-integration-pass.properties` in the selected server directory with all
selected conditional resources present. The exact expectations are 546 recipes and
546 advancements for BOP and 975 recipes and advancements for BWG. The combined
stack contains 1,521 recipes and 1,521 advancements.

The probe also records the loaded versions and per-integration counts. It is built
from `src/optionalIntegrationTest` and is explicitly excluded from all release
artifacts.
