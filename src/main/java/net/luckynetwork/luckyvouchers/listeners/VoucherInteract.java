package net.luckynetwork.luckyvouchers.listeners;

import net.luckynetwork.luckyvouchers.LuckyVouchers;
import net.luckynetwork.luckyvouchers.inventory.ConfirmationInventory;
import net.luckynetwork.luckyvouchers.managers.VoucherManager;
import net.luckynetwork.luckyvouchers.objects.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class VoucherInteract implements Listener {

    private final LuckyVouchers plugin;
    public VoucherInteract(LuckyVouchers plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getHand() != EquipmentSlot.HAND) return;
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){

            Player player = event.getPlayer();
            VoucherManager voucherManager = plugin.getVoucherManager();

            ItemStack stack = player.getInventory().getItemInMainHand();
            if(stack.getType() == Material.AIR || stack.getItemMeta() == null) return;
            ItemMeta im = stack.getItemMeta();

            String voucherName = im.getPersistentDataContainer().get(voucherManager.getNamespacedKey(), PersistentDataType.STRING);
            if(voucherName == null) return;

            event.setCancelled(true);

            Voucher voucher = voucherManager.getVoucher(voucherName);
            if(voucher == null) return;

            for(String permission : voucher.getPermissionCheck()){
                if(player.hasPermission(permission)){
                    player.sendMessage(this.color(voucher.getFailedCheck()));
                    return;
                }
            }

            ConfirmationInventory inventory = new ConfirmationInventory(player, voucher, player.getInventory().getItemInMainHand());
            inventory.open(player);

        }

    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
