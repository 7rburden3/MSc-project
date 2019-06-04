package mainProgram;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class CitedAuthPaper {
	
	public CitedAuthPaper() throws IOException{
		
		String output;
		ChangeFileType ctf = new ChangeFileType();  //call ChangeFileType to get path name to .cermxml file
		output = ctf.toString();
		
		try {
			PrintStream out = new PrintStream(new FileOutputStream("C:\\Users\\ralph\\neo4j-community-3.4.6-windows\\neo4j-community-3.4.6\\import\\inputCiteAuthPaper.csv"));
			System.setOut(out);
			File inputFile = new File(output);	         
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();	         
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);	 
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();	         
			//NodeList nList = doc.getElementsByTagName("article-title");
			NodeList refList = doc.getElementsByTagName("ref");
			
			String title = null, lastName = null, firstName = null, corTitle = null;
			out.println("Cites," + "Author,");
			if (refList.getLength() > 0) {
				
				for (int temp = 0; temp < refList.getLength(); temp++) {
					Node nNode = refList.item(temp);					

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;


						//System.out.println("Ref id :" + eElement.getAttribute("id"));

						NodeList mixCite = eElement.getElementsByTagName("mixed-citation");

						for (int loop = 0; loop < mixCite.getLength(); loop++) {
							Element mElement = (Element) mixCite.item(loop);
							

							NodeList articleList = mElement.getElementsByTagName("article-title");
							for (int loop2 = 0; loop2 < articleList.getLength(); loop2++) {
								Element artTitle = (Element) articleList.item(loop2);
								if (artTitle.getParentNode().getNodeName().equals("mixed-citation")) {
									title = artTitle.getFirstChild().getNodeValue().replaceAll(",”", "").replaceAll("“", "").replaceAll("”,", "").replaceAll(", www", "");
									corTitle = ("\"" + title + "\"");
									
								}//end if artTitle.getParentNode()
							}//end for loop2 articleList.getLength()
							
							
							if (articleList.getLength() > 0) {
							NodeList nameList = mElement.getElementsByTagName("string-name");
							
							for (int loop3 = 0; loop3 < nameList.getLength(); loop3++) {								
								
								Element authName = (Element) nameList.item(loop3);
								Element authFirst = (Element) nameList.item(loop3);
								
								
								NodeList surname = authName.getElementsByTagName("surname");
								NodeList initial = authFirst.getElementsByTagName("given-names");
								for (int loop4 = 0; loop4 < initial.getLength(); loop4++) {
									firstName = null;
									lastName = null;
									Element authSurname = (Element) surname.item(loop4);
									if (authSurname.getParentNode().getNodeName().equals("string-name")) {
										lastName = authSurname.getFirstChild().getNodeValue();
										//out.print(lastName + " ");
									}
									Element authInitial = (Element) initial.item(loop4);
									if (authInitial.getParentNode().getNodeName().equals("string-name")) {
										firstName = authInitial.getFirstChild().getNodeValue();
										//out.print(firstName + ",");
									}							
								}out.println(corTitle + "," + firstName + " " + lastName + ",");
							}
							
							}//end for loop3 nameList.getLength()
							
						}//end for loop mixCite

					}//end if nNode.getNodeType()
				}//end for loop titleList.getLength()
			}//end if titleList.getLength()



		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
