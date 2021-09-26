package de.dhbwheidenheim.informatik.chatClient;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGui extends JFrame {
	public MainGui() {
		
		JButton btnNewButton = new JButton("openPopup");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IncomingCallPopup pu=new IncomingCallPopup();
				pu.setVisible(true);
			}
		});
		getContentPane().add(btnNewButton, BorderLayout.EAST);
	}
	
	
	
	

	public static void main(String[] args) {
		MainGui gui=new MainGui();
		gui.setVisible(true);
		gui.pack();
	}

}
