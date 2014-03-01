package net.minecraftforge.lex.yunomakegoodmap;

import static net.minecraftforge.common.Configuration.CATEGORY_GENERAL;

import java.io.File;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.Property;
import net.minecraftforge.lex.yunomakegoodmap.generators.*;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;

@Mod(modid = "YUNoMakeGoodMap", name = "YUNoMakeGoodMap", version = "3.0")
public class YUNoMakeGoodMap 
{
    @Instance("YUNoMakeGoodMap")
    public static YUNoMakeGoodMap instance;
    private VoidWorldType worldType;
    private boolean overrideDefault = false;
    private String platformType = "grass";
    private boolean generateSpikes = false;
    private boolean generateNetherFortress = false;
    private Map<String, IPlatformGenerator> generators = Maps.newHashMap();
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        Configuration config = null;
        File cfgFile = event.getSuggestedConfigurationFile();
        try
        {
            config = new Configuration(cfgFile);
        }
        catch (Exception e)
        {
            FMLLog.severe("[YUNoMakeGoodMap] Error loading config, deleting file and resetting: ");
            e.printStackTrace();

            if (cfgFile.exists())
                cfgFile.delete();

            config = new Configuration(cfgFile);
        }

        Property prop;

        prop = config.get(CATEGORY_GENERAL, "overrideDefault", overrideDefault);
        prop.comment = "Set to true to force the default world types to be void world. Use with caution.";
        overrideDefault = prop.getBoolean(overrideDefault);

        prop = config.get(CATEGORY_GENERAL, "platformType", platformType);
        prop.comment = "Set the type of platform to create in the overworld, Possible values: \n" + 
                       "  'grass' A single grass block\n" +
                       "  'tree' a small oak tree on a grass block\n" +
                       "  'skyblock21' For SkyBlock v2.1 platforms";
        platformType = prop.getString();
        
        prop = config.get(CATEGORY_GENERAL, "generateSpikes", generateSpikes);
        prop.comment = "Set to true to enable generation of the obsidian 'spikes' in the end.";
        generateSpikes = prop.getBoolean(generateSpikes);
        
        prop = config.get(CATEGORY_GENERAL, "generateNetherFortress", generateNetherFortress);
        prop.comment = "Set to true to enable generation of the nether fortresses.";
        generateNetherFortress = prop.getBoolean(generateNetherFortress);


        if (config.hasChanged())
        {
            config.save();
        }

        generators.put("grass", new SingleBlockPlatform(Block.grass));
        generators.put("tree", new TreePlatform());
        generators.put("skyblock21", new SkyBlock21());
    }

    @EventHandler
    public void load(FMLInitializationEvent event) 
    {
        
        FMLLog.log(Level.INFO, "YUNoMakeGoodMap Initalized");
        LanguageRegistry.instance().addStringLocalization("generator.void", "Void World");

        for (int x = 0; x < WorldType.worldTypes.length; x++)
        {
            if (WorldType.worldTypes[x] == null)
            {
                FMLLog.log(Level.INFO, "YUNoMakeGoodMap Type ID: %d", x);
                worldType = new VoidWorldType(x);
                break;
            }
        }

        Hashtable<Integer, Class<? extends WorldProvider>> providers = ReflectionHelper.getPrivateValue(DimensionManager.class, null, "providers");
        providers.put(-1, WorldProviderHellVoid.class);
        providers.put(0,  WorldProviderSurfaceVoid.class);
        providers.put(1,  WorldProviderEndVoid.class);
    }

    public boolean shouldBeVoid(World world)
    {
        return overrideDefault || world.getWorldInfo().getTerrainType() == worldType;
    }

    public IPlatformGenerator getPlatformType(World world)
    {
        if (platformType == null) platformType = "grass";
        IPlatformGenerator ret = generators.get(platformType.toLowerCase(Locale.ENGLISH));
        if (ret == null)
        {
            platformType = "grass";
            ret = generators.get(platformType);
        }
        return ret;
    }

    public boolean shouldGenerateSpikes(World world)
    {
        return generateSpikes;
    }

    public boolean shouldGenerateNetherFortress(World world)
    {
        return generateNetherFortress;
    }
}
