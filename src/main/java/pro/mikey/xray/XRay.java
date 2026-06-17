package pro.mikey.xray;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.mikey.xray.utils.RefreshOnOpen;

@Mod(XRay.MOD_ID)
public class XRay {
	public static final String MOD_ID = "xray";
	public static final String PREFIX_GUI = String.format("%s:textures/gui/", MOD_ID);

	public static Logger logger = LogManager.getLogger();

	public XRay() {
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (c, b) -> true));
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientController::setup);
        MinecraftForge.EVENT_BUS.register(new RefreshOnOpen());
	}
}
