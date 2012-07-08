package gdwNet.server;

import gdwNet.NETCONSTANTS;
import gdwNet.RESPONCECODES;
import gdwUtils.DefaultCharSet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public abstract class BasicServer
{
	// constants
	private static final long JOINREQUEST_TIMEOUT = 1000;

	// Threads
	private  MulticastresponceThread mThread;
	private final BroadcastresponceThread bThread;
	private final IncommingConnectionHandlerThread iThread;


	// attributes
	protected final int maxPlayer;
	private int currentConnections;
	private final HashMap<Integer, BasicClientConnection> clientConnections;
	private int idCounter;
	protected final String infoText;
	protected final long serverId;

	// to work with
	private final LinkedList<JoinRequestWrapper> requests;

	public BasicServer(int maxPlayer, String infoText) throws IOException
	{
		// setting up
		this.maxPlayer = maxPlayer;
		currentConnections = 0;
		this.clientConnections = new HashMap<Integer, BasicClientConnection>();
		this.idCounter = 0;
		this.infoText = infoText;
		this.serverId = new Random().nextLong();

		// to work with
		this.requests = new LinkedList<JoinRequestWrapper>();
		GDWServerLogger.logMSG("Server loaded");

		// start Threads
		this.iThread = new IncommingConnectionHandlerThread(this);
		GDWServerLogger.logMSG("TCP thread started on port "
				+ this.iThread.getBoundPort());
		
		this.bThread = new BroadcastresponceThread(maxPlayer, this.iThread.getBoundPort(), infoText, maxPlayer, this);
		
		try
		{
			this.mThread = new MulticastresponceThread(maxPlayer, this.iThread.getBoundPort(), infoText, maxPlayer, this);
		}catch (IOException e)
		{
			GDWServerLogger.logMSG("Es kann kein MulticastSocket erstellt werden, nicht weiter schlimm");
			this.mThread = null;
		}
		
		GDWServerLogger.logMSG("UDP thread started on port "
				+ this.bThread.getBoundedPort());
	}

	protected void handshake(SocketChannel socket)
	{
		requests.add(new JoinRequestWrapper(socket));
	}

	public int getCurrentConnections()
	{
		return currentConnections;
	}
	
	protected void proccedIncomming(boolean blockConnections)
	{
		if ((!blockConnections) || (currentConnections == maxPlayer))
		{
			int size = this.requests.size();
			for (int i = 0; i < size; ++i)
			{
				// connections
				try
				{
					byte responceCode = RESPONCECODES.OK;
					String name = null;
					int port = 0;
					JoinRequestWrapper req = this.requests.pop();
					ByteBuffer buf = ByteBuffer
							.allocate(NETCONSTANTS.PACKAGELENGTH);
					buf.clear();
					if (req.socket.read(buf) > 0)
					{

						// got message validate
						if (buf.position() > (1 + 4 + 1))// else incomplete
														// message(magicword+port+name)
						{
							buf.position(0);
							if (buf.get() == NETCONSTANTS.MAGIC_LOGIN_CODE)
							{

								port = buf.getInt();
								int nameSize = buf.getInt();
								if (nameSize < 1)
								{
									GDWServerLogger
											.logMSG("Namenslänge zu kurz");
									responceCode = RESPONCECODES.NICK_CORRUPTED;
								} else
								{
									byte[] arr = new byte[nameSize];
									buf.get(arr);
									name = new String(arr, DefaultCharSet.getDefaultCharset());
								}

							} else
							{
								GDWServerLogger
										.logMSG("Magic_Login_code falsch");
								responceCode = RESPONCECODES.DATA_CORRUPTED;
							}
						} else
						{
							GDWServerLogger.logMSG("Einlognachricht zu kurz");
							responceCode = RESPONCECODES.DATA_CORRUPTED;
						}

					} else
					{
						if (req.timestamp + JOINREQUEST_TIMEOUT < System
								.currentTimeMillis())
						{
							// waited too long kicking out
							responceCode = RESPONCECODES.TIMEOUT;
						} else
						{
							// still got time
							this.requests.add(req);
							continue;
						}

					}
					// send responceCode
					if (responceCode != RESPONCECODES.OK)
					{
						// send errormessage and kick
						sendErrorCodeToRequest(responceCode, req.socket);
						req.socket.close();
					} else
					{
						// give implementation to verfiy
						BasicClientConnection client = null;
						ConnectionInfo info = new ConnectionInfo(req.socket,
								port, name, idCounter);
						

						client = incommingConnection(info);
						if (client == null)
						{
							GDWServerLogger
									.logMSG("client denied from highlevel impl");
							// implentation dont like connection kick
							info.tcpConnection.close();
							info.udpConnection.close();
						} else
						{
							// add player in list
							this.clientConnections.put(idCounter, client);
							this.currentConnections++;

							// send ok with port and id
							ByteBuffer send = ByteBuffer.allocate(9);// code+port+id
							send.clear();
							send.put(responceCode);
							send.putInt(info.udpConnection.socket()
									.getLocalPort());

							send.putInt(info.id);
							send.flip();
							info.tcpConnection.write(send);
							idCounter++;
							GDWServerLogger.logMSG("Connection accepted");
						}
					}
				} catch (IOException e)
				{
				}
			}
		} else
		{
			if (!blockConnections)
			{
				GDWServerLogger.logMSG("max Player erreicht, lasse Verbindungen fallen");
			}
			requests.clear();
		}
	}
	
	protected void sendErrorCodeToRequest(byte code, ConnectionInfo info)
	{
		sendErrorCodeToRequest(code, info.tcpConnection);
	}

	private void sendErrorCodeToRequest(byte code, SocketChannel socket)
	{
		ByteBuffer send = ByteBuffer.allocate(1);
		send.flip();

		try
		{
			socket.write(send);
		} catch (IOException e)
		{
		}
	}

	protected abstract BasicClientConnection incommingConnection(
			ConnectionInfo info);

	public void shutMeDown()
	{
		if(this.mThread != null)
			this.mThread.interrupt();
		this.bThread.interrupt();
		this.iThread.interrupt();
	}

	public ByteBuffer getMessageBuffer()
	{
		ByteBuffer buf = ByteBuffer.allocate(NETCONSTANTS.PACKAGELENGTH);
		buf.put(NETCONSTANTS.MESSAGE);
		return buf;
	}

	public void proccedInputData(boolean blockConnection)
	{
		this.proccedIncomming(blockConnection);
		Iterator<Integer> iter = this.clientConnections.keySet().iterator();
		LinkedList<Integer> toLeave = new LinkedList<Integer>();
		while (iter.hasNext())
		{
			Integer pos = iter.next();
			BasicClientConnection client = this.clientConnections.get(pos);
			if (!client.checkForDisconnect(System.currentTimeMillis()))
			{
				// not disconnected
			} else
			{
				toLeave.add(pos);
			}
		}
		while (!toLeave.isEmpty())
		{
			disconnectPlayer(this.clientConnections.get(toLeave.pop()));
		}
		iter = this.clientConnections.keySet().iterator();
		while (iter.hasNext())
		{
			this.clientConnections.get(iter.next()).pollInput();
		}
		
		
	}

	protected abstract void PlayerDisconnected(BasicClientConnection client);

	private void disconnectPlayer(BasicClientConnection client)
	{
		PlayerDisconnected(client);
		--this.currentConnections;
		this.clientConnections.remove(client.id);

	}

	public void sendToAll(ByteBuffer buf, boolean safe)
	{
		Iterator<Integer> iter = this.clientConnections.keySet().iterator();
		while (iter.hasNext())
		{
			this.clientConnections.get(iter.next()).sendMsg(buf, safe);
		}
	}
}
