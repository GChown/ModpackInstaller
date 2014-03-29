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
	JTextField modsDir;

	Icon getforge = new ImageIcon("forgeimg.png"), mcmods = new ImageIcon("mcimg.png");
	SpringLayout sl = new SpringLayout();
	File forge = new File("ForgeInstaller.jar");
	SaveURL saveUrl;
	public ReadXML reader = new ReadXML();
	public String path;
	

	public Gui() {
		path = getModsPath();
		modsDir = new JTextField(path);
		
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
		frame.add(modsDir);
		sl.putConstraint(SpringLayout.NORTH, update, 0, SpringLayout.NORTH,
				download);
		sl.putConstraint(SpringLayout.WEST, update, 10, SpringLayout.EAST,
				download);
		sl.putConstraint(SpringLayout.NORTH, details, 0, SpringLayout.SOUTH,
				update);
		sl.putConstraint(SpringLayout.NORTH, modsDir, 0, SpringLayout.SOUTH,
				details);
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
				try {
					reader.getMods(path);
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
		});
		modsDir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				 String text = modsDir.getText();
			     path = text;
			}
		});
		
	}
	private String getModsPath(){
		File modsDir = null;
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
		
		return modsDir.toString();
	}

	
}
