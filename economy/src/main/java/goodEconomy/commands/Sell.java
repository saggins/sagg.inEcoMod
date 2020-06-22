package goodEconomy.commands;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.cryptomorin.xseries.XMaterial;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import goodEconomy.GoodEconomy;
import goodEconomy.database.Database;
import goodEconomy.database.Listing;
import goodEconomy.utilities.PriceLogic;
import me.vagdedes.mysql.database.SQL;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Sell  extends Transaction implements CommandExecutor {

	
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)  {
		if (!doChecks(arg0, arg3)) {
			return false;
		}
				
	    Economy economy = ins.getEconomy();
	    OfflinePlayer playername = Bukkit.getOfflinePlayer(player.getUniqueId());
		try {
		  	// Checks if new item
		if ( !Database.getListing( Listing.BLOCK , itemname, Listing.BLOCK).next()) { 
			
			Database.addListing(Listing.BLOCK, itemname);
			Database.updateListing(Listing.PRICE,  ins.getConfig().getString("deafultprice"), itemname);
			Database.updateListing(Listing.SOLD, String.valueOf(ammount), itemname);
		}
		Object dbprice = SQL.get("price", new String[] {"blocks="+ "\""+itemname+"\""}, "mcserver");
		Object dbsold = SQL.get("sold", new String[] {"blocks="+ "\""+itemname+"\""}, "mcserver");
		PriceLogic sellLogic = new PriceLogic( ammount, Double.parseDouble(dbprice.toString()));
		sellLogic.doSell();
		
		if (!sellLogic.checkInteger()) {
			arg0.sendMessage(ChatColor.RED + "Sorry, the price of %item% is too low to sell :P".replace("%item%", itemname));
			return true;
		}
		
		Double finalprice = sellLogic.getFinalPrice();
		Double newprice = sellLogic.getNewPrice();
		String newsold = dbsold.toString();
		 
		long unixTime = System.currentTimeMillis() / 1000L;								
		String[] items = new String[] {player.getUniqueId().toString(), playername.getName().toString(), itemname, String.valueOf(1),String.valueOf(finalprice), String.valueOf(unixTime)};
		
		if ( newprice <= 1) {
			ItemStack olditem = new ItemStack(material.get().parseMaterial(), ammount);
			player.getInventory().addItem(olditem);
			arg0.sendMessage(ChatColor.RED + "Sorry, the price of %item% is too low to sell :P".replace("%item%", itemname));
			return true;
		} else {
  		  	//Gets rid of item
  		  	item.setAmount(givenAmmount - ammount);
			player.getInventory().setItem(player.getInventory().getHeldItemSlot(), item);
			String dbNewPrice = String.valueOf(newprice);
			Database.updateListing(Listing.PRICE, dbNewPrice, itemname);
			Database.updateListing(Listing.SOLD, newsold, itemname);
			Database.addTransaction(items);
						
			economy.depositPlayer(playername, finalprice);
			arg0.sendMessage(ChatColor.GREEN + "Sucess! You have sold %item%. It costed you %bal%!. The new price of %item% is now %price%"
					.replace("%price%", String.valueOf(newprice)).replace("%item%", itemname).replace("%bal%", String.valueOf(finalprice)));
			return true;
		}
		// Update newprice/newsold
		// Add transaction for gdpb rpice
				
  	  	} catch (SQLException e) {
			// TODO Auto-generated catch block
			arg0.sendMessage("SQL ERROR");
			e.printStackTrace();
			return false;
  	  	}
		
    }
}
