import java.nio.ByteBuffer;

public class MessageRequestFileResponse extends Message {
	String filename;
	int filesize;
	
	public byte[] encode()
	{
		ByteBuffer buffer = ByteBuffer.allocate(2000);
		
		buffer.putInt(Message.REQUEST_FILE_RESPONSE_TYPE);
		buffer.putInt(filename.length());
		buffer.put(filename.getBytes());
		buffer.putInt(filesize);
		
		return buffer.array();
	}
	
}
