package me.titan.titaninvs.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * GUI pagination manager.
 *
 * @See {@link me.titan.titaninvs.examples.UsersGUI} for an example.
 *
 */
public class Pagination {

	int itemsPerPage;

	boolean boxed;
	List<ClickableItem> items= new ArrayList<>();

	Map<Integer, Map<Integer,ClickableItem>> cached = new HashMap<>();

	/**
	 *
	 * Creates an instance.
	 *
	 * @param itemsPerPage items per page.
	 * @param isBoxed if true, borders of each page will be excluded
	 */
	public Pagination(int itemsPerPage, boolean isBoxed){
		this.itemsPerPage = itemsPerPage;
		this.boxed = isBoxed;
		itemsOfList(null);
	}

	/**
	 *
	 * Set all items, to be sorted into pages.
	 * Important method.
	 *
	 * @return self
	 */
	public Pagination setItems(List<ClickableItem> items) {
		itemsOfList(items);
		return this;
	}

	public List<ClickableItem> getItems() {
		return items;
	}

	/**
	 * Get pages amount.
	 * @return number of pages of the gui's pagination
	 */
	public int getPages() {
		if(getItems().size() % itemsPerPage != 0){
			return (getItems().size()/itemsPerPage)+1;
		}
		return getItems().size()/itemsPerPage;
	}

	public boolean isBoxed(){
		return boxed;
	}


	/**
	 * Is there a page after the given page?
	 * @param page the number of the current page
	 * @return if there is a page after the given page
	 */
	public boolean hasNext(int page){
		return getPages()-1>page;
	}
	/**
	 * Is there a page before the given page?
	 * @param page the current page number
	 * @return if there is a page before the current page
	 */
	public boolean hasPrevious(int page){
		return getPages() > 1 && page > 0;
	}

	/**
	 *
	 * @param page the page to get items of
	 * @param invSize gui size
	 * @return content in a page.
	 */
	public Map<Integer, ClickableItem> getPage(int page, int invSize){

		if(cached.containsKey(page)){
			return cached.get(page);
		}
		Map<Integer, ClickableItem> map = new HashMap<>();

		// 28 56
		int from = page*itemsPerPage;
		if(items.size() < from){
			return null;
		}
		int to = Math.min(items.size(),(page+1)*itemsPerPage);
		int slot = isBoxed() ? 10 : 0;
		int i = from;
		while(i<to){
			ClickableItem item = items.get(i);
			if(!isBoxed() || (slot % 9 != 0 && slot % 9 != 8 && slot<=(invSize-10))){
				map.put(slot,item);
				i++;
			}
			slot++;
		}

		cached.put(page,map);

		return map;
	}
	private void itemsOfList(List<ClickableItem> list){
		items = new ArrayList<>() {
			@Override
			public ClickableItem set(int index, ClickableItem element) {
				cached.clear();
				return super.set(index, element);
			}

			@Override
			public boolean add(ClickableItem clickableItem) {
				cached.clear();
				return super.add(clickableItem);
			}

			@Override
			public void add(int index, ClickableItem element) {
				cached.clear();
				super.add(index, element);
			}
		};
		if(list != null){
			items.addAll(list);
		}
	}
}
