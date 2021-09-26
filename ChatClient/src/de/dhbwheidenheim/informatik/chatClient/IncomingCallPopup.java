package de.dhbwheidenheim.informatik.chatClient;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class IncomingCallPopup extends JDialog {
	URI callURL;
	
	public IncomingCallPopup() {
		IncomingCallPopup self=this;
		try {
			callURL=new URI("https://marci.vidmar.de");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		
		this.setAlwaysOnTop(true);
		
		JButton btnNewButton = new JButton("ANNEHMEN");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(callURL);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				self.setVisible(false);
			}
		});
		getContentPane().add(btnNewButton, BorderLayout.WEST);
		
		JLabel lblNewLabel = new JLabel("Eingehender Anruf!");
		getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		
		JButton btnNewButton_1 = new JButton("ABLEHNEN");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				self.setVisible(false);
			}
		});
		getContentPane().add(btnNewButton_1, BorderLayout.EAST);
		
		JTextPane txtpnDetails = new JTextPane();
		txtpnDetails.setText("Details:");
		getContentPane().add(txtpnDetails, BorderLayout.CENTER);
		
		this.pack();
	}

}
