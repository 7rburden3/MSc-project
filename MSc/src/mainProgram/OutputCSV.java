package mainProgram;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OutputCSV {

	public OutputCSV() {

		String output;
		String strAuthor;
		ChangeFileType ctf = new ChangeFileType(); //call ChangeFileType to get path name to .cermxml file
		output = ctf.toString();


		try {
			PrintStream out = new PrintStream(new FileOutputStream("C:\\Users\\ralph\\neo4j-community-3.4.6-windows\\neo4j-community-3.4.6\\import\\outputAuthTitle.csv"));
			//System.setOut(out);
			File inputFile = new File(output);	         
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();	         
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);	 
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();	         
			NodeList nList = doc.getElementsByTagName("contrib");
			NodeList titleList = doc.getElementsByTagName("front");

			out.print("Author,");
			int count = 0;
			char toCount = ',';
			
			if (nList.getLength() > 0) {
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				
				

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					strAuthor = 
							eElement
							.getElementsByTagName("string-name")
							.item(0)
							.getTextContent();
					
					String author = (strAuthor + ",").replaceAll(",,", ",").replaceAll(", ", ",");
					out.print(author.trim());
					for (int loop = 0; loop < author.length(); loop++) {
						if (author.charAt(loop) == toCount) {
							count++;
						}
					}
					
				}	
				
			}out.println();
			
			} else {
				out.println("Unknown,");
				count++;
				
			}//out.println();
			

			for(int temp = 0; temp < titleList.getLength(); temp++) {

				Node nNode = titleList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					String title = eElement
							.getElementsByTagName("article-title")
							.item(0)
							.getTextContent();
					out.print("Paper,");
					for (int loop = 0; loop < count; loop++) {
						out.print(title + ",");
					}
				}

			}out.println();

			for(int temp = 0; temp < titleList.getLength(); temp++) {

				Node nNode = titleList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					if (eElement.getElementsByTagName("year").getLength() > 0) {
						String year = eElement
								.getElementsByTagName("year")
								.item(0)
								.getTextContent();
						out.print("Published_Year,");
						for (int loop = 0; loop < count; loop++) {

							out.print(year + ",");
						}
					} else {
						out.print("Published_Year,");
						for (int loop = 0; loop < count; loop++) {

							out.print(0 + ",");
						}
					}

				} out.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
