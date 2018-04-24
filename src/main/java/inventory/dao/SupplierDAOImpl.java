package inventory.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import inventory.model.SupplierEntity;
import inventory.ws.DALException;
import inventory.ws.DALFault;

public class SupplierDAOImpl extends BaseDao implements SupplierDAO {
	
	public boolean validName(SupplierEntity ie){
		if (ie.getName() == null || ie.getName().length() == 0) return false;
		return true;
	}
	
	public boolean validId(SupplierEntity ie){
		if (ie.getId() == null) return false;
		return true;
	}

	@Override
	public SupplierEntity saveSupplier(SupplierEntity s) throws DALException {
		if (!validName(s)){
			throw new DALException("ERRDAO3000","Supplier name attribute is mandatory");
		}
		return (SupplierEntity)save(s);
	}

	@Override
	public SupplierEntity getByName(String name) throws DALException  {
		if (name == null || name.isEmpty()) {
			DALFault f = new DALFault("ERRDAO3012","Supplier name is empty");
			throw new DALException("DAL exception input data", f);
		}
		EntityManager em = getEntityManager();
		List<SupplierEntity> l=null;
		try{ 
			Query query =em.createQuery("select p from Supplier p where p.name = ?1",SupplierEntity.class);
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

	@Override
	public SupplierEntity updateSupplier(SupplierEntity s) throws DALException {
		if (!validName(s)){
			throw new DALException("ERRDAO5000","Mandatory name attribute needs to be present");
		}
		if (!validId(s)){
			throw new DALException("ERRDAO5001","On update operation id attribute needs to be present");
		}
		merge(s);
		return s;
	}

	@Override
	public String deleteSupplier(long id) throws DALException {
		SupplierEntity entity=null;
		EntityManager em =null;
		try {
			 em = begin();
			 entity=em.find(SupplierEntity.class, id);
			 if (entity != null) {
					logger.info("Removing entity "+entity.getId()+" "+entity.getName());
					 em.remove(entity);
					 em.getTransaction().commit();
				} 
		}catch (Exception e){
			e.printStackTrace();
			 throw new DALException("ERRDAO5000","Error on delete operation at tx level id="+id);
		} finally {
			if (em != null) {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
				em.close();
			}
		}
		if (entity == null) {
			 throw new DALException("ERRDAO5001","Error on delete, entity not found for id="+id);
		}
		
		 return "Success";
	}

	@Override
	public Collection<SupplierEntity> getSuppliers() throws DALException {
		EntityManager em = getEntityManager();
		List<SupplierEntity> results = new ArrayList<SupplierEntity>();
		try{ 
			Query query =em.createNamedQuery("Supplier.findAll");
			results = query.getResultList ();
		
		} finally {
			em.close();
		}
		return results;
	}

	@Override
	public SupplierEntity getById(long supplierId) throws DALException{
		if (supplierId <= 0) {
			DALFault f = new DALFault("ERRDAO4001","Supplier identifier negative or 0");
			throw new DALException("DAL exception input data", f);
		}
		EntityManager em = getEntityManager();
		SupplierEntity entity=em.find(SupplierEntity.class, supplierId);
		em.close();
		return entity;
	}

}
