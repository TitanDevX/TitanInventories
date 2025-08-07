package me.titan.titaninvs.content.two;

import me.titan.titaninvs.invs.other.InventoryContext;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickEvent {

   InventoryContext context;
    InventoryClickEvent event;

    public ClickEvent(InventoryContext context, InventoryClickEvent event) {
        this.context = context;
        this.event = event;
    }

    public InventoryContext getContext() {
        return context;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }
}
