package me.titan.titaninvs.invs;

import me.titan.titaninvs.content.InventoryContents;
import me.titan.titaninvs.core.TitanInvAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * A GUI that auto updates every {@link #delay}
 *
 */
public abstract class UpdatingInv extends TitanInv {

	private final Set<Player> viewers = new HashSet<>();

	private long delay;

	BukkitRunnable task;

	boolean updating;

	public UpdatingInv(String title, int size, long delay) {
		super(title, size);
		this.delay = delay;

		(task = new BukkitRunnable() {
			@Override
			public void run() {
				Iterator<Player> it = viewers.iterator();
				while(it.hasNext()){
					Player p = it.next();
					if(p.isOnline()){
						update(p);
					}else{
						it.remove();
					}
				}
			}
		}).runTaskTimer(TitanInvAPI.getPlugin(),delay,delay);
	}

	/**
	 *
	 * Updates items in the given {@link InventoryContents}
	 *
	 * @param p player
	 * @param con contens to be updated.
	 */
	public abstract void updateItems(Player p, InventoryContents con);

	@Override
	public void onOpen(InventoryOpenEvent e) {

		getViewers().add((Player) e.getPlayer());
		if(task.isCancelled()){
			task.runTaskTimer(TitanInvAPI.getPlugin(),delay,delay);
		}

	}

	@Override
	public void onClose(InventoryCloseEvent e) {
		if(closeable()){
			getViewers().remove((Player) e.getPlayer());
			if(getViewers().isEmpty()){
				task.cancel();
			}
		}else{
			e.getPlayer().openInventory(e.getInventory());
		}


	}

	public boolean closeable(){
		return true;
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
	public void update(Player p) {
		InventoryContents con = new InventoryContents(getSize(),getId());
		updateItems(p,con);
		super.update(p, con);
	}

	/**
	 *
	 *
	 * @return current viewers of the inventory.
	 */
	public Set<Player> getViewers() {
		return viewers;
	}
}
