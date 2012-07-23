package gdwNet.server;

import gdwNet.NETCONSTANTS;
import gdwNet.RESPONCECODES;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class IncomingConnectionHandlerThread extends Thread
{
	private final static int WAIT_SOTIMEOUT = 600;

	private final ServerSocketChannel socket;
	private final BasicServer ref;
	private boolean close;
	private final int boundPort;

	protected IncomingConnectionHandlerThread(BasicServer ref)
			throws IOException
	{
		this.ref = ref;
		this.socket = ServerSocketChannel.open();
		InetSocketAddress isa = new InetSocketAddress(0);
		this.socket.socket().bind(isa);

		this.boundPort = this.socket.socket().getLocalPort();
		this.close = false;

		this.start();
	}

	@Override
	public void run()
	{
		while (!close)
		{
			SocketChannel socket = null;
			try
			{
				socket = this.socket.accept();
				socket.socket().setTcpNoDelay(true);
				socket.configureBlocking(true);
				socket.socket().setKeepAlive(true);
				socket.socket().setSoTimeout(WAIT_SOTIMEOUT);
				
				
				//get Input
				ByteBuffer buf = ByteBuffer.allocate(NETCONSTANTS.PACKAGELENGTH);
				if(socket.read(buf)<0)
				{
					sendError(RESPONCECODES.TIMEOUT, socket);
					continue;
				}
				// message validate
				
				int id = -1;
				int sharedSecredt = new Random().nextInt();
				int udpPort = -1;
				boolean recoRequest = false;
					
				buf.position(0);
				if (buf.get() == NETCONSTANTS.MAGIC_LOGIN_CODE)
				{
					//port
					udpPort = buf.getInt();
					
					//reco?
					switch (buf.get())
					{
					case NETCONSTANTS.CONNECT:
						recoRequest = false;
						
					break;
					
					case NETCONSTANTS.RECONNECT:
						recoRequest = true;
						id = buf.getInt();
						sharedSecredt = buf.getInt();
					break;

					default:
						sendError(RESPONCECODES.DATA_CORRUPTED, socket);
						continue;
					}

				} else
				{
					sendError(RESPONCECODES.DATA_CORRUPTED, socket);
				}// magiccode
				
				//tcp, udp socket create
				ConnectionInfo info = new ConnectionInfo(socket, udpPort, id, sharedSecredt);
				
				if(recoRequest)
				{
					this.ref.addReconnectRequest(info);
				}else
				{
					this.ref.addJoinRequest(info,buf);
				}
				
			} catch (SocketTimeoutException e)
			{
				continue;
			} catch (Exception e)
			{
				sendError(RESPONCECODES.DATA_CORRUPTED, socket);
			}
	
		}
		
	}

	private void sendError(byte code, SocketChannel socket)
	{
		if(socket == null)
			return;
		
		ByteBuffer buf =  ByteBuffer.allocate(1);
		buf.put(code);
		try
		{
			socket.write(buf);
			socket.close();
		} catch (IOException e)
		{
			//its ok...
		}
		
	}
		

	public int getBoundPort()
	{
		return boundPort;
	}

}
