package inventory.dao;

import java.util.Collection;

import inventory.model.Inventory;
import inventory.ws.DALException;

public interface InventoryDAO {
	// Inventory API
	Collection<Inventory> getItemsPerSite(String siteName);

	Inventory createInventoryEntry(Inventory iv) throws DALException;
	
	Inventory updateInventoryEntry(Inventory iv) throws DALException;

	Inventory getInventoryById(long inventoryId) throws DALException;
	
	String deleteInventoryEntry(long id) throws DALException;

	Inventory getInventoryForSiteAndItemId(long itemIdToKeep, String siteName)  throws DALException;

	Collection<Inventory> getInventoryCrossSite()  throws DALException;

	Collection<Inventory> getSiteInventoryByItemId(long itemId) throws DALException;

	Collection<Inventory> getInventoryPerSupplier(Long supplierId) throws DALException;

}
