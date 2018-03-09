package info.loenwind.thatsagoodmap;

import java.io.File;

import org.apache.logging.log4j.Level;

import info.loenwind.thatsagoodmap.end.WorldProviderEndVoid;
import info.loenwind.thatsagoodmap.nether.WorldProviderHellVoid;
import info.loenwind.thatsagoodmap.overworld.WorldProviderSurfaceVoid;
import info.loenwind.thatsagoodmap.voidworld.VoidWorldType;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

@Mod(modid = ThatsAGoodMapMod.MODID, name = ThatsAGoodMapMod.NAME, version = "@MOD_VERSION@")
public class ThatsAGoodMapMod {

  public static final String NAME = "ThatsAGoodMap";
  public static final String MODID = "thatsagoodmap";

  @Instance(MODID)
  public static ThatsAGoodMapMod instance;
  private VoidWorldType worldType;
  private boolean generateSpikes = false;
  private boolean generateNetherFortress = false;
  private boolean generateEndCities = false;
  private File configDir = null;

  @EventHandler
  public void preinit(FMLPreInitializationEvent event) {
    Configuration config = null;

    this.configDir = new File(event.getModConfigurationDirectory(), NAME);

    File cfgFile = new File(this.configDir, NAME + ".cfg");

    try {
      config = new Configuration(cfgFile);
    } catch (Exception e) {
      FMLLog.severe("[" + MODID + "] Error loading config, deleting file and resetting: ");
      e.printStackTrace();

      if (cfgFile.exists())
        cfgFile.delete();

      config = new Configuration(cfgFile);
    }

    Property prop;

    prop = config.get(CATEGORY_GENERAL, "generateSpikes", generateSpikes);
    prop.setComment("Set to true to enable generation of the obsidian 'spikes' in the end.");
    generateSpikes = prop.getBoolean(generateSpikes);

    prop = config.get(CATEGORY_GENERAL, "generateNetherFortress", generateNetherFortress);
    prop.setComment("Set to true to enable generation of the nether fortresses.");
    generateNetherFortress = prop.getBoolean(generateNetherFortress);

    prop = config.get(CATEGORY_GENERAL, "generateEndCities", generateEndCities);
    prop.setComment("Set to true to enable generation of the end cities.");
    generateEndCities = prop.getBoolean(generateEndCities);

    if (config.hasChanged()) {
      config.save();
    }

    MinecraftForge.EVENT_BUS.register(this);
  }

  @EventHandler
  public void load(FMLInitializationEvent event) {
    FMLLog.log(Level.INFO, MODID + " Initalized");
    worldType = new VoidWorldType();

    DimensionManager.unregisterDimension(-1);
    DimensionManager.unregisterDimension(0);
    DimensionManager.unregisterDimension(1);
    DimensionManager.registerDimension(-1, DimensionType.register("Nether", "_nether", -1, WorldProviderHellVoid.class, false));
    DimensionManager.registerDimension(0, DimensionType.register("Overworld", "", 0, WorldProviderSurfaceVoid.class, true));
    DimensionManager.registerDimension(1, DimensionType.register("The End", "_end", 1, WorldProviderEndVoid.class, false));
  }

  @SubscribeEvent
  public void onWorldLoad(WorldEvent.Load event) {
    // Load a 3x3 around spawn to make sure that it populates and calls our hooks.
    if (!event.getWorld().isRemote && event.getWorld() instanceof WorldServer) {
      WorldServer world = (WorldServer) event.getWorld();
      int spawnX = (int) (event.getWorld().getWorldInfo().getSpawnX() / world.provider.getMovementFactor() / 16);
      int spawnZ = (int) (event.getWorld().getWorldInfo().getSpawnZ() / world.provider.getMovementFactor() / 16);
      for (int x = -1; x <= 1; x++)
        for (int z = -1; z <= 1; z++)
          world.getChunkProvider().loadChunk(spawnX + x, spawnZ + z);
    }
  }

  public boolean shouldBeVoid(World world) {
    return world.getWorldInfo().getTerrainType() == worldType;
  }

  public boolean shouldGenerateSpikes(World world) {
    return generateSpikes;
  }

  public boolean shouldGenerateNetherFortress(World world) {
    return generateNetherFortress;
  }

  public boolean shouldGenerateEndCities(World world) {
    return generateEndCities;
  }

}
