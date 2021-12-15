package com.seenk.practice.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.seenk.practice.Main;
import com.seenk.practice.MainList;
import com.seenk.practice.managers.match.MatchType;
import com.seenk.practice.utils.ItemBuilder;

import net.md_5.bungee.api.ChatColor;

public class InventoryManager {
	
	// LOBBY //
	private final Inventory unrankedInventory = Bukkit.createInventory(null, 18, "Unranked Queue:");
	private final Inventory rankedInventory = Bukkit.createInventory(null, 18, "Ranked Queue:");
	private final Inventory duelInventory = Bukkit.createInventory(null, 18, "Duel:");
	private final Inventory acceptDuelInventory = Bukkit.createInventory(null, 9, "Request:");
	private final Inventory optionsInventory = Bukkit.createInventory(null, 9, "Options:");
	private final Inventory boInventory = Bukkit.createInventory(null, 9, "Best Of:");
	
	// Manage Player //
	private final Inventory manageInventory = Bukkit.createInventory(null, 9, "Manage:");
	private final Inventory rankInventory = Bukkit.createInventory(null, 9, "Rank:");
	
	// Settings //
	private final Inventory settingsInventory = Bukkit.createInventory(null, 9, "Settings:");
	
	// FFA //
	private final Inventory ffaInventory = Bukkit.createInventory(null, 9, "Play to FFA:");
	
	// CONFIG //
	private final Inventory locationConfigInventory = Bukkit.createInventory(null, 9, "Location:");
	private final Inventory locationFFAConfigInventory = Bukkit.createInventory(null, 9, "Location FFA:");
	private final Inventory arenaConfigInventory = Bukkit.createInventory(null, 9, "Arena:");
	private final Inventory arenaTypeConfigInventory = Bukkit.createInventory(null, 9, "Arena Type:");
	
	// PARTY //
	private final Inventory partyEvent = Bukkit.createInventory(null, 9, "Party Events:");
	private final Inventory partyEventFFA = Bukkit.createInventory(null, 18, "Party FFA:");
	private final Inventory partyEventSplit = Bukkit.createInventory(null, 18, "Party Split:");
	private final Inventory otherPartyInventory = Bukkit.createInventory(null, 54, "Other Party List:");
	private final Inventory partyInventory = Bukkit.createInventory(null, 9, "Party:");

	// SS //
	private final Inventory ssInventory = Bukkit.createInventory(null, 9, "SS Inventory");
	
	public void loadUnrankedLadder() {
		unrankedInventory.clear();
		MainList.getInstance().ladders.forEach(ladder -> unrankedInventory.addItem(new ItemBuilder(ladder.material(), 1, ladder.data()).setName(ladder.displayName()).addLoreLine(ChatColor.DARK_RED + "Queueing" + ChatColor.GRAY + ": " + ChatColor.WHITE + ladder.queueSize()).toItemStack()));
	}
	
	public void loadOtherParty() {
		otherPartyInventory.clear();
		if (MainList.getInstance().partyList.size() == 0) {
			otherPartyInventory.addItem(new ItemBuilder(Material.PAPER).setName(ChatColor.DARK_RED + "No party as available!").addLoreLine(ChatColor.DARK_GRAY + "Click here close the inventory!").toItemStack());
		}
		else {
			MainList.getInstance().partyList.forEach(party -> otherPartyInventory.addItem(new ItemBuilder(Material.GOLD_HELMET).setName(Bukkit.getPlayer(party).getName()).addLoreLine(ChatColor.DARK_RED + "Click here to ask join party!").toItemStack()));
		}
	}
	
	public Inventory getOtherPartyInventory() {
		return otherPartyInventory;
	}
	
	public void loadDuelLadder() {
		duelInventory.clear();
		MainList.getInstance().ladders.forEach(ladder -> duelInventory.addItem(new ItemBuilder(ladder.material(), 1, ladder.data()).setName(ladder.displayName()).toItemStack()));
	}
	
