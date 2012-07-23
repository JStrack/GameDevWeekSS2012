package gdwNet.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public class ConnectionInfo
{
	
	public final int id;
	
	public final SocketChannel tcpConnection;
	
	public final DatagramChannel udpConnection;
	
	protected final int sharedSecret;

	public ConnectionInfo(SocketChannel tcpConnection,
 int port,
			int id, int secret) throws IOException
	{
		this.id = id;
		this.tcpConnection = tcpConnection;

		this.sharedSecret = secret;
		this.udpConnection = DatagramChannel.open();
		this.udpConnection.socket().bind(null);
		this.udpConnection.connect(new InetSocketAddress(tcpConnection.socket()
				.getInetAddress(), port));

	}
	
	/**
	 * Don´t run this methode manually.
	 */
	public void closeOpenSockets()
	{
		try
		{
			this.udpConnection.close();
			this.tcpConnection.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
}
