package example;

import java.nio.ByteBuffer;

import gdwNet.server.BasicClientConnection;
import gdwNet.server.BasicServer;
import gdwNet.server.ConnectionInfo;
import gdwNet.server.GDWServerLogger;
import gdwUtils.DefaultCharSet;
/**
 * Eine Beispielimplementierung für einen Clientverbindungsklasse. 
 * 
 * Sie nimmt nur Chatnachrichten entgegen und leitet dies an die ServerCoreLoop weiter.
 * @author firen
 *
 */
public class MyBasicClientConnection extends BasicClientConnection
{
	protected final String name;

	public MyBasicClientConnection(ConnectionInfo info, BasicServer ref, String name)
	{
		super(info, ref);
		this.name = name;
	}

	@Override
	/**
	 * Diese Methode muss implementiert werden. Hier wird euer Netzwerkprotokol implementiert.
	 * Haltet euch am besten an diesem Beispiel und fügt einfach neue case-Teile hinzu.
	 * @param msg der ByteBuffer der übergeben wird
	 * @param wasSafe Wurde die Nachricht über Tcp = true oder UDP = false versendet. Sofern es euch intressiert^^
	 */
	protected void incommingMessage(ByteBuffer msg, boolean wasSafe)
	{
		//lese Nachrichten code aus
		switch (msg.get())
		{
		case MyMessageConstants.ChatMessage:
			this.encodeChatmessage(msg);
			break;

		default:
			msg.position(msg.position() - 1);
			GDWServerLogger.logMSG("Unbekannte Nachricht" + msg.get());
			break;
		}
	}

	/**
	 * Es wurde eine Chatnachricht gesendet. 
	 * Ich lese sie aus und leite sie weiter an die ServerCoreLoop
	 * @param msg Die Nachricht
	 */
	private void encodeChatmessage(ByteBuffer msg)
	{
		//lese die Anzahl der bytes aus
		byte size = msg.get();
		byte[] arr = new byte[size];
		//fülle das byte array
		msg.get(arr);

		//wandele das ByteArray in einen String um. Nutze dafür das defaultcharset, 
		//welches schon definiert ist^^
		String message = new String(arr, DefaultCharSet.getDefaultCharset());

		//sende meiner Serverklasse die Nachricht
		((ChatServer) this.ref).chatIncom(this.name, message);
	}

}
