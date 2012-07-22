package gdwNet.client;

import gdwNet.NETCONSTANTS;
import gdwNet.RESPONCECODES;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
/**
 * Die Hauptlientklasse. Sie hat keinen öffentlichen Konstruktor, sondern gibt euch über einen 
 * registierten {@link IBasicClientListener} ein Objekt diese Klassen, wenn die Verbindung erfolgreich
 * aufgebaut wurde.
 * 
 * Diese Klasse stellt die Verbindung zum Server da. Sie gibt einen NachrichtenbyteBuffer und
 * kann Nachrichten zum Server senden.
 * @author firen
 *
 */ 

public class BasicClient
{
	private static final int messagesPerUpdate = 5;

	private final DatagramChannel udpConnection;

	private final SocketChannel tcpConnection;

	private static ServerlistPendingThread pendingThread = null;

	protected final int id;
	
	protected final int sharedSecret;

	private static BasicClient ClientSingelton = null;

	private static IBasicClientListener listener = null;

	private long lastHearthbeat;

	private long pongRequest;

	private boolean discoFlag;
	
	private final ServerInfo lastServer;


	private BasicClient(DatagramChannel udpConnection,
			SocketChannel tcpConnection, int id, int secret, ServerInfo server)
	{
		this.udpConnection = udpConnection;
		this.tcpConnection = tcpConnection;
		try
		{
			this.tcpConnection.configureBlocking(false);
		} catch (IOException e)
		{
		}

		this.id = id;
		this.lastHearthbeat = System.currentTimeMillis();
		this.pongRequest = -1L;
		this.discoFlag = false;
		this.sharedSecret = secret;
		this.lastServer = server;
	}

	/**
	 * Registiert einen {@link IBasicClientListener} beim Client.
	 * Erforderlich, da ihr sonst überhaupt nicht verbinden könnt^^.
	 * @param lis Einen Referenz auf eueren Listener
	 */
	public static void setListener(IBasicClientListener lis)
	{
		BasicClient.listener = lis;
	}

	/**
	 * @return Gibt eine Referenz auf den aktuellen {@link IBasicClientListener} zurück.
	 * Null wenn nichts registiert ist.
	 */
	public static IBasicClientListener getListener()
	{
		return BasicClient.listener;
	}

	/**
	 * Diese Methode weißt den Client auf das Netzwerk nach Servern zu durchsuchen.
	 */
	public static void refreshServerList()
	{
		if (BasicClient.pendingThread != null)
		{
			BasicClient.pendingThread.interrupt();

		}

		try
		{
			BasicClient.pendingThread = new ServerlistPendingThread(
					listener);
		} catch (SocketException e)
		{

			e.printStackTrace();
		}

	}

	/**
	 * Weißt den Client an sich zu einen geben Server zu Verbinden.
	 * Benutzt diese Methode wenn ihr den Server nicht im Netzwerk gefunden habt, 
	 * aber sicher seit das er auf den übergebenen Addresse zu finden ist.
	 * @param address IPaddresse des Servers, muss nicht IPv4 sein, aber empfholen
	 * @param port Der Port aufdem der Server lauscht
	 * @param additionalData daten die ihr zur Anmeldung zusätzlich schicken wollt
	 */
	public static void connectToServer(InetAddress address, int port,
			ByteBuffer additionalData)
	{
		ServerInfo info = new ServerInfo("", 0, 0, 0, port, address, 0);
		BasicClient.connectToServer(info, additionalData);
	}

	/**
	 * Weißt den Client an sich zu einem Server zu verbinden. 
	 * Benutzt diese Methode mit den Daten die ihr bekommten habt beim Durchsuchen
	 * des Netzwerkes
	 * @param info Serverinfodaten
	 * @param additionalData daten die ihr zur Anmeldung zusätzlich schicken wollt
	 */
	public static void connectToServer(ServerInfo info, ByteBuffer additionalData)
	{
		try
		{
			SocketChannel tcpSocket = SocketChannel.open();
			DatagramChannel udpSocket = DatagramChannel.open();
			udpSocket.socket().bind(null);

			ByteBuffer buf = ByteBuffer.allocate(NETCONSTANTS.PACKAGELENGTH);
			buf.clear();
			buf.put(NETCONSTANTS.MAGIC_LOGIN_CODE);// magic code
			buf.putInt(udpSocket.socket().getLocalPort());// udp port
			buf.put(NETCONSTANTS.CONNECT);//what we want
			additionalData.flip();
			buf.put(additionalData);
			buf.flip();

			// connect
			IBasicClientListener lis = BasicClient.getListener();
			lis.connectionUpdate(RESPONCECODES.CONNECTING);

			new ConnectionResponceThread(tcpSocket, udpSocket, buf, info);
		} catch (IOException e)
		{
			BasicClient.getListener().connectionUpdate(
					RESPONCECODES.UNREACHABLE);
		}
	}
	
