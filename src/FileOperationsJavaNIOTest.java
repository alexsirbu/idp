import junit.framework.TestCase;

public class FileOperationsJavaNIOTest extends TestCase{

	public void testReadAll()
	{
		try
		{
			FileOperations fo = new FileOperationsJavaNIO("fileoptest");
			byte[] read = fo.readAll();
			assertTrue("Read all bytes of the file", read.length == 16);
			assertTrue("Read correct bytes", new String(read).equals("This is a test!"+(char)(10)));
		}
		catch (Exception e)
		{
			fail("ReadAll threw exception!");
		}
	}
	
	public void testReadAllUnexistingFile()
	{
		try
		{
			FileOperations fo = new FileOperationsJavaNIO("fileoptest_dummy");
			fo.readAll();
		}
		catch (Exception e)
		{
			return;
		}
		fail("ReadAll din not throw exception!");
	}
	
	public void testReadAllFromPosition()
	{
		try
		{
			FileOperations fo = new FileOperationsJavaNIO("fileoptest");
			byte[] read = fo.readAllFromPosition(8);
			assertTrue("Read all bytes of the file", read.length == 8);
			assertTrue("Read correct bytes", new String(read).equals("a test!"+(char)(10)));
		}
		catch (Exception e)
		{
			fail("ReadAllFromPosition threw exception!");
		}
	}
	
	public void testReadNumberFromPosition()
	{
		try
		{
			FileOperations fo = new FileOperationsJavaNIO("fileoptest");
			byte[] read = fo.readNumberFromPosition(6, 8);
			assertTrue("Read all bytes of the file", read.length == 6);
			assertTrue("Read correct bytes", new String(read).equals("a test"));
		}
		catch (Exception e)
		{
			fail("ReadNumberFromPosition threw exception!");
		}
	}
	
	public void testOverwrite()
	{
		try
		{
			FileOperations fo = new FileOperationsJavaNIO("fileoptest");
			fo.overWrite(("This is a test!"+(char)(10)).getBytes());
			this.testReadAll();
		}
		catch (Exception e)
		{
			fail("Overwrite threw exception!");
		}
	}
	
	public void testAppend()
	{
		try
		{
			FileOperations fo = new FileOperationsJavaNIO("fileoptest");
			fo.overWrite("This is".getBytes());
			fo.append((" a test!"+(char)(10)).getBytes());
			this.testReadAll();
		}
		catch (Exception e)
		{
			fail("Append threw exception!");
		}
	}
	
	public void testWriteAtPosition()
	{
		try
		{
			FileOperations fo = new FileOperationsJavaNIO("fileoptest");
			fo.overWrite(("This is a fake!"+(char)(10)).getBytes());
			fo.writeAtPosition("test".getBytes(), 10);
			this.testReadAll();
		}
		catch (Exception e)
		{
			fail("Append threw exception!");
		}
	}
	
}
