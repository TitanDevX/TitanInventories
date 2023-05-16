package me.titan.titaninvs.test;

import me.titan.titaninvs.content.ItemBuilder;
import me.titan.titaninvs.core.TitanInvAPI;
import me.titan.titaninvs.examples.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitanInvTest extends JavaPlugin {
	@Override
	public void onEnable() {
		TitanInvAPI.init(this);
		getCommand("titaninv").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		String inv = args[0];
		if(inv.equalsIgnoreCase("usersGUI")){
			int amount = Integer.parseInt(args[1]);
			List<User> u = new ArrayList<>();
			for(int i =0;i<amount;i++){
				u.add(new User("User" + i,"lastName" + i));
			}

				UsersGUI.open((Player) s,u);
		}else if(inv.equalsIgnoreCase("updating")){
			int amount = Integer.parseInt(args[1]);
			Map<Integer, ItemStack[]> map = new HashMap<>();
			for(int i =0;i<amount;i++){
				ItemStack[] ar = new ItemStack[amount];
				for(int i2 =0;i2<amount;i2++){
					ar[i2] = new ItemStack(Material.values()[i+i2+1]);
				}
				map.put(i,ar);
			}
				ExampleUpdatingInv.open((Player) s,map);
		}else if(inv.equalsIgnoreCase("spin")){
			int amount = Integer.parseInt(args[1]);
			List<ItemStack> items = new ArrayList<>(amount);
			for(int i =0;i<amount;i++){
				items.add((int) i, ItemBuilder.create(Material.values()[i+1]).name("Item " + i).getItemStack());
			}

			new RewardWiningGUI(items).open((Player) s,0);
		}else if(inv.equalsIgnoreCase("chest")){
			new ChestInvExample().open((Player) s,0);
		}

		return true;

	}
}
