package dal.ut;

import java.io.File;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import inventory.dao.InventoryPersistenceManager;
import inventory.ws.DALService;

public class BaseTest {
	static DALService dalWebService;
	static Properties jdbcProperties = new Properties();

	@BeforeClass
	public static void initPersistenceFactory() {
		jdbcProperties.setProperty("javax.persistence.jdbc.driver", "org.apache.derby.jdbc.EmbeddedDriver");
		jdbcProperties.setProperty("javax.persistence.jdbc.url", "jdbc:derby:invdb;create=true");
		jdbcProperties.setProperty("javax.persistence.jdbc.user", "user");
		jdbcProperties.setProperty("javax.persistence.jdbc.password", "user");
		jdbcProperties.setProperty("javax.persistence.jdbc.url", "jdbc:derby:invdb;create=true");
		jdbcProperties.setProperty("javax.persistence.jdbc.user", "user");
		jdbcProperties.setProperty("javax.persistence.jdbc.password", "user");
		jdbcProperties.setProperty("openjpa.Log", "SQL=TRACE");
		jdbcProperties.setProperty("openjpa.jdbc.SynchronizeMappings", "buildSchema");
		jdbcProperties.setProperty("javax.persistence.jdbc.Schema", "APP");
		jdbcProperties.setProperty("openjpa.DataCache", "true");
		jdbcProperties.setProperty("openjpa.RemoteCommitProvider", "sjvm");
		jdbcProperties.setProperty("openjpa.DynamicEnhancementAgent", "true");
		jdbcProperties.setProperty("openjpa.ConnectionFactoryProperties",
				"PrettyPrint=true, PrettyPrintLineLength=80, PrintParameters=True");
		EntityManagerFactory emfTest = Persistence.createEntityManagerFactory("inventory", jdbcProperties);
		InventoryPersistenceManager.getInstance().setEntityManagerFactory(emfTest);
	}

	// Delete the DB files
	static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDir(f);
			}
		}
		file.delete();
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dalWebService = new DALService();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		deleteDir(new File("./invdb"));
	}
}
