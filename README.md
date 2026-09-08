[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.mcmoddev.com)

# Iron Age Furniture

Furniture from the iron age of men, for Minecraft 1.10.2 and Forge 12.18.3.2511.

The current development version is `0.3.0.110021`. This Phase 3 candidate is unreleased; the latest published Minecraft 1.10.2 build remains `0.2.0.5`.

## Development

The project uses ForgeGradle 7.0.34 and Gradle 9.6.1. Run Gradle itself on Java 17; the build compiles Java 8 bytecode with a Java 8 toolchain.

The maintained Java package is `zone.moddev.mc.ironagefurniture`, and the Maven coordinate for this candidate is `zone.moddev.mc:iron-age-furniture:0.3.0.110021`.

Power Advantage remains optional at runtime. Until its migrated Maven artifact can be published,
the build compiles against the deobfuscated development jar produced from pinned Power Advantage
commit `88e9818b4b7011a430436b40367fb1609073875b`. Build that sibling checkout with `deobfJar`, or pass
its exact jar as `-PpowerAdvantageDeobfJar=<path>`; the build verifies the expected API and SHA-256.
At runtime IAF selects that typed API only when it is present in the loaded Power Advantage source.
The published Power Advantage 2.3.0 binary surface is supported by a small, isolated legacy adapter
until the API release is available; its CurseMaven file is used only for verification and never enters
IAF's compile, runtime, Maven, or release artifacts.

```text
./gradlew clean check build javadoc verifyReleaseArtifacts writeReleaseChecksums
./gradlew prepareEclipse verifyEclipseProductionClasspath
```

Release publication is manual and gated. A branch push or tag alone does not publish artifacts.
