package gdwNet.server;

import gdwNet.NETCONSTANTS;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public abstract class BasicClientConnection
{
	private static final int messagesPerUpdate = 5;

	private final DatagramChannel udpConnection;

	private final SocketChannel tcpConnection;

	protected final int id;

	protected final String name;

	private long lastHearthbeat;

	private long pongRequest;

	private boolean discoFlag;

	protected final BasicServer ref;

	public BasicClientConnection(ConnectionInfo info, BasicServer ref)
	{
		this.ref = ref;
		this.id = info.id;
		this.name = info.name;
		this.lastHearthbeat = System.currentTimeMillis();
		this.discoFlag = false;
		this.pongRequest = -1L;
		this.udpConnection = info.udpConnection;
		this.tcpConnection = info.tcpConnection;
		
		try
		{
			this.udpConnection.configureBlocking(false);
			this.tcpConnection.configureBlocking(false);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

	public boolean checkForDisconnect(long current)
	{
		if ((this.lastHearthbeat + NETCONSTANTS.HEARTBEAT_REQUESTIME) < current)
		{
			// check if ping request is needed
			if (pongRequest > -1L)
			{
				if ((pongRequest + NETCONSTANTS.PONG_TIMEOUT) < current)
				{
					// not alive
					this.discoFlag = true;
					return true;
				}
			} else
			{
				// send ping
				sendPing(current);
			}
		}
		return false;
	}

	public boolean isDisconnectFlaged()
	{
		return discoFlag;
	}

	public void sendMsg(ByteBuffer buf, boolean safe)
	{
		try
		{
			buf.flip();
			if (safe)
			{
				this.tcpConnection.write(buf);

			} else
			{
				this.udpConnection.write(buf);
			}
		} catch (IOException e)
		{}

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

	public void disconnect()
	{
		try
		{
			this.tcpConnection.close();
			this.udpConnection.close();
		} catch (IOException e)
		{

		}
	}

	public void sendMSG(ByteBuffer msg, boolean isSafe)
	{
		msg.flip();
		try
		{
			if (isSafe)
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

	public boolean pollInput()
	{

		int counter = 0;
		try
		{

			for (; counter < messagesPerUpdate; ++counter)
			{
				ByteBuffer buf = ByteBuffer
						.allocate(NETCONSTANTS.PACKAGELENGTH);
				if (tcpConnection.read(buf) > 0)
				{
					this.incommingMsg(buf, true);
					continue;
				}
				if (udpConnection.read(buf) > 0)
				{
					this.incommingMsg(buf, false);
					continue;
				}
				break;
			}

		} catch (IOException e)
		{
			//e.printStackTrace();
			this.discoFlag = true;
			return false;
		}
		if (counter > 0)
		{
			this.lastHearthbeat = System.currentTimeMillis();
			this.pongRequest = -1L;
		}
		return true;

	}

	private void incommingMsg(ByteBuffer buf, boolean wasSafe)
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
			this.incommingMessage(buf, wasSafe);

		default:
			break;
		}
	}

	protected abstract void incommingMessage(ByteBuffer buf, boolean wasSafe);

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

}
