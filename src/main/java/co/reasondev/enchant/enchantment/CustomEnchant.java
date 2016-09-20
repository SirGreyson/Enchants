package co.reasondev.enchant.enchantment;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchant extends Enchantment {

    private String name;
    int maxLevel;
    int minLevel = 1;

    public CustomEnchant(int id, String name, int maxLevel) {
        super(id);
        this.name = name;
        this.maxLevel = maxLevel;
    }

    public CustomEnchant(int id, String name, int maxLevel, int minLevel) {
        super(id);
        this.name = name;
        this.maxLevel = maxLevel;
        this.minLevel = minLevel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getStartLevel() {
        return minLevel;
    }

    @Override
    public abstract EnchantmentTarget getItemTarget();

    @Override
    public abstract boolean conflictsWith(Enchantment enchantment);

    @Override
    public abstract boolean canEnchantItem(ItemStack itemStack);

    public boolean hasEnchant(ItemStack itemStack) {
        return itemStack != null && itemStack.getEnchantments() != null && itemStack.getEnchantments().containsKey(this);
    }

    public abstract void handleItemChange(PlayerItemHeldEvent e);

    public abstract void handleItemEquip(ArmorEquipEvent e);

    public abstract void handleEntityDamageByEntity(EntityDamageByEntityEvent e);

    public abstract void handleEntityDeath(EntityDeathEvent e);

    public abstract void handleBlockBreak(BlockBreakEvent e);
}
