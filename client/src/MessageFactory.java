import java.nio.ByteBuffer;

public class MessageFactory {

	public static Message decodeMessage(byte[] encodedMessage)
	{
		ByteBuffer buffer = ByteBuffer.wrap(encodedMessage);
		
		int type = buffer.getInt();
				
		if (type == Message.REQUEST_FILE_PART_RESPONSE_TYPE)
			return new MessageRequestFilePartResponse(encodedMessage);
		else if (type == Message.REQUEST_FILE_PART_TYPE)
			return new MessageRequestFilePart(encodedMessage);
		else if (type == Message.REQUEST_FILE_RESPONSE_TYPE)
			return new MessageRequestFileResponse(encodedMessage);
		else if (type == Message.REQUEST_FILE_TYPE)
			return new MessageRequestFile(encodedMessage);
		
		return null;
	}
}
