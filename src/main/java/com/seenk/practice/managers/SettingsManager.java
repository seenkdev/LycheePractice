package com.seenk.practice.managers;

public class SettingsManager {
	
	private boolean scoreboard;
	private boolean chat;
	private boolean privateMessage;
	private boolean duel;
	private boolean partyInvite;
	
	public SettingsManager(final PlayerManager pm) {
		this.scoreboard = true;
		this.chat = true;
		this.duel = true;
		this.privateMessage = true;
		this.partyInvite = true;
	}
	
	public boolean isChat() {
		return chat;
	}
	
	public boolean isDuel() {
		return duel;
	}
	
	public boolean isPartyInvite() {
		return partyInvite;
	}
	
	public boolean isPrivateMessage() {
		return privateMessage;
	}
	
	public boolean isScoreboard() {
		return scoreboard;
	}

	public void setChat(boolean chat) {
		this.chat = chat;
	}
	
	public void setDuel(boolean duel) {
		this.duel = duel;
	}
	
	public void setPartyInvite(boolean partyInvite) {
		this.partyInvite = partyInvite;
	}
	
	public void setPrivateMessage(boolean privateMessage) {
		this.privateMessage = privateMessage;
	}
	
	public void setScoreboard(boolean scoreboard) {
		this.scoreboard = scoreboard;
	}
}
