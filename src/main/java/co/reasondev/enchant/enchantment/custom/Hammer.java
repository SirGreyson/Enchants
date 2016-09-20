package co.reasondev.enchant.enchantment.custom;

import co.reasondev.enchant.enchantment.CustomEnchant;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Hammer extends CustomEnchant {

    public Hammer(int id, String name, int maxLevel, int minLevel) {
        super(id, name, maxLevel, minLevel);
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

    private BlockFace getBlockFace(Player player, Block broken) {
        List<Block> blocks = player.getLastTwoTargetBlocks((Set<Material>) null, 8);
        return blocks.get(1).getFace(blocks.get(0)).getOppositeFace();
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
        World world = e.getBlock().getWorld();
        int x = e.getBlock().getLocation().getBlockX();
        int y = e.getBlock().getLocation().getBlockY();
        int z = e.getBlock().getLocation().getBlockZ();
        List<Block> toBreak = new ArrayList<>();
        BlockFace face = getBlockFace(e.getPlayer(), e.getBlock());
        switch (face) {
            case UP:
            case DOWN:
                toBreak.add(world.getBlockAt(x + 1, y, z));
                toBreak.add(world.getBlockAt(x - 1, y, z));
                toBreak.add(world.getBlockAt(x, y, z + 1));
                toBreak.add(world.getBlockAt(x, y, z - 1));
                toBreak.add(world.getBlockAt(x + 1, y, z + 1));
                toBreak.add(world.getBlockAt(x - 1, y, z - 1));
                toBreak.add(world.getBlockAt(x + 1, y, z - 1));
                toBreak.add(world.getBlockAt(x - 1, y, z + 1));
                break;
            case EAST:
            case WEST:
                toBreak.add(world.getBlockAt(x, y, z + 1));
                toBreak.add(world.getBlockAt(x, y, z - 1));
                toBreak.add(world.getBlockAt(x, y + 1, z));
                toBreak.add(world.getBlockAt(x, y - 1, z));
                toBreak.add(world.getBlockAt(x, y + 1, z + 1));
                toBreak.add(world.getBlockAt(x, y - 1, z - 1));
                toBreak.add(world.getBlockAt(x, y - 1, z + 1));
                toBreak.add(world.getBlockAt(x, y + 1, z - 1));
                break;
            case NORTH:
            case SOUTH:
                toBreak.add(world.getBlockAt(x + 1, y, z));
                toBreak.add(world.getBlockAt(x - 1, y, z));
                toBreak.add(world.getBlockAt(x, y + 1, z));
                toBreak.add(world.getBlockAt(x, y - 1, z));
                toBreak.add(world.getBlockAt(x + 1, y + 1, z));
                toBreak.add(world.getBlockAt(x - 1, y - 1, z));
                toBreak.add(world.getBlockAt(x + 1, y - 1, z));
                toBreak.add(world.getBlockAt(x - 1, y + 1, z));
                break;
            default:
                break;
        }
        for (Block block : toBreak) {
            if (block.getType() == Material.AIR || block.getType() == Material.BEDROCK) {
                continue;
            }
            block.breakNaturally(e.getPlayer().getItemInHand());
        }
    }
}
