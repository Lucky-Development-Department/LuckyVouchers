package net.luckynetwork.luckyvouchers.commands.subcommands;

import net.luckynetwork.luckyvouchers.LuckyVouchers;
import net.luckynetwork.luckyvouchers.abstraction.SubCommand;
import net.luckynetwork.luckyvouchers.inventory.ListInventory;
import net.luckynetwork.luckyvouchers.managers.VoucherManager;
import net.luckynetwork.luckyvouchers.objects.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GiveCommand extends SubCommand {
    // /vouchers give <player> <voucher> [amount]

    @Override
    public List<String> parseTabCompletions(LuckyVouchers plugin, CommandSender sender, String[] args) {

        VoucherManager voucherManager = plugin.getVoucherManager();

        if(args.length == 2){
            List<String> suggestion = new ArrayList<>();
            for(Player online : Bukkit.getOnlinePlayers()){
                suggestion.add(online.getName());
            }
            return suggestion;
        }

        if(args.length == 3){
            return voucherManager.getVoucherNameList();
        }

        if(args.length == 4){
            return Collections.singletonList("[<amount>]");
        }

        return null;
    }

    @Override
    public void perform(LuckyVouchers plugin, CommandSender sender, String[] args) {

        VoucherManager voucherManager = plugin.getVoucherManager();
        if(args.length == 2){
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null){
                sender.sendMessage(this.color("&cThat player is not online!"));
                return;
            }

            ListInventory listInventory = new ListInventory(voucherManager.getVoucherList(), player);
            listInventory.open(player);

            return;
        }

        if(args.length < 4){
            sender.sendMessage(this.color("&cUsage: /vouchers give <player> <voucher> [<amount>]"));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if(player == null){
            sender.sendMessage(this.color("&cThat player is not online!"));
            return;
        }

        Voucher voucher = voucherManager.getVoucher(args[2]);
        if(voucher == null){
            sender.sendMessage(this.color("&cThere is no voucher with that name!"));
            return;
        }

        if(!this.isInteger(args[3])){
            sender.sendMessage(this.color("&cAmount must be a number!"));
            return;
        }

        int amount = Integer.parseInt(args[3]);

        ItemStack stack = voucher.getVoucherStack().clone();
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
