package de.dhbwheidenheim.informatik.chatClient;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import de.dhbwheidenheim.informatik.chatClient.PopupElements.CustomTreeCellRenderer;
import de.dhbwheidenheim.informatik.chatClient.PopupElements.CustomTreeNode;

public class IncomingCallPopup extends JFrame {
	private JScrollPane scrollPane;
	private Clip clip;
	private JButton btnNewButton_1;

	public IncomingCallPopup(URI roomURL, String organizername, boolean isPrivate, ArrayList<String> otherInvitees,
			ArrayList<String> attendees, String id, String username) {

		// Icons laden
		ImageIcon persons_icon = new ImageIcon(IncomingCallPopup.class.getResource("/resources/persons_tiny.png"));
		ImageIcon person_icon = new ImageIcon(IncomingCallPopup.class.getResource("/resources/person-icon_tiny.png"));
		ImageIcon details_icon = new ImageIcon(IncomingCallPopup.class.getResource("/resources/details.png"));
		ImageIcon accept_icon = new ImageIcon(IncomingCallPopup.class.getResource("/resources/acceptCall_small.png"));
		ImageIcon decline_icon = new ImageIcon(IncomingCallPopup.class.getResource("/resources/declineCall_small.png"));
		ImageIcon plus_icon = new ImageIcon(IncomingCallPopup.class.getResource("/resources/plus.png"));
		ImageIcon minus_icon = new ImageIcon(IncomingCallPopup.class.getResource("/resources/minus.png"));

		setTitle("Eingehender Anruf!");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(IncomingCallPopup.class.getResource("/resources/acceptCall_small.png")));
		IncomingCallPopup self = this;

		try {
			// Audiofile mit loop abspielen wird beim Schlie�en vom window oder
			// annehmen/ablehen gestoppt
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(getClass().getResource("/resources/callsound.wav").toURI()));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
		this.setSize(450, 275);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		getContentPane().setLayout(null);

		// Windowicon festlegen
		URL iconURL = getClass().getResource("/resources/BigBlueButton_icon.svg.png");
		ImageIcon icon = new ImageIcon(iconURL);
		this.setIconImage(icon.getImage());

		// Label zur �berschrift bestimmen und anzeigen
		String headline;
		if (username.equals(organizername))
			headline = "Angemeldet als: " + username + " eigenem Anruf beitreten?";
		else
			headline = "Eingehender Anruf an: " + username + " von: " + organizername;
		JLabel lblNewLabel = new JLabel(headline);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 0, 420, 30);
		getContentPane().add(lblNewLabel);

		// Button ohne Rand und so machen, nur clickbares icon
		btnNewButton_1 = new JButton("");
		btnNewButton_1.setLocation(330, 30);
		btnNewButton_1.setIcon(new ImageIcon(IncomingCallPopup.class.getResource("/resources/declineCall_small.png")));
		btnNewButton_1.setToolTipText("ABLEHNEN");
		btnNewButton_1.setSize(100, 100);
		btnNewButton_1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			// Button gedrückt
			public void actionPerformed(ActionEvent e) {
				// Ablehnen des Anrufs
				String Anfrage = "http://localhost:8080/leaveCall?username=" + username + "&callID=" + id;
				URL url;
				try {
					// HTTPRequest Erstellung

					url = new URL(Anfrage);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					// Abfrage der Rückgabe des Requests
					try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
						StringBuilder response = new StringBuilder();
						String responseLine = null;
						while ((responseLine = br.readLine()) != null) {
							response.append(responseLine.trim());
						}
						if (response.isEmpty())
							System.out.println("Fehler bei der Antwort");
						else {
							if (response.toString().equals("true"))
								System.out
										.println("Erfolgreich Anruf als:" + username + " mit ID: " + id + " abgelehnt");
							else
								System.out.println("Fehler beim Ablehnen des Anruf");

						}
						con.disconnect();
					} catch (UnsupportedEncodingException e1) {

						e1.printStackTrace();
					} catch (IOException e1) {

						System.out.println("Fehler bei HTTP Request Spring Boot Server muss gestartet sein");
					}
				}

				catch (IOException e1) {
				}

				clip.stop();
				self.setVisible(false);
			}
		});
// Abfangen des Schließen Buttons um ANruf abzulehnen/ Sound beenden
		self.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(self, "Sind sie sich sicher das sie den Anruf ablehen wollen? ",
						"Fenster schlie�en?",

						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					btnNewButton_1.doClick();
				}
			}
		});

		// Button ohne Rand und so machen, nur clickbares icon
		JButton btnNewButton = new JButton("");
		btnNewButton.setBounds(10, 30, 100, 100);
		btnNewButton.setToolTipText("ANNEHMEN");
		btnNewButton.setIcon(new ImageIcon(IncomingCallPopup.class.getResource("/resources/acceptCall_small.png")));
		btnNewButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnNewButton.setBorderPainted(false);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setFocusPainted(false);
		btnNewButton.addActionListener(new ActionListener() {
			// Button gedrückt
			public void actionPerformed(ActionEvent e) {
				try {
					// Anruf annehmen
					String Anfrage = "http://localhost:8080/joinCall?callID=" + id + "&username=" + username;
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
								System.out
										.println("Erfolgreich Anruf an:" + username + " mit ID: " + id + " angenommen");
							}
							con.disconnect();
						} catch (UnsupportedEncodingException e1) {

							e1.printStackTrace();
						} catch (IOException e1) {

							System.out.println("Fehler bei HTTP Request Spring Boot Server muss gestartet sein");
						}
					} catch (IOException e1) {
					}

					Desktop.getDesktop().browse(roomURL);
					PopupLeaveCall lc = new PopupLeaveCall(username, id);
					lc.setVisible(true);
					self.setVisible(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				clip.stop();
			}
		});

		getContentPane().add(btnNewButton);

		// Rootnode
		CustomTreeNode top = new CustomTreeNode(details_icon, "Details");
		// Node gibt an ob Anruf privat
		if (isPrivate)
			top.add(new CustomTreeNode(minus_icon, "Privater Anruf"));
		else
			top.add(new CustomTreeNode(plus_icon, "Öffentlicher Anruf"));

		// Node für die eingeladenen User
		CustomTreeNode inviteesNode = new CustomTreeNode(persons_icon, "Eingeladene");
		for (String s : otherInvitees) {
			CustomTreeNode node = new CustomTreeNode(person_icon, s);
			inviteesNode.add(node);

		}
		top.add(inviteesNode);

		// Node für die User die bereits beigetreten sind
		CustomTreeNode attendeesNode = new CustomTreeNode(persons_icon, "Anwesende");
		for (String s : attendees) {
			CustomTreeNode node = new CustomTreeNode(person_icon, s);
			attendeesNode.add(node);
		}
		top.add(attendeesNode);

		// Tree erstellen anhand von vorhandenen Nodes
		JTree tree = new JTree(top);
		tree.setCellRenderer(new CustomTreeCellRenderer());

		// Tree aufklappen
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
		// Scrollpane erstelln und tree hier hinzufügen um diesen scrollbar zu machen
		scrollPane = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(120, 30, 200, 200);
		getContentPane().add(scrollPane);

		getContentPane().add(btnNewButton_1);

	}
}
