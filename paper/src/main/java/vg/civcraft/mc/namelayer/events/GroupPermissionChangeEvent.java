package vg.civcraft.mc.namelayer.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import vg.civcraft.mc.namelayer.GroupManager.PlayerType;
import vg.civcraft.mc.namelayer.group.Group;
import vg.civcraft.mc.namelayer.permission.PermissionType;

public class GroupPermissionChangeEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean isCancelled = false;

	private Group group;
	private PlayerType playerType;
	private PermissionType permission;
	private ChangeType changeType;
	private Player player;

	public GroupPermissionChangeEvent(Group group, PlayerType playerType, PermissionType permission, ChangeType changeType, Player player){
		this.group = group;
		this.playerType = playerType;
		this.permission = permission;
		this.changeType = changeType;
		this.player = player;
	}

	/**
	 * @return Group for which permission was removed
	 */
	public Group getGroup(){
		return group;
	}

	/**
	 * @return Player type from which the permission was taken
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}

	/**
	 * @return Permission changed
	 */
	public PermissionType getPermission() {
		return permission;
	}

	/**
	 * @return Type of change that occurred
	 */
	public ChangeType getChange(){
		return changeType;
	}

	/**
	 * @return The player, if any, responsible for the change
	 */
	public Player getPlayer(){
		return player;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean value) {
		isCancelled = value;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum ChangeType {
		ADD,
		REMOVE
	}

}
