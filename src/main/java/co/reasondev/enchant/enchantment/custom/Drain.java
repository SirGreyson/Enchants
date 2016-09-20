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

public class Drain extends CustomEnchant {

    public Drain(int id, String name, int maxLevel) {
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

    public double getMultiplier(int level) {
        switch (level) {
            case 0:
                return 1;
            case 1:
                return 1.5;
            case 2:
                return 1.75;
            default:
                return 2.0;
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

    }

    @Override
    public void handleEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null || e.getEntity().getKiller().getItemInHand() == null) {
            return;
        }
        Player killer = e.getEntity().getKiller();
        int level = killer.getItemInHand().getEnchantments().get(this);
        e.setDroppedExp((int) (e.getDroppedExp() * getMultiplier(level)));
    }

    @Override
    public void handleBlockBreak(BlockBreakEvent e) {

    }
}
