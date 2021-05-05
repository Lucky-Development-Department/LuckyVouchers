package net.luckynetwork.luckyvouchers.inventory;

import com.google.common.primitives.Ints;
import net.luckynetwork.luckyvouchers.inventory.handlers.FastInv;
import net.luckynetwork.luckyvouchers.objects.Voucher;
import net.luckynetwork.luckyvouchers.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmationInventory extends FastInv {

    public ConfirmationInventory(Player player, Voucher voucher, ItemStack hand) {
        super(27, "Confirmation Menu");

        List<Integer> dummySlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26);

        this.setItems(Ints.toArray(dummySlots), getDummyItem());
        this.setItem(13, voucher.getVoucherStack());
        this.setItem(16, getCancelItem(), event -> player.closeInventory());

        this.setItem(10, getAcceptItem(), event -> {

            for(String command : voucher.getCommands()){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
            }

            // Send messages.
            if(voucher.isMessageEnabled()){
                for(String message : voucher.getMessages()){
                    player.sendMessage(this.color(message));
                }
            }

            // Play sound.
            if(voucher.isSoundEnabled()){
                Sound sound = Sound.valueOf(voucher.getSoundName());
                player.playSound(player.getLocation(), sound, 1f, 1f);
            }

            hand.setAmount(hand.getAmount() - 1);
            player.closeInventory();

        });


    }

    public ItemStack getDummyItem(){
        return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(this.color("&f")).build();
    }

    public ItemStack getAcceptItem(){
        return new ItemBuilder(Material.LIME_WOOL).name(this.color("&aAccept"))
                .lore(this.color("&7&o\"Accept to redeem this voucher.\"")).build();
    }

    public ItemStack getCancelItem(){
        return new ItemBuilder(Material.RED_WOOL).name(this.color("&cCancel"))
                .lore(this.color("&7&o\"Cancel to redeem this voucher.\"")).build();
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private List<String> color(List<String> strings) {
        return strings.stream().map(this::color).collect(Collectors.toList());
    }

}
