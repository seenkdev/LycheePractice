package com.seenk.practice.managers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.seenk.practice.Main;
import com.seenk.practice.MainList;
import com.seenk.practice.arena.ArenaType;
import com.seenk.practice.ffa.FFAStatus;
import com.seenk.practice.ladders.MainLadders;
import com.seenk.practice.managers.match.MatchPreviewInventory;
import com.seenk.practice.managers.party.PartyManager;
import com.seenk.practice.party.PartyGroup;
import com.seenk.practice.player.IKit;
import com.seenk.practice.runnable.RankedRunnable;
import com.seenk.practice.state.ProfileState;
import com.seenk.practice.utils.LocationHelper;

import co.aikar.idb.DB;


public class PlayerManager {
	
	private static HashMap<UUID, PlayerManager> playerManager = new HashMap<UUID, PlayerManager>();
	private Player player;
	private UUID uuid;
	private boolean checkingRobot;
	private boolean frozen;
	private String frozenBy;
	private String ban;
	private String mute;
	private String name;
	private Player target;
	private String duelLadder;
	
	private MatchManager match;
	private int enderpearl;
	private int wonRound;
	
	private boolean isCreateArena;
	private ArenaType arenaType;
	private String arenaNameCreate;
	private boolean createArenaChat;
	private PartyGroup partyGroup;
	private int numberRound;
	private boolean askedToJoinParty;
	private String rank;
	private String prefix;
	private String permissions;
	
	private int tryToConnect;
	private ProfileState profileState;
	private SettingsManager settingsManager;
	private PartyManager partyManager;
	private int[] elos;
	private boolean partyInvite;
	private int kill = 0;
	private int death = 0;

	
	private RankedRunnable ranked;
	
	private boolean arenaChoosed;
	private FFAStatus ffaStatus;
	private IKit currentFightLadder;
	
	public PlayerManager(final UUID uuid) {
        this.elos = new int[MainList.getInstance().ladders.size()];
        for(int i = 0; i <= elos.length-1; i++) elos[i] = 1000;
		playerManager.put(uuid, this);	
		setUuid(uuid);
		createPlayerManager(uuid);
		this.update();
	}
	
	public static HashMap<UUID, PlayerManager> getPlayerManager() {
		return playerManager;
	}
	
    public static void saveInventory(Player... players) {
        for (Player player : players) {
            new MatchPreviewInventory(player);
        }
    }
	
	    public static int[] getSplitValue(final String string, final String spliter) {
	        final String[] split = string.split(spliter);
	        final int[] board = new int[split.length];
	        for (int i = 0; i <= split.length - 1; ++i) {
	            board[i] = Integer.parseInt(split[i]);
	        }
	        return board;
	    }
	
	    public static String getStringValue(final int[] board, final String spliter) {
	        final StringBuilder stringBuilder = new StringBuilder();
	        for (int i = 0; i <= board.length - 1; ++i) {
	            stringBuilder.append(board[i]);
	            if (i != board.length - 1) {
	                stringBuilder.append(spliter);
	            }
	        }
	        return stringBuilder.toString();
	    }
	
	public void createPlayerManager(final UUID uuid) {
		this.name = Bukkit.getPlayer(uuid).getName();
		this.ban = "no";
		this.mute = "no";
		this.frozen = false;
		this.partyInvite = false;
		this.checkingRobot = false;
		this.isCreateArena = false;
		this.arenaChoosed = false;
		this.enderpearl = 0;
		this.numberRound = 1;
		this.frozenBy = null;
		this.wonRound = 0;
		this.rank = "default";
		this.askedToJoinParty = false;
		this.prefix = "§a";
		this.permissions = "default";
		this.ffaStatus = FFAStatus.FREE;
		this.ranked = new RankedRunnable();
		this.tryToConnect = 0;
		this.settingsManager = new SettingsManager(playerManager.get(uuid));
		setPlayer(Bukkit.getPlayer(uuid));
	}

	public void setFrozenBy(String frozenBy) {
		this.frozenBy = frozenBy;
	}

