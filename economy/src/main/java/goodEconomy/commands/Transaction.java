package goodEconomy.commands;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import goodEconomy.GoodEconomy;
import net.milkbowl.vault.permission.Permission;

public class Transaction {

	protected GoodEconomy ins;
	protected List<String> blacklist;
	protected int ammount;
	protected String itemname;
	protected ItemStack item;
	protected Optional<XMaterial> material;
	protected Player player;
	protected int givenAmmount;
	public Transaction () {
		this.ins = GoodEconomy.getInstance();
		this.blacklist = ins.getConfig().getStringList("blacklist");
	}
	
	public boolean checkPerms(CommandSender sender) {
    	Permission perms = ins.getPermissions();
		if (!perms.has(sender, "saggeco.sell")) {
			sender.sendMessage(ChatColor.RED + "Invalid Permissions");
			return true;
		}else {
			return false;
		}
	}
	public int getAmmount(String[] args, CommandSender sender) throws NumberFormatException {
		int howmuch;
		if (args.length != 0) {
			howmuch = Integer.parseInt(args[0]);
		}
		else {
			howmuch = 1;
		}
		return howmuch;
	}
	public String getMaterial( CommandSender sender)throws NoSuchElementException {
		player = sender.getServer().getPlayer(sender.getName());
		item = player.getInventory().getItemInMainHand();
		String testitem = item.toString().trim().split("\\{")[1].split(" ")[0];
		material = XMaterial.matchXMaterial(testitem);
		return 	material.get().name();
	}
	
	public boolean checkBlacklist() {
		if (this.blacklist.contains(itemname)) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean checkAmmount(CommandSender sender) {
		givenAmmount = item.getAmount();
  	  	int itmCheck = givenAmmount - ammount;
  	  	if (itmCheck < 0) {
  	  		sender.sendMessage(ChatColor.RED + "Sorry, You don't have enough %item% in your inventory".replace("%item%", itemname));
  	  		return false;
  	  	}
		return true;
	}
	
	
	public boolean doChecks (CommandSender sender, String[] args) {
		if(checkPerms(sender)) {
			sender.sendMessage(ChatColor.RED + "Invalid Permissions");
			return false;
		}
		//Test the ammount given
		try {
			ammount = getAmmount(args, sender);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Sorry, you need an real number after itemname ");
			return false;
		}
		//Test the item
		try {
			itemname = getMaterial(sender);
		}catch (NoSuchElementException e) {
			sender.sendMessage(ChatColor.RED + "Sorry, you need have a real item ");
			return false;
		}
		
		if(checkBlacklist()) {
			sender.sendMessage(ChatColor.RED + "Sorry, %item% is to rare to be sold!".replace("%item%", itemname));
			return false;
		}
		if (!checkAmmount(sender)) {
			return false;
		}
		return true;
		
		
	}

}
