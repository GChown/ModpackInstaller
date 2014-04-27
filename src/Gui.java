import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Gui {


	JTextField onlineLocation= new JTextField("http://gord360.com/ModList.xml");
	JFrame frame = new JFrame();
	JButton download = new JButton(),
			update = new JButton();
	JLabel details = new JLabel("Pick an option");
	JLabel listVersions = new JLabel("");
	JLabel TextInput = new JLabel("Install location:"), XMLInput = new JLabel("XML location:");
	JTextField modsPathTextBox;

	//this version for the JAR 
	//Icon getforge = new ImageIcon(getClass().getResource("forgeimg.png")), mcmods = new ImageIcon(getClass().getResource("mcimg.png")), mcimgupdate = new ImageIcon(getClass().getResource("mcimgupdate.png"));

	//this version for running from eclipse 
	Icon getforge = new ImageIcon("forgeimg.png"), forgUpdateAvailabe = new ImageIcon("forgeimgupdate.png"), mcmods = new ImageIcon("mcimg.png"), mcimgupdate = new ImageIcon("mcimgupdate.png");


	SpringLayout sl = new SpringLayout();
	File forge = new File("ForgeInstaller.jar");

	ArrayList<Mod> modsArray = new ArrayList<Mod>();
	SaveURL saveUrl;
	public ReadXML webReader = new ReadXML();
	public ReadXML localReader = new ReadXML();
	private String modsPath = "";
	private String configPath = "";

	public Gui() {
		//pre GUI setup
		getModsPath(); //get the mods path based on OS

		//check if modList.xml is on system


		compareVersions();


		//Check if forge is installed.
		getConfigPath();
		//check if forge is installed
		download.setIcon(getforge);
		if(!isForgeInstalled()){
			System.out.println("Forge is not installed");
			download.setIcon(forgUpdateAvailabe);
		}


		//gui setup
		modsPathTextBox = new JTextField(modsPath, 21);
		download.setBackground(Color.white);
		update.setBackground(Color.white);
		frame.setSize(getforge.getIconWidth()+mcmods.getIconWidth()+100, getforge.getIconHeight()+mcmods.getIconHeight()+10);
		frame.setTitle("Install Modpack");
		frame.setLayout(sl);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(2);
		frame.add(download);
		frame.add(update);
		frame.add(details);
		frame.add(listVersions);
		frame.add(TextInput);
		frame.add(modsPathTextBox);
		frame.add(onlineLocation);
		frame.add(XMLInput);
		sl.putConstraint(SpringLayout.NORTH, update, 0, SpringLayout.NORTH,
				download);
		sl.putConstraint(SpringLayout.WEST, update, 10, SpringLayout.EAST,
				download);
		sl.putConstraint(SpringLayout.NORTH, details, 0, SpringLayout.SOUTH,
				update);
		sl.putConstraint(SpringLayout.NORTH, TextInput, 10, SpringLayout.SOUTH,
				details);
		sl.putConstraint(SpringLayout.NORTH, modsPathTextBox, 0, SpringLayout.SOUTH,
				TextInput);
		sl.putConstraint(SpringLayout.NORTH, listVersions, 0, SpringLayout.SOUTH,
				modsPathTextBox);
		sl.putConstraint(SpringLayout.WEST, onlineLocation, 10, SpringLayout.EAST,	modsPathTextBox);
		sl.putConstraint(SpringLayout.NORTH, onlineLocation, 0, SpringLayout.NORTH,	modsPathTextBox);
		sl.putConstraint(SpringLayout.SOUTH, XMLInput, 0, SpringLayout.NORTH,	onlineLocation);
		sl.putConstraint(SpringLayout.WEST, XMLInput, 0, SpringLayout.WEST,	onlineLocation);


		frame.setVisible(true);
		download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!forge.exists()) {
					try {
						details.setText("Downloading Forge...");
						saveUrl = new SaveURL("ForgeInstaller.jar",
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
				DownloadProgress dp = new DownloadProgress();
				try {
					modsPath = modsPathTextBox.getText();
					webReader.readFileFromServer(onlineLocation.getText());
					compareVersions();

					System.out.println("Saving modlist from server");
					webReader.writeDocToFile(modsPath); // save the new list from the server to the file
					webReader.getMods(modsPath, modsArray); // get the mods
					details.setText("Done getting mods");
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
				dp.close();
			}
		});
		modsPathTextBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				modsPath = modsPathTextBox.getText();
				getConfigPath();
				compareVersions();
			}
		});
		onlineLocation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				webReader.readFileFromServer(onlineLocation.getText());
				compareVersions();
			}
		});
	}

	private void compareVersions(){ // update GUI and modsArray based on new information from user
		update.setIcon(mcmods);
		webReader.readFileFromServer(onlineLocation.getText());
		boolean localAvailable = false;
		//get list versions and populate mods array
		if(localReader.readFileFromSystem(modsPath) == ReadXML.Status.SUCCESS){ // if the file exists on the system
			listVersions.setText("ModList versions| Latest: " + webReader.getListVersion() + "\t On System: " + localReader.getListVersion());
			if(webReader.getVersion().isBiggerVersion(localReader.getVersion())){ // if web version is bigger change update icon
				update.setIcon(mcimgupdate);
			}
			localAvailable = true;
		}

		else{ // otherwise the version is unknown and localReader.getListVersion will cause an error (filenotfound)
			listVersions.setText("ModList versions| Latest: " + webReader.getListVersion() + "\t On System: " + "Unknown (probably not installed yet)");
			update.setIcon(mcimgupdate);
		}

		populateModsArray(localAvailable, modsPath); 
	}


	private void getModsPath(){
		String OS = System.getProperty("os.name").toLowerCase();
		OS = OS.substring(0,3);

		if(OS.equals("win")){
			modsPath = System.getProperty("user.home")+ "/AppData/Roaming/.minecraft/mods";
		}
		else if(OS.equals("mac")){
			modsPath =System.getProperty("user.home")+"/Library/Application Support/minecraft/mods"; 
		}
		else if(OS.equals("lin")){
			modsPath = System.getProperty("user.home")+"/.minecraft/mods";
		}
		else{
			System.out.println("UNSUPORTED OS");
			//unsuported OS (for now), add more
		}
	}
	private void getConfigPath(){
		//ALWAYS RUN getModsPath() first
		configPath = modsPath.substring(0, modsPath.lastIndexOf("/"));
		configPath += "/config";

	}
	private boolean isForgeInstalled(){
		File localModlist = new File(configPath + "/forge.cfg");
		if(localModlist.isFile())
			return true;
		return false;
	}

	public void populateModsArray(boolean localListAvailable, String modsFolder){ //this should only be called from the webReader instance
		modsArray.clear(); //clear mods array incase it is already populated
		NodeList webList = webReader.modsListDocument.getElementsByTagName("Mod");
		int numModsWeb = webList.getLength();
		// ADD ALL mods in the web list to the modsArray
		String webVersionString = ""; // this can't be defined in teh for loop, we need it for the next for loop aswell
		for (int i = 0; i < numModsWeb; i++){
			Node webNode = webList.item(i);	 
			Element webElement = (Element) webNode;
			String newURL = webElement.getElementsByTagName("URL").item(0).getTextContent();
			String Name = webElement.getElementsByTagName("Name").item(0).getTextContent();
			webVersionString = webElement.getElementsByTagName("modVersion").item(0).getTextContent();
			String Path = modsFolder + "/" + webElement.getElementsByTagName("Name").item(0).getTextContent();
			Version webVersion = new Version(webVersionString);
			modsArray.add(new Mod(newURL, Name, webVersion, Path));
		}

		if(localListAvailable){ // if the local list is available, compare it to the web
			
			//get local list data
			NodeList localList = localReader.getDocumentObject().getElementsByTagName("Mod");
			System.out.println(localList.getLength());
			int numModsLocal = localList.getLength();
			
			//DELTE all mods that are in the local list AND (are diffrent versions OR have different URLs)
			for (int i = 0; i < numModsLocal; i++){
				Node localNode = localList.item(i); // get the i'th mod in the local node list
				Element localElement = (Element) localNode;
				String name = localElement.getElementsByTagName("Name").item(0).getTextContent();
				String localURL = localElement.getElementsByTagName("URL").item(0).getTextContent();
				String localVersionString = localElement.getElementsByTagName("modVersion").item(0).getTextContent();
				Version localVersion = new Version(localVersionString);

				//search for the name in modsArray
				int numInModsArray = -1;
				for(int k = 0; k < modsArray.size(); k++){
					if(name.compareTo(modsArray.get(k).name) == 0){
						numInModsArray = k;
						break;
					}
				}
				if (numInModsArray == -1)//this mod isn't in the web version, we can't do anything else
					continue; //  continue variable (i) for loop

				if( (webVersionString.compareTo(localVersionString) == 0) || (localURL.compareTo(modsArray.get(numInModsArray).webPath) == 0) || (! modsArray.get(numInModsArray).webVer.isBiggerVersion(localVersion))); // we need to add this one
				modsArray.remove(numInModsArray);
			}
		}
	}
}
