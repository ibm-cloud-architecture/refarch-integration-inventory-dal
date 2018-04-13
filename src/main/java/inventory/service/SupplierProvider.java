package inventory.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import inventory.dao.SupplierDAO;
import inventory.dao.SupplierDAOImpl;
import inventory.model.ItemEntity;
import inventory.model.SupplierEntity;
import inventory.ws.DALException;
import inventory.ws.dto.Supplier;
/**
 * A supplier provider class manages the persistence and any service orchestration as defined by 
 * the business requirements. It uses DAO to call the persistence layer.
 *  
 * @author Jerome Boyer
 *
 */
public class SupplierProvider {

	private SupplierDAO supplierDao;
	
	public SupplierProvider() {
	 	this.supplierDao = new SupplierDAOImpl();	
	}
	
	public Supplier newSupplier(Supplier s) throws DALException{
		SupplierEntity se = new SupplierEntity(s);
		se.setUpdateDate(new Date());
		se.setCreationDate(se.getUpdateDate());
		SupplierEntity se2= supplierDao.saveSupplier(se);
		return new Supplier(se2);
	}
	
	public Supplier getSupplierById(long supplierId)  throws DALException {
		SupplierEntity se = supplierDao.getById(supplierId);
		return new Supplier(se);
	}

	public Supplier getSupplierByName(String name) throws DALException {
		return new Supplier( supplierDao.getByName(name));
	}

	public Collection<Supplier> getSuppliers() throws DALException {		
		Collection<SupplierEntity> sel=supplierDao.getSuppliers();
		List<Supplier> sl=new ArrayList<Supplier>();
		for (SupplierEntity se : sel){
			Supplier s = new Supplier(se);
			sl.add(s);
		}
		return sl;
	}
	
	public Supplier updateSupplier(Supplier s) throws DALException{
		SupplierEntity se = new SupplierEntity(s);
		se.setUpdateDate(new Date());
		supplierDao.updateSupplier(se);
		return s;
	}
	
	public String deleteSupplier(long id) throws DALException {
		return supplierDao.deleteSupplier(id);
	}

	public ItemEntity newProduct(ItemEntity ie) {
		// TODO Auto-generated method stub
		return null;
	}
}
