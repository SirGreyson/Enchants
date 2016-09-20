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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Adrenaline extends CustomEnchant {

    private Random random = new Random();

    public Adrenaline(int id, String name, int maxLevel) {
        super(id, name, maxLevel);
    }

    private boolean canApply(int level) {
        int chance = random.nextInt(100) + 1;
        switch (level) {
            case 0:
                return false;
            case 1:
                return chance <= 10;
            case 2:
                return chance <= 15;
            default:
                return chance <= 20;
        }
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return enchantment instanceof Agility;
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
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        if (player.getHealth() > 8 || player.hasPotionEffect(PotionEffectType.SPEED)) {
            return;
        }
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (hasEnchant(armor)) {
                int level = armor.getEnchantments().get(this);
                if (canApply(level)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
                    break;
                }
            }
        }
    }

    @Override
    public void handleEntityDeath(EntityDeathEvent e) {

    }

    @Override
    public void handleBlockBreak(BlockBreakEvent e) {

    }
}
