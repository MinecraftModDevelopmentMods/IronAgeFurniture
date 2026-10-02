# Build logic

`build.gradle` contains the Forge 1.12 setup: dependencies, the Java 8
compiler, development runs, processed resources, and ordinary jars. The
longer checks live in smaller applied scripts. They are part of this project,
not separate Gradle plugins.

| Script | Purpose |
| --- | --- |
| `furniture-catalog.gradle` | Verify generated furniture files or update them on request. |
| `phase-four-*.gradle` | Generate the Phase 4 furniture, models, and metal recipes. |
| `verification/legacy-fixtures.gradle` | Compare checked-in migration samples with freshly generated ones. |
| `verification/optional-integrations.gradle` | Check pinned optional-mod jars and run packaged-server probes. |
| `verification/project-audits.gradle` | Check resources, metadata, namespace, workflows, and tracked files. |
| `release/artifacts.gradle` | Audit and checksum the three release jars. |
| `release/publishing.gradle` | Configure Maven publication and require explicit credentials. |
| `ide/eclipse.gradle` | Prepare Buildship and Forge 1.12 Eclipse launches. |

For a routine local check, run `gradlew.bat clean check build javadoc
verifyReleaseArtifacts verifyReleaseChecksums`. After changing the build or
importing it into Eclipse, run `gradlew.bat prepareEclipse
verifyEclipseProductionClasspath`. The packaged optional-mod tests are
separate: they need the exact jars listed in
`verification/optional-integration-mods.json` and the Forge installer checked
by `verifyOptionalIntegrationForgeInstaller`.
