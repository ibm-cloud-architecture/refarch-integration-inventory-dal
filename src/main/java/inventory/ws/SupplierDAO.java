package inventory.ws;

import java.util.Collection;

import inventory.model.Supplier;

public interface SupplierDAO {

	Supplier saveSupplier(Supplier s) throws DALException ;

	Supplier getByName(String name) throws DALException ;

	Supplier updateSupplier(Supplier s) throws DALException ;
	
	String deleteSupplier(long id) throws DALException ;
	
	Collection<Supplier> getSuppliers() throws DALException ;

	Supplier getById(long supplierId) throws DALException;
}
