import java.nio.ByteBuffer;

public class MessageRequestFile extends Message{
	
	String filename;
	
	public MessageRequestFile(String filename)
	{
		this.filename=filename;
	}
	
	public MessageRequestFile(byte[] encodedMessage)
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
	}
	
	public byte[] encode()
	{
		ByteBuffer buffer = ByteBuffer.allocate(2000);
		
		buffer.putInt(Message.REQUEST_FILE_TYPE);
		buffer.putInt(filename.length());
		buffer.put(filename.getBytes());
		
		return buffer.array();
	}
	
	public int getType()
	{
		return Message.REQUEST_FILE_TYPE;
	}
}
