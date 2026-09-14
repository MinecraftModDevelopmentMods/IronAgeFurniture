# Optional integration runtime test

This positive-path test starts Forge 1.20.1 with Iron Age Furniture and all three
supported optional integrations. A test-only Forge mod then checks the live server
registries for every conditional recipe and recipe advancement and stops the server.

The third-party mod jars are deliberately not committed or redistributed. Their
exact filenames, versions, SHA-256 checksums, roles, and source pages are pinned in
`gradle/verification/optional-integration-mods.json`.

## Setup

1. Install the Forge `1.20.1-47.4.10` server into the ignored
   `run-optional-integrations` directory and accept its EULA.
2. Download every jar listed in the manifest into
   `run-optional-integrations/mods`. This directory may also contain the generated
   probe and Iron Age Furniture jars.
3. Run `gradlew.bat runOptionalIntegrationServer` with the project's pinned Java
   installations available as described in `gradle.properties`.

An existing disposable Forge server can be used without moving it by adding
`-PoptionalIntegrationServerDir=<server-directory>` to the command. The manifest
jars must be in that server's `mods` directory. The task stages only the current
packaged Iron Age Furniture jar and generated probe; the server installation and
third-party jars remain local.

The Gradle task verifies every supplied jar against the pinned checksum before the
server starts. It also checks every BOP, BWG, and Immersive Engineering texture
referenced by an Iron Age Furniture block model against the contents of those
exact jars. Catalog-generated woods must use their own declared log, log-top, and
planks mappings, so a valid but incorrect shared texture cannot pass the check.
It then builds and stages
`ironagefurniture-optional-integration-probe.jar`, removes any stale result, and
accepts success only when the probe writes
`optional-integration-pass.properties` in the selected server directory with all
1,497 conditional recipes and all 1,481 conditional recipe advancements present.

The probe also records the loaded versions and per-integration counts. It is built
from `src/optionalIntegrationTest` and is explicitly excluded from all release
artifacts.
