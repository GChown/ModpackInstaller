import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class Gui {
	JFrame frame = new JFrame();
	JButton download = new JButton("1. Download & Install Forge"),
			update = new JButton("2. Download & Install Mods");
	SpringLayout sl = new SpringLayout();
	File file = new File("ForgeInstaller.jar");
	File modlist = new File("modlist.txt");

	ArrayList<File> mods = new ArrayList<File>();

	 ImageIcon icon = createImageIcon("splash.png", "beaut.");
	 JLabel splash = new JLabel("splash here", icon, JLabel.CENTER);

	 JLabel details = new JLabel("Do what you need to do!");
	public Gui() {
		// frame.setSize(icon.getIconWidth(), 320);
		frame.setSize(icon.getIconWidth(), 320);
		frame.setTitle("Install Modpack");
		frame.setLayout(sl);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(2);
		 frame.add(splash);
		frame.add(download);
		frame.add(update);
		 frame.add(details);
		 sl.putConstraint(SpringLayout.NORTH, download, 0, SpringLayout.SOUTH,
		 splash);		 
		sl.putConstraint(SpringLayout.NORTH, update, 0, SpringLayout.NORTH,
				download);
		sl.putConstraint(SpringLayout.WEST, update, 10, SpringLayout.EAST,
				download);
		sl.putConstraint(SpringLayout.NORTH, details, 0, SpringLayout.SOUTH,
				 update);
		frame.setVisible(true);
		download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!file.exists()) {
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
						Desktop.getDesktop().open(file);
					} catch (IOException f) {
						f.printStackTrace();
					}
				} else {
					try {
						details.setText("Installing Forge...");
						Desktop.getDesktop().open(file);
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
				}
				;
			}

			protected void getMods() throws MalformedURLException, IOException {
				if (!modlist.exists()) {
					details.setText("Downloading modlist...");
					System.out.println("Downloading modlist");
					saveUrl("modlist.txt","http://www.gord360.com/modlist.txt");
					getMods();
				} else {
					details.setText("Downloading mods...");
					System.out.println("Reading from modlist");
					BufferedReader br = new BufferedReader(new FileReader("modlist.txt"));
					String line;
					try {
						
						while ((line = br.readLine()) != null) {
							String name = System.getProperty("user.home")+"//%appdata%//.minecraft//mods1//"+line.substring(line.lastIndexOf('/')+1);
							File mod = new File(name);
							if(mod.exists()){
								System.out.println(name + " found, not downloading.");
							}else{
							System.out.println("getting "+line);							
							saveUrl(name, line);}
						}
					} catch (IOException e1) {
						System.out.println("failed");
						e1.printStackTrace();
					}
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("Done getting mods!");
				}
			}
		});
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

	public static void saveUrl(String filename, String urlString)
			throws MalformedURLException, IOException {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			in = new BufferedInputStream(new URL(urlString).openStream());
			fout = new FileOutputStream(filename);

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
