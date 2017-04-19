package inventory.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import inventory.model.ItemEntity;

@WebService
public class InventoryService {
	private String message = new String("Hello, ");
    //private InventoryDAO dao = new InventoryDaoImpl();
    private InventoryDAO dao = new InventoryDaoMock();
    
	@WebMethod
	public String sayHello(String name) {
	        return message + name + ".";
	}

	@WebMethod(operationName="items")
	public Collection<Item> getItems(){
		Collection<ItemEntity> l=dao.getItems();
		List<Item> li=new ArrayList<Item>();
		for (ItemEntity ie : l){
			Item i = new Item(ie);
			li.add(i);
		}
		return li; 
	}

	@WebMethod(operationName="itemById")
	public Item getItemById(long id){
		ItemEntity ie =dao.getItemEntityById(id);
		if (ie != null) return new Item(ie);
		return null;
	}

	@WebMethod(operationName="updateItem")
	public Item updateItem(Item inItem){
		ItemEntity ie = new ItemEntity(inItem);
		return new Item(dao.updateItem(ie));
	}
	
	@WebMethod(operationName="newItem")
	public Item newItem(Item inItem){
		ItemEntity ie = new ItemEntity(inItem);
		ie.setCreationDate(new Date());
		ie.setUpdateDate(ie.getCreationDate());		
		if ("Success".equals(dao.addItem(ie))) {
			List<ItemEntity> outItems=dao.getItemByName(ie.getName());
			ItemEntity outItem = outItems.get(outItems.size()-1);
			return new Item(outItem);
		}
		return null;
	}

	@WebMethod(operationName="deleteItem")
	public String deleteItem(long id){
		return dao.deleteItem(id);
	}
}
