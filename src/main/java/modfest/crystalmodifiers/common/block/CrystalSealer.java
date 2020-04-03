package modfest.crystalmodifiers.common.block;

import modfest.crystalmodifiers.common.block.blockentity.CrystalSealerBlockEntity;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CrystalSealer extends Block implements BlockEntityProvider {
    public static final Identifier ID = new Identifier("crystal_sealer");

    public CrystalSealer(Settings settings) {
        super(settings.nonOpaque());
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new CrystalSealerBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.PASS;
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity != null && entity instanceof CrystalSealerBlockEntity) {
            ContainerProviderRegistry.INSTANCE.openContainer(CrystalSealer.ID, player, (packetByteBuf -> packetByteBuf.writeBlockPos(pos)));
        }
        return ActionResult.SUCCESS;
    }
}
