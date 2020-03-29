package goodEconomy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import goodEconomy.TabCompleter.TabMaterial;
import goodEconomy.commands.Buy;
import goodEconomy.commands.BuySellEco;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public final class GoodEconomy extends JavaPlugin{
    private Economy econ;
    private Permission perms;
    private Chat chat;
    private static GoodEconomy INSTANCE;
    
	@Override
	public void onEnable() {
        INSTANCE = this;
		log("Enabling Sagg.in Ecnonmy Mod");

		
		Locales.load();
		
		
//		this.getCommand("buyselleco").setExecutor(new BuySellEco());
//		this.getCommand("buyz").setExecutor(new Buy());
//		this.getCommand("buyz").setTabCompleter(new TabMaterial());;

		log("Version " + getDescription().getVersion() + "by " + getDescription().getAuthors() + "is enabled!");
	}
	
	@Override
	public void onDisable() {
		log("Sagg.in Buy/Sell mod, CYA");
	}
	
	public void log(String msg) {
		this.getLogger().info(msg);
	}
	
    public static GoodEconomy getInstance() {
        return INSTANCE;
    }

	
	
	
	
	
	
	
	
//	private boolean setupEconomy() {
//        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
//            return false;
//        }
//
//        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
//        if (rsp == null) {
//            return false;
//        }
//        econ = rsp.getProvider();
//        return econ != null;
//    }
//
//	
//	
//    private boolean setupChat() {
//        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
//        chat = rsp.getProvider();
//        return chat != null;
//    }
//
//    private boolean setupPermissions() {
//        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
//        perms = rsp.getProvider();
//        return perms != null;
//    }
//
//    public Economy getEconomy() {
//        return econ;
//    }
//
//    public Permission getPermissions() {
//        return perms;
//    }
//
//    public Chat getChat() {
//        return chat;
//    }
}
