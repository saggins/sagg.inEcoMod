package goodEconomy.commands.TabCompleters;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.google.common.collect.Lists;

public class TabListings implements TabCompleter {

	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

	    Material[] list = Material.values();
	    List<String> fList = Lists.newArrayList();
	   
	
	
	    if (arg3.length == 1) {
	      for (Material s : list) {
	        if (s.name().toLowerCase().startsWith(arg3[0].toLowerCase())) {
	          fList.add(s.name().toLowerCase());
	        }
	      }
	      return fList;
	    }
	    return null;
	}
} 
