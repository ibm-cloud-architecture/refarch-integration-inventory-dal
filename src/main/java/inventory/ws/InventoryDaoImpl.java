package inventory.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import inventory.model.ItemEntity;

public class InventoryDaoImpl extends BaseDao implements InventoryDAO {
	Logger logger = Logger.getLogger(InventoryDaoImpl.class.getName());
	
	public InventoryDaoImpl(){
		
	}
	
	public InventoryDaoImpl(String persistanceName) {
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemEntity> getItems() throws DALException{
		EntityManager em = getEntityManager();
		List<ItemEntity> results = new ArrayList<ItemEntity>();
		try{ 
			Query query =em.createNamedQuery("Item.findAll");
			results = query.getResultList ();
		
		} finally {
			em.close();
		}
		return results;
	}

	public boolean validName(ItemEntity ie){
		if (ie.getName() == null || ie.getName().length() == 0) return false;
		return true;
	}
	
	public boolean validId(ItemEntity ie){
		if (ie.getId() == null) return false;
		return true;
	}
	
	@Override
	public String createItem(ItemEntity ie) throws DALException {
		if (!validName(ie)){
			throw new DALException("ERRDAO1000","Mandatory name attribute needs to be present in the item");
		}
		return save(ie);
	}
	
	@Override
	public ItemEntity updateItem(ItemEntity inItem) throws DALException {
		if (!validName(inItem)){
			throw new DALException("ERRDAO2000","Mandatory name attribute needs to be present");
		}
		if (!validId(inItem)){
			throw new DALException("ERRDAO2001","On update operation id attribute needs to be present");
		}
		merge(inItem);
		return inItem;
	}

	@Override
	public ItemEntity getItemEntityById(long id) throws DALException {
		if (id <= 0) {
			DALFault f = new DALFault("ERRDAO3001","Item identifier negative or 0");
			throw new DALException("DAL exception input data", f);
		}
		EntityManager em = getEntityManager();
		ItemEntity entity=em.find(ItemEntity.class, id);
		em.close();
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ItemEntity getItemEntityByName(String name) throws DALException {
		if (name == null || name.isEmpty()) {
			DALFault f = new DALFault("ERRDAO3002","Item name is empty");
			throw new DALException("DAL exception input data", f);
		}
		EntityManager em = getEntityManager();
		List<ItemEntity> l=null;
		try{ 
			Query query =em.createQuery("select p from Item p where p.name = ?1",ItemEntity.class);
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
	public String deleteItem(long id) throws DALException{
		ItemEntity entity=null;
		 EntityManager em =null;
		try {
			 em = begin();
			 entity=em.find(ItemEntity.class, id);
			 if (entity != null) {
					logger.info("Removing entity "+entity.getId()+" "+entity.getName());
					 em.remove(entity);
					 em.getTransaction().commit();
				} 
		}catch (Exception e){
			e.printStackTrace();
			 throw new DALException("ERRDAO4000","Error on delete delete");
		} finally {
			if (em != null) {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
				em.close();
			}
		}
		if (entity == null) {
			 throw new DALException("ERRDAO4001","Error on delete delete");
		}
		
		 return "Success";
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemEntity> searchItemEntitiesByName(String name) {
		EntityManager em = getEntityManager();
		List<ItemEntity> results = null;
		try{ 
			Query query =em.createQuery("select p from Item p where p.name LIKE ?1",ItemEntity.class);
			query.setParameter (1, name+"%");
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
