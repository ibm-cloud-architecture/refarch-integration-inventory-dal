package inventory.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import inventory.model.Inventory;
import inventory.model.ItemEntity;
import inventory.model.SupplierEntity;

@WebService
public class DALService {

    private ItemDAO itemDao;
    private InventoryDAO invDao;
    private SupplierDAO supplierDao;
    
    public DALService(){
    	invDao = new InventoryDAOImpl();
    	itemDao = new ItemDAOImpl();
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
		if (ie != null) return new Item(ie,getItemStock(id));
		return null;
	}
	
	
	@WebMethod(operationName="itemByName")
	public Item getItemByName(@WebParam(name="name")String name) throws DALException{
		ItemEntity ie =itemDao.getItemEntityByName(name);
		if (ie != null) {
			return new Item(ie,getItemStock(ie.getId()));
		}
		return null;
	}

	@WebMethod(operationName="updateItem")
	public Item updateItem(Item inItem) throws DALException{
		ItemEntity ie = new ItemEntity(inItem);
		ie.setUpdateDate(new Date());
		return new Item(itemDao.updateItem(ie),inItem.getQuantity());
	}
	
	@WebMethod(operationName="newItem")
	public Item newItem(Item inItem) throws DALException{
		ItemEntity ie = new ItemEntity(inItem);
		ie.setId(null);
		ie.setCreationDate(new Date());
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
	@WebMethod(operationName="getItemsPerSite")
	public Collection<Item> getItemsPerSite(String siteName) throws DALException{
		Collection<Inventory> il=invDao.getItemsPerSite(siteName);
		List<Item> li=new ArrayList<Item>();
		for (Inventory iv : il){
			ItemEntity ie=itemDao.getItemEntityById(iv.getItemId());
			Item i = new Item(ie,iv.getQuantity());
			li.add(i);
		}
		return li;
	}
	
	@WebMethod(operationName="getInventoryPerSite")
	public Collection<Inventory> getInventoryBySite(String siteName) throws DALException{
		if (siteName != null) return invDao.getItemsPerSite(siteName);
		return null;
	}
	
	@WebMethod(operationName="getInventoryPerSupplier")
	public Collection<Inventory> getInventoryBySupplier(Long supplierId) throws DALException{
		if (supplierId != null) {
			return invDao.getInventoryPerSupplier(supplierId);
		}
		return null;
	}
	
	@WebMethod(operationName="getSuppliersOfItem")
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
	
	@WebMethod(operationName="getItemStock")
	public int getItemStock(Long id) throws DALException {
		int total=0;
		Collection<Inventory> results = invDao.getSiteInventoryByItemId(id);
		for (Inventory iv : results) {
			total+=iv.getQuantity();
		}
		return total;
	}

	@WebMethod(operationName="addItemToSite")
	/**
	 * 
	 * @param item ID
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
	
	@WebMethod(operationName="provisionItem")
	// ATTENTION this code is not XA transactional, it can lead to bad data in DB
	public Inventory newItemToSite(Item it, Supplier s, String siteName, int q,double cost)  throws DALException {
		ItemEntity ie = new ItemEntity(it);
		ItemEntity ieOut=itemDao.createItem(ie);
		SupplierEntity sue = new SupplierEntity(s);
		SupplierEntity sueOut = supplierDao.saveSupplier(sue);
		return newInventoryEntry(ieOut.getId(),q,siteName,sueOut.getId(),cost);
	}

	@WebMethod(operationName="sellItem")
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
	
	@WebMethod(operationName="getInventoryForSiteAndItemId")
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


	
	// ----------------------------------------------------------------
	// Supplier
	// ----------------------------------------------------------------
	@WebMethod(operationName="newSupplier")
	public Supplier newSupplier(Supplier s) throws DALException{
		SupplierEntity se = new SupplierEntity(s);
		se.setUpdateDate(new Date());
		se.setCreationDate(se.getUpdateDate());
		SupplierEntity se2= supplierDao.saveSupplier(se);
		return new Supplier(se2);
	}

	@WebMethod(operationName="supplierById")
	public Supplier getSupplierById(long supplierId)  throws DALException {
		SupplierEntity se =supplierDao.getById(supplierId);
		return new Supplier(se);
	}

	@WebMethod(operationName="supplierByName")
	public Supplier getSupplierByName(String name) throws DALException {
		return new Supplier( supplierDao.getByName(name));
	}

	@WebMethod(operationName="suppliers")
	public Collection<Supplier> getSuppliers() throws DALException {		
		Collection<SupplierEntity> sel=supplierDao.getSuppliers();
		List<Supplier> sl=new ArrayList<Supplier>();
		for (SupplierEntity se : sel){
			Supplier s = new Supplier(se);
			sl.add(s);
		}
		return sl;
	}
	
	@WebMethod(operationName="updateSupplier")
	public Supplier updateSupplier(Supplier s) throws DALException{
		SupplierEntity se = new SupplierEntity(s);
		se.setUpdateDate(new Date());
		supplierDao.updateSupplier(se);
		return s;
	}
	
	@WebMethod(operationName="deleteSupplier")
	public String deleteSupplier(long id) throws DALException {
		return supplierDao.deleteSupplier(id);
	}


}
