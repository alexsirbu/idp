import java.util.ArrayList;

/*
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
	private Peer localPeer;
	
	
	/*
	 * 
	 */
	public Mediator(String localPeerName) {
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
	public Peer getLocalPeer() {
		return localPeer;
	}
	
	/*
	 * 
	 */
	public Peer getPeer(String peerName) {
		for(int i = 0; i < peers.size(); i++)
			if (peers.get(i).getName().equals(peerName))
				return peers.get(i);
	
		return null;
	}
	
	/*
	 * 
	 */
	public void addPeer(String peerName, ArrayList<File> sharedFiles) {
		Peer peer = new Peer(peerName, sharedFiles);
		
		peers.add(peer);
		gui.addPeer(peer);
	}
	
	/*
	 * 
	 */
	public void addLocalPeer(String localPeerName, ArrayList<File> sharedFiles) {
		assert localPeer == null;
		
		addPeer(localPeerName, sharedFiles);
		localPeer = getPeer(localPeerName);
	}
	
	/*
	 * 
	 */
	public void deletePeer(String peerName) {
		Peer peer = getPeer(peerName);
		if(peer == null)
			return;
		
		peers.remove(peer);
		gui.deletePeer(peer);
	}
	
	/*
	 * 
	 */
	public void updatePeerSharedFiles(String peerName, ArrayList<File> sharedFiles) {
		Peer peer = getPeer(peerName);
		if(peer == null)
			return;
		
		peer.setSharedFiles(sharedFiles);
	}
	
	/*
	 * 
	 */
	public void addTransferOutgoingRequest(Transfer transfer) {
		transfers.add(transfer);
	}
	
	/*
	 * 
	 */
	public void addTransferIncomingRequest(String requestingPeerName, String fileName) {
		Peer requestingPeer = getPeer(requestingPeerName);
		if(requestingPeer == null)
			return;
		
		File requestedFile = localPeer.getSharedFile(fileName);
		if(requestedFile == null)
			return;
		
		Transfer transfer = new Transfer(requestedFile, localPeer, requestingPeer);
		transfers.add(transfer);
		gui.addTransferIncomingRequest(transfer);
	}
	
	/*
	 * 
	 */
	public void updateTransferProgress(String sendingPeerName, String fileName, int chunkSize) {
		Transfer transfer = null;
		
		for(int i = 0; i < transfers.size(); i++) {
			if(transfers.get(i).getFile().getName().equals(fileName)
					&& transfers.get(i).getSendingPeer().getName().equals(sendingPeerName))
				transfer = transfers.get(i);
		}
		
		if(transfer == null)
			return;
		
		transfer.updateProgress(chunkSize);
		gui.updateTransferProgress(transfer);
	}
}
