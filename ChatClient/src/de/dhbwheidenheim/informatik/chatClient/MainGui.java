package de.dhbwheidenheim.informatik.chatClient;

import javax.swing.JFrame;
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

public class MainGui extends JFrame {
	private JTextField textField;
	private JPasswordField passwordField;
	static String id;
	public MainGui() {
		
		
		JButton btnNewButton = new JButton("openMenuAsTest");
		btnNewButton.setBounds(347, 0, 87, 261);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainMenu m= new MainMenu("test");
				m.setVisible(true);
			}
		});
		getContentPane().setLayout(null);
		getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Anmelden:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 11, 87, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Id:");
		lblNewLabel_1.setBounds(10, 36, 58, 14);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Passwort:");
		lblNewLabel_2.setBounds(10, 61, 58, 14);
		getContentPane().add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBounds(78, 33, 86, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		
		passwordField = new JPasswordField();
		passwordField.setBounds(78, 58, 86, 20);
		getContentPane().add(passwordField);
		
		JButton btnNewButton_1 = new JButton("Anmelden");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				 id = textField.getText();
				String p = passwordField.getText();
				if(id.isEmpty()||p.isEmpty())
					System.out.println("Bitte die Felder ausfüllen");
				else
				{
					if(id.length()>15||p.length()>15)
						System.out.println("Die maximale Eingabelänge ist 16 Zeichen");
					else
					{
				//Logik id und pw prüfen
						MainMenu mm=new MainMenu(id);
						mm.setVisible(true);
					}
				}
					
			}
		});
		btnNewButton_1.setBounds(10, 86, 89, 23);
		getContentPane().add(btnNewButton_1);
		
		JLabel lblNewLabel_3 = new JLabel("<html><u>Noch keinen Account?</u></<html>");
		lblNewLabel_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Register r= new Register();
				r.setVisible(true);
				
			}
		});
		lblNewLabel_3.setBounds(10, 120, 154, 14);
		getContentPane().add(lblNewLabel_3);
	}
	
	
	
	

	public static void main(String[] args) {
		MainGui gui=new MainGui();
		gui.setVisible(true);
		gui.pack();
		gui.setResizable(false);
		gui.setSize(500,500);
	}
}
