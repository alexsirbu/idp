import java.nio.ByteBuffer;

public class MessageRequestFilePart extends Message {

	String filename;
	int position;
	int length;
	String requester;
	
	public MessageRequestFilePart(String filename, int position, int length, String requester)
	{
		this.filename = filename;
		this.position = position;
		this.length = length;
		this.requester = requester;
	}
	
	public MessageRequestFilePart(byte[] encodedMessage)
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
		position=buffer.getInt();
		length=buffer.getInt();
		
		requesterSize = buffer.getInt();
		bytes=new byte[requesterSize];
		for(int i=0; i<requesterSize; i++)
			bytes[i]=buffer.get();
		
		requester = new String(bytes);
	}
	
	public byte[] encode()
	{
		ByteBuffer buffer = ByteBuffer.allocate(2000);
		
		buffer.putInt(Message.REQUEST_FILE_PART_TYPE);
		buffer.putInt(filename.length());
		buffer.put(filename.getBytes());
		buffer.putInt(position);
		buffer.putInt(length);
		buffer.putInt(requester.length());
		buffer.put(requester.getBytes());
		
		byte dst[] = new byte[buffer.position()]; 
		buffer.flip();
		buffer.get(dst);
		
		return dst;
	}
	
	public int getType()
	{
		return Message.REQUEST_FILE_PART_TYPE;
	}
	
	public String getFilename()
	{
		return filename;
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public int getLength()
	{
		return length;
	}
	
	public String getRequester()
	{
		return requester;
	}
	
}
