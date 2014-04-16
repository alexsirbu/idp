import java.util.Timer;
import java.util.TimerTask;


public class NetworkModuleTimer
{
	private static int TIMEOUT = 1000;
	
	private Timer timer;
	private long timeout;
	private boolean timedOut;
	
	
	public NetworkModuleTimer()
	{
		this.timer = new Timer();
		this.timeout = NetworkModuleTimer.TIMEOUT;
		this.timedOut = false;
	}
	
	public void start()
	{
		final NetworkModuleTimer extThis = this;
		
		this.timer.schedule(new TimerTask() {
			@Override
			public void run()
			{
				extThis.timedOut = true;
			}
		}, this.timeout);
	}
	
	public void stop()
	{
		this.timer.cancel();
		this.timer.purge();
	}
	
	public void restart()
	{
		this.stop();
		this.start();
	}
	
	public boolean hasTimedOut()
	{
		return this.timedOut;
	}
}
