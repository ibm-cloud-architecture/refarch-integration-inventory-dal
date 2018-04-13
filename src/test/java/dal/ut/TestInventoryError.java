package dal.ut;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import inventory.model.ItemEntity;
import inventory.service.InventoryProvider;
import inventory.ws.DALException;
import inventory.ws.dto.Item;

/**
 * Test at the service level the basic CRUD operation on inventory item.
 * The persistence.xml uses derby embedded for testing purpose so testcases
 * can delete the DB at the end of their unit tests
 * @author jerome boyer
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestInventoryError extends BaseTest{


	static long idToKeep=351;
	static InventoryProvider invProvider = new InventoryProvider();
	
	@Test
	// name is a not null attribute
	public void saveWithoutNameItem(){
		boolean gotIt=false;
		ItemEntity ie= new ItemEntity();
		ie.setDescription("wrong item without name");
		try {
			ie=invProvider.newItem(ie);
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
			ItemEntity item = invProvider.getItemById(-1);
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
			ItemEntity item = invProvider.getItemByName("wrong product name");
			Assert.assertNull(item);
		} catch (DALException e) {
			Assert.fail("Should not have an exception reported");
		}
	}
	
	@Test
	public void testUpdateWithoutName(){
		boolean gotIt=false;
		try {
			ItemEntity ie= new ItemEntity();
			ItemEntity item = invProvider.updateItem(ie);
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
			ItemEntity ie= new ItemEntity("Name of a good");
			ItemEntity item = invProvider.updateItem(ie);
		} catch (DALException e) {
			Assert.assertTrue("ERRDAO2001".equals(e.getFaultInfo().getCode()));
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
			invProvider.deleteItem(0);
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
