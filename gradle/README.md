# Build logic

The main `build.gradle` configures Forge 1.16.5, the Java 8 compiler, development
launches, processed resources and the three release jars. Longer checks and
optional workflows live in smaller scripts:

| Script | Purpose |
| --- | --- |
| `furniture-catalog.gradle` | Verify the committed furniture resources or regenerate them explicitly. |
| `phase-four-shields.gradle` | Generate and check removable shield-chair frames and crafting recipes. |
| `phase-four-upholstery.gradle` | Generate and check colour-specific beds, tall chairs, models and crafting recipes. |
| `phase-four-lighting.gradle` | Generate and check candles, multi-candle/twin-torch sconces, waterlogged states and tallow recipes. |
| `phase-four-cfm.gradle` | Generate and check eight conditional CFM chair conversion recipes and their exact ingredients. |
| `phase-four-metals.gradle` | Generate and check gold and conditional Base Metals recipes and frame names. |
| `locales.gradle` | Combine retained translations with current English fallback names for every supported language. |
| `verification/optional-integrations.gradle` | Check pinned dependency jars and launch packaged-server integration tests. |
| `verification/phase-four-runtime.gradle` | Build the isolated furniture test mod and run it on a disposable server. |
| `verification/project-audits.gradle` | Check resources, metadata, namespaces, workflow pins and tracked files. |
| `release/artifacts.gradle` | Audit the main, sources and Javadoc jars, then checksum the release bundle. |
| `release/publishing.gradle` | Configure Maven uploads and require publication credentials. |
| `ide/eclipse.gradle` | Generate and check the nested Buildship project and production launches. |
| `ide/development-mixins.gradle` | Translate published-mod mixin references for Eclipse and Gradle development launches. |
| `ide/development-integrations.gradle` | Keep original optional-mod downloads and prepare renamed copies for development. |

For ordinary validation, run `gradlew.bat clean check build javadoc
verifyReleaseArtifacts verifyReleaseChecksums`. After changing the project
setup, run `gradlew.bat prepareEclipse verifyEclipseProductionClasspath`.

For optional-mod testing in Eclipse, put the supported published jars in
`run/mods` before preparing the project, then select `runClientCompatibility`.
The ordinary `runClient` uses a separate `run-core` folder and loads IAF alone.
The compatibility profile retains the existing `run` saves and settings; neither
profile copies or upgrades the other's worlds. Keep `run-core/mods` empty.
Both profile names are also Gradle tasks. To use other separate game folders,
set `-PrunDirectory=<core folder>` and
`-PcompatibilityRunDirectory=<compatibility folder>` before regenerating launches.

Preparation preserves verified originals
in the local Gradle directory and replaces only those installed mods with
development-named copies. Do not use these copies in a normal Minecraft profile;
use the original downloads there instead. Removing a mod from `run/mods` does
not reinstall it on the next preparation.

The integration probes are separate because they need the exact optional-mod
jars listed in `verification/optional-integration-mods.json` and a verified
Forge installer. Local builds and Eclipse imports never publish a release.

The furniture probe checks placement, mining, pick block, removable shields,
falling chairs and saved data. Run `phaseFourProbeJar` to build it, then use
`runPhaseFourServer` with `-PphaseFourServerDir` pointing to a disposable installed
Forge 1.16.5 server. Running it twice checks the decorated chair and empty frame
saved by the first run. Never point this test at a world you want to keep.

It also mines standalone and sconce-mounted lava lamps using ordinary,
Efficiency-enchanted and Silk Touch picks, then repeats the test in Creative.
This checks intact-lamp recovery, empty-sconce drops and fire on shattering.
The server probe also covers bare-hand breaking and glass-vial mining speed.
On a disposable in-world client, add `-Diaf.probe.inWorldClient=true` and
`-Diaf.probe.lampMiningClient=true` to test the real client controller and hit
particles, including the final swing that removes the lamp. It checks all lamp
facings, glow/red variants, lava sconces, hands, pickaxes, Silk Touch and Creative.

