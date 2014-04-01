import java.util.ArrayList;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.swing.SwingWorker;

/*
 * 
 */
public class GUITest {
	/*
	 * 
	 */
	private static int MAX_INITIAL_PEERS = 10;
	/*
	 * 
	 */
	private static int MAX_PEER_FILES = 5;
	/*
	 * 
	 */
	private static int MAX_FILE_SIZE = 100;
	
	/*
	 * 
	 */
	private SecureRandom random;
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
	private SwingWorker<Integer, Integer> worker;
	/*
	 * 
	 */
	private Mediator mediator;
	
	
	/*
	 * 
	 */
	public GUITest(Mediator mediator) {
		this.mediator = mediator;
		this.random = new SecureRandom();
		this.peers = new ArrayList<Peer>();
		this.transfers = new ArrayList<Transfer>();
		
		this.worker = new GUITestWorker(this); 
	}
	
	/*
	 * 
	 */
	protected void setUp() {
		int noInitialPeers = Math.max(Math.abs(random.nextInt() % (MAX_INITIAL_PEERS + 1)), 1);
		
		for(int i = 0; i < noInitialPeers; i++) {
			String peerName = generatePeerName();
			mediator.addPeer(peerName, generatePeerSharedFiles());
			peers.add(mediator.getPeer(peerName));
		}
	}
	
	/*
	 * 
	 */
	protected void tearDown() {
		for(int i = 0; i < peers.size(); i++)
			mediator.deletePeer(peers.get(i).getName());
		
		peers.clear();
	}
	
	/*
	 * 
	 */
	public void run() {	
		worker.execute();
	}
	
	/*
	 * 
	 */
	protected void addPeer() {
		String peerName = generatePeerName();
		mediator.addPeer(peerName, generatePeerSharedFiles());
		peers.add(mediator.getPeer(peerName));
	}
	
	/*
	 * 
	 */
	protected void deletePeer() {
		int peerNo = Math.abs(random.nextInt() % peers.size());
		if(!peerIsActive(peers.get(peerNo)))
			mediator.deletePeer(peers.get(peerNo).getName());
	}
	
	/*
	 * 
	 */
	protected void updatePeer() {
		int peerNo = Math.abs(random.nextInt() % peers.size());
		peerNo = 0;
		if(!peerIsActive(peers.get(peerNo)))
			mediator.updatePeerSharedFiles(peers.get(peerNo).getName(), generatePeerSharedFiles());
	}
	
	/*
	 * 
	 */
	protected void initiateTransfer() {
		Peer localPeer = mediator.getLocalPeer();
		ArrayList<File> localPeerSharedFiles = localPeer.getSharedFiles();
		
		if(localPeerSharedFiles.size() == 0)
			return;
		
		int localPeerSharedFileNo = Math.abs(random.nextInt() % localPeerSharedFiles.size()); 
		String requestedFileName = localPeerSharedFiles.get(localPeerSharedFileNo).getName();
		
		if(peers.size() == 0)
			return;
		
		int requestingPeerNo = Math.abs(random.nextInt() % peers.size());
		String requestingPeerName = peers.get(requestingPeerNo).getName();
		
		for(int i = 0; i < transfers.size(); i++)
		{
			if(transfers.get(i).getReceivingPeer().getName().equals(requestingPeerName)
					&& transfers.get(i).getSendingPeer().getName().equals(localPeer.getName())
					&& transfers.get(i).getFile().getName().equals(requestedFileName))
				return;
		}
		
		mediator.addTransferIncomingRequest(requestingPeerName, requestedFileName);
		transfers.add(new Transfer(localPeerSharedFiles.get(localPeerSharedFileNo), localPeer, peers.get(requestingPeerNo)));
	}
	
	/*
	 * 
	 */
	protected void continueTransfer() {
		if(transfers.size() == 0)
			return;
		
		int transferNo = Math.abs(random.nextInt() % transfers.size()); 
		Transfer transfer = transfers.get(transferNo);
		int transferChunkSize = transfer.getFile().getSize() / 3;
		transfer.updateProgress(transferChunkSize);
		
		mediator.updateTransferProgress(transfer.getSendingPeer().getName(), transfer.getReceivingPeer().getName(), transfer.getFile().getName(), transferChunkSize);
		
		if(transfer.getProgress() == 100)
			transfers.remove(transferNo);
	}
	
	/*
	 * 
	 */
	private boolean peerIsActive(Peer peer) {
		for(int i = 0; i < transfers.size(); i++) {
			if(transfers.get(i).getReceivingPeer() == peer
					|| transfers.get(i).getSendingPeer() == peer)
				return true;
		}
		
		return false;
	}
	
	/*
	 * 
	 */
	private String generatePeerName() {
		String peerName = null;
		
		while(true) {
			peerName = new BigInteger(20, random).toString();
			
			boolean duplicate = false;
			for(int i = 0; i < peers.size(); i++)
			{
				if(peers.get(i).getName().equals(peerName))
				{
					duplicate = true;
					break;
				}
			}
					
			if(!duplicate)
				break;
		}
		
		peerName = "peer" + peerName;
		
		return peerName;
	}
	
	/*
	 * 
	 */
	private ArrayList<File> generatePeerSharedFiles() {
		ArrayList<File> peerSharedFiles = new ArrayList<File>();
		int noPeerSharedFiles = Math.max(Math.abs(random.nextInt() % (MAX_PEER_FILES + 1)), 1);
		
		for(int i = 0; i < noPeerSharedFiles; i++) {
			String peerSharedFileName = null;
			
			
			while(true) {
				peerSharedFileName = new BigInteger(20, random).toString();
				
				boolean duplicate = false;
				for(int j = 0; j < i; j++)
				{
					if(peerSharedFiles.get(j).getName().equals(peerSharedFileName))
					{
						duplicate = true;
						break;
					}
				}
				
				if(!duplicate)
					break;
			}
			
			peerSharedFileName = "file" + peerSharedFileName;
			
			peerSharedFiles.add(new File(peerSharedFileName, Math.abs(random.nextInt() % (MAX_FILE_SIZE + 1))));
		}
		
		return peerSharedFiles;
	}
}
