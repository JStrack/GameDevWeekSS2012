package gdwNet;



public final class RESPONCECODES
{

	public static final byte DISCONNECTED = -1;

    public static final byte OK = 0;

    public static final byte SERVER_FULL = 1;

    public static final byte DATA_CORRUPTED = 2;

    public static final byte CONNECT_REFUSE = 3;

	public static final byte NICK_TAKEN = 4;

    public static final byte NICK_CORRUPTED = 5;

	public static final byte TIMEOUT = 6;

	// status updates

	public static final byte CONNECTING = 7;

	public static final byte HANDSHAKE = 8;

	public static final byte UNREACHABLE = 9;

	public static final byte CONNECTED = 10;
	
	public static final byte RECONNECTING = 11;



    
}
