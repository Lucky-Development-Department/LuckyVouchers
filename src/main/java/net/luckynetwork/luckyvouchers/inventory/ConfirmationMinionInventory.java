package net.luckynetwork.luckyvouchers.inventory;

import com.google.common.primitives.Ints;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckynetwork.luckyvouchers.LuckyVouchers;
import net.luckynetwork.luckyvouchers.inventory.handlers.FastInv;
import net.luckynetwork.luckyvouchers.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConfirmationMinionInventory extends FastInv {

    public ConfirmationMinionInventory(LuckyVouchers plugin, Player player, ItemStack hand, User user, String removedNode, String addNode) {
        super(27);

        List<Integer> dummySlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26);

        this.setItems(Ints.toArray(dummySlots), getDummyItem());
        this.setItem(13, hand);
        this.setItem(16, getCancelItem(), event -> player.closeInventory());

        this.setItem(10, getAcceptItem(), event -> {

            handlePermissionUpgrade(plugin, player, user, removedNode, addNode);

            hand.setAmount(hand.getAmount() - 1);
            player.closeInventory();

        });


    }

    private void handlePermissionUpgrade(LuckyVouchers plugin, Player player, User user, String removed, String added){
        LuckPerms api = plugin.getLuckPermsAPI();

        user.data().remove(Node.builder(removed).build());
        user.data().add(Node.builder(added).build());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            api.getUserManager().saveUser(user);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            player.sendTitle(this.color("&a&lCompleted!"), this.color("&7We have successfully modified your minion data!"), 0, 40, 0);

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

}
