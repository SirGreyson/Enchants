package co.reasondev.enchant;

import co.reasondev.enchant.enchantment.EnchantManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Enchants extends JavaPlugin {

    public String SIGN_HEADER;
    public String SIGN_EXP;
    public String SIGN_TIER;
    private EnchantManager manager;

    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        getManager().loadEnchants();
        getCommand("enchantment").setExecutor(new EnchantmentCmd(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getLogger().info("has been enabled");
    }

    public void loadConfig() {
        SIGN_HEADER = ChatColor.translateAlternateColorCodes('&', getConfig().getString("SIGN_HEADER"));
        SIGN_EXP = ChatColor.translateAlternateColorCodes('&', getConfig().getString("SIGN_EXP"));
        SIGN_TIER = ChatColor.translateAlternateColorCodes('&', getConfig().getString("SIGN_TIER"));
    }

    public void onDisable() {
        getLogger().info("has been enabled");
    }

    public EnchantManager getManager() {
        if (manager == null) {
            manager = new EnchantManager(this);
        }
        return manager;
    }
}
