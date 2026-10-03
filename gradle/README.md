# Build logic

The main `build.gradle` configures Forge 1.14.4, the Java 8 compiler, development
launches, processed resources and the three release jars. Longer checks and
optional workflows live in smaller scripts:

| Script | Purpose |
| --- | --- |
| `furniture-catalog.gradle` | Verify the committed furniture resources or regenerate them explicitly. |
| `phase-four-shields.gradle` | Generate and check removable shield-chair frames and crafting recipes. |
| `verification/optional-integrations.gradle` | Check pinned dependency jars and launch packaged-server integration tests. |
| `verification/phase-four-runtime.gradle` | Build the isolated shield-chair test mod and run it on a disposable server. |
| `verification/project-audits.gradle` | Check resources, metadata, namespaces, workflow pins and tracked files. |
| `release/artifacts.gradle` | Audit the main, sources and Javadoc jars, then checksum the release bundle. |
| `release/publishing.gradle` | Configure Maven uploads and require publication credentials. |
| `ide/eclipse.gradle` | Generate and check the nested Buildship project and production launches. |

For ordinary validation, run `gradlew.bat clean check build javadoc
verifyReleaseArtifacts verifyReleaseChecksums`. After changing the project
setup, run `gradlew.bat prepareEclipse verifyEclipseProductionClasspath`.

The integration probes are separate because they need the exact optional-mod
jars listed in `verification/optional-integration-mods.json` and a verified
Forge installer. Local builds and Eclipse imports never publish a release.

The shield-chair probe checks placement, mining, pick block, removable shields,
falling chairs and saved data. Run `phaseFourProbeJar` to build it, then use
`runPhaseFourServer` with `-PphaseFourServerDir` pointing to a disposable installed
Forge 1.14.4 server. Running it twice checks the decorated chair and empty frame
saved by the first run. Never point this test at a world you want to keep.

The same test jar can check baked item and block models on a packaged client.
It closes the test client after checking its models and writes
`phase-four-client-pass.properties`. These checks cover shield chairs only;
the rest of the Phase 4 port still needs its own gameplay and upgrade tests.
