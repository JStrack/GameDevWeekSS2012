package gdwNet.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public class ConnectionInfo
{
	public final String name;
	
	public final int id;
	
	public final SocketChannel tcpConnection;
	
	public final DatagramChannel udpConnection;

	public ConnectionInfo(SocketChannel tcpConnection,
 int port, String name,
			int id) throws IOException
	{
		this.name = name;
		this.id = id;
		this.tcpConnection = tcpConnection;

		this.udpConnection = DatagramChannel.open();
		this.udpConnection.socket().bind(null);
		this.udpConnection.connect(new InetSocketAddress(tcpConnection.socket()
				.getInetAddress(), port));

	}
	
	
}
