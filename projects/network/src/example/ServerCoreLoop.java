package example;

import gdwUtils.DefaultCharSet;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * Speziell:
 * Beispielimplementierung für eine CoreLoop auf der Serverseite. Es werden die Nachrichten von den Verbunden Clients abgefragt
 * und Nachrichten verteilt.
 * 
 * Allgemein:
 * Eine so genannten CoreLoop ist eine Endlosschleife, die praktische in jedem Spiel anwendung findet.
 * Sie bildet die Grundeinheit und besteht aus normalerweise aus:
 * - update(hole Spielereingaben, zb; von Tastatur. Simuliere weiter das Spielgeschehen)
 * - draw(Zeichne das aktuelle Spielgeschehen)
 * 
 * @author firen
 *
 */
public class ServerCoreLoop extends Thread
{
	/**
	 * Die Nicknames, der Clients, die seit dem letzten Durchlauf die Verbindung geschlossen haben.
	 */
	private final LinkedList<String> leaverNames;
	/**
	 * Die Nicknames, der Clients, die seit dem letzten Durchlauf die Verbinung aufgebaut haben.
	 */
	private final LinkedList<String> joinerNames;
	/**
	 * Alle gesammelten Chatnachrichten der Clients seit dem letzten Durchlauf.
	 */
	private final LinkedList<String> messageQeueu;

	private final ChatServer ref;

	public ServerCoreLoop(ChatServer ref)
	{
		this.ref = ref;
		this.leaverNames = new LinkedList<String>();
		this.joinerNames = new LinkedList<String>();
		this.messageQeueu = new LinkedList<String>();
	}

	@Override
	public void run()
	{
		while (!this.isInterrupted())
		{
			try
			{
				//sonst läuft sonst zu schnell, da nicht gezeichnet wird...
				sleep(250);
			} catch (InterruptedException e)
			{
			}

			// ruf die updateMethode vom Server auf, es werden keinen eingehenden Verbindungen geblockt
			this.ref.proccedInputData();
			// überpüfen ob jemand dazu kam
			if (!this.joinerNames.isEmpty())
			{
				// lass dir einen ByteBuffer für Nachrichten geben
				ByteBuffer buf = this.ref.getMessageBuffer();
				
				//fürge den code für Spielerbeitritt dazu
				buf.put(MyMessageConstants.PlayerConnected);
				
				//rufe pack methode auf
				packListInBytBuffer(this.joinerNames, buf);
				
				// sende an alle per UDP (ohne Ausfallsicherheit)
				this.ref.sendToAll(buf, true);
			}
			if (!this.leaverNames.isEmpty())
			{
				//selbe wie eingehende nur mit verlasende
				ByteBuffer buf = this.ref.getMessageBuffer();
				buf.put(MyMessageConstants.PlayerDisconnected);
				packListInBytBuffer(this.leaverNames, buf);
				this.ref.sendToAll(buf, true);
			}
			if (!this.messageQeueu.isEmpty())
			{
				//dito
				ByteBuffer buf = this.ref.getMessageBuffer();
				buf.put(MyMessageConstants.ChatMessage);
				packListInBytBuffer(this.messageQeueu, buf);
				this.ref.sendToAll(buf, true);
			}
		}
	}
	/**
	 * Eine Verpacktmethode für die Liste die diese Klasse verwendet.
	 * @param list die Liste die durchgearbeitet werden soll
	 * @param buf der ByteBuffer welcher befüllt wird
	 */

	private void packListInBytBuffer(LinkedList<String> list, ByteBuffer buf)
	{
		// füge Anzahl der Elemente der Liste ein
		buf.put((byte) list.size());

		//so lange Liste nicht leer 
		while (!list.isEmpty())
		{
			//nehme erstes Element heraus
			String elem = list.pop();
			
			// lese das byte-array, welches den string repräsentiert
			byte[] arr = elem.getBytes(DefaultCharSet.getDefaultCharset());
			
			//schreibe länge des byte-arrays
			buf.put((byte) arr.length);
			
			//schreibe das array hinein
			buf.put(arr);
			
		}
		

	}

	/**
	 * fügt einen Namen zur "Leaverlist" hinzu
	 * @param name der Name
	 */
	public void addLeaverName(String name)
	{
		this.leaverNames.add(name);
	}
	
	/**
	 * fügt einen Namen zur "Joinerliste" hinzu
	 * @param name der Name
	 */
	public void addJoinerName(String name)
	{
		this.joinerNames.add(name);
	}

	/**
	 * fügt eine Nachricht an zur Nachrichtenliste hinzu
	 * @param msg die Nachricht
	 */
	public void addMessage(String msg)
	{
		this.messageQeueu.add(msg);
	}
}
