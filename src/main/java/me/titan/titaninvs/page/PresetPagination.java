package me.titan.titaninvs.page;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.Pagination;

import java.util.HashMap;
import java.util.Map;

public class PresetPagination extends Pagination {

    Map<Integer, Map<Integer, ClickableItem>> items = new HashMap<>();
    int lastAddedPage = 0;

    /**
     * Creates an instance.
     *
     * @param itemsPerPage items per page.
     * @param isBoxed      if true, borders of each page will be excluded
     */
    public PresetPagination(int itemsPerPage, boolean isBoxed) {
        super(itemsPerPage, isBoxed);
    }

    public PresetPagination(){
        super(0,false);
    }
    public void addPage(Map<Integer, ClickableItem> page){

        lastAddedPage++;
        items.put(lastAddedPage,page);
    }
    public void setPage(int id, Map<Integer, ClickableItem> page){
        if(id > lastAddedPage) lastAddedPage = id;
        items.put(id,page);
    }

    @Override
    public int getPages() {
        return items.size();
    }

    @Override
    public Map<Integer, ClickableItem> getPage(int page) {
        return items.get(page);
    }
}
