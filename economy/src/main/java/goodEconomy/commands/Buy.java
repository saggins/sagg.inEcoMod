package goodEconomy.commands;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import goodEconomy.GoodEconomy;
import goodEconomy.database.Database;
import goodEconomy.database.Listing;
import goodEconomy.utilities.PriceLogic;
import me.vagdedes.mysql.database.SQL;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Buy  extends Transaction implements CommandExecutor {
	Boolean hasnum;

	public String getMaterial( CommandSender sender, String[] args)throws NoSuchElementException {
		if (args.length==0) {
			item = player.getInventory().getItemInMainHand().asOne();
			String testitem = item.toString().trim().split("\\{")[1].split(" ")[0];
			material = XMaterial.matchXMaterial(testitem);
		} else {
			material = XMaterial.matchXMaterial(args[0]); 
			Material actmat = material.get().parseMaterial();
			item = new ItemStack (actmat, ammount);
		}
		return 	material.get().name();
	}
	
	
	public boolean doChecks (CommandSender sender, String[] args) {
		if(checkPerms(sender)) {
			sender.sendMessage(ChatColor.RED + "Invalid Permissions");
			return false;
		}
		
		if (hasnum) {
			try {
				ammount = Integer.parseInt(args[１]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Sorry, you need an real number after itemname ");
				return false;
			}
		}else {
			ammount=1;
		}
			
		try {
			itemname = getMaterial(sender, args);
		}catch (NoSuchElementException e) {
			sender.sendMessage(ChatColor.RED + "Sorry, you need have a real item ");
			return false;
		}
		
		//Test the item
		
		
		if(checkBlacklist()) {
			sender.sendMessage(ChatColor.RED + "Sorry, %item% is to rare to be sold!".replace("%item%", itemname));
			return false;
		}
		if (!checkAmmount(sender)) {
			return false;
		}
		return true;
		
	}

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
 		int length = arg3.length;
		switch (length) {

		case 1:
			hasnum = false;
			break;
		case 2:
			hasnum = true;
    	}
		if (!doChecks(arg0, arg3)) {
			return false;
		}
			
		Player player = arg0.getServer().getPlayer(arg0.getName());
	    Economy economy = ins.getEconomy();
		OfflinePlayer playername = Bukkit.getOfflinePlayer(player.getUniqueId());
		
		
		try {
			if ( !Database.getListing( Listing.BLOCK , itemname, Listing.BLOCK).next()) {
				arg0.sendMessage(ChatColor.RED + "Sorry, %item% hasn't been sold!".replace("%item%", itemname));
				return true;
			} 
			if (player.getInventory().firstEmpty() == -1) {
				arg0.sendMessage(ChatColor.RED + "Sorry your inventory is full");
				return true;
			}
			Object dbprice = SQL.get("price", new String[] {"blocks="+ "\""+itemname+"\""}, "mcserver");
			Object dbsold = SQL.get("sold", new String[] {"blocks="+ "\""+itemname+"\""}, "mcserver");

			PriceLogic buyLogic = new PriceLogic( ammount, Double.parseDouble(dbprice.toString()));
			buyLogic.doBuy();
			if (!buyLogic.checkInteger()) {
				arg0.sendMessage(ChatColor.RED + "Sorry, the price of %item% is too low to sell :P".replace("%item%", itemname));
				return true;
			}
			
			Double finalprice = buyLogic.getFinalPrice();
			Double newprice = buyLogic.getNewPrice();
			String newsold = dbsold.toString();
			
			if (economy.getBalance(playername) >= Double.valueOf(finalprice))
			{
				// Market Price
				long unixTime = System.currentTimeMillis() / 1000L;								
				String[] items = new String[] {player.getUniqueId().toString(), playername.getName().toString(), itemname, String.valueOf(0),String.valueOf(finalprice), String.valueOf(unixTime)};
				Database.addTransaction(items);
				
				economy.withdrawPlayer(playername, finalprice);
				Database.updateListing(Listing.PRICE, String.valueOf(newprice), itemname);
				Database.updateListing(Listing.SOLD, newsold, itemname);
				arg0.getServer().getPlayer(arg0.getName()).getInventory().addItem(item);
				arg0.sendMessage(ChatColor.GREEN + "Sucess! You have bought %item%. It costed you %bal%!. The new price of %item% is now %price%"
						.replace("%price%", String.valueOf(newprice)).replace("%item%", itemname).replace("%bal%", String.valueOf(finalprice)));							} else {
				arg0.sendMessage(ChatColor.RED + "Sorry You don't have enough money, your %diff% short".replace("%diff%", String.valueOf(economy.getBalance(playername) - Double.valueOf(finalprice))));
			}
			return true;
		} catch (SQLException e) {
			arg0.sendMessage("SQL ERROR");
			e.printStackTrace();
			return true;
		}
	}
}