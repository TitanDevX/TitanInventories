package me.titan.titaninvs.invs;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.ItemInfo;
import me.titan.titaninvs.content.Pagination;
import me.titan.titaninvs.core.TitanInvAPI;
import me.titan.titaninvs.invs.other.AdvancedInventoryFlags;
import me.titan.titaninvs.invs.other.InventoryContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

    protected InventoryContext context;
    int contextObjSize = -1;

    boolean flipingPage;
    public TitanInv(String title, int size) {
        this.title = title;
        this.size = size;
    }
    public TitanInv() {

    }
    public void useContext(int objSize){
       contextObjSize = objSize;
    }
    public void define(String title, int size){

        this.title = title;
        this.size = size;
    }
    public Inventory open(Player p) {
        return open(p,0);
    }

    public Inventory open(Player p, int page) {


        if (isCached() && inventory != null) {
            p.openInventory(inventory);
            return inventory;
        }
        if(contextObjSize != -1 && context == null){
            context = new InventoryContext(p, new Object[contextObjSize]);
        }
        init(p);
        if (pagination != null) {
            putAll(pagination.getPage(page));
            if (nextPageButton != null && pagination.hasNext(page)) {
                put(nextPageButtonSlot, nextPageButton);
            }
            if (previousPageButton != null && pagination.hasPrevious(page)) {
                put(previousPageButtonSlot, previousPageButton);
            }
        }
        inventory = Bukkit.createInventory(this, size, ChatColor.translateAlternateColorCodes('&', getTitle()));


        for (Map.Entry<Integer, ClickableItem> en : entrySet()) {
            setInvItem(en.getKey(), en.getValue().getItem());
        }

        p.openInventory(inventory);
        return inventory;


    }
    public void refresh(Player p){
        if(inventory == null){
            open(p,currentPage);
            return;
        }
        inventory.clear();
        clear();
        init(p);
        if (pagination != null) {
            putAll(pagination.getPage(currentPage));
            if (nextPageButton != null && pagination.hasNext(currentPage)) {
                put(nextPageButtonSlot, nextPageButton);
            }
            if (previousPageButton != null && pagination.hasPrevious(currentPage)) {
                put(previousPageButtonSlot, previousPageButton);
            }
        }
        for (Map.Entry<Integer, ClickableItem> en : entrySet()) {
            setInvItem(en.getKey(), en.getValue().getItem());
        }
    }

    /**
     * Updates the inventory.
     *
     * <p></p>Note: this does not call any initialize function.
     *
     */
    public void update(Player p) {
        inventory = getInventory();
        if (isTitleChanged) {
            inventory = Bukkit.createInventory(this, size,
                    ChatColor.translateAlternateColorCodes('&', getTitle()));
        } else {
            inventory.clear();
        }

        for (Map.Entry<Integer, ClickableItem> en : entrySet()) {

            setInvItem(en.getKey(), en.getValue().getItem());
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
     */
    public void pagination(Pagination pagination) {
        this.pagination = pagination;
        pagination.setInvSize(size);
    }

    /**
     * Open a page, assuming the inventory already is initialized.
     *
     */
    protected Inventory openPage(Player p, int page) {


     //   clear();
        if (pagination == null) {
            throw new RuntimeException("Cannot open page with no-pagination GUI!");
        }
        // CLEARING
        for (Entry<Integer, ClickableItem> en : pagination.getPage(currentPage).entrySet()) {
            remove(en.getKey());
        }
        remove(nextPageButtonSlot);
        remove(previousPageButtonSlot);
        currentPage = page;

        inventory = Bukkit.createInventory(this, size, ChatColor.translateAlternateColorCodes('&', getTitle()));
        onPageOpen(p,page,inventory);
        putAll(pagination.getPage(page));
        if (nextPageButton != null && pagination.hasNext(page)) {
            put(nextPageButtonSlot, nextPageButton);
        }
        if (previousPageButton != null && pagination.hasPrevious(page)) {
            put(previousPageButtonSlot, previousPageButton);
        }


        inventory.clear();
        for (Map.Entry<Integer, ClickableItem> en : entrySet()) {
            setInvItem(en.getKey(), en.getValue().getItem());
        }
        p.openInventory(inventory);
        return inventory;
    }
    public void onPageOpen(Player p, int page, Inventory inventory){}

    public abstract void init(Player p);

    private void setInvItem(int slot, ItemStack item){
        if(slot < 0 || slot > size-1){
            TitanInvAPI.getPlugin().getLogger().severe("[" + getClass().getSimpleName() + " GUI] Item slot is out of valid slot range (slot: " + slot +
                    ", slot range: 0-" + (size-1) + ") for item: " + item);
            return;
        }
        inventory.setItem(slot, item);
    }

    @Override
    @Nonnull
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Override this if you want this inventory to be static so it's only initialized 1 time.
     * (you have to cache it yourself but this method will guarantee that the init function don't get called per open call).
     *
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

    private void prePageFlip(int prevPage, int newPage, Inventory prevInv){
        flipingPage = true;
        onPageFlip(prevPage,newPage,prevInv);
    }
    private void postPageFlip(int prevPage, int newPage, Inventory prevInv){
        flipingPage = false;
        onPostPageFlip(prevPage,newPage,prevInv);
    }
    /**
     * Invoked as soon as the user clicks on next/previous page buttons.
     */
    public void onPageFlip(int prevPage, int newPage, Inventory prevInv){

    }
    /**
     * Invoked after the user finished flipping a page (and now he is saying the new page).
     */
    public void onPostPageFlip(int prevPage, int newPage, Inventory prevInv){

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
            prePageFlip(currentPage,currentPage+1,e.getClickedInventory());
            openPage((Player) e.getWhoClicked(), currentPage + 1);
            postPageFlip(currentPage,currentPage+1,e.getClickedInventory());
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
            prePageFlip(currentPage,currentPage-1,e.getClickedInventory());
            openPage((Player) e.getWhoClicked(), currentPage - 1);
            postPageFlip(currentPage,currentPage-1,e.getClickedInventory());
        });
    }

    public void setPreviousPageItemInfo(ItemInfo info){
        if(info == null) return;
        setPreviousPageButton(info.getSlot(),ClickableItem.empty(info.getItem()));
    }
    public void setNextPageItemInfo(ItemInfo info){
        if(info == null) return;
        setNextPageButton(info.getSlot(),ClickableItem.empty(info.getItem()));
    }
    /**
     * Fills content with the item.
     *
     */
    public void fill(ClickableItem item) {
       fill(item,true);
    }
    public void fillEmptyItem(ItemInfo info) {
        fillEmptyItem(info, true);
    }
    public void fillEmptyItem(ItemInfo info, boolean override) {
        if(info == null) return;
        fill(ClickableItem.empty(info.getItem()), override);
    }
    /**
     * Fills content with the item.
     *
     */
    public void fill(ClickableItem item, boolean override) {
        for (int i = 0; i < size; i++) {
            if(override || get(i) == null)
                put(i, item);
        }
    }
    /**
     * Fill a specific column
     *
     */
    public void fillColumn(int column, ClickableItem item) {
        fillColumn(column,item,true);
    }
    /**
     * Fill a specific column
     *
     */
    public void fillColumn(int column, ClickableItem item,boolean override) {

        for (int i = column; i <= size - (9 - (column)); i += 9) {
            if(override || get(i) == null)
                put(i, item);

        }
    }
    public void fillRow(int row, ClickableItem item) {
        fillRow(row,item,true);
    }
    public void fillRow(int row, ClickableItem item, boolean override) {

        // 54 - (9-1)

        for (int i = row * 9; i < (row * 9) + 9; i++) {
            if(override || get(i) == null)
             put(i, item);
        }

    }

    /**
     * Fills slots between min slot (inclusive) and max slot (inclusive).
     */
    public void fillInRange(int minSlot, int maxSlot, ClickableItem item){
        for(int i = minSlot;i<=maxSlot;i++){
            set(i,item);
        }
    }
    public void fillBorders(ClickableItem item) {
       fillBorders(item,true);
    }
    public void fillBorders(ClickableItem item,boolean override) {
        fillRow(0, item,override);
        fillColumn(0, item,override);
        fillRow((size / 9) - 1, item,override);
        fillColumn(8, item,override);
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
        return title.replace("%page%",currentPage+1 + "").replace("%pages%",pagination == null ? "" : pagination.getPages() + "");
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

    public void setEmptyItem(ItemInfo info){
        if(info == null) return;
        set(info.getSlot(),ClickableItem.empty(info.getItem()));
    }
    public void setItem(ItemInfo info, Consumer<InventoryClickEvent> con){
        if(info == null) return;
        set(info.getSlot(),ClickableItem.of(info.getItem(),con));
    }

    public boolean isFlipingPage() {
        return flipingPage;
    }
    public boolean preventClickOwnInv(){
        return false;
    }

    public void onClick(InventoryClickEvent e){}
    public void onDrag(InventoryDragEvent e){}

    public List<AdvancedInventoryFlags> getAdvancedFlags(){
        return null;
    }

    public InventoryContext getContext() {
        return context;
    }
}