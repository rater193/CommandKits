package com.gmail.rater193.euphoriakits;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;

public class PlayerManager {
	//uuid, uniqueid
	public static HashMap<String, String> playerVars = new HashMap<String, String>();

	public static String getUUID(EntityPlayer player) {
		return player.getUniqueID().toString();
	}
	public static String getUUID(ICommandSender player) {
		return getUUID((EntityPlayer)player.getCommandSenderEntity());
	}
	
	public String getVariable(EntityPlayer player, String variable) {
		if(playerVars.containsKey(variable)) {
			return playerVars.get(variable);
		}
		return "";
	}
	
	public String getVariable(ICommandSender player, String variables) {
		return getVariable((EntityPlayer)player.getCommandSenderEntity(), variables);
	}
	
	public static void save(File config) {
		
	}
	
	public static void load(File config) {
		
	}
}
