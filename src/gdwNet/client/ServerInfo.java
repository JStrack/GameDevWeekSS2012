package gdwNet.client;

import java.net.InetAddress;

public class ServerInfo
{
	public final String infoMsg;

	public final int currentPlayer;

	public final int maxPlayer;

	public final int ping;

	public final int port;

	public final long id;

	public final InetAddress address;

	public ServerInfo(String infoMsg, int currentPlayer, int maxPlayer,
			int ping, int port, InetAddress address, long id)
	{
		this.infoMsg = infoMsg;
		this.currentPlayer = currentPlayer;
		this.maxPlayer = maxPlayer;
		this.ping = ping;
		this.port = port;
		this.address = address;
		this.id = id;
	}

}
