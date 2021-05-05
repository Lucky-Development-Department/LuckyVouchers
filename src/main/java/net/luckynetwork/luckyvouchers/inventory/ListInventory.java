package net.luckynetwork.luckyvouchers.inventory;

import net.luckynetwork.luckyvouchers.inventory.handlers.FastInv;
import net.luckynetwork.luckyvouchers.objects.Voucher;
import org.bukkit.entity.Player;

import java.util.List;

public class ListInventory extends FastInv {

    public ListInventory(List<Voucher> voucherList) {
        super(54, "Vouchers List");

        int slot = 0;
        for(Voucher voucher : voucherList){
            this.setItem(slot, voucher.getVoucherStack(), event -> {
                Player player = (Player) event.getWhoClicked();
                player.getInventory().addItem(voucher.getVoucherStack());
            });

            slot++;
        }

    }
}
