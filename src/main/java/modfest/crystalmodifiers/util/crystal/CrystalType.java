package modfest.crystalmodifiers.util.crystal;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Formatting;

public enum CrystalType {
    // for anyone looking to add a custom crystal type, you can probably just mixin this class and insert an enum right here - thanks for wanting to check out the code though!
    BROKEN(Formatting.DARK_GRAY, 0, "Broken", null),
    STRONG(Formatting.BLUE, 1, "Strong", StatusEffects.STRENGTH),
    WEAK(Formatting.RED, 2, "Weak", StatusEffects.WEAKNESS),
    DRAINING(Formatting.RED, 3, "Draining", StatusEffects.INSTANT_DAMAGE),
    ABSORPTIVE(Formatting.BLUE, 4, "Absorptive", StatusEffects.ABSORPTION),
    POISONOUS(Formatting.RED, 5, "Poisonous", StatusEffects.POISON),
    QUICK(Formatting.BLUE, 6, "Quick", StatusEffects.SPEED),
    BLIND(Formatting.RED, 7, "Blind", StatusEffects.BLINDNESS),
    ENERGETIC(Formatting.BLUE, 8, "Energetic", StatusEffects.JUMP_BOOST),
    SLOWED(Formatting.RED, 9, "Slowed", StatusEffects.SLOWNESS),
    AQUATIC(Formatting.BLUE, 10, "Aquatic", StatusEffects.WATER_BREATHING),
    NAUSEOUS(Formatting.RED, 11, "Nauseous", StatusEffects.NAUSEA),
    REGENERATIVE(Formatting.BLUE, 12, "Regenerative", StatusEffects.REGENERATION),
    HUNGRY(Formatting.RED, 13, "Hungry", StatusEffects.HUNGER),
    LUCKY(Formatting.BLUE, 14, "Lucky", StatusEffects.LUCK),
    CHAOTIC(Formatting.DARK_GRAY, 15, "Chaotic", StatusEffects.WITHER),
    HEAVY(Formatting.DARK_GRAY, 16, "Heavy", StatusEffects.SLOW_FALLING),
    LIGHT(Formatting.DARK_GRAY, 17, "Light", StatusEffects.LEVITATION);

    public final Formatting formatting;
    public final int type;
    public final String name;
    public final StatusEffect effect;

    private CrystalType(Formatting formatting, int type, String name, StatusEffect effect) {
        this.formatting = formatting;
        this.type = type;
        this.name = name;
        this.effect = effect;
    }
}