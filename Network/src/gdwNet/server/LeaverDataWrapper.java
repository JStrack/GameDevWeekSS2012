package gdwNet.server;


public class LeaverDataWrapper
{
	public final BasicClientConnection client;
	public final long timestamp;

	public LeaverDataWrapper(BasicClientConnection client)
	{
		this.client = client;
		this.timestamp = System.currentTimeMillis();

	}
	
	public boolean compareWithReco(ReconnectRequestWrapper reco)
	{
		ConnectionInfo recoInfo = reco.info;
		return ((this.client.id==recoInfo.id)&&(this.client.sharedSecret==recoInfo.sharedSecret));
	}

}
