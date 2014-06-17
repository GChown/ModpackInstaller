import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
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
	
	Document modsListDocument = null;
	SaveURL saveUrl;
	Integer numModsWeb;
	Integer numModsLocal;
	Version modListVersion = null;
	public enum Status {SUCCESS, FAIL, FILENOTFOUND};
	public String fVersion;
	public Document getDocumentObject(){
		return modsListDocument;
	}
	
	public int readFileFromServer(String URL){
		try{
			URL xmlURL = new URL(URL);
			InputStream xml = xmlURL.openStream();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			modsListDocument = db.parse(xml);
			return 0;
		}
		catch(UnknownHostException e){
			return -1;
		}
		catch(Exception e){
			e.printStackTrace();
			return -2;
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

	public void getMods(String modsFolder, ArrayList<Mod> modsArray) throws MalformedURLException, IOException, SAXException, ParserConfigurationException {
		
		File modsFile = new File(modsFolder);
		if(!modsFile.exists())
			modsFile.mkdir(); // create the mods folder if it does not exist
		
		//Read the file
		for (int temp = 0; temp < modsArray.size(); temp++) {
				System.out.println("Saving: " + modsArray.get(temp).name);
				saveUrl = new SaveURL(modsFolder + "/" + modsArray.get(temp).name, modsArray.get(temp).webPath);
		}
		System.out.println("DONE GETTING MODS");

	}
	
	

	public String getListVersion(){
		NodeList nList = modsListDocument.getElementsByTagName("listVersion");

		modListVersion = new Version(nList.item(0).getTextContent()); // assume there is no more than 1 listVersion element
		return modListVersion.getVersion();
	}

	public Version getVersion(){
		return modListVersion;
	}
	
	public String getfVersion(){		
		NodeList nList = modsListDocument.getElementsByTagName("forgeVersion");
		return nList.item(0).getTextContent();
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

	public void update() {
		System.out.println("Updating");
	}
}
