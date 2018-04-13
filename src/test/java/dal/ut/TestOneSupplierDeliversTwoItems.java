package dal.ut;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import inventory.dao.ItemDAO;
import inventory.dao.ItemDAOImpl;
import inventory.dao.SupplierDAO;
import inventory.dao.SupplierDAOImpl;
import inventory.model.Inventory;
import inventory.model.ItemEntity;
import inventory.model.SupplierEntity;
import inventory.service.SupplierProvider;
import inventory.ws.DALException;
import inventory.ws.dto.Item;
import inventory.ws.dto.Supplier;

/**
 * Test at the resource level, so web service without HTTP
 * @author jeromeboyer
 *
 */
public class TestOneSupplierDeliversTwoItems extends BaseTest{

	static long supplierId=0;
	
	/*
	 * We have one supplier deliver one item on site A
	 */
	// @Test
	public void aSupplierDeliverItemsSiteA() {
		System.out.println("STEP 1 - Add supplier");
		Supplier s = new Supplier();
		s.setName("TestSupplier1");
		s.setStatus("New");
		s.setState("CA");
		s.setCity("San Francisco");
		s.setStreet("10 first street");
		s.setZipcode("90000");
		s.setType("ORGANIZATION");
		Supplier sOut=null;
		try {
			sOut = dalWebService.newSupplier(s);
			supplierId=sOut.getId();
		} catch (DALException e) {
			e.printStackTrace();
			Assert.fail("Exception in save new supplier");
		}
		Assert.assertNotNull(sOut);
		Assert.assertTrue(sOut.getId()>0);
		Assert.assertTrue("TestSupplier1".equals(sOut.getName()));
		
		System.out.println("STEP 2 - Add 4 items in the product DB");
		int nb_items = 4;
		Long[] itemIds = new Long[nb_items];
		for (int i = 0; i<nb_items;i++) {
			Item ie= new Item("item_"+i);
			ie.setDescription("This is item_"+i);
			ie.setPrice(1000+i);
			try {
				Item itemOut=dalWebService.newItem(ie);
				itemIds[i]=itemOut.getId();
			} catch (DALException e) {
				e.printStackTrace();
				fail("Exception while saving items");
			}
		}
		
		System.out.println("STEP 3 - now supplier delivers the items to site A");
		try {
			dalWebService.newInventoryEntry(itemIds[0], 10, "SiteA",supplierId,50);
			dalWebService.newInventoryEntry(itemIds[1], 20, "SiteA",supplierId,70);
			System.out.println("  -> two items in site A");
			Collection<Item> itdto=dalWebService.getItemsPerSite("SiteA");
			Assert.assertTrue(2==itdto.size());
			System.out.println("  -> one supplier of item "+itemIds[0]);
			Collection<Supplier> suppliers=dalWebService.getSuppliersOfItem(itemIds[0]);
			Assert.assertTrue(1==suppliers.size());
			Supplier su = suppliers.iterator().next();
			Assert.assertTrue(su.getId()==supplierId);
			System.out.println("  who supply item "+itemIds[0]+"? -> "+su.getName());
			System.out.println("STEP 4 - supplier delivers 2new  items to site B");
			dalWebService.newInventoryEntry(itemIds[0], 15, "SiteB",supplierId,50);
			dalWebService.newInventoryEntry(itemIds[1], 25, "SiteB",supplierId,70);
			System.out.println("  what items were delivered by  "+su.getName()+" ?");
			Collection<Inventory> ivs = dalWebService.getInventoryBySupplier(supplierId);
			Assert.assertTrue(ivs.size() == 4);
			for (Inventory iv : ivs) {
				System.out.print(iv.getItemId()+" ");
			}
			int q=+dalWebService.getItemStock(itemIds[0]);
			System.out.println("   How many item "+itemIds[0]+" overall ? -> "+q);
			Assert.assertTrue(q == 25);
			
			ivs=dalWebService.getInventoryCrossSite();
			for (Inventory iv : ivs) {
				System.out.println(iv.toString());
			}
		} catch (DALException e) {
			e.printStackTrace();
		}
		
	}

	//@Test
	public void testProvisioningItemAtHigherLevel() throws DALException{
		Item it = new Item();
		it.setId(new Long(2));
		it.setName("itemTest");
		it.setDescription("This is a cool item");
		it.setPrice(1000);
		it.setModel("ModelS");
		it.setSerialNumber("12345");
		Supplier s = new Supplier();
		s.setId(new Long(1));
		s.setName("TestSupplier3");
		s.setStatus("New");
		s.setState("CA");
		s.setCity("San Francisco");
		s.setStreet("13 first street");
		s.setZipcode("90000");
		s.setType("ORGANIZATION");
		
		System.out.println("Test newItemToSite which is a higher level API");
		Inventory iv = dalWebService.newInventoryEntry(it.getId(),10,"SiteC",s.getId(),50);

		Assert.assertNotNull(iv);
		System.out.println("Inventory:"+iv.toString());
		Item it2 = dalWebService.getItemById(iv.getItemId());
		Assert.assertNotNull(it2);
		System.out.println("Item:"+it2.toString());
		Supplier s2= dalWebService.getSupplierById(iv.getSupplierId());
		Assert.assertNotNull(s2);
		System.out.println("Supplier:"+s2.toString());
		
		System.out.println("## Sell 20 of it");
		Inventory iv2 = dalWebService.sellItem(it2.getId(),10,"SiteC",201,60);
		Assert.assertNotNull(iv2);
		int q=+dalWebService.getItemStock(it2.getId());
		Assert.assertTrue(q == 190);
	}
}
