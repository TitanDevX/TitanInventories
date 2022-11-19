package me.titan.titaninvs.invs;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.InventoryContents;
import me.titan.titaninvs.content.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Super class for all inventories,
 * manages inventory creations, title, size, pagination and content.
 *
 * It's recommend that you cache a single instance of this class as in {@link me.titan.titaninvs.examples.UsersGUI}
 *
 */
public abstract class TitanInv{

	int id =0;
	Inventory inventory;

	final String title;
	final int size;

	int currentPage = 0;
	Pagination pagination;

	private static Map<Integer, TitanInv> inventories = new HashMap<>();


	public TitanInv(String title, int size) {
		this.title = title;
		this.size = size;
		id = inventories.size();
		inventories.put(id,this);
	}
	public boolean isProtected(){
		return true;
	}

	/**
	 *
	 * Initialize content of the inventory, and possible {@link #pagination}.
	 *
	 * @param p player
	 * @param con content to be initialized.
	 * @param data data given from open function.
	 */
	public abstract void init(Player p, InventoryContents con, Object[] data);

	/**
	 *
	 * Opens the inventory for player, calling the {@link #init(Player, InventoryContents, Object[])} function.
	 *
	 * <p></p>
	 * Note: for paginated GUI use {@link #openPage(Player, int, Object[])}
	 *
	 * @param p player
	 * @param data data you want to pass to the init function.
	 */
	public void open(Player p, Object[] data){

		InventoryContents con = new InventoryContents(title,size, id);
		if(shouldCacheInventory() && inventory != null){
			p.openInventory(inventory);
			return;
		}
		init(p,con,data);

		Inventory inv = Bukkit.createInventory(con,size, ChatColor.translateAlternateColorCodes('&',con.getTitle()));

		con.setInventory(inv);
		for(Map.Entry<Integer, ClickableItem> en : con.entrySet()){
			inv.setItem(en.getKey(),en.getValue().getItem());
		}
		this.inventory = inv;
		p.openInventory(inv);
	}

	/**
	 *
	 * Updates the inventory with specific a {@link InventoryContents}
	 *
	 * @param p
	 * @param con
	 */
	public void update(Player p, InventoryContents con){
		Inventory inv = Bukkit.createInventory(con,size
				, ChatColor.translateAlternateColorCodes('&',con.getTitle()));

		for (Map.Entry<Integer, ClickableItem> en : con.entrySet()) {
			inv.setItem(en.getKey(), en.getValue().getItem());
		}
		p.openInventory(inv);
	}

	/**
	 *
	 * Sets the pagination of the inventory
	 *<p></p>
	 * Note: this function itself doesn't update the inventory,
	 * it's recommend that you call it in {@link #init}
	 * functions, otherwise you need to follow it with {@link #openPage} function.
	 *
	 * @param pagination
	 */
	public void pagination(Pagination pagination){
		this.pagination = pagination;
	}
	public static TitanInv getById(int id){
		return inventories.get(id);
	}

	/**
	 *
	 * Open a specific page
	 * <p>
	 *     Note: {@link #pagination} must be set for this to work, use {@link #pagination(Pagination)} function.
	 * </p>
	 *
	 * @param p player
	 * @param page page (starting from 0)
	 * @param data data to be passed to the {@link #init} function.
	 *
	 * @throws IllegalCallerException If this method is called while {@link #pagination} is not set.
	 */
	public void openPage(Player p, int page, Object[] data){
		if(pagination == null){
			throw new IllegalCallerException("Cannot open page with no-pagination GUI!");
		}

		InventoryContents con = new InventoryContents(title,size, this.id);
		Inventory inv = Bukkit.createInventory(con,size, ChatColor.translateAlternateColorCodes('&',con.getTitle()));
		con.setInventory(inv);
		currentPage = page;
		init(p,con,data);
		con.putAll( pagination.getPage(page,size));
		if(nextPageButton != null && pagination.hasNext(page)){
			con.put(nextPageButtonSlot,nextPageButton);
		}
		if(previousPageButton != null && pagination.hasPrevious(page)){
			con.put(previousPageButtonSlot,previousPageButton);
		}
		
		for(Map.Entry<Integer, ClickableItem> en : con.entrySet()){
			inv.setItem(en.getKey(),en.getValue().getItem());
		}
		p.openInventory(inv);
	}

	int nextPageButtonSlot;
	ClickableItem nextPageButton;
	int previousPageButtonSlot;
	ClickableItem previousPageButton;

	/**
	 *
	 * Automatically places next page button with given slot and item,
	 *
	 * <p>
	 *     Note: the given ClickableItem's consumer will be overridden.
	 * </p>
	 *
	 * @param slot slot for the item to be placed in.
	 * @param item item, the consumer of it will be overridden.
	 * @param data data to be passed for {@link #openPage} function.
	 */
	public void setNextPageButton(int slot, ClickableItem item, Object[] data){

		nextPageButtonSlot = slot;
		(nextPageButton = item).setConsumer((e) -> {
			openPage((Player) e.getWhoClicked(),currentPage+1, data);
		});
	}
	/**
	 *
	 * Automatically places next previous button with given slot and item,
	 *
	 * <p>
	 *     Note: the given ClickableItem's consumer will be overridden.
	 * </p>
	 *
	 * @param slot slot for the item to be placed in.
	 * @param item item, the consumer of it will be overridden.
	 * @param data data to be passed for {@link #openPage} function.
	 */
	public void setPreviousPageButton(int slot, ClickableItem item, Object[] data){
		previousPageButtonSlot = slot;
		(previousPageButton = item).setConsumer((e) -> {
			openPage((Player) e.getWhoClicked(),currentPage-1,data);
		});
	}

	public String getTitle() {
		return title;
	}

	/**
	 *
	 * If true an instance of {@link Inventory} will be cached on first open,
	 * and used on next time {@link #open} method gets called.
	 *
	 * @return if an instance of {@link Inventory} should be cached.
	 */
	protected boolean shouldCacheInventory(){
		return false;
	}

	/**
	 *
	 * Sets the cached inventory
	 *
	 * @param inventory new cached inventory, can be null.
	 */
	public void setCachedInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	/**
	 *
	 * Called on inventory open.
	 *
	 * @param e
	 */
	public void onOpen(InventoryOpenEvent e){

	}

	/**
	 *
	 * Called on inventory close.
	 *
	 * @param e
	 */
	public void onClose(InventoryCloseEvent e){

	}

	public int getSize() {
		return size;
	}

	/**
	 *
	 *
	 * Return the id of the TitanInv.
	 * @return
	 */
	public int getId() {
		return id;
	}
}
