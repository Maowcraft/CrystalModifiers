package modfest.crystalmodifiers.common.item;

import io.netty.buffer.Unpooled;
import modfest.crystalmodifiers.CrystalModifiers;
import modfest.crystalmodifiers.util.crystal.CrystalType;
import modfest.crystalmodifiers.util.crystal.CrystalTypeUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class CrystalOrb extends Item {
    public CrystalOrb(Settings settings) {
        super(settings.maxCount(1));
    }

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
        tooltip.add(new LiteralText(type.formatting + type.name));
        if (Screen.hasShiftDown()) {
            tooltip.add(new LiteralText(Formatting.YELLOW + "Creates an AoE potion effect cloud."));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!stack.hasTag() || stack.getTag() == null) { stack.setTag(new CompoundTag()); }
        CrystalType type = CrystalTypeUtils.intToType(stack.getTag().getInt("CrystalType"));
        if (type != CrystalType.BROKEN) {
            if (world.isClient()) {
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeInt(type.type);
                passedData.writeBlockPos(user.getBlockPos());
                ClientSidePacketRegistry.INSTANCE.sendToServer(CrystalModifiers.CRYSTAL_ORB_SPAWN_PACKET, passedData);
                if (!user.isCreative()) {
                    user.inventory.removeOne(stack);
                }
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }
}
