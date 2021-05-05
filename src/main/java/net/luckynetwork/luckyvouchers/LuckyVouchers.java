package net.luckynetwork.luckyvouchers;

import net.luckynetwork.luckyvouchers.commands.VoucherCommand;
import net.luckynetwork.luckyvouchers.inventory.handlers.FastInvManager;
import net.luckynetwork.luckyvouchers.listeners.VoucherInteract;
import net.luckynetwork.luckyvouchers.managers.VoucherManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckyVouchers extends JavaPlugin {

    private VoucherManager voucherManager;

    @Override
    public void onEnable(){

        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        voucherManager = new VoucherManager(this);

        registerCommandsAndListeners();

        voucherManager.loadVoucher();
        FastInvManager.register(this);

    }

    public void registerCommandsAndListeners(){

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new VoucherInteract(this), this);

        this.getCommand("vouchers").setExecutor(new VoucherCommand(this));

    }

    public VoucherManager getVoucherManager() { return voucherManager; }

}
