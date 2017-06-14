package dal.ut;

import java.io.File;

import org.junit.AfterClass;

public class BaseTest {

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

		@AfterClass
		public static void tearDownAfterClass() throws Exception {
			deleteDir(new File("./invdb"));
		}
}
