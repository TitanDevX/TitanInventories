package me.titan.titaninvs.invs;

import me.titan.titaninvs.content.InventoryContents;
import me.titan.titaninvs.content.InventoryContext;
import me.titan.titaninvs.core.TitanInvAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * A GUI that auto updates every {@link #delay}
 *
 */
public abstract class UpdatingInv extends TitanInv {


	private long delay;

	BukkitRunnable task;

	boolean updating;


	Map<String, InventoryContext> contextMap = new HashMap<>();


	public UpdatingInv(String title, int size, long delay) {
		super(title, size);
		this.delay = delay;


	}





	/**
	 *
	 * Updates items in the given {@link InventoryContents}
	 *
	 * @param player player
	 * @param inventoryContext context to be updates.
	 */
	public abstract void updateItems(Player player, InventoryContext inventoryContext);



	public boolean closeable(){
		return true;
	}

	@Override
	public InventoryContents open(Player p, int page, Object[] data) {
		InventoryContents inv = super.open(p,page, data);
		InventoryContext cont = new InventoryContext(inv,data);
		contextMap.put(p.getName(),cont);
		if(task == null){
			runTask();
		}
		return inv;
	}

	@Override
	protected InventoryContents openPage(Player p, InventoryContents con, int page) {
		super.openPage(p,con, page);
		contextMap.get(p.getName()).setContents(con);
		return con;
	}

	private void runTask(){
		(task = new BukkitRunnable() {
			@Override
			public void run() {
				Iterator<Map.Entry<String, InventoryContext>> it = contextMap.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String, InventoryContext> en = it.next();
					Player p = Bukkit.getPlayer(en.getKey());
					if(p != null && p.isOnline() && p.getOpenInventory().getTopInventory().getHolder() != null && p.getOpenInventory().getTopInventory().getHolder().equals(en.getValue().getContents())){
						update(p, en.getValue());
					}else{
						it.remove();
					}
				}
				if(contextMap.isEmpty()){
					task.cancel();
					task = null;
				}

			}
		}).runTaskTimer(TitanInvAPI.getPlugin(),delay,delay);
	}

	@Override
	public void onClose(InventoryCloseEvent e) {

		if(contextMap.containsKey(e.getPlayer().getName())){
			if(getPagination() != null ){
				return;
			}
			contextMap.remove(e.getPlayer().getName());
			if(contextMap.isEmpty() && task != null){
				task.cancel();
				task = null;
			}
		}

	}

	protected void setPageItems(InventoryContents con){

		if(pagination == null) return;
		con.putAll( pagination.getPage(con.getPage(),size));
		if(nextPageButton != null && pagination.hasNext(con.getPage())){
			con.put(nextPageButtonSlot,nextPageButton);
		}
		if(previousPageButton != null && pagination.hasPrevious(con.getPage())){
			con.put(previousPageButtonSlot,previousPageButton);
		}

	}
	/**
	 *
	 * Updates the inventory contents and displays it.
	 *
	 * <p>
	 *     Note: this is automatically called normally.
	 * </p>
	 *
	 * @param p
	 */
	private void update(Player p, InventoryContext con) {
		updateItems(p,con);
		super.update(p, con.getContents());
	}

	/**
	 * Return a modifiable context for the given player
	 * A context holds modifiable inventory contents and data.
	 * However {@link #TitanInv#update(Player, InventoryContents)}
	 * has to be called for changes to inventory content to apply.
	 *
	 * @param p
	 * @return
	 */
	public InventoryContext getContext(Player p) {
		return contextMap.get(p.getName());
	}
}
