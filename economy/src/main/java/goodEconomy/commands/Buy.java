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
import goodEconomy.database.Transactions;
import goodEconomy.utilities.PriceLogic;
import me.vagdedes.mysql.database.SQL;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Buy  implements CommandExecutor {

	private GoodEconomy ins;
	private List<String> blacklist;
	public Buy () {
		this.ins = GoodEconomy.getInstance();
		this.blacklist = ins.getConfig().getStringList("blacklist");
	}
    @Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    	Permission perms = ins.getPermissions();
		if (!perms.has(arg0, "saggeco.buy")) {
			arg0.sendMessage(ChatColor.RED + "Invalid Permissions");
			return true;
		} else {
			
		Player player = arg0.getServer().getPlayer(arg0.getName());
	    Economy economy = ins.getEconomy();
		int howmuch = 0;
		int length = arg3.length;
		switch (length) {
		case 0:
			arg0.sendMessage(ChatColor.RED + "Sorry You have to specify what you want");
			return false;
		case 1:
			howmuch=1;
			break;
		case 2:
			try {
				howmuch = Integer.parseInt(arg3[1]);
			} catch (NumberFormatException e) {
				arg0.sendMessage(ChatColor.RED + "Sorry, you need an real number after itemname ");
			return false;
			}
    	}
		Optional<XMaterial> material = XMaterial.matchXMaterial(arg3[0]);
		try {
			material.get().name();
		}catch (NoSuchElementException e) {
			arg0.sendMessage(ChatColor.RED + "Sorry, you need have a real item ");
			return false;

		}
    	if (PriceLogic.BuyLogic( String.valueOf(howmuch) ,123 ) == null) {
			arg0.sendMessage(ChatColor.RED + "Sorry, you need a correct number 64, 32, 8 ... ");
			return false;
		}
		OfflinePlayer playername = Bukkit.getOfflinePlayer(player.getUniqueId());
		String itemname = material.get().name();
		Material actmat = material.get().parseMaterial();
		ItemStack item = new ItemStack (actmat, howmuch);
		if (!blacklist.contains(itemname)) {
				try {
  					if ( !Database.getListing( Listing.BLOCK , itemname, Listing.BLOCK).next()) {
  						arg0.sendMessage(ChatColor.RED + "Sorry, %item% hasn't been sold!".replace("%item%", itemname));
						return true;
						} else {
							if (player.getInventory().firstEmpty() == -1) {
		  						arg0.sendMessage(ChatColor.RED + "Sorry your inventory is full");
								return true;
							}
							Object test = SQL.get("price", new String[] {"blocks="+ "\""+itemname+"\""}, "mcserver");
							Object tests = SQL.get("sold", new String[] {"blocks="+ "\""+itemname+"\""}, "mcserver");
							//String sqlprice = Database.getListing( Listing.PRICE , itemname, Listing.BLOCK).getString("price");
							//String sqlsold = Database.getListing( Listing.SOLD , itemname, Listing.BLOCK).getString("sold");
							String newprice = PriceLogic.BuyLogic( String.valueOf(howmuch) , Integer.valueOf(test.toString())); 
							String newsold = String.valueOf(Integer.parseInt(tests.toString()) + howmuch);
							double finalprice = Double.valueOf(newprice);
							Double MarketFee = howmuch*ins.getConfig().getDouble("marketfee");
							finalprice += MarketFee;
							finalprice *= howmuch;
							if (finalprice <= 0) {
								finalprice = 0;
							}
							
							if (economy.getBalance(playername) >= Double.valueOf(finalprice)) {
								// Market Price
								String[] items = new String[] {player.getUniqueId().toString(), playername.getName().toString(), itemname, String.valueOf(0),String.valueOf(finalprice)};
								Database.addTransaction(items);
								
								economy.withdrawPlayer(playername, finalprice);
								Database.updateListing(Listing.PRICE, newprice, itemname);
								Database.updateListing(Listing.SOLD, newsold, itemname);
								arg0.getServer().getPlayer(arg0.getName()).getInventory().addItem(item);
								arg0.sendMessage(ChatColor.GREEN + "Sucess! You have bought %item%. Your balance is %bal%".replace("%item%", itemname).replace("%bal%", String.valueOf(economy.getBalance(playername))));
							} else {
								arg0.sendMessage(ChatColor.RED + "Sorry You don't have enough money, your %diff% short".replace("%diff%", String.valueOf(economy.getBalance(playername) - Double.valueOf(finalprice))));
							}
							return true;

						}
	  					} catch (SQLException e) {
							arg0.sendMessage("SQL ERROR");
							e.printStackTrace();
  						}
				} else {
			arg0.sendMessage(ChatColor.RED + "Sorry, %item% is to rare to be bought!".replace("%item%", itemname));
			return true;
		}

		
			return false;
	}
    }
}