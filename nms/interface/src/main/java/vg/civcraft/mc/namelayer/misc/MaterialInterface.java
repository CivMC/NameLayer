package vg.civcraft.mc.namelayer.misc;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface MaterialInterface {

	public enum Specific {
		GREEN, RED, BACK, MOD, BLACKLIST, PERMS, MERGE, DEFAULT;
	}

	Material getMaterial(MaterialInterface.Specific specific);
	ItemStack getItemStack(MaterialInterface.Specific specific);
}

