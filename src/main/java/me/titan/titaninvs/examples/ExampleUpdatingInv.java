package me.titan.titaninvs.examples;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.InventoryContents;
import me.titan.titaninvs.invs.UpdatingInv;
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

	Map<Integer, ItemStack[]> map;
	int currentIndex = 0;

	public ExampleUpdatingInv() {
		super("Test", 9,20 * 5);
	}
	public static void open(Player p, Map<Integer, ItemStack[]> map){
		if(instance == null){
			(instance = new ExampleUpdatingInv()).open(p,new Object[]{map});
		}else{
			instance.open(p, new Object[]{map});
		}
	}


	@Override
	public void init(Player p, InventoryContents con, Object[] data) {
		map = (Map<Integer, ItemStack[]>) data[0];

		for(int i =0;i<9;i++){
			int finalI = i;
			con.set(i, ClickableItem.of(map.get(i)[currentIndex], (e) -> {
				e.getWhoClicked().sendMessage("Click action: i:" + finalI + " ind:" + currentIndex);
			}));
		}

	}

	@Override
	public void updateItems(Player p, InventoryContents con, Object[] data) {

		if(map == null) return;
		if(currentIndex > 4){
			currentIndex = 0;
		}
		for(int i =0;i<9;i++){
			int finalI = i;
			con.set(i, ClickableItem.of(map.get(i)[currentIndex], (e) -> {
				e.getWhoClicked().sendMessage("Click action: i:" + finalI + " ind:" + currentIndex);
			}));
		}
		currentIndex++;
		if(currentIndex > 4){
			currentIndex = 0;
		}
	}
}
