import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Gui {
	JFrame frame = new JFrame();
	JButton download = new JButton("1. Download & Install Forge"),
			update = new JButton("2. Download & Install Mods");
	SpringLayout sl = new SpringLayout();
	File forge = new File("ForgeInstaller.jar");
	File modlist = new File("modlist.xml");

	ArrayList<File> mods = new ArrayList<File>();
	ArrayList<String> modsURL = new ArrayList<String>();

	// ImageIcon icon = createImageIcon("splash.png", "beaut.");
	// JLabel splash = new JLabel("splash here", icon, JLabel.CENTER);

	JLabel details = new JLabel("Do what you need to do!");

	public Gui() {
		// frame.setSize(icon.getIconWidth(), 320);
		frame.setSize(400, 320);
		frame.setTitle("Install Modpack");
		frame.setLayout(sl);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(2);
		// frame.add(splash);
		frame.add(download);
		frame.add(update);
		frame.add(details);
		// sl.putConstraint(SpringLayout.NORTH, download, 0, SpringLayout.SOUTH,
		// splash);
		sl.putConstraint(SpringLayout.NORTH, update, 0, SpringLayout.NORTH,
				download);
		sl.putConstraint(SpringLayout.WEST, update, 10, SpringLayout.EAST,
				download);
		sl.putConstraint(SpringLayout.NORTH, details, 0, SpringLayout.SOUTH,
				update);
		frame.setVisible(true);
		download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!forge.exists()) {
					try {
						details.setText("Downloading Forge...");
						saveUrl("ForgeInstaller.jar",
								"http://files.minecraftforge.net/maven/net/minecraftforge/forge/1.6.4-9.11.1.965/forge-1.6.4-9.11.1.965-installer.jar");
					} catch (MalformedURLException f) {
						f.printStackTrace();
					} catch (IOException f) {
						f.printStackTrace();
					}
					try {
						details.setText("Installing Forge...");
						Desktop.getDesktop().open(forge);
					} catch (IOException f) {
						f.printStackTrace();
					}
				} else {
					try {
						details.setText("Installing Forge...");
						Desktop.getDesktop().open(forge);
					} catch (IOException f) {
						f.printStackTrace();
					}
				}
			}
		});
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					getMods();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (SAXException e1){
					e1.printStackTrace();
				} catch (ParserConfigurationException e1){
					e1.printStackTrace();
				}
				;
			}

			protected void getMods() throws MalformedURLException, IOException, SAXException, ParserConfigurationException {



				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = builder.parse(modlist);
				NodeList mainList = doc.getDocumentElement().getChildNodes();

				if (!modlist.exists()) { // if the modlist does not exsist, get it
					details.setText("Downloading modlist...");
					System.out.println("Downloading modlist");
					saveUrl("modlist.xml", "http://www.gord360.com/modlist.xml");
				}
				
				//Get new modlist from server
				File newModlist = new File("newModlist.xml");
				details.setText("Downloading New modlist...");
				System.out.println("Downloading New modlist");
				//saveUrl("newModlist.xml", "http://www.gord360.com/modlist.xml"); 
				saveUrl("newModlist.xml", "https://gist.githubusercontent.com/puregame/722112bc3577774f9323/raw/80ff5caef905cd593dc6c479dcce147077d7f630/gistfile1.txt");
				

				Document newMSDoc = builder.parse(newModlist);
				NodeList newModNodes = newMSDoc.getDocumentElement().getChildNodes(); //setup new modList nodes

				if(getModListVersion(newModNodes) > getModListVersion(mainList)){//this is a new mod list, overwrite the old one
			
					//________Save the newmodlist and overwrite the old one
					mainList = newModNodes;
					//saveUrl("modlist.xml", "http://www.gord360.com/modlist.xml"); //this SHOULD overwrite modlist.xml
					saveUrl("Modlist.xml", "https://gist.githubusercontent.com/puregame/722112bc3577774f9323/raw/80ff5caef905cd593dc6c479dcce147077d7f630/gistfile1.txt");
					details.setText("Downloading mods...");
					System.out.println("Reading from modlist");
					
					String line;
					File modsDir = null;

					
					//_____________gets OS type and sets minecraft mods directory accordingly_______________
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
					putModsToMods(modListNode); //this takes the modlistNode and parses the URLs into an array (the modsURL arraylist)
					
					//____________Download mods
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
								saveUrl(modsDir.toString() +"//"+ name, line);
							}
						}
					} catch (IOException e1) {
						System.out.println("failed");
						e1.printStackTrace();

					}
					System.out.println("Done getting mods!");
				}
			}

		});
	}
	
	private void putModsToMods(Node modListNode){ //Function to add mod URLs to the modsURL list
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

	private String getNodeID(Node inNode){// get a second level node's ID by its <key>ID</kety><string>IDGOESHERE</string> tags
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

	public ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public static void saveUrl(String fileName, String urlString)
			throws MalformedURLException, IOException {



		BufferedInputStream in = null;
		FileOutputStream fout = null;
		//BufferedWriter fout =  null;
		try {
			in = new BufferedInputStream(new URL(urlString).openStream());
			fout = new FileOutputStream(fileName);
			byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null)
				in.close();
			if (fout != null)
				fout.close();
		}
	}
}
