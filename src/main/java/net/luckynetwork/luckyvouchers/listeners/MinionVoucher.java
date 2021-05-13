package net.luckynetwork.luckyvouchers.listeners;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckynetwork.luckyvouchers.LuckyVouchers;
import net.luckynetwork.luckyvouchers.inventory.ConfirmationInventory;
import net.luckynetwork.luckyvouchers.inventory.ConfirmationMinionInventory;
import net.luckynetwork.luckyvouchers.managers.VoucherManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MinionVoucher implements Listener {

    private final LuckyVouchers plugin;
    public MinionVoucher(LuckyVouchers plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getHand() != EquipmentSlot.HAND) return;
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){

            VoucherManager voucherManager = plugin.getVoucherManager();
            Player player = event.getPlayer();

            if(player.isOp()) return;

            ItemStack stack = player.getInventory().getItemInMainHand();

            if(stack.getType() == Material.AIR || stack.getItemMeta() == null) return;
            ItemMeta im = stack.getItemMeta();

            String voucherName = im.getPersistentDataContainer().get(voucherManager.getNamespacedKey(), PersistentDataType.STRING);
            if(voucherName == null) return;

            event.setCancelled(true);

            if(voucherName.equalsIgnoreCase("minion")){

                int maxMinion = this.getMaxMinions(player);
                int finalMaxMinion = maxMinion + 1;

                String removedNode = "minions.place." + maxMinion;
                String addedNode = "minions.place." + finalMaxMinion;

                User user = this.getUser(player.getUniqueId());

                if(user == null){
                    System.out.println("[LuckyVouchers] DEBUG INTERACT EVENT");
                    System.out.println("[LuckyVouchers] There is an error checking permission for " + player.getName());
                    System.out.println("[LuckyVouchers] With UUID: " + player.getUniqueId().toString());
                    return;
                }

                if(maxMinion == 0){
                    player.sendMessage(this.color("&cThere is something wrong! Please contact the developer."));
                    return;
                }

                if(maxMinion >= 20){
                    player.sendMessage(this.color("&cYou are already on the max minion limits!"));
                    return;
                }

                ConfirmationMinionInventory minionInventory = new ConfirmationMinionInventory(plugin, player, stack, user, removedNode, addedNode);
                minionInventory.open(player);

            }

        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(player.isOp()) return;
        User user = this.getUser(player.getUniqueId());

        if(user == null){
            System.out.println("[LuckyVouchers] DEBUG JOIN EVENT");
            System.out.println("[LuckyVouchers] There is an error checking permission for " + player.getName());
            System.out.println("[LuckyVouchers] With UUID: " + player.getUniqueId().toString());
            return;
        }

        if(this.getMaxMinions(player) == 0){
            this.addDefaultPermission(user);
        }

    }

    private void addDefaultPermission(User user){
        user.data().add(Node.builder("minions.place.1").build());
        this.saveUser(user);
    }

    private User getUser(UUID uniqueId) {
        UserManager userManager = plugin.getLuckPermsAPI().getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uniqueId);

        return userFuture.join();
    }

    private void saveUser(User user){
        UserManager userManager = plugin.getLuckPermsAPI().getUserManager();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> userManager.saveUser(user));
    }

    private int getMaxMinions(Player player){
        for(PermissionAttachmentInfo perm : player.getEffectivePermissions()){
            if(perm.getPermission().startsWith("minions.place.")){
                String permission = perm.getPermission().replace(".", ";");
                String[] split = permission.split(";");
                return Integer.parseInt(split[2]);
            }
        }
        return 0;
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
