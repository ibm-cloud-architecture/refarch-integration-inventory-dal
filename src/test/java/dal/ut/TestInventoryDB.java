package dal.ut;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Collection;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import inventory.model.Inventory;
import inventory.ws.DALException;
import inventory.ws.DALService;
import inventory.ws.Item;

/**
 * Test at the service level the basic CRUD operation on inventory item.
 * The persistence.xml uses derby embedded for testing purpose so each test case
 * can delete the DB at the end of their unit tests
 * @author jerome boyer
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestInventoryDB extends BaseTest{


	// keep ids for item and inventory
	static long itemIdToKeep=351;
	static long inventoryId=0;


	@Test
	public void saveOneItem(){
		System.out.println("Save one item with the inventory on one site");
		Inventory iv = null;
		Item ie= new Item("Old Stuff");
		ie.setDescription("This is an old computer");
		ie.setPrice(1000);
		ie.setImg("a path to an image");
		try {
			ie=serv.newItem(ie);
			// add to the inventory
			iv=serv.newInventoryEntry(ie.getId(),10,"site A",501,50);
			
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception");
		}
		Assert.assertNotNull(ie);
		Assert.assertTrue(ie.getId()>0);
		itemIdToKeep=ie.getId();
		System.out.println("Id of the new record:"+itemIdToKeep);
		Assert.assertNotNull(iv);
		Assert.assertTrue(iv.getId()>0);
		inventoryId=iv.getId();
		System.out.println(iv.toString());
	}
	

	@Test
	public void testGetOneItem() {
		System.out.println("Load Item Id:"+itemIdToKeep);
		Item item = null;
		try {
			item = serv.getItemById(itemIdToKeep);
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception to get one item");
		}
		Assert.assertNotNull(item);
		Assert.assertTrue(item.getId() == itemIdToKeep);
	}
	
	@Test
	public void testGetOneInventory() {
		System.out.println("Load inventory Id:"+inventoryId);
		Inventory ivt = null;
		try {
			ivt = serv.getInventoryById(inventoryId);
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception to get one item");
		}
		Assert.assertNotNull(ivt);
		Assert.assertTrue(ivt.getId() == inventoryId);
	}
	
	@Test
	public void testLoadAllItems() {
		Collection<Item> items =null;
		try {
			items = serv.getItems();
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception");
		}
		Assert.assertNotNull(items);
		Assert.assertTrue(items.size() >= 1);
	}
	
	@Test
	public void testLoadAllInventory() {
		Collection<Inventory> items =null;
		try {
			items = serv.getInventoryCrossSite();
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception");
		}
		Assert.assertNotNull(items);
		Assert.assertTrue(items.size() >= 1);
	}
	
	
	@Test
	public void testUpdateQuantity(){
		System.out.println("Update inventory for site A and item:"+itemIdToKeep);
		try {
			Inventory entry= serv.getInventoryForSiteAndItemId(itemIdToKeep,"site A");
			Assert.assertNotNull(entry);
			long currentQuantity=entry.getQuantity();
			Assert.assertEquals(10, currentQuantity);
			entry.setQuantity(entry.getQuantity()+1);
			serv.updateInventoryEntry(entry);
			Inventory ivOut=serv.getInventoryById(entry.getId());
			Assert.assertTrue(ivOut.getQuantity()==currentQuantity +1);
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception in update Inventory test");
		}	
	}
	
	
	
	@Test
	public void testItemsPerSite() {
		System.out.println("Get items per site for site A");
		Collection<Inventory> il=null;
		try {
			il = serv.getInventoryBySite("site A");
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception in accessing inventory tables");
		}
		Assert.assertNotNull(il);
		Assert.assertTrue(il.size()>0);
		System.out.println(il.iterator().next().toString());
		// or with items
		try {
			Collection<Item> items = serv.getItemsPerSite("site A");
			Assert.assertNotNull(items);
			Assert.assertTrue(items.size()>0);
			System.out.println(items.iterator().next().toString());
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception in accessing inventory tables");
		}
		
	}
	
	
	@Test
	/**
	 * Add a site for the same item; compute stock
	 */
	public void testCrossSite(){
		try {
			Inventory iv=serv.newInventoryEntry(itemIdToKeep,2,"site B",501,50);
			Assert.assertTrue(12 == serv.getItemById(itemIdToKeep).getQuantity());
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception to get one item");
		}
	}
	
	
	@Test
	public void testWDeleteAInventory() {
		String s=null;
		try {
			s = serv.deleteInventoryEntry(inventoryId);
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception in delete Inventory test");
		}
		Assert.assertNotNull(s);
		Assert.assertTrue("Success".equals(s));
	}
	
	@Test
	public void testWDeleteOneItem() {
		String s=null;
		try {
			s = serv.deleteItem(itemIdToKeep);
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception in delete item test");
		}
		Assert.assertNotNull(s);
		Assert.assertTrue("Success".equals(s));
	}
	
	@Test
	public void wellDoASearch(){
		try {
			Item ie= new Item("item1");
			ie.setDescription("This is item");
			ie.setPrice(1000);
			ie.setImg("a path to an image");
			ie.setPrice(10000);
			ie=serv.newItem(ie);
			ie= new Item("item2");
			ie.setDescription("This is item");
			ie.setPrice(1000);
			ie.setImg("a path to an image");
			ie.setPrice(10000);
			ie=serv.newItem(ie);
			ie= new Item("item3");
			ie.setDescription("This is item");
			ie.setPrice(1000);
			ie.setImg("a path to an image");
			ie.setPrice(10000);
			ie=serv.newItem(ie);
			Collection<Item>  items = serv.getItems();
			Assert.assertTrue(items.size() >= 3);
			items=serv.searchByName("item");
			Assert.assertTrue(items.size() >= 3);
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception");
		}
	}
	
}
