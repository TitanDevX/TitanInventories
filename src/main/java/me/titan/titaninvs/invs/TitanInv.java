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
	 *
	 * This is used for all types of inventories, paginated or not.
	 *
	 * @param p player
	 * @param data data you want to pass to the init function.
	 */
	public InventoryContents open(Player p, int page, Object[] data){

		InventoryContents con = new InventoryContents(title,size, id, page);
		if(shouldCacheInventory() && inventory != null){
			p.openInventory(inventory);
			return con;
		}
		init(p,con,data);

		if(pagination != null){
			con.putAll( pagination.getPage(page,size));
			if(nextPageButton != null && pagination.hasNext(page)){
				con.put(nextPageButtonSlot,nextPageButton);
			}
			if(previousPageButton != null && pagination.hasPrevious(page)){
				con.put(previousPageButtonSlot,previousPageButton);
			}
		}

		Inventory inv = Bukkit.createInventory(con,size, ChatColor.translateAlternateColorCodes('&',con.getTitle()));


		for(Map.Entry<Integer, ClickableItem> en : con.entrySet()){
			inv.setItem(en.getKey(),en.getValue().getItem());
		}
		con.setInventory(inv);
		this.inventory = inv;
		p.openInventory(inv);
		return con;
	}
	/**
	 *
	 * Opens the inventory for player, calling the {@link #init(Player, InventoryContents, Object[])} function.
	 *
	 * @param p player
	 * @param data data you want to pass to the init function.
	 *
	*/
	public InventoryContents open(Player p, Object[] data){
		return open(p,0,data);
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
	 * @deprecated to be removed, call {@link #open(Player, int, Object[])} instead.
	 */
	@Deprecated
	public void openPaged(Player p, int page, Object[] data){

		if(pagination == null) {
			throw new IllegalCallerException("Cannot open page with no-pagination GUI!");
		}
		open(p,page,data);
	}
	/**
	 *
	 * Updates the inventory with specific a {@link InventoryContents}
	 *
	 * <p></p>Note: this does not call any initialize function.
	 *
	 * @param p
	 * @param con
	 */
	public void update(Player p, InventoryContents con){
		Inventory inv = con.getInventory();
		if(con.isTitleChanged()) {
			inv = Bukkit.createInventory(con, size,
					ChatColor.translateAlternateColorCodes('&', con.getTitle()));
		}else{
			inv.clear();
		}

		for (Map.Entry<Integer, ClickableItem> en : con.entrySet()) {
			inv.setItem(en.getKey(), en.getValue().getItem());
		}
		con.setInventory(inv);
		if(con.isTitleChanged()){
			p.openInventory(inv);
			con.resetTitleChanged();
		}

	}

	/**
	 *
	 * Sets the pagination of the inventory
	 *<p></p>
	 * Note: this function itself doesn't update the inventory,
	 * it's recommend that you call it in {@link #init}
	 * functions, otherwise you need to follow it with {@link #openPaged} function.
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
	 * Open a page, assuming the inventory already is initialized.
	 *
	 * @param p
	 * @param page
	 */
	protected InventoryContents openPage(Player p, InventoryContents con, int page){


		if(pagination == null){
			throw new IllegalCallerException("Cannot open page with no-pagination GUI!");
		}
		con.clear();
		con.setPage(page);
		//InventoryContents con = new InventoryContents(title,size, this.id, page);

		Inventory inv = Bukkit.createInventory(con,size, ChatColor.translateAlternateColorCodes('&',con.getTitle()));
		con.setInventory(inv);
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
		return con;
	}

	/**
	 *
	 * Refreshes the inventory automatically calling the initialize function.
	 *
	 * <p>Has support for pagination, it refreshes the current page.
	 *
	 * @param p
	 * @param con
	 * @param data
	 */
	public void refresh(Player p, InventoryContents con,Object[] data){

		con.clear();
		init(p,con,data);
		if(pagination != null){
			con.putAll( pagination.getPage(con.getPage(),size));
			if(nextPageButton != null && pagination.hasNext(con.getPage())){
				con.put(nextPageButtonSlot,nextPageButton);
			}
			if(previousPageButton != null && pagination.hasPrevious(con.getPage())){
				con.put(previousPageButtonSlot,previousPageButton);
			}
		}

		update(p,con);
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
	 */
	public void setNextPageButton(int slot, ClickableItem item, InventoryContents con){

		nextPageButtonSlot = slot;
		(nextPageButton = item).setConsumer((e) -> {
			openPage((Player) e.getWhoClicked(),con,con.getPage()+1);
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
	 */
	public void setPreviousPageButton(int slot, ClickableItem item, InventoryContents con){
		previousPageButtonSlot = slot;
		(previousPageButton = item).setConsumer((e) -> {
			openPage((Player) e.getWhoClicked(),con,con.getPage()-1);
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

	public Pagination getPagination() {
		return pagination;
	}
}
