import java.nio.ByteBuffer;

public class NetworkMessage {

	Message message;
	boolean complete;
	
	public NetworkMessage(Message message)
	{
		this.message = message;
		this.complete = true;
	}
	
	public NetworkMessage(byte[] encodedMessage)
	{
		ByteBuffer buffer = ByteBuffer.wrap(encodedMessage);
		
		int messageSize = buffer.getInt();
		if (buffer.remaining() != messageSize)
			complete=false;
		else
		{
			byte[] messageBytes = new byte[messageSize];
			
			for(int i=0; i<messageSize; i++)
				messageBytes[i]=buffer.get();
			
			message = MessageFactory.decodeMessage(messageBytes);
			
			complete = true;
		}
	}
	
	public byte[] encode()
	{
		byte[] encodedMessage = message.encode();
		
		ByteBuffer buffer = ByteBuffer.allocate(encodedMessage.length+(Integer.SIZE/8));
		
		buffer.putInt(encodedMessage.length);
		buffer.put(encodedMessage);
		
		return buffer.array();
	}
	
	public Message getMessage()
	{
		return message;
	}
	
	public boolean getComplete()
	{
		return complete;
	}
	
}
