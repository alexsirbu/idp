/**
 * @author cristian
 *
 */
public class Transfer {
	private File file;
	private Peer peer;
	private int progress;
	
	public Transfer(File file, Peer peer) {
		this.file = file;
		this.peer = peer;
	}
	
	public File getFile() {
		return file;
	}
	
	public Peer getPeer() {
		return peer;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	
}
