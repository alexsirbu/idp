import java.util.ArrayList;

/*
 * @author cristian
 * 
 */
public class Mediator {
	/*
	 * 
	 */
	private GUI gui;
	/*
	 * 
	 */
	private ArrayList<Peer> peers;
	/*
	 * 
	 */
	private ArrayList<Transfer> transfers;
	
	
	public Mediator()
	{
		peers = new ArrayList<Peer>();
		transfers = new ArrayList<Transfer>();
	}
	/*
	 * 
	 */
	public void setGUI(GUI gui) {
		this.gui = gui;
	}
	
	/*
	 * 
	 */
	public ArrayList<Peer> getPeers() {
		return peers;
	}
	
	public ArrayList<Transfer> getTransfers() {
		return transfers;
	}
	
	public Peer getPeerByName(String peerName) {
		for(int i=0; i<peers.size(); i++)
			if (peers.get(i).getName().equals(peerName))
			{
				return peers.get(i);
			}
	
		assert(false);
		return null;
	}
	
	/*
	 * 
	 */
	public void updateFileSendProgress() {
		
	}
	
	/*
	 * 
	 */
	public void updateFileReceiveProgress() {
		
	}
	
	/*
	 * 
	 */
	public void addPeer(String name, ArrayList<File> sharedFiles) {
		peers.add(new Peer(name, sharedFiles));
	}
	
	/*
	 * 
	 */
	public void deletePeer(String name) {
		
	}
	
	/*
	 * 
	 */
	public void updatePeerSharedFiles(String peerName, ArrayList<File> sharedFiles) {
		
	}
}
