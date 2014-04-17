import java.io.IOException;
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
	private String localPeerRealName;
	
	/*
	 * 
	 */
	private String localPeerIP;
	
	/*
	 * 
	 */
	private int localPeerPort;
	
	/*
	 * Temporary, for stage 2 only;
	 */
	private String usersFolder;
	
	/*
	 * 
	 */
	public Mediator(String localPeerName) {
		peers = new ArrayList<Peer>();
		transfers = new ArrayList<Transfer>();
		localPeerRealName = localPeerName;
	}
	
	/*
	 * 
	 */
	public void registerLocalPeer() throws IOException{
		
		FileOperations fo = new FileOperationsJavaNIO(localPeerRealName+".txt");
		
		String configFile = new String(fo.readAll());
		
		String[] configs = configFile.split("\n");
		
		for (int i=0; i<configs.length; i++)
		{
			String[] configMembers = configs[i].split(": ");
			if (configMembers[0].equals("user-ip"))
				this.localPeerIP=configMembers[1];
			else if (configMembers[0].equals("user-port"))
				this.localPeerPort=Integer.parseInt(configMembers[1]);
			else if (configMembers[0].equals("users-folder"))
				this.usersFolder=configMembers[1];
		}
		
		ArrayList<File> files = new ArrayList<File>();
		java.io.File folder = new java.io.File(localPeerRealName);
		for(java.io.File file : folder.listFiles())
			files.add(new File(file.getName(), (int)file.length()));
			
		addLocalPeer(Main.LOCAL_PEER_NAME, files);
		
		// TODO Stage 3: here or in addLocalPeer, send info to server with my files
		getPeers();
		
	}
	
	/*
	 * this will change in stage 3
	 */
	public void getPeers() throws IOException
	{
		String IP="";
		int port=0;
		String name="";
		ArrayList<File> files;
		
		java.io.File folder = new java.io.File(usersFolder);
		for(java.io.File file : folder.listFiles())
			if (!file.getName().equals(localPeerRealName+".txt"))
			{
				files = new ArrayList<File>();
				name = file.getName().replaceAll(".txt","");
				
				FileOperations fo = new FileOperationsJavaNIO(usersFolder+"/"+file.getName());
				
				String configFile = new String(fo.readAll());
				
				String[] configs = configFile.split("\n");
				
				for (int i=0; i<configs.length; i++)
				{
					String[] configMembers = configs[i].split(": ");
					if (configMembers[0].equals("user-ip"))
						IP=configMembers[1];
					else if (configMembers[0].equals("user-port"))
						port=Integer.parseInt(configMembers[1]);
					else if (configMembers[0].equals("user-file"))
						files.add(new File(configMembers[1], (int)new java.io.File(name+"/"+configMembers[1]).length()));

				}
				
				addPeer(name, IP, port, files);
			}
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
	public String getLocalPeerIP(){
		return localPeerIP;
	}
	
	/*
	 * 
	 */
	public int getLocalPeerPort(){
		return localPeerPort;
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
	public void addPeer(String peerName, String IP, int port, ArrayList<File> sharedFiles) {
		Peer peer = new Peer(peerName, IP, port, sharedFiles);
		
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
		
		gui.deletePeer(peer);
		peers.remove(peer);
	}
	
	/*
	 * 
	 */
	public void updatePeerSharedFiles(String peerName, ArrayList<File> sharedFiles) {
		Peer peer = getPeer(peerName);
		if(peer == null)
			return;
		
		peer.setSharedFiles(sharedFiles);
		gui.updatePeerFiles(peer);
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
	public void updateTransferProgress(String sendingPeerName, String recevingPeerName, String fileName, int chunkSize) {
		Transfer transfer = null;
		
		for(int i = 0; i < transfers.size(); i++) {
			if(transfers.get(i).getFile().getName().equals(fileName)
					&& transfers.get(i).getSendingPeer().getName().equals(sendingPeerName)
					&& transfers.get(i).getReceivingPeer().getName().equals(recevingPeerName))
				transfer = transfers.get(i);
		}
		
		if(transfer == null)
			return;
		
		transfer.updateProgress(chunkSize);
		gui.updateTransferProgress(transfer);
	}
}
