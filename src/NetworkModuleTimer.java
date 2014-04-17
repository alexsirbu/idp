import java.util.Timer;
import java.util.TimerTask;


public class NetworkModuleTimer
{
	private static int TIMEOUT = 3000;
	
	private Timer timer;
	private TimerTask timerTask;
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
		this.timedOut = false;
		
		final NetworkModuleTimer extThis = this;
		
		this.timerTask = new TimerTask() {
			@Override
			public void run()
			{
				extThis.timedOut = true;
			}
		};
		
		this.timer.schedule(this.timerTask, this.timeout);
	}
	
	public void stop()
	{
		this.timerTask.cancel();
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
