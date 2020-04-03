package modfest.crystalmodifiers.client;

import modfest.crystalmodifiers.CrystalModifiers;
import modfest.crystalmodifiers.client.gui.CrystalSealerController;
import modfest.crystalmodifiers.client.gui.CrystalSealerScreen;
import modfest.crystalmodifiers.common.block.CrystalSealer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.container.BlockContext;

@Environment(EnvType.CLIENT)
public class CrystalModifiersClient extends CrystalModifiers implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // render layers
        BlockRenderLayerMap.INSTANCE.putBlock(CrystalModifiers.CRYSTAL_ORE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(CrystalModifiers.CRYSTAL_SEALER, RenderLayer.getTranslucent());

        // screen registration
        ScreenProviderRegistry.INSTANCE.registerFactory(CrystalSealer.ID, (syncId, identifier, player, buf) -> new CrystalSealerScreen(new CrystalSealerController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
    }
}
