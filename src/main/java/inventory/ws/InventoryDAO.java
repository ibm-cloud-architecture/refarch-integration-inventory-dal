package inventory.ws;

import java.util.List;

import inventory.model.ItemEntity;

public interface InventoryDAO {

	List<ItemEntity> getItems() throws DALException;

	ItemEntity updateItem(ItemEntity inItem) throws DALException;

	ItemEntity getItemEntityById(long id) throws DALException;

	String addItem(ItemEntity ie) throws DALException;

	String deleteItem(long id) throws DALException;
	
	ItemEntity getItemEntityByName(String name) throws DALException;
}
