package example;

import gdwNet.client.ServerInfo;

import java.util.ArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Da der Client eine JList verwendet, braucht es eine ListModel und hier ist die Implementierung.
 * Für Beispielcode nicht weiter von Belangen, da diese Klasse nur der Darstellung der GuiElementen dient.
 * @author firen
 *
 */
public class ServerListModel implements ListModel<String>
{

	private final ArrayList<ServerInfo> data;
	private final ArrayList<ListDataListener> listeners;

	public ServerListModel()
	{
		this.data = new ArrayList<ServerInfo>();
		this.listeners = new ArrayList<ListDataListener>();
	}

	@Override
	public void addListDataListener(ListDataListener l)
	{
		this.listeners.add(l);
	}

	@Override
	public String getElementAt(int index)
	{
		ServerInfo point = this.data.get(index);
		return point.infoMsg + " (" + point.currentPlayer + "/"
				+ point.maxPlayer + ")";
	}

	@Override
	public int getSize()
	{
		return this.data.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{
		this.listeners.remove(l);
	}

	public void addEntry(ServerInfo info)
	{
		synchronized (data)
		{
			for (ServerInfo myInfo : data)
			{
				if (myInfo.id == info.id)
				{
					return;
				}
			}
			this.data.add(info);
			updateLis(null);
		}

	}
	
	public void clear()
	{
		this.data.clear();
		updateLis(null);
	}
	
	private void updateLis(ListDataEvent e)
	{
		for(ListDataListener lis : this.listeners)
		{
			lis.contentsChanged(e);
		}
	}

	public ServerInfo getServerInfoForIndex(int index)
	{
		return this.data.get(index);
	}
}
