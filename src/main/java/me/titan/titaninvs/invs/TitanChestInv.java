package me.titan.titaninvs.invs;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class TitanChestInv extends TitanInv{
    public TitanChestInv(String title, int size) {
        super(title, size);
    }


    public void onItemAdd(int slot, ItemStack item,Player p, int amount){}
    public void onItemRemove(int slot, ItemStack item,Player p, int amount){}

    @Override
    public boolean isProtected() {
        return false;
    }
}
