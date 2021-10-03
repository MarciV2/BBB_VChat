package de.dhbwheidenheim.informatik.chatClient;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import org.json.JSONObject;

public class IncomingCallPopup extends JDialog {
	
	public IncomingCallPopup(URI roomURL, String organizername ,boolean isPrivate, ArrayList<String> otherInvitees, ArrayList<String> attendees ) {
		IncomingCallPopup self=this;
		
		
		this.setAlwaysOnTop(true);
		
		JButton btnNewButton = new JButton("ANNEHMEN");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(roomURL);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				self.setVisible(false);
			}
		});
		getContentPane().add(btnNewButton, BorderLayout.WEST);
		
		JLabel lblNewLabel = new JLabel("<html>Eingehender Anruf von <B>"+organizername+"</B> </html>");
		getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		
		JButton btnNewButton_1 = new JButton("ABLEHNEN");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				self.setVisible(false);
			}
		});
		getContentPane().add(btnNewButton_1, BorderLayout.EAST);
		
		JTextPane txtpnDetails = new JTextPane();
		
		
		String detailText="Details:\n";
		if(isPrivate)detailText+="\tPrivater Anruf\n";
		else detailText+="\t÷ffentlicher Anruf\n";
		detailText+="\tEingeladene:\n";
		for(String s:otherInvitees)
		detailText+="\t\t"+s+"\n";
		
		detailText+="\tAnwesende:\n";
		for(String s:attendees)
			detailText+="\t\t"+s+"\n";
		txtpnDetails.setText(detailText);
		getContentPane().add(txtpnDetails, BorderLayout.CENTER);
		
		this.pack();
	}

}
