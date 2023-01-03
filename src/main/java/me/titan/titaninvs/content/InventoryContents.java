package me.titan.titaninvs.content;

import me.titan.titaninvs.invs.TitanInv;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

/**
 * Inventory content holder and manager.
 */
public class InventoryContents extends HashMap<Integer, ClickableItem> implements org.bukkit.inventory.InventoryHolder {
	Inventory inventory;

	String title;
	final int size;
	final int titanInv;
	int page = 0;
	private boolean titleChanged;


	public InventoryContents(String title, int size, int titanInv) {
		this.title = title;
		this.size = size;
		this.titanInv = titanInv;
	}
	public InventoryContents(String title, int size, int titanInv, int page) {
		this.title = title;
		this.size = size;
		this.titanInv = titanInv;
		this.page = page;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	public void set(int slot, ClickableItem item){
		put(slot,item);
	}
	public ClickableItem get(int slot){
		return ((HashMap<Integer, ClickableItem>) this).get(slot);
	}

	/**
	 * Fills content with the item.
	 * @param item
	 */
	public void fill(ClickableItem item){
		for(int i =0;i<size;i++){
			put(i,item);
		}
	}

	/**
	 * Fill a specific column
	 * @param column
	 * @param item
	 */
	public void fillColumn(int column, ClickableItem item){

		// 54 - (9-1)

		for(int i = column;i<=size-(9-(column));i+=9){

			put(i,item);

		}

	}
	public void fillRow(int row, ClickableItem item){

		// 54 - (9-1)

		for(int i = row*9;i<(row*9)+9;i++){
			put(i,item);
		}

	}
	public void fillBorders(ClickableItem item){
		fillRow(0,item);
		fillColumn(0,item);
		fillRow((size/9)-1,item);
		fillColumn(8,item);
	}

	public TitanInv getTitanInv() {
		return TitanInv.getById(titanInv);
	}

	public void setTitle(String title) {
		if(!title.equals(this.title)){
			titleChanged = true;
		}
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public boolean isTitleChanged() {
		return titleChanged;
	}

	public void resetTitleChanged() {
		this.titleChanged = false;
	}
}
