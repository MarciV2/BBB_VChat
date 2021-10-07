package de.dhbwheidenheim.informatik.chatClient;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTree;

import de.dhbwheidenheim.informatik.chatClient.PopupElements.CustomTreeCellRenderer;
import de.dhbwheidenheim.informatik.chatClient.PopupElements.CustomTreeNode;

public class IncomingCallPopup extends JDialog {

	public IncomingCallPopup(URI roomURL, String organizername ,boolean isPrivate, ArrayList<String> otherInvitees, ArrayList<String> attendees ) {
		setTitle("Eingehender Anruf!");
		setIconImage(Toolkit.getDefaultToolkit().getImage(IncomingCallPopup.class.getResource("/resources/acceptCall_small.png")));
		IncomingCallPopup self=this;


		this.setAlwaysOnTop(true);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Eingehender Anruf von ");
		lblNewLabel.setBounds(10, 0, 140, 30);
		getContentPane().add(lblNewLabel);

		JLabel lblOrgName=new JLabel(organizername);
		lblOrgName.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblOrgName.setBounds(150, 0, 200, 25);
		lblOrgName.setPreferredSize(new Dimension(100,30));
		getContentPane().add(lblOrgName);


		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setLocation(330, 30);
		btnNewButton_1.setIcon(new ImageIcon(IncomingCallPopup.class.getResource("/resources/declineCall_small.png")));
		btnNewButton_1.setToolTipText("ABLEHNEN");
		btnNewButton_1.setSize(100,100);
		btnNewButton_1.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				self.setVisible(false);
			}
		});

		JButton btnNewButton = new JButton("");
		btnNewButton.setBounds(10, 30, 100, 100);
		btnNewButton.setToolTipText("ANNEHMEN");
		btnNewButton.setIcon(new ImageIcon(IncomingCallPopup.class.getResource("/resources/acceptCall_small.png")));
		btnNewButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		btnNewButton.setBorderPainted(false);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setFocusPainted(false);
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

		getContentPane().add(btnNewButton);



		ImageIcon persons_icon=new ImageIcon(IncomingCallPopup.class.getResource("/resources/persons_tiny.png"));		
		ImageIcon person_icon=new ImageIcon(IncomingCallPopup.class.getResource("/resources/person-icon_tiny.png"));	
		ImageIcon plus_icon=new ImageIcon(IncomingCallPopup.class.getResource("/resources/plus.png"));	
		ImageIcon minus_icon=new ImageIcon(IncomingCallPopup.class.getResource("/resources/minus.png"));
		ImageIcon details_icon=new ImageIcon(IncomingCallPopup.class.getResource("/resources/details.png"));		



		

		CustomTreeNode top=new CustomTreeNode(details_icon,"Details");
		if(isPrivate)top.add(new CustomTreeNode(minus_icon,"Privater Anruf"));
		else top.add(new CustomTreeNode(plus_icon,"Öffentlicher Anruf"));

		CustomTreeNode inviteesNode=new CustomTreeNode(persons_icon,"Eingeladene");
		for(String s:otherInvitees) {
			CustomTreeNode node=new CustomTreeNode(person_icon,s);
			inviteesNode.add(node);

		}
		top.add(inviteesNode);




		CustomTreeNode attendeesNode=new CustomTreeNode(persons_icon,"Eingeladene");
		for(String s:attendees) {
			CustomTreeNode node=new CustomTreeNode(person_icon,s);
			attendeesNode.add(node);
		}
		top.add(attendeesNode);

		JTree tree = new JTree(top);
		tree.setBounds(120, 30, 200, 200);
		getContentPane().add(tree);
		tree.setCellRenderer(new CustomTreeCellRenderer());
		
		//Tree aufklappen
		for (int i = 0; i < tree.getRowCount(); i++) {
		    tree.expandRow(i);
		}

		getContentPane().add(btnNewButton_1);




		this.setSize(450,275);
		this.setResizable(false);
	}
}




