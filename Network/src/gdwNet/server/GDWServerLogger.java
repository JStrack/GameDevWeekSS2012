package gdwNet.server;

public class GDWServerLogger
{
	private static GDWServerLoggerListener lis;

	public static void registerListener(GDWServerLoggerListener lis)
	{
		GDWServerLogger.lis = lis;
	}

	public static void logMSG(String msg)
	{
		if (GDWServerLogger.lis == null)
		{
			System.out.println(msg);
		} else
		{
			lis.logMessage(msg);
		}
	}
}
