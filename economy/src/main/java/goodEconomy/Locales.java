package goodEconomy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import goodEconomy.TabCompleter.TabMaterial;
import goodEconomy.commands.Buy;
import goodEconomy.commands.BuySellEco;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Locales {
    private static Economy econ;
    private static Permission perms;
    private static Chat chat;
	private static GoodEconomy ins;
    
	public static void load() {
		ins = GoodEconomy.getInstance();
		
		checkEconomy();
		setupPermissions();
	    setupChat();
	    loadCommands();
	}
	
	private static void loadCommands() {
		ins.getCommand("buyselleco").setExecutor(new BuySellEco());
		ins.getCommand("buyz").setExecutor(new Buy());
		ins.getCommand("buyz").setTabCompleter(new TabMaterial());;
	}
	
	private static void checkEconomy() {
		if(!setupEconomy()) {
			ins.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(ins);
            return;
        }
	}
	private static boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp =  ins.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

	
	
    private static boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = ins.getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private static boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = ins.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    public Permission getPermissions() {
        return perms;
    }

    public Chat getChat() {
        return chat;
    }
}
