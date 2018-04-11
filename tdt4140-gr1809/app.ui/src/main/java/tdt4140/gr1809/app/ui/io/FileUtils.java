package tdt4140.gr1809.app.ui.io;

import java.io.File;
import java.io.PrintWriter;

import tdt4140.gr1809.app.core.model.User;

public class FileUtils {
	
	public static void writeObjectToFile(final Object object, final String fileName) {
		try {
			PrintWriter writer = new PrintWriter(new File(fileName));
			writer.println(User.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
