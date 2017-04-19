package inventory.ws;

import java.util.List;

import inventory.model.ItemEntity;

public interface InventoryDAO {

	List<ItemEntity> getItems();

	ItemEntity updateItem(ItemEntity inItem);

	ItemEntity getItemEntityById(long id);

	String addItem(ItemEntity ie);

	String deleteItem(long id);

	List<ItemEntity> getItemByName(String name);

}
