package gdwNet.client;


public class ConnetionResponceThreadTimeoutHelper extends Thread
{
	private ConnectionResponceThread thread;
	private static final long timeout = 5000;

	public ConnetionResponceThreadTimeoutHelper(ConnectionResponceThread thread)
	{
		this.thread = thread;
		this.setDaemon(true);
		this.start();
	}

	@Override
	public void run()
	{
		try
		{
			sleep(timeout);
		} catch (InterruptedException e)
		{

		}
		if(this.thread.isPending())
		{
			this.thread.interrupt();
			this.thread = null;
		}
	}

}
