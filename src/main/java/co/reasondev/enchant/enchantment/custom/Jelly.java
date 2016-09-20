package co.reasondev.enchant.enchantment.custom;

import co.reasondev.enchant.enchantment.CustomEnchant;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Jelly extends CustomEnchant {

    public Jelly(int id, String name, int maxLevel) {
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


    @Override
    public void handleItemChange(PlayerItemHeldEvent e) {

    }

    @Override
    public void handleItemEquip(ArmorEquipEvent e) {

    }

    @Override
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof LivingEntity)) {
            return;
        }
        Player p = (Player) e.getEntity();
        LivingEntity damager = (LivingEntity) e.getDamager();
        int level = 0;
        for (ItemStack armor : p.getInventory().getArmorContents()) {
            if (hasEnchant(armor)) {
//                if (level < armor.getEnchantments().get(this)) {
                level += armor.getEnchantments().get(this);
//                }
            }
        }
        Vector push = damager.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
        damager.setVelocity(push.multiply(level));
    }

    @Override
    public void handleEntityDeath(EntityDeathEvent e) {

    }

    @Override
    public void handleBlockBreak(BlockBreakEvent e) {

    }
}
