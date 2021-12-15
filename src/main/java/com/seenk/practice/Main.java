package com.seenk.practice;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.seenk.practice.command.ManageCommand;
import com.seenk.practice.command.SpawnCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.seenk.practice.arena.ArenaManager;
import com.seenk.practice.board.ScoreboardRunnable;
import com.seenk.practice.command.ArenaCommand;
import com.seenk.practice.command.DuelCommand;
import com.seenk.practice.command.JoinPartyCommand;
import com.seenk.practice.command.LocationCommand;
import com.seenk.practice.database.PracticeSQL;
import com.seenk.practice.ladders.MainLadders;
import com.seenk.practice.leaderboard.Leaderboard;
import com.seenk.practice.leaderboard.LeaderboardInventory;
import com.seenk.practice.listener.ChatListener;
import com.seenk.practice.listener.EntityListener;
import com.seenk.practice.listener.InventoryListener;
import com.seenk.practice.listener.ItemInteractListener;
import com.seenk.practice.listener.PlayerListener;
import com.seenk.practice.listener.ServerList;
import com.seenk.practice.managers.InventoryManager;
import com.seenk.practice.managers.ItemManager;
import com.seenk.practice.managers.MatchManager;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.managers.QueueManager;
import com.seenk.practice.managers.match.MatchType;
import com.seenk.practice.runnable.RankedRunnable;
import com.seenk.practice.utils.LocationHelper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.DB;
import co.aikar.idb.Database;

public class Main extends JavaPlugin {
	
	static Main instance;
	
    public File locationFile;
    public YamlConfiguration locationConfig;
    public File arenaFile;
    public YamlConfiguration arenaConfig;
    private String configPath;
    
	private ItemManager itemManager;
	private InventoryManager inventoryManager;
	private QueueManager queueManager;
	
    public Connection connection;
    public PracticeSQL practiceDB = new PracticeSQL();
	
	public LocationHelper spawn;
	public LocationHelper checkingSpawn;
	
	public LocationHelper ffa1;
	public LocationHelper ffa2;
	public LocationHelper ffa3;
	
    public Thread LeaderboardThread;
    public Inventory leaderboardInventory;
    public Leaderboard leaderboard;
	
	public ArrayList<Location> spawnFFA = new ArrayList<Location>();
	
	public Main() {
		this.spawn = new LocationHelper("spawn");
		this.checkingSpawn = new LocationHelper("checking");
		this.ffa1 = new LocationHelper("ffa-1");
		this.ffa2 = new LocationHelper("ffa-2");
		this.ffa3 = new LocationHelper("ffa-3");
        this.leaderboard = new Leaderboard();
	}
	
	public void onEnable() {
		Main.instance = this;
		new ScoreboardRunnable().runTaskTimer(this, 0, 1);
		this.registerResource();
		this.setupHikariCP();
		this.setupDatabase();
		this.registerFile();
		this.registerLocation();
		this.registerManager();
		this.registerListener();
		this.registerCommand();
		this.registerArena();
		this.registerRankedTask();
        (this.LeaderboardThread = new Thread(new LeaderboardInventory())).start();
	}
	
	private void registerCommand() {
		this.getCommand("location").setExecutor(new LocationCommand());
		this.getCommand("arena").setExecutor(new ArenaCommand());
		this.getCommand("duel").setExecutor(new DuelCommand());
		this.getCommand("manage").setExecutor(new ManageCommand());
		this.getCommand("jp").setExecutor(new JoinPartyCommand());
		this.getCommand("spawn").setExecutor(new SpawnCommand());
	}

	public void onDisable() {
		LocationHelper.getAll().forEach(LocationHelper::save);
		ArenaManager.getAll().forEach(ArenaManager::save);
		try {
			this.locationConfig.save(locationFile);
			this.arenaConfig.save(arenaFile);
		}
		catch (IOException io){
			io.printStackTrace();
		}
	}
	
    private void registerArena() {
        final ConfigurationSection cs = this.arenaConfig.getConfigurationSection("arenas");
        if (cs != null) {
            for (final String s : cs.getKeys(false)) {
                if (s != null) {
                    final ConfigurationSection cs2 = cs.getConfigurationSection(s);
                    if (cs2 == null) {
                        continue;
                    }
                    final ArenaManager arena = new ArenaManager(cs2.getName());
                    arena.load();
                    this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Registered Arena: " + ChatColor.WHITE + arena.getName() + ChatColor.GRAY + " ->" + ChatColor.RED + " Type: " + ChatColor.WHITE + arena.getArenaType().toString());
                }
            }
        }
    }

