import java.io.IOException;


public abstract class FileOperations {

	public String filename;
	
	public FileOperations(String filename)
	{
		this.filename = filename;
	}
	
	public abstract byte[] readAll() throws IOException;
	public abstract byte[] readAllFromPosition(int position) throws IOException;
	public abstract byte[] readNumberFromPosition(int bytesNumber, int position) throws IOException;
	public abstract void overWrite(byte[] content) throws IOException;
	public abstract void append(byte[] content) throws IOException;
	public abstract void writeAtPosition(byte[] content, int position) throws IOException;
	
	
}
