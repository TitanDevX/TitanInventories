package me.titan.titaninvs.invs;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class TitanChestInv extends TitanInv{
    public TitanChestInv(String title, int size) {
        super(title, size);
    }


    public boolean onItemAdd(int slot, ItemStack item,Player p, int amount){return false;}
    public boolean onItemRemove(int slot, ItemStack item,Player p, int amount){return false;}

    @Override
    public boolean isProtected() {
        return false;
    }
}
