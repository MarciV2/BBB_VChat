package de.dhbwheidenheim.informatik.chatClient;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import de.dhbwheidenheim.informatik.chatClient.PopupElements.CustomTreeCellRenderer;
import de.dhbwheidenheim.informatik.chatClient.PopupElements.CustomTreeNode;
import javax.swing.JCheckBox;

public class MainMenu extends JFrame {
	private JTextField textField;
	private String username;
	private IncomingCallPopup incomingCallPopup;
	private JTree tree;
	private CustomTreeNode top;
	private JCheckBox chckbxNewCheckBox;
	//	 private JTree tree = new JTree();
	private JScrollPane scrollPane;
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
		self.setSize(467, 305);
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
					//self.setVisible(false);
				}
			}
		});
		// Gui Elemente hinzufügen
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 444, 258);
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
		btnNewButton.addActionListener(new ActionListener() {
			//Button gedrückt
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> selectedusernames=new ArrayList<String>();
				//Vorgabe welche Nodes verboten sind
				String[] forbiddenNames= {"Benutzer","Online","Offline","Nicht Stören","Beschäftigt","Kein anderer User Registriert","Gesamt"};
				for(javax.swing.tree.TreePath tp:tree.getSelectionPaths()) {

					//Prüfen, dass nicht default-Nodes sondern wirklich ein Benutzer ausgewählt wurde
					String selectedusername=tp.getLastPathComponent().toString().split(" ")[0];
					if(!Arrays.asList(forbiddenNames).contains(selectedusername)) {

						//Prüfen, dass Benutzer auch online ist
						if(tp.getLastPathComponent().toString().split(" ")[1].equals("ONLINE")) {
							System.out.println("Benutzer ausgewählt: " +selectedusername);
							selectedusernames.add(selectedusername);
						}else {
							System.out.println("Benutzer "+selectedusername+" ist nicht Online und wird nicht eingeladen!");
						}
					}

				}
				//Wurde ein gültiger Name ausgewählt
				if(selectedusernames.size()==0) {
					System.out.println("Kein valider Benutzer ausgewählt->Abgebrochen!");
					return;
				}
				System.out.println("Benutzer für neuen Anruf ausgewählt: "+selectedusernames);

				//Eigntliche Anrufsfunktion
				String callType=chckbxNewCheckBox.isSelected()?"private":"public";
				String rückgabe = createCall(selectedusernames, callType);
				if(rückgabe.isEmpty())System.out.println("Keine freien Räume vorhanden");
				else
				System.out.println("Anruf erstellt?: "+rückgabe);
				
				//eigenes Popup für neuen anruf öffnen
				amICalled();
				


			}
		});
		btnNewButton.setBounds(225, 57, 204, 23);
		panel.add(btnNewButton);

		JLabel lblNewLabel_1 = new JLabel("Status setzen:");
		lblNewLabel_1.setBounds(235, 93, 103, 14);
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
		rdbtnNewRadioButton.setBounds(245, 114, 109, 23);
		panel.add(rdbtnNewRadioButton);

		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Beschäftigt");
		rdbtnNewRadioButton_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnNewRadioButton_1.isSelected())
					statusÄndern("BUSY");
			}
		});
		rdbtnNewRadioButton_1.setBounds(245, 140, 109, 23);
		panel.add(rdbtnNewRadioButton_1);

		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Nicht stören");
		rdbtnNewRadioButton_2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnNewRadioButton_2.isSelected())
					statusÄndern("DONOTDISTURB");
			}
		});
		rdbtnNewRadioButton_2.setBounds(245, 166, 126, 23);
		panel.add(rdbtnNewRadioButton_2);

		JRadioButton rdbtnNewRadioButton_3 = new JRadioButton("Als Offline anzeigen");
		rdbtnNewRadioButton_3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnNewRadioButton_3.isSelected())
					statusÄndern("OFFLINE");
			}
		});
		rdbtnNewRadioButton_3.setBounds(245, 196, 137, 23);
		panel.add(rdbtnNewRadioButton_3);

		JButton btnNewButton_1 = new JButton("Abmelden");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusÄndern("OFFLINE");
				self.setVisible(false);
			}
		});
		btnNewButton_1.setBounds(225, 226, 204, 23);
		panel.add(btnNewButton_1);
		// Zusammengruppieren der radiobuttons
		ButtonGroup gruppe = new ButtonGroup();
		gruppe.add(rdbtnNewRadioButton);
		gruppe.add(rdbtnNewRadioButton_1);
		gruppe.add(rdbtnNewRadioButton_2);
		gruppe.add(rdbtnNewRadioButton_3);


		//Scrollbar und tree initialisieren
		top = new CustomTreeNode(new ImageIcon(
				IncomingCallPopup.class.getResource("/resources/details.png"), "Benutzer"));
		tree=new JTree(top);
		scrollPane = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10, 31, 205, 184);
		getContentPane().add(scrollPane);
		// Tree renderer um Bilder darzustellen
		tree.setCellRenderer(new CustomTreeCellRenderer());



		JButton btnNewButton_2 = new JButton("Benutzerliste aktualisieren");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treeSchreiben();
			}
		});
		btnNewButton_2.setBounds(0, 226, 215, 23);
		panel.add(btnNewButton_2);
		
		//Möglichkeit für öffentliche Anrufe
		//Funktionalität für öffentliche Anrufe wurde durchdacht, bei mehr Zeit wäre dies auch umgesetzt worden.
		//Prinzip ist, dass man einen Anruf startet, dem dann alle beliebige Nutzer beitreten können. Dieser würde im Tree unter den Personen angezeigt, Bei Anclicken würde der "Anrufen" button zu "beitreten geändert.
		chckbxNewCheckBox = new JCheckBox("Privater Anruf");
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setBounds(272, 27, 137, 23);
		panel.add(chckbxNewCheckBox);

		// Timer für Baum-Aktualisierung erstellen
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (self.isVisible()) {
					// Benutzerliste wird aktualisiert
					treeSchreiben();


				}
			}
		}, 100, // Erster Aufruf nach 100ms
				30000); // Alle 30 Sekunden danach

		//Timer für Anruf-Prüfung erstellen
		Timer t2 = new Timer();
		t2.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (self.isVisible()) {
					amICalled();
				}
			}
		}, 100, // Erster Aufruf nach 100ms
				10000); // Alle 10 Sekunden danach


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
					top=new CustomTreeNode(new ImageIcon(
							IncomingCallPopup.class.getResource("/resources/details.png"), "Benutzer"));
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
					if (l.size()>0) {
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
					
					((DefaultTreeModel) tree.getModel()).setRoot(top);

					// Tree aufklappen
					for (int i = 0; i < tree.getRowCount(); i++) {
						tree.expandRow(i);
					}


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
/**
 * Funktion frägt mittels Polling ab ob ein Anruf existiert in den der User eingeladen wurde
 */
	void amICalled() {
		
		String Anfrage = "http://localhost:8080/amICalled?username=" + username;
		
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
					System.out.println("kein eingehender Anruf für Benutzer "+username);
				else {
					//System.out.println("AmiCalled-Antwort: " + response.toString());
					// Nur popup anzeigen, wenn noch nicht angezeigt
					if (incomingCallPopup == null || !incomingCallPopup.isVisible()) {
						
						//Details von Anruf extrahieren
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
						String id=String.valueOf(call.getInt("id"));
						String organizer = call.getJSONObject("organizer").getString("username");
						//						invitees.remove(organizer);
						//						invitees.remove(username);
						incomingCallPopup = new IncomingCallPopup(roomURI, organizer, isPrivate, invitees, attendees,id,username);
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

/**
 * Funktion erstellt einen Anruf anhand der übergebenen Liste
 * @param users Liste mit den anzurufenden Teilnehmer
 * @param callType gibt an ob Anruf privat/öffentlich ist
 * @return
 */
	private String createCall(ArrayList<String> users, String callType) {

		Map<String, String> parameters = new HashMap<>();
		parameters.put("organizername", username);
		parameters.put("callType", callType);

		//Einzuladende umformatieren und anhängen
		String inviteesStr="";
		for(String s:users) inviteesStr+=s+",";
		inviteesStr=inviteesStr.substring(0, inviteesStr.length()-1);
		parameters.put("invitees", inviteesStr);

		try {
			//Aufruf HTTPRequest Methode und die Antwort dieser wird zurückgegben
			return doHttpRequest("newCall", parameters);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

/**
 * Funktion um HTTPRequest für Amruf zu starten
 * @param reqMethod
 * @param params
 * @return
 * @throws IOException
 */
	public String doHttpRequest(String reqMethod, Map<String,String>params) throws IOException {
		//Aufruf Url Erstellung
		URL url = new URL("http://localhost:8080/"+reqMethod+"?"+ParameterStringBuilder.getParamsString(params));
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		//Antwort lesen
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		con.disconnect();

		//Gibt Antwort zurück an createCall Funktion
		return content.toString();
	}

}
