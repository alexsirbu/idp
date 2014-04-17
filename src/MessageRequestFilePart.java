import java.nio.ByteBuffer;

public class MessageRequestFilePart extends Message {

	String filename;
	int position;
	int length;
	
	public byte[] encode()
	{
		ByteBuffer buffer = ByteBuffer.allocate(2000);
		
		buffer.putInt(Message.REQUEST_FILE_TYPE);
		buffer.putInt(filename.length());
		buffer.put(filename.getBytes());
		buffer.putInt(position);
		buffer.putInt(length);
		
		return buffer.array();
	}
	
}