	public String getFrozenBy() {
		return frozenBy;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public boolean isPartyInvite() {
		return partyInvite;
	}

	public void setPartyInvite(boolean partyInvite) {
		this.partyInvite = partyInvite;
	}

	public Integer getNumberRound() {
		return numberRound;
	}
	
	public void setNumberRound(Integer numberRound) {
		this.numberRound = numberRound;
	}
	
	public int getWonRound() {
		return wonRound;
	}
	
	public void setWonRound(int wonRound) {
		this.wonRound = wonRound;
	}
	
	public boolean isAskedToJoinParty() {
		return askedToJoinParty;
	}
	
	public void setAskedToJoinParty(boolean askedToJoinParty) {
		this.askedToJoinParty = askedToJoinParty;
	}
	
	public String getDuelLadder() {
		return duelLadder;
	}
	
	public void setDuelLadder(String duelLadder) {
		this.duelLadder = duelLadder;
	}
	
	public boolean isArenaChoosed() {
		return arenaChoosed;
	}
	
	public void setArenaChoosed(boolean arenaChoosed) {
		this.arenaChoosed = arenaChoosed;
	}
	
	public Player getTarget() {
		return target;
	}
	
	public void setTarget(Player target) {
		this.target = target;
	}
	
	public String getRank() {
		return rank;
	}
	
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPermissions() {
		return permissions;
	}
	
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	
	public PartyGroup getPartyGroup() {
		return partyGroup;
	}
	
	public void setPartyGroup(PartyGroup partyGroup) {
		this.partyGroup = partyGroup;
	}
	
	public Integer getDeath() {
		return death;
	}
	
	public void setDeath(Integer death) {
		this.death = death;
	}
	
	public Integer getKill() {
		return kill;
	}
	
	public void setKill(Integer kill) {
		this.kill = kill;
	}

	public int getEnderpearl() {
		return enderpearl;
	}

	public void setEnderpearl(int enderpearl) {
		this.enderpearl = enderpearl;
	}

	public String getBan() {
		return ban;
	}
	
	public void setBan(String ban) {
		this.ban = ban;
	}
	
	public String getMute() { return mute; }
	

	public void setMute(String mute) { this.mute = mute; }
	
	public FFAStatus getFfaStatus() {
		return ffaStatus;
	}
	
	public void setFfaStatus(FFAStatus ffaStatus) {
		this.ffaStatus = ffaStatus;
	}
	
	public MatchManager getMatch() {
		return match;
	}
	
	public void setMatch(MatchManager match) {
		this.match = match;
	}
	
	public boolean isCreateArenaChat() {
		return createArenaChat;
	}
	
	public void setCreateArenaChat(boolean createArenaChat) {
		this.createArenaChat = createArenaChat;
	}
	
	public String getArenaNameCreate() {
		return arenaNameCreate;
	}
	
	public void setArenaNameCreate(String arenaNameCreate) {
		this.arenaNameCreate = arenaNameCreate;
	}
	
	public ArenaType getArenaType() {
		return arenaType;
	}
	
	public void setArenaType(ArenaType arenaType) {
		this.arenaType = arenaType;
	}
	
	public boolean isCreateArena() {
		return isCreateArena;
	}
	
	public void setCreateArena(boolean isArena) {
		this.isCreateArena = isArena;
	}
	
	public PartyManager getPartyManager() {
		return partyManager;
	}
	
	public void setPartyManager(PartyManager partyManager) {
		this.partyManager = partyManager;
	}
	
	public SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}
	
	public int getTryToConnect() {
		return tryToConnect;
	}
	
	public void setTryToConnect(int tryToConnect) {
		this.tryToConnect = tryToConnect;
	}
	
	public ProfileState getProfileState() {
		return profileState;
	}
	
	public void setProfileState(ProfileState profileState) {
		this.profileState = profileState;
	}
	
	public boolean isCheckingRobot() {
		return checkingRobot;
	}
	
