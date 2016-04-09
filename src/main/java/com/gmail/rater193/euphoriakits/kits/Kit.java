package com.gmail.rater193.euphoriakits.kits;

import java.io.File;
import java.rmi.UnexpectedException;
import java.util.HashMap;
import org.apache.commons.io.FilenameUtils;

import net.minecraftforge.common.config.Configuration;
import scala.actors.UncaughtException;

public class Kit {
	public static HashMap<String, Kit> kits;
	public static Configuration mainConfig;
	public static String configKitsDir;

	public String[] commands;
	public int timeDelay;
	public int maxUses;
	
	public static void loadKits() {
		
		kits = new HashMap<String, Kit>();
		
		if(mainConfig!=null) {
			System.out.println(mainConfig.getConfigFile().getPath());
			System.out.println(mainConfig.getConfigFile().getParentFile().getAbsolutePath());
			String kitsdir = mainConfig.getConfigFile().getParentFile().getAbsolutePath()+File.separator+"kits"+File.separator;
			
			File kitsfolder = new File(kitsdir);
			if(kitsfolder.exists()==false) {
				kitsfolder.mkdirs();
			}
			
			File[] targetkits = kitsfolder.listFiles();
			
			for(File newkitfile : targetkits) {
				String kitname = FilenameUtils.removeExtension(newkitfile.getName());
				
				Configuration kitcfg = new Configuration(newkitfile);
				
				kitcfg.load();
				kitcfg.addCustomCategoryComment("Commands", "The list of commands you can execute");
				String[] newcmds = (kitcfg.get("Commands", "Cmd", new String[] {
		    			"/say default command"
		    		}).getStringList()
				);
				
				int newtime = kitcfg.get("Commands", "executionDelayTimeSeconds", 1).getInt();
				int newmaxuses = kitcfg.get("Commands", "maxUses", 1).getInt();
				
				if(kitcfg.hasChanged()) {
					kitcfg.save();
				}
				
				//new Kit(kitname, new String[] {"/say tst"});
				

	        	Kit newkit = new Kit(kitname, newcmds, newtime, newmaxuses);
			}
			
			configKitsDir = kitsdir;
			
			//Configuration test = new Configuration(new File(kitsdir+"test.cfg"));
			//test.save();
			
			/*
        	new Kit("trainer", new String[] {
        		"/say Giving kit, trainer, to <player>",
        		"/give <player> pixelmon:item.Poke_Ball 5",
        		"/give <player> pixelmon:item.Great_Ball 2",
        		"/give <player> pixelmon:item.Potion 5",
        		"/give <player> pixelmon:item.Super_Potion 2",
        		"/give <player> pixelmon:item.Rare_Candy 1",
        		"/give <player> pixelmon:item.Old_Running_Boots 1",
        		"/give <player> minecraft:iron_shovel 1",
        		"/give <player> minecraft:iron_pickaxe 1",
        		"/give <player> minecraft:iron_axe 1",
        		"/givemoney <player> 500"
        	});*/
		}
	}
	
	public Kit(String name, String[] commands, int timedelay, int maxUses) {
		this.commands = commands;
		kits.put(name, this);
		this.timeDelay = timedelay;
		this.maxUses = maxUses;
	}
}