	public void reconnect()
	{
		try
		{
			SocketChannel tcpSocket = SocketChannel.open();
			DatagramChannel udpSocket = DatagramChannel.open();
			udpSocket.socket().bind(null);
			
			ByteBuffer buf = ByteBuffer.allocate(NETCONSTANTS.PACKAGELENGTH);
			buf.clear();
			buf.put(NETCONSTANTS.MAGIC_LOGIN_CODE);// magic code
			buf.putInt(udpSocket.socket().getLocalPort());// udp port
			buf.put(NETCONSTANTS.RECONNECT);//what we want
			buf.putInt(this.id);
			buf.putInt(this.sharedSecret);
			buf.flip();
		
			new ConnectionResponceThread(tcpSocket, udpSocket, buf, this.lastServer);
		} catch (IOException e)
		{
			BasicClient.getListener().connectionUpdate(
					RESPONCECODES.UNREACHABLE);
		}
	}
	
	protected static void registerClient(SocketChannel tcp,
			DatagramChannel udp, int id, int sharedSecret, ServerInfo server)
	{
		BasicClient client = new BasicClient(udp, tcp, id, sharedSecret , server);
		BasicClient.ClientSingelton = client;
		BasicClient.listener.connectionEstablished(ClientSingelton);

	}

	/**
	 * Ruft die Methode auf um die eingehenden Nachrichten vom Server zu erhalten.
	 * Sie überpüft ob ihr einen Verbindungsverlust hattet und ruft eure Netzwerkprotokol-
	 * implementierung auf.
	 * @return true wenn kein disconnect, false bei disconnect
	 */
	public boolean pollInput()
	{
		if ((this.discoFlag) || (checkDisconnect(System.currentTimeMillis())))
		{
			System.out.println("client schließt verbindung");
			disconnect();
			return false;
		}

		int counter = 0;
		try
		{

			for (; counter < messagesPerUpdate; ++counter)
			{
				ByteBuffer buf = ByteBuffer
						.allocate(NETCONSTANTS.PACKAGELENGTH);
				if (tcpConnection.read(buf) > 0)
				{
					this.incommingMessage(buf, true);
					continue;
				}
				if (udpConnection.read(buf) > 0)
				{
					this.incommingMessage(buf, false);
					continue;
				}
				break;
			}

		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		if (counter > 0)
		{
			this.lastHearthbeat = System.currentTimeMillis();
			this.pongRequest = -1L;
		}
		return true;

	}

	private void incommingMessage(ByteBuffer buf, boolean wasReliable)
	{
		buf.position(0);
		switch (buf.get())
		{
			case NETCONSTANTS.PING:
				sendPong();
				break;
			case NETCONSTANTS.PONG:
				this.pongRequest = -1L;
				break;

			case NETCONSTANTS.MESSAGE:
				listener.incommingMessage(buf, wasReliable);

			default:
				break;
		}

	}

	private boolean checkDisconnect(long currentTimeStamp)
	{
		if ((this.lastHearthbeat + NETCONSTANTS.HEARTBEAT_REQUESTIME) < currentTimeStamp)
		{
			// check if ping request is needed
			if (pongRequest > -1L)
			{
				if ((pongRequest + NETCONSTANTS.PONG_TIMEOUT) < currentTimeStamp)
				{
					// not alive
					this.discoFlag = true;
					return true;
				}
			} else
			{
				// send ping
				sendPing(currentTimeStamp);
			}
		}
		return false;
	}
	
	private void disconnect()
	{
		listener.connectionUpdate(RESPONCECODES.DISCONNECTED);
		try
		{
			this.tcpConnection.close();
			this.udpConnection.close();
		} catch (IOException e)
		{

		}
	}
	
	/**
	 * Ruft die Methode auf um einen Nachricht an den Server zu senden.
	 * @param msg Eure Nachricht
	 * @param wasReliable true wenn gesichert(TCP), false wenn ungesichert (UDP)
	 */
	public void sendMSG(ByteBuffer msg, boolean wasReliable)
	{
		msg.flip();
		try
		{
			if (wasReliable)
			{
				this.tcpConnection.write(msg);
			} else
			{
				this.udpConnection.write(msg);
			}
		} catch (IOException e)
		{
			this.discoFlag = true;
		}
	}

	private void sendPing(long currentTimeStamp)
	{
		ByteBuffer buf = ByteBuffer.allocate(1);
		buf.put(NETCONSTANTS.PING);
		buf.flip();
		try
		{
			this.udpConnection.write(buf);
			this.pongRequest = currentTimeStamp;
		} catch (IOException e)
		{
			// somethings wrong shutdown the connection
			e.printStackTrace();
			discoFlag = true;
		}
	}

	private void sendPong()
	{
		ByteBuffer buf = ByteBuffer.allocate(1);
		buf.put(NETCONSTANTS.PONG);
		buf.flip();

		try
		{
			this.udpConnection.write(buf);
		} catch (IOException e)
		{
			// somethings wrong shutdown the connection
			e.printStackTrace();
			discoFlag = true;
		}
	}

	/**
	 * Gibt einen NachrichtenByteBuffer zurück. Benutzt ihn um Nachrichten zu versenden.
	 * @return
	 */
	public ByteBuffer getMessageBuffer()
	{
		ByteBuffer buf = ByteBuffer.allocate(NETCONSTANTS.PACKAGELENGTH);
		buf.clear();
		buf.put(NETCONSTANTS.MESSAGE);
		return buf;
	}
}
