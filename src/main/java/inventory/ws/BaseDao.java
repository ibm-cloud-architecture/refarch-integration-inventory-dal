package inventory.ws;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public abstract class BaseDao {

	@PersistenceContext(unitName = "inventory")
	protected EntityManager em;
	 /**
	  *  An EntityManager is not a heavy object.
	  *  It's not safe to traverse lazy-loaded relationships once the EntityManager is closed
	  * @return
	  */
	protected EntityManager getEntityManager(){
            if (em == null || !em.isOpen()) {
                em = InventoryPersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
                System.out.println("@@@@ In JSE the em is not injected");
            }
            return em;
	}
	
	
    protected EntityManager begin() {
          EntityManager em = getEntityManager();
          if (!em.getTransaction().isActive()) {
                 em.getTransaction().begin();
          }
          return em;
    }

	public String merge(Object entity) {
    	try {
			 EntityManager em = begin();
			 em.merge(entity);
			 em.getTransaction().commit();
			 em.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "Failure: "+e.getMessage();
		}
		 return "Success";
    }
	

	public String save(Object entity) {
		 EntityManager em=null;
    	try {
			 em = begin();
			 em.persist(entity);
			 em.getTransaction().commit();
			 
		} catch (Exception e) {
			e.printStackTrace();
			return "Failure: "+e.getMessage();
		} finally {
			if (em != null) em.close();
		}
		 return "Success";
    }
	
	public String delete(Object entity){
		try {
			 EntityManager em = begin();
			 em.remove(entity);
			 em.getTransaction().commit();
			 em.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "Failure: "+e.getMessage();
		} finally {
		    if (em.getTransaction().isActive()) em.getTransaction().rollback();
		  }
		 return "Success";
	}
}
