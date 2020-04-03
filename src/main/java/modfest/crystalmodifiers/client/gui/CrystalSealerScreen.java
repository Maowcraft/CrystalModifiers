package modfest.crystalmodifiers.client.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.netty.buffer.Unpooled;
import modfest.crystalmodifiers.CrystalModifiers;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.PacketByteBuf;

public class CrystalSealerScreen extends CottonInventoryScreen<CrystalSealerController> {
    public CrystalSealerScreen(CrystalSealerController container, PlayerEntity player) {
        super(container, player);
    }
}
