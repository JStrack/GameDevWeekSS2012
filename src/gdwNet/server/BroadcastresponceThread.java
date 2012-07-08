package gdwNet.server;

import gdwNet.NETCONSTANTS;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class BroadcastresponceThread extends Thread
{
	private final int tcpBoundPort;
	private final DatagramSocket socket;
	private final String infoText;
	private final int maxPlayer;
	private final BasicServer ref;
	private boolean close;
	// public final NetworkInterface netInf;
	public final int boundedPort;
	private final long serverId;

	public BroadcastresponceThread(long serverId, int tcpBoundPort,
			String infoText, int maxPlayer, BasicServer ref) throws IOException
	{
		this.serverId = serverId;
		this.tcpBoundPort = tcpBoundPort;
		this.socket = new DatagramSocket(NETCONSTANTS.BROADCAST_PORT);
		// this.netInf = netInf;
		this.boundedPort = this.socket.getLocalPort();
		this.infoText = infoText;
		this.maxPlayer = maxPlayer;
		this.ref = ref;
		this.close = false;

		this.socket.setBroadcast(true);

		this.start();
	}

	@Override
	public void run()
	{
		ByteBuffer buf = ByteBuffer
				.allocate(NETCONSTANTS.BROADCAST_PACKET_LENGTH);
		buf.putInt(tcpBoundPort);// placeholder
		buf.putInt(maxPlayer);
		buf.putInt(tcpBoundPort);
		buf.putLong(serverId);
		buf.put(infoText.getBytes());

		while (!close)
		{

			buf.position(0);

			DatagramPacket packet = new DatagramPacket(new byte[0], 0);

			try
			{
				this.socket.receive(packet);
				buf.putInt(ref.getCurrentConnections());
				buf.position(0);
				DatagramPacket responceDatagramPacket = new DatagramPacket(
						buf.array(), buf.capacity(), packet.getAddress(),
						packet.getPort());

				this.socket.send(responceDatagramPacket);

			} catch (IOException e)
			{
				e.printStackTrace();
				this.close = true;

			}
			GDWServerLogger.logMSG("Broadcastresponce gesendet");
		}
		this.socket.close();
	}

	public int getBoundedPort()
	{
		return this.socket.getLocalPort();
	}
}
