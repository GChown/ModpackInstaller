import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SaveURL {

	public SaveURL(String fileName, String urlString)
			throws MalformedURLException, IOException, FileNotFoundException{
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
}
