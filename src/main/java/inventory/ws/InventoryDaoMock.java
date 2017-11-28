package inventory.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import inventory.model.Inventory;
import inventory.model.ItemEntity;

public class InventoryDaoMock implements ItemDAO,InventoryDAO {
    public static HashMap<Long,ItemEntity> items = new HashMap<Long,ItemEntity>();
    public static int count =3;
    
    public InventoryDaoMock(){
    	ItemEntity t = new ItemEntity("Smart phone");
    	t.setId(1);
    	items.put(new Long(t.getId()),t);
		ItemEntity t2 = new ItemEntity("Tablet");
		t2.setId(2);
		items.put(new Long(t2.getId()),t2);
    }
    
	@Override
	public List<ItemEntity> getItems() {
		return new ArrayList<ItemEntity>(items.values());
	}

	@Override
	public ItemEntity updateItem(ItemEntity inItem) {
		return inItem;
	}

	@Override
	public ItemEntity getItemEntityById(long id) {
		return items.get(new Long(id));
	}

	@Override
	public ItemEntity createItem(ItemEntity ie) {
		ie.setId(count);
		items.put(new Long(count), ie);
		count++;
		return ie;
	}

	@Override
	public String deleteItem(long id) {
		items.remove(new Long(id));
		return "Success";
	}

	@Override
	public ItemEntity getItemEntityByName(String name) {
		for (ItemEntity ie : items.values()) {
			if (name.equals(ie.getName())) {
				return ie;
			}
		}
		return null;
	}

	@Override
	public List<ItemEntity> searchItemEntitiesByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Inventory> getItemsPerSite(String siteName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory createInventoryEntry(Inventory iv) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory getInventoryById(long inventoryId) throws DALException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory updateInventoryEntry(Inventory iv) throws DALException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteInventoryEntry(long id) throws DALException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory getInventoryForSiteAndItemId(long itemIdToKeep, String siteName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Inventory> getInventoryCrossSite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStock(Long id) throws DALException {
		// TODO Auto-generated method stub
		return 0;
	}

}
