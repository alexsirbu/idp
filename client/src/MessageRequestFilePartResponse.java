import java.nio.ByteBuffer;

public class MessageRequestFilePartResponse extends Message {

	String filename;
	int position;
	int length;
	byte[] content;
	
	public MessageRequestFilePartResponse(String filename, int position, int length, byte[] content)
	{
		this.filename = filename;
		this.position = position;
		this.length = length;
		this.content = content;
	}
	
	public MessageRequestFilePartResponse(byte[] encodedMessage)
	{
		super(encodedMessage);
		
		int type;
		int filenameSize;
		
		ByteBuffer buffer = ByteBuffer.wrap(encodedMessage);
		
		type = buffer.getInt();
		assert(type == Message.REQUEST_FILE_PART_RESPONSE_TYPE);

		filenameSize = buffer.getInt();
		
		byte[] bytes= new byte[filenameSize];
		
		for (int i=0; i<filenameSize; i++)
			bytes[i]=buffer.get();
		
		filename=new String(bytes);
		position=buffer.getInt();
		length=buffer.getInt();
		
		content = new byte[length];
		
		for (int i=0; i<length; i++)
			content[i]=buffer.get();
	}
	
	public byte[] encode()
	{
		ByteBuffer buffer = ByteBuffer.allocate(2000);
		
		buffer.putInt(Message.REQUEST_FILE_PART_RESPONSE_TYPE);
		buffer.putInt(filename.length());
		buffer.put(filename.getBytes());
		buffer.putInt(position);
		buffer.putInt(length);
		buffer.put(content);
		
		byte dst[] = new byte[buffer.position()]; 
		buffer.flip();
		buffer.get(dst);
		
		return dst;
	}
	
	public int getType()
	{
		return Message.REQUEST_FILE_PART_RESPONSE_TYPE;
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
	
	public byte[] getContent()
	{
		return content;
	}
	
}
