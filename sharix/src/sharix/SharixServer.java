package sharix;


import java.util.ArrayList;
import java.util.HashMap;


public class SharixServer
{
	private HashMap<String, String> peerIPs = new HashMap<String, String>();
	private HashMap<String, Integer> peerPorts = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<String>> peerFiles = new HashMap<String, ArrayList<String>>();
	
	
	public void registerPeer(String name, String ip, int port)
	{
			this.peerIPs.put(name, ip);
			this.peerPorts.put(name, port);
			this.peerFiles.put(name, new ArrayList<String>());
	}
	
	public void unregisterPeer(String name)
	{	
		this.peerIPs.remove(name);
		this.peerPorts.remove(name);
		this.peerFiles.remove(name);
	}
	
	public String[] getPeers()
	{
		return (String[])this.peerIPs.keySet().toArray();
	}
	
	public void updatePeerFileList(String name, String[] files)
	{	
		if(!this.peerFiles.containsKey(name))
			return;
		
		this.peerFiles.get(name).clear();
		
		for(int i = 0; i < files.length; i++)
			this.peerFiles.get(name).add(files[i]);
	}
	
	public String[] getPeerFiles(String name)
	{
		if(!this.peerFiles.containsKey(name))
			return null;
		
		return (String[])this.peerFiles.get(name).toArray();
	}
}

