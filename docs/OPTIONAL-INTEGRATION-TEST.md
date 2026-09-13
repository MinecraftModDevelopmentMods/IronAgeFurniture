# Optional integration runtime test

This positive-path test starts NeoForge 26.2 with Iron Age Furniture and the supported
Biomes O' Plenty integration. A test-only NeoForge mod then checks the live server
registries for every conditional recipe and recipe advancement and stops the server.

The third-party mod jars are deliberately not committed or redistributed. Their
exact filenames, versions, SHA-256 checksums, roles, and source pages are pinned in
`gradle/verification/optional-integration-mods.json`.

## Setup

1. Install the NeoForge `26.2.0.45-beta` server into the ignored
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

The default probes BOP and its pinned dependencies. It can also be selected explicitly:

```text
gradlew.bat runOptionalIntegrationServer -PoptionalIntegrationProbeMods=biomesoplenty
```

To repeat the packaged-jar probe against another compatible NeoForge 26.2 server,
set both `-PoptionalIntegrationServerDir=<server-directory>` and
`-PoptionalIntegrationNeoVersion=<loader-version>`. This is used to validate the
release candidate against stable NeoForge `26.2.0.87` without changing the branch's
declared build identity.

The Gradle task verifies every supplied jar against the pinned checksum before the
server starts. It builds and stages
`ironagefurniture-optional-integration-probe.jar`, removes any stale result, and
accepts success only when the probe writes
`optional-integration-pass.properties` in the selected server directory with all
selected conditional resources present. The exact expectations are 546 recipes and
546 advancements for BOP.

The probe also records the loaded versions and per-integration counts. It is built
from `src/optionalIntegrationTest` and is explicitly excluded from all release
artifacts.
