package inventory.dao;

import java.util.List;

import inventory.model.ItemEntity;
import inventory.ws.DALException;

public interface ItemDAO {

	// Item CRUD 
	List<ItemEntity> getItems() throws DALException;

	ItemEntity updateItem(ItemEntity inItem) throws DALException;

	ItemEntity getItemEntityById(long id) throws DALException;

	ItemEntity createItem(ItemEntity ie) throws DALException;

	String deleteItem(long id) throws DALException;
	
	ItemEntity getItemEntityByName(String name) throws DALException;

	List<ItemEntity> searchItemEntitiesByName(String name) throws DALException;

}
