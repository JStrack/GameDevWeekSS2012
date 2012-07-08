package gdwNet.client;

import java.nio.ByteBuffer;

public interface BasicClientListener
{
	public void serverResponce(ServerInfo info);

	public void connectionUpdate(int msg);

	public void connectionEstablished(BasicClient clientRef);

	public void incommingMessage(ByteBuffer msg, boolean wasSafe);

}
