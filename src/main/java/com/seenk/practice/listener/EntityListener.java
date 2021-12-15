package com.seenk.practice.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.seenk.practice.Main;
import com.seenk.practice.ffa.FFAStatus;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.party.PartyGroup;
import com.seenk.practice.state.ProfileState;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityListener implements Listener {
	
	@EventHandler
	public void EntityDamageSolo(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
			if (pm.getProfileState() == ProfileState.LOBBY || pm.getProfileState() == ProfileState.CHECKING || pm.getProfileState() == ProfileState.QUEUE || pm.getProfileState() == ProfileState.PARTY) {
				event.setCancelled(true);	
			}
			if (pm.getProfileState() == ProfileState.FIGHT) {
				event.setCancelled(false);
			}
			if (pm.getProfileState() == ProfileState.PARTY_FIGHT) {
				event.setCancelled(false);
			}
			if (pm.getProfileState() == ProfileState.FFA) {
				pm.setFfaStatus(FFAStatus.FIGHT);
	    		Bukkit.getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
	    			pm.setFfaStatus(FFAStatus.FREE);
	    		}, 300L);
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler
	public void EntityDamageByAnotherEntity(EntityDamageByEntityEvent event) {
		final Player player = (Player) event.getEntity();
        if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.PLAYER) {
    		final Player anotherEntity = (Player) event.getDamager();
    		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
    		final PlayerManager aePm = PlayerManager.getPlayerManager().get(anotherEntity.getUniqueId());
    		if (pm.getProfileState() == ProfileState.MOD || pm.getProfileState() == ProfileState.FIGHT || pm.getProfileState() == ProfileState.PARTY_FIGHT || aePm.getProfileState() == ProfileState.LOBBY || aePm.getProfileState() == ProfileState.FIGHT || aePm.getProfileState() == ProfileState.FFA || aePm.getProfileState() == ProfileState.PARTY || aePm.getProfileState() == ProfileState.PARTY_FIGHT || aePm.getProfileState() == ProfileState.QUEUE){
    			if (pm.getProfileState() == ProfileState.FIGHT || pm.getProfileState() == ProfileState.PARTY_FIGHT){
    				player.sendMessage(ChatColor.DARK_RED + "Your opponnent was frozen please, wait or leave the match.");
    				return;
				}
    			if(player.getItemInHand().getType() == Material.PACKED_ICE) {
					if (aePm.isFrozen()) {
						if (!aePm.getFrozenBy().contains(player.getName())){
							player.sendMessage(ChatColor.DARK_RED + "You cannot frozen him because he is already frozen by a another mod!");
							return;
						}
						else {
							player.sendMessage(ChatColor.DARK_RED + "You've been unfrozen: " + ChatColor.WHITE + anotherEntity.getName());
							anotherEntity.sendMessage(ChatColor.DARK_RED + "You've been get unfrozen.");
							aePm.setFrozen(true);
							aePm.setFrozenBy(player.getName());
							anotherEntity.setWalkSpeed(0.2f);
							anotherEntity.setFlySpeed(0.25f);
							anotherEntity.setFoodLevel(20);
							anotherEntity.setSprinting(true);
							anotherEntity.removePotionEffect(PotionEffectType.JUMP);
						}
						return;
					}
					else {
						player.sendMessage(ChatColor.DARK_RED + "You've been frozen: " + ChatColor.WHITE + anotherEntity.getName());
						anotherEntity.sendMessage(ChatColor.DARK_RED + "You've been get frozen by: " + ChatColor.WHITE + player.getName());
						aePm.setFrozen(true);
						aePm.setFrozenBy(player.getName());
						anotherEntity.setWalkSpeed(0.0f);
						anotherEntity.setFlySpeed(0.0f);
						anotherEntity.setFoodLevel(0);
						anotherEntity.setSprinting(false);
						anotherEntity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
					}
				}
				if(player.getItemInHand().getType() == Material.ANVIL) {
					Main.getInstance().getInventoryManager().loadManagePanel(anotherEntity.getName());
					anotherEntity.openInventory(Main.getInstance().getInventoryManager().getManageInventory());
				}
			}
    		if (pm.getProfileState() == ProfileState.LOBBY && aePm.getProfileState() == ProfileState.LOBBY || pm.getProfileState() == ProfileState.CHECKING && aePm.getProfileState() == ProfileState.CHECKING) {
    			event.setCancelled(true);
    		}
    		if (pm.getProfileState() == ProfileState.QUEUE && aePm.getProfileState() == ProfileState.QUEUE || pm.getProfileState() == ProfileState.PARTY && aePm.getProfileState() == ProfileState.PARTY) {
    			event.setCancelled(true);
    		}
    		if (pm.getProfileState() == ProfileState.FIGHT && aePm.getProfileState() == ProfileState.FIGHT) {
    			event.setCancelled(false);
    		}
    		if (pm.getProfileState() == ProfileState.PARTY_FIGHT && aePm.getProfileState() == ProfileState.PARTY_FIGHT) {
    			if (pm.getPartyGroup() == PartyGroup.BLUE && aePm.getPartyGroup() == PartyGroup.BLUE) {
    				player.sendMessage(ChatColor.RED + "You can't hit your teammate.");
    				event.setCancelled(true);
    			}
    			if (pm.getPartyGroup() == PartyGroup.RED && aePm.getPartyGroup() == PartyGroup.RED) {
    				player.sendMessage(ChatColor.RED + "You can't hit your teammate.");
    				event.setCancelled(true);
    			}
    			if (pm.getPartyGroup() == PartyGroup.BLUE && aePm.getPartyGroup() == PartyGroup.RED) {
    				event.setCancelled(false);
    			}
    			if (pm.getPartyGroup() == PartyGroup.RED && aePm.getPartyGroup() == PartyGroup.BLUE) {
    				event.setCancelled(false);
    			}
    		}
    		if (pm.getProfileState() == ProfileState.FFA && aePm.getProfileState() == ProfileState.FFA) {
    			aePm.setFfaStatus(FFAStatus.FIGHT);
				pm.setFfaStatus(FFAStatus.FIGHT);
	    		Bukkit.getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
	    			aePm.setFfaStatus(FFAStatus.FREE);
	    			pm.setFfaStatus(FFAStatus.FREE);
	    		}, 300L);
    			event.setCancelled(false);
    		}
        }
	}

}
