# Optional integration tests

The 26.3 core contains vanilla furniture. Biomes O' Plenty furniture belongs to the separate BOP add-on; do not add its wood registrations or resources to the core catalog.

## Tested Phase 4 dependencies

- Biomes O' Plenty `26.3.0.0.15`, GlitchCore `26.3.0.0.5` and TerraBlender `26.3.0.0.9`, with the matching `0.4.0.2603002` add-on.
- Mineralogy `6.1.4.2603002` and OreSpawn `4.1.0.2603002` for the full integration stack and rock-salt lamp support.

Packaged server probes pass with the BOP stack on NeoForge `.77` and `.83`, and with the full integration stack on `.83`. They include furniture and candle storage, lighting transitions and exact-item drops. The `.83` BOP client also checks visible furniture models and shared shield-chair renderers after synchronization.

The old BOP `.2` stack fails independently of Iron Age Furniture with current dependencies. It is not a supported minimum. Loader-rejection tests check that older BOP, GlitchCore and TerraBlender combinations fail with dependency errors rather than proceeding to world startup.

Base Metals and legacy CFM do not have a verified live 26.3 integration in this candidate. Their absence, conditional recipes and saved-data contracts are tested, but that is not a claim of live provider compatibility. Refurbished Furniture is not treated as legacy CFM. BYG and Immersive Engineering furniture are not supplied by this target.

## Running the probes

The development probes are a separate test mod. Build them with `phaseFourProbeJar` and install them only in an external disposable game directory alongside the exact packaged candidate and checksum-verified dependency jars. They must not enter a release artifact or a normal Eclipse launch. Keep logs and test worlds outside the repository.

Check explicit pass/fail markers and complete logs, not only the process exit code. World-upgrade tests need a fingerprinted source, exact first-load comparisons, a second load, and a final source-hash check. No test should launch a protected original world.

The local candidates pass the final build and repeat-build gates. The upgrade guide distinguishes the verified furniture scope from unsupported historical modpack content; publication remains a separate step.
