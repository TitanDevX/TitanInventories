package me.titan.titaninvs.content.itemglow;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemGlowManager {
    static int whatMethod = 0;
    public static void init(){
        if(GlowEnchant.register()){
            whatMethod = 1;
        }else{
            whatMethod = 2;
        }
    }
    public static ItemStack glow(ItemStack item){
        if(whatMethod == 1){
            item.addUnsafeEnchantment(GlowEnchant.instance,1);
        }else{
            item.addUnsafeEnchantment(Enchantment.WATER_WORKER,1);
            ItemMeta im = item.getItemMeta();
            if(!item.hasItemMeta()){
                im = Bukkit.getItemFactory().getItemMeta(item.getType());
            }
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(im);
        }
        return item;
    }
    public static boolean isGlow(ItemStack item){
        if(!item.hasItemMeta()) return false;
        if(whatMethod == 1){
            return (item.getItemMeta().getEnchantLevel(GlowEnchant.instance)) != 0;
        }else{
            return item.getItemMeta().getEnchantLevel(Enchantment.WATER_WORKER) != 0 && item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS);
        }
    }
    public static boolean isGlowEnchant(Enchantment ench){
        if(whatMethod == 1){
            return ench == GlowEnchant.instance;
        }else{
            return ench == Enchantment.WATER_WORKER;
        }
    }
    public static ItemStack unglow(ItemStack item){
        if(whatMethod == 1){
            item.removeEnchantment(GlowEnchant.instance);
        }else{
            item.removeEnchantment(Enchantment.WATER_WORKER);
            ItemMeta im = item.getItemMeta();
            if(!item.hasItemMeta()){
                im = Bukkit.getItemFactory().getItemMeta(item.getType());
            }
            im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(im);
        }
        return item;
    }
}
