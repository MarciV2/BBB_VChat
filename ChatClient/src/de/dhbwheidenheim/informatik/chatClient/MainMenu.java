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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.FlowLayout;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.dhbwheidenheim.informatik.chatClient.PopupElements.CustomTreeCellRenderer;
import de.dhbwheidenheim.informatik.chatClient.PopupElements.CustomTreeNode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class MainMenu extends JFrame {
	private JTextField textField;
	private String username;
	private IncomingCallPopup incomingCallPopup;
	// private JTree tree = new JTree();

	public MainMenu(String username) {
		// Speichern des eigenen usernames
		this.username = username;

		MainMenu self = this;
		// Windowicon setzen
		URL iconURL = getClass().getResource("/resources/BigBlueButton_icon.svg.png");
		ImageIcon icon = new ImageIcon(iconURL);
		this.setIconImage(icon.getImage());
		// Grundeinstellung für Fenster wie Größe festlegen und Veränderungen verbieten
		self.setAlwaysOnTop(true);
		self.setSize(1000, 1000);
		self.setResizable(false);
		getContentPane().setLayout(null);
		// Fenster in der Mitte zentriert öffnen
		setLocationRelativeTo(null);

		// Abfangen des Schließen Buttons um Status auf Offline setzen zu können
		self.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(self, "Sind sie sich sicher das sie das Fenster schließen wollen? ",
						"Fenster schließen?",

						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					statusÄndern("OFFLINE");
					self.setVisible(false);
				}
			}
		});
		// Gui Elemente hinzufügen
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 439, 309);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Sie sind angemeldet als:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(0, 0, 175, 17);
		panel.add(lblNewLabel);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(180, 0, 249, 20);
		panel.add(textField);
		textField.setColumns(10);
		textField.setText(username);

		JButton btnNewButton = new JButton("Ausgew\u00E4hlte Nutzer anrufen");
		btnNewButton.setBounds(225, 29, 204, 23);
		panel.add(btnNewButton);

		JLabel lblNewLabel_1 = new JLabel("Status setzen:");
		lblNewLabel_1.setBounds(235, 59, 103, 14);
		panel.add(lblNewLabel_1);
		// Wenn sich Status eines Radiobuttons ändert und der Zustand selected ist wird
		// die dazugehörige Status setzen Funktion geöffnet
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Online");
		rdbtnNewRadioButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnNewRadioButton.isSelected())
					statusÄndern("ONLINE");
			}
		});
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.setBounds(245, 80, 109, 23);
		panel.add(rdbtnNewRadioButton);

		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Beschäftigt");
		rdbtnNewRadioButton_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnNewRadioButton_1.isSelected())
					statusÄndern("BUSY");
			}
		});
		rdbtnNewRadioButton_1.setBounds(245, 106, 109, 23);
		panel.add(rdbtnNewRadioButton_1);

		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Nicht stören");
		rdbtnNewRadioButton_2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnNewRadioButton_2.isSelected())
					statusÄndern("DONOTDISTURB");
			}
		});
		rdbtnNewRadioButton_2.setBounds(245, 132, 126, 23);
		panel.add(rdbtnNewRadioButton_2);

		JRadioButton rdbtnNewRadioButton_3 = new JRadioButton("Als Offline anzeigen");
		rdbtnNewRadioButton_3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnNewRadioButton_3.isSelected())
					statusÄndern("OFFLINE");
			}
		});
		rdbtnNewRadioButton_3.setBounds(245, 162, 137, 23);
		panel.add(rdbtnNewRadioButton_3);

		JButton btnNewButton_1 = new JButton("Abmelden");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusÄndern("OFFLINE");
				self.setVisible(false);
			}
		});
		btnNewButton_1.setBounds(225, 192, 204, 23);
		panel.add(btnNewButton_1);
		// Zusammengruppieren der radiobuttons
		ButtonGroup gruppe = new ButtonGroup();
		gruppe.add(rdbtnNewRadioButton);
		gruppe.add(rdbtnNewRadioButton_1);
		gruppe.add(rdbtnNewRadioButton_2);
		gruppe.add(rdbtnNewRadioButton_3);

		JButton btnNewButton_2 = new JButton("Benutzerliste aktualisieren");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treeSchreiben();
			}
		});
		btnNewButton_2.setBounds(10, 226, 205, 23);
		panel.add(btnNewButton_2);

		// Timer erstellen
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (self.isVisible()) {
					// Benutzerliste wird aktualisiert
					treeSchreiben();

//		        	amICalled();	//TODO wieder aktivieren

				}
			}
		}, 100, // Erster Aufruf nach 100ms
				30000); // Alle 30 Sekunden danach

		amICalled();
	}

	/**
	 * Funktion holt Benutzerliste und schreibt diese in JTree
	 */
	void treeSchreiben() {

		// Url zum Aufruf mit Eingaben befüllen
		String Anfrage = "http://localhost:8080/listPersons";
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

					List<String> l = new ArrayList<String>();
					// JSONArray mit Daten aus Rückgabe befüllen
					JSONArray ja = new JSONArray(response.toString());
					// Icons hinzufügen
					ImageIcon persons_icon = new ImageIcon(
							IncomingCallPopup.class.getResource("/resources/persons_tiny.png"));
					ImageIcon person_icon = new ImageIcon(
							IncomingCallPopup.class.getResource("/resources/person-icon_tiny.png"));
					ImageIcon details_icon = new ImageIcon(
							IncomingCallPopup.class.getResource("/resources/details.png"));
					// Erstellen der Nodes
					CustomTreeNode top = new CustomTreeNode(details_icon, "Benutzer");
					CustomTreeNode onlineNode = new CustomTreeNode(persons_icon, "Online");
					CustomTreeNode busyNode = new CustomTreeNode(persons_icon, "Beschäftigt");
					CustomTreeNode dontdisturbNode = new CustomTreeNode(persons_icon, "Nicht stören");
					CustomTreeNode offlineNode = new CustomTreeNode(persons_icon, "Offline");
					CustomTreeNode gesNode = new CustomTreeNode(persons_icon, "Gesamt");
					CustomTreeNode keinNode = new CustomTreeNode(persons_icon, "Kein anderer User Registriert");
					// Hilfsvariablen
					String hilf[];
					String s;
					boolean n1 = false, n2 = false, n3 = false, n4 = false;
					// Daten aus Jsonarray in Liste übertragen
					for (int i = 0; i < ja.length(); i++) {
						hilf = ja.get(i).toString().substring(10).split("\"");
						// Falls username der eigene dann nicht aufnehmen in Liste
						if (!hilf[4].equals(username))
							l.add(hilf[4] + " " + hilf[0]);

					}
					// Liste sortieren
					l.sort(null);

					// Liste leer?
					if (l.get(0) != null) {
						// Für alle Listenelemente einmal abfragen
						for (int i = 0; i < l.size(); i++) {
							s = l.get(i).toString();
							// Abfrage des Status der username und dann hinzufügen zur passenden Node und
							// true setzen des zugehörigen boolean
							if (s.contains("ONLINE")) {
								n1 = true;
								CustomTreeNode node = new CustomTreeNode(person_icon, s);
								onlineNode.add(node);
							}
							if (s.contains("BUSY")) {
								n2 = true;
								CustomTreeNode node = new CustomTreeNode(person_icon, s);
								busyNode.add(node);
							}
							if (s.contains("DONOTDISTURB")) {
								n3 = true;
								CustomTreeNode node = new CustomTreeNode(person_icon, s);
								dontdisturbNode.add(node);
							}
							if (s.contains("OFFLINE")) {
								n4 = true;
								CustomTreeNode node = new CustomTreeNode(person_icon, s);
								offlineNode.add(node);
							}
							CustomTreeNode node = new CustomTreeNode(person_icon, s);
							gesNode.add(node);
						}

						// Nodes hinzufügen wenn User innerhalb der Nodes gespeichert
						if (n1)
							top.add(onlineNode);
						if (n2)
							top.add(busyNode);
						if (n3)
							top.add(dontdisturbNode);
						if (n4)
							top.add(offlineNode);
						top.add(gesNode);

					} else {
						// Falls Liste leer nur eine Node mit keine anderen User registriert
						top.add(keinNode);
					}

					// Tree erstellen
					JTree tree = new JTree(top);

					// Tree aufklappen
					for (int i = 0; i < tree.getRowCount(); i++) {
						tree.expandRow(i);
					}

					// Scrollpane erstellen und tree mitgeben
					JScrollPane scrollPane = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setBounds(10, 31, 205, 184);
					getContentPane().add(scrollPane);
					// Tree renderer um Bilder darzustellen
					tree.setCellRenderer(new CustomTreeCellRenderer());
					System.out.println("Benutzerliste für:" + username + " wurde aktualisiert");

				}
				// Verbindung trennen
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

	/**
	 * Funktion ändert den Status des Benutzers
	 * 
	 * @param state gewünschter Status, mögliche sind:
	 *              ONLINE,OFFLINE,BUSY,DONOTDISTURB
	 */
	void statusÄndern(String state) {

		// Url zum Aufruf mit Eingaben befüllen
		String Anfrage = "http://localhost:8080/setPersonState?username=" + username + "&state=" + state;
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
						System.out.println("Erfolgreich Status von:" + username + " auf: " + state + " gesetzt");
					else
						System.out.println("Status konnte nicht gesetzt werden");

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

	void amICalled() {
		JFrame self = this;
		String Anfrage = "http://localhost:8080/amICalled?username=" + username;
		System.out.println(Anfrage);
		URL url;
		try {
			url = new URL(Anfrage);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				if (response.isEmpty())
					System.out.println("Fehler bei der Antwort");
				else {
					System.out.println("AmiCalled-Antwort: " + response.toString());
					// Nur popup anzeigen, wenn noch nicht angezeigt
					if (incomingCallPopup == null || !incomingCallPopup.isVisible()) {
						JSONObject call = new JSONObject(response.toString());
						JSONObject room = call.getJSONObject("chatRoom");
						URI roomURI = new URI(room.getString("roomURL"));
						boolean isPrivate = (boolean) call.getBoolean("callPrivate");
						ArrayList<String> invitees = new ArrayList<>();
						for (Object s : call.getJSONArray("invitees")) {
							JSONObject o = new JSONObject(String.valueOf(s));
							invitees.add(o.getString("username"));
						}
						ArrayList<String> attendees = new ArrayList<>();
						for (Object s : call.getJSONArray("attendees")) {
							JSONObject o = new JSONObject(String.valueOf(s));
							attendees.add(o.getString("username"));
						}
						String organizer = call.getJSONObject("organizer").getString("username");
//						invitees.remove(organizer);
//						invitees.remove(username);
						incomingCallPopup = new IncomingCallPopup(roomURI, organizer, isPrivate, invitees, attendees);
					}
					incomingCallPopup.setVisible(true);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			con.disconnect();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
