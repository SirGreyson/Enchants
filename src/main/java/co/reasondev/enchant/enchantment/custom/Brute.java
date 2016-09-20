package co.reasondev.enchant.enchantment.custom;

import co.reasondev.enchant.enchantment.CustomEnchant;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class Brute extends CustomEnchant {

    public Brute(int id, String name, int maxLevel) {
        super(id, name, maxLevel);
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType().toString().contains("_HELMET") || itemStack.getType().toString().contains("_CHESTPLATE")
                || itemStack.getType().toString().contains("_LEGGINGS") || itemStack.getType().toString().contains("_BOOTS");
    }

    private double getMultiplier(int level) {
        switch (level) {
            case 0:
                return 1;
            case 1:
                return 1.1;
            case 2:
                return 1.35;
            default:
                return 1.8;
        }
    }

    @Override
    public void handleItemChange(PlayerItemHeldEvent e) {

    }

    @Override
    public void handleItemEquip(ArmorEquipEvent e) {

    }

    @Override
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        Player damager = (Player) e.getDamager();
        int level = 0;
        for (ItemStack armor : damager.getInventory().getArmorContents()) {
            if (hasEnchant(armor)) {
                level += armor.getEnchantments().get(this);
            }
        }
        e.setDamage(e.getFinalDamage() * getMultiplier(level));
    }

    @Override
    public void handleEntityDeath(EntityDeathEvent e) {

    }

    @Override
    public void handleBlockBreak(BlockBreakEvent e) {

    }
}
