package pro.mikey.xray.utils;

import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pro.mikey.xray.xray.Controller;

import static pro.mikey.xray.xray.Controller.requestBlockFinder;

public class RefreshOnOpen
{
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        // 获取方块状态
        if (Controller.isXRayActive()){
            requestBlockFinder(true);
        }
    }

}
