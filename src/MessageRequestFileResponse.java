import java.nio.ByteBuffer;

public class MessageRequestFileResponse extends Message {
	String filename;
	int filesize;
	
	public MessageRequestFileResponse(byte[] encodedMessage)
	{
		super(encodedMessage);
		
		int type;
		int filenameSize;
		
		ByteBuffer buffer = ByteBuffer.wrap(encodedMessage);
		
		type = buffer.getInt();
		filenameSize = buffer.getInt();
		
		byte[] bytes= new byte[filenameSize];
		
		for (int i=0; i<filenameSize; i++)
			bytes[i]=buffer.get();
		
		filename=new String(bytes);
		filesize = buffer.getInt();
	}
	
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
