package dal.ut;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import inventory.model.Supplier;
import inventory.ws.DALException;
import inventory.ws.InventoryService;


public class TestSuppliers extends BaseTest{
	static InventoryService serv;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 serv = new InventoryService();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testSaveNonameSupplier() {
		boolean gotIt=false;
		Supplier s = new Supplier();
		Supplier sOut=null;
		try {
			sOut = serv.newSupplier(s);
		} catch (DALException e) {
			gotIt=true;
		}
		if (!gotIt) {
			Assert.fail("Should have an exception reported");
		}
		
	}
	
	@Test
	public void testSaveSupplier() {
		Supplier s = new Supplier();
		s.setName("TestSupplier");
		s.setStatus("New");
		s.setState("CA");
		s.setCity("San Francisco");
		s.setStreet("10 first street");
		s.setZipcode("90000");
		Supplier sOut=null;
		try {
			sOut = serv.newSupplier(s);
		} catch (DALException e) {
			e.printStackTrace();
			Assert.fail("Exception in save new supplier");
		}
		Assert.assertNotNull(sOut);
		Assert.assertTrue(sOut.getId()>0);
		Assert.assertTrue(s.getName().equals(sOut.getName()));
		
	}

}
