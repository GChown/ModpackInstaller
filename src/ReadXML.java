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
	ArrayList<Mod> modsArray = new ArrayList<Mod>();
	
	Document modsListDocument = null;
	SaveURL saveUrl;
	Integer numMods;
	Version modListVersion = null;
	public enum Status {SUCCESS, FAIL, FILENOTFOUND};

	public Document getDocumentObject(){
		return modsListDocument;
	}
	
	public void readFileFromServer(String URL){
		try{
			URL xmlURL = new URL(URL);
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
		
		//Read the file
		for (int temp = 0; temp < modsArray.size(); temp++) {
				System.out.println("Saving: " + modsArray.get(temp).name);
				saveUrl = new SaveURL(modsFolder + "/" + modsArray.get(temp).name, modsArray.get(temp).webPath);
		}
		System.out.println("DONE GETTING MODS");

	}
	
	public void populateModsArray(ReadXML localReadr, boolean localListAvailable, String modsFolder){ //this should only be called from the webReader instance
		NodeList webList = this.modsListDocument.getElementsByTagName("Mod");
		numMods = webList.getLength();
		
		if(!localListAvailable){
			// populate modsArray with webList
			for (int i = 0; i < numMods; i++){
				Node nNode = webList.item(i);	 
				Element eElement = (Element) nNode;
				
				String URL = eElement.getElementsByTagName("URL").item(0).getTextContent();
				String Name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				
				modsArray.set(i, new Mod(URL, Name));
			}
			return; // mods array is populated, get outta here!
		}
		
		
		NodeList localList = localReadr.getDocumentObject().getElementsByTagName("Mod");
		
		//for each mod in the web reader
		for (int i = 0; i < numMods; i++){
			Node webNode = webList.item(i);	 
			Element webElement = (Element) webNode;
			
			Node localNode = localList.item(i);
			Element localElement = (Element) localNode;
			
			//get the web mod NAME string to a String object
			String newURL = webElement.getElementsByTagName("URL").item(0).getTextContent();
			String oldURL = localElement.getElementsByTagName("URL").item(0).getTextContent();
			String Name = webElement.getElementsByTagName("Name").item(0).getTextContent();
			String Path = modsFolder + "/" + localElement.getElementsByTagName("Name").item(0).getTextContent();
			String webVersionString = webElement.getElementsByTagName("modVersion").item(0).getTextContent();
			String localVersionString = localElement.getElementsByTagName("modVersion").item(0).getTextContent();
			Version webVersion = new Version(webVersionString);
			Version localVersion = new Version(localVersionString);
			
			/* debug code
			System.out.println("web version: " + webVersion.getVersion());
			System.out.println("local version: " + localVersion.getVersion());
			System.out.println("Bigger version:" + webVersion.isBiggerVersion(localVersion) );
			*/
			
			if( (webVersionString.compareTo(localVersionString) != 0) || (oldURL.compareTo(newURL) != 0) || (webVersion.isBiggerVersion(localVersion))) // we need to add this one
				modsArray.add(new Mod(localVersion, webVersion, Path, newURL, Name));
		}
		
	}

	public String getListVersion(){
		NodeList nList = modsListDocument.getElementsByTagName("listVersion");

		modListVersion = new Version(nList.item(0).getTextContent()); // assume there is no more than 1 listVersion element
		return modListVersion.getVersion();
	}

	public Version getVersion(){
		return modListVersion;
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
