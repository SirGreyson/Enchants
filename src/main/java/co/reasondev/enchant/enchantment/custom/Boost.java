package co.reasondev.enchant.enchantment.custom;

import co.reasondev.enchant.enchantment.CustomEnchant;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class Boost extends CustomEnchant {

    public Boost(int id, String name, int maxLevel, int minLevel) {
        super(id, name, maxLevel, minLevel);
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
        if (hasEnchant(e.getOldArmorPiece())) {
            int level = 0;
            for (ItemStack armor : e.getPlayer().getInventory().getArmorContents()) {
                if (hasEnchant(armor) && !armor.equals(e.getOldArmorPiece())) {
                    if (level < armor.getEnchantments().get(this)) {
                        level = armor.getEnchantments().get(this);
                    }
                }
            }
            e.getPlayer().setMaxHealth(20 + (level > 1 ? level * 2 : 0));
        }
        if (hasEnchant(e.getNewArmorPiece())) {
            int level = e.getNewArmorPiece().getEnchantments().get(this);
            for (ItemStack armor : e.getPlayer().getInventory().getArmorContents()) {
                if (hasEnchant(armor) && !armor.equals(e.getNewArmorPiece())) {
                    if (level < armor.getEnchantments().get(this)) {
                        level = armor.getEnchantments().get(this);
                    }
                }
            }
            e.getPlayer().setMaxHealth(20 + (level > 1 ? level * 2 : 0));
        }
    }

    @Override
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {

    }

    @Override
    public void handleEntityDeath(EntityDeathEvent e) {

    }

    @Override
    public void handleBlockBreak(BlockBreakEvent e) {

    }
}
