package me.titan.titaninvs.examples;

import me.titan.titaninvs.content.*;
import me.titan.titaninvs.invs.UpdatingInv;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 *
 * Example of an auto updating GUI,
 * It iterates through items on each slot.
 *
 * given an array of items per slot.
 *
 */
public class ExampleUpdatingInv extends UpdatingInv {

	private static ExampleUpdatingInv instance ;

	//int currentIndex = 0;

	public ExampleUpdatingInv() {
		super("Test", 27,20);
	}
	public static void open(Player p, Map<Integer, ItemStack[]> map){
		if(instance == null){
			(instance = new ExampleUpdatingInv()).open(p,0,new Object[]{map, 1});
		}else{
			instance.open(p,0, new Object[]{map, 1});
		}
	}


	@Override
	public void init(Player p, InventoryContents con, Object[] data) {
		var map = (Map<Integer, ItemStack[]>) data[0];
		int currentIndex = (int) data[1];

		Pagination page = new Pagination(9,false);
		for(int i =0;i<map.size();i++){
			int finalI = i;

			page.getItems().add(ClickableItem.of(map.get(i)[currentIndex], (e) -> {
				e.getWhoClicked().sendMessage("Click action: i:" + finalI + " ind:" + currentIndex + ", material: " + map.get(finalI)[currentIndex]);
			}));
		}
		pagination(page);
		setNextPageButton(26,ClickableItem.empty(ItemBuilder.create(Material.ARROW).name("&cNext page").getItemStack()), con);
		setPreviousPageButton(18,ClickableItem.empty(ItemBuilder.create(Material.ARROW).name("&cPrevious page").getItemStack()), con);
	}

	@Override
	public void updateItems(Player p, InventoryContext cont) {


		InventoryContents con = cont.getContents();
		var map = (Map<Integer, ItemStack[]>) cont.getData()[0];
		int currentIndex = (int) cont.getData()[1];
		cont.getContents().setTitle("Page " + con.getPage());
		/*
		Might need to call con.clear method to clear contents but in our example we are overriding items
		in the same slots.
		 */
		if(map == null) return;
		//System.out.println("UPDATE");
		if(currentIndex >= map.get(0).length){
			currentIndex = 0;
		}
		Pagination page = getPagination();
		for(int i =0;i<map.size();i++){
			int finalI = i;
			int finalCurrentIndex = currentIndex;
			page.getItems().set(i, ClickableItem.of(map.get(i)[currentIndex], (e) -> {
				e.getWhoClicked().sendMessage("Click action: i:" + finalI + " ind:" + finalCurrentIndex);
			}));
		}
		currentIndex++;
		if(currentIndex >= map.get(0).length){
			currentIndex = 0;
		}
		cont.getData()[1] = currentIndex;

		setPageItems(cont.getContents());
	}
}
