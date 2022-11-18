package me.titan.titaninvs.examples;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.InventoryContents;
import me.titan.titaninvs.content.ItemBuilder;
import me.titan.titaninvs.content.Pagination;
import me.titan.titaninvs.invs.TitanInv;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * Example paginated GUI, accepts a list of users
 * and displays only 7 users per page with a next
 * & previous pages buttons.
 *
 * The isBoxed parameter in #Pagination makes it
 * only skip borders in the inventory and display
 * them in a nice clean way.
 *
 */
public class UsersGUI extends TitanInv {

	private static TitanInv instance;

	public UsersGUI() {
		super("Test", 27);
	}
	public static void open(Player p, Collection<User> users){
		if(instance == null){
			(instance = new UsersGUI()).openPage(p,0, new Object[]{users});
		}else{
			instance.openPage(p,0, new Object[]{users});
		}
	}

	@Override
	public void init(Player p, InventoryContents con, Object[] data) {
		if(data.length == 0) return;
		Collection<User> users = (Collection<User>) data[0];
		List<ClickableItem> items = new ArrayList<>();
		for(User u : users){
			items.add(ClickableItem.of(ItemBuilder.create(Material.GOLD_BLOCK).name("&a" + u.getName()).lore("","&aLast name: " + u.getLastName()) , (e) -> {
				e.getWhoClicked().sendMessage("This is an action u clicked user " + u.getName());
			}));
		}
		pagination(new Pagination(7,true).setItems(items));
		setNextPageButton(26,ClickableItem.empty(ItemBuilder.create(Material.ARROW).name("&cNext page").getItemStack()), data);
		setPreviousPageButtonPageButton(18,ClickableItem.empty(ItemBuilder.create(Material.ARROW).name("&cPrevious page").getItemStack()), data);
	}

	@Override
	protected boolean shouldCacheInventory() {
		return false;
	}
}
