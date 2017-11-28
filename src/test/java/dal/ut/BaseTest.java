package dal.ut;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import inventory.ws.DALService;

public class BaseTest {
	static DALService serv;
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
			 serv = new DALService();
		}

		@AfterClass
		public static void tearDownAfterClass() throws Exception {
			deleteDir(new File("./invdb"));
		}
}
