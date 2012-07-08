package gdwNet.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class JoinRequestWrapper
{
	public final SocketChannel socket;
	public final long timestamp;

	public JoinRequestWrapper(SocketChannel socket)
	{
		this.socket = socket;
		this.timestamp = System.currentTimeMillis();
		try
		{
			GDWServerLogger.logMSG(socket.getRemoteAddress().toString()
					+ " versucht verbindung aufzubauchen");
		} catch (IOException e)
		{

		}
	}

}
