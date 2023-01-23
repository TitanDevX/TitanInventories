package me.titan.titaninvs.content;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * A tool that helps making items.
 *
 * @see #create(ItemStack) {@link #create(Material)}
 *
 */
public class ItemBuilder {

	ItemStack current;

	public ItemBuilder(ItemStack current) {
		this.current = current.clone();
	}

	public static ItemBuilder create(Material material){

		ItemStack item = new ItemStack(material);
		return new ItemBuilder(item);

	}
	public static ItemBuilder create(Material material, int amount){

		ItemStack item = new ItemStack(material,amount);
		return new ItemBuilder(item);

	}

	public static ItemBuilder create(Material material, short damage){

		ItemStack item = new ItemStack(material, 1, damage);
		return new ItemBuilder(item);

	}




	public static ItemBuilder create(Material material, int amount, short damage) {

		ItemStack item = new ItemStack(material, amount, damage);
		return new ItemBuilder(item);

	}

	public static ItemBuilder create(Material material, int amount, byte data) {

		ItemStack item = new ItemStack(material, amount, (short) 0, data);
		return new ItemBuilder(item);
	}
	public static ItemBuilder create(Material material, int amount, short damage, byte data){

		ItemStack item = new ItemStack(material, amount, damage,data);
		return new ItemBuilder(item);

	}

	public static ItemBuilder create(Material material, byte data) {

		ItemStack item = new ItemStack(material, 1, (short) 0, data);
		return new ItemBuilder(item);

	}

	public static ItemBuilder create(ItemStack item) {

		return new ItemBuilder(item);

	}

	public ItemBuilder color(DyeColor color) {
		data(color.getWoolData());

		return this;
	}

	public ItemBuilder enchants(Map<Enchantment, Integer> enchantMap){

		current.addUnsafeEnchantments(enchantMap);

		return this;

	}
	public ItemBuilder flag(ItemFlag flag){
		ItemMeta meta = getItemMeta();
		meta.addItemFlags(flag);
		itemMeta(meta);
		return this;

	}
	public ItemBuilder flags(List<ItemFlag> flags){
		ItemMeta meta = getItemMeta();
		for(ItemFlag f : flags){
			meta.addItemFlags(f);
		}
		itemMeta(meta);
		return this;

	}
	public ItemBuilder data(byte data) {
		current.setData(current.getType().getNewData(data));

		return this;
	}



	public ItemBuilder damage(short damage) {
		current.setDurability(damage);

		return this;
	}

	public ItemMeta getItemMeta(){
		ItemMeta m = current.getItemMeta();
		if(m == null){
			m = Bukkit.getItemFactory().getItemMeta(current.getType());
		}
		current.setItemMeta(m);
		return m;
	}
	public ItemBuilder itemMeta(ItemMeta meta){
		current.setItemMeta(meta);
		return this;
	}
	public ItemBuilder amount(int amount){
		current.setAmount(amount);
		return this;
	}
	public ItemBuilder color( Color color){
		if(!current.getType().name().contains("LEATHER_")) current.setType(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
		meta.setColor(color);
		current.setItemMeta(meta);
		return this;
	}
//	public ItemBuilder nbt(String key, String value){
//		this.current = Utils1_16.setNBT(key,value,current);
//		return this;
//	}
//	public ItemBuilder headURL(String url){
//		current.setType(Material.PLAYER_HEAD);
//		this.current.setItemMeta(Utils1_16.setSkullURL(getItemMeta(),url));
//		return this;
//	}

	public ItemBuilder name(String name){
		ItemMeta m = getItemMeta();
		m.setDisplayName(colorize(name));
		return itemMeta(m);
	}
	public ItemBuilder lores(String... lores){
		ItemMeta m = getItemMeta();
		m.setLore(colorize(Arrays.asList(lores)));
		return itemMeta(m);
	}
	public static String colorize(String msg){
		if(msg == null) return "";
		return ChatColor.translateAlternateColorCodes('&',msg);
	}
	public static List<String> colorize(List<String> msgs){
		if(msgs == null) return null;
		List<String> list = new ArrayList<>();
		for(String msg : msgs){
			list.add(colorize(msg));
		}


		return list;
	}
	/*
	Adds
	 */
	public ItemBuilder lore(String... lores){
		ItemMeta m = getItemMeta();
		List<String> lore = new ArrayList<>(m.getLore() != null ? m.getLore() : new ArrayList<>());
				lore.addAll(Arrays.asList(lores));
		m.setLore(colorize(lore));
		return itemMeta(m);
	}
	/*
	Adds
	 */
	public ItemBuilder lore(List<String> lores){
		ItemMeta m = getItemMeta();
		List<String> lore = new ArrayList<>(m.getLore() != null ? m.getLore() : new ArrayList<>());
		lore.addAll(lores);
		m.setLore(colorize(lore));
		return itemMeta(m);
	}
	public ItemBuilder setLore(List<String> lores){
		ItemMeta m = getItemMeta();
		m.setLore(colorize(lores));
		return itemMeta(m);
	}
	public ItemStack getItemStack(){
		return current.clone();
	}

}
