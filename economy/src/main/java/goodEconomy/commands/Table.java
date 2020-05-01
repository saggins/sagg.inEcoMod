package goodEconomy.commands;

import java.sql.SQLException;
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
import goodEconomy.database.Database;
import goodEconomy.database.Listing;
import me.vagdedes.mysql.database.SQL;
import net.milkbowl.vault.permission.Permission;

public class Table implements CommandExecutor {
	
	private GoodEconomy ins;
	private List<String> blacklist;
	public Table () {
		this.ins = GoodEconomy.getInstance();
		this.blacklist = ins.getConfig().getStringList("blacklist");
	}
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Permission perms = ins.getPermissions();
		Player player = arg0.getServer().getPlayer(arg0.getName());
		String itemname;
		Optional<XMaterial> material;
		
		if (!perms.has(arg0, "saggeco.table")) {
			arg0.sendMessage(ChatColor.RED + "Invalid Permissions");
			return true;
		} else {
		int length = arg3.length;
		switch (length) {
		case 1:
			material = XMaterial.matchXMaterial(arg3[0]);
			try {
				material.get().name();
			}catch (NoSuchElementException e) {
				arg0.sendMessage(ChatColor.RED + "Sorry, you need have a real item ");
				return false;

			}
			
			itemname = material.get().name();
		default:
			ItemStack item = player.getInventory().getItemInMainHand();
			String testitem = item.toString().trim().split("\\{")[1].split(" ")[0];
			material = XMaterial.matchXMaterial(testitem);
			try {
				material.get().name();
			}catch (NoSuchElementException e) {
				arg0.sendMessage(ChatColor.RED + "Sorry, you need have a real item ");
				return false;

			}
			itemname = material.get().name();
			break;
		}

		//CHECK itmem
		if (!blacklist.contains(itemname)) {
			try {
				if ( !Database.getListing( Listing.BLOCK , itemname, Listing.BLOCK).next()) {
					arg0.sendMessage(ChatColor.RED + "Sorry Noone has sold this item yet :(");
					return true;
				}
				else {
					Object test = SQL.get("price", new String[] {"blocks="+ "\""+itemname+"\""}, "mcserver");
					arg0.sendMessage(ChatColor.GREEN + "The price for " + itemname + " is %price%".replace("%price%", test.toString()));
					

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

}
	

}
