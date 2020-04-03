package modfest.crystalmodifiers.common.block.blockentity;

import modfest.crystalmodifiers.CrystalModifiers;
import modfest.crystalmodifiers.common.item.Crystal;
import modfest.crystalmodifiers.util.inventory.ImplementedInventory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;

public class CrystalSealerBlockEntity extends BlockEntity implements ImplementedInventory {
    public CrystalSealerBlockEntity() {
        super(CrystalModifiers.CRYSTAL_SEALER_ENTITY);
    }

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        Inventories.fromTag(tag,items);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag,items);
        return super.toTag(tag);
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        return pos.isWithinDistance(player.getBlockPos(), 4.5);
    }

    @Override
    public boolean isValidInvStack(int slot, ItemStack stack) {
        return stack.getItem() == CrystalModifiers.CRYSTAL || stack.getItem() == CrystalModifiers.CRYSTAL_DAGGER || stack.getItem() == CrystalModifiers.CRYSTAL_ORB;
    }
}
