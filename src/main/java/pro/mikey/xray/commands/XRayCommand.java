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

    // Furniture Refurbished - drawers
    private static final String[] FURNITURE_DRAWERS = {
            "refurbished_furniture:oak_drawer", "refurbished_furniture:spruce_drawer",
            "refurbished_furniture:birch_drawer", "refurbished_furniture:jungle_drawer",
            "refurbished_furniture:acacia_drawer", "refurbished_furniture:dark_oak_drawer",
            "refurbished_furniture:crimson_drawer", "refurbished_furniture:warped_drawer",
            "refurbished_furniture:mangrove_drawer", "refurbished_furniture:cherry_drawer"
    };

    // Furniture Refurbished - storage cabinets
    private static final String[] FURNITURE_CABINETS = {
            "refurbished_furniture:oak_storage_cabinet", "refurbished_furniture:spruce_storage_cabinet",
            "refurbished_furniture:birch_storage_cabinet", "refurbished_furniture:jungle_storage_cabinet",
            "refurbished_furniture:acacia_storage_cabinet", "refurbished_furniture:dark_oak_storage_cabinet",
            "refurbished_furniture:crimson_storage_cabinet", "refurbished_furniture:warped_storage_cabinet",
            "refurbished_furniture:mangrove_storage_cabinet", "refurbished_furniture:cherry_storage_cabinet"
    };

    // Furniture Refurbished - kitchen drawers
    private static final String[] FURNITURE_KITCHEN_DRAWERS = {
            "refurbished_furniture:oak_kitchen_drawer", "refurbished_furniture:spruce_kitchen_drawer",
            "refurbished_furniture:birch_kitchen_drawer", "refurbished_furniture:jungle_kitchen_drawer",
            "refurbished_furniture:acacia_kitchen_drawer", "refurbished_furniture:dark_oak_kitchen_drawer",
            "refurbished_furniture:crimson_kitchen_drawer", "refurbished_furniture:warped_kitchen_drawer",
            "refurbished_furniture:mangrove_kitchen_drawer", "refurbished_furniture:cherry_kitchen_drawer",
            "refurbished_furniture:white_kitchen_drawer", "refurbished_furniture:orange_kitchen_drawer",
            "refurbished_furniture:magenta_kitchen_drawer", "refurbished_furniture:light_blue_kitchen_drawer",
            "refurbished_furniture:yellow_kitchen_drawer", "refurbished_furniture:lime_kitchen_drawer",
            "refurbished_furniture:pink_kitchen_drawer", "refurbished_furniture:gray_kitchen_drawer",
            "refurbished_furniture:light_gray_kitchen_drawer", "refurbished_furniture:cyan_kitchen_drawer",
            "refurbished_furniture:purple_kitchen_drawer", "refurbished_furniture:blue_kitchen_drawer",
            "refurbished_furniture:brown_kitchen_drawer", "refurbished_furniture:green_kitchen_drawer",
            "refurbished_furniture:red_kitchen_drawer", "refurbished_furniture:black_kitchen_drawer"
    };

    // Furniture Refurbished - stoves
    private static final String[] FURNITURE_STOVES = {
            "refurbished_furniture:dark_stove", "refurbished_furniture:light_stove"
    };

    // Furniture Refurbished - freezers
    private static final String[] FURNITURE_FREEZERS = {
            "refurbished_furniture:dark_freezer", "refurbished_furniture:light_freezer"
    };

    // Furniture Refurbished - mail boxes
    private static final String[] FURNITURE_MAILBOXES = {
            "refurbished_furniture:oak_mail_box", "refurbished_furniture:spruce_mail_box",
            "refurbished_furniture:birch_mail_box", "refurbished_furniture:jungle_mail_box",
            "refurbished_furniture:acacia_mail_box", "refurbished_furniture:dark_oak_mail_box",
            "refurbished_furniture:crimson_mail_box", "refurbished_furniture:warped_mail_box",
            "refurbished_furniture:mangrove_mail_box", "refurbished_furniture:cherry_mail_box"
    };

    // Doomsday Decoration - luggage
    private static final String[] DOOMSDAY_LUGGAGE = {
            "doomsday_decoration:luggage", "doomsday_decoration:blackluggage",
            "doomsday_decoration:blueluggage", "doomsday_decoration:greenluggage",
            "doomsday_decoration:greyluggage", "doomsday_decoration:khakiluggage",
            "doomsday_decoration:redluggage"
    };

    // Doomsday Decoration - shelves
    private static final String[] DOOMSDAY_SHELVES = {
            "doomsday_decoration:shelf", "doomsday_decoration:shelf_2",
            "doomsday_decoration:shelf_3", "doomsday_decoration:shelf_4"
    };

    // Doomsday Decoration - trash cans
    private static final String[] DOOMSDAY_TRASH = {
            "doomsday_decoration:trashcan", "doomsday_decoration:trashcan_2",
            "doomsday_decoration:trashcan_3", "doomsday_decoration:trashcan_4",
            "doomsday_decoration:indoorgarbagebin"
    };

    // Doomsday Decoration - body bags
    private static final String[] DOOMSDAY_BODIES = {
            "doomsday_decoration:bodybag"
    };

    // Zombie Extreme - trash bucket
    private static final String[] ZOMBIE_TRASH = {
            "zombie_extreme:trash_bucket"
    };

    // Apocalypse Now - washing machine
    private static final String[] APOCALYPSE_WASHER = {
            "apocalypsenow:washing_machine"
    };

    private static final int FURNITURE_COLOR = 0xCD853F;    // peru
    private static final int DOOMSDAY_COLOR = 0x708090;     // slate gray
    private static final int ZOMBIE_COLOR = 0x32CD32;       // lime green
    private static final int APOCALYPSE_COLOR = 0xDC143C;   // crimson

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("xray")
                .then(Commands.literal("addLootRBlock")
                        .executes(ctx -> addLootRBlocks()))
                .then(Commands.literal("addMRYH")
                        .executes(ctx -> addMRYHBlocks()))
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

    private static int addMRYHBlocks() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return 0;

        int added = 0;
        int skipped = 0;

        // Furniture Refurbished
        added += addBlocks(FURNITURE_DRAWERS, FURNITURE_COLOR);
        added += addBlocks(FURNITURE_CABINETS, FURNITURE_COLOR);
        added += addBlocks(FURNITURE_KITCHEN_DRAWERS, FURNITURE_COLOR);
        added += addBlocks(FURNITURE_STOVES, FURNITURE_COLOR);
        added += addBlocks(FURNITURE_MAILBOXES, FURNITURE_COLOR);
        added += addBlocks(FURNITURE_FREEZERS, FURNITURE_COLOR);

        // Doomsday Decoration
        added += addBlocks(DOOMSDAY_LUGGAGE, DOOMSDAY_COLOR);
        added += addBlocks(DOOMSDAY_SHELVES, DOOMSDAY_COLOR);
        added += addBlocks(DOOMSDAY_TRASH, DOOMSDAY_COLOR);
        added += addBlocks(DOOMSDAY_BODIES, DOOMSDAY_COLOR);

        // Zombie Extreme
        added += addBlocks(ZOMBIE_TRASH, ZOMBIE_COLOR);

        // Apocalypse Now
        added += addBlocks(APOCALYPSE_WASHER, APOCALYPSE_COLOR);

        // Save to disk
        ArrayList<BlockData> allBlocks = new ArrayList<>(Controller.getBlockStore().getStore().values());
        ClientController.blockStore.write(allBlocks);

        // Refresh XRay if active
        if (Controller.isXRayActive()) {
            Controller.requestBlockFinder(true);
        }

        int totalBlocks = FURNITURE_DRAWERS.length + FURNITURE_CABINETS.length + FURNITURE_KITCHEN_DRAWERS.length
                + FURNITURE_STOVES.length + FURNITURE_MAILBOXES.length + FURNITURE_FREEZERS.length
                + DOOMSDAY_LUGGAGE.length + DOOMSDAY_SHELVES.length + DOOMSDAY_TRASH.length + DOOMSDAY_BODIES.length
                + ZOMBIE_TRASH.length + APOCALYPSE_WASHER.length;
        skipped = totalBlocks - added;

        mc.player.displayClientMessage(
                Component.literal("§6[XRay] §aAdded " + added + " container blocks" + (skipped > 0 ? " §e(skipped " + skipped + " already added)" : "")),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int addBlocks(String[] blocks, int color) {
        int count = 0;
        for (String blockName : blocks) {
            ResourceLocation location = new ResourceLocation(blockName);
            Block block = ForgeRegistries.BLOCKS.getValue(location);
            if (block == null)
                continue;

            if (Controller.getBlockStore().getStoreByReference(blockName) != null)
                continue;

            String displayName = Component.translatable(block.getDescriptionId()).getString();
            BlockData data = new BlockData(
                    displayName,
                    blockName,
                    color,
                    new ItemStack(block, 1),
                    true,
                    Controller.getBlockStore().getStore().size()
            );

            Controller.getBlockStore().put(data);
            count++;
        }
        return count;
    }
}
