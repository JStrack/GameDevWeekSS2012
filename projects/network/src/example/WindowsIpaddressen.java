package example;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Da die normale Java Methode um die Broadcastaddresse herauszufinden ( InterfaceAddress.getBroadcast()) 
 * für Windows nicht greift (ab XP), muss man "ipconfig" durchparsen.
 * 
 * Die Methode ist die direkte Implementierung die im GDW-Client verwendet wird.(falls das Hoastsystem Windwos ist)
 * @author firen
 *
 */
public class WindowsIpaddressen
{
	public static void main(String[] args)
	{
		try
		{
			LinkedList<InetAddress> list = new WindowsIpaddressen().getSubnet();
			for (InetAddress add : list)
			{
				System.out.println(add);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private LinkedList<InetAddress> getSubnet() throws IOException
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
		return result;
	}
}
