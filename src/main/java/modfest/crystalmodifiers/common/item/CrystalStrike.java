package modfest.crystalmodifiers.common.item;

import io.netty.buffer.Unpooled;
import modfest.crystalmodifiers.CrystalModifiers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;


public class CrystalStrike extends Item {
    public CrystalStrike(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getStack().hasTag() || context.getStack().getTag() == null) { context.getStack().setTag(new CompoundTag()); }
        context.getStack().getTag().putInt("SPosX", context.getBlockPos().getX());
        context.getStack().getTag().putInt("SPosY", context.getBlockPos().getY());
        context.getStack().getTag().putInt("SPosZ", context.getBlockPos().getZ());
        if (context.getPlayer() != null) {
            context.getPlayer().addChatMessage(new LiteralText("Saved Crystal Strike coordinates - Shift-click to strike location."), true);
        }
        return ActionResult.SUCCESS;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new LiteralText(Formatting.YELLOW + "Causes 2 TNT to spawn a user-set spot, has an individual value per item."));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            if (!stack.hasTag() || stack.getTag() == null) { stack.setTag(new CompoundTag()); }
                if (world.isClient()) {
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeInt(stack.getTag().getInt("SPosX"));
                    passedData.writeInt(stack.getTag().getInt("SPosY"));
                    passedData.writeInt(stack.getTag().getInt("SPosZ"));
                    ClientSidePacketRegistry.INSTANCE.sendToServer(CrystalModifiers.CRYSTAL_STRIKE_SPAWN_PACKET, passedData);
                    if (!user.isCreative()) {
                        user.inventory.removeOne(stack);
                    }
                    return TypedActionResult.pass(stack);
                }
        }
        return TypedActionResult.fail(stack);
    }
}