	public void loadManagePanel(String name) {
		final Player target = Bukkit.getPlayer(name);
		if(!target.isOnline()) {
			manageInventory.clear();
			manageInventory.addItem(new ItemBuilder(Material.IRON_BARDING).setName(ChatColor.DARK_RED.toString() + "Ban " + ChatColor.WHITE + "Him").addLoreLine(ChatColor.DARK_GRAY + "Click here for ban the player!").toItemStack());
			manageInventory.addItem(new ItemBuilder(Material.PAPER).setName(ChatColor.DARK_RED.toString() + "Mute " + ChatColor.WHITE + "Him").addLoreLine(ChatColor.DARK_GRAY + "Click here for mute the player!").toItemStack());
			manageInventory.addItem(new ItemBuilder(Material.SKULL_ITEM).setName(ChatColor.WHITE.toString() + name + ChatColor.DARK_RED + " Ranks").addLoreLine(ChatColor.DARK_GRAY + "Actualy: Offline").toItemStack());
		}
		else {
			manageInventory.clear();
			manageInventory.addItem(new ItemBuilder(Material.IRON_BARDING).setName(ChatColor.DARK_RED.toString() + "Ban " + ChatColor.WHITE + "Him").addLoreLine(ChatColor.DARK_GRAY + "Click here for ban the player!").toItemStack());
			manageInventory.addItem(new ItemBuilder(Material.PAPER).setName(ChatColor.DARK_RED.toString() + "Mute " + ChatColor.WHITE + "Him").addLoreLine(ChatColor.DARK_GRAY + "Click here for mute the player!").toItemStack());
			manageInventory.addItem(new ItemBuilder(Material.SKULL_ITEM).setName(ChatColor.WHITE.toString() + name + ChatColor.DARK_RED + " Ranks").addLoreLine(ChatColor.DARK_GRAY + "Actualy: Online").toItemStack());
		}
	}

	public void ssInventory() {
		ssInventory.clear();
		ssInventory.addItem(new ItemBuilder(Material.INK_SACK, 1, (byte)10).setName(ChatColor.GREEN + "Accept SS").addLoreLine(ChatColor.DARK_GRAY + "Click here for accept ss!").toItemStack());
		ssInventory.addItem(new ItemBuilder(Material.INK_SACK, 1, (byte)1).setName(ChatColor.GREEN + "Refuse SS").addLoreLine(ChatColor.DARK_GRAY + "Click here for refuse ss!").toItemStack());
	}

	public void loadRankPanel() {
		rankInventory.clear();
		rankInventory.addItem(new ItemBuilder(Material.GOLDEN_APPLE).setName(ChatColor.BLUE.toString() + "Founder").addLoreLine(ChatColor.DARK_GRAY + "Click here for set Founder rank!").toItemStack());
		rankInventory.addItem(new ItemBuilder(Material.PAPER).setName(ChatColor.DARK_RED.toString() + "Manager").addLoreLine(ChatColor.DARK_GRAY + "Click here for set Manager Rank!").toItemStack());
		rankInventory.addItem(new ItemBuilder(Material.SKULL_ITEM).setName(ChatColor.DARK_PURPLE.toString() + "Animator").addLoreLine(ChatColor.DARK_GRAY + "Click here for set Animator rank!").toItemStack());
		rankInventory.addItem(new ItemBuilder(Material.IRON_BARDING).setName(ChatColor.DARK_GREEN.toString() + "Mod").addLoreLine(ChatColor.DARK_GRAY + "Click here for set Mod rank!").toItemStack());
		rankInventory.addItem(new ItemBuilder(Material.GRASS).setName(ChatColor.AQUA.toString() + "Builder").addLoreLine(ChatColor.DARK_GRAY + "Click here for set Builder rank!").toItemStack());
		rankInventory.addItem(new ItemBuilder(Material.ITEM_FRAME).setName(ChatColor.LIGHT_PURPLE.toString() + "Media").addLoreLine(ChatColor.DARK_GRAY + "Click here for set Media rank!").toItemStack());
		rankInventory.addItem(new ItemBuilder(Material.DIAMOND).setName(ChatColor.GOLD.toString() + "Customers").addLoreLine(ChatColor.DARK_GRAY + "Click here for set Customers rank!").toItemStack());
		rankInventory.addItem(new ItemBuilder(Material.LEASH).setName(ChatColor.GREEN.toString() + "default").addLoreLine(ChatColor.DARK_GRAY + "Click here for set player rank to default!").toItemStack());
	}
	
