package me.titan.titaninvs.content.two;



import me.titan.titaninvs.content.ClickableItem;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;


public class ClickableItem2 extends ClickableItem {



	String tag;
	Consumer<ClickEvent> contextConsumer;
	ItemStack item;


	public ClickableItem2(ItemStack item, Consumer<ClickEvent> contextConsumer) {
		super(item , null);
		this.item = item;
		this.contextConsumer = contextConsumer;
	}



	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public Consumer<ClickEvent> getV2Consumer() {
		return contextConsumer;
	}
}
