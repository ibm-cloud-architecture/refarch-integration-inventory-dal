package inventory.ws;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class InventoryPersistenceManager {
	// EntityManagerFactory instances are heavy weight objects. 
	// Each factory might maintain a metadata cache, object state cache, EntityManager pool, connection pool...
	protected EntityManagerFactory emf;
	private static final InventoryPersistenceManager singleton = new InventoryPersistenceManager();
	public static final boolean DEBUG = true;
	 
	public static InventoryPersistenceManager getInstance() {   
	    return singleton;
	}
	  
	private InventoryPersistenceManager() {
	}
	 
	
	public EntityManagerFactory getEntityManagerFactory() {    
	    if (emf == null)
	      synchronized (singleton) {
	    	  createEntityManagerFactory();
		}
	    return emf;
	}
	  
	public void closeEntityManagerFactory() {	    
	    if (emf != null) {
	      emf.close();
	      emf = null;
	      if (DEBUG)
	          System.out.println("@@@@ Persistence manager factory finished at " + new java.util.Date());
	    }
	}
	  
	protected void createEntityManagerFactory() {	    
	   emf = Persistence.createEntityManagerFactory("inventory");
	   if (DEBUG)
		      System.out.println("@@@@ Persistence manager factory started at " + new java.util.Date());
	}
}
