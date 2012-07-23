package example;

import gdwNet.client.BasicClient;
import gdwUtils.DefaultCharSet;

import java.nio.ByteBuffer;
/**
 * Speziell:
 * Beispielimplementierung für eine CoreLoop auf der Clientseite. Sie prüft ob eine Chatnachricht 
 * getippt wurde und ob Nachrichten vom Server kamen. 
 * 
 * Allgemein:
 * Eine so genannten CoreLoop ist eine Endlosschleife, die praktische in jedem Spiel anwendung findet.
 * Sie bildet die Grundeinheit und besteht aus normalerweise aus:
 * - update(hole Spielereingaben, zb; von Tastatur. Simuliere weiter das Spielgeschehen)
 * - draw(Zeichne das aktuelle Spielgeschehen)
 * 
 * @author firen
 *
 */
public class ClientCoreLoop extends Thread
{

	private BasicClient clientRef;
	private final ChatClient guiRef;

	private static final int MAXCHATINPUTLENGTH = 35;

	public ClientCoreLoop(BasicClient clientRef, ChatClient guiRef)
	{
		this.clientRef = clientRef;
		this.guiRef = guiRef;

		this.start();

	}

	public void setRef(BasicClient ref)
	{
		this.clientRef = ref;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				//Damit es nicht auf 100% CPU läuft, weil hier nichts geszeichnet wird
				sleep(250);
			} catch (InterruptedException e1)
			{
			}
			// würde mir mist gegeben? oder alles geschlossen
			if (this.clientRef == null)
			{
				return;
			}
			

			// frag nach ob etwas vom Server gesendet wurde
			if (!clientRef.pollInput())
			{
				//ich habe einen Verbindungsverlust
				return;
			}

			// frage die eventuell eingetippe Nachricht ab
			String msg = this.guiRef.getChatBuffer();
			if (msg.length() != 0)
			{
				// ist nicht leer
				if (msg.length() > MAXCHATINPUTLENGTH)
				{
					//schneide Nachricht ab, da Nachrichten an den Server
					//nur begrenzten Platz haben
					msg = msg.substring(0, MAXCHATINPUTLENGTH);
				}

				// lese das bytearray aus der Nachricht
				byte[] arr = msg.getBytes(DefaultCharSet.getDefaultCharset());

				// hole Nachrichten ByteBuffer
				ByteBuffer buf = this.clientRef.getMessageBuffer();
				
				// schreibe Chatnachricht code
				buf.put(MyMessageConstants.ChatMessage);

				// füge Läge des bytearrays ein
				buf.put((byte) arr.length);
				
				// füge bytearray ein
				buf.put(arr);

				// sende Nachricht an Server
				this.clientRef.sendMSG(buf, false);

			}
		}

	}
}
