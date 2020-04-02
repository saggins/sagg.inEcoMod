package goodEconomy;

import org.bukkit.plugin.java.JavaPlugin;

public final class GoodEconomy extends JavaPlugin{

    private static GoodEconomy INSTANCE;
    
	@Override
	public void onEnable() {
        INSTANCE = this;
		log("Enabling Sagg.in Ecnonmy Mod");
		Startup.load();
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

}
