package mainProgram;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

public class RotateCiteCSV {
	
	public RotateCiteCSV() throws IOException {
		
		String row;
		String inputFile = "C:\\Users\\ralph\\neo4j-community-3.4.6-windows\\neo4j-community-3.4.6\\import\\outputCite.csv";
		PrintStream out = new PrintStream (new FileOutputStream("C:\\Users\\ralph\\neo4j-community-3.4.6-windows\\neo4j-community-3.4.6\\import\\inputCite.csv"));
		System.setOut(out);
		FileInputStream fis = new FileInputStream(inputFile);
		BufferedReader myInput = new BufferedReader(new InputStreamReader(fis));

		int line = 0;//line count of csv
		String[][] data = new String[0][];//csv data line count=0 initially
		while ((row = myInput.readLine()) != null) {
		    line++;//increment the line count when new line found

		    String[][] newData = new String[line][2];//create new array for data

		    String strarray[] = row.split(",");//get contents of line as an array
		    newData[line - 1] = strarray;//add new line to the array

		    System.arraycopy(data, 0, newData, 0, line - 1);//copy previously read values to new array
		    data = newData;//set new array as csv data
		}
		fis.close();
		
		String[][] matrixCW = rotateCW(data);
	    printMatrix(matrixCW);
		}
		
	    static void printMatrix(String[][] matrix) {		    
		    for (String[] row : matrix) {
		        System.out.println(Arrays.toString(row).replace("[","").replace("]","").replaceAll(", ", ","));
		        //regex to remove [] and whitespace after , to align values correctly in excel cells
		    }
		}
	    
	    static String[][] rotateCW(String[][] matrix) {
		    final int M = matrix.length;
		    final int N = matrix[0].length;
		    String[][] rotate = new String[N][M];
		    for (int row = 0; row < M; row++) {
		        for (int col = 0; col < N; col++) {
		            rotate[col][M-1-row] = matrix[row][col];
		        }
		    }
		    return rotate;
	}

}
