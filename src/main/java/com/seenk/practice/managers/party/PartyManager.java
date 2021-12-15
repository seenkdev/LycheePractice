package com.seenk.practice.managers.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.seenk.practice.Main;
import com.seenk.practice.MainList;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.state.ProfileState;

import net.md_5.bungee.api.ChatColor;

public class PartyManager {
	
	private HashMap<PlayerManager, PartyManager> partyManager = new HashMap<PlayerManager, PartyManager>();
	private boolean open;
	private int maxSize;
	private int currentSize;
	private String partyName;
	private PartyFightType fightType;
	private UUID leader;
	private ArrayList<UUID> AllMembers = new ArrayList<UUID>();
	private ArrayList<UUID> members = new ArrayList<UUID>();
	
	public PartyManager(final PlayerManager pm) {
		this.open = false;
		this.maxSize = 4;
		this.AllMembers.add(pm.getUuid());
		this.currentSize = 1;
		this.partyName = Bukkit.getPlayer(pm.getUuid()).getName();
		this.partyManager.put(pm, this);
		createParty(pm.getPlayer());
	}
	
	public void setFightType(PartyFightType fightType) {
		this.fightType = fightType;
	}
	
	public PartyFightType getFightType() {
		return fightType;
	}
	
	public void createParty(final Player player) {
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getProfileState() == ProfileState.PARTY) {
			player.sendMessage(ChatColor.RED + "You can't join this party you are in another partys!");
			return;
		}
		if (pm.isPartyInvite() == true) {
			pm.setPartyInvite(false);
		}
		pm.setPartyManager(this);
		setLeader(player.getUniqueId());
		setOpen(false);
		pm.setProfileState(ProfileState.PARTY);
		Main.getInstance().getItemManager().partyItems(player);
		pm.teleport(player, Main.getInstance().spawn);
		MainList.getInstance().partyList.add(player.getUniqueId());
		player.sendMessage(ChatColor.DARK_RED + "You have been created your " + ChatColor.WHITE + "party");
	}
	
	public void addMember(final UUID uuid) {
		final Player player = Bukkit.getPlayer(uuid);
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		pm.setPartyManager(this);
		pm.getPartyManager().getAllMembers().add(player.getUniqueId());
		if (pm.isPartyInvite() == true) {
			pm.setPartyInvite(false);
		}
		setCurrentSize(getCurrentSize() + 1);
		this.members.add(uuid);
		pm.setProfileState(ProfileState.PARTY);
		pm.teleport(player, Main.getInstance().getSpawn());
		Main.getInstance().getItemManager().partyItems(player);
	}
	
	public void leaveParty(final Player player) {
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getPartyManager().getLeader() == player.getUniqueId()) {
			for (int i = 0; i<AllMembers.size();i++) {
				final UUID uuid = AllMembers.get(i);
				final Player partyPlayer = Bukkit.getPlayer(uuid);
				partyPlayer.sendMessage(ChatColor.DARK_RED + "The party was disbanded!");
				final PlayerManager partyPM = PlayerManager.getPlayerManager().get(uuid);
				partyPM.setPartyManager(null);
				if (partyPM.isPartyInvite() == true) {
					partyPM.setPartyInvite(false);
				}
				partyPM.sendToSpawn(player);
				AllMembers.remove(uuid);
			}
			MainList.getInstance().partyList.remove(player.getUniqueId());
		}
		else {
			pm.getPartyManager().setCurrentSize(getCurrentSize() - 1);
			AllMembers.remove(player.getUniqueId());
			pm.setPartyManager(null);
			for (UUID uuid : AllMembers) {
				final Player partyPlayer = Bukkit.getPlayer(uuid);
				partyPlayer.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.DARK_RED + " have left the party!");
			}
			player.sendMessage(ChatColor.DARK_RED + "You have been left the party.");
			pm.sendToSpawn(player);	
		}
	}
	
	public ArrayList<UUID> getMembers() {
		return members;
	}
	
	public int getCurrentSize() {
		return currentSize;
	}
	
	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}
	
	public void setLeader(UUID leader) {
		this.leader = leader;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	
	public ArrayList<UUID> getAllMembers() {
		return AllMembers;
	}
	
	public int getMaxSize() {
		return maxSize;
	}
	
	public HashMap<PlayerManager, PartyManager> getPartyManager() {
		return partyManager;
	}
	
	public String getPartyName() {
		return partyName;
	}
	
	public UUID getLeader() {
		return leader;
	}
	
	public boolean isOpen() {
		return open;
	}

	public void sendAskToJoinParty(Player target, Player player) {
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getPartyManager().isOpen() == true) {
			addMember(target.getUniqueId());
			target.sendMessage(ChatColor.DARK_RED + "You have join the party!");
		}
		else {
			pm.setAskedToJoinParty(true);
			pm.setTarget(target);
			target.sendMessage(ChatColor.DARK_RED + "The request as been sended.");
			player.sendMessage(target.getName() + ChatColor.DARK_RED + " ask to join your party! Answer in chat with no or yes.");	
		}
	}
}
