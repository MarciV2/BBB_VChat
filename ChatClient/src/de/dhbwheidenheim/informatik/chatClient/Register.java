package de.dhbwheidenheim.informatik.chatClient;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.net.ConnectException;
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
import javax.swing.JFrame;
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
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.FlowLayout;

public class Register extends JFrame {
	private JTextField textField;
	private JPasswordField passwordField;

	public Register() {
		//Grundeinstellungen des Windows
		Register self=this;
		self.setAlwaysOnTop(true);
		self.setSize(220, 165);
		self.setResizable(false);
		setLocationRelativeTo(null);
		URL iconURL = getClass().getResource("/resources/BigBlueButton_icon.svg.png");
		ImageIcon icon = new ImageIcon(iconURL);
		self.setIconImage(icon.getImage());

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 192, 112);
		getContentPane().add(panel);

		JLabel lblNewLabel = new JLabel("Registrierung:");
		lblNewLabel.setBounds(10, 11, 111, 17);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblNewLabel_1 = new JLabel("Benutzername:");
		lblNewLabel_1.setBounds(10, 39, 75, 14);


		JLabel lblNewLabel_3 = new JLabel("Passwort:");
		lblNewLabel_3.setBounds(10, 67, 58, 14);

		textField = new JTextField();
		textField.setBounds(94, 36, 96, 20);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("Registrieren");
		btnNewButton.setBounds(10, 92, 180, 23);
		//Button zur Registrierung geklickt
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				//Registrierungsrequest
				String username = textField.getText();
				String p=new String(passwordField.getPassword());
				
				//Grundvorgaben der Felder
				if(username.isEmpty()||p.isEmpty())
					System.out.println("Bitte die Felder ausfüllen");
				else
				{
					if(username.contains("\"")||username.contains(":")||username.contains(",")||username.contains("{")||username.contains("}")||username.contains("[")||username.contains("]")||username.contains(" "))
						System.out.println("Sonderzeichen wie (Leerzeichen :,\"{}[]) sind für den usernamen nicht erlaubt");
					else {
						if(username.equals("username"))
							System.out.println("Der Benutzername darf nicht username sein");
						else {
				if(username.length()>15||p.length()>15)
						System.out.println("Die maximale Eingabelänge ist 16 Zeichen");
					else {
						
				//Passwort verschlüsseln
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
						//Url zum Aufruf mit Eingaben befüllen
						String Anfrage = "http://localhost:8080/registerPerson?username="+username+"&password="+generatedPassword;
						URL url;
						try {
							//HTTPRequest Erstellung
							
							url = new URL(Anfrage);
							HttpURLConnection con = (HttpURLConnection) url.openConnection();
							con.setRequestMethod("GET");
							//Abfrage der Rückgabe des Requests
							try(BufferedReader br = new BufferedReader(
									new InputStreamReader(con.getInputStream(), "utf-8"))) {
								StringBuilder response = new StringBuilder();
								String responseLine = null;
								while ((responseLine = br.readLine()) != null) {
									response.append(responseLine.trim());
								}
								if(response.isEmpty()) System.out.println("Fehler bei der Antwort");
								else {
									if(response.toString().equals("Fehler der Username ist bereits vergeben"))
										System.out.println(response);
									else
									System.out.println("Erfolgreich registriert, ihr Benutzername ist:"+response.toString());
									self.setVisible(false);
								}
								con.disconnect();
							} catch (UnsupportedEncodingException e1) {
								
								e1.printStackTrace();
							} catch (IOException e1) {
								
								System.out.println("Fehler bei HTTP Request Spring Boot Server muss gestartet sein");
							}
							}
							
						 catch (IOException e1)  {
							
							//e1.printStackTrace();
						}
						
					}	
				}}}
			}
		});

		passwordField = new JPasswordField();
		passwordField.setBounds(94, 64, 96, 20);
		panel.setLayout(null);
		panel.add(lblNewLabel);
		panel.add(lblNewLabel_1);
		panel.add(lblNewLabel_3);
		panel.add(textField);
		panel.add(btnNewButton);
		panel.add(passwordField);
	}
}
