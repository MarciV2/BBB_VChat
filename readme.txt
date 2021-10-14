Kurze Anleitung zum starten BBB_VChats von Marcel Vidmar und Johannes Fahr:

1.  Chatclient und Chatserver Projekte zu einer IDE (entwickelt/getestet in Eclipse) hinzufügen.
2.  Innerhalb des Chatservers die ChatServerApplication.java ausführen.
3.  Nach dem der Chatserver vollständig gestartet ist, innerhalb des ChatClient Projekts MainGui.java starten.
4.  Um sich anmelden zu können muss erst ein account erstellt werden mit einem Klick auf "Noch keinen Account?" wird das Registrierungsfenster geöffnet.
5.  Username und Passwort eingeben und auf registrieren klicken.
6.  Nun kann sich Mittels username und Passwort im MainGui angemeldet werden.
7.  Bei erfolgreichen anmelden öffnet sich das MainMenu von diesem können Anrufe getätigt und empfangen werden, außerdem werden Benutzer und deren Status angezeigt und der eigene Status kann gesetzt werden.
8.  Um andere User anrfen zu können sollten diese Online sein und innerhalb des Jtrees ausgewählt(Mehrere User möglich) sein. Danach einfach auf den Button "Ausgewälte Nutzer anrufen" drücken.
9.  Ist man selber einem Anruf zugeteilt so öffnet sich das IncominCallPopup, dieses zeigt Teilnehmer, Eingeladene und Organisator an. Bei drücken auf annehmen wird der dem Anruf zugeteilten Raum geöffnet.
10. Wenn der BBB Raum verlassen wird sollte der Button gedrückt oder das Fenster des PopupLeaveCall geschlossen werden. Aufgrund der Limitierungen des Servers muss manuell mitgeteilt werden wer den Anruf verlassen hat.

________________________________________________________________________________________________________________________

Allgemeine Informationen:

Benutzerliste wird alle 30 sek oder mittels Button "Benutzerliste aktualisieren" aktualisiert.
Chekcbox "Privater Anruf " ursprünglich war geplant öffentliche Chaträume zu ertsllen können und als Nodes in der Benutzerliste darstellen aus Zeitgründe konnte dies jedoch nicht fertiggestellt werden.
Eingehende Anrufe werden alle 10 sek abgefragt.
Es können mehrere User zu einem Anruf hinzugefügt werden.
Momentan werden die drei vorhanden Links verwaltet sollten alle belegt sein kann kein neuer Anruf erstellt werden bis ein Raum freigegeben wird.
Personen/calls/room Liste wird gespeichert und ist somit auch nach neustarten des Servers noch verfügbar.
