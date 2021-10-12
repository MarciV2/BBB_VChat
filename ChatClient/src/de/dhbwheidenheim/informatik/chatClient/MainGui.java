package de.dhbwheidenheim.informatik.chatClient;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.SwingConstants;

public class MainGui extends JFrame {
	private JTextField textField;
	private JPasswordField passwordField;
	static String username;

	public MainGui() {
		// Windowicon festlegen
		URL iconURL = getClass().getResource("/resources/BigBlueButton_icon.svg.png");
		ImageIcon icon = new ImageIcon(iconURL);
		this.setIconImage(icon.getImage());

		JLabel lblNewLabel = new JLabel("Anmelden:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 11, 87, 14);
		getContentPane().add(lblNewLabel);
		getContentPane().setLayout(null);
		JLabel lblNewLabel_1 = new JLabel("Benutzername:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 36, 100, 14);
		getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Passwort:");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(10, 61, 100, 14);
		getContentPane().add(lblNewLabel_2);

		textField = new JTextField();
		textField.setBounds(116, 33, 125, 20);
		getContentPane().add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(116, 58, 125, 20);
		getContentPane().add(passwordField);

		JButton btnNewButton_1 = new JButton("Anmelden");
		btnNewButton_1.addActionListener(new ActionListener() {
			// Button gedrückt
			public void actionPerformed(ActionEvent e) {
				// Registrierungsrequest
				String username = textField.getText();
				String p = new String(passwordField.getPassword());

				// Grundvorgaben der Felder
				if (username.isEmpty() || p.isEmpty())
					System.out.println("Bitte die Felder ausfüllen");
				else {
					if (username.length() > 15 || p.length() > 15)
						System.out.println("Die maximale Eingabelänge ist 16 Zeichen");
					else {

						// Passwort verschlüsseln
						String generatedPassword = null;
						try {
							MessageDigest md = MessageDigest.getInstance("SHA-512");
							md.reset();
							byte[] rawBytes = p.getBytes(StandardCharsets.UTF_8);
							byte[] bytes = md.digest(p.getBytes(StandardCharsets.UTF_8));
							StringBuilder sb = new StringBuilder();
							for (int i = 0; i < bytes.length; i++) {
								sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
							}
							generatedPassword = sb.toString();
						} catch (NoSuchAlgorithmException ex) {
							ex.printStackTrace();
						}
						// Url zum Aufruf mit Eingaben befüllen
						String Anfrage = "http://localhost:8080/login?username=" + username + "&password="
								+ generatedPassword;
						URL url;
						try {
							// HTTPRequest Erstellung

							url = new URL(Anfrage);
							HttpURLConnection con = (HttpURLConnection) url.openConnection();
							con.setRequestMethod("GET");
							// Abfrage der Rückgabe des Requests
							try (BufferedReader br = new BufferedReader(
									new InputStreamReader(con.getInputStream(), "utf-8"))) {
								StringBuilder response = new StringBuilder();
								String responseLine = null;
								while ((responseLine = br.readLine()) != null) {
									response.append(responseLine.trim());
								}
								if (response.isEmpty())
									System.out.println("Fehler bei der Antwort");
								else {
									if (response.toString().equals("true")) {
										System.out.println("Erfolgreich als: " + username + " angemeldet");
										// Aufrufen Hauptmenü bzw. einloggen als user mit dem gegebenen usernamen
										MainMenu mm = new MainMenu(username);
										mm.setVisible(true);
									} else
										System.out.println("Falscher Benutzername oder falsches Passwort");
								}
								con.disconnect();
							} catch (UnsupportedEncodingException e1) {

								e1.printStackTrace();
							} catch (IOException e1) {

								System.out.println("Fehler bei HTTP Request Spring Boot Server muss gestartet sein");
							}
						}

						catch (IOException e1) {

							// e1.printStackTrace();
						}

					}
				}
			}
		});
		btnNewButton_1.setBounds(6, 80, 100, 23);
		getContentPane().add(btnNewButton_1);

		JLabel lblNewLabel_3 = new JLabel("<html><u>Noch keinen Account?</u></<html>");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_3.addMouseListener(new MouseAdapter()
		// Button gedrückt
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				// Öffnen des Registrieren Windows
				Register r = new Register();
				r.setVisible(true);

			}
		});
		lblNewLabel_3.setBounds(116, 84, 186, 14);
		getContentPane().add(lblNewLabel_3);
	}

	public static void main(String[] args) {
		// Grundeigenschaften des Login Windows setzen
		MainGui gui = new MainGui();
		gui.setVisible(true);
//		gui.pack();
		gui.setResizable(false);
		gui.setSize(270, 150);
		gui.setLocationRelativeTo(null);
	}
}
