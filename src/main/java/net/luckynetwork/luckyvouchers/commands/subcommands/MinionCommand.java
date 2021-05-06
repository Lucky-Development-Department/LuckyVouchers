package net.luckynetwork.luckyvouchers.commands.subcommands;

import net.luckynetwork.luckyvouchers.LuckyVouchers;
import net.luckynetwork.luckyvouchers.abstraction.SubCommand;
import net.luckynetwork.luckyvouchers.managers.VoucherManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MinionCommand extends SubCommand {

    @Override
    public List<String> parseTabCompletions(LuckyVouchers plugin, CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public void perform(LuckyVouchers plugin, CommandSender sender, String[] args) {

        VoucherManager voucherManager = plugin.getVoucherManager();

        if(args.length < 3){
            sender.sendMessage(this.color("&cUsage: /vouchers giveminion <player> <amount>"));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if(player == null){
            sender.sendMessage(this.color("&cThat player is not online!"));
            return;
        }

        if(!this.isInteger(args[2])){
            sender.sendMessage(this.color("&cAmount must be a number!"));
            return;
        }

        int amount = Integer.parseInt(args[2]);

        ItemStack stack = voucherManager.getMinionVoucherStack().clone();
        stack.setAmount(amount);

        player.getInventory().addItem(stack);

        player.sendMessage(this.color("&aYou have received a voucher!"));
        sender.sendMessage(this.color("&aYou have send &e" + player.getName() + " &aa voucher!"));

    }

    private boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
