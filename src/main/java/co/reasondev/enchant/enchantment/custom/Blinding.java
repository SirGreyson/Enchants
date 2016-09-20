package co.reasondev.enchant.enchantment.custom;

import co.reasondev.enchant.enchantment.CustomEnchant;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
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

public class Blinding extends CustomEnchant {

    private Random random = new Random();

    public Blinding(int id, String name, int maxLevel) {
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

    private boolean canBlind(int level) {
        int chance = random.nextInt(100) + 1;
        switch (level) {
            case 0:
                return false;
            case 1:
                return chance <= 3;
            case 2:
                return chance <= 5;
            default:
                return chance <= 8;
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
        if (!(e.getEntity() instanceof LivingEntity) || !(e.getDamager() instanceof Player)) {
            return;
        }
        if (((Player) e.getDamager()).getItemInHand() == null) {
            return;
        }
        LivingEntity entity = (LivingEntity) e.getEntity();
        Player player = (Player) e.getDamager();
        int level = player.getItemInHand().getEnchantments().get(this);
        if (canBlind(level)) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * (level + 1), 1));
        }
    }

    @Override
    public void handleEntityDeath(EntityDeathEvent e) {

    }

    @Override
    public void handleBlockBreak(BlockBreakEvent e) {

    }
}
