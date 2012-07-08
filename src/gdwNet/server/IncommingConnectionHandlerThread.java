package gdwNet.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class IncommingConnectionHandlerThread extends Thread
{
	private final ServerSocketChannel socket;
	private final BasicServer ref;
	private boolean close;
	private final int boundPort;

	protected IncommingConnectionHandlerThread(BasicServer ref)
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
			SocketChannel socket;
			try
			{
				socket = this.socket.accept();
				socket.socket().setTcpNoDelay(true);
				this.ref.handshake(socket);
			} catch (ClosedByInterruptException e)
			{
				this.close = true;
			} catch (IOException e)
			{
				this.close = true;
				e.printStackTrace();
			}
		}
		try
		{
			this.socket.close();
		} catch (IOException e)
		{

		}
		GDWServerLogger.logMSG("IncommingConHandlerThread beendet");
	}

	public int getBoundPort()
	{
		return boundPort;
	}

}