	public Inventory getDuelInventory() {
		return duelInventory;
	}
	
	public void loadRankedLadder() {
		rankedInventory.clear();
		MainList.getInstance().ladders.forEach(ladder -> rankedInventory.addItem(new ItemBuilder(ladder.material(), 1, ladder.data()).setName(ladder.displayName()).addLoreLine(ChatColor.DARK_RED + "Queueing" + ChatColor.GRAY + ": " + ChatColor.WHITE + Main.getInstance().getQueueManager().getQueue(MatchType.UNRANKED)).toItemStack()));
	}
	
	public Inventory getRankedInventory() {
		return rankedInventory;
	}
	
	public void loadFFALadder() {
		ffaInventory.clear();
		MainList.getInstance().ladders.stream().filter(ladder -> ladder.isPlayingFFA()).forEach(ladder -> ffaInventory.addItem(new ItemBuilder(ladder.material(), 1, ladder.data()).setName(ladder.displayName()).toItemStack()));
	}
	
	public Inventory getFfaInventory() {
		return ffaInventory;
	}
	
	public void acceptDuelPanel() {
		acceptDuelInventory.clear();
		acceptDuelInventory.addItem(new ItemBuilder(Material.AIR).toItemStack());
		acceptDuelInventory.addItem(new ItemBuilder(Material.AIR).toItemStack());
		acceptDuelInventory.addItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(ChatColor.DARK_GREEN.toString() + "Accept Duel Request").addLoreLine(ChatColor.DARK_GRAY + "Click here for accept the duel!").toItemStack());
		acceptDuelInventory.addItem(new ItemBuilder(Material.AIR).toItemStack());
		acceptDuelInventory.addItem(new ItemBuilder(Material.AIR).toItemStack());
		acceptDuelInventory.addItem(new ItemBuilder(Material.INK_SACK, 1, (byte)1).setName(ChatColor.DARK_RED.toString() + "Deny Duel Request").addLoreLine(ChatColor.DARK_GRAY + "If you want decline this request!").toItemStack());
		acceptDuelInventory.addItem(new ItemBuilder(Material.AIR).toItemStack());
		acceptDuelInventory.addItem(new ItemBuilder(Material.AIR).toItemStack());
	}
	
	public void locationConfigPanel(final Player player) {
		locationConfigInventory.clear();
		locationConfigInventory.addItem(new ItemBuilder(Material.FEATHER).setName(ChatColor.DARK_RED.toString() + "Spawn Location").addLoreLine(ChatColor.DARK_GRAY + "Click here set the spawn location!").toItemStack());
		locationConfigInventory.addItem(new ItemBuilder(Material.IRON_BARDING).setName(ChatColor.DARK_RED.toString() + "Checking Location").addLoreLine(ChatColor.DARK_GRAY + "Click here set the checking location!").toItemStack());
		locationConfigInventory.addItem(new ItemBuilder(Material.DIAMOND_AXE).setName(ChatColor.DARK_RED.toString() + "FFA").addLoreLine(ChatColor.DARK_GRAY + "Click here set the first arena postion!").toItemStack());
	}
	
	public void locationFFAConfigPanel(final Player player) {
		locationFFAConfigInventory.clear();
		locationFFAConfigInventory.addItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(ChatColor.DARK_RED.toString() + "FFA 1").addLoreLine(ChatColor.DARK_GRAY + "Click here set the spawn location!").toItemStack());
		locationFFAConfigInventory.addItem(new ItemBuilder(Material.GOLD_SWORD).setName(ChatColor.DARK_RED.toString() + "FFA 2").addLoreLine(ChatColor.DARK_GRAY + "Click here set the checking location!").toItemStack());
		locationFFAConfigInventory.addItem(new ItemBuilder(Material.STONE_SWORD).setName(ChatColor.DARK_RED.toString() + "FFA 3").addLoreLine(ChatColor.DARK_GRAY + "Click here set the first arena postion!").toItemStack());
	}
	
	public void loadSettings() {
		settingsInventory.clear();
		settingsInventory.addItem(new ItemBuilder(Material.ITEM_FRAME).setName(ChatColor.DARK_RED.toString() + "Scoreboard").addLoreLine(ChatColor.DARK_GRAY + "Did you want untoggled the scoreboard?!").toItemStack());
		settingsInventory.addItem(new ItemBuilder(Material.GOLD_SWORD).setName(ChatColor.DARK_RED.toString() + "Duel").addLoreLine(ChatColor.DARK_GRAY + "Do you want stop receives invites to duel?").toItemStack());
		settingsInventory.addItem(new ItemBuilder(Material.PAPER).setName(ChatColor.DARK_RED.toString() + "Chat").addLoreLine(ChatColor.DARK_GRAY + "You want hide the chat?").toItemStack());
	}
	
	public Inventory getSettingsInventory() {
		return settingsInventory;
	}
	
	public void arenaConfigPanel(final Player player) {
		arenaConfigInventory.clear();
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (!pm.isCreateArena()) {
			arenaConfigInventory.addItem(new ItemBuilder(Material.ARROW).setName(ChatColor.WHITE.toString() + "Create Arena").addLoreLine(ChatColor.DARK_GRAY + "Click here for open ArenaConfigType").toItemStack());
			arenaConfigInventory.addItem(new ItemBuilder(Material.PAPER).setName(ChatColor.WHITE.toString() + "Save Config").addLoreLine(ChatColor.DARK_GRAY + "Click here for save arenas.yml").toItemStack());
		}
		else {
			arenaConfigInventory.addItem(new ItemBuilder(Material.WOOL, 1, (byte)14).setName(ChatColor.DARK_RED.toString() + "First Location").addLoreLine(ChatColor.DARK_GRAY + "Click here set the first arena postion!").toItemStack());
			arenaConfigInventory.addItem(new ItemBuilder(Material.WOOL, 1, (byte)11).setName(ChatColor.BLUE.toString() + "Second Location").addLoreLine(ChatColor.DARK_GRAY + "Click here set the second arena location").toItemStack());
		}
	}
	
	public void arenaTypeConfig() {
		arenaTypeConfigInventory.clear();
		arenaTypeConfigInventory.addItem(new ItemBuilder(Material.POTION, 1, (byte)16421).setName(ChatColor.WHITE + "NORMAL").toItemStack());
		arenaTypeConfigInventory.addItem(new ItemBuilder(Material.LEASH).setName(ChatColor.WHITE + "SUMO").toItemStack());
	}
	
	public void loadPartyFight() {
		partyEventFFA.clear();
		MainList.getInstance().ladders.forEach(ladder -> partyEventFFA.addItem(new ItemBuilder(ladder.material(), 1, ladder.data()).setName(ladder.displayName()).toItemStack()));
		partyEventSplit.clear();
		MainList.getInstance().ladders.forEach(ladder -> partyEventSplit.addItem(new ItemBuilder(ladder.material(), 1, ladder.data()).setName(ladder.displayName()).toItemStack()));
	}
	
	public Inventory getLocationConfigInventory() {
		return locationConfigInventory;
	}
	
	public Inventory getLocationFFAConfigInventory() {
		return locationFFAConfigInventory;
	}
	
	public Inventory getManageInventory() {
		return manageInventory;
	}
	
	public Inventory getRankInventory() {
		return rankInventory;
	}
	
	public Inventory getPartyEventFFA() {
		return partyEventFFA;
	}
	
	public Inventory getPartyEventSplit() {
		return partyEventSplit;
	}

	public void loadBestOf(){
		boInventory.clear();
		boInventory.addItem(new ItemBuilder(Material.GOLDEN_CARROT).setName(ChatColor.DARK_RED.toString() + "Best Of 1").setLore(ChatColor.WHITE + "Click here for set bo to 1 round").toItemStack());
		boInventory.addItem(new ItemBuilder(Material.APPLE).setName(ChatColor.DARK_RED.toString() + "Best Of 3").setLore(ChatColor.WHITE + "Click here for set bo to 3 round").toItemStack());
		boInventory.addItem(new ItemBuilder(Material.COOKED_FISH).setName(ChatColor.DARK_RED.toString() + "Best Of 5").setLore(ChatColor.WHITE + "Click here for set bo to 5 round").toItemStack());
	}

	public Inventory getBoInventory() { return boInventory; }

	public void loadPartyEvent() {
		partyEvent.clear();
		partyEvent.addItem(new ItemBuilder(Material.SHEARS).setName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Party" + ChatColor.WHITE + " Split").addLoreLine(ChatColor.DARK_RED + "Click here if you want to manage your choice of party").toItemStack());
		partyEvent.addItem(new ItemBuilder(Material.COMPASS).setName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Party" + ChatColor.WHITE + " FFA").addLoreLine(ChatColor.DARK_RED + "Do you want edit your settings?").toItemStack());
	}
	
	public void loadOptionsInventory() {
		optionsInventory.clear();
		optionsInventory.addItem(new ItemBuilder(Material.ANVIL).setName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Par" + ChatColor.WHITE + "ty").addLoreLine(ChatColor.DARK_RED + "Click here if you want to manage your choice of party").toItemStack());
		optionsInventory.addItem(new ItemBuilder(Material.NAME_TAG).setName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Sett" + ChatColor.WHITE + "ings").addLoreLine(ChatColor.DARK_RED + "Do you want edit your settings?").toItemStack());
		optionsInventory.addItem(new ItemBuilder(Material.EMERALD).setName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Leader" + ChatColor.WHITE + "board").addLoreLine(ChatColor.DARK_RED + "Do you want see the leaderboard?").toItemStack());
		optionsInventory.addItem(new ItemBuilder(Material.SKULL_ITEM).setName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Stat" + ChatColor.WHITE + "istics").addLoreLine(ChatColor.DARK_RED + "Do you want see your stats?").toItemStack());

	}
	
	public void loadPartyInvite() {
		partyInventory.clear();
		partyInventory.addItem(new ItemBuilder(Material.ENCHANTMENT_TABLE).setName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Create" + ChatColor.WHITE + " Party").addLoreLine(ChatColor.DARK_RED + "Do you want create party?").toItemStack());
		partyInventory.addItem(new ItemBuilder(Material.NETHER_STAR).setName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Party" + ChatColor.WHITE + " List").addLoreLine(ChatColor.DARK_RED + "Do you want ask to party for join it?").toItemStack());
	}
	
	public Inventory getArenaConfigInventory() {
		return arenaConfigInventory;
	}
	
	public Inventory getArenaTypeConfigInventory() {
		return arenaTypeConfigInventory;
	}
	
	public Inventory getPartyEvent() {
		return partyEvent;
	}
	
	public Inventory getPartyInventory() {
		return partyInventory;
	}
	
	public Inventory getOptionsInventory() {
		return optionsInventory;
	}
	
	public Inventory getUnrankedInventory() {
		return unrankedInventory;
	}
	
	public Inventory getAcceptDuelInventory() {
		return acceptDuelInventory;
	}

	public Inventory getSsInventory() { return ssInventory; }

}
