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

public class Grub extends CustomEnchant {

    public Grub(int id, String name, int maxLevel) {
        super(id, name, maxLevel);
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_HEAD;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType().toString().contains("_HELMET");
    }


    @Override
    public void handleItemChange(PlayerItemHeldEvent e) {

    }

    @Override
    public void handleItemEquip(ArmorEquipEvent e) {
        if (hasEnchant(e.getOldArmorPiece())) {
            e.getPlayer().removePotionEffect(PotionEffectType.SATURATION);
        }
        if (hasEnchant(e.getNewArmorPiece())) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0));
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
