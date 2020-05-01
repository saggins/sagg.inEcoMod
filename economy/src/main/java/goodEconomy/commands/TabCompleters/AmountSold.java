package goodEconomy.commands.TabCompleters;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;




public class AmountSold implements TabCompleter {

	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		switch (arg3.length) {
		case 1:
			List<String> list = Arrays.asList("64", "32", "16", "8", "1");	
			return list;
		/**case 2:

		    XMaterial[] mat = XMaterial.values();
		    List<String> fList = Lists.newArrayList();
			for (XMaterial s : mat) {
		        if (s.name().toLowerCase().startsWith(arg3[0].toLowerCase())) {
		          fList.add(s.name().toLowerCase());
		        }
		      }
		      return fList;
		}**/
	}
		return null;

	
}
}