The same test jar can check baked item and block models on a packaged client.
It closes the test client after checking its models and writes
`phase-four-client-pass.properties`. The bed and tall-chair checks also cover all
sixteen colours, every facing, wet/dry placement, whole-structure cleanup and
real Forge harvesting. The client checks both inventory wool textures and every
registered blockstate. Candle tests cover all floor/wall facings, water entry,
draining, relighting, support loss, real harvesting and tallow output counts.
The new sconces also check content insertion/removal, Creative consumption,
flame orientation and saved-state reloads. Metal tests cover every known metal,
all registered sconce states, real harvesting, light changes and saved-frame
reloads. Saved-world checks run separately so synthetic furniture tests do not
overwrite the imported cells they are meant to inspect.

`metalContractFixtureJar` builds a separate synthetic tag provider. It uses
vanilla gold as a stand-in and deliberately omits one metal to test incomplete
catalogs. It must never be included in a published artifact or a normal modpack.
It verifies the dormant Base Metals contract, not a real Base Metals release.

For published Base Metals tests, `verification/base-metals-mods.json` pins
Base Metals 3.1.0.116051 and its OreSpawn 4.1.0.116051 dependency. Supply the
downloaded jars with `-PbaseMetalsModsDir=<directory>` and run
`verifyBaseMetalsMods` to check their hashes, metal tags and actual model textures.
Add those verified jars to the disposable server's mods folder and use
`runPhaseFourServer -PphaseFourServerDir=<directory> -PphaseFourLiveBaseMetals=true`.
The live probe requires all 21 additional metals and checks their real nugget
recipes, metal-dependent mining speed and blast resistance, drops and light changes.

For in-world client checks, put a copy of that saved world in the disposable
client's `saves/world` folder and launch with `-Diaf.probe.liveBaseMetalsClient=true`.
The probe loads the copy before checking textures, so it uses the synchronized
Base Metals tags rather than the main menu's iron fallback. Set the world's
`baseMetalsIntegration` option to false and add
`-Diaf.probe.baseMetalsDisabled=true` to test the disabled configuration.

`-PphaseFourLegacyMetals=true` reads a hash-checked 1.12 saved fixture and checks
all 23 metals, four facings and 66 pre-Nether sconce IDs, plus player, Ender
Chest, container, nested and dropped frame items. A second load checks that the
converted block properties and item data persist.

For a general upgrade fixture, place an independently prepared
`saved-world-expectations.json` beside the disposable server and use
`-PphaseFourSavedWorld=true`. The probe reads the saved chunks and player files
through Minecraft's normal load path, checking registry names, shared block
properties, shield data and stored or nested stacks. It waits for queued chunk
conversions before checking the result. Repeat with
`-PphaseFourSavedWorldReload=true` to check persistence after normal redstone
updates and neighbouring water have settled. The expected data must come from
the source world, not from a copy that has already been upgraded.

The translation overrides in `locales/` come from the released 1.12 language
files. `updateLocales` explicitly regenerates the complete 1.16 JSON files;
ordinary builds only verify them. To import revised legacy translations, run
`tools/import-legacy-locales.ps1` with `-LegacyLanguageDirectory` pointing to the
reviewed `.lang` directory, then run `updateLocales` and inspect the changes.

The optional-mod manifest also pins Mineralogy and its OreSpawn dependency.
With them installed, the probe checks rock-salt lamp insertion, removal,
underwater light, harvesting and reloads in all floor/wall facings. Without
Mineralogy, neither rock-salt sconce is registered.

CFM checks use a saved world containing all six wooden chairs in every facing,
named and enchanted stacks, nested containers, dropped items, a player inventory
and an Ender Chest. `-PphaseFourCfmMode=seed` creates this fixture with the pinned
CFM jar installed. Run `unchanged` against a copy to check the default-off policy,
`converted` to check opted-in replacement, and `recovered` against a copy without
CFM to check removal recovery. Running the converted copies again checks that
conversion does not repeat. These modes change only the disposable probe's
configuration; the normal mod never reads probe properties.

The `legacy110` and `legacy112` modes check saved pre-flattening chair fixtures:
all six woods, their original facings, stored items, and dropped or nested
stacks. Use a verified copy of the corresponding fixture, not a newly generated
world. The recovery modes accept Forge's missing-mod prompt only inside the
disposable test server; normal play still asks for confirmation.
