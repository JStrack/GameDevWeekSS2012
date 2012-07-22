package gdwNet.server;

import gdwNet.NETCONSTANTS;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class BroadcastresponseThread extends Thread
{
	private final DatagramSocket socket;
	private final BasicServer ref;
	private boolean close;
	public final int boundedPort;

	public BroadcastresponseThread( BasicServer ref) throws IOException
	{
		this.socket = new DatagramSocket(NETCONSTANTS.BROADCAST_PORT);
		this.boundedPort = this.socket.getLocalPort();
		this.ref = ref;
		this.close = false;

		this.socket.setBroadcast(true);

		this.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
		while (!close)
		{


			DatagramPacket packet = new DatagramPacket(new byte[0], 0);

			try
			{
				this.socket.receive(packet);
				
				byte [] buf = this.ref.getBroadcastResponce().array();
				DatagramPacket responceDatagramPacket = new DatagramPacket(
						buf, buf.length, packet.getAddress(),
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
