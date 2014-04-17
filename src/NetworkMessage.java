import java.nio.ByteBuffer;

public class NetworkMessage {

	int messageSize;
	Message message;
	
	public NetworkMessage(int messageSize, Message message)
	{
		this.messageSize = messageSize;
		this.message = message;
	}
	
	public NetworkMessage(byte[] encodedMessage)
	{
		ByteBuffer buffer = ByteBuffer.wrap(encodedMessage);
		
		messageSize = buffer.getInt();
		byte[] messageBytes = new byte[messageSize];
		
		for(int i=0; i<messageSize; i++)
			messageBytes[i]=buffer.get();
		
		message = MessageFactory.decodeMessage(messageBytes);
	}
	
	public byte[] encode()
	{
		byte[] encodedMessage = message.encode();
		
		ByteBuffer buffer = ByteBuffer.allocate(encodedMessage.length+(Integer.SIZE/8));
		
		buffer.putInt(encodedMessage.length);
		buffer.put(encodedMessage);
		
		return buffer.array();
	}
	
}
