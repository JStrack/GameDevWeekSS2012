package example;

import gdwNet.RESPONCECODES;
import gdwNet.client.BasicClient;
import gdwNet.client.IBasicClientListener;
import gdwNet.client.ServerInfo;
import gdwUtils.DefaultCharSet;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 * Wieder so eine must-have Klasse, hier habe ich die Schnittstelle {@link IBasicClientListener} implementiert
 * diese Schnittstelle sagt euch wie ihr momentan als Client verbunden seit, was so an Servern im Netzwerk 
 * rumläuft bietet einen Verbindungsstelle zum Server. 
 * 
 * Der Code ist haupstächlich für die Gui da, also nicht von Relevanz. Das was wichtig ist ist kommentiert.
 * 
 * @author firen
 *
 */
public class ChatClient extends JFrame implements IBasicClientListener
{
	private static final long serialVersionUID = 1L;
	private final JList<String> serverList;
	private final ServerListModel myModel;

	private final JTextArea chatArea;

	private final JTextField nameField;

	private final JTextField chatInputField;

	private final JLabel connectionStatus;

	private BasicClient clientSession;

	private final JButton connectb;

	private final JTextField ipaddressField;

	private final JTextField portField;

	private final JButton connectDirectButton;

	// puffer
	private String messageBuffer;

