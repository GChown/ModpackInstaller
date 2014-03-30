import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ReadXML {
	File Modlist = new File("Modlist.xml");
	ArrayList<File> mods = new ArrayList<File>();
	ArrayList<String> modsURL = new ArrayList<String>();
	Document modsListDocument = null;
	SaveURL saveUrl;
	public enum Status {SUCCESS, FAIL, FILENOTFOUND};

	public Document getDocumentObject(){
		return modsListDocument;
	}
	
	public void readFileFromServer(){
		try{
			URL xmlURL = new URL("http://gord360.com/ModList.xml");
			InputStream xml = xmlURL.openStream();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			modsListDocument = db.parse(xml);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public Status readFileFromSystem(String path){
		try{
			File localModlist = new File(path + "/modList.xml");
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			modsListDocument = builder.parse(localModlist);
		}
		catch(FileNotFoundException fnf){
			System.out.println("No file exsists in mods folder");
			return Status.FILENOTFOUND;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return Status.SUCCESS;
	}

	public void getMods(String modsFolder) throws MalformedURLException, IOException, SAXException, ParserConfigurationException {
		File modsFile = new File(modsFolder);
		if(!modsFile.exists())
			modsFile.mkdir(); // create the mods folder if it does not exist

		NodeList nList = modsListDocument.getElementsByTagName("Mod");
		//Read the file
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {	 
				Element eElement = (Element) nNode;
				//Save the URL with the filename Name
				System.out.println("Saving: " + eElement.getElementsByTagName("Name").item(0).getTextContent());
				saveUrl = new SaveURL(modsFolder + "/" + eElement.getElementsByTagName("Name").item(0).getTextContent(), eElement.getElementsByTagName("URL").item(0).getTextContent());
			}
		}
		System.out.println("DONE GETTING MODS");

	}

	public String getListVersion(){
		NodeList nList = modsListDocument.getElementsByTagName("version");

		return nList.item(0).getTextContent(); // assume there is no more than 1 listVersion element
	}
	
	public void writeDocToFile(String path){
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(getDocumentObject());
			StreamResult streamResult =  new StreamResult(new File(path+"/modList.xml"));
			transformer.transform(source, streamResult);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
