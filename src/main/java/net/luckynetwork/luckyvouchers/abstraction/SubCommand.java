package net.luckynetwork.luckyvouchers.abstraction;

import net.luckynetwork.luckyvouchers.LuckyVouchers;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    public abstract List<String> parseTabCompletions(LuckyVouchers plugin, CommandSender sender, String[] args);

    public abstract void perform(LuckyVouchers plugin, CommandSender sender, String[] args);

}
