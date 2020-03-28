package goodEconomy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public final class GoodEconomy extends JavaPlugin{
    private Economy econ;
    private Permission perms;
    private Chat chat;
    
	@Override
	public void onEnable() {
		log("Enabling Sagg.in Ecnonmy Mod");

		if(!setupEconomy()) {
			this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
		  this.setupPermissions();
	    this.setupChat();
		
		
		this.getCommand("buyselleco").setExecutor(new BuySellEco());
		log("Version " + getDescription().getVersion() + "by " + getDescription().getAuthors() + "is enabled!");
	}
	
	@Override
	public void onDisable() {
		log("Sagg.in Buy/Sell mod, CYA");
	}
	
	public void log(String msg) {
		this.getLogger().info(msg);
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
	
	
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
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
