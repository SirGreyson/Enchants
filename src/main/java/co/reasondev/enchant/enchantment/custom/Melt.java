package co.reasondev.enchant.enchantment.custom;

import co.reasondev.enchant.enchantment.CustomEnchant;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Melt extends CustomEnchant {

    private Random random = new Random();

    public Melt(int id, String name, int maxLevel) {
        super(id, name, maxLevel);
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return enchantment instanceof Hammer;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType().toString().contains("_PICKAXE");
    }

    private ItemStack getDrops(Block block, ItemStack pick) {
        Material ingot = block.getType() == Material.GOLD_ORE ? Material.GOLD_INGOT : Material.IRON_INGOT;
        int level = pick.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        return new ItemStack(ingot, random.nextInt((level <= 0 ? 1 : level) + 2));
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

    }

    @Override
    public void handleBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() != Material.IRON_ORE && e.getBlock().getType() != Material.GOLD_ORE) {
            return;
        }
        ItemStack pick = e.getPlayer().getItemInHand();
        e.getBlock().getDrops().clear();
        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), getDrops(e.getBlock(), pick));
    }
}
