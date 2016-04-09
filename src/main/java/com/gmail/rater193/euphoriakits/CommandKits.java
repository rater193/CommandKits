package com.gmail.rater193.euphoriakits;

import java.util.ArrayList;
import java.util.List;

import com.gmail.rater193.euphoriakits.kits.Kit;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandExecuteAt;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandKits implements ICommand {

    private List<String> aliases;
    private ICommandSender simmedPlayer;
    
    public CommandKits() {
    	aliases = new ArrayList();
    	aliases.add("kit");
    	aliases.add("kits");
    	

		
		simmedPlayer = new ICommandSender() {
            private static final String __OBFID = "CL_00002343";

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return "Kits";
			}

			@Override
			public IChatComponent getDisplayName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void addChatMessage(IChatComponent component) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public BlockPos getPosition() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Vec3 getPositionVector() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public World getEntityWorld() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Entity getCommandSenderEntity() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean sendCommandFeedback() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setCommandStat(Type type, int amount) {
				// TODO Auto-generated method stub
				
			}
			
		};
    }
    
	@Override
	public int compareTo(ICommand arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "kit";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		sender.addChatMessage(new ChatComponentText("/kit <kitname> - get the targeted kit"));
		sender.addChatMessage(new ChatComponentText("/kit - list the avalible kits"));
		return "/kit";
	}

	@Override
	public List<String> getCommandAliases() {
		// TODO Auto-generated method stub
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();

		NBTTagCompound tag = ((EntityPlayer)sender.getCommandSenderEntity()).getEntityData();
		
		if(world.isRemote) {
			System.out.println("This can not be ran on a client!");
		} else {
			if(args.length<1) {
				sender.addChatMessage(new ChatComponentText("List of kits:"));
				
				
				for(String kitname : Kit.kits.keySet()) {
					if(sender.canCommandSenderUseCommand(0, "kits."+kitname)) {
						boolean canShow = true;
						

						Kit kit = Kit.kits.get(kitname);

						String usestagname = "kits."+kitname+".times";
						String lasttimeusedtagname = "kits."+kitname+".lastTimeUsedKits";
						
						NBTBase usestag = tag.getTag(usestagname);
						NBTBase lasttimeusedtag = tag.getTag(lasttimeusedtagname);
						int uses = 0;
						long lastTimeUsed = -1;
						
						if(usestag!=null) {
							uses = tag.getInteger(usestagname);
						}
						if(lasttimeusedtag!=null) {
							lastTimeUsed = tag.getLong(lasttimeusedtagname);
						}
						

						if(uses>=kit.maxUses && kit.maxUses >0) {
							canShow = false;
						}else if(Math.abs(System.currentTimeMillis()-lastTimeUsed) < kit.timeDelay*1000){
							canShow = false;
						}
						
						if(canShow) {
						sender.addChatMessage(new ChatComponentText(kitname));
						}
						
					}
				}
			}else{
				String arg = args[0].toLowerCase();
				//switch(arg) {
					//default:
					if(arg=="reload") {

						if(sender.canCommandSenderUseCommand(0, "kits.admin")) {
							Kit.loadKits();
							sender.addChatMessage(new ChatComponentText("[Kits] Reloading kits."));
						}else{
							sender.addChatMessage(new ChatComponentText("[Kits] You are not allowed to use that!"));
						}
						
					}else{
						if(Kit.kits.containsKey(arg)) {

							if(sender.canCommandSenderUseCommand(0, "kits."+arg)) {
								Kit kit = Kit.kits.get(arg);
	
								String usestagname = "kits."+arg+".times";
								String lasttimeusedtagname = "kits."+arg+".lastTimeUsedKits";
								
								NBTBase usestag = tag.getTag(usestagname);
								NBTBase lasttimeusedtag = tag.getTag(lasttimeusedtagname);
								int uses = 0;
								long lastTimeUsed = -1;
								
								if(usestag!=null) {
									uses = tag.getInteger(usestagname);
								}
								if(lasttimeusedtag!=null) {
									lastTimeUsed = tag.getLong(lasttimeusedtagname);
								}
								
								if((uses<kit.maxUses || kit.maxUses<0) && (lastTimeUsed==-1 || Math.abs(System.currentTimeMillis()-lastTimeUsed) >= kit.timeDelay*1000) ) {
									sender.addChatMessage(new ChatComponentText("[Kits] Giving kit, "+arg));
									
									ICommandManager cmdmanager = MinecraftServer.getServer().getCommandManager();
									
									
									for(String command : kit.commands) {
										cmdmanager.executeCommand(simmedPlayer, formatString(sender, command));
									}
									
									uses++;
									lastTimeUsed = System.currentTimeMillis();
									tag.setInteger(usestagname, uses);
									tag.setLong(lasttimeusedtagname, lastTimeUsed);
								}else{
									if(uses>=kit.maxUses && kit.maxUses >0) {
										sender.addChatMessage(new ChatComponentText("[Kits] You are only allowed to use this kit "+kit.maxUses+" time(s)!"));
									}else{
										sender.addChatMessage(new ChatComponentText("[Kits] You have to wait "+((int)Math.abs(kit.timeDelay-(Math.floor((System.currentTimeMillis()-lastTimeUsed)/1000))))+" seconds before you can use this again!"));
									}
								}
							}
						}
					}
				//}
			}
		}
	}

	private String formatString(ICommandSender player, String string) {
		// TODO Auto-generated method stub
		string = string.replaceAll("<name>", player.getName());
		string = string.replaceAll("<player>", player.getName());
		string = string.replaceAll("<playername>", player.getName());
		string = string.replaceAll("<uuid>", PlayerManager.getUUID(player));
		return string;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		if(sender.canCommandSenderUseCommand(0, "kits.commands")) {
			return true;
		}
		return false;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
