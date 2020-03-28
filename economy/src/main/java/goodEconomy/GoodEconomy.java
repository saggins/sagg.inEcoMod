package goodEconomy;

import org.bukkit.plugin.java.JavaPlugin;

public final class GoodEconomy extends JavaPlugin{
	
	@Override
	public void onEnable() {
		log("Enabling Sagg.in Ecnonmy Mod");
		
		log("Version " + getDescription().getVersion() + "by " + getDescription().getAuthors() + "is enabled!");
	}
	
	@Override
	public void onDisable() {
		log("Sagg.in Buy/Sell mod, CYA");
	}
	
	public void log(String msg) {
		this.getLogger().info(msg);
	}
	
}
