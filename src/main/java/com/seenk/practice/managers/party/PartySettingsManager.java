package com.seenk.practice.managers.party;

import java.util.ArrayList;

public class PartySettingsManager {
	
	private boolean open;
	private ArrayList<PartyManager> party = new ArrayList<PartyManager>();
	
	public PartySettingsManager(final PartyManager party) {
		this.open = false;
		this.party.add(party);
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public boolean isOpen() {
		return open;
	}

}
