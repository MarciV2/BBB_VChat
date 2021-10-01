package de.dhbwheidenheim.informatik.chatClient;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.FlowLayout;

public class Register extends JDialog {
	private JTextField textField;
	private JTextField textField_1;
	private JPasswordField passwordField;

	public Register() {
		Register self=this;
		self.setAlwaysOnTop(true);
		self.setSize(220, 185);
		self.setResizable(false);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 192, 132);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Registrierung:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(0, 0, 111, 17);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Vorname:");
		lblNewLabel_1.setBounds(0, 28, 58, 14);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Name:");
		lblNewLabel_2.setBounds(0, 53, 46, 14);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Passwort:");
		lblNewLabel_3.setBounds(0, 78, 58, 14);
		panel.add(lblNewLabel_3);

		textField = new JTextField();
		textField.setBounds(70, 25, 110, 20);
		panel.add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("Registrieren");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				//Registrierungsrequest
				String vn = textField.getText();
				String n = textField_1.getText();
				String p=new String(passwordField.getPassword());

				//Passwort verschl�sseln
				String generatedPassword = null;
				try {
					MessageDigest md = MessageDigest.getInstance("SHA-512");
					md.reset();
					byte[] rawBytes=p.getBytes(StandardCharsets.UTF_8);
					byte[] bytes = md.digest(p.getBytes(StandardCharsets.UTF_8));
					StringBuilder sb = new StringBuilder();
					for(int i=0; i< bytes.length ;i++){
						sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
					}
					generatedPassword = sb.toString();
				} catch (NoSuchAlgorithmException ex) {
					ex.printStackTrace();
				}


				if(vn.isEmpty()||n.isEmpty()||p.isEmpty())
					System.out.println("Bitte die Felder ausf�llen");
				else
				{
					if(vn.length()>15||n.length()>15||p.length()>15)
						System.out.println("Die maximale Eingabel�nge ist 16 Zeichen");
					else {
						String Anfrage = "http://localhost:8080/registerPerson?firstName="+vn+"&lastName="+n+"&password="+generatedPassword;
						System.out.println(Anfrage);
						URL url;
						try {
							url = new URL(Anfrage);
							HttpURLConnection con = (HttpURLConnection) url.openConnection();
							con.setRequestMethod("GET");
							try(BufferedReader br = new BufferedReader(
									new InputStreamReader(con.getInputStream(), "utf-8"))) {
								StringBuilder response = new StringBuilder();
								String responseLine = null;
								while ((responseLine = br.readLine()) != null) {
									response.append(responseLine.trim());
								}
								if(response.isEmpty()) System.out.println("Fehler bei der Antwort");
								else {
									System.out.println("Erfolgreich registriert, ihre ID ist:"+response.toString());
									self.setVisible(false);
								}
							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							con.disconnect();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}	
				}
			}
		});
		btnNewButton.setBounds(0, 103, 180, 23);
		panel.add(btnNewButton);

		textField_1 = new JTextField();
		textField_1.setBounds(70, 50, 110, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(70, 75, 110, 20);
		panel.add(passwordField);
	}
}
