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

public class Sell  implements CommandExecutor {

	private GoodEconomy ins;
	private List<String> blacklist;
	public Sell () {
		this.ins = GoodEconomy.getInstance();
		this.blacklist = ins.getConfig().getStringList("blacklist");
	}
    @Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    	Permission perms = ins.getPermissions();
		if (!perms.has(arg0, "saggeco.sell")) {
			arg0.sendMessage(ChatColor.RED + "Invalid Permissions");
			return true;
		} else {
			
		
		Player player = arg0.getServer().getPlayer(arg0.getName());
	    Economy economy = ins.getEconomy();
		int howmuch;
		if (arg3.length != 0) {
			try {
				howmuch = Integer.parseInt(arg3[0]);
			} catch (NumberFormatException e) {
				arg0.sendMessage(ChatColor.RED + "Sorry, you need an real number after itemname ");
			return false;
			}
		}
		else {
			howmuch = 1;
		}
		if (PriceLogic.BuyLogic( String.valueOf(howmuch) ,123 ) == null) {
			arg0.sendMessage(ChatColor.RED + "Sorry, you need a correct number 64, 32, 8 ... ");
			return false;
		}
		ItemStack item = player.getInventory().getItemInMainHand();
		String testitem = item.toString().trim().split("\\{")[1].split(" ")[0];
		Optional<XMaterial> material = XMaterial.matchXMaterial(testitem);
		try {
			material.get().name();
		}catch (NoSuchElementException e) {
			arg0.sendMessage(ChatColor.RED + "Sorry, you need have a real item ");
			return false;

		}
		String itemname = material.get().name();
		if (!blacklist.contains(itemname)) {
				try {
					  int itemammount = item.getAmount();
	            	  int itmCheck = itemammount - howmuch;
	            	  if (itmCheck < 0) {
	            		  arg0.sendMessage(ChatColor.RED + "Sorry, You don't have enough %item% in your inventory".replace("%item%", itemname));
	            		  return true;
	            	  } else {
	            		  item.setAmount(itemammount - howmuch);
	            		  player.getInventory().setItem(player.getInventory().getHeldItemSlot(), item );
  					if ( !Database.getListing( Listing.BLOCK , itemname, Listing.BLOCK).next()) {
	            		  OfflinePlayer playername = Bukkit.getOfflinePlayer(player.getUniqueId());

  						//TODO:give money
						Database.addListing(Listing.BLOCK, itemname);
						Database.updateListing(Listing.PRICE,  ins.getConfig().getString("deafultprice"), itemname);
						Database.updateListing(Listing.SOLD, String.valueOf(howmuch), itemname);
						arg0.sendMessage(ChatColor.GREEN + "Congrats, %item% has been sold!".replace("%item%", itemname));
						Double finalprice = Double.valueOf(howmuch *  Integer.parseInt(ins.getConfig().getString("deafultprice")));
						economy.depositPlayer(playername, finalprice );
						return true;
						} else {
		            		  OfflinePlayer playername = Bukkit.getOfflinePlayer(player.getUniqueId());

							Object test = SQL.get("price", new String[] {"blocks="+ "\""+itemname+"\""}, "mcserver");
							Object tests = SQL.get("sold", new String[] {"blocks="+ "\""+itemname+"\""}, "mcserver");
							String newprice = PriceLogic.SellLogic( String.valueOf(howmuch) ,Integer.parseInt( test.toString() )); 
							String newsold = String.valueOf(Integer.parseInt(tests.toString()) + howmuch);
							double finalprice = Double.valueOf(newprice);
							finalprice *= howmuch;
							if (finalprice <= 0) {
								finalprice = 0;
								ItemStack olditem = new ItemStack(material.get().parseMaterial(), howmuch);
			            		player.getInventory().addItem(olditem);
			        			arg0.sendMessage(ChatColor.RED + "Sorry %item% has too low of a price".replace("%item%", itemname));
			        			return true;
							}
							Database.updateListing(Listing.PRICE, newprice, itemname);
							Database.updateListing(Listing.SOLD, newsold, itemname);
							economy.depositPlayer(playername, finalprice);
							arg0.sendMessage(ChatColor.GREEN + "Sucess! You have sold %item%. Your balance is %bal%".replace("%item%", itemname).replace("%bal%", String.valueOf(economy.getBalance(playername))));
							return true;

						}
		        }
        	  }
					catch (SQLException e) {
					// TODO Auto-generated catch block
					arg0.sendMessage("SQL ERROR");
					e.printStackTrace();
				}
			} else {
			arg0.sendMessage(ChatColor.RED + "Sorry, %item% is to rare to be sold!".replace("%item%", itemname));
			return true;
		}
		
			return false;
	}
    }
}
