package sharix;


import java.util.ArrayList;
import java.util.HashMap;


public class SharixServer
{
	private static HashMap<String, String> peerIPs = new HashMap<String, String>();
	private static HashMap<String, Integer> peerPorts = new HashMap<String, Integer>();
	private static HashMap<String, ArrayList<String>> peerFiles = new HashMap<String, ArrayList<String>>();
	
	
	public void registerPeer(String name, String ip, int port)
	{
			SharixServer.peerIPs.put(name, ip);
			SharixServer.peerPorts.put(name, port);
			SharixServer.peerFiles.put(name, new ArrayList<String>());
	}
	
	public void unregisterPeer(String name)
	{	
		SharixServer.peerIPs.remove(name);
		SharixServer.peerPorts.remove(name);
		SharixServer.peerFiles.remove(name);
	}
	
	public String[] getPeers()
	{
		if(SharixServer.peerIPs.size() == 0)
			return null;
		
		return SharixServer.peerIPs.keySet().toArray(new String[0]);
	}
	
	public void updatePeerFileList(String name, String[] files)
	{	
		if(!SharixServer.peerFiles.containsKey(name))
			return;
		
		SharixServer.peerFiles.get(name).clear();
		
		if(files == null)
			files = new String[0];
		
		for(int i = 0; i < files.length; i++)
			SharixServer.peerFiles.get(name).add(files[i]);
	}
	
	public String[] getPeerFiles(String name)
	{
		if(!SharixServer.peerFiles.containsKey(name))
			return null;
		
		return (String[])SharixServer.peerFiles.get(name).toArray();
	}
}

