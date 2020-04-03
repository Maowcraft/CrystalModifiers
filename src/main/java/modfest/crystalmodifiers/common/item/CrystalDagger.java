package modfest.crystalmodifiers.common.item;

import modfest.crystalmodifiers.util.crystal.CrystalType;
import modfest.crystalmodifiers.util.crystal.CrystalTypeUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class CrystalDagger extends Item {
    public CrystalDagger(Settings settings) {
        super(settings.maxCount(1));
    }

    private static Random rand = new Random();

    @Override
    public boolean hasEnchantmentGlint(ItemStack stack) {
        if (!stack.hasTag() || stack.getTag() == null) { stack.setTag(new CompoundTag()); }
        boolean output = (stack.getTag().getInt("CrystalType") == 0) ? false : true;
        return output;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (!stack.hasTag() || stack.getTag() == null) { stack.setTag(new CompoundTag()); }
        CrystalType type = CrystalTypeUtils.intToType(stack.getTag().getInt("CrystalType"));
        if (type == CrystalType.BROKEN) {
            tooltip.add(new LiteralText(Formatting.DARK_GRAY + "Inactive"));
        } else {
            tooltip.add(new LiteralText(type.formatting + type.name));
        }
        if (Screen.hasShiftDown()) {
            tooltip.add(new LiteralText(Formatting.YELLOW + "Inflicts an effect on an entity when it is hit."));
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!stack.hasTag() || stack.getTag() == null) { stack.setTag(new CompoundTag()); }
        CrystalType type = CrystalTypeUtils.intToType(stack.getTag().getInt("CrystalType"));
        if (type != CrystalType.BROKEN) {
            target.addStatusEffect(new StatusEffectInstance(type.effect, 250));
            return true;
        }
        return false;
    }
}
