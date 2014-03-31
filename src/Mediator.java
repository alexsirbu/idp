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
	public void updateTransfer(String senderName, String fileName, int chunckSize) {
		Transfer transfer = null;
		
		for(int i = 0; i < transfers.size(); i++) {
			if(transfers.get(i).getFile().getName().equals(fileName)
					&& transfers.get(i).getSender().getName().equals(senderName))
				transfer = transfers.get(i);
		}
		
		if(transfer == null)
			return;
		
		transfer.updateProgress(chunckSize);
		gui.updateTransferProgress(transfer);
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
	public void updatePeer(String peerName, ArrayList<File> sharedFiles) {
		Peer peer = null;
		
		for(int i = 0; i < peers.size(); i++) {
			if(peers.get(i).getName().equals(peerName))
				peer = peers.get(i);
		}
		
		if(peer == null)
			return;
		
		peer.setSharedFiles(sharedFiles);
	}
}
