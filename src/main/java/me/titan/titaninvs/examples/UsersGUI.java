package me.titan.titaninvs.examples;


import me.titan.titaninvs.content.ClickableItem;
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


	Collection<User> users;
	public UsersGUI(Collection<User> users) {
		super("Test", 27);
		this.users = users;
	}
	public static void open(Player p, Collection<User> users){
		new UsersGUI(users).open(p,0);
	}


	@Override
	public void init(Player p) {
		List<ClickableItem> items = new ArrayList<>();

		for(User u : users){
			items.add(ClickableItem.of(ItemBuilder.create(Material.GOLD_BLOCK).name("&a" + u.getName()).lore("","&aLast name: " + u.getLastName()) , (e) -> {
				e.getWhoClicked().sendMessage("This is an action u clicked user " + u.getName());
			}));
		}
		pagination(new Pagination(7,true).setItems(items));
		setNextPageButton(26,ClickableItem.empty(ItemBuilder.create(Material.ARROW).name("&cNext page").getItemStack()));
	 	setPreviousPageButton(18,ClickableItem.empty(ItemBuilder.create(Material.ARROW).name("&cPrevious page").getItemStack()));


//		Bukkit.getScheduler().runTaskLater(TitanInvAPI.getPlugin(),() -> {
//			List<User> u = new ArrayList<>();
//			for(int i =0;i<users.size();i++){
//				u.add(new User("2_User" + i,"2_lastName" + i));
//			}
//			data[0] = u;
//			refresh(p,con,data);
//		},20 * 5);
	}

	@Override
	public boolean isCached() {
		return false;
	}
}
