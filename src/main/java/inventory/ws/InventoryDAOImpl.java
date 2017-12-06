package inventory.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import inventory.model.Inventory;

public class InventoryDAOImpl extends BaseDao implements InventoryDAO {
	Logger logger = Logger.getLogger(InventoryDAOImpl.class.getName());
	
	public InventoryDAOImpl(){
		
	}
	
	public InventoryDAOImpl(String persistanceName) {
		
	}
	
	
	public boolean validId(Inventory ie){
		if (ie.getId() == null) return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Inventory> getItemsPerSite(String siteName) {
		EntityManager em = getEntityManager();
		List<Inventory> results = null;
		try{ 
			Query query =em.createQuery("select p from Inventory p where p.site LIKE ?1",Inventory.class);
			query.setParameter (1, siteName+"%");
			results=query.getResultList();
		} finally {
			if (em != null) {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
				em.close();
			}
		}
		return results;
	} // getItemsPerSite

	@Override
	public Inventory createInventoryEntry(Inventory iv) throws DALException {
		return (Inventory)save(iv);
	}

	@Override
	public Inventory getInventoryById(long id) throws DALException{
		if (id <= 0) {
			DALFault f = new DALFault("ERRDAO3005","Inventory identifier negative or 0");
			throw new DALException("DAL exception input data", f);
		}
		EntityManager em = getEntityManager();
		Inventory entity=em.find(Inventory.class, id);
		em.close();
		return entity;
	}

	@Override
	public Inventory updateInventoryEntry(Inventory iv) throws DALException {
		merge(iv);
		return iv;
	}

	@Override
	public String deleteInventoryEntry(long id) throws DALException {
		Inventory entity=null;
		 EntityManager em =null;
		try {
			 em = begin();
			 entity=em.find(Inventory.class, id);
			 if (entity != null) {
					logger.info("Removing entity "+entity.getId());
					 em.remove(entity);
					 em.getTransaction().commit();
				} 
		}catch (Exception e){
			e.printStackTrace();
			 throw new DALException("ERRDAO4002","Error on delete operation at tx level id="+id);
		} finally {
			if (em != null) {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
				em.close();
			}
		}
		if (entity == null) {
			 throw new DALException("ERRDAO4003","Error on delete, entity not found for id="+id);
		}
		 return "Success";
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public Inventory getInventoryForSiteAndItemId(long itemId, String siteName) {
		EntityManager em = getEntityManager();
		List<Inventory> results = null;
		try{ 
			Query query =em.createQuery("select p from Inventory p where p.itemId = ?1 and p.site like ?2",Inventory.class);
			query.setParameter (2, siteName+"%");
			query.setParameter (1, new Long(itemId));
			results=query.getResultList();
		} finally {
			if (em != null) {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
				em.close();
			}
		}
		if (results.isEmpty()) return null;
		return results.get(0);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Inventory> getInventoryCrossSite() {
		EntityManager em = getEntityManager();
		List<Inventory> results = new ArrayList<Inventory>();
		try{ 
			Query query =em.createNamedQuery("Inventory.findAll");
			results = query.getResultList ();
		
		} finally {
			em.close();
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Inventory> getSiteInventoryByItemId(long itemId) throws DALException{
		EntityManager em = getEntityManager();
		List<Inventory> results = null;
		try{ 
			Query query =em.createQuery("select p from Inventory p where p.itemId = ?1",Inventory.class);
			query.setParameter (1, itemId);
			results=query.getResultList();
		} finally {
			if (em != null) {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
				em.close();
			}
		}
		return results;	
	}
	
	@Override
	public Collection<Inventory> getInventoryPerSupplier(Long supplierId) throws DALException {
		EntityManager em = getEntityManager();
		List<Inventory> results = null;
		try{ 
			Query query =em.createQuery("select p from Inventory p where p.supplierId = ?1",Inventory.class);
			query.setParameter (1, supplierId);
			results=query.getResultList();
		} finally {
			if (em != null) {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
				em.close();
			}
		}
		return results;	
	}
	
}
