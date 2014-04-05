import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpringLayout;


public class DownloadProgress {


	JFrame frame = new JFrame();
	JButton cancel = new JButton("Cancel");
	JLabel details = new JLabel("Downloading Mods");
	
	SpringLayout sl = new SpringLayout();
	
	public DownloadProgress() {

		//gui setup
		
		frame.setSize(300, 30);
		frame.setTitle("Downloading Modpack");
		frame.setLayout(sl);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(2);
		frame.add(details);
		frame.add(cancel);
		sl.putConstraint(SpringLayout.NORTH, details, 10, SpringLayout.NORTH, cancel);
		sl.putConstraint(SpringLayout.EAST, details, 0, SpringLayout.WEST, cancel);
		frame.setVisible(true);
	}
	public void close(){
		frame.setVisible(false);
	}
	
}
