package gdwNet;

import java.net.DatagramPacket;

public class ResponseMessage
{
	private DatagramPacket packet;

	private long timeStamp;

	private short id;

	public ResponseMessage(DatagramPacket packet, long timeStamp, short id)
	{
		this.packet = packet;
		this.timeStamp = timeStamp;
		this.id = id;
	}

	public DatagramPacket getPacket()
	{
		return packet;
	}

	public long getTimeStamp()
	{
		return timeStamp;
	}

	public short getId()
	{
		return id;
	}
	

}
