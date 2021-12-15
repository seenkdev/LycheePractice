package com.seenk.practice.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.seenk.practice.managers.match.MatchType;

public class QueueManager {
    private UUID normalQueuePlayer;
    private UUID classeQueuePlayer;
    private String nameLadder;
    private HashMap<String, PlayerManager> rankedQueuePlayer;
    private ArrayList<PlayerManager> ranked;
    private HashMap<String, UUID> unrankedQueue;
    private Map<MatchType, List<MatchManager>> fightPlayer;
    
    public QueueManager() {
        this.ranked = new ArrayList<PlayerManager>();
        this.rankedQueuePlayer = new HashMap<String, PlayerManager>();
        this.unrankedQueue = new HashMap<String, UUID>();
        (this.fightPlayer = new HashMap<MatchType, List<MatchManager>>()).put(MatchType.UNRANKED, new ArrayList<MatchManager>());
        this.fightPlayer.put(MatchType.RANKED, new ArrayList<MatchManager>());
    }
    
    public ArrayList<PlayerManager> getRanked() {
		return ranked;
	}
    
    public void setRanked(ArrayList<PlayerManager> ranked) {
		this.ranked = ranked;
	}
    
    public void setClasseQueuePlayer(UUID classeQueuePlayer) {
		this.classeQueuePlayer = classeQueuePlayer;
	}
    
    public UUID getClasseQueuePlayer() {
		return classeQueuePlayer;
	}
    
    public void setNameLadder(String nameLadder) {
		this.nameLadder = nameLadder;
	}
    
    public String getNameLadder() {
		return nameLadder;
	}
    
    @SuppressWarnings("incomplete-switch")
	public int getQueue(final MatchType fightType) {
    	switch (fightType) {
		case RANKED:
			return (this.getRankedQueuePlayer() == null) ? 0 : this.getRankedQueuePlayer().size();
		case UNRANKED:
			return this.normalQueuePlayer != null ? 1 : 0; 	
    	}
		return 0;
    }
    
    public HashMap<String, PlayerManager> getRankedQueuePlayer() {
		return rankedQueuePlayer;
	}
    
    public int getFight(final MatchType fightType) {
        if (this.fightPlayer.get(fightType) == null) {
            return 0;
        }
        return this.fightPlayer.get(fightType).size() * 2;
    }
    
    public HashMap<String, UUID> getUnrankedQueue() {
		return unrankedQueue;
	}
    
    public UUID getNormalQueuePlayer() {
        return this.normalQueuePlayer;
    }
    
    public Map<MatchType, List<MatchManager>> getFightPlayer() {
        return this.fightPlayer;
    }
    
    public void setNormalQueuePlayer(final UUID normalQueuePlayer) {
        this.normalQueuePlayer = normalQueuePlayer;
    }
    
    public void setFightPlayer(final Map<MatchType, List<MatchManager>> fightPlayer) {
        this.fightPlayer = fightPlayer;
    }
}