package goodEconomy.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class TestBuy implements CommandExecutor  {
    @Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		String itemname = arg3[0].toUpperCase();
		Material material = Material.getMaterial(itemname);
		if( material.isItem() ) {
			ItemStack item = new ItemStack(material, 1);
			arg0.getServer().getPlayer(arg0.getName()).getInventory().addItem(item);
		} else {
			
			arg0.sendMessage("Sorry, %item% isn't the correct item".replace("%item%", itemname));
			return true;
		}
		
		return false;
	}
}

