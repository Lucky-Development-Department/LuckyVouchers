package net.luckynetwork.luckyvouchers.commands.subcommands;

import net.luckynetwork.luckyvouchers.LuckyVouchers;
import net.luckynetwork.luckyvouchers.abstraction.SubCommand;
import net.luckynetwork.luckyvouchers.managers.VoucherManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends SubCommand {

    @Override
    public List<String> parseTabCompletions(LuckyVouchers plugin, CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public void perform(LuckyVouchers plugin, CommandSender sender, String[] args) {

        VoucherManager voucherManager = plugin.getVoucherManager();

        plugin.reloadConfig();
        voucherManager.clearVoucherMap();
        sender.sendMessage(this.color("&aYou have reloaded the configuration!"));

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, voucherManager::loadVoucher, 5L);


    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
