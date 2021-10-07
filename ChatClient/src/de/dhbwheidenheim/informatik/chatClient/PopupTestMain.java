package de.dhbwheidenheim.informatik.chatClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PopupTestMain {

	public static void main(String[] args) {
		IncomingCallPopup popup;
		try {
			ArrayList<String> list=new ArrayList<String>();
			list.add("blabla");
			list.add("blablabla");
			
			
			popup = new IncomingCallPopup(new URI("http://marci.vidmar.de"), "Organisator", false, list, list);
			popup.setVisible(true);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
