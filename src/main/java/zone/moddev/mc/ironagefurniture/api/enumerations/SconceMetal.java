package zone.moddev.mc.ironagefurniture.api.enumerations;

import net.minecraft.util.IStringSerializable;

/** Stable names and legacy item numbers are shared with the 1.10/1.12 saves. */
public enum SconceMetal implements IStringSerializable {
    IRON("iron", 8, 8), GOLD("gold", 1, 1), ADAMANTINE("adamantine", 12, 100),
    ANTIMONY("antimony", 1, 1), AQUARIUM("aquarium", 4, 4), BISMUTH("bismuth", 1, 1),
    BRASS("brass", 4, 4), BRONZE("bronze", 8, 8), COLDIRON("coldiron", 7, 7),
    COPPER("copper", 4, 4), CUPRONICKEL("cupronickel", 6, 6), ELECTRUM("electrum", 5, 5),
    INVAR("invar", 9, 9), LEAD("lead", 1, 1), MITHRIL("mithril", 9, 9), NICKEL("nickel", 4, 4),
    PEWTER("pewter", 1, 1), PLATINUM("platinum", 3, 3), SILVER("silver", 5, 5),
    STARSTEEL("starsteel", 10, 25), STEEL("steel", 8, 8), TIN("tin", 3, 3), ZINC("zinc", 1, 1);

    private final String name;
    private final int hardness, strength;
    SconceMetal(String name, int hardness, int strength) { this.name = name; this.hardness = hardness; this.strength = strength; }
    @Override public String getName() { return name; }
    public float hardness(float iron) { return Math.max(0.1F, iron * hardness / 8F); }
    public float resistance(float iron) { return Math.max(1F, iron * strength / 8F); }
    public int harvestLevel() { return this == GOLD ? 0 : 1; }
    public boolean isBaseMetal() { return this != IRON && this != GOLD; }
    public static SconceMetal byName(String name) {
        for (SconceMetal metal : values()) if (metal.name.equals(name)) return metal;
        return IRON;
    }
    public static SconceMetal byLegacyMetadata(int number) {
        return number >= 0 && number < values().length ? values()[number] : IRON;
    }
}
