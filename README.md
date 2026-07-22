# Iron Age Furniture
A mod for adding Iron Age Furniture to Minecraft

[![Discord Badge](https://img.shields.io/badge/Discord-16181C?style=for-the-badge&logo=discord&logoColor=5865F2)](https://discord.moddev.zone)
[![CurseForge Badge](https://img.shields.io/badge/CurseForge-16181C?style=for-the-badge&logo=curseforge&logoColor=FF784D)](https://www.curseforge.com/minecraft/mc-mods/ironagefurniture)
[![Github Badge](https://img.shields.io/badge/GitHub-16181C?style=for-the-badge&logo=github&logoColor=BBDDE5)](https://github.com/minecraftmoddevelopmentmods/ironagefurniture)

## CLI development setup

Install a JDK 17. The Gradle wrapper runs on Java 17 while the build's Java
toolchain compiles the Minecraft 1.10.2 mod for Java 8.

On Windows:

```powershell
.\gradlew.bat eclipse genEclipseRuns
.\gradlew.bat build
```

On Linux or macOS:

```bash
./gradlew eclipse genEclipseRuns
./gradlew build
```

`setupDecompWorkspace` was a ForgeGradle 2 task and is not used by this
ForgeGradle 7 build. The first command can take several minutes while the
Minecraft Mavenizer prepares the legacy Forge dependency.
