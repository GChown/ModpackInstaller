import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Gui {
	JFrame frame = new JFrame();
	JButton download = new JButton(),
			update = new JButton();
	JLabel details = new JLabel("Pick an option");
	JLabel listVersions = new JLabel("");
	JLabel TextInput = new JLabel("Install location:"), XMLInput = new JLabel("XML location:");
	JTextField modsPathTextBox;
	public static JTextField onlineLocation= new JTextField("http://gord360.com/ModList.xml");

	//this version for the JAR 
	//Icon getforge = new ImageIcon(getClass().getResource("forgeimg.png")), mcmods = new ImageIcon(getClass().getResource("mcimg.png")), mcimgupdate = new ImageIcon(getClass().getResource("mcimgupdate.png"));

	//this version for running from eclipse 
	Icon getforge = new ImageIcon("forgeimg.png"), mcmods = new ImageIcon("mcimg.png"), mcimgupdate = new ImageIcon("mcimgupdate.png");


	SpringLayout sl = new SpringLayout();
	File forge = new File("ForgeInstaller.jar");
	SaveURL saveUrl;
	public ReadXML webReader = new ReadXML();
	public ReadXML localReader = new ReadXML();
	public String path = "";


	public Gui() {
		//pre GUI setup
		getModsPath(); //get the mods path based on OS

		webReader.readFileFromServer(onlineLocation.getText()); // read the file from server

		if(localReader.readFileFromSystem(path) == ReadXML.Status.SUCCESS){ // if the file exists on the system
			listVersions.setText("Latest: " + webReader.getListVersion() + "\t On System: " + localReader.getListVersion());
			if(webReader.getVersion().isBiggerVersion(localReader.getVersion())){
				update.setIcon(mcimgupdate);
			}
		}

		else{ // otherwise the version is unknown and localReader.getListVersion will cause an error (filenotfound)

			listVersions.setText("Latest: " + webReader.getListVersion() + "\t On System: " + "Unknown (probably not installed yet)");
			webReader.writeDocToFile(path);
		}



		//gui setup
		modsPathTextBox = new JTextField(path, 21);
		download.setBackground(Color.white);
		update.setBackground(Color.white);
		download.setIcon(getforge);
		update.setIcon(mcmods);
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
					System.out.println("Saving modlist from server");
					webReader.writeDocToFile(path); // save the new list from the server to the file
					webReader.getMods(path); // get the mods
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
				path = modsPathTextBox.getText();
			}
		});

	}
	private void getModsPath(){
		String OS = System.getProperty("os.name").toLowerCase();
		OS = OS.substring(0,3);

		if(OS.equals("win")){
			path = System.getProperty("user.home")+ "/AppData/Roaming/.minecraft/mods";
		}
		else if(OS.equals("mac")){
			path =System.getProperty("user.home")+"/Library/Application Support/minecraft/mods"; 
		}
		else if(OS.equals("lin")){
			path = System.getProperty("user.home")+"/.minecraft/mods";
		}
		else{
			System.out.println("UNSUPORTED OS");
			//unsuported OS (for now), add more
		}
	}


}