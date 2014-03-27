import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ReadXML {
	File Modlist = new File("Modlist.xml");
	ArrayList<File> mods = new ArrayList<File>();
	ArrayList<String> modsURL = new ArrayList<String>();
	SaveURL saveUrl;
	
	
	public void getMods() throws MalformedURLException, IOException, SAXException, ParserConfigurationException {

		if (!Modlist.exists()) { // if the modlist does not exsist, get it
			System.out.println("Downloading modlist");			
			saveUrl = new SaveURL("Modlist.xml", "http://www.gord360.com/matt/ModList.xml");
		}
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(Modlist);
		NodeList mainList = doc.getDocumentElement().getChildNodes();
		
		Document newMSDoc = builder.parse(Modlist);
		NodeList newModNodes = newMSDoc.getDocumentElement().getChildNodes(); //setup new modList nodes
		if(getModListVersion(newModNodes) > getModListVersion(mainList)){//this is a new mod list, overwrite the old one
	
			//Save the newmodlist and overwrite the old one
			mainList = newModNodes;
			//saveUrl("modlist.xml", "http://www.gord360.com/modlist.xml"); //this SHOULD overwrite modlist.xml
			saveUrl = new SaveURL("Modlist.xml", "http://Gord360.com/matt/ModList.txt");
			System.out.println("Reading from modlist");
			
			String line;
			File modsDir = null;

			
			//gets OS type and sets minecraft mods directory accordingly
			String OS = System.getProperty("os.name").toLowerCase();
			OS = OS.substring(0,3);
			if(OS.equals("win")){
				modsDir = new File(System.getProperty("user.home")+ "//AppData//Roaming//.minecraft//mods");
			}
			else if(OS.equals("mac")){
				modsDir = new File(System.getProperty("user.home")+"//Library//Application Support//minecraft//mods"); 
			}
			else{
				//unsuported OS (for now), add more
			}

			// if the mods directory does not exist, create it
			if (!modsDir.exists()) {
				System.out.println("creating directory: " + "mods");
				boolean result = modsDir.mkdir();  

				if(result) {    
					System.out.println("DIR created");
				}
			}
			
			//get the ModList node
			Node modListNode = getNodeByID(mainList, "ModList");
			
			//put mods into modsURL (arraylist)
			putModsToArray(modListNode); //this takes the modlistNode and parses the URLs into an array (the modsURL arraylist)
			
			//Download mods
			try {
				for(int i = 0; i < modsURL.size(); i++){ // for each mod in the arrayList
					line = modsURL.get(i);
					String name = line.substring(line.lastIndexOf('/') + 1); //get the local file name from the URL
					File mod = new File(name);
					if (mod.exists()) { //if the mod already exists do not download
						System.out.println(name
								+ " found, not downloading.");
					} else {
						System.out.println("getting " + line);
						saveUrl = new SaveURL(modsDir.toString() +"//"+ name, line);
					}
				}
			} catch (IOException e1) {
				System.out.println("failed");
				e1.printStackTrace();

			}
			System.out.println("Done getting mods!");
		}
	}
	private void putModsToArray(Node modListNode){ //Function to add mod URLs to the modsURL list
		Node arrayNode = null;
		arrayNode = getNodeByID(modListNode.getChildNodes(), "modsArray", 1);
		
		for(int i = 0; i < arrayNode.getChildNodes().getLength(); i++) {// for each mod in the arrayNode
			Node tempModNode = arrayNode.getChildNodes().item(i);
			System.out.println("Getting mod: " + getNodeByID(tempModNode.getChildNodes(), "Mod Name", 1).getTextContent());
			tempModNode = getNodeByID(tempModNode.getChildNodes(), "URL",1);
			modsURL.add(tempModNode.getTextContent());
		}
	}

	private double getModListVersion(NodeList masterList){ // function to search the master nodeList for the version information
		Node infoNode = getNodeByID(masterList, "info");
		double version = 0;
		Node versionNode = getNodeByID(infoNode.getChildNodes(), "Version", 1);
		version = Double.parseDouble(versionNode.getTextContent());
		return version;
	}

	private Node getNodeByID(NodeList inList, String ID, int offset){ // get a node in the list that is offset from the string ID by the variable offset
		for(int i = 0; i < inList.getLength(); i++){
			if(inList.item(i).getTextContent().equals(ID)){
				return inList.item(i+offset);
			}
		}
		return null;
	}

	private Node getNodeByID(NodeList inList, String ID){ // find a node that has the text of [String ID]
		for(int i = 0; i < inList.getLength(); i++){
			if(getNodeID(inList.item(i)).equals(ID)){
				return inList.item(i);
			}
		}
		return null;
	}

	private String getNodeID(Node inNode){// get a second level node's ID by its <key>ID</key><string>IDGOESHERE</string> tags
		NodeList childNodes = inNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++){
			if(childNodes.item(i).getTextContent().equals("ID")){
				//next node is ID
				//System.out.println(childNodes.item(i+1).getTextContent());
				return childNodes.item(i+1).getTextContent();
			}
		}
		return "";
	}
}
