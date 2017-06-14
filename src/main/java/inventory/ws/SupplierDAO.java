package inventory.ws;

import inventory.model.Supplier;

public interface SupplierDAO {

	String saveSupplier(Supplier s) throws DALException ;

	Supplier getByName(String name) throws DALException ;

}
