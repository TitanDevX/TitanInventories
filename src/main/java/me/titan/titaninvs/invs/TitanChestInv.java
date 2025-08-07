package me.titan.titaninvs.invs;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TitanChestInv extends TitanInv{
    public TitanChestInv(String title, int size) {
        super(title, size);
    }

    public TitanChestInv(){
        super();
    }

    public boolean onItemAdd(int slot, ItemStack item,Player p, int amount, @Nullable ItemStack existingItem){return true;}
    public boolean onItemRemove(int slot, ItemStack item,Player p, int amount, @Nullable ItemStack existingItem){return true;}
    public boolean onMoveToOtherInv(InventoryClickEvent e) {return true;}

    /**
     *
     * @param e
     * @return true if the event was handled externally, false for default behivour.
     */
    public boolean onCollectToCursor(InventoryClickEvent e){return false;}
    @Override
    public boolean isProtected() {
        return false;
    }

    public boolean preciseInvItemMoveEvents() {
        return true;
    }
}
