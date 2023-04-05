package me.titan.titaninvs.examples;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.ItemBuilder;
import me.titan.titaninvs.content.Pagination;
import me.titan.titaninvs.invs.TitanChestInv;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ChestInvExample extends TitanChestInv
{
    private final static Map<Integer, ItemStack> items = new HashMap<>();
    public ChestInvExample() {
        super("Chest Inv", 36);
    }

    @Override
    public void init(Player p) {
        Pagination page = new Pagination(27,false);
        for (Entry<Integer, ItemStack> en : items.entrySet()) {
             page.getItems().add( ClickableItem.empty(en.getValue()));
            page.getItems().add( ClickableItem.empty(en.getValue()));
        }
        pagination(page);
        setNextPageButton(35,ClickableItem.empty(ItemBuilder.create(Material.ARROW).getItemStack()));
        setPreviousPageButton(27,ClickableItem.empty(ItemBuilder.create(Material.ARROW).getItemStack()));
    }

    @Override
    public void onItemAdd(int slot, ItemStack item, Player p, int amount) {
        items.put(slot, item.clone());
    }

    @Override
    public void onItemRemove(int slot, ItemStack item, Player p, int amount) {
        if(items.containsKey(slot)){
            if(amount == item.getAmount()){
                items.remove(slot);
            }else{
                items.put(slot,item);
            }
        }
    }
}
