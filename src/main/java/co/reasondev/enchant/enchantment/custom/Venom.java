package co.reasondev.enchant.enchantment.custom;

import co.reasondev.enchant.enchantment.CustomEnchant;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Venom extends CustomEnchant {

    private Random random = new Random();

    public Venom(int id, String name, int maxLevel) {
        super(id, name, maxLevel);
    }

    private boolean canPoison(int level) {
        int chance = random.nextInt(100) + 1;
        switch (level) {
            case 1:
                return chance <= 20;
            case 2:
                return chance <= 30;
            default:
                return chance <= 50;
        }
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.BOW;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType() == Material.BOW;
    }

    @Override
    public void handleItemChange(PlayerItemHeldEvent e) {

    }

    @Override
    public void handleItemEquip(ArmorEquipEvent e) {
    }

    @Override
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity) || !(e.getDamager() instanceof Arrow)) {
            return;
        }
        if (!(((Arrow) e.getDamager()).getShooter() instanceof Player)) {
            return;
        }
        LivingEntity entity = (LivingEntity) e.getEntity();
        Player shooter = (Player) ((Arrow) e.getDamager()).getShooter();
        int level = shooter.getItemInHand().getEnchantments().get(this);
        if (canPoison(level)) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
        }
    }

    @Override
    public void handleEntityDeath(EntityDeathEvent e) {

    }

    @Override
    public void handleBlockBreak(BlockBreakEvent e) {

    }
}
