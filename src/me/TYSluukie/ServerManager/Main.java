package me.TYSluukie.ServerManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getConfig().addDefault("enabled", "true");
		getConfig().addDefault("commandspy", "true");
		getConfig().addDefault("lock", "false");
		getConfig().addDefault("lockbroadcast", "true");
		getConfig().addDefault("lockreason", "De server is momenteel in lockdown!"
				+ "Probeer het later nog eens!");
		getConfig().addDefault("commands",
				"- help"
				+ "- spawn");
		getConfig().options().copyDefaults(true);
		saveConfig();
		if((getConfig().getString("enabled")).equals(true)) {
			Bukkit.getServer().getPluginManager().registerEvents(this, this);
			this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "ServerManager" + ChatColor.GRAY + "]" + ChatColor.BLUE + " ServerManager by TYSluukie is enabled" + ChatColor.RED + "!");
		} else {
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("lock")) {
			if(sender.hasPermission("ServerManager.Lockdown")) {
				if(args.length == 0) {
					getConfig().set("lockreason", "De server is momenteel in lockdown!"
				+ "Probeer het later nog eens!");
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + sender.getName() + ChatColor.GOLD + " heeft de lockdown aangezet met rede: " + ChatColor.GREEN + getConfig().getString("lockreason"));
				}
				if(args.length >= 1) {
					getConfig().set("lockreason", "De server is momenteel in lockdown!"
							+ "Probeer het later nog eens!");
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN + sender.getName() + ChatColor.GOLD + " heeft de lockdown aangezet met rede: " + ChatColor.GREEN + args);
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Je mag dit commando niet gebruiken!");
			}
		}
		return true;
	}
	public void onDisable() {
		if((getConfig().getString("enabled")).equals(true)) {
			Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "ServerManager" + ChatColor.GRAY + "]" + ChatColor.BLUE + " ServerManager by TYSluukie is disabled" + ChatColor.RED + "!");
		} else {
			Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "ServerManager" + ChatColor.GRAY + "]" + ChatColor.BLUE + " ServerManager by TYSluukie is disabled" + ChatColor.BOLD + "" + ChatColor.DARK_RED + " because enabled is set to false!");
		}
	}
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if(getConfig().getStringList("commands").contains(e.getMessage())) {
			e.setCancelled(false);
			if(getConfig().getString("commandspy").equals("true")) {
				
			Player all = (Player) Bukkit.getServer().getOnlinePlayers();
			if(all.hasPermission("ServerManager.spy")) {
				all.sendMessage(ChatColor.GREEN + e.getPlayer().getName() + ChatColor.GRAY + " -> " + ChatColor.BLUE + e.getMessage());
			}
			} else {
				return;
			}
		} else {
			e.getPlayer().sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "ServerManager" + ChatColor.GRAY + "]" + ChatColor.RED + " You are not allowed to use this command!");
		}
	}
		@EventHandler
		public void onJoin(PlayerJoinEvent e) {
			if (getConfig().getString("lock").equals("true")) {
				e.getPlayer().kickPlayer(ChatColor.GOLD + getConfig().getString("lockreason"));
				if(getConfig().getString("lockbroadcast").equals("true")) {
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN + e.getPlayer().getName() + ChatColor.GOLD + "probeerde te joinen!");
				}
			}
			if (getConfig().getString("lock").equals("false")) {
				return;
			}
	}
}
