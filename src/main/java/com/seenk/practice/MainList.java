package com.seenk.practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.seenk.practice.ladders.MainLadders;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.managers.QueueManager;
import com.seenk.practice.ladders.*;

public class MainList {
	
	 //Get the main in another class
	static MainList instance;
	
	private List<UUID> staffOnline;
	private List<String> chat;
	public List<MainLadders> ladders;
	public HashMap<Integer, UUID> queue;
	public Map<UUID, UUID> duelRequest;
	public List<UUID> partyList;
	public ArrayList<UUID> inFFA;
	public ArrayList<UUID> boselected;
	public static ArrayList<PlayerManager> haveRanked;
	public static ArrayList<UUID> answerduelselected;
	public static HashMap<String, QueueManager> fight;


	public MainList() {
		MainList.instance = this;
		this.ladders = Arrays.asList(new NoDebuff(), new Debuff(), new Sumo(), new Axe(), new Gapple(), new Archer(), new Soup());
		this.staffOnline =  new ArrayList<UUID>();
		this.chat = new ArrayList<String>();
		this.queue = new HashMap<Integer, UUID>();
		this.inFFA = new ArrayList<UUID>();
		this.boselected = new ArrayList<UUID>();
		this.setAnswerduelselected(new ArrayList<UUID>());
		this.duelRequest = new HashMap<UUID, UUID>();
		this.partyList = new ArrayList<UUID>();
		haveRanked = new ArrayList<>();
		this.fight = new HashMap<String, QueueManager>();
	}

	public static ArrayList<PlayerManager> getHaveRanked() {
		return haveRanked;
	}

	public List<UUID> getPartyList() {
		return partyList;
	}
	
	public Map<UUID, UUID> getDuelRequest() {
		return duelRequest;
	}
	
	public ArrayList<UUID> getInFFA() {
		return inFFA;
	}
	
	public Map<Integer, UUID> getQueue() {
		return queue;
	}
	
	public List<UUID> getStaffOnline() {
		return staffOnline;
	}
	
	public List<String> getChat() {
		return chat;
	}
	
	public static MainList getInstance() {
		return instance;
	}

	public ArrayList<UUID> getAnswerduelselected() {
		return answerduelselected;
	}

	public void setAnswerduelselected(ArrayList<UUID> answerduelselected) {
		this.answerduelselected = answerduelselected;
	}
}
