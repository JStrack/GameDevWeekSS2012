package gdwNet.client;

import gdwNet.NETCONSTANTS;
import gdwNet.RESPONCECODES;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public class ConnectionResponceThread extends Thread
{

	private final SocketChannel tcpSocket;

	private final DatagramChannel udpSocket;

	private final ByteBuffer buf;

	private final ServerInfo info;

	private final String name;

	private boolean pending;


	public ConnectionResponceThread(SocketChannel tcpSocket,
			DatagramChannel udpSocket, ByteBuffer buf, ServerInfo info,
			String name)
	{
		this.tcpSocket = tcpSocket;
		try
		{
			this.tcpSocket.socket().setTcpNoDelay(true);
		} catch (SocketException e)
		{

		}
		this.udpSocket = udpSocket;
		this.buf = buf;
		this.info = info;
		this.name = name;
		this.pending = true;
		new ConnetionResponceThreadTimeoutHelper(this);
		this.start();
	}


	@Override
	public void run()
	{
		BasicClientListener lis = BasicClient.getListener();
		try
		{

			// tcp blocking
			tcpSocket.configureBlocking(true);
			tcpSocket.connect(new InetSocketAddress(info.address, info.port));
			lis.connectionUpdate(RESPONCECODES.HANDSHAKE);

			// send buf
			tcpSocket.write(this.buf);

			// wait for responce
			ByteBuffer responce = ByteBuffer
					.allocate(NETCONSTANTS.PACKAGELENGTH);

			try
			{
				tcpSocket.read(responce);
			} catch (IOException e)
			{
				lis.connectionUpdate(RESPONCECODES.TIMEOUT);
			}

			// get responce
			responce.position(0);
			byte resCode = responce.get();
			lis.connectionUpdate(resCode);
			if (resCode != RESPONCECODES.OK)
			{
				// clean up

				this.tcpSocket.close();
				this.udpSocket.close();
				return;
			}

			tcpSocket.configureBlocking(false);
			int udpPort = responce.getInt();
			int id = responce.getInt();

			// nat punch through and udp socket



			udpSocket.connect(new InetSocketAddress(tcpSocket.socket()
					.getInetAddress(), udpPort));

			ByteBuffer pingBuf = ByteBuffer.allocate(1);
			pingBuf.put(NETCONSTANTS.PING);
			pingBuf.flip();
			this.udpSocket.write(pingBuf);
			this.udpSocket.configureBlocking(false);
			// create client
			this.pending = false;
			BasicClient.registerClient(tcpSocket, udpSocket, id, name);
		} catch (IOException e)
		{
			lis.connectionUpdate(RESPONCECODES.UNREACHABLE);
			// clean up

			try
			{
				this.tcpSocket.close();
				this.udpSocket.close();
			} catch (IOException e1)
			{

			}
			
			return;
		}
	}

	public boolean isPending()
	{
		return pending;
	}

}
