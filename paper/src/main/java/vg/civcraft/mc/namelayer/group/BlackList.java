package vg.civcraft.mc.namelayer.group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import vg.civcraft.mc.namelayer.NameLayerPlugin;

public class BlackList {
	private Map<Integer, Set<UUID>> blacklistsByGroup;
	
	public BlackList() {
		blacklistsByGroup = new HashMap<>();
	}
	
	public Set<UUID> getBlacklist(Group g) {
		return getBlacklist(g.getGroupId());
	}
	
	public Set<UUID> getBlacklist(int group) {
		Set<UUID> black = blacklistsByGroup.get(group);
		if (black == null) {
			loadBlacklistMembersFromDb(group);
			black = blacklistsByGroup.get(group);
		}
		return black;
	}
	
	public boolean isBlacklisted(Group group, UUID uuid) {
		return isBlacklisted(group.getGroupId(), uuid);
	}
	
	public boolean isBlacklisted(int group, UUID uuid) {
		Set <UUID> ids = blacklistsByGroup.get(group);
		if (ids == null) {
			loadBlacklistMembersFromDb(group);
			ids = blacklistsByGroup.get(group);
		}
		if (ids != null && ids.contains(uuid)) {
			return true;
		}
		return false;
	}
	
	public void loadBlacklistMembersFromDb(int group) {
		blacklistsByGroup.put(group, NameLayerPlugin.getGroupManagerDao().getBlackListMembers(group));
	}
	
	public void initEmptyBlackList(int group) {
		blacklistsByGroup.put(group, new HashSet<UUID>());
	}
	
	public void addBlacklistMember(Group group, UUID uuid, boolean writeToDb) {
		addBlacklistMember(group.getGroupId(), uuid, writeToDb);
	}
	
	public void addBlacklistMember(int group, UUID uuid, boolean writeToDb) {
		Set <UUID> ids = blacklistsByGroup.get(group);
		if (ids == null) {
			loadBlacklistMembersFromDb(group);
			ids = blacklistsByGroup.get(group);
		}
		if (ids != null && !ids.contains(uuid)) {
			ids.add(uuid);
			if (writeToDb) {
				NameLayerPlugin.getGroupManagerDao().addBlackListMember(group, uuid);
			}
		}
	}
	
	public void removeBlacklistMember(Group group, UUID uuid, boolean writeToDb) {
		removeBlacklistMember(group.getGroupId(), uuid, writeToDb);
	}
	
	public void removeBlacklistMember(int group, UUID uuid, boolean writeToDb) {
		Set <UUID> ids = blacklistsByGroup.get(group);
		if (ids == null) {
			loadBlacklistMembersFromDb(group);
			ids = blacklistsByGroup.get(group);
		}
		if (ids != null && ids.contains(uuid)) {
			ids.remove(uuid);
			if (writeToDb) {
				NameLayerPlugin.getGroupManagerDao().removeBlackListMember(group, uuid);
			}
		}
	}
	
	public void removeFromCache(String groupName) {
		blacklistsByGroup.remove(groupName);
	}
	
}
