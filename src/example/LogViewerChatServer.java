package example;

import gdwNet.server.GDWServerLogger;
import gdwNet.server.GDWServerLoggerListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
/**
 * Einfach nur eine Klasse die einen LogListener implementiert und anzeigt.
 * 
 * Der so genannte GDWServerLoggerListener ist eine Schnittstelle der die der Server
 * ansprechen kann, wenn ihr etwas protokollieren wollt. 
 * Ich empfehle dies Schnittstelle irgendwo zu implementieren, da sonst das Protokoll in die 
 * Konsole geschrieben wird, da hilft es nicht viel.
 * @author firen
 *
 */
public class LogViewerChatServer extends JFrame implements GDWServerLoggerListener
{
	
	private static final long serialVersionUID = 1L;
	private JTextArea area;
	private Date date;
	private JButton closeButton;
	private ChatServer ref;

	public LogViewerChatServer()
	{
		super("ChatServerExample LogViewer");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		//logview
		this.area = new JTextArea();
		this.area.setEditable(false);
		this.closeButton = new JButton("Close");
		this.closeButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// shutdown server
				ref.shutMeDown();
				System.exit(0);

			}
		});
		GDWServerLogger.registerListener(this);
		
		this.date = new Date();
		
		this.add(this.area,BorderLayout.CENTER);
		this.add(this.closeButton, BorderLayout.SOUTH);
		
		//end stuff
		this.setSize(600, 400);
		this.setVisible(true);
	}

	/**
	 * Es wird die eingehende Nachrichte mit einem Zeitstemple versehen und in die 
	 * JTextArea geschrieben.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void logMessage(String msg)
	{
		this.area.append(this.date.getMinutes() + ":" + this.date.getSeconds()
				+ ":>" + msg + "\n");
	}

	public void setRef(ChatServer ref)
	{
		this.ref = ref;
	}

}
