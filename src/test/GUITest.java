package test;



import org.uispec4j.Button;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;

import app.Main;


public class GUITest extends UISpecTestCase{
	
	@Override
	protected void setUp()
	{
		setAdapter(new MainClassAdapter(Main.class, new String[0]));
	}

	@Override
	protected void tearDown()
	{
	
	}
	
	public void testExport()
	{
		Window window = this.getMainWindow();
		Button button = window.getButton("Export image");
		button.click();
	
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(window.getProgressBar().isCompleted());
	}
}
