package dal.ut;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import inventory.model.ItemEntity;
import inventory.model.SupplierEntity;
import inventory.ws.DALException;
import inventory.ws.Item;
import inventory.ws.SupplierDAO;
import inventory.ws.SupplierDAOImpl;

public class TestSupplierItems extends BaseTest{

	static long supplierId=0;
	SupplierDAO dao = new SupplierDAOImpl();
	
	/*
	 * This test plays with different entry points: DAO or service. The objects are not the same: some are DTO some are entities
	 */
	@Test
	public void mainTest() {
		System.out.println("STEP 1 - Add 2 suppliers");
		SupplierEntity s = new SupplierEntity();
		s.setName("TestSupplier1");
		s.setStatus("New");
		s.setState("CA");
		s.setCity("San Francisco");
		s.setStreet("10 first street");
		s.setZipcode("90000");
		s.setType("ORGANIZATION");
		SupplierEntity sOut=null;
		try {
			s = dao.saveSupplier(s);
			supplierId=s.getId();
		} catch (DALException e) {
			e.printStackTrace();
			Assert.fail("Exception in save new supplier");
		}
		Assert.assertNotNull(s);
		Assert.assertTrue(s.getId()>0);
		
		SupplierEntity s2 = new SupplierEntity();
		s2.setName("TestSupplier2");
		s2.setStatus("New");
		s2.setState("CA");
		s2.setCity("San Francisco");
		s2.setStreet("11 Market street");
		s2.setZipcode("90000");
		s.setType("ORGANIZATION");
		try {
			sOut =  dao.saveSupplier(s2);
		} catch (DALException e) {
			e.printStackTrace();
			Assert.fail("Exception in save new supplier");
		}
		
		System.out.println("STEP 2 - Add 3 items to supplier 1 and update it");
		for (int i = 0; i<3;i++) {
			ItemEntity ie= new ItemEntity("item_"+i);
			ie.setDescription("This is item_"+i);
			ie.setPrice(1000+i);
			ie.setCreationDate(new Date());
			s.addItemEntity(ie);
		}
		try {
			sOut=dao.updateSupplier(s);
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception while updating supplier");
		}
		System.out.println("STEP 3 - now 3 new items where created");
		Assert.assertTrue(sOut.getItems().size()== 3);
		try {
			Item ie=serv.getItemByName("item_2");
			System.out.println(ie.getId()+" "+ie.getPrice());
		} catch (DALException e) {
			e.printStackTrace();
		}
		
	}

}