	private void registerRankedTask() {
		final BukkitScheduler scheduler = this.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, () -> {
			for (final MainLadders ladder : MainList.getInstance().ladders) {
				final QueueManager fightManager = MainList.getInstance().fight.get(ladder.displayName());
				if (fightManager != null && fightManager.getRankedQueuePlayer().size() >= 2) {
					final PlayerManager playerManager0 = fightManager.getRankedQueuePlayer().get(0);
					for (final PlayerManager playerManager2 : fightManager.getRanked()) {
						if (RankedRunnable.checkCompatibilityElo(playerManager0, playerManager2, ladder)) {
							new MatchManager(MatchType.RANKED,false, 1, playerManager0.getUuid(), playerManager2.getUuid(), ladder.displayName());
							break;
						}
					}
				}
			}
		}, 0L, 20L);
	}

	private void registerManager() {
		new MainList();
		this.itemManager = new ItemManager();
		this.inventoryManager = new InventoryManager();
		this.queueManager = new QueueManager();
	}
	
    private void registerFile() {
        this.locationFile = new File(this.getDataFolder() + "/locations.yml");
        this.arenaFile = new File(this.getDataFolder() + "/arenas.yml");
        this.locationConfig = YamlConfiguration.loadConfiguration(this.locationFile);
        this.arenaConfig = YamlConfiguration.loadConfiguration(this.arenaFile);
    }
	
    private void registerLocation() {
        for (final LocationHelper locationHelper : LocationHelper.getAll()) {
            final String message = locationHelper.load() ? (ChatColor.DARK_RED + "Location register: " + ChatColor.WHITE + locationHelper.getName()) : (ChatColor.RED + "Location doesn't registered: " + ChatColor.DARK_RED + locationHelper.getName());
            this.getServer().getConsoleSender().sendMessage(message);
        }
    	if (ffa1.getLocation() != null) {
    		spawnFFA.add(ffa1.getLocation());
    		System.out.println("Spawn1 of FFA as been added to the virtual list.");
    	}
    	if (ffa2.getLocation() != null) {
    		spawnFFA.add(ffa2.getLocation());
    		System.out.println("Spawn2 of FFA as been added to the virtual list.");
    	}
    	if (ffa3.getLocation() != null) {
    		spawnFFA.add(ffa3.getLocation());
    		System.out.println("Spawn3 of FFA as been added to the virtual list.");
    	}
    }
    
    private void registerResource() {
        this.configPath = this.getDataFolder() + "/hikari.properties";
        this.saveResource("hikari.properties", false);
        this.saveResource("locations.yml", false);
        this.saveResource("arenas.yml", false);
    }

	private void registerListener() {
		final PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new ServerList(), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new ChatListener(), this);
		pm.registerEvents(new EntityListener(), this);
		pm.registerEvents(new ItemInteractListener(), this);
		pm.registerEvents(new InventoryListener(), this);
		
	}
	
    private void setupDatabase() {
        if (this.connection != null) {
            this.practiceDB.createPlayerManagerTable();
        }
        else {
            System.out.println("WARNING enter valid database information (" + this.configPath + ") \n You will not be able to access many features");
        }
    }
    
    private void setupHikariCP() {
        try {
            final HikariConfig config = new HikariConfig(this.configPath);
            @SuppressWarnings("resource")
			final HikariDataSource ds = new HikariDataSource(config);
            final String passwd = (config.getDataSourceProperties().getProperty("password") == null) ? "" : config.getDataSourceProperties().getProperty("password");
            final Database db = BukkitDB.createHikariDatabase(this, config.getDataSourceProperties().getProperty("user"), passwd, config.getDataSourceProperties().getProperty("databaseName"), config.getDataSourceProperties().getProperty("serverName") + ":" + config.getDataSourceProperties().getProperty("portNumber"));
            DB.setGlobalDatabase(db);
            this.connection = ds.getConnection();
        }
        catch (SQLException e) {
            System.out.println("Error could not connect to SQL database.");
            e.printStackTrace();
        }
        System.out.println("Successfully connected to the SQL database.");
    }
	
	public Connection getConnection() {
		return connection;
	}
	
	public PracticeSQL getPracticeDB() {
		return practiceDB;
	}
	
	public YamlConfiguration getLocationConfig() {
		return locationConfig;
	}
	
	public File getLocationFile() {
		return locationFile;
	}
	
	
	public YamlConfiguration getArenaConfig() {
		return arenaConfig;
	}
	
	public File getArenaFile() {
		return arenaFile;
	}
	
	public QueueManager getQueueManager() {
		return queueManager;
	}
	
	public LocationHelper getCheckingSpawn() {
		return checkingSpawn;
	}
	
	public LocationHelper getSpawn() {
		return spawn;
	}
	
	public LocationHelper getFfa1() {
		return ffa1;
	}
	
	public LocationHelper getFfa2() {
		return ffa2;
	}
	
	public LocationHelper getFfa3() {
		return ffa3;
	}
	
	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}
	
	public ItemManager getItemManager() {
		return itemManager;
	}
	
	public static Main getInstance() {
		return instance;
	}
}
