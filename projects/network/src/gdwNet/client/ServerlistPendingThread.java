package gdwNet.client;

import gdwNet.NETCONSTANTS;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ServerlistPendingThread extends Thread
{
	private static final int TIMEOUT = 2000;

	private long startTime;

	private final DatagramSocket socket;

	private final IBasicClientListener lis;

	public ServerlistPendingThread(IBasicClientListener lis)
			throws SocketException
	{
		this.lis = lis;
		this.socket = new DatagramSocket();
		this.socket.setSoTimeout(TIMEOUT);
		this.socket.setBroadcast(true);

		this.start();
	}

	private LinkedList<InetAddress> getBroadcastWindows() throws IOException
	{
		Process process = Runtime.getRuntime().exec("ipconfig");
		Scanner scanner = new Scanner(process.getInputStream());

		LinkedList<InetAddress> result = new LinkedList<InetAddress>();
		String line;
		
		//get Programmoutput
		while (scanner.hasNextLine())
		{
			if ((line = scanner.nextLine()).contains("IPv4"))
			{
				String partResult = new String();
				//found input
				String ipAddress = line.split(":")[1];
				line = scanner.nextLine();
				String subnetMask = line.split(":")[1];
				ipAddress = ipAddress.trim();
				subnetMask = subnetMask.trim();
				//split into parts	
				
				String[] ipAddressParts = ipAddress.split("\\.");
				String[] subnetMaskParts = subnetMask.split("\\.");
				
				//parse...
				for(int i=0;i<4;++i)
				{
					if(subnetMaskParts[i].equals("255"))
					{//copy complete
						partResult += ipAddressParts[i]+".";
					}else if(subnetMaskParts[i].equals("0"))
					{//empty part
						partResult += "255.";
					}else
					{//calc...
						short ipValue = Short.parseShort(ipAddressParts[i]);
						short subValue = Short.parseShort(subnetMaskParts[i]);
						subValue *= -1;
						
						ipValue &= subValue;
						
						partResult += Short.toString(ipValue)+".";
					}
				}
				//debug
				String sringResult = partResult.substring(0, partResult.length()-1);
				result.add(InetAddress.getByName(sringResult));
			}
		}
		scanner.close();
		return result;
	}

	@Override
	public void run()
	{
		try
		{
			LinkedList<InetAddress> broadcastIps;
			if(System.getProperty("os.name").startsWith("Windows"))
			{//we have a windows here....
				broadcastIps = getBroadcastWindows();
			}else
			{
				//do normal stuff
				broadcastIps =  new LinkedList<InetAddress>();
					Enumeration<NetworkInterface> infs = NetworkInterface
							.getNetworkInterfaces();
					while (infs.hasMoreElements())
					{
						NetworkInterface networkInterface = infs.nextElement();
						List<InterfaceAddress> infsadd = networkInterface
								.getInterfaceAddresses();
						for (InterfaceAddress add : infsadd)
						{
							if (add.getBroadcast() != null)
								broadcastIps.add(add.getBroadcast());
						}

					}
			}
			
			

			// send multicast
			InetAddress group = InetAddress
					.getByName(NETCONSTANTS.MULITCASTGROUP);
			DatagramPacket packet = new DatagramPacket(new byte[0], 0, group,
					NETCONSTANTS.BROADCAST_PORT);
			this.socket.send(packet);
			
			//workaround for loopbackdevice
			broadcastIps.add(InetAddress.getByName("127.0.0.1"));

			// send broadcasts s s s s
			while (!broadcastIps.isEmpty())
			{
				packet.setAddress(broadcastIps.pop());
				packet.setPort(NETCONSTANTS.BROADCAST_PORT);
				this.socket.send(packet);
			}

			this.startTime = System.currentTimeMillis();
			while (!this.isInterrupted())
			{
				// waiting for reply
				DatagramPacket reply = new DatagramPacket(
						new byte[NETCONSTANTS.BROADCAST_PACKET_LENGTH],
						NETCONSTANTS.BROADCAST_PACKET_LENGTH);
				try
				{
					this.socket.receive(reply);
					ByteBuffer buf = ByteBuffer.wrap(reply.getData());
					int currentPlayer = buf.getInt();
					int maxPlayer = buf.getInt();
					int port = buf.getInt();
					long id = buf.getLong();
					byte[] arr = new byte[buf.get()];
					buf.get(arr);
					String infoMsg = new String(arr);
					int ping = (int) (System.currentTimeMillis() - startTime);

					ServerInfo info = new ServerInfo(infoMsg, currentPlayer,
							maxPlayer, ping, port, reply.getAddress(), id);
					lis.serverResponce(info);

				} catch (SocketTimeoutException e)
				{

					break;
				}

			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
