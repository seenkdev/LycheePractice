package com.seenk.practice.database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.seenk.practice.Main;

import co.aikar.idb.DB;
import co.aikar.idb.DbStatement;

public class PracticeSQL {

    public boolean createPlayerManagerTable() {
        return DB.createTransaction(stm -> createPlayerManagerTable(stm));
    }

    private boolean createPlayerManagerTable(DbStatement stm) {
        String player_manager = "CREATE TABLE playersdata ("
                + "ID INT(64) NOT NULL AUTO_INCREMENT,"
                + "name VARCHAR(16) NOT NULL,"
                + "uuid VARCHAR(64) NOT NULL,"
                + "ban VARCHAR(16) NOT NULL,"
                + "mute VARCHAR(16) NOT NULL,"
                + "rank VARCHAR(16) DEFAULT 'default',"
                + "prefix VARCHAR(16) DEFAULT '§a',"
                + "permissions VARCHAR(64) DEFAULT 'player',"
                + "elos VARCHAR(255) DEFAULT '1000:1000:1000:1000:1000:1000:1000:1000:1000:1000',"
                + "PRIMARY KEY (`ID`))";
        try {
            DatabaseMetaData dbm = Main.getInstance().getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, "playersdata", null);
            if (tables.next()) {
                //table exist
                return false;
            } else {
                //table doesn't exist
                stm.executeUpdateQuery(player_manager);
                System.out.println("SUCESS create playersdata table.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("ERROR while creating playersdata table.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean createPlayerManager(UUID uuid, String name, String ban, String mute, String rank, String prefix, String permissions)
    {
        return DB.createTransaction(stm -> createPlayerManager(uuid, name, ban, mute, rank, prefix, permissions, stm));
    }
    

    private boolean createPlayerManager(UUID uuid, String name, String ban, String mute,  String rank, String prefix, String permissions, DbStatement stm) {
        String query = "INSERT INTO playersdata (uuid, name, ban, mute, rank, prefix, permissions) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            return stm.executeUpdateQuery(query, uuid.toString(), name, ban, mute, rank, prefix, permissions) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    public boolean updatePlayerManager(String name, UUID uuid)
    {
        return DB.createTransaction(stm -> updatePlayerManager(name, uuid, stm));
    }
    

    private boolean updatePlayerManager(String name, UUID uuid, DbStatement stm) {
        String query = "UPDATE playersdata SET name=? WHERE uuid=?";
        try {
            return stm.executeUpdateQuery(query, name, uuid.toString()) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean existPlayerManager(UUID uuid)
    {
        return DB.createTransaction(stm -> existPlayerManager(uuid, stm));
    }
    
    private boolean existPlayerManager(UUID uuid, DbStatement stm) {
        String query = "SELECT * FROM playersdata WHERE uuid=?";
        try {
            return stm.executeQueryGetFirstRow(query, uuid.toString()) != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}