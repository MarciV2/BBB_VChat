Kurze Anleitung zum Starten BBB_VChats von Marcel Vidmar und Johannes Fahr:

1.  Chatclient und Chatserver Projekte zu einer IDE (entwickelt/getestet in Eclipse) hinzufügen.
2.  Innerhalb des Chatservers die ChatServerApplication.java ausführen.
3.  Sobald der Chatserver vollständig gestartet ist, kann man innerhalb des ChatClient Projekts MainGui.java starten.
4.  Um sich anmelden zu können muss erst ein account erstellt werden mit einem Klick auf "Noch keinen Account?" wird das Registrierungsfenster geöffnet.
5.  Username und Passwort eingeben und auf registrieren klicken.
6.  Nun kann sich mittels username und Passwort im MainGui angemeldet werden.
7.  Bei erfolgreichen Anmelden öffnet sich das MainMenu. Von diesem können Anrufe getätigt und empfangen werden, außerdem werden Benutzer und deren Status angezeigt und der eigene Status kann gesetzt werden.
8.  Um andere User anrufen zu können müssen diese Online sein und innerhalb des Jtrees ausgewählt(Mehrere User möglich) sein. Danach einfach auf den Button "Ausgewählte Nutzer anrufen" drücken.
9.  Ist man selbst einem Anruf zugeteilt, so öffnet sich das IncominCallPopup, dieses zeigt Teilnehmer, Eingeladene und Organisator an. Beim Drücken auf das grüne Annehmen-Symbol wird der dem Anruf zugeteilten Raum im Default-Web-Browser geöffnet.
10. Wenn der BBB Raum verlassen wird sollte der Button gedrückt oder das Fenster des PopupLeaveCall geschlossen werden. Aufgrund der Limitierungen des Servers muss manuell mitgeteilt werden wer den Anruf verlassen hat.

________________________________________________________________________________________________________________________

Allgemeine Informationen:

Benutzerliste wird alle 30 sek oder mittels Button "Benutzerliste aktualisieren" aktualisiert.
Checkbox "Privater Anruf ": Ursprünglich war geplant, öffentliche Chaträume erstellen zu können, welche als Nodes in der Benutzerliste dargestellt werden. Aus Zeitgründen konnte dies jedoch nicht fertiggestellt werden.
Eingehende Anrufe werden alle 10 sek abgefragt.
Es können mehrere User zu einem Anruf hinzugefügt werden.
Momentan werden die drei vorhanden Räume verwaltet. Sollten alle belegt sein, kann kein neuer Anruf erstellt werden bis ein Raum wieder freigegeben wird. (Alle Benutzer diesen verlassen haben)
Personen/Calls/Rooms-Listen werden gespeichert und sind somit auch Neustart des Servers noch verfügbar.
