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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ObsidianBreaker extends CustomEnchant {

    private Random random = new Random();

    public ObsidianBreaker(int id, String name, int maxLevel) {
        super(id, name, maxLevel);
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType().toString().contains("_PICKAXE");
    }

    private ItemStack getDrops(Block block, ItemStack pick) {
        int level = pick.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        return new ItemStack(Material.OBSIDIAN, random.nextInt((level <= 0 ? 1 : level) + 2));
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

    }

    public void handleBlockHit(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
        if (b.getType() != Material.OBSIDIAN) {
            return;
        }
        //TODO Make sure Player can actually break Block?
        b.setType(Material.AIR);
        b.getWorld().dropItemNaturally(b.getLocation(), getDrops(b, e.getItem()));
    }
}
