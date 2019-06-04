package mainProgram;

import java.io.File;

public class ChangeFileType {

	public String toString() {

		File directory = new File("C:\\Users\\ralph\\Desktop\\tfol");
		String inputFile = null;

		// get all the files from a directory
		File[] fList = directory.listFiles();

		//find files ending in .cermxml
		for (File file : fList) {
			if (file.isFile()) {
				if (file.getName().endsWith(".cermxml")) {
					inputFile = file.getAbsolutePath();}
				//get path name to file ending in .cermxml

			}//end if inner	   

		}//end for

		return inputFile;

	}//end toString


}//end class
