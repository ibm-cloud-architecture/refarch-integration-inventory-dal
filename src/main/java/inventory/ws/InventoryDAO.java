package inventory.ws;

import java.util.Collection;

import inventory.model.Inventory;

public interface InventoryDAO {
	// Inventory API
	Collection<Inventory> getItemsPerSite(String siteName);

	Inventory createInventoryEntry(Inventory iv) throws DALException;
	
	Inventory updateInventoryEntry(Inventory iv) throws DALException;

	Inventory getInventoryById(long inventoryId) throws DALException;
	
	String deleteInventoryEntry(long id) throws DALException;

	Inventory getInventoryForSiteAndItemId(long itemIdToKeep, String siteName)  throws DALException;

	Collection<Inventory> getInventoryCrossSite()  throws DALException;

	int getStock(Long id) throws DALException;
}
