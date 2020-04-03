package modfest.crystalmodifiers.client.gui;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.netty.buffer.Unpooled;
import modfest.crystalmodifiers.CrystalModifiers;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.PacketByteBuf;

public class CrystalSealerController extends CottonCraftingController {
    public CrystalSealerController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(75, 125);

        root.add(new WLabel(new TranslatableText("gui.title.crystalmodifiers.crystal_sealer")), 0, 0, 4, 1);

        root.add(WItemSlot.of(getBlockInventory(context), 0), root.getX() / 2, root.getY() * 2 - root.getX() + 1);


        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            WButton button = new WButton(new TranslatableText("gui.button.crystalmodifiers.seal"));
            button.setOnClick(() -> {
                context.run((world, pos) -> {
                    if (world.isClient()) {
                        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                        passedData.writeBlockPos(pos);
                        ClientSidePacketRegistry.INSTANCE.sendToServer(CrystalModifiers.CRYSTAL_TYPE_CHANGE_PACKET, passedData);
                    }
                });
            });
            root.add(button, root.getX() / 2 + 4, root.getY() * 2 - root.getX() + 1, 3, 1);
        }

        root.add(this.createPlayerInventoryPanel(), root.getX() * 2, root.getY() * 2 + 3);

        root.validate(this);
    }
}
