import junit.framework.TestCase;

public class MessageFactoryTest extends TestCase {
	
	public void testRequestFile()
	{
		Message msg = new MessageRequestFile("filename", "requester");
		Message decoded = MessageFactory.decodeMessage(msg.encode());
		assertTrue("Correct message type", decoded.getType()==Message.REQUEST_FILE_TYPE);
		assertTrue("Correct filename", ((MessageRequestFile)decoded).getFilename().equals("filename"));
		assertTrue("Correct requester", ((MessageRequestFile)decoded).getRequester().equals("requester"));		
	}
	
	public void testRequestFileResponse()
	{
		Message msg = new MessageRequestFileResponse("filename", 1024);
		Message decoded = MessageFactory.decodeMessage(msg.encode());
		assertTrue("Correct message type", decoded.getType()==Message.REQUEST_FILE_RESPONSE_TYPE);
		assertTrue("Correct filename", ((MessageRequestFileResponse)decoded).getFilename().equals("filename"));
		assertTrue("Correct filesize", ((MessageRequestFileResponse)decoded).getFilesize() == 1024);		
	}

}
