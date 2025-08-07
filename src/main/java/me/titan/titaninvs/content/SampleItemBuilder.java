package me.titan.titaninvs.content;

import me.titan.titaninvs.content.itemglow.ItemGlowManager;
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
import java.util.function.Function;

/**
 *
 * A tool that helps making items.
 *
 * @see #create(ItemStack) {@link #create(Material)}
 *
 */
public class SampleItemBuilder {

	ItemStack current;
	private static Function<String, String> colorizer;

	public SampleItemBuilder(ItemStack current) {
		this.current = current.clone();
	}

	public static SampleItemBuilder create(Material material){

		ItemStack item = new ItemStack(material);
		return new SampleItemBuilder(item);

	}
	public static SampleItemBuilder create(Material material, int amount){

		ItemStack item = new ItemStack(material,amount);
		return new SampleItemBuilder(item);

	}

	public static SampleItemBuilder create(Material material, short damage){

		ItemStack item = new ItemStack(material, 1, damage);
		return new SampleItemBuilder(item);

	}




	public static SampleItemBuilder create(Material material, int amount, short damage) {

		ItemStack item = new ItemStack(material, amount, damage);
		return new SampleItemBuilder(item);

	}

	public static SampleItemBuilder create(Material material, int amount, byte data) {

		ItemStack item = new ItemStack(material, amount, (short) 0, data);
		return new SampleItemBuilder(item);
	}
	public static SampleItemBuilder create(Material material, int amount, short damage, byte data){

		ItemStack item = new ItemStack(material, amount, damage,data);
		return new SampleItemBuilder(item);

	}

	public static SampleItemBuilder create(Material material, byte data) {

		ItemStack item = new ItemStack(material, 1, (short) 0, data);
		return new SampleItemBuilder(item);

	}

	public static SampleItemBuilder create(ItemStack item) {

		return new SampleItemBuilder(item);

	}

	public SampleItemBuilder color(DyeColor color) {
		data(color.getWoolData());

		return this;
	}

	public SampleItemBuilder enchants(Map<Enchantment, Integer> enchantMap){

		current.addUnsafeEnchantments(enchantMap);

		return this;

	}
	public SampleItemBuilder enchant(Enchantment ench, int level){

		current.addUnsafeEnchantment(ench,level);

		return this;

	}
	public SampleItemBuilder flag(ItemFlag flag){
		ItemMeta meta = getItemMeta();
		meta.addItemFlags(flag);
		itemMeta(meta);
		return this;

	}
	public SampleItemBuilder flags(List<ItemFlag> flags){
		ItemMeta meta = getItemMeta();
		for(ItemFlag f : flags){
			meta.addItemFlags(f);
		}
		itemMeta(meta);
		return this;

	}
	public SampleItemBuilder data(byte data) {
		current.setData(current.getType().getNewData(data));

		return this;
	}



	public SampleItemBuilder damage(short damage) {
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
	public SampleItemBuilder itemMeta(ItemMeta meta){
		current.setItemMeta(meta);
		return this;
	}
	public SampleItemBuilder amount(int amount){
		current.setAmount(amount);
		return this;
	}
	public SampleItemBuilder color(Color color){
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

	public SampleItemBuilder name(String name){
		ItemMeta m = getItemMeta();
		m.setDisplayName(colorize(name));
		return itemMeta(m);
	}
	public SampleItemBuilder lores(String... lores){
		ItemMeta m = getItemMeta();
		m.setLore(colorize(Arrays.asList(lores)));
		return itemMeta(m);
	}
	public static void colorizer(Function<String, String> col){
		colorizer = col;
	}
	public static String colorize(String msg){
		if(msg == null) return "";
		if(colorizer != null){
			return colorizer.apply(msg);
		}
		return ChatColor.translateAlternateColorCodes('&',msg);
	}
	public boolean isGlow;
	public SampleItemBuilder glow(){
		return glow(true);
	}
	public SampleItemBuilder glow(boolean flag){
		if(flag){
			isGlow = true;
			current = ItemGlowManager.glow(current);
		}else{
			isGlow = false;
			current = ItemGlowManager.unglow(current);
		}

		return this;
	}
	public SampleItemBuilder customModelData(int data){
		ItemMeta meta = getItemMeta();
		try {
			meta.setCustomModelData(data);
		}catch (NoSuchMethodError ignored){}
		current.setItemMeta(meta);
		return this;
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
	public SampleItemBuilder lore(String... lores){
		ItemMeta m = getItemMeta();
		List<String> lore = new ArrayList<>(m.getLore() != null ? m.getLore() : new ArrayList<>());
				lore.addAll(Arrays.asList(lores));
		m.setLore(colorize(lore));
		return itemMeta(m);
	}
	/*
	Adds
	 */
	public SampleItemBuilder lore(List<String> lores){
		ItemMeta m = getItemMeta();
		List<String> lore = new ArrayList<>(m.getLore() != null ? m.getLore() : new ArrayList<>());
		lore.addAll(lores);
		m.setLore(colorize(lore));
		return itemMeta(m);
	}
	public SampleItemBuilder setLore(List<String> lores){
		ItemMeta m = getItemMeta();
		m.setLore(colorize(lores));
		return itemMeta(m);
	}
	public ItemStack getItemStack(){
		return current.clone();
	}

	public ItemStack readOnlyItem(){
		return current;
	}
}
