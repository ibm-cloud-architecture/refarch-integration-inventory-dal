package inventory.ws;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;


public abstract class BaseDao {
	Logger logger = Logger.getLogger("BaseDao");
	
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

	public Object merge(Object entity) throws DALException{
		EntityManager em=null;
		try{
			em = begin();
			em.merge(entity);
			em.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DALException("ERRDAO1004","Error in merge operation "+e.getMessage());
		} finally {
			try {
				if (em != null) {
					if (em.getTransaction().isActive()) {
						em.getTransaction().rollback();
					}
					em.close();
				}
			} catch(Exception e){
				e.printStackTrace();
				throw new DALException("ERRDAO1005","Error in merge-close operation "+e.getMessage());
			}
		}
		 return entity;
    }
	

	
	public Object save(Object entity) throws DALException{
		EntityManager em=null;
    	try {
			 em = begin();
			 em.persist(entity);
			 em.getTransaction().commit();		 
		} catch (Exception e) {
			e.printStackTrace();
			throw new DALException("ERRDAO1001","Error in save operation "+e.getMessage());
		} 
    	finally {
			if (em != null) em.close();
		}
		 return entity;
    }
	
	public String delete(Object entity) throws DALException{
		EntityManager em=null;
		try {
			 em = begin();
			 em.remove(entity);
			 em.getTransaction().commit();
			 em.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DALException("ERRDAO1006","Error in delete operation "+e.getMessage());
		} finally {
			try {
				if (em != null) {
					if (em.getTransaction().isActive()) {
						em.getTransaction().rollback();
					}
					em.close();
				}
			} catch(Exception e){
				e.printStackTrace();
				throw new DALException("ERRDAO1007","Error in delete-close operation "+e.getMessage());
			}
		}
		 return "Success";
	}
}
