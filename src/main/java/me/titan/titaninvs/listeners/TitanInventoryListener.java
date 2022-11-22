package me.titan.titaninvs.listeners;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class TitanInventoryListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e){


		Player p = (Player) e.getWhoClicked();
		if(e.getClickedInventory() == null || e.getClickedInventory().getHolder() == null) return;
		if(!(e.getClickedInventory().getHolder() instanceof InventoryContents titanInv)){
			return;
		}
		if(e.getAction() == InventoryAction.COLLECT_TO_CURSOR || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
			e.setCancelled(true);
			return;
		}


		if(e.getAction() == InventoryAction.NOTHING && e.getClick() != ClickType.MIDDLE) {
			e.setCancelled(true);
			return;
		}

		if(e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {

				e.setCancelled(true);



			if(titanInv.containsKey(e.getSlot()) ){
				ClickableItem item = titanInv.get(e.getSlot());
				if(item.getConsumer() != null){
					item.getConsumer().accept(e);
				}
			}

			p.updateInventory();
		}
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent e){
		if(!(e.getInventory().getHolder() instanceof InventoryContents con)){
			return;
		}
		con.getTitanInv().onOpen(e);
	}
	@EventHandler
	public void onClose(InventoryCloseEvent e){
		if(!(e.getInventory().getHolder() instanceof InventoryContents con)){
			return;
		}
		con.getTitanInv().onClose(e);
	}


}
