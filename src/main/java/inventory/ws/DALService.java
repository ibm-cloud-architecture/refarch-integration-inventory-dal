package inventory.ws;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import inventory.model.Inventory;
import inventory.model.ItemEntity;
import inventory.model.Supplier;

@WebService
public class DALService {

    private ItemDAO itemDao;
    private InventoryDAO invDao;
    private SupplierDAO supplierDao;
    
    public DALService(){
    	invDao = new InventoryDaoImpl();
    	itemDao = new ItemDaoImpl();
    	supplierDao=new SupplierDAOImpl();
    }
    
    
	@WebMethod
	public String sayHello(@WebParam(name="name")String name) {
	        return "Inventory DAL welcome, " + name + ".";
	}

	
	@WebMethod(operationName="items")
	public Collection<Item> getItems() throws DALException{
		return processList(itemDao.getItems());
	}
	
	private Collection<Item>  processList(Collection<ItemEntity> l ){
		List<Item> li=new ArrayList<Item>();
		for (ItemEntity ie : l){
			// Attention the quantity to 0 is to avoid too much loading
			Item i = new Item(ie,0);
			li.add(i);
		}
		return li;
	}

	@WebMethod(operationName="itemById")
	public Item getItemById(@WebParam(name="id")long id) throws DALException{
		ItemEntity ie =itemDao.getItemEntityById(id);
		if (ie != null) return new Item(ie,invDao.getStock(id));
		return null;
	}
	
	
	@WebMethod(operationName="itemByName")
	public Item getItemByName(@WebParam(name="name")String name) throws DALException{
		ItemEntity ie =itemDao.getItemEntityByName(name);
		if (ie != null) {
			return new Item(ie,invDao.getStock(ie.getId()));
		}
		return null;
	}

	@WebMethod(operationName="updateItem")
	public Item updateItem(Item inItem) throws DALException{
		ItemEntity ie = new ItemEntity(inItem);
		return new Item(itemDao.updateItem(ie),inItem.getQuantity());
	}
	
	@WebMethod(operationName="newItem")
	public Item newItem(Item inItem) throws DALException{
		ItemEntity ie = new ItemEntity(inItem);
		ie.setId(null);
		ie.setCreationDate(new Timestamp((new Date()).getTime()));
		ie.setUpdateDate(ie.getCreationDate());		
		ItemEntity outItem=itemDao.createItem(ie);
		return new Item(outItem,0);
	}

	@WebMethod(operationName="deleteItem")
	public String deleteItem(@WebParam(name="id")long id) throws DALException{
		return itemDao.deleteItem(id);
	}

	@WebMethod(operationName="searchByName")
	public Collection<Item> searchByName(String name) throws DALException{		
		return processList(itemDao.searchItemEntitiesByName(name));
	}
	
	// ----------------------------------------------------------------
	// Inventory
	// ----------------------------------------------------------------
	@WebMethod(operationName="itemsPerSite")
	public Collection<Item> getItemsPerSite(String siteName) throws DALException{
		Collection<Inventory> il=invDao.getItemsPerSite(siteName);
		List<Item> li=new ArrayList<Item>();
		for (Inventory iv : il){
			ItemEntity ie=itemDao.getItemEntityById(iv.getItem());
			Item i = new Item(ie,iv.getQuantity());
			li.add(i);
		}
		return li;
	}
	
	@WebMethod(operationName="getInventoryBySite")
	public Collection<Inventory> getInventoryBySite(String siteName) throws DALException{
		if (siteName != null) return invDao.getItemsPerSite(siteName);
		return null;
	}
	
	@WebMethod(operationName="newInventoryEntry")
	public Inventory newInventoryEntry(long it, int q, String site) throws DALException {
		Inventory iv = new Inventory();
		iv.setQuantity(q);
		iv.setSite(site);
		iv.setItem(it);
		return invDao.createInventoryEntry(iv);
	}
	
	@WebMethod(operationName="inventoryById")
	public Inventory getInventoryById(long inventoryId) throws DALException {
		return invDao.getInventoryById(inventoryId);
	}
	
	@WebMethod(operationName="updateInventoryEntry")
	public Inventory updateInventoryEntry(Inventory iv) throws DALException {
		return invDao.updateInventoryEntry(iv);
	}
	
	@WebMethod(operationName="getInventoryForSiteAndItemId")
	public Inventory getInventoryForSiteAndItemId(long itemIdToKeep, String siteName)  throws DALException {
		if (siteName != null && itemIdToKeep > 0) {
			return invDao.getInventoryForSiteAndItemId(itemIdToKeep,siteName);
		}
		return null;
	}
	
	@WebMethod(operationName="getInventoryCrossSite")
	public Collection<Inventory> getInventoryCrossSite()  throws DALException{
		return invDao.getInventoryCrossSite();
	}

	@WebMethod(operationName="deleteInventory")
	public String deleteInventoryEntry(long inventoryId) throws DALException {
		return invDao.deleteInventoryEntry(inventoryId);
	}

	
	// ----------------------------------------------------------------
	// Supplier
	// ----------------------------------------------------------------
	@WebMethod(operationName="newSupplier")
	public Supplier newSupplier(Supplier s) throws DALException{
		return supplierDao.saveSupplier(s);
	}

	@WebMethod(operationName="supplierById")
	public Supplier getSupplierById(long supplierId)  throws DALException {
		return supplierDao.getById(supplierId);
	}

	@WebMethod(operationName="supplierByName")
	public Supplier getSupplierByName(String name) throws DALException {
		return supplierDao.getByName(name);
	}

	@WebMethod(operationName="suppliers")
	public Collection<Supplier> getSuppliers() throws DALException {
		return supplierDao.getSuppliers();
	}
	
	@WebMethod(operationName="updateSupplier")
	public Supplier updateSupplier(Supplier s) throws DALException{
		return supplierDao.updateSupplier(s);
	}
	
	@WebMethod(operationName="deleteSupplier")
	public String deleteSupplier(long id) throws DALException {
		return supplierDao.deleteSupplier(id);
	}
}
