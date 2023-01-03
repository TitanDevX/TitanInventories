package me.titan.titaninvs.content;

/**
 *
 * An inventory contexts holds both inventory contents (items) and data (provided on initialization)
 *
 * Both data and contents are modifiable however you will need to call some updating method for the contents to apply.
 *
 * This class is specially made for {@link me.titan.titaninvs.invs.UpdatingInv}.
 *
 */
public class InventoryContext {

	private InventoryContents contents;
	private Object[] data;


	public InventoryContext( InventoryContents contents, Object[] data) {
		this.contents = contents;
		this.data = data;
	}

	public InventoryContents getContents() {
		return contents;
	}

	public void setContents(InventoryContents contents) {
		this.contents = contents;
	}

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}


}
