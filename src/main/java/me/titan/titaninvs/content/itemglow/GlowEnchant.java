package me.titan.titaninvs.content.itemglow;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class GlowEnchant extends Enchantment {
    public static GlowEnchant instance;
    public GlowEnchant() {
        super(NamespacedKey.fromString("titanlib:glow"));
    }

    @Override
    public String getName() {
        return "TitanGlow";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEARABLE;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem( ItemStack itemStack) {
        return false;
    }
    public static boolean register() {
        try {
            instance = new GlowEnchant();
            boolean isAcc= Enchantment.isAcceptingRegistrations();
            if(!isAcc) {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            }

            Enchantment.registerEnchantment(instance);
            if(!isAcc){
                Enchantment.stopAcceptingRegistrations();
            }
        }catch(Throwable t){
           return false;
        }
        return true;
    }
}
