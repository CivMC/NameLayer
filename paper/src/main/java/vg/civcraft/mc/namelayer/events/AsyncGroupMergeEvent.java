package vg.civcraft.mc.namelayer.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.sql.Connection;

public class AsyncGroupMergeEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	private final Connection connection; // the connection currently running the transaction, an error will cause the whole merge to rollback
	private final int beingMerged; // the group that will join into another
	private final int mergingInto; // the group that is receiving the other

	public AsyncGroupMergeEvent(int group, int toBeMerged, Connection connection) {
		super(true);
		this.mergingInto = group;
		this.beingMerged = toBeMerged;
		this.connection = connection;
	}
	/**
	 * @return Returns the group to be merged.
	 */
	public int getToBeMerged(){
		return beingMerged;
	}
	/**
	 * @return Returns the group that will be left after the merging.
	 */
	public int getMergingInto(){
		return mergingInto;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
