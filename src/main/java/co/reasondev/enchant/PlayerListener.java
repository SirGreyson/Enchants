package co.reasondev.enchant;

import co.reasondev.enchant.enchantment.CustomEnchant;
import co.reasondev.enchant.enchantment.custom.Adrenaline;
import co.reasondev.enchant.enchantment.custom.Brute;
import co.reasondev.enchant.enchantment.custom.Jelly;
import co.reasondev.enchant.enchantment.custom.ObsidianBreaker;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import com.codingforcookies.armorequip.ArmorListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    private Enchants plugin;

    public PlayerListener(Enchants plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new ArmorListener(new ArrayList<String>()), plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSignPlace(SignChangeEvent e) {
        if (!e.getLine(0).equalsIgnoreCase("[Enchant]")) {
            return;
        }
        if (!e.getPlayer().isOp() && !e.getPlayer().hasPermission("enchants.admin")) {
            e.getBlock().breakNaturally();
            e.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to create this Sign!");
            return;
        }
        try {
            int cost = Integer.parseInt(e.getLine(1));
            int tier = Integer.parseInt(e.getLine(2));
            e.setLine(0, plugin.SIGN_HEADER);
            e.setLine(1, String.format(plugin.SIGN_EXP, cost));
            e.setLine(2, String.format(plugin.SIGN_TIER, tier));
        } catch (NumberFormatException exc) {
            e.getBlock().breakNaturally();
            e.getPlayer().sendMessage(ChatColor.RED + "You entered an invalid cost or tier!");
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onSignClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!(e.getClickedBlock().getState() instanceof Sign)) {
            return;
        }
        Sign sign = (Sign) e.getClickedBlock().getState();
        if (!sign.getLine(0).equals(plugin.SIGN_HEADER)) {
            return;
        }
        e.setCancelled(true);
        int cost = Integer.parseInt(ChatColor.stripColor(sign.getLine(1)).split(" ")[0]);
        if (e.getPlayer().getLevel() < cost) {
            e.getPlayer().sendMessage(ChatColor.RED + "You do not have enough EXP to buy an Enchanting Book!");
            return;
        }
        int tier = Integer.parseInt(ChatColor.stripColor(sign.getLine(2).split(" ")[1]));
        Enchantment enchantment = plugin.getManager().getRandomEnchant(tier);
        e.getPlayer().setLevel(e.getPlayer().getLevel() - cost);
        plugin.getManager().giveBook(e.getPlayer(), enchantment, tier);
        e.getPlayer().sendMessage(ChatColor.GREEN + "You have received an Enchanting Book with " + plugin.getManager().getLore(enchantment, tier));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        // Attacked Player is wearing Custom Enchanted Armor
        for (ItemStack armor : ((Player) e.getEntity()).getInventory().getArmorContents()) {
            if (armor != null) {
                for (Enchantment enchantment : armor.getEnchantments().keySet()) {
                    if (enchantment instanceof CustomEnchant) {
                        ((CustomEnchant) enchantment).handleEntityDamageByEntity(e);
                        if (enchantment instanceof Jelly || enchantment instanceof Adrenaline) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        // Attacking Player is wearing Custom Enchanted Armor
        for (ItemStack armor : ((Player) e.getDamager()).getInventory().getArmorContents()) {
            if (armor != null) {
                for (Enchantment enchantment : armor.getEnchantments().keySet()) {
                    if (enchantment instanceof CustomEnchant) {
                        ((CustomEnchant) enchantment).handleEntityDamageByEntity(e);
                        if (enchantment instanceof Brute) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByBow(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (!(e.getDamager() instanceof Arrow)) {
            return;
        }
        if (!(((Arrow) e.getDamager()).getShooter() instanceof Player)) {
            return;
        }
        Player shooter = (Player) ((Arrow) e.getDamager()).getShooter();
        if (shooter.getItemInHand() != null && shooter.getItemInHand().getType() == Material.BOW) {
            for (Enchantment enchantment : shooter.getItemInHand().getEnchantments().keySet()) {
                if (enchantment instanceof CustomEnchant) {
                    ((CustomEnchant) enchantment).handleEntityDamageByEntity(e);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageBySword(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (((Player) e.getDamager()).getItemInHand() == null ||
                !((Player) e.getDamager()).getItemInHand().getType().toString().contains("_SWORD")) {
            return;
        }
        ItemStack sword = ((Player) e.getDamager()).getItemInHand();
        for (Enchantment enchantment : sword.getEnchantments().keySet()) {
            if (enchantment instanceof CustomEnchant) {
                ((CustomEnchant) enchantment).handleEntityDamageByEntity(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onArmorEquip(ArmorEquipEvent e) {
        if (e.getOldArmorPiece() == null && e.getNewArmorPiece() == null) {
            return;
        }
        if (e.getOldArmorPiece() != null) {
            for (Enchantment enchantment : e.getOldArmorPiece().getEnchantments().keySet()) {
                if (enchantment instanceof CustomEnchant) {
                    ((CustomEnchant) enchantment).handleItemEquip(e);
                }
            }
        }
        if (e.getNewArmorPiece() != null) {
            for (Enchantment enchantment : e.getNewArmorPiece().getEnchantments().keySet()) {
                if (enchantment instanceof CustomEnchant) {
                    ((CustomEnchant) enchantment).handleItemEquip(e);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) {
            return;
        }
        Player killer = e.getEntity().getKiller();
        if (killer.getItemInHand() == null) {
            return;
        }
        for (Enchantment enchantment : killer.getItemInHand().getEnchantments().keySet()) {
            if (enchantment instanceof CustomEnchant) {
                ((CustomEnchant) enchantment).handleEntityDeath(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getItemInHand() == null) {
            return;
        }
        if (e.getPlayer().getItemInHand() == null) {
            return;
        }
        for (Enchantment enchantment : e.getPlayer().getItemInHand().getEnchantments().keySet()) {
            if (enchantment instanceof CustomEnchant) {
                ((CustomEnchant) enchantment).handleBlockBreak(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (e.getItem() == null || !e.getItem().getType().toString().contains("_PICKAXE")) {
            return;
        }
        for (Enchantment enchantment : e.getItem().getEnchantments().keySet()) {
            if (enchantment instanceof CustomEnchant && enchantment instanceof ObsidianBreaker) {
                ((ObsidianBreaker) enchantment).handleBlockHit(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnchantItem(final InventoryClickEvent e) {
        if (!(e.getInventory() instanceof AnvilInventory)) {
            return;
        }
        final AnvilInventory inv = (AnvilInventory) e.getInventory();
        if (inv.getItem(2) != null) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                ItemStack item = inv.getItem(0);
                ItemStack book = inv.getItem(1);
                if (item == null || book == null || book.getType() != Material.ENCHANTED_BOOK) {
                    return;
                }
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
                ItemStack result = item.clone();
                List<String> lore = result.getItemMeta().hasLore() ? result.getItemMeta().getLore() : new ArrayList<String>();
                for (Enchantment enchantment : meta.getStoredEnchants().keySet()) {
                    if (!enchantment.canEnchantItem(result)) {
                        return;
                    }
                    if (!(enchantment instanceof CustomEnchant)) {
                        return;
                    }
                    lore = plugin.getManager().removeLore(enchantment, lore);
                    result.addUnsafeEnchantment(enchantment, meta.getStoredEnchantLevel(enchantment));
                    lore.add(0, plugin.getManager().getLore(enchantment, meta.getStoredEnchantLevel(enchantment)));
                }
                if (plugin.getManager().getCustomEnchants(result) > 3) {
                    return;
                }
                ItemMeta newMeta = result.getItemMeta();
                newMeta.setLore(lore);
                result.setItemMeta(newMeta);
                inv.setItem(2, result);
            }
        }, 5);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnchantCombine(final InventoryClickEvent e) {
        if (!(e.getInventory() instanceof AnvilInventory)) {
            return;
        }
        final AnvilInventory inv = (AnvilInventory) e.getInventory();
        if (inv.getItem(2) != null) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                ItemStack book1 = inv.getItem(0);
                ItemStack book2 = inv.getItem(1);
                if (book1 == null || book1.getType() != Material.ENCHANTED_BOOK || book2 == null || book2.getType() != Material.ENCHANTED_BOOK) {
                    return;
                }
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book1.getItemMeta();
                EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) book2.getItemMeta();
                ItemStack result = book1.clone();
                List<String> lore = result.getItemMeta().hasLore() ? result.getItemMeta().getLore() : new ArrayList<String>();
                for (Enchantment enchantment : meta.getStoredEnchants().keySet()) {
                    if (!(enchantment instanceof CustomEnchant)) {
                        return;
                    }
                    if (!meta2.hasStoredEnchant(enchantment)) {
                        continue;
                    }
                    int level = meta.getStoredEnchantLevel(enchantment) + meta2.getStoredEnchantLevel(enchantment);
                    lore = plugin.getManager().removeLore(enchantment, lore);
                    result.addUnsafeEnchantment(enchantment, level > enchantment.getMaxLevel() ? enchantment.getMaxLevel() : level);
                    lore.add(0, plugin.getManager().getLore(enchantment, level > enchantment.getMaxLevel() ? enchantment.getMaxLevel() : level));
                }
                ItemMeta newMeta = result.getItemMeta();
                newMeta.setLore(lore);
                result.setItemMeta(newMeta);
                inv.setItem(2, result);
            }
        }, 5);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClaimEnchant(InventoryClickEvent e) {
        if (!(e.getInventory() instanceof AnvilInventory)) {
            return;
        }
        if (e.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        boolean hasCustom = false;
        for (Enchantment enchantment : e.getCurrentItem().getEnchantments().keySet()) {
            if (enchantment instanceof CustomEnchant) {
                hasCustom = true;
                break;
            }
        }
        if (!hasCustom) {
            return;
        }
        e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
        e.getInventory().setItem(0, null);
        e.getInventory().setItem(1, null);
    }
}
