package gdwNet.server;

import java.nio.ByteBuffer;


public class JoinRequestWrapper
{
	public final ConnectionInfo info;
	public final long timestamp;
	public final ByteBuffer data;

	public JoinRequestWrapper(ConnectionInfo info, ByteBuffer data)
	{
		this.info = info;
		this.timestamp = System.currentTimeMillis();
		this.data = data;
	}

}
