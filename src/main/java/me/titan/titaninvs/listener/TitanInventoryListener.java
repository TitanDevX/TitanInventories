package me.titan.titaninvs.listener;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.two.ClickEvent;
import me.titan.titaninvs.content.two.ClickableItem2;
import me.titan.titaninvs.invs.TitanChestInv;
import me.titan.titaninvs.invs.TitanInv;
import me.titan.titaninvs.invs.other.AdvancedInventoryFlags;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TitanInventoryListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e){


		Player p = (Player) e.getWhoClicked();
		if(e.getClickedInventory() == null) return;
		Inventory titanBukkitInv = e.getView().getTopInventory();
		if(!(titanBukkitInv.getHolder() instanceof TitanInv)){
			return;
		}
		TitanInv titanInv = (TitanInv) titanBukkitInv.getHolder();
		if(titanInv == null) return;
		if(e.getClickedInventory() == titanBukkitInv ) {
			titanInv.onClick(e);

				if (titanInv.isProtected())
					e.setCancelled(true);
				if (titanInv.containsKey(e.getSlot())) {
					ClickableItem item = titanInv.get(e.getSlot());
					if (item.getConsumer() != null) {
						item.getConsumer().accept(e);
						// make sure it gets canceled even for non-protected inventory
						e.setCancelled(true);
						if (titanInv.isProtected())
							p.updateInventory();
						return;
					}else if(item instanceof ClickableItem2){
						ClickableItem2 v2 = (ClickableItem2) item;
						if(v2.getV2Consumer() != null){
							v2.getV2Consumer().accept(new ClickEvent(titanInv.getContext(),e));
						}

					}
				}


			handleInvClick(e, titanInv);
		}
		if(titanInv instanceof TitanChestInv ) {
			TitanChestInv c = (TitanChestInv) titanInv;
			handleChestInvClick(e,c);
		}
	}
	private void handleInvClick(InventoryClickEvent e, TitanInv titanInv){
		if(titanInv.isProtected()) {
			if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				e.setCancelled(true);
				return;
			}


			if (e.getAction() == InventoryAction.NOTHING && e.getClick() != ClickType.MIDDLE) {
				e.setCancelled(true);
				return;
			}
		}else{
			// advaned inv flags listener.
			List<AdvancedInventoryFlags> f = titanInv.getAdvancedFlags();
			if(f != null) { // no flags.
				InventoryView view = e.getView(); // we'll need this
				Inventory clickedInventory = e.getClickedInventory();
				if (f.contains(AdvancedInventoryFlags.PREVENT_ADD)) {

					if (clickedInventory == null) return;
//
					if (clickedInventory.equals(view.getTopInventory())) {
						if (!isNull(e.getCursor())) {
							switch (e.getAction()) {
								case PLACE_ALL:
								case PLACE_ONE:
								case PLACE_SOME:// when the stack doesn't fit over what's there already and only a part of the items in the cursor are placed
								case HOTBAR_SWAP:
								case SWAP_WITH_CURSOR:
									e.setCancelled(true);
									break;
								default:
									break;
							}
						} else if (e.getAction() == InventoryAction.HOTBAR_SWAP) // when the player swaps the item in the hovered slot with a hotbar item
							e.setCancelled(true);
					}
				} else if (f.contains(AdvancedInventoryFlags.PREVENT_TAKE)) {
					if (clickedInventory.equals(view.getTopInventory())) {
						if (isNull(e.getCursor())) {

						}
					}
				}
			}
		}
	}

	private boolean handleMoveToOtherInv(InventoryClickEvent e, TitanChestInv c){

		Player p = (Player)e.getWhoClicked();
		InventoryView v = e.getView();
		Inventory clicked = e.getClickedInventory();
		Inventory target = clicked == v.getTopInventory() ? v.getBottomInventory() : v.getTopInventory();

		ItemStack item = e.getCurrentItem();
		if(isNull(item)) return false;
		int finalAmount = item.getAmount();
		for (int i = 0; i < target.getContents().length; i++) {
			ItemStack ii = target.getItem(i);
			if(finalAmount >= item.getAmount() || finalAmount <= 0) break;
			if(isNull(ii)) {
				finalAmount = item.getAmount();
				if(target == v.getTopInventory()){
					if(!c.onItemAdd(i,item,p,finalAmount,ii)){
						return true;
					}
				}else{
				 if(!c.onItemRemove(e.getSlot(),item,p,finalAmount,null)){
					 return true;
				 }
				}
			}else if(ii.isSimilar(item) && ii.getAmount() < ii.getMaxStackSize()){
				int transferAmount =
						Math.min(finalAmount, ii.getMaxStackSize() - ii.getAmount());
				// affectedSlots[i] = targetItem.clone()
				// affectedSlots[i]!!.amount =  transferAmount
				if(target == v.getTopInventory()){
					 if(!c.onItemAdd(i,item,p,transferAmount,ii)){
						 return true;
					 }
				}else{
					if(!c.onItemRemove(e.getSlot(),item,p,transferAmount,null)){
						return true;
					}
				}

				finalAmount -= transferAmount;
			}
		}
		return false;

	}
	private void handleChestInvClick(InventoryClickEvent e, TitanChestInv c){
		Player p = (Player) e.getWhoClicked();
		if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ){
			boolean ev = c.onMoveToOtherInv(e);
			e.setCancelled(!ev);
			if(ev && c.preciseInvItemMoveEvents()){
				e.setCancelled(handleMoveToOtherInv(e,c));
			}

			return;
		}

		if(e.getAction() == InventoryAction.COLLECT_TO_CURSOR){
			if(c.onCollectToCursor(e)) {

				return;
			}
			ItemStack cursor = e.getCursor();
			Inventory topInv = e.getView().getTopInventory();
			if (cursor == null || cursor.getType().isAir()) return;

			Material type = cursor.getType();
			int collected = 0;

			int dif = cursor.getMaxStackSize() - cursor.getAmount();
			for (int i = 0; i < topInv.getContents().length; i++) {
				ItemStack item = topInv.getItem(i);
				if(item == null) continue;
				if(i == e.getSlot()) continue;
				if(collected >= dif){
					break;
				}
				if ( item.getType() == type && item.isSimilar(cursor) ) {
					collected += Math.min(item.getAmount(),dif-collected);
				}
			}

			if (collected > 0) {
				e.setCancelled(!c.onItemRemove(e.getSlot(),e.getCursor(),p,collected,null));
			}
			return;
		}

		if(e.getClickedInventory() != e.getView().getTopInventory()) return;

		if(!isNull(e.getCursor())){
			int amount = 0;
			if(e.getAction() == InventoryAction.PLACE_ALL){
				amount = e.getCursor().getAmount();
			}else if(e.getAction() == InventoryAction.PLACE_ONE){
				amount = 1;
			}else if(e.getAction() == InventoryAction.PLACE_SOME && !isNull(e.getCurrentItem())){
				amount = Math.abs(e.getCursor().getAmount()-e.getCurrentItem().getAmount());
			}
			if(amount != 0){
				e.setCancelled(!c.onItemAdd(e.getSlot(),e.getCursor(),p,amount,e.getCurrentItem()));
			}
		}else
		if(!isNull(e.getCurrentItem()) && isNull(e.getCursor())) {
			int amount = 0;


			if (e.getAction() == InventoryAction.PICKUP_ALL
					 || e.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || e.getAction() == InventoryAction.HOTBAR_SWAP){
				amount = e.getCurrentItem().getAmount();
			}else if(e.getAction() == InventoryAction.PICKUP_HALF){
				if(e.getCurrentItem().getAmount() == 1){
					amount = 1;
				}else{
					amount = (int) Math.ceil(e.getCurrentItem().getAmount()/2d);
				}

			}else if(e.getAction() == InventoryAction.PICKUP_ONE){
				amount = 1;
			}else if(e.getAction() == InventoryAction.DROP_ALL_SLOT){
				amount = e.getCurrentItem().getAmount();
			}else if(e.getAction() == InventoryAction.DROP_ONE_SLOT){
				amount = 1;
			}
			if(amount != 0){
				e.setCancelled(!c.onItemRemove(e.getSlot(),e.getCurrentItem(),p,amount,e.getCurrentItem()));
			}


		}

		if(e.getAction() == InventoryAction.SWAP_WITH_CURSOR){
			e.setCancelled(!c.onItemRemove(e.getSlot(),e.getCurrentItem(),p,e.getCurrentItem().getAmount(),e.getCurrentItem()));
			if(!isNull(e.getCursor()))
				e.setCancelled(!c.onItemAdd(e.getSlot(),e.getCursor(),p,e.getCursor().getAmount(),e.getCurrentItem()));
		}
	}
	@EventHandler
	public void onDrag(InventoryDragEvent e){
		if(e.getInventory().getHolder() == null) return;
		if(!(e.getInventory().getHolder() instanceof TitanInv)){
			return;
		}
		TitanInv titanInv = (TitanInv) e.getInventory().getHolder();
		titanInv.onDrag(e);
		if(titanInv.isProtected()){
			e.setCancelled(true);
			return;
		}else {
			// advanced flag listener.
			List<AdvancedInventoryFlags> f = titanInv.getAdvancedFlags();
			if(f != null) {
				if(f.contains(AdvancedInventoryFlags.PREVENT_ADD)){
					if (e.getInventory().equals(e.getView().getTopInventory()))
						e.setCancelled(true);
				}
			}


		}

	}
	@EventHandler
	public void onOpen(InventoryOpenEvent e){
		if(!(e.getInventory().getHolder() instanceof TitanInv)){
			return;
		}
		TitanInv titanInv = (TitanInv) e.getInventory().getHolder();
		titanInv.onOpen(e);
	}
	@EventHandler
	public void onClose(InventoryCloseEvent e){
		if(!(e.getInventory().getHolder() instanceof TitanInv)){
			return;
		}

		TitanInv titanInv = (TitanInv) e.getInventory().getHolder();
		if(!titanInv.isFlipingPage())
			titanInv.onClose(e);
	}


	@EventHandler
	public void onOwnClick(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		if(e.getClickedInventory() == null) return;
		if(e.getClickedInventory().equals(e.getView().getBottomInventory())){
			Inventory top = e.getView().getTopInventory();
			if(top.getHolder() instanceof TitanInv){
				TitanInv ti = (TitanInv) top.getHolder();
				ti.onClick(e);
				if(ti instanceof TitanChestInv){

					return;
				}
				if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && ti.isProtected()){
					e.setCancelled(true);
					return;
				}
				if(ti.preventClickOwnInv()){
					e.setCancelled(true);
					return;
				}
				List<AdvancedInventoryFlags> f = ti.getAdvancedFlags();
				if(f != null) {  // no flags.
					if (f.contains(AdvancedInventoryFlags.PREVENT_ADD)) {
						if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
							e.setCancelled(true);
					}
				}
			}
		}
	}
	public boolean isNull(ItemStack item){
		return item == null || item.getType() == Material.AIR || item.getAmount() <= 0;
	}
}
