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

import java.util.Random;

public class Leech extends CustomEnchant {

    Random random = new Random();

    public Leech(int id, String name, int maxLevel) {
        super(id, name, maxLevel);
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType().toString().contains("_SWORD");
    }

    private boolean canLeech(int level) {
        int chance = random.nextInt(100) + 1;
        switch (level) {
            case 0:
                return false;
            case 1:
                return chance <= 5;
            case 2:
                return chance <= 10;
            default:
                return chance <= 15;
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
        if (!(e.getDamager() instanceof Player) || ((Player) e.getDamager()).getItemInHand() == null) {
            return;
        }
        Player player = (Player) e.getDamager();
        int level = ((Player) e.getDamager()).getItemInHand().getEnchantments().get(this);
        if (canLeech(level) && player.getHealth() < player.getMaxHealth()) {
            double newHealth = player.getHealth() + (level > 2 ? 4 : 2);
            player.setHealth(newHealth > player.getMaxHealth() ? player.getMaxHealth() : newHealth);
        }
    }

    @Override
    public void handleEntityDeath(EntityDeathEvent e) {

    }

    @Override
    public void handleBlockBreak(BlockBreakEvent e) {

    }
}
