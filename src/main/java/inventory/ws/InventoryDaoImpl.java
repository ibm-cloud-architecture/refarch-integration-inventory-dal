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
	public List<ItemEntity> getItems() {
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

	@Override
	public ItemEntity updateItem(ItemEntity inItem) {
		merge(inItem);
		return inItem;
	}

	@Override
	public ItemEntity getItemEntityById(long id) {
		EntityManager em = getEntityManager();
		ItemEntity entity=em.find(ItemEntity.class, id);
		em.close();
		return entity;
	}

	@Override
	public String addItem(ItemEntity ie) {
		return save(ie);
	}

	@Override
	public String deleteItem(long id){
		try {
			 EntityManager em = getEntityManager();
			 em.getTransaction().begin();
			 ItemEntity entity=em.find(ItemEntity.class, id);
			 logger.info("Removing entity "+entity.getId()+" "+entity.getName());
			 em.remove(entity);
			 em.getTransaction().commit();
			 em.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "Failure: "+e.getMessage();
		}
		 return "Success";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemEntity> getItemByName(String name) {
		EntityManager em = getEntityManager();
		List<ItemEntity> results = new ArrayList<ItemEntity>();
		try{ 
			Query query =em.createQuery("select p from Item p where p.name = ?1",ItemEntity.class);
			query.setParameter (1, name);
			results = query.getResultList();
		} finally {
			em.close();
		}
		return results;
	}
}
