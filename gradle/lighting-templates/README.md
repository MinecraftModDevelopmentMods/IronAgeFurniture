# Candle and twin-torch models

These are Iron Age Furniture's candle and twin-torch models by FoxNekitsune, carried forward
from the released Forge 1.12 branch under the project's LGPL-2.1 licence.
They include the smaller inventory model and its raised first-person flame.

`phase-four-lighting.gradle` converts the texture references to Minecraft 1.14
paths and generates lit/unlit, floor/wall and waterlogged blockstates. Candle
sconces keep the original one-to-four-candle geometry and wick positions. The
models continue to use vanilla iron and fire textures; no third-party assets
are included. Normal builds only check the output. Use
`updatePhaseFourLightingCatalog` when deliberately changing these models.
