import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SaveURL {

	public SaveURL(String fileName, String urlString)
			throws MalformedURLException, IOException {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		// BufferedWriter fout = null;
		try {
			in = new BufferedInputStream(new URL(urlString).openStream());
			String mcdir;
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

	void getModsDir() {
		String OS = System.getProperty("os.name").toLowerCase();
		OS = OS.substring(0, 3);
		if (OS.equals("win")) {
			modsDir = new File(System.getProperty("user.home")
					+ "//AppData//Roaming//.minecraft//mods");
		} else if (OS.equals("mac")) {
			modsDir = new File(System.getProperty("user.home") + "//Library//Application Support//minecraft//mods");
		} else {
			// unsuported OS (for now), add more
		}

		// if the mods directory does not exist, create it
		if (!modsDir.exists()) {
			System.out.println("creating directory: " + "mods");
			boolean result = modsDir.mkdir();

			if (result) {
				System.out.println("DIR created");
			}
		}
	}
}