	public void setCheckingRobot(boolean checkingRobot) {
		this.checkingRobot = checkingRobot;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public void setUuid(final UUID uuid) {
		this.uuid = uuid;
		playerManager.put(this.uuid, this);
	}
    
    public void reset(final Player player, final GameMode gameMode) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.updateInventory();
        for (final PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setMaximumNoDamageTicks(20);
        player.setNoDamageTicks(20);
        player.setHealthScale(20.0);
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setFireTicks(0);
        player.setSaturation(10.0f);
        player.setGameMode(gameMode);
        player.setAllowFlight(false);
        player.setFlying(false);
    }
	
    public void sendKit(final IKit kit) {
        final Player player = Bukkit.getPlayer(this.uuid);
        player.getInventory().clear();
        player.getInventory().setContents(kit.content());
        player.getInventory().setArmorContents(kit.armor());
        player.updateInventory();
    }
	
    public void teleport(final Player player, final LocationHelper locationHelper) {
        if (locationHelper != null) {
            player.teleport(locationHelper.getLocation());
        }
        else {
            player.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
            System.out.println("Erreur - 1");
        }
    }
    
    public void disconnect(final Player player) {
    	save();
    	final Player p = player;
    	playerManager.remove(player.getUniqueId());
    	p.kickPlayer(ChatColor.WHITE + "Thanks for playing on " + ChatColor.DARK_RED + "Xenki.red");
    }
    
	public void sendToSpawn(final Player player) {
		setProfileState(ProfileState.LOBBY);
		teleport(player, Main.getInstance().getSpawn());
		Main.getInstance().getItemManager().spawnItems(player);
	}
    
    private boolean existPlayer() {
        return Main.getInstance().practiceDB.existPlayerManager(this.uuid);
    }
    
    private void update() {
        if (!this.existPlayer()) {
            Main.getInstance().practiceDB.createPlayerManager(this.uuid,this.name,this.ban,this.rank,this.mute,this.prefix,this.permissions);
        }
        else {
            Main.getInstance().practiceDB.updatePlayerManager(this.name, this.uuid);
            this.load();
        }
    }
    
    private void load() {
        try {
			this.rank = DB.getFirstRow("SELECT rank FROM playersdata WHERE name=?", this.name).getString("rank");
			this.prefix = DB.getFirstRow("SELECT prefix FROM playersdata WHERE name=?", this.name).getString("prefix");
			this.permissions = DB.getFirstRow("SELECT permissions FROM playersdata WHERE name=?", this.name).getString("permissions");
            this.elos = getSplitValue(DB.getFirstRow("SELECT elos FROM playersdata WHERE name=?", this.name).getString("elos"), ":");
            this.ban = DB.getFirstRow("SELECT ban FROM playersdata WHERE name=?", this.name).getString("ban");;
			this.mute = DB.getFirstRow("SELECT mute FROM playersdata WHERE name=?", this.name).getString("mute");;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
	   
    private void save() {
        try {
			DB.executeUpdate("UPDATE playersdata SET rank=? WHERE name=?", this.rank, this.name);
			DB.executeUpdate("UPDATE playersdata SET prefix=? WHERE name=?", this.prefix, this.name);
			DB.executeUpdate("UPDATE playersdata SET permissions=? WHERE name=?", this.permissions, this.name);
            DB.executeUpdate("UPDATE playersdata SET ban=? WHERE name=?", this.ban, this.name);
			DB.executeUpdate("UPDATE playersdata SET mute=? WHERE name=?", this.mute, this.name);
			final String elos = getStringValue(getElos(), ":");
            DB.executeUpdate("UPDATE playersdata SET elos=? WHERE name=?", elos, this.name);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
	    
    public int getElo(MainLadders lm) {
        return elos.length;
    }
	   
	public IKit getCurrentFightLadder() {
		return currentFightLadder;
	}
	   
	public void setCurrentFightLadder(IKit currentFightLadder) {
		this.currentFightLadder = currentFightLadder;
	}
	
	public int[] getElos() {
		return elos;
	}
	   
	public RankedRunnable getRanked() {
		return ranked;
	}
}
