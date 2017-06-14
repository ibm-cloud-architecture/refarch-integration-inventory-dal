package inventory.ws;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import inventory.model.Supplier;

public class SupplierDAOImpl extends BaseDao implements SupplierDAO {
	
	public boolean validName(Supplier ie){
		if (ie.getName() == null || ie.getName().length() == 0) return false;
		return true;
	}

	@Override
	public String saveSupplier(Supplier s) throws DALException {
		if (!validName(s)){
			throw new DALException("ERRDAO3000","Supplier name attribute is mandatory");
		}
		return save(s);
	}

	@Override
	public Supplier getByName(String name) throws DALException  {
		if (name == null || name.isEmpty()) {
			DALFault f = new DALFault("ERRDAO3012","Supplier name is empty");
			throw new DALException("DAL exception input data", f);
		}
		EntityManager em = getEntityManager();
		List<Supplier> l=null;
		try{ 
			Query query =em.createQuery("select p from Supplier p where p.name = ?1",Supplier.class);
			query.setParameter (1, name);
			l=query.getResultList();
		} finally {
			if (em != null) {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
				em.close();
			}
		}
		if (l != null && ! l.isEmpty()) 
			return l.get(0);
		return null;
	}

}
