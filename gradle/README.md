# Build scripts

`build.gradle` sets up the Minecraft target, Java toolchain, development launches
and reproducible jars. The smaller scripts keep related work together:

| Script | Purpose |
| --- | --- |
| `furniture-catalog.gradle` | Normalize and check the existing furniture catalog. |
| `phase-four-upholstery.gradle` | Generate beds, tall chairs and colour-specific recipes. |
| `phase-four-shields.gradle` | Generate shield-chair frames and shield-preserving recipes. |
| `phase-four-lighting.gradle` | Generate candles, twin torches and their models and recipes. |
| `phase-four-metals.gradle` | Generate metal recipes, item models and mining tags. |
| `phase-four-cfm.gradle` | Generate conditional Crayfish chair conversions. |
| `locales.gradle` | Keep retained translations and fill missing names from English. |
| `verification/` | Check resources, identities, workflows and separate runtime probes. |
| `ide/eclipse.gradle` | Prepare production-only Eclipse launches and processed metadata. |
| `ide/development-integrations.gradle` | Prepare verified optional-mod copies for the readable development mappings. |
| `ide/development-mixins.gradle` | Translate published mods' mixin references for development launches. |
| `release/artifacts.gradle` | Audit release jars and their checksums. |
| `release/publishing.gradle` | Publish only the audited, credential-gated Maven bundle. |

Run `updateFurnitureCatalog` after changing a generator or its input templates,
then `check` to verify the resulting files. Runtime probes are separate test mods
and must never be distributed with Iron Age Furniture.
