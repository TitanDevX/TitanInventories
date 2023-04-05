package me.titan.titaninvs.examples;


import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.ItemBuilder;
import me.titan.titaninvs.content.Pagination;
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


	//int currentIndex = 0;
	Map<Integer, ItemStack[]> map;
	int currentIndex = 0;
	public ExampleUpdatingInv( Map<Integer, ItemStack[]> map) {
		super("Test", 27,20);
		this.map = map;
	}
	public static void open(Player p, Map<Integer, ItemStack[]> map){
		new ExampleUpdatingInv(map).open(p,0);
	}


	@Override
	public void init(Player p) {

		Pagination page = new Pagination(9,false);
		for(int i =0;i<map.size();i++){
			int finalI = i;

			page.getItems().add(ClickableItem.of(map.get(i)[currentIndex], (e) -> e.getWhoClicked().sendMessage("Click action: i:" + finalI + " ind:" + currentIndex + ", material: " + map.get(finalI)[currentIndex])));
		}
		pagination(page);
		setNextPageButton(26,ClickableItem.empty(ItemBuilder.create(Material.ARROW).name("&cNext page").getItemStack()));
		setPreviousPageButton(18,ClickableItem.empty(ItemBuilder.create(Material.ARROW).name("&cPrevious page").getItemStack()));
	}

	@Override
	public void updateItems(Player p) {


		setTitle("Page " + getCurrentPage());
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
			page.getItems().set(i, ClickableItem.of(map.get(i)[currentIndex], (e) -> e.getWhoClicked().sendMessage("Click action: i:" + finalI + " ind:" + finalCurrentIndex)));
		}
		currentIndex++;
		if(currentIndex >= map.get(0).length){
			currentIndex = 0;
		}

		setPageItems();
	}
}
