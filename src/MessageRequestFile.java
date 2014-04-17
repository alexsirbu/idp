import java.nio.ByteBuffer;

public class MessageRequestFile extends Message{
	
	String filename;
	String requester;
	
	public MessageRequestFile(String filename, String requester)
	{
		this.filename=filename;
		this.requester=requester;
	}
	
	public MessageRequestFile(byte[] encodedMessage)
	{
		super(encodedMessage);
		
		int type;
		int filenameSize;
		int requesterSize;
		
		ByteBuffer buffer = ByteBuffer.wrap(encodedMessage);
		
		type = buffer.getInt();
		filenameSize = buffer.getInt();
		
		byte[] bytes= new byte[filenameSize];
		
		for (int i=0; i<filenameSize; i++)
			bytes[i]=buffer.get();
		
		filename=new String(bytes);
		
		requesterSize = buffer.getInt();
		bytes = new byte[requesterSize];
		
		for (int i=0; i<requesterSize; i++)
			bytes[i]=buffer.get();
		
		requester = new String(bytes);
	}
	
	public byte[] encode()
	{
		ByteBuffer buffer = ByteBuffer.allocate(2000);
		
		buffer.putInt(Message.REQUEST_FILE_TYPE);
		buffer.putInt(filename.length());
		buffer.put(filename.getBytes());
		buffer.putInt(requester.length());
		buffer.put(requester.getBytes());
		
		return buffer.array();
	}
	
	public int getType()
	{
		return Message.REQUEST_FILE_TYPE;
	}
	
	public String getFilename()
	{
		return filename;
	}
	
	public String getRequester()
	{
		return requester;
	}
}
