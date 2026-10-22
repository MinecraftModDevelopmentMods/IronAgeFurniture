# Optional integration runtime test

This positive-path test starts NeoForge 1.21.1 with Iron Age Furniture and the supported
Biomes O' Plenty, Oh The Biomes We've Gone, and Immersive Engineering integrations. A test-only NeoForge mod then checks the live server
registries for every conditional recipe and recipe advancement and stops the server.

The third-party mod jars are deliberately not committed or redistributed. Their
exact filenames, versions, SHA-256 checksums, roles, and source pages are pinned in
`gradle/verification/optional-integration-mods.json`.

## Setup

1. Install the NeoForge `21.1.247` server into the ignored
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

The default probes the combined BOP, BWG, and Immersive Engineering stack. Each
integration can also be selected independently. Remove the other integration mods
from that disposable server first; their library dependencies may remain installed:

```text
gradlew.bat runOptionalIntegrationServer -PoptionalIntegrationProbeMods=biomesoplenty
gradlew.bat runOptionalIntegrationServer -PoptionalIntegrationProbeMods=biomeswevegone
gradlew.bat runOptionalIntegrationServer -PoptionalIntegrationProbeMods=immersiveengineering
```

The Gradle task verifies every supplied jar against the pinned checksum before the
server starts. It builds and stages
`ironagefurniture-optional-integration-probe.jar`, removes any stale result, and
accepts success only when the probe writes
`optional-integration-pass.properties` in the selected server directory with all
selected conditional resources present. The expectations are 1,755 recipes and
1,755 advancements for BOP, 3,375 and 3,375 for BWG, and 150 and 150 for Immersive
Engineering. The combined stack contains 5,280 recipes and 5,280 advancements.

The probe also records the loaded versions and per-integration counts. It is built
from `src/optionalIntegrationTest` and is explicitly excluded from all release
artifacts.
