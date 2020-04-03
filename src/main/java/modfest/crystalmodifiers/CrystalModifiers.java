package modfest.crystalmodifiers;

import modfest.crystalmodifiers.client.gui.CrystalSealerController;
import modfest.crystalmodifiers.common.block.CrystalSealer;
import modfest.crystalmodifiers.common.block.CrystallicPillar;
import modfest.crystalmodifiers.common.block.blockentity.CrystalSealerBlockEntity;
import modfest.crystalmodifiers.common.item.Crystal;
import modfest.crystalmodifiers.common.item.CrystalDagger;
import modfest.crystalmodifiers.common.item.CrystalOrb;
import modfest.crystalmodifiers.common.item.CrystalStrike;
import modfest.crystalmodifiers.util.crystal.CrystalTypeUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class CrystalModifiers implements ModInitializer {
    public static final String MODID = "crystalmodifiers";

    // packet identifiers
    public static final Identifier CRYSTAL_TYPE_CHANGE_PACKET = new Identifier(CrystalModifiers.MODID, "crystal_type_packet");
    public static final Identifier CRYSTAL_ORB_SPAWN_PACKET = new Identifier(CrystalModifiers.MODID, "crystal_spawn_packet");
    public static final Identifier CRYSTAL_STRIKE_SPAWN_PACKET = new Identifier(CrystalModifiers.MODID, "crystal_lightning_packet");

    // creative tab
    public static final ItemGroup TAB = FabricItemGroupBuilder.build(
            new Identifier(CrystalModifiers.MODID, "tab"),
            () -> new ItemStack(CrystalModifiers.CRYSTAL_ORE));

    // block creation
    public static final Block CRYSTAL_ORE = new Block(FabricBlockSettings.of(Material.STONE).hardness(3).nonOpaque().slipperiness(1.0f).build());
    public static final Block CRYSTAL_SEALER = new CrystalSealer(FabricBlockSettings.of(Material.METAL).hardness(2).build());
    public static final Block CRYSTALLIC_PILLAR = new CrystallicPillar(FabricBlockSettings.of(Material.METAL).hardness(1).build());

    // item creation
    public static final Item CRYSTAL = new Crystal(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item CRYSTAL_DAGGER = new CrystalDagger(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item CRYSTAL_ORB = new CrystalOrb(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item CRYSTAL_STRIKE = new CrystalStrike(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item CRYSTAL_REFRACTION_LENS = new Item(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item CRYSTAL_ATTRACTION_LENS = new Item(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item CRYSTAL_REVERSION_LENS = new Item(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item CRYSTAL_PLATING = new Item(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item CRYSTALIRON_ALLOY = new Item(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item STRIKEUM_NUGGET = new Item(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item UNACTIVATED_STRIKEUM_INGOT = new Item(new Item.Settings().group(CrystalModifiers.TAB));
    public static final Item ACTIVATED_STRIKEUM_INGOT = new Item(new Item.Settings().group(CrystalModifiers.TAB));

    // blockentity creation
    public static BlockEntityType<CrystalSealerBlockEntity> CRYSTAL_SEALER_ENTITY;

    // ore generation
    private void handleBiome(Biome biome) {
        biome.addFeature(
                GenerationStep.Feature.UNDERGROUND_ORES,
                Feature.ORE.configure(
                        new OreFeatureConfig(
                                OreFeatureConfig.Target.NATURAL_STONE,
                                CrystalModifiers.CRYSTAL_ORE.getDefaultState(),
                                8
                        )).createDecoratedFeature(
                                Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
                                        8,
                                    0,
                                    0,
                                    64
                                ))));
    }

    @Override
    public void onInitialize() {
        // server packet registration
        ServerSidePacketRegistry.INSTANCE.register(CRYSTAL_TYPE_CHANGE_PACKET, (packetContext, attachedData) -> {
            BlockPos pos = attachedData.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                BlockEntity entity = packetContext.getPlayer().world.getBlockEntity(pos);
                if (entity instanceof CrystalSealerBlockEntity) {
                    ItemStack crystal = ((CrystalSealerBlockEntity) entity).getItems().get(0);
                    if (((CrystalSealerBlockEntity) entity).isValidInvStack(0, crystal)) {
                        CrystalTypeUtils.sealCrystal(crystal);
                        packetContext.getPlayer().damage(DamageSource.MAGIC, 0.1f);
                    }
                }
            });
        });
        ServerSidePacketRegistry.INSTANCE.register(CRYSTAL_ORB_SPAWN_PACKET, (packetContext, attachedData) -> {
            int crystalType = attachedData.readInt();
            BlockPos pos = attachedData.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                World world = packetContext.getPlayer().world;
                AreaEffectCloudEntity entity = new AreaEffectCloudEntity(world, pos.getX(), pos.getY(), pos.getZ());
                entity.setPotion(new Potion(new StatusEffectInstance(CrystalTypeUtils.intToType(crystalType).effect, 200)));
                world.spawnEntity(entity);
            });
        });
        ServerSidePacketRegistry.INSTANCE.register(CRYSTAL_STRIKE_SPAWN_PACKET, (packetContext, attachedData) -> {
            int posX = attachedData.readInt();
            int posY = attachedData.readInt();
            int posZ = attachedData.readInt();
            BlockPos pos = new BlockPos(posX, posY, posZ);
            packetContext.getTaskQueue().execute(() -> {
                World world = packetContext.getPlayer().world;
                TntEntity tnt1 = new TntEntity(world, posX, posY + 30, posZ, packetContext.getPlayer());
                TntEntity tnt2 = new TntEntity(world, posX + 5, posY + 30, posZ, packetContext.getPlayer());
                world.spawnEntity(tnt1);
                world.spawnEntity(tnt2);
            });
        });

        // block registration
        Registry.register(Registry.BLOCK, new Identifier(CrystalModifiers.MODID, "crystal_ore"), CRYSTAL_ORE);
        Registry.register(Registry.BLOCK, new Identifier(CrystalModifiers.MODID, "crystal_sealer"), CRYSTAL_SEALER);
        Registry.register(Registry.BLOCK, new Identifier(CrystalModifiers.MODID, "crystallic_pillar"), CRYSTALLIC_PILLAR);

        // blockitem registration
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystal_ore"), new BlockItem(CRYSTAL_ORE, new Item.Settings().group(CrystalModifiers.TAB)));
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystal_sealer"), new BlockItem(CRYSTAL_SEALER, new Item.Settings().group(CrystalModifiers.TAB)));
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystallic_pillar"), new BlockItem(CRYSTALLIC_PILLAR, new Item.Settings().group(CrystalModifiers.TAB)));

        // blockentity registration
        CRYSTAL_SEALER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CrystalModifiers.MODID + ":crystal_sealer", BlockEntityType.Builder.create(CrystalSealerBlockEntity::new, CRYSTAL_SEALER).build(null));

        // item registration
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystal"), CRYSTAL);
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystal_dagger"), CRYSTAL_DAGGER);
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystal_orb"), CRYSTAL_ORB);
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystal_strike"), CRYSTAL_STRIKE);
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystal_refraction_lens"), CRYSTAL_REFRACTION_LENS);
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystal_attraction_lens"), CRYSTAL_ATTRACTION_LENS);
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystal_reversion_lens"), CRYSTAL_REVERSION_LENS);
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystal_plating"), CRYSTAL_PLATING);
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "crystaliron_alloy"), CRYSTALIRON_ALLOY);
        //////////// unused ////////////
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "strikeum_nugget"), STRIKEUM_NUGGET);
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "unactivated_strikeum_ingot"), UNACTIVATED_STRIKEUM_INGOT);
        Registry.register(Registry.ITEM, new Identifier(CrystalModifiers.MODID, "activated_strikeum_ingot"), ACTIVATED_STRIKEUM_INGOT);

        // container registration
        ContainerProviderRegistry.INSTANCE.registerFactory(CrystalSealer.ID, (syncId, id, player, buf) -> new CrystalSealerController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));

        // ore generation registration
        Registry.BIOME.forEach(this::handleBiome);
        RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> handleBiome(biome));
    }
}
