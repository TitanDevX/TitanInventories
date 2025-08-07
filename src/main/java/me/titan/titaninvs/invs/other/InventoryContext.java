package me.titan.titaninvs.invs.other;

import org.bukkit.entity.Player;

/**
 * Used for inventory with items properties (such as name, lore, etc) and slots are
 * constant but click action changes depend on given context (like player or any object)
 */
public class InventoryContext {
    Player player;
    Object[] objects;

    public InventoryContext(Player player, Object... objects) {
        this.player = player;
        this.objects = objects;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    public Player getPlayer() {
        return player;
    }
}
