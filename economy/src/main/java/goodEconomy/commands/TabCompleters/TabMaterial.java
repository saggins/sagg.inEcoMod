package goodEconomy.commands.TabCompleters;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Lists;

public final class TabMaterial implements TabCompleter {

	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		 switch (arg3.length) {
		   case 1:
			   XMaterial[] matlist = XMaterial.values();
			   List<String> fList = Lists.newArrayList();
		       for (XMaterial s : matlist) {
		    	   if (s.name().toLowerCase().startsWith(arg3[0].toLowerCase())) {
		    		   fList.add(s.name().toLowerCase());
		    	   }
		      }
		      return fList;
		 }
		return null;
	}
	
}
