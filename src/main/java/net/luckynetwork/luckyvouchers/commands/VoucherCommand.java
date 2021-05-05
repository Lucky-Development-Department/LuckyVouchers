package net.luckynetwork.luckyvouchers.commands;

import net.luckynetwork.luckyvouchers.LuckyVouchers;
import net.luckynetwork.luckyvouchers.abstraction.SubCommand;
import net.luckynetwork.luckyvouchers.commands.subcommands.ListCommand;
import net.luckynetwork.luckyvouchers.commands.subcommands.ReloadCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.stream.Collectors;


public class VoucherCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommandMap = new HashMap<>();
    private final LuckyVouchers plugin;

    public VoucherCommand(LuckyVouchers plugin){
        this.plugin = plugin;

        subCommandMap.put("list", new ListCommand());
        subCommandMap.put("reload", new ReloadCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender.hasPermission("luckyvouchers.admin"))){
            sender.sendMessage(this.color("&cYou don't have enough permission!"));
            return true;
        }

        if(args.length == 0){
            // Send help messages.
            return true;
        }

        SubCommand subCommand = this.subCommandMap.get(args[0].toLowerCase());
        if(subCommand == null) return true;

        subCommand.perform(plugin, sender, args);

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender.hasPermission("luckyvouchers.admin"))) return null;

        if(args.length == 1){
            return Arrays.asList("list", "reload");
        }

        return null;
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
