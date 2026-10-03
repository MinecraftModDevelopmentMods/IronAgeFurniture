# Build logic

The main `build.gradle` configures Forge 1.14.4, the Java 8 compiler, development
launches, processed resources and the three release jars. Longer checks and
optional workflows live in smaller scripts:

| Script | Purpose |
| --- | --- |
| `furniture-catalog.gradle` | Verify the committed furniture resources or regenerate them explicitly. |
| `verification/optional-integrations.gradle` | Check pinned dependency jars and launch packaged-server integration tests. |
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
