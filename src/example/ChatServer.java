package example;

import gdwNet.server.BasicClientConnection;
import gdwNet.server.BasicServer;
import gdwNet.server.ConnectionInfo;
import gdwUtils.DefaultCharSet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;

/**
 * Beispielimplementierung für den Server. 
 * So eine Klasse müsst ihr auch haben. Sie ist euere Anlaufstelle, wenn ihr Nachrichten bekommt
 * oder über einen Verbindungsaufbau bzw. -abbau informiert werdet. Hier könnt ihr auch Steuern ob
 * ein Client abgewiesen wird(vielleicht mögt seinen Nick nicht)
 * @author firen
 *
 */
public class ChatServer extends BasicServer
{
	private ServerCoreLoop coreLoop;
	
	private HashSet<String> takenNames; 

	/**
	 * Ihr müsste der Basisklasse die Spieleranzahl und den Infotext übergeben
	 * @param maxPlayer Maximale Spieleranzahl die ihr erlauben wollt
	 * @param infoText Der Text, der dem Client bei einer Listeabfrage gesendet wird.
	 * @throws IOException Ja es kann sein das es Probleme gibt bei erstellen der Netzwerkschnitstellen,
	 * dies ist meistens der Fall falls noch ein Serverläuft. Eingestellter Port wird bereitsverwendet.
	 */
	public ChatServer(int maxPlayer, String infoText) throws IOException
	{
		super(maxPlayer, infoText,false,false);
		
		this.takenNames = new HashSet<>();
		
		this.coreLoop = new ServerCoreLoop(this);
		this.coreLoop.start();
	}

	public static void main(String[] args)
	{
		try
		{
			new LogViewerChatServer().setRef(new ChatServer(5,
					"Chatserver"));
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(2);
		}
	}

	/**
	 * Ein Client möchte sich verbinden, so schaut nach was er möchte und winkt ihn durch oder 
	 * nicht.
	 * Gebt ihn dieser Klasse die von {@link BasicClientConnection} abgeleitet ist wenn ihr ihn akzeptiert
	 * oder eine <code>null</code> wenn nicht.
	 * 
	 * @param info Enthällt alle Informationen
	 */
	@Override
	protected BasicClientConnection incomingConnection(ConnectionInfo info,
			ByteBuffer data)
	{
		try
		{
			byte[] arr = new byte[data.get()];
			data.get(arr);
			String name = new String(arr, DefaultCharSet.getDefaultCharset());
			if(name.equalsIgnoreCase("idiot"))
			{
				return null;
			}else if(this.takenNames.contains(name))
			{
				return null;
			}else
			{
				return new MyBasicClientConnection(info, this, name);
			}
		}catch (Exception e)
		{
			return null;
		}
	}
	
	
	/**
	 * Ein Spieler hat die Verbindungverloren, das kann euer verschulden sein(kick)
	 * oder er hat einfach die Verbindung verloren.
	 * @param client Eine Referenz auf den Spieler
	 */
	@Override
	protected void playerDisconnected(BasicClientConnection client)
	{
		this.coreLoop.addLeaverName(((MyBasicClientConnection)client).name);
	}

	/**
	 * Eine Chatnachricht kommt rein, diese Methode ist von mir für Beispielzwecke.
	 * 
	 * @param name Der Nickname vom Spieler
	 * @param msg Die ChatNachricht
	 */
	protected void chatIncom(String name, String msg)
	{
		this.coreLoop.addMessage(name + ":> " + msg);
	}
	

	@Override
	protected void playerReconnected(BasicClientConnection client)
	{
		this.coreLoop.addJoinerName(((MyBasicClientConnection)client).name);		
	}

	

}
