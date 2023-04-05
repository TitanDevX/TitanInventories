package me.titan.titaninvs.content;



import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 *
 * Represent a clickable item
 *
 * @see #of(ItemStack, Consumer)
 *
 */
public class ClickableItem {

	ItemStack item;
	Consumer<InventoryClickEvent> consumer;

	private ClickableItem(ItemStack item, Consumer<InventoryClickEvent> consumer) {
		this.item = item;
		this.consumer = consumer;
	}

	/**
	 * Creates a @{@link ClickableItem} without a @{@link InventoryClickEvent} consumer.
	 * Check @{@link ItemBuilder} to create items with ease.
	 *
	 * @param item
	 * @return
	 */
	public static ClickableItem empty(ItemStack item){
		return new ClickableItem(item,null);
	}

	/**
	 * Creates a @{@link ClickableItem} with a @{@link InventoryClickEvent} consumer.
	 *
	 * Check @{@link ItemBuilder} to create items with ease.
	 *
	 * @param item
	 * @return
	 */
	public static ClickableItem of(ItemStack item,Consumer<InventoryClickEvent> consumer ){
		return new ClickableItem(item,consumer);
	}

	/**
	 * Creates a @{@link ClickableItem} with a @{@link InventoryClickEvent} consumer.
	 *
	 * Check @{@link ItemBuilder} to create items with ease.
	 *
	 * @param item
	 * @return
	 */
	public static ClickableItem of(ItemBuilder item, Consumer<InventoryClickEvent> consumer ){
		return new ClickableItem(item.getItemStack(),consumer);
	}
	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public Consumer<InventoryClickEvent> getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer<InventoryClickEvent> consumer) {
		this.consumer = consumer;
	}
}
