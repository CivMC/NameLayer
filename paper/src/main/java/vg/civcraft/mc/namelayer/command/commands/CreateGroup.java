package vg.civcraft.mc.namelayer.command.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Syntax;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vg.civcraft.mc.namelayer.GroupManager;
import vg.civcraft.mc.namelayer.NameAPI;
import vg.civcraft.mc.namelayer.NameLayerPlugin;
import vg.civcraft.mc.namelayer.RunnableOnGroup;
import vg.civcraft.mc.namelayer.command.BaseCommandMiddle;
import vg.civcraft.mc.namelayer.group.Group;

public class CreateGroup extends BaseCommandMiddle {

	@CommandAlias("nlcg|nlcreategroup|creategroup|create|ctcreate|creategroup")
	@Syntax("<group> [password]")
	@Description("Create a group (Public or Private). Password is optional.")
	public void execute(Player sender, String groupName, @Optional String userPassword) {
		Player p = (Player) sender;
		String name = groupName;
		int currentGroupCount = gm.countGroups(p.getUniqueId());
		
		if (NameLayerPlugin.getInstance().getGroupLimit() < currentGroupCount + 1 && !(p.isOp() || p.hasPermission("namelayer.admin"))){
			p.sendMessage(ChatColor.RED + "You cannot create any more groups! Please delete an un-needed group before making more.");
			return;
		}
		
		//enforce regulations on the name
		boolean validName = validateGroupName(p, name);
		
		if(!validName) {
			p.sendMessage(ChatColor.RED + "You used characters, which are not allowed");
			return;
		}
		
		if (GroupManager.getGroup(name) != null){
			p.sendMessage(ChatColor.RED + "That group is already taken. Try another unique group name.");
			return;
		}
		String password = "";
		if (userPassword != null) {
			password = userPassword;
		} else {
			password = null;
		}
		final UUID uuid = NameAPI.getUUID(p.getName());
		Group g = new Group(name, uuid, false, password, -1, System.currentTimeMillis());
		gm.createGroupAsync(g, new RunnableOnGroup() {
			@Override
			public void run() {
				Player p = null;
				p = Bukkit.getPlayer(uuid);
				Group g = getGroup();
				if (p != null) {
					if (g.getGroupId() == -1) { // failure
						p.sendMessage(ChatColor.RED + "That group is already taken or creation failed.");
					}
					p.sendMessage(ChatColor.GREEN + "The group " + g.getName() + " was successfully created.");
				} else {
					NameLayerPlugin.getInstance().getLogger().log(Level.INFO, "Group {0} creation complete resulting in group id: {1}",
							new Object[] {g.getName(), g.getGroupId()});
				}
			}
		}, false);
		if (NameLayerPlugin.getInstance().getGroupLimit() == (currentGroupCount + 1)){
			p.sendMessage(ChatColor.YELLOW + "You have reached the group limit with " + NameLayerPlugin.getInstance().getGroupLimit() + " groups! Please delete un-needed groups if you wish to create more.");
		}
		p.sendMessage(ChatColor.GREEN + "Group creation request is in process.");
	}

	/**
	 * Returns if the group name was validated successfully
	 * @param sender Player or console
	 * @param groupName Group name to validate
	 * @return True if the group name is fine, false if it violated any of our constraints
	 */
	public static boolean validateGroupName(CommandSender sender, String groupName) {
		if (groupName.length() > 32) {
			sender.sendMessage(ChatColor.RED + "The group name is not allowed to contain more than 32 characters");
			return false;
		}
		Charset latin1 = StandardCharsets.ISO_8859_1;
		boolean invalidCharacters = false;
		if (!latin1.newEncoder().canEncode(groupName)) {
			invalidCharacters = true;
		}
		//cant allow them to hurt mercury :(
		if (groupName.contains("|")) {
			invalidCharacters = true;
		}

		for(char c:groupName.toCharArray()) {
			if (Character.isISOControl(c)) {
				invalidCharacters = true;
			}
		}
		return !invalidCharacters;
	}
}
