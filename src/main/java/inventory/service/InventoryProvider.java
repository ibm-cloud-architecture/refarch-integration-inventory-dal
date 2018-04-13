package inventory.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import inventory.dao.InventoryDAO;
import inventory.dao.InventoryDAOImpl;
import inventory.dao.ItemDAO;
import inventory.dao.ItemDAOImpl;
import inventory.dao.SupplierDAO;
import inventory.dao.SupplierDAOImpl;
import inventory.model.Inventory;
import inventory.model.ItemEntity;
import inventory.model.SupplierEntity;
import inventory.ws.DALException;
import inventory.ws.dto.Supplier;

/**
 * A inventory a provider class manages the stock of item. It supports the 
 * business requirements of the application. It uses DAO to call the persistence layer.
 *  
 * @author Jerome Boyer
 *
 */
public class InventoryProvider {
	private ItemDAO itemDao;
    private InventoryDAO invDao;
    private SupplierDAO supplierDao;
	 
	public InventoryProvider() {
		this.itemDao = new ItemDAOImpl();
	  	this.invDao = new InventoryDAOImpl();
	  	this.supplierDao = new SupplierDAOImpl();
	}
	 
	public Collection<ItemEntity> getItems() throws DALException{
		return itemDao.getItems();
	}
		

	public ItemEntity getItemById(long id) throws DALException{
		return itemDao.getItemEntityById(id);
	}
	
	
	public ItemEntity getItemByName(String name) throws DALException{
		return itemDao.getItemEntityByName(name);
	}

	public ItemEntity updateItem(ItemEntity inItem) throws DALException{
		inItem.setUpdateDate(new Date());
		return itemDao.updateItem(inItem);
	}
	
	public ItemEntity newItem(ItemEntity ie) throws DALException{
		ie.setId(null);
		ie.setCreationDate(new Date());
		ie.setUpdateDate(ie.getCreationDate());		
		return itemDao.createItem(ie);
	}


	public String deleteItem(long id) throws DALException{
		return itemDao.deleteItem(id);
	}

	
	public Collection<ItemEntity> searchByName(String name) throws DALException{		
		return itemDao.searchItemEntitiesByName(name);
	}
	
	// ----------------------------------------------------------------
	// Inventory
	// ----------------------------------------------------------------
	public Collection<ItemEntity> getItemsPerSite(String siteName) throws DALException{
		// inventory keeps item id in a given site
		Collection<Inventory> il = this.getInventoryBySite(siteName);
		List<ItemEntity> li = new ArrayList<ItemEntity>();
		for (Inventory iv : il){
			ItemEntity ie = itemDao.getItemEntityById(iv.getItemId());
			li.add(ie);
		}
		return li;
	}
	

	public Collection<Inventory> getInventoryBySite(String siteName) throws DALException{
		if (siteName != null) return invDao.getItemsPerSite(siteName);
		return null;
	}
	

	public Collection<Inventory> getInventoryBySupplier(Long supplierId) throws DALException{
		if (supplierId != null) {
			return invDao.getInventoryPerSupplier(supplierId);
		}
		return null;
	}
	
	public Collection<Supplier> getSuppliersOfItem(Long itemId) throws DALException{
		Collection<Supplier> ls = new ArrayList<Supplier>();
		if (itemId != null) {
				Collection<Inventory> l=invDao.getSiteInventoryByItemId(itemId);
				if (l != null) {
					for (Inventory iv :l) {
						SupplierEntity sue = supplierDao.getById(iv.getSupplierId());
						Supplier su = new Supplier(sue);
						ls.add(su);
					}
					return ls;
				}
		}
		return null;
	}
	

	public int getItemStock(Long id) throws DALException {
		int total = 0;
		Collection<Inventory> results = invDao.getSiteInventoryByItemId(id);
		for (Inventory iv : results) {
			total += iv.getQuantity();
		}
		return total;
	}

	/**
	 * Create a new inventory entry
	 * @param item ID of the persisted Item. There is only one instance of an item
	 * @param quantity
	 * @param site name
	 * @param supplierId
	 * @return an inventory with an id
	 * @throws DALException
	 */
	public Inventory newInventoryEntry(long it, int q, String site, long supplierId,double cost) throws DALException {
		Inventory iv = new Inventory();
		iv.setQuantity(q);
		iv.setSite(site);
		iv.setItemId(it);
		iv.setSupplierId(supplierId);
		iv.setUpdateDate(new Date());
		iv.setCost(cost);
		iv.setCreationDate(iv.getUpdateDate());
		return invDao.createInventoryEntry(iv);
	}
	
	// ATTENTION this code is not XA transaction, it can lead to bad data in DB
	public Inventory newItemToSite(ItemEntity it, Supplier s, String siteName, int q,double cost)  throws DALException {
		ItemEntity ieOut = itemDao.createItem(it);
		SupplierEntity sue = new SupplierEntity(s);
		SupplierEntity sueOut = supplierDao.saveSupplier(sue);
		return newInventoryEntry(ieOut.getId(),q,siteName,sueOut.getId(),cost);
	}


	public Inventory sellItem(long it, int q, String site, long customerId,double amount)  throws DALException {
		Inventory iv = new Inventory();
		if (q > 0) q=-q;
		iv.setQuantity(q);
		iv.setSite(site);
		iv.setItemId(it);
		iv.setCustomerId(customerId);
		iv.setUpdateDate(new Date());
		iv.setSoldPrice(amount);
		iv.setCreationDate(iv.getUpdateDate());
		return invDao.createInventoryEntry(iv);
	}
	
	public Inventory getInventoryById(long inventoryId) throws DALException {
		return invDao.getInventoryById(inventoryId);
	}
	
	
	public Inventory updateInventoryEntry(Inventory iv) throws DALException {
		iv.setUpdateDate(new Date());
		return invDao.updateInventoryEntry(iv);
	}
	

	public Inventory getInventoryForSiteAndItemId(long itemIdToKeep, String siteName)  throws DALException {
		if (siteName != null && itemIdToKeep > 0) {
			return invDao.getInventoryForSiteAndItemId(itemIdToKeep,siteName);
		}
		return null;
	}
	

	public Collection<Inventory> getInventoryCrossSite()  throws DALException{
		return invDao.getInventoryCrossSite();
	}

	public String deleteInventoryEntry(long inventoryId) throws DALException {
		return invDao.deleteInventoryEntry(inventoryId);
	}

	public Collection<Inventory> getSiteInventoryByItemId(Long itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
