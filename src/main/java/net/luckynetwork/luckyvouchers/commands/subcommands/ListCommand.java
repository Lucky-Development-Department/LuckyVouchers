package net.luckynetwork.luckyvouchers.commands.subcommands;

import net.luckynetwork.luckyvouchers.LuckyVouchers;
import net.luckynetwork.luckyvouchers.abstraction.SubCommand;
import net.luckynetwork.luckyvouchers.inventory.ListInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ListCommand extends SubCommand {

    @Override
    public List<String> parseTabCompletions(LuckyVouchers plugin, CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public void perform(LuckyVouchers plugin, CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        ListInventory listInventory = new ListInventory(plugin.getVoucherManager().getVoucherList());
        listInventory.open(player);

    }

}
