package pro.mikey.xray.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.registries.ForgeRegistries;
import pro.mikey.xray.ClientController;
import pro.mikey.xray.Configuration;
import pro.mikey.xray.utils.BlockData;
import pro.mikey.xray.xray.Controller;

import java.util.ArrayList;
import java.util.List;

public class XRayCommand {

    private static final int GOLD_COLOR = 0xFFD700;

    private static final String[] LOOTR_BLOCKS = {
            "lootr:lootr_barrel",
            "lootr:lootr_chest",
            "lootr:lootr_inventory",
            "lootr:lootr_shulker",
            "lootr:lootr_trapped_chest"
    };

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(register());
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("xray")
                .then(Commands.literal("addLootRBlock")
                        .executes(ctx -> addLootRBlocks()))
                .then(Commands.literal("filter")
                        .then(Commands.literal("lootr")
                                .executes(ctx -> toggleFilter("lootr")))
                        .then(Commands.literal("empty")
                                .executes(ctx -> toggleFilter("empty"))));
    }

    private static int toggleFilter(String filterName) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return 0;

        boolean newValue;
        String displayName;

        switch (filterName) {
            case "lootr" -> {
                newValue = !Configuration.general.lootrFilter.get();
                Configuration.general.lootrFilter.set(newValue);
                displayName = "LootR Filter";
            }
            case "empty" -> {
                newValue = !Configuration.general.filterEmptyContainers.get();
                Configuration.general.filterEmptyContainers.set(newValue);
                displayName = "Empty Container Filter";
            }
            default -> { return 0; }
        }

        String status = newValue ? "§aON" : "§cOFF";
        mc.player.displayClientMessage(
                Component.literal("§6[XRay] §f" + displayName + ": " + status),
                false
        );

        // Refresh XRay if active
        if (Controller.isXRayActive()) {
            Controller.requestBlockFinder(true);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int addLootRBlocks() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return 0;

        List<String> added = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        for (String blockName : LOOTR_BLOCKS) {
            ResourceLocation location = new ResourceLocation(blockName);
            Block block = ForgeRegistries.BLOCKS.getValue(location);
            if (block == null) {
                skipped.add(blockName);
                continue;
            }

            // Check if already in store
            if (Controller.getBlockStore().getStoreByReference(blockName) != null) {
                skipped.add(blockName);
                continue;
            }

            String displayName = Component.translatable(block.getDescriptionId()).getString();
            BlockData data = new BlockData(
                    displayName,
                    blockName,
                    GOLD_COLOR,
                    new ItemStack(block, 1),
                    true,
                    Controller.getBlockStore().getStore().size()
            );

            Controller.getBlockStore().put(data);
            added.add(displayName);
        }

        // Save to disk
        ArrayList<BlockData> allBlocks = new ArrayList<>(Controller.getBlockStore().getStore().values());
        ClientController.blockStore.write(allBlocks);

        // Refresh XRay if active
        if (Controller.isXRayActive()) {
            Controller.requestBlockFinder(true);
        }

        if (!added.isEmpty()) {
            mc.player.displayClientMessage(
                    Component.literal("§6[XRay] §aAdded " + added.size() + " LootR blocks: " + String.join(", ", added)),
                    false
            );
        }
        if (!skipped.isEmpty()) {
            mc.player.displayClientMessage(
                    Component.literal("§6[XRay] §eSkipped " + skipped.size() + " (not found or already added): " + String.join(", ", skipped)),
                    false
            );
        }

        return Command.SINGLE_SUCCESS;
    }
}
