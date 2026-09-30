# Upgrading older worlds

Back up a world before opening it with a newer Minecraft or IronAgeFurniture
version. Minecraft and Forge may need to rewrite chunks and registry data on
the first load.

The Forge 1.12.2 Phase 4 candidate has been tested with disposable copies of
worlds created by the 1.10.2 and 1.12.2 Phase 2 releases, both Phase 3
multicolour builds, and a 1.10.2 Phase 4 build. These worlds loaded directly
without an intermediate Minecraft version and passed a second load after
saving. The original source worlds were not launched or modified.

The Sylvester Phase 2 world was also checked on a disposable copy. Its sampled
birch seating and red padded bench survived, and its historical Natura redwood
registry names are available again when Natura is installed. No redwood
furniture was placed in that particular source world, so the test checks the
registry contract rather than a placed redwood block. Other mods from the
original server were not carried into this focused furniture test.

In the multicolour fixtures, both padded bench forms kept all sixteen placed
upholstery colours and all sixteen stored item variants. The older red-only
fixtures kept their red benches and ordinary seating. The 1.10.2 Phase 2
fixture also retained two stored red padded-bench items. The 1.12.2 Phase 2
fixture did not contain stored padded-bench items, so that particular path is
not covered by its world test.

On a direct 1.10.2 upgrade, Forge may report old vanilla sound IDs during the
first load. Those warnings did not involve IronAgeFurniture, and the tested
worlds reloaded without them. A modded world may still need its other mods or
their own migration steps; these fixture results do not certify a whole
modpack. CFM chair recovery and its default-off forced-conversion option have
separate packaged-world tests.
