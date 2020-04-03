package modfest.crystalmodifiers.util.crystal;

import modfest.crystalmodifiers.CrystalModifiers;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public class CrystalTypeUtils {
    private static Random rand = new Random();

    public static void sealCrystal(ItemStack stack) {
        if (stack.getItem() == CrystalModifiers.CRYSTAL || stack.getItem() == CrystalModifiers.CRYSTAL_DAGGER || stack.getItem() == CrystalModifiers.CRYSTAL_ORB) {
            if (!stack.hasTag() || stack.getTag() == null) { stack.setTag(new CompoundTag()); }
            CrystalType type = CrystalTypeUtils.intToType(stack.getTag().getInt("CrystalType"));
            if (type == CrystalType.BROKEN) {
                int amount = 0;
                for (CrystalType type1 : CrystalType.values()) { amount++; }
                int newType = rand.nextInt(amount);
                stack.getTag().putInt("CrystalType", newType);
            }
        }
    }

    public static CrystalType intToType(int id) {
        for (CrystalType type : CrystalType.values()) { if (type.type == id) return type; }
        return CrystalType.BROKEN;
    }
}
