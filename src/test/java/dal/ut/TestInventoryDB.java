package dal.ut;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import inventory.ws.DALException;
import inventory.ws.InventoryService;
import inventory.ws.Item;

/**
 * Test at the service level the basic CRUD operation on inventory item
 * @author jerome boyer
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestInventoryDB {

	static InventoryService serv;
	static long idToKeep=351;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 serv = new InventoryService();
//		 try {
//			 Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
//			 DriverManager.getConnection("jdbc:derby:memory:INVDB;create=true");
//		 } catch (Exception ex) {
//	         ex.printStackTrace();
//	         org.junit.Assert.fail("Exception during database startup.");
//		 }
	}
	
	static void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		deleteDir(new File("./invdb"));

	}

	@Test
	public void saveItem(){
		Item ie= new Item("Old Stuff");
		ie.setDescription("This is an old computer");
		ie.setPrice(1000);
		ie.setImg("a path to an image");
		ie.setPrice(10000);
		
		try {
			ie=serv.newItem(ie);
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception");
		}
		Assert.assertNotNull(ie);
		Assert.assertTrue(ie.getId()>0);
		idToKeep=ie.getId();
		System.out.println("Id of the new record:"+idToKeep);
	}
	

	@Test
	public void testGetOneItem() {
		System.out.println("Id:"+idToKeep);
		Item item = null;
		try {
			item = serv.getItemById(idToKeep);
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exception");
		}
		Assert.assertNotNull(item);
		Assert.assertTrue(item.getId() == idToKeep);
	}
	
	@Test
	public void testLoadAllItems() {
		Collection<Item> items =null;
		try {
			items = serv.getItems();
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exception");
		}
		Assert.assertNotNull(items);
		Assert.assertTrue(items.size() >= 1);
	}
	
	@Test
	public void testWDeleteOneItem() {
		String s=null;
		try {
			s = serv.deleteItem(idToKeep);
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(s);
		Assert.assertTrue("Success".equals(s));
	}
	
	

}
