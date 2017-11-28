package dal.ut;

import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import inventory.model.PartyType;
import inventory.model.Supplier;
import inventory.ws.DALException;
import inventory.ws.DALService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSuppliers extends BaseTest{
	static DALService serv;
	static long supplierId=0;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 serv = new DALService();
	}


	@Test
	public void testCreateNonameSupplier() {
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
	public void testCreateSupplier() {
		Supplier s = new Supplier();
		s.setName("TestSupplier");
		s.setStatus("New");
		s.setState("CA");
		s.setCity("San Francisco");
		s.setStreet("10 first street");
		s.setZipcode("90000");
		s.setType(PartyType.ORGANIZATION);
		Supplier sOut=null;
		try {
			sOut = serv.newSupplier(s);
			supplierId=sOut.getId();
		} catch (DALException e) {
			e.printStackTrace();
			Assert.fail("Exception in save new supplier");
		}
		Assert.assertNotNull(sOut);
		Assert.assertTrue(sOut.getId()>0);
		Assert.assertTrue(s.getName().equals(sOut.getName()));
		
	}

	@Test
	public void testReadSupplierById(){
		System.out.println("@@@ testReadSupplierById");
		Supplier sOut=null;
		try {
			sOut = serv.getSupplierById(supplierId);
		} catch (DALException e) {
			e.printStackTrace();
			Assert.fail("Exception in load supplier");
		}
		Assert.assertNotNull(sOut);
		System.out.println(sOut.toString());
		Assert.assertTrue(sOut.getId()==supplierId);
	}
	
	@Test
	public void testReadSupplierByName(){
		System.out.println("@@@ testReadSupplierByName");
		Supplier sOut=null;
		try {
			sOut = serv.getSupplierByName("TestSupplier");
		} catch (DALException e) {
			e.printStackTrace();
			Assert.fail("Exception in load supplier");
		}
		Assert.assertNotNull(sOut);
		System.out.println(sOut.toString());
		Assert.assertTrue(sOut.getId()==supplierId);
	}
	
	@Test
	public void testUpdateSupplier(){
		Supplier sOut=null;
		try {
			Supplier s = serv.getSupplierById(supplierId);
			s.setStatus("Accepted");
			sOut=serv.updateSupplier(s);
		} catch (DALException e) {
			e.printStackTrace();
			Assert.fail("Exception in load supplier");
		}
		Assert.assertNotNull(sOut);
		Assert.assertTrue("Accepted".equals(sOut.getStatus()));
	}
	
	@Test
	public void testGetSuppliers(){
		Collection<Supplier> sOut=null;
		try {
			sOut = serv.getSuppliers();
		} catch (DALException e) {
			e.printStackTrace();
			Assert.fail("Exception in load supplier");
		}
		Assert.assertNotNull(sOut);
		Assert.assertTrue(sOut.size()>0);
	}
	

	
	@Test
	public void testWDeleteSupplier(){
		String s=null;
		try {
			s = serv.deleteSupplier(supplierId);
		} catch (DALException e) {
			e.printStackTrace();
			Assert.fail("Exception in load supplier");
		}
		Assert.assertTrue("Success".equals(s));
	}
}
