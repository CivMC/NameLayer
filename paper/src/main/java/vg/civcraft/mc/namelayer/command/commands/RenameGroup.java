package vg.civcraft.mc.namelayer.command.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vg.civcraft.mc.namelayer.GroupManager;
import vg.civcraft.mc.namelayer.NameAPI;
import vg.civcraft.mc.namelayer.command.BaseCommandMiddle;
import vg.civcraft.mc.namelayer.group.Group;
import vg.civcraft.mc.namelayer.permission.PermissionType;

public class RenameGroup extends BaseCommandMiddle {

	@CommandAlias("nlrg|renamegroup")
	@Syntax("<current_group> <new_name>")
	@Description("Renames a group")
	@CommandCompletion("@NL_Groups @nothing")
	public void execute(CommandSender sender, String currentGroup, String newGroupName) {
		if (currentGroup == null || currentGroup.isEmpty()) {
			sender.sendMessage(Component.text(currentGroup + " is not a valid group", NamedTextColor.RED));
			return;
		}
		if (newGroupName == null || newGroupName.isEmpty()) {
			sender.sendMessage(Component.text(currentGroup + " is not a valid name", NamedTextColor.RED));
			return;
		}
		boolean validGroupName = CreateGroup.validateGroupName(sender, newGroupName);
		if (!validGroupName) {
			sender.sendMessage(ChatColor.RED + "You used characters, which are not allowed");
			return;
		}
		Group targetGroup = GroupManager.getGroup(currentGroup);
		if (targetGroup == null) {
			sender.sendMessage(Component.text(currentGroup + " is not a valid group", NamedTextColor.RED));
			return;
		}
		if (!(sender instanceof Player player)) {
			//We are console here
			targetGroup.setName(newGroupName, true);
			sender.sendMessage(Component.text("We have changed the group " + currentGroup + " to " + newGroupName, NamedTextColor.GREEN));
			return;
		}
		boolean hasPerm = NameAPI.getGroupManager().hasAccess(targetGroup, player.getUniqueId(), PermissionType.getPermission("OWNER"));
		if (!hasPerm) {
			player.sendMessage(Component.text("You do not have the adequate permission to change this groups name!", NamedTextColor.RED));
			return;
		}
		//We do have permission here
		GroupManager.invalidateCache(targetGroup.getName());
		targetGroup.setName(newGroupName);
		GroupManager.getGroup(targetGroup.getName());
		player.sendMessage(Component.text("We have changed the group " + currentGroup + " to " + newGroupName, NamedTextColor.GREEN));
	}
}
