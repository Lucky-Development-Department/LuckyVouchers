package net.luckynetwork.luckyvouchers.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Voucher {

    private final ItemStack voucherStack;

    private final List<String> permissionCheck;
    private final String failedCheck;

    private final List<String> commands;

    private final boolean messageEnabled;
    private final List<String> messages;

    private final boolean soundEnabled;
    private final String soundName;

    public Voucher(ItemStack voucherStack, List<String> permissionCheck, String failedCheck, List<String> commands,
                   boolean messageEnabled, List<String> messages, boolean soundEnabled, String soundName) {
        this.voucherStack = voucherStack;
        this.permissionCheck = permissionCheck;
        this.failedCheck = failedCheck;
        this.commands = commands;
        this.messageEnabled = messageEnabled;
        this.messages = messages;
        this.soundEnabled = soundEnabled;
        this.soundName = soundName;
    }

    public ItemStack getVoucherStack(){
        return voucherStack;
    }

    public List<String> getPermissionCheck() {
        return permissionCheck;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean isMessageEnabled() {
        return messageEnabled;
    }

    public List<String> getMessages() {
        return messages;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public String getSoundName() {
        return soundName;
    }

    public String getFailedCheck() {
        return failedCheck;
    }
}
