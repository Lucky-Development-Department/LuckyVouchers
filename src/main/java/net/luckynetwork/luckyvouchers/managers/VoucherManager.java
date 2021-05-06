package net.luckynetwork.luckyvouchers.managers;

import net.luckynetwork.luckyvouchers.LuckyVouchers;
import net.luckynetwork.luckyvouchers.inventory.ConfirmationInventory;
import net.luckynetwork.luckyvouchers.objects.Voucher;
import net.luckynetwork.luckyvouchers.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.management.InvalidApplicationException;
import java.util.*;
import java.util.stream.Collectors;

public class VoucherManager {

    private final Map<String, Voucher> voucherMap = new HashMap<>();
    private final NamespacedKey namespacedKey;
    private final ItemStack minionVoucherStack;

    private final LuckyVouchers plugin;
    public VoucherManager(LuckyVouchers plugin){
        this.plugin = plugin;
        namespacedKey = new NamespacedKey(plugin, "VoucherName");
        minionVoucherStack = this.getMinionStack();
    }

    public NamespacedKey getNamespacedKey(){
        return namespacedKey;
    }

    public List<String> getVoucherNameList(){
        return new ArrayList<>(this.voucherMap.keySet());
    }

    public Voucher getVoucher(String voucherName){
        return this.voucherMap.get(voucherName);
    }

    public List<Voucher> getVoucherList(){
        List<Voucher> voucherList = new ArrayList<>();
        for(String voucher : voucherMap.keySet()){
            voucherList.add(this.voucherMap.get(voucher));
        }

        return voucherList;
    }

    public void clearVoucherMap(){
        this.voucherMap.clear();
    }

    public void assignVoucher(String voucherName, Voucher voucher){
        this.voucherMap.put(voucherName, voucher);
    }

    public void loadVoucher(){

        FileConfiguration config = plugin.getConfig();
        for(String voucherName : config.getConfigurationSection("vouchers").getKeys(false)){

            Material material = Material.matchMaterial(config.getString("vouchers." + voucherName + ".item.material"));
            boolean isEnchanted = config.getBoolean("vouchers." + voucherName + ".item.enchanted");
            String name = config.getString("vouchers." + voucherName + ".item.name");
            List<String> lore = config.getStringList("vouchers." + voucherName + ".item.lore");
            List<String> flags = config.getStringList("vouchers." + voucherName + ".item.flags");

            ItemBuilder builder = new ItemBuilder(material).name(this.color(name)).lore(this.color(lore))
                    .flagsList(flags).addStringPDC(namespacedKey, voucherName);

            if(isEnchanted) builder.enchant(Enchantment.ARROW_INFINITE);

            ItemStack stack = builder.build();

            List<String> permissionCheck = config.getStringList("vouchers." + voucherName + ".item.permissionsCheck");
            String failedCheck = config.getString("vouchers." + voucherName + ".item.failedCheck");
            List<String> commands = config.getStringList("vouchers." + voucherName + ".item.commands");

            boolean isMessageEnabled = config.getBoolean("vouchers." + voucherName + ".message.enabled");
            List<String> messages = config.getStringList("vouchers." + voucherName + ".message.message");

            boolean isSoundEnabled = config.getBoolean("vouchers." + voucherName + ".sound.enabled");
            String sound = config.getString("vouchers." + voucherName + ".sound.name");

            this.assignVoucher(voucherName, new Voucher(stack, permissionCheck, failedCheck, commands,
                    isMessageEnabled, messages, isSoundEnabled, sound));

        }

    }

    public ItemStack getMinionStack(){

        ItemStack stack = new ItemStack(Material.ARMOR_STAND);

        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.setDisplayName(this.color("&e&lMinion Limit Increase &7(Right Click)"));
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "minion");
        itemMeta.setLore(this.color(Arrays.asList(" ", " &e&l[!] Perks: &fPlace More Minion", " ", "&7&o\"This perks can give you ability", "&7&oto add 1 more max minion", "&7&oto your account\"", " ", " &7&o(( &f&oRight-click &7&oto redeem ))")));
        itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        stack.setItemMeta(itemMeta);
        return stack;
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private List<String> color(List<String> strings) {
        return strings.stream().map(this::color).collect(Collectors.toList());
    }

    public ItemStack getMinionVoucherStack() { return minionVoucherStack; }

}
