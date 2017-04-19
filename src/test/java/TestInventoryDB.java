import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import inventory.ws.InventoryService;
import inventory.ws.Item;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestInventoryDB {

	static InventoryService serv;
	static long idToKeep=351;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 serv = new InventoryService();
		 /**
		 try {
			 Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			 DriverManager.getConnection("jdbc:derby:memory:INVDB;create=true");
		 } catch (Exception ex) {
	         ex.printStackTrace();
	         fail("Exception during database startup.");
	     }
	     */
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void saveItem(){
		Item ie= new Item("Old Stuff");
		ie.setDescription("This is an old computer");
		ie.setPrice(1000);
		ie=serv.newItem(ie);
		Assert.assertNotNull(ie);
		Assert.assertTrue(ie.getId()>0);
		idToKeep=ie.getId();
		System.out.println("Id of the new record:"+idToKeep);
	}
	

	@Test
	public void testGetOneItem() {
		System.out.println("Id:"+idToKeep);
		Item item= serv.getItemById(idToKeep);
		Assert.assertNotNull(item);
		Assert.assertTrue(item.getId() == idToKeep);
	}
	
	@Test
	public void testLoadAllItems() {
		Collection<Item> items = serv.getItems();
		Assert.assertNotNull(items);
		Assert.assertTrue(items.size() >= 1);
	}
	
	@Test
	public void testWDeleteOneItem() {
		String s= serv.deleteItem(idToKeep);
		Assert.assertNotNull(s);
		Assert.assertTrue("Success".equals(s));
	}
	
	

}
