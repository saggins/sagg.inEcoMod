package goodEconomy.commands;

import java.util.Optional;

import org.bukkit.command.CommandSender;

import com.cryptomorin.xseries.XMaterial;

public interface ITransaction {
	public boolean checkPerms(CommandSender sender);
	public int getAmmount(String[] args, CommandSender sender);
	public String getMaterial( CommandSender sender);
}
