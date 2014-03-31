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
	
	
	/*
	 * 
	 */
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
	public Peer getPeerByName(String peerName) {
		for(int i=0; i<peers.size(); i++)
			if (peers.get(i).getName().equals(peerName))
				return peers.get(i);
	
		return null;
	}
	
	/*
	 * 
	 */
	public void addPeer(String name, ArrayList<File> sharedFiles) {
		Peer peer = new Peer(name, sharedFiles);
		peers.add(peer);
		gui.addPeer(peer);
	}
	
	/*
	 * 
	 */
	public void deletePeer(String peerName) {
		Peer peer = getPeerByName(peerName);
		if(peer == null)
			return;
		
		peers.remove(peer);
		gui.deletePeer(peer);
	}
	
	/*
	 * 
	 */
	public void updatePeer(String peerName, ArrayList<File> sharedFiles) {
		Peer peer = getPeerByName(peerName);
		if(peer == null)
			return;
		
		peer.setSharedFiles(sharedFiles);
	}
	
	/*
	 * 
	 */
	public void addTransferRequest(Transfer transfer) {
		transfers.add(transfer);
	}
	
	/*
	 * 
	 */
	public void addTransferIncomingRequest(String requesterName, String fileName) {
		Peer requesterPeer = getPeerByName(requesterName);
		if(requesterPeer == null)
			return;
		
		Peer myPeer = getPeerByName(Main.MY_PEER_NAME);
		assert myPeer != null;
		
		File file = myPeer.getSharedFileByName(fileName);
		if(file == null)
			return;
		
		Transfer transfer = new Transfer(file, myPeer, requesterPeer);
		transfers.add(transfer);
		gui.addIncomingTransfer(transfer);
	}
	
	/*
	 * 
	 */
	public void updateTransferProgress(String senderName, String fileName, int chunckSize) {
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
}
