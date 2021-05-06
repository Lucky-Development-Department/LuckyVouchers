package net.luckynetwork.luckyvouchers;

import net.luckperms.api.LuckPerms;
import net.luckynetwork.luckyvouchers.commands.VoucherCommand;
import net.luckynetwork.luckyvouchers.inventory.handlers.FastInvManager;
import net.luckynetwork.luckyvouchers.listeners.MinionVoucher;
import net.luckynetwork.luckyvouchers.listeners.VoucherInteract;
import net.luckynetwork.luckyvouchers.managers.VoucherManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckyVouchers extends JavaPlugin {

    private VoucherManager voucherManager;
    private LuckPerms luckPermsAPI;

    @Override
    public void onEnable(){

        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        voucherManager = new VoucherManager(this);

        registerCommandsAndListeners();
        registerLuckPerms();

        voucherManager.loadVoucher();
        FastInvManager.register(this);

    }

    private void registerCommandsAndListeners(){

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new VoucherInteract(this), this);
        pm.registerEvents(new MinionVoucher(this), this);

        VoucherCommand voucherCommand = new VoucherCommand(this);
        this.getCommand("vouchers").setExecutor(voucherCommand);
        this.getCommand("vouchers").setTabCompleter(voucherCommand);

    }

    private void registerLuckPerms(){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsAPI = provider.getProvider();
        }
    }

    public VoucherManager getVoucherManager() { return voucherManager; }
    public LuckPerms getLuckPermsAPI() { return luckPermsAPI; }

}
