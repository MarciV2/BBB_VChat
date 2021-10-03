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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.FlowLayout;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.tree.DefaultTreeModel;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.tree.DefaultMutableTreeNode;

public class MainMenu extends JDialog {
	private JTextField textField;
	private String username;
	private IncomingCallPopup incomingCallPopup;
	
	public MainMenu(String username) {
		this.username = username;
		String userDaten=null;
		//HTTP Request der userDaten setzt
		
		MainMenu self=this;
		self.setAlwaysOnTop(true);
		self.setSize(1000, 1000);
		self.setResizable(false);
		getContentPane().setLayout(null);
		
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
		textField.setText(userDaten);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 31, 165, 158);
		panel.add(scrollPane);
		
		JTree tree = new JTree();
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Benutzer") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("Alle");
						node_1.add(new DefaultMutableTreeNode("blue"));
						node_1.add(new DefaultMutableTreeNode("violet"));
						node_1.add(new DefaultMutableTreeNode("red"));
						node_1.add(new DefaultMutableTreeNode("yellow"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Online");
						node_1.add(new DefaultMutableTreeNode("basketball"));
						node_1.add(new DefaultMutableTreeNode("soccer"));
						node_1.add(new DefaultMutableTreeNode("football"));
						node_1.add(new DefaultMutableTreeNode("hockey"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Besch\u00E4ftigt");
						node_1.add(new DefaultMutableTreeNode("hot dogs"));
						node_1.add(new DefaultMutableTreeNode("pizza"));
						node_1.add(new DefaultMutableTreeNode("ravioli"));
						node_1.add(new DefaultMutableTreeNode("bananas"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Offline");
						node_1.add(new DefaultMutableTreeNode("1"));
						node_1.add(new DefaultMutableTreeNode("2"));
						node_1.add(new DefaultMutableTreeNode("3"));
					add(node_1);
				}
			}
		));
		scrollPane.setViewportView(tree);
		
		JButton btnNewButton = new JButton("Ausgew\u00E4hlte Nutzer anrufen");
		btnNewButton.setBounds(180, 29, 249, 23);
		panel.add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("Status setzen:");
		lblNewLabel_1.setBounds(180, 63, 103, 14);
		panel.add(lblNewLabel_1);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Online");
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.setBounds(190, 84, 109, 23);
		panel.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Besch\u00E4ftigt");
		rdbtnNewRadioButton_1.setBounds(190, 110, 109, 23);
		panel.add(rdbtnNewRadioButton_1);
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Als Offline anzeigen");
		rdbtnNewRadioButton_2.setBounds(190, 136, 126, 23);
		panel.add(rdbtnNewRadioButton_2);
		
		JButton btnNewButton_1 = new JButton("Abmelden");
		btnNewButton_1.setBounds(180, 166, 89, 23);
		panel.add(btnNewButton_1);
		ButtonGroup gruppe = new ButtonGroup();
		gruppe.add(rdbtnNewRadioButton);
		gruppe.add(rdbtnNewRadioButton_1);
		gruppe.add(rdbtnNewRadioButton_2);
		treeSchreiben();
		Timer t = new Timer();
		
		t.scheduleAtFixedRate(
		    new TimerTask()
		    {
		        public void run()
		        {
		        	if(self.isVisible())
		        	{
		        	treeSchreiben();
//		        	amICalled();	//TODO wieder aktivieren
		        	//Abfrage der Personenliste und speichern in jtree
		            System.out.println("10 seconds passed");}
		        }
		    },
		    0,      // run first occurrence immediately
		    10000);  // run every three seconds
	
	amICalled();
	}
	void treeSchreiben()
	{
		
	}
	
	void amICalled() {
		JDialog self=this;
		String Anfrage = "http://localhost:8080/amICalled?username="+username;
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
					System.out.println("AmiCalled-Antwort: "+response.toString());
					//Nur popup anzeigen, wenn noch nicht angezeigt
					if(incomingCallPopup==null || !incomingCallPopup.isVisible()) {
						JSONObject call=new JSONObject(response.toString());
						JSONObject room=call.getJSONObject("chatRoom");
						URI roomURI=new URI(room.getString("roomURL"));
						boolean isPrivate=(boolean) call.getBoolean("callPrivate");
						ArrayList<String> invitees=new ArrayList<>();
						for(Object s: call.getJSONArray("invitees")) {
							JSONObject o=new JSONObject(String.valueOf(s));
							invitees.add(o.getString("username"));
						}
						ArrayList<String> attendees=new ArrayList<>();
						for(Object s: call.getJSONArray("attendees")) {
							JSONObject o=new JSONObject(String.valueOf(s));
							attendees.add(o.getString("username"));
						}
						String organizer=call.getJSONObject("organizer").getString("username");
//						invitees.remove(organizer);
//						invitees.remove(username);
						incomingCallPopup=new IncomingCallPopup(roomURI,organizer,isPrivate,invitees,attendees);
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
