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
	public void addPeer(String name) {
		peers.add(new Peer(name));
	}
	
	/*
	 * 
	 */
	public void deletePeer(String name) {
		
	}
	
	/*
	 * 
	 */
	public void updatePeerSharedFiles(String peerName, ArrayList<File> files) {
		
	}
}
