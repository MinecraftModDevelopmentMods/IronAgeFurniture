[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.mcmoddev.com)

# Iron Age Furniture

Furniture from the iron age of men, for Minecraft 1.10.2 and Forge 12.18.3.2511.

The current development version is `0.3.0.110021`. This Phase 3 candidate is unreleased; the latest published Minecraft 1.10.2 build remains `0.2.0.5`.

## Development

The project uses ForgeGradle 7.0.34 and Gradle 9.6.1. Run Gradle itself on Java 17; the build compiles Java 8 bytecode with a Java 8 toolchain.

```text
./gradlew clean check build javadoc verifyReleaseArtifacts writeReleaseChecksums
./gradlew prepareEclipse verifyEclipseProductionClasspath
```

Release publication is manual and gated. A branch push or tag alone does not publish artifacts.
