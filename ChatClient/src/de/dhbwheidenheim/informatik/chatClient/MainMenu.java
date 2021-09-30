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
import javax.swing.tree.DefaultMutableTreeNode;

public class MainMenu extends JDialog {
	private JTextField textField;
	
	public MainMenu(String id) {
		String user = id;
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
		        	//Abfrage der Personenliste und speichern in jtree
		            System.out.println("10 seconds passed");}
		        }
		    },
		    0,      // run first occurrence immediately
		    10000);  // run every three seconds
	}
	void treeSchreiben()
	{
		
	}
}
