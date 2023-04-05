package me.titan.titaninvs.invs;

import me.titan.titaninvs.core.TitanInvAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 *
 * A GUI that auto updates every {@link #globalDelay}
 *
 */
public abstract class UpdatingInv extends TitanInv {



	private static BukkitRunnable globalTask;

	private final static Map<String, UpdatingInv> updatingInvs = new HashMap<>();
	private static long globalDelay = 20;

	private long delay;
	private BukkitRunnable task;

	private final List<String> viewers = new ArrayList<>();
	public UpdatingInv(String title, int size) {
		super(title, size);
	}
	public UpdatingInv(String title, int size, long delay) {
		super(title, size);
		this.delay = delay;
	}
	public static void setStaticDelay(long de){
		globalDelay = de;
	}
	public boolean isStaticDelay(){
		return true;
	}
	/**
	 *
	 *
	 * @param player player
	 */
	public abstract void updateItems(Player player);



	public boolean closeable(){
		return true;
	}

	@Override
	public Inventory open(Player p, int page) {
		Inventory inv  = super.open(p,page);
		//InventoryContext cont = new InventoryContext(inv,data);
		if(!isStaticDelay()){
			runTask();
			return inv;
		}
		updatingInvs.put(p.getName(),this);
		if(globalTask == null){
			runGlobalTask();
		}
		return inv;
	}



	private void runTask(){
		(task = new BukkitRunnable() {
			@Override
			public void run() {
				if(viewers.isEmpty()){
					cancel();
					task = null;
					return;
				}

				for (Iterator<String> it = viewers.iterator(); it.hasNext(); ) {
					String v =  it.next();
					Player p = Bukkit.getPlayer(v);
					if(p != null && p.isOnline() && p.getOpenInventory().getTopInventory().getHolder() != null && p.getOpenInventory().getTopInventory().getHolder().equals(UpdatingInv.this)){
						tick(p);
					}else{
						it.remove();
					}
				}

			}
		}).runTaskTimer(TitanInvAPI.getPlugin(), delay,delay);
	}
	private static void runGlobalTask(){
		(globalTask = new BukkitRunnable() {
			@Override
			public void run() {
				Iterator<Map.Entry<String, UpdatingInv>> it = updatingInvs.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String, UpdatingInv> en = it.next();
					Player p = Bukkit.getPlayer(en.getKey());
					if(p != null && p.isOnline() && p.getOpenInventory().getTopInventory().getHolder() != null && p.getOpenInventory().getTopInventory().getHolder().equals(en.getValue())){
						en.getValue().tick(p);
					}else{
						it.remove();
					}
				}
				if(updatingInvs.isEmpty()){
					globalTask.cancel();
					globalTask = null;
				}

			}
		}).runTaskTimer(TitanInvAPI.getPlugin(),globalDelay,globalDelay);
	}

	@Override
	public void onOpen(InventoryOpenEvent e) {
		viewers.add(e.getPlayer().getName());
	}

	@Override
	public void onClose(InventoryCloseEvent e) {
		if(!closeable() && !closed){
			e.getPlayer().openInventory(getInventory());
			return;
		}
		if(updatingInvs.containsKey(e.getPlayer().getName())){
			if(pagination != null ){
				return;
			}
			updatingInvs.remove(e.getPlayer().getName());
			if(updatingInvs.isEmpty() && globalTask != null){
				globalTask.cancel();
				globalTask = null;
			}
		}
		viewers.remove(e.getPlayer().getName());

	}

	protected void setPageItems(){

		if(pagination == null) return;
		putAll( pagination.getPage(getCurrentPage(),size));
		if(nextPageButton != null && pagination.hasNext(getCurrentPage())){
			put(nextPageButtonSlot,nextPageButton);
		}
		if(previousPageButton != null && pagination.hasPrevious(getCurrentPage())){
			put(previousPageButtonSlot,previousPageButton);
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
	 * @param p the player
	 */
	private void tick(Player p) {

		updateItems(p);
		super.update(p);
	}

	boolean closed = false;

	/**
	 * Closes the inventory for this player ,use this method for none-closeable guis.
	 * @param p the player
	 */
	public void close(Player p){
		closed = true;
		p.closeInventory();
	}
	public void stop(){
		if(task != null){
			task.cancel();
		}
	}
}
