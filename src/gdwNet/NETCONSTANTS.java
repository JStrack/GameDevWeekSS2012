package gdwNet;

public final class NETCONSTANTS
{
    public static final byte CONNECT = 1;

    public static final byte DISCONNECT = 2;

    public static final byte PING = 3;

	public static final byte PONG = 4;

    public static final byte MESSAGE = 5;
    
    public static final byte RECONNECT = 6;
    

    //constants
    public static final int PACKAGELENGTH = 256;

	public static final int BROADCAST_PORT = 8888;
	
	public static final String MULITCASTGROUP = "225.1.33.7";
	
	public static final String DEFAULT_INFOTEXT = "GDW-Default Greeter";


    /*
     * int port;
     * byte current player;
     * byte max player;
     *  string welcomemessage
     */
    public static final int BROADCAST_PACKET_LENGTH = 256;

	public static final byte MAGIC_LOGIN_CODE = 42;

	public static final long TIMEOUT_RECEIVE = 1000L;

	public static final long PONG_TIMEOUT = 500L;

	public static final long HEARTBEAT_REQUESTIME = 1000L;
	
	public static final long DISPOSE_LEAVERDATA_TTL = 10000L;


}
