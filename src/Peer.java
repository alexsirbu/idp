import java.util.ArrayList;

/**
 * @author cristian
 *
 */
public class Peer {
	/*
	 * 
	 */
	private String name;
	/*
	 * 
	 */
	private ArrayList<File> sharedFiles;
	
	/*
	 * 
	 */
	public Peer(String name) {
		this.name = name;
	}
	
	/*
	 * 
	 */
	public Peer(String name, ArrayList<File> sharedFiles) {
		this.name = name;
		this.sharedFiles = sharedFiles;
	}
	
	/*
	 * 
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * 
	 */
	public ArrayList<File> getSharedFiles() {
		return sharedFiles;
	}
	
	/*
	 * 
	 */
	public File getSharedFileByName(String fileName) {
		for(int i=0; i<sharedFiles.size(); i++)
			if (sharedFiles.get(i).getName().equals(fileName))
			{
				return sharedFiles.get(i);
			}
	
		assert(false);
		return null;
	}
	
	/*
	 * 
	 */
	public void setSharedFiles(ArrayList<File> sharedFiles) {
		this.sharedFiles = sharedFiles;
	}
}
