package co.reasondev.enchant.enchantment;

import co.reasondev.enchant.Enchants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.lang.reflect.Field;
import java.util.*;

import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;

public class EnchantManager {

    private Enchants plugin;
    private Map<String, Integer> chances = new HashMap<>();

    public EnchantManager(Enchants plugin) {
        this.plugin = plugin;
    }

    private void setAllowNew() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            plugin.getLogger().severe("Error! Could not register new Enchantments!");
            e.printStackTrace();
        }
    }

    public void loadEnchants() {
        setAllowNew();
        for (CustomType type : CustomType.values()) {
            if (plugin.getConfig().getBoolean("enchants." + type.toString() + ".enabled")) {
                Enchantment.registerEnchantment(type.getEnchantment());
                chances.put(type.toString(), plugin.getConfig().getInt("enchants." + type.toString() + ".chance"));
                plugin.getLogger().info(type.toString() + " ENABLED!");
            }
        }
    }

    public void reloadChances() {
        chances = new HashMap<>();
        for (CustomType type : CustomType.values()) {
            if (plugin.getConfig().getBoolean("enchants." + type.toString() + ".enabled")) {
                chances.put(type.toString(), plugin.getConfig().getInt("enchants." + type.toString() + ".chance"));
            }
        }
    }

    private ChatColor getColor(int level) {
        return ChatColor.valueOf(plugin.getConfig().getString("level_color." + level, "WHITE"));
    }

    private String getTargetString(EnchantmentTarget target) {
        switch (target) {
            case ARMOR_HEAD:
                return "Helmet";
            case ARMOR_TORSO:
                return "Chestplate";
            case ARMOR_LEGS:
                return "Leggings";
            case ARMOR_FEET:
                return "Boots";
            default:
                return UPPER_UNDERSCORE.to(UPPER_CAMEL, target.name());
        }
    }

    public void giveBook(Player player, Enchantment enchantment, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        meta.addStoredEnchant(enchantment, level > enchantment.getMaxLevel() ? enchantment.getMaxLevel() : level, false);
        meta.setLore(Arrays.asList(
                ChatColor.BLUE + getTargetString(enchantment.getItemTarget()),
                getLore(enchantment, level)));
        book.setItemMeta(meta);
        player.getInventory().addItem(book);
    }

    private String convertToRoman(int mInt) {
        String[] rnChars = {"M", "CM", "D", "C", "XC", "L", "X", "IX", "V", "I"};
        int[] rnVals = {1000, 900, 500, 100, 90, 50, 10, 9, 5, 1};
        String retVal = "";

        for (int i = 0; i < rnVals.length; i++) {
            int numberInPlace = mInt / rnVals[i];
            if (numberInPlace == 0) continue;
            retVal += numberInPlace == 4 && i > 0 ? rnChars[i] + rnChars[i - 1] :
                    new String(new char[numberInPlace]).replace("\0", rnChars[i]);
            mInt = mInt % rnVals[i];
        }
        return retVal;
    }

    public String getLore(Enchantment enchantment, int level) {
        return getColor(level) + UPPER_UNDERSCORE.to(UPPER_CAMEL, enchantment.getName()) + " " + convertToRoman(level);
    }

    public List<String> removeLore(Enchantment enchantment, List<String> lore) {
        if (lore == null || lore.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> output = new ArrayList<>(lore);
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).contains(UPPER_UNDERSCORE.to(UPPER_CAMEL, enchantment.getName()))) {
                output.remove(i);
            }
        }
        return output;
    }

    public int getCustomEnchants(ItemStack i) {
        int counter = 0;
        for (Enchantment enchantment : i.getEnchantments().keySet()) {
            if (enchantment instanceof CustomEnchant) {
                counter++;
            }
        }
        return counter;
    }

    public Enchantment getRandomEnchant(int level) {
        int totalWeight = 0;
        for (String enchant : chances.keySet()) {
            totalWeight += chances.get(enchant);
        }
        int r = (int) (Math.random() * totalWeight);
        int countWeight = 0;
        for (String enchant : chances.keySet()) {
            countWeight += chances.get(enchant);
            Enchantment e = Enchantment.getByName(enchant);
            if (countWeight >= r && e.getStartLevel() <= level && e.getMaxLevel() >= level) {
                return CustomEnchant.getByName(enchant);
            }
        }
        throw new RuntimeException("Failed to select Random Enchantment!");
    }
}
