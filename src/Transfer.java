/**
 *
 */
public class Transfer {
	/*
	 * 
	 */
	private File file;
	/*
	 * 
	 */
	private Peer sendingPeer;
	/*
	 * 
	 */
	private Peer receivingPeer;
	/*
	 * 
	 */
	private int progress;
	
	
	/*
	 * 
	 */
	public Transfer(File file, Peer sender, Peer receiver) {
		this.file = file;
		this.sendingPeer = sender;
		this.receivingPeer = receiver;
		this.progress = 0;
	}
	
	/*
	 * 
	 */
	public File getFile() {
		return file;
	}
	
	/*
	 * 
	 */
	public Peer getSendingPeer() {
		return sendingPeer;
	}
	
	/*
	 * 
	 */
	public Peer getReceivingPeer() {
		return receivingPeer;
	}

	/*
	 * 
	 */
	public int getProgress() {
		return progress;
	}

	/*
	 * 
	 */
	public void updateProgress(int chunkSize) {
		progress += (chunkSize * 100) / file.getSize();
		progress = Math.min(progress, 100);
	}
}
