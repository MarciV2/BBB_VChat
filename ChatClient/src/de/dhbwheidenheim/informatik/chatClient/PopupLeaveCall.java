package de.dhbwheidenheim.informatik.chatClient;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;

import de.dhbwheidenheim.informatik.chatClient.PopupElements.CustomTreeCellRenderer;
import de.dhbwheidenheim.informatik.chatClient.PopupElements.CustomTreeNode;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

//Pop
public class PopupLeaveCall extends JDialog {
	private String username;
	private String id;

	public PopupLeaveCall(String username, String id) {
		this.username = username;
		this.id = id;
		URL iconURL = getClass().getResource("/resources/BigBlueButton_icon.svg.png");
		ImageIcon icon = new ImageIcon(iconURL);
		this.setIconImage(icon.getImage());
		PopupLeaveCall self = this;

		this.setAlwaysOnTop(true);

		this.setSize(300, 100);
		this.setResizable(false);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JButton btnNewButton = new JButton("Haben sie den Anruf im Browser verlassen?");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leaveCall();
			}
		});
		getContentPane().add(btnNewButton, BorderLayout.CENTER);
		// Abfangen des Schließen Buttons um leaveCall aufrufen zu können
		self.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(self,
						"Bitte nur schließen wenn sie den Anruf im Browser verlassen haben!!!", "Fenster schließen?",

						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					leaveCall();
					// self.setVisible(false);
				}
			}
		});
	}

	/*
	 * Funktion gibt Chatserver Bescheid das der Teilnehmer den Anruf verlassen hat
	 */
	void leaveCall() {

		// Url zum Aufruf befüllen
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
					// Schließen des Windows
					if (response.toString().equals("true")) {
						System.out.println("Erfolgreich den Anruf verlassen");
						this.setVisible(false);
					} else {
						System.out.println("Anruf bereits verlassen");
						this.setVisible(false);
					}
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
