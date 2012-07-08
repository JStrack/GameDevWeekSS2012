package example;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Eine kleine Klasse, die bei der Netzwerkentwicklung enstanden ist. Sie dient zur Ausgabe aller für 
 * Java sichtbaren Netzwerkschnittstellen, sowie deren für Netzwerk intressanten informationen.
 * @author firen
 *
 */
public class Netzwerktestings
{
	public static void main(String[] args)
	{
		try
		{
			Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();
			while(enu.hasMoreElements())
			{
				NetworkInterface inf = enu.nextElement();
				List<InterfaceAddress> adds = inf.getInterfaceAddresses();
				if(adds == null)
					continue;
				System.out.println("\n\nInterface:"+inf.getDisplayName());
				for(InterfaceAddress add : adds)
				{
					if (add == null)
					{
						System.out.println("InterfaceAddress is null");
						continue;
					}
					System.out.println("InterfaceAddres:"+add.getAddress());
					System.out.println("Prefixlaenge: "+add.getNetworkPrefixLength());
					System.out.println("InterfaceBrodacast:"+add.getBroadcast()+"\n");
					
				}
			}
			
		} catch (SocketException e)
		{
			e.printStackTrace();
		}
	}
}
