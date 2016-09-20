package co.reasondev.enchant;

import co.reasondev.enchant.enchantment.CustomType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantmentCmd implements CommandExecutor {

    private Enchants plugin;

    public EnchantmentCmd(Enchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            return msg(sender, "&cInvalid arguments! Try &e/enchantment reload|list|add");
        }
        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.loadConfig();
            plugin.getManager().reloadChances();
            return msg(sender, "&aSuccessfully reloaded Configuration file");
        }
        if (args[0].equalsIgnoreCase("list")) {
            StringBuilder sb = new StringBuilder("&eHere are the available custom Enchants: ");
            for (CustomType type : CustomType.values()) {
                boolean enabled = plugin.getConfig().getBoolean("enchants." + type.toString() + ".enabled");
                sb.append((enabled ? "&a" : "&c") + type.toString() + "&7, ");
            }
            sb.delete(sb.length() - 2, sb.length());
            return msg(sender, sb.toString());
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (!(sender instanceof Player)) {
                return msg(sender, "Error! You must be in-game to use this command!");
            }
            if (args.length != 3) {
                return msg(sender, "&cInvalid arguments! Try &e/enchantment add <enchantment> <level>");
            }
            Enchantment enchantment = Enchantment.getByName(args[1].toUpperCase());
            if (enchantment == null) {
                return msg(sender, "&cYou have specified an invalid Enchantment!");
            }
            try {
                int level = Integer.parseInt(args[2]);
                if (enchantment.getMaxLevel() < level) {
                    return msg(sender, "&cError! That Enchantment has a max level of &e" + enchantment.getMaxLevel());
                }
                if (enchantment.getStartLevel() > level) {
                    return msg(sender, "&cError! That Enchantment has a min level of &e" + enchantment.getStartLevel());
                }
                ItemStack book = ((Player) sender).getItemInHand();
                if (book == null || book.getType() != Material.ENCHANTED_BOOK) {
                    return msg(sender, "&cError! You must be holding an Enchanted Book!");
                }
                if (book.getAmount() > 1) {
                    book.setAmount(book.getAmount() - 1);
                } else {
                    ((Player) sender).setItemInHand(null);
                }
                plugin.getManager().giveBook((Player) sender, enchantment, level);
                return msg(sender, "&aYou have successfully added &e" + enchantment.getName() + " &ato this Book!");
            } catch (NumberFormatException e) {
                return msg(sender, "&cError! &e" + args[1] + " &cis not a valid number!");
            }
        }
        return true;
    }

    private boolean msg(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return true;
    }
}
