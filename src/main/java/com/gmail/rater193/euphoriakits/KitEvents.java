package com.gmail.rater193.euphoriakits;

import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class KitEvents {

    
	@SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
    	System.out.println("Loading vars");
    }

    
    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
    	System.out.println("Saving player");
    }
    
    
}
