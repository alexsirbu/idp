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

	public void testRequestFilePart()
	{
		Message msg = new MessageRequestFilePart("filename", 10, 10, "requester");
		Message decoded = MessageFactory.decodeMessage(msg.encode());
		assertTrue("Correct message type", decoded.getType()==Message.REQUEST_FILE_PART_TYPE);
		assertTrue("Correct filename", ((MessageRequestFilePart)decoded).getFilename().equals("filename"));
		assertTrue("Correct length", ((MessageRequestFilePart)decoded).getLength() == 10);
		assertTrue("Correct position", ((MessageRequestFilePart)decoded).getPosition() == 10);
		assertTrue("Correct requester", ((MessageRequestFilePart)decoded).getRequester().equals("requester"));		
	}
	
	public void testRequestFilePartResponse()
	{
		Message msg = new MessageRequestFilePartResponse("filename", 10, 10, "requester".getBytes());
		Message decoded = MessageFactory.decodeMessage(msg.encode());
		assertTrue("Correct message type", decoded.getType()==Message.REQUEST_FILE_PART_RESPONSE_TYPE);
		assertTrue("Correct filename", ((MessageRequestFilePartResponse)decoded).getFilename().equals("filename"));
		assertTrue("Correct length", ((MessageRequestFilePartResponse)decoded).getLength() == 10);
		assertTrue("Correct position", ((MessageRequestFilePartResponse)decoded).getPosition() == 10);
		//assertTrue("Correct content", new String(((MessageRequestFilePartResponse)decoded).getContent()).equals("requester"));		
	}
}