	public ChatClient()
	{
		this.messageBuffer = "";
		this.setTitle("ChatTestClient");
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.myModel = new ServerListModel();
		this.clientSession = null;

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 0));

		this.serverList = new JList<String>(this.myModel);
		topPanel.add(this.serverList);
		JPanel rightSubTop = new JPanel();
		rightSubTop.setLayout(new GridLayout(0, 1));

		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				sendRefresh();
			}
		});
		this.nameField = new JTextField();
		this.nameField.setEditable(true);
		rightSubTop.add(this.nameField);

		this.connectb = new JButton("Connect");
		connectb.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				connect();
			}
		});

		rightSubTop.add(refresh);
		rightSubTop.add(connectb);

		topPanel.add(rightSubTop);

		this.add(topPanel, BorderLayout.NORTH);

		// bottom
		JPanel botPanel = new JPanel();
		botPanel.setLayout(new GridLayout(0, 1));

		// direct connect
		this.ipaddressField = new JTextField();
		this.ipaddressField.setEditable(true);
		this.portField = new JTextField();
		this.portField.setEditable(true);
		this.connectDirectButton = new JButton();
		this.connectDirectButton.setText("Verbinde direkt");
		this.connectDirectButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				connectDirect();

			}
		});

		botPanel.add(new JLabel("Ipaddresse:"));
		botPanel.add(this.ipaddressField);
		botPanel.add(new JLabel("Port:"));
		botPanel.add(this.portField);
		botPanel.add(this.connectDirectButton);

		this.connectionStatus = new JLabel("Sehr langes bla");
		botPanel.add(this.connectionStatus);
		this.chatArea = new JTextArea();
		this.chatArea.setEditable(false);
		JScrollPane jsPane = new JScrollPane(this.chatArea);
		botPanel.add(jsPane);

		this.chatInputField = new JTextField();
		this.chatInputField.setEditable(true);
		this.chatInputField.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				chatInput();
			}
		});

		this.add(botPanel);
		this.add(this.chatInputField, BorderLayout.SOUTH);

		this.setSize(500, 500);
		this.setVisible(true);

		//Man muss ich als Listener beim Client registieren
		BasicClient.setListener(this);
	}

	/**
	 * Eine Beispielimplementierung, falls das finden der Server im Netzwerk nicht klappt.
	 * Ich lese einen IP-Addresse ein und einen Port verbinde mich.
	 */
	public void connectDirect()
	{
		InetAddress address;
		try
		{
			address = InetAddress.getByName(this.ipaddressField.getText());

			int port = Integer.parseInt(this.portField.getText());
			String name = this.nameField.getText();
			if (name.equals(""))
			{
				JOptionPane.showMessageDialog(this, "Der Loginname ist leer!");
				return;
			}
			ByteBuffer buf = ByteBuffer.allocate(50);
			try
			{
				byte [] arr = name.getBytes(DefaultCharSet.getDefaultCharset());
				buf.put((byte)arr.length);
				buf.put(arr);
			}catch (Exception e)
			{
				throw e;
			}
			BasicClient.connectToServer(address, port,buf);
		} catch (UnknownHostException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
			return;
		}

	}

	/**
	 * Ich möchte die Liste der sich im Netzwerk befindlichen Server haben.
	 * Falls man nicht schon beim Clientregistiert hat sollte man das hier spätestens tun.
	 */
	public void sendRefresh()
	{
		//gui sauber machen
		this.myModel.clear();
		//habe ich mich vergessen zu registrieren?
		if (BasicClient.getListener() == null)
		{
			BasicClient.setListener(this);
		}
		//rufe die refreshMethode auf
		BasicClient.refreshServerList();
	}

	public static void main(String[] args)
	{
		new ChatClient();
	}

	/**
	 * Eine Methode die von der Schnittstelle vorgeben wird.
	 * Sie wird aufgefufen, wenn der Client einen Server im Netzwerk findet.
	 */
	@Override
	public void serverResponce(ServerInfo info)
	{
		this.myModel.addEntry(info);
		this.invalidate();
	}

	/**
	 * Eine Methode die von der Schnittstelle vorgeben wird.
	 * Sie wird aufgerufen, wenn der Client euch über seinen Verbindungsstatus mitteilt.
	 */
	@Override
	public void connectionUpdate(int msg)
	{
		String text = null;
		switch (msg)
		{
		// Ok bin verbunden
		case RESPONCECODES.OK:
			text = "Verbunden";
			break;
		// er kommuniziert gerade mit dem Server über deinen Login
		case RESPONCECODES.HANDSHAKE:
			text = "Handshake läuft";
			break;
		// Verbindungsaufbau zum Server
		case RESPONCECODES.CONNECTING:
			text = "Verbinde...";
			break;

		default:
			// ab hier ist was schlecht gelaufen
			this.connectb.setEnabled(true);
			switch (msg)
			{
			// wenn das auf tritt habe ich Mist gebaut nicht ihr 
			// oder die Netzwerkverbindung ist schrott
			case RESPONCECODES.DATA_CORRUPTED:
				text = "Wir senden Scheiße";
				break;
			// Der Nick den ihr gesendet wird nicht akzeptiert
			case RESPONCECODES.NICK_CORRUPTED:
				text = "Nickname ist Mist";
				break;
			// Nick bereits vergeben
			case RESPONCECODES.NICK_TAKEN:
				text = "Dein Nick ist unkreativ";
				break;
			// Er mag einen nicht haben, erblockt alle Verbindungsversuche
			case RESPONCECODES.CONNECT_REFUSE:
				text = "Er mag uns nicht";
				break;
			// Er ist voll
			case RESPONCECODES.SERVER_FULL:
				text = "Schon alles voll";
				break;
			// Die Antwort dauert zu lange, vielleicht Netzwerk nicht in Ordnung?
			// Oder Kick?
			case RESPONCECODES.TIMEOUT:
				text = "Zu lange Leitung";
				break;
			// Kann nicht zum Server verbinden 
			case RESPONCECODES.UNREACHABLE:
				text = "Kaputte Leitung";
				break;
			// Und weg
			case RESPONCECODES.DISCONNECTED:
				text = "Disconnect";
				break;

			default:
				break;
			}
			break;
		}
		this.connectionStatus.setText(text);
		this.invalidate();
	}

	/**
	 * Ich verbinde zum Server per Listeneintrag
	 */
	public void connect()
	{
		if (this.serverList.isSelectionEmpty())
		{
			return;
		}
		
		//lese Nickname
		String name = this.nameField.getText();
		// check input
		if (name.equals(""))
		{
			JOptionPane.showMessageDialog(this, "Der Loginname ist leer!");
			return;
		}

		this.connectb.setEnabled(false);
		//hole gespeicherte Daten
		ServerInfo info = this.myModel.getServerInfoForIndex(this.serverList
				.getSelectedIndex());

		// und los gehts
		ByteBuffer buf = ByteBuffer.allocate(50);
		try
		{
			byte [] arr = name.getBytes(DefaultCharSet.getDefaultCharset());
			buf.put((byte)arr.length);
			buf.put(arr);
		}catch (Exception e)
		{
			throw e;
		}	
		
		BasicClient.connectToServer(info,buf);

	}
	
	public void chatInput()
	{
		this.messageBuffer = this.chatInputField.getText();
		this.chatInputField.setText("");
	}
	
	/**
	 * Schnittstellen Methode.
	 * 
	 * Die Verbindung wurde hergestellt. Hier ist deine Referenz zum Client, der mit dem Server verbunden
	 * ist.
	 */
	@Override
	public void connectionEstablished(BasicClient clientRef)
	{
		this.clientSession = clientRef;
		new ClientCoreLoop(this.clientSession, this);
	}

	/**
	 * Meine Beispielimplementierung vom Netzwerportokol.
	 * Haltet euch am besten daran und baut drauf auf.
	 */
	@Override
	public void incommingMessage(ByteBuffer msg, boolean wasSafe)
	{
		switch (msg.get())
		{
		// Es kamen Chatnachrichten rein
		case MyMessageConstants.ChatMessage:
			encodeChatMessageInput(msg);
			break;
		//Es haben sich Leute verbunden
		case MyMessageConstants.PlayerConnected:
			encodePlayerConnected(msg);
			break;
		//Es haben sich Leute abgemeldet
		case MyMessageConstants.PlayerDisconnected:
			encodePlacerDisconned(msg);
			break;

		//Es wurde was gesendet, was ich nicht verstehe
		default:
			msg.position(msg.position() - 1);
			unknownMessageCode(msg.get());
			break;
		}
	}

	public String getChatBuffer()
	{
		String result = this.messageBuffer;
		this.messageBuffer = "";
		return result;
	}

	private void unknownMessageCode(byte code)
	{
		this.chatArea.append("Unkown Messagecode: " + code);
	}

	private void encodeChatMessageInput(ByteBuffer msg)
	{

		byte amount = msg.get();

		for (int i = 0; i < amount; ++i)
		{
			byte size = msg.get();
			byte[] nameBytes = new byte[size];
			msg.get(nameBytes);

			String clearMsg = new String(nameBytes,
					DefaultCharSet.getDefaultCharset());
			this.chatArea.append(clearMsg + "\n");
			this.invalidate();

		}

	}

	private final static String CONNECTEDMSG = " lungert nun hier rum*\n";

	private void encodePlayerConnected(ByteBuffer msg)
	{
		byte amount = msg.get();
		for (int i = 0; i < amount; ++i)
		{
			byte size = msg.get();
			byte[] nameBytes = new byte[size];
			msg.get(nameBytes);
			String name = new String(nameBytes,
					DefaultCharSet.getDefaultCharset());
			this.chatArea.append("*" + name + CONNECTEDMSG);
			this.invalidate();
		}

	}

	private final static String DISCONNECTEDMSG = " ist abgehauen*\n";

	private void encodePlacerDisconned(ByteBuffer msg)
	{
		byte amount = msg.get();
		for (int i = 0; i < amount; ++i)
		{
			byte size = msg.get();
			byte[] nameBytes = new byte[size];
			msg.get(nameBytes);
			String name = new String(nameBytes,
					DefaultCharSet.getDefaultCharset());
			this.chatArea.append("*" + name + DISCONNECTEDMSG);

		}

	}
}
