import java.nio.ByteBuffer;

public class MessageRequestFileResponse extends Message {
	String filename;
	int filesize;
	
	public MessageRequestFileResponse(String filename, int filesize)
	{
		this.filename = filename;
		this.filesize = filesize;
	}
	
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
		buffer.putInt(this.filename.length());
		buffer.put(this.filename.getBytes());
		buffer.putInt(this.filesize);
				
		byte dst[] = new byte[buffer.position()];
		buffer.flip();
		buffer.get(dst);
		
		return dst;
	}
	
	public int getType()
	{
		return Message.REQUEST_FILE_RESPONSE_TYPE;
	}
	
	public String getFilename()
	{
		return filename;
	}
	
	public int getFilesize()
	{
		return filesize;
	}
	
}
