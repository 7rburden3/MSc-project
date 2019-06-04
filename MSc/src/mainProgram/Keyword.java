package mainProgram;

import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Keyword {
	
	public Keyword() throws IOException {
		
		String output;
		ChangeFileType ctf = new ChangeFileType();  //call ChangeFileType to get path name to .cermxml file
		output = ctf.toString();
		
		try {
			PrintStream out = new PrintStream(new FileOutputStream("C:\\Users\\ralph\\neo4j-community-3.4.6-windows\\neo4j-community-3.4.6\\import\\outputKey.csv"));
			System.setOut(out);
			File inputFile = new File(output);	         
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();	         
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);	 
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();	         
			NodeList nList = doc.getElementsByTagName("kwd");
			NodeList titleList = doc.getElementsByTagName("front");

			if (nList.getLength() > 0) {

				for (int temp = 0; temp < titleList.getLength(); temp++) {
					Node nNode = titleList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						String title = eElement
								.getElementsByTagName("article-title")
								.item(0)
								.getTextContent();
						out.print("Paper,");
						for (int loop = 0; loop < nList.getLength(); loop++) {
							out.print(title + ",");
						}
					}
				}out.println();


				out.print("Keyword,");

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Element eElement = (Element) nList.item(temp);
					NodeList innernodes = eElement.getChildNodes();	               
					out.print(innernodes.item(0).getNodeValue() + ",");

				} out.close();

			} else {
				
				out.println("Paper,");
				out.println("Keyword,");

			}
//


		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new RotateKeyCSV();
	}

}
