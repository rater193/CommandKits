package com.gmail.rater193.euphoriakits;

import java.io.File;

import com.gmail.rater193.euphoriakits.kits.Kit;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@Mod(modid = EuphoriaKits.MODID, version = EuphoriaKits.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class EuphoriaKits
{
    public static final String MODID = "EuphoriaKits";
    public static final String VERSION = "1.0";
    
    Configuration config;
    File euophoriaKitsPlayerSaves;
    
    KitEvents events = new KitEvents();
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
    	
    	FMLCommonHandler.instance().bus().register(events);
    	MinecraftForge.EVENT_BUS.register(events);
    	
    	
    	config = new Configuration(event.getSuggestedConfigurationFile());
    	String path = (config.getConfigFile().getParentFile().getAbsolutePath())+File.pathSeparator;
    	config.addCustomCategoryComment("Commands", "The list of commands you can execute");
    	config.load();
    	config.get("Commands", "Cmd", new String[] {
    			"/say Command example, <name>!",
    			"/say You can have multiple commands!",
    			"/say You can also get the users UUID!",
    			"/say UUID: <uuid>"
    		}).getStringList();
    	if(config.hasChanged()) {
    		config.save();
    	}
    	
    	euophoriaKitsPlayerSaves = new File(path+"euophoriaKitsPlayerSaves.cfg");
    	PlayerManager.load(euophoriaKitsPlayerSaves);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("EuphoriaKits >> "+Blocks.dirt.getUnlocalizedName());
        Kit.mainConfig = config;
        Kit.loadKits();
    }
    
    @EventHandler
    public void onServerLoad(FMLServerStartingEvent event) {
    	event.registerServerCommand(new CommandKits());
    	
    }
    
    @EventHandler
    public void onServerStop(FMLServerStoppingEvent event) {
    	PlayerManager.save(euophoriaKitsPlayerSaves);
    }
}
