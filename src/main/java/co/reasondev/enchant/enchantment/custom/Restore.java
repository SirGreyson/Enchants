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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Restore extends CustomEnchant {

    public Restore(int id, String name, int maxLevel) {
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
        int counter = 0;
        if (hasEnchant(e.getOldArmorPiece())) {
            for (ItemStack armor : e.getPlayer().getInventory().getArmorContents()) {
                if (hasEnchant(armor) && !armor.equals(e.getOldArmorPiece())) {
                    counter++;
                }
            }
            if (counter < 2) {
                e.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
            }
        }
        counter = 0;
        int level = 0;
        if (hasEnchant(e.getNewArmorPiece())) {
            level = e.getNewArmorPiece().getEnchantments().get(this);
            for (ItemStack armor : e.getPlayer().getInventory().getArmorContents()) {
                if (hasEnchant(armor) && !armor.equals(e.getNewArmorPiece())) {
                    counter++;
                    if (armor.getEnchantments().get(this) > level) {
                        level = armor.getEnchantments().get(this);
                    }
                }
            }
        }
        if (counter >= 2) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, level > 2 ? 1 : level - 1));
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
