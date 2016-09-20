package co.reasondev.enchant.enchantment;

import co.reasondev.enchant.enchantment.custom.*;

public enum CustomType {

    ADRENALINE(new Adrenaline(75, "ADRENALINE", 3)),
    AGILITY(new Agility(76, "AGILITY", 2)),
    AQUA(new Aqua(77, "AQUA", 1)),
    BLINDING(new Blinding(78, "BLINDING", 3)),
    BOOST(new Boost(79, "BOOST", 3, 2)),
    BOUNCE(new Bounce(80, "BOUNCE", 3)),
    BRUTE(new Brute(81, "BRUTE", 3)),
    BULK(new Bulk(82, "BULK", 3)),
    DECAY(new Decay(83, "DECAY", 3)),
    DRAIN(new Drain(84, "DRAIN", 3)),
    EXTINGUISHER(new Extinguisher(85, "EXTINGUISHER", 1)),
    GRUB(new Grub(86, "GRUB", 1)),
    HAMMER(new Hammer(87, "HAMMER", 2, 2)),
    JELLY(new Jelly(88, "JELLY", 3)),
    LEECH(new Leech(89, "LEECH", 3)),
    MELT(new Melt(90, "MELT", 1)),
    //TODO Dense?
    OBSIDIAN_BREAKER(new ObsidianBreaker(91, "OBSIDIAN_BREAKER", 1)),
    RESTORE(new Restore(92, "RESTORE", 2)),
    VENOM(new Venom(93, "VENOM", 3));

    private CustomEnchant enchantment;

    CustomType(CustomEnchant enchantment) {
        this.enchantment = enchantment;
    }

    public CustomEnchant getEnchantment() {
        return enchantment;
    }
}
