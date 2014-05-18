import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpringLayout;


public class Notification {
	
	JFrame frame = new JFrame();
	JLabel details = new JLabel();
	JButton okButton = new JButton("OK");
	
	SpringLayout sl = new SpringLayout();
	
	public Notification() {

		//gui setup
		sl.putConstraint(SpringLayout.NORTH, okButton, 5, SpringLayout.SOUTH, details);
		frame.setSize(300, 80);
		frame.setLayout(sl);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(2);
		frame.add(details);
		frame.add(okButton);
		
		
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.setVisible(false);
			}
		});
	}
	public void showNotice(String title, String message){
		details.setText(message);
		frame.setTitle(title);
		frame.setVisible(true);
	}
	public void show(){
		frame.setVisible(true);
	}
	public void close(){
		frame.setVisible(false);
	}
	public String getCurrentMessage(){
		return details.getText();
	}
	public String getCurrentTitle(){
		return frame.getTitle();
	}
}
