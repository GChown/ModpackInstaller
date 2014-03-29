import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ReadXML {
	File Modlist = new File("Modlist.xml");
	ArrayList<File> mods = new ArrayList<File>();
	ArrayList<String> modsURL = new ArrayList<String>();
	SaveURL saveUrl;
	//Gui gui = new Gui();
	
	
	public void getMods() throws MalformedURLException, IOException, SAXException, ParserConfigurationException {
		//Get file
		URL xmlURL = new URL("http://gord360.com/matt/TestModList.xml");
		InputStream xml = xmlURL.openStream();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		xml.close();
		NodeList nList = doc.getElementsByTagName("Mod");
		System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
		 
	 //Read the file
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {	 
				Element eElement = (Element) nNode;
				//Save the URL with the filename Name
				saveUrl = new SaveURL(eElement.getElementsByTagName("Name").item(0).getTextContent(), eElement.getElementsByTagName("URL").item(0).getTextContent());
			}
		}
		
	}
}
