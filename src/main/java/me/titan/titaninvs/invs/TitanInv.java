package me.titan.titaninvs.invs;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public abstract class TitanInv extends HashMap<Integer, ClickableItem> implements InventoryHolder {

    String title;
    int size;

    int currentPage;

    Pagination pagination;

    Inventory inventory;

    int nextPageButtonSlot;
    ClickableItem nextPageButton;
    int previousPageButtonSlot;
    ClickableItem previousPageButton;

    boolean isTitleChanged;

    public TitanInv(String title, int size) {
        this.title = title;
        this.size = size;
    }

    public Inventory open(Player p, int page) {

        init(p);
        if (isCached() && inventory != null) {
            p.openInventory(inventory);
            return inventory;
        }
        if (pagination != null) {
            putAll(pagination.getPage(page, size));
            if (nextPageButton != null && pagination.hasNext(page)) {
                put(nextPageButtonSlot, nextPageButton);
            }
            if (previousPageButton != null && pagination.hasPrevious(page)) {
                put(previousPageButtonSlot, previousPageButton);
            }
        }
        inventory = Bukkit.createInventory(this, size, ChatColor.translateAlternateColorCodes('&', title));


        for (Map.Entry<Integer, ClickableItem> en : entrySet()) {
            inventory.setItem(en.getKey(), en.getValue().getItem());
        }

        p.openInventory(inventory);
        return inventory;


    }

    /**
     * Updates the inventory.
     *
     * <p></p>Note: this does not call any initialize function.
     *
     * @param p
     */
    public void update(Player p) {
        inventory = getInventory();
        if (isTitleChanged) {
            inventory = Bukkit.createInventory(this, size,
                    ChatColor.translateAlternateColorCodes('&', title));
        } else {
            inventory.clear();
        }

        for (Map.Entry<Integer, ClickableItem> en : entrySet()) {
            if(en.getKey() > size){

                throw new RuntimeException("Slot out of bound: " + en.getKey() + " for item with type " + en.getValue().getItem().getType() );
            }
            inventory.setItem(en.getKey(), en.getValue().getItem());
        }
        if (isTitleChanged) {
            p.openInventory(inventory);
            isTitleChanged = false;
        }
    }

    /**
     * Sets the pagination of the inventory
     * <p></p>
     * Note: this function itself doesn't update the inventory,
     * it's recommend that you call it in {@link #init}
     *
     * @param pagination
     */
    public void pagination(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * Open a page, assuming the inventory already is initialized.
     *
     * @param p
     * @param page
     */
    protected Inventory openPage(Player p, int page) {


        clear();
        if (pagination == null) {
            throw new IllegalCallerException("Cannot open page with no-pagination GUI!");
        }
        currentPage = page;

        inventory = Bukkit.createInventory(this, size, ChatColor.translateAlternateColorCodes('&', title));

        putAll(pagination.getPage(page, size));
        if (nextPageButton != null && pagination.hasNext(page)) {
            put(nextPageButtonSlot, nextPageButton);
        } else if (nextPageButton != null) {
            remove(nextPageButtonSlot);
        }
        if (previousPageButton != null && pagination.hasPrevious(page)) {
            put(previousPageButtonSlot, previousPageButton);
        } else if (previousPageButton != null) {
            remove(previousPageButtonSlot);
        }
        onPageOpen(p,page,inventory);

        for (Map.Entry<Integer, ClickableItem> en : entrySet()) {
            inventory.setItem(en.getKey(), en.getValue().getItem());
        }
        p.openInventory(inventory);
        return inventory;
    }
    public void onPageOpen(Player p, int page, Inventory inventory){}

    public abstract void init(Player p);


    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Override this if you want this inventory to be static so it's only initialized 1 time.
     * (you have to cache it yourself but this method will guarantee that the init function don't get called per open call).
     *
     * @return
     */
    public boolean isCached() {
        return false;
    }

    public void set(int slot, ClickableItem item) {
        put(slot, item);
    }

    public ClickableItem get(int slot) {
        return ((HashMap<Integer, ClickableItem>) this).get(slot);
    }

    /**
     * Automatically places next page button with given slot and item,
     *
     * <p>
     * Note: the given ClickableItem's consumer will be overridden.
     * </p>
     *
     * @param slot slot for the item to be placed in.
     * @param item item, the consumer of it will be overridden.
     */
    public void setNextPageButton(int slot, ClickableItem item) {

        nextPageButtonSlot = slot;
        (nextPageButton = item).setConsumer((e) -> {
            openPage((Player) e.getWhoClicked(), currentPage + 1);
        });
    }

    /**
     * Automatically places next previous button with given slot and item,
     *
     * <p>
     * Note: the given ClickableItem's consumer will be overridden.
     * </p>
     *
     * @param slot slot for the item to be placed in.
     * @param item item, the consumer of it will be overridden.
     */
    public void setPreviousPageButton(int slot, ClickableItem item) {
        previousPageButtonSlot = slot;
        (previousPageButton = item).setConsumer((e) -> {
            openPage((Player) e.getWhoClicked(), currentPage - 1);
        });
    }

    /**
     * Fills content with the item.
     *
     * @param item
     */
    public void fill(ClickableItem item) {
        for (int i = 0; i < size; i++) {
            put(i, item);
        }
    }

    /**
     * Fill a specific column
     *
     * @param column
     * @param item
     */
    public void fillColumn(int column, ClickableItem item) {

        // 54 - (9-1)

        for (int i = column; i <= size - (9 - (column)); i += 9) {

            put(i, item);

        }

    }

    public void fillRow(int row, ClickableItem item) {

        // 54 - (9-1)

        for (int i = row * 9; i < (row * 9) + 9; i++) {
            put(i, item);
        }

    }

    public void fillBorders(ClickableItem item) {
        fillRow(0, item);
        fillColumn(0, item);
        fillRow((size / 9) - 1, item);
        fillColumn(8, item);
    }

    public void setTitle(String title) {
        isTitleChanged = true;
        this.title = title;
    }

    public void onClose(InventoryCloseEvent e) {

    }

    public void onOpen(InventoryOpenEvent e) {

    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public boolean isProtected(){
        return true;
    }
}