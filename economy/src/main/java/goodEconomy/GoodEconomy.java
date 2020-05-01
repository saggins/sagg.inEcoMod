package goodEconomy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import goodEconomy.commands.Buy;
import goodEconomy.commands.BuySellEco;
import goodEconomy.commands.Sell;
import goodEconomy.commands.Table;
import goodEconomy.commands.TestBuy;
import goodEconomy.commands.TabCompleters.AmountSold;
import goodEconomy.commands.TabCompleters.TabBuy;
import goodEconomy.commands.TabCompleters.TabMaterial;
import goodEconomy.database.Database;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;


public final class GoodEconomy extends JavaPlugin{

    private static GoodEconomy INSTANCE;
    private static Economy econ;
    private static Permission perms;
    private static Chat chat;
	@Override
	public void onEnable() {
		INSTANCE = this;
		log("Enabling Sagg.in Ecnonmy Mod");
		getInstance().saveDefaultConfig();
		load();
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

	public  void load() { 
		loadConfigs();
		setupPermissions();
		//setupChat();
	    Database things = new Database();
	    things.init();
	    loadCommands();
	    setupEconomy();
	    
	}

	public void loadConfigs() {
		String[] params = {"DB-NAME", "DB-HOST", "DB-USER", "DB-PASS", "DB-PORT"};
		
		for (int i = 0; i < params.length; i++) {
			 String check = this.getConfig().getString(params[i]);
			 if (check == "") {
				 this.getLogger().severe("ERROR sagg.in plugin: "+ check +" is empty");
		         Bukkit.getPluginManager().disablePlugin(this);
			 }
		}
	
	}
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public void loadCommands() {
		this.getCommand("SaggEco").setExecutor(new BuySellEco());
		
		this.getCommand("table").setExecutor(new Table() );
		this.getCommand("table").setTabCompleter(new TabMaterial());
		
		this.getCommand("sell").setExecutor(new Sell() );
		this.getCommand("sell").setTabCompleter(new AmountSold());
		
		this.getCommand("buy").setExecutor(new Buy() );
		this.getCommand("buy").setTabCompleter(new TabBuy());

	}
	

	public  void setupChat() {
        RegisteredServiceProvider<Chat> rsp = this.getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }

    public  boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = this.getServer().getServicesManager().getRegistration(Permission.class);
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
