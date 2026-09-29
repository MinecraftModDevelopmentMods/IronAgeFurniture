# Build logic

`build.gradle` contains the Forge 1.10 setup: dependencies, Java 8 compilation,
development runs, processed resources and ordinary jars. The longer tasks live
in small, applied scripts so their purpose is easier to find. These scripts are
part of this repository; they are not separate Gradle plugins.

| Script | Purpose |
| --- | --- |
| `furniture-catalog.gradle` | Check the generated furniture catalog and update it only when explicitly requested. |
| `verification/legacy-fixtures.gradle` | Rebuild and compare the checked-in old-world migration fixtures. |
| `verification/optional-integrations.gradle` | Inspect pinned optional-mod jars and run the opt-in packaged-server probe. |
| `verification/project-audits.gradle` | Check source resources, metadata, namespace, workflows and repository hygiene. |
| `release/artifacts.gradle` | Audit the three release jars, checksums and prepared bundle. |
| `release/publishing.gradle` | Set Maven publication details and require explicit remote credentials. |
| `ide/eclipse.gradle` | Prepare Buildship and keep normal Eclipse launches free of test/probe output. |

The root build applies these scripts in dependency order and shares only the
few values they need. Keep existing task names stable: CI, Eclipse setup and
the release workflow call them directly.

For a routine local check, run `gradlew.bat clean check build javadoc
verifyReleaseArtifacts verifyReleaseChecksums`. Run `gradlew.bat prepareEclipse
verifyEclipseProductionClasspath` after changing the build or importing the
project into Eclipse. The optional-integration server probe is separate from
the routine build. It needs the pinned mod jars listed in
`verification/optional-integration-mods.json` and the official Forge installer
checked by `verifyOptionalIntegrationForgeInstaller`.
