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
 * Test at the service level the basic CRUD operation on inventory item.
 * The persistence.xml uses derby embedded for testing purpose so testcase
 * can delete the DB at the end of their unit tests
 * @author jerome boyer
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestInventoryError {

	static InventoryService serv;
	static long idToKeep=351;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 serv = new InventoryService();
	}
	
	// Delete the DB files
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
	// name is a not null attribute
	public void saveWithoutNameItem(){
		boolean gotIt=false;
		Item ie= new Item();
		ie.setDescription("wrong item without name");
		try {
			ie=serv.newItem(ie);
		} catch (DALException e) {
			Assert.assertTrue("ERRDAO1000".equals(e.getFaultInfo().getCode()));
			System.out.println(e.getFaultInfo().getMessage());
			gotIt=true;
		}
		if (!gotIt) {
			Assert.fail("Should have an exception reported");
		}
	}
	
	
	@Test
	public void testGetWrongId(){
		boolean gotIt=false;
		try {
			Item item = serv.getItemById(-1);
		} catch (DALException e) {
			Assert.assertTrue("ERRDAO3001".equals(e.getFaultInfo().getCode()));
			System.out.println(e.getFaultInfo().getMessage());
			gotIt=true;
		}
		if (!gotIt) {
			Assert.fail("Should have an exception reported");
		}
	}
	
	@Test
	public void testGetWrongName(){
		try {
			Item item = serv.getItemByName("wrong product name");
			Assert.assertNull(item);
		} catch (DALException e) {
			Assert.fail("Should not have an exception reported");
		}
	}
	
	@Test
	public void testUpdateWithoutName(){
		boolean gotIt=false;
		try {
			Item ie= new Item();
			Item item = serv.updateItem(ie);
		} catch (DALException e) {
			Assert.assertTrue("ERRDAO2000".equals(e.getFaultInfo().getCode()));
			System.out.println(e.getFaultInfo().getMessage());
			gotIt=true;
		}
		if (!gotIt) {
			Assert.fail("Should have an exception reported");
		}
	}
	

	@Test
	public void testUpdateWithoutId(){
		boolean gotIt=false;
		try {
			Item ie= new Item("Name is good");
			Item item = serv.updateItem(ie);
		} catch (DALException e) {
			Assert.assertTrue("ERRDAO1004".equals(e.getFaultInfo().getCode()));
			System.out.println(e.getFaultInfo().getMessage());
			gotIt=true;
		}
		if (!gotIt) {
			Assert.fail("Should have an exception reported");
		}
	}
	
	@Test
	public void deleteWithoutId(){
		boolean gotIt=false;
		try {
			serv.deleteItem(0);
		} catch (DALException e) {
			Assert.assertTrue("ERRDAO4001".equals(e.getFaultInfo().getCode()));
			System.out.println(e.getFaultInfo().getMessage());
			gotIt=true;
		}
		if (!gotIt) {
			Assert.fail("Should have an exception reported");
		}
	}
	
}
