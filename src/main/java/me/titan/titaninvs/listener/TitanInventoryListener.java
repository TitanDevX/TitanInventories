package me.titan.titaninvs.listener;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.invs.TitanChestInv;
import me.titan.titaninvs.invs.TitanInv;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class TitanInventoryListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e){


		Player p = (Player) e.getWhoClicked();
		if(e.getClickedInventory() == null || e.getClickedInventory().getHolder() == null) return;
		if(!(e.getClickedInventory().getHolder() instanceof TitanInv titanInv)){
			return;
		}
		if(titanInv.isProtected()) {
			if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				e.setCancelled(true);
				return;
			}


			if (e.getAction() == InventoryAction.NOTHING && e.getClick() != ClickType.MIDDLE) {
				e.setCancelled(true);
				return;
			}
		}
		if(titanInv instanceof TitanChestInv c) {
			if(e.getCurrentItem() != null) {
				int amount = 0;

				if (e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || e.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || e.getAction() == InventoryAction.HOTBAR_SWAP){
					amount = e.getCurrentItem().getAmount();
				}else if(e.getAction() == InventoryAction.PICKUP_HALF){
					amount = e.getCurrentItem().getAmount()/2;
				}else if(e.getAction() == InventoryAction.PICKUP_ONE){
					amount = 1;
				}else if(e.getAction() == InventoryAction.DROP_ALL_SLOT){
					amount = e.getCurrentItem().getAmount();
				}else if(e.getAction() == InventoryAction.DROP_ONE_SLOT){
					amount = 1;
				}else if(e.getAction() == InventoryAction.COLLECT_TO_CURSOR){
					amount = e.getCurrentItem().getAmount();
				}
				if(amount != 0)
					c.onItemRemove(e.getSlot(),e.getCurrentItem(),p,amount);
				if(e.getAction() == InventoryAction.SWAP_WITH_CURSOR){
					e.setCancelled(c.onItemRemove(e.getSlot(),e.getCurrentItem(),p,e.getCurrentItem().getAmount()));
					if(e.getCursor() != null)
						e.setCancelled(c.onItemAdd(e.getSlot(),e.getCursor(),p,e.getCursor().getAmount()));
				}
			}else if(e.getCursor() != null){

				int amount = 0;
				if(e.getAction() == InventoryAction.PLACE_ALL){
					amount = e.getCursor().getAmount();
				}else if(e.getAction() == InventoryAction.PLACE_ONE){
					amount = 1;
				}
				if(amount != 0){
					e.setCancelled(c.onItemAdd(e.getSlot(),e.getCursor(),p,amount));
				}
			}

		}

		if(e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {

			if(titanInv.isProtected())
				e.setCancelled(true);



			if(titanInv.containsKey(e.getSlot()) ){
				ClickableItem item = titanInv.get(e.getSlot());
				if(item.getConsumer() != null){
					item.getConsumer().accept(e);
					// make sure it gets canceled even for non-protected inventory
					e.setCancelled(true);
				}
			}

			if(titanInv.isProtected())
				p.updateInventory();
		}
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e){
		if(e.getInventory().getHolder() == null) return;
		if(!(e.getInventory().getHolder() instanceof TitanInv titanInv)){
			return;
		}
		if(titanInv.isProtected()){
			e.setCancelled(true);
			return;
		}
		if(titanInv instanceof TitanChestInv c){
			for (Map.Entry<Integer, ItemStack> en : e.getNewItems().entrySet()) {
				c.onItemAdd(en.getKey(),en.getValue(),(Player) e.getWhoClicked(),en.getValue().getAmount());
			}
		}
	}
	@EventHandler
	public void onOpen(InventoryOpenEvent e){
		if(!(e.getInventory().getHolder() instanceof TitanInv ti)){
			return;
		}
		ti.onOpen(e);
	}
	@EventHandler
	public void onClose(InventoryCloseEvent e){
		if(!(e.getInventory().getHolder() instanceof TitanInv ti)){
			return;
		}
		ti.onClose(e);
	}


}
