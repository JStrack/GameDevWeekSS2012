package gdwNet.server;

import gdwNet.NETCONSTANTS;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public abstract class BasicClientConnection
{
	private static final int messagesPerUpdate = 5;

	private  DatagramChannel udpConnection;

	private  SocketChannel tcpConnection;
	
	protected final int sharedSecret;

	protected int id;

	private long lastHearthbeat;

	private long pongRequest;

	private boolean discoFlag;

	protected final BasicServer ref;

	public BasicClientConnection(ConnectionInfo info, BasicServer ref)
	{
		this.ref = ref;
		this.id = info.id;
		this.lastHearthbeat = System.currentTimeMillis();
		this.discoFlag = false;
		this.pongRequest = -1L;
		this.udpConnection = info.udpConnection;
		this.tcpConnection = info.tcpConnection;
		this.sharedSecret = info.sharedSecret;
		
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

	public void sendMSG(ByteBuffer msg, boolean reliable)
	{
		msg.flip();
		try
		{
			if (reliable)
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

	private void incommingMsg(ByteBuffer buf, boolean wasReliable)
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
			this.incommingMessage(buf, wasReliable);

		default:
			break;
		}
	}

	protected void revive(ConnectionInfo info)
	{
		this.tcpConnection = info.tcpConnection;
		this.udpConnection = info.udpConnection;
		this.discoFlag = false;	
	}
	
	protected abstract void incommingMessage(ByteBuffer buf, boolean wasReliable);

	public int getId()
	{
		return id;
	}
}
