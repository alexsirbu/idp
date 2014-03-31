package worker;

import java.util.List;

import javax.swing.SwingWorker;

public class ExportTask extends SwingWorker<Integer, Integer> {
	  private static final int DELAY = 1000;

	  public ExportTask() {
		  System.out.println("constructor thread:"+Thread.currentThread());
		  
	  }

	  @Override
	  protected Integer doInBackground() throws Exception {
		// TODO 3.2
		  System.out.println("doInBackground thread:"+Thread.currentThread());
		  
		  int DELAY = 1000;
		  int count = 10;
		  int i     = 0;
		  try {
		      while (i < count) {
		          i++;
		  	Thread.sleep(DELAY);
		  	publish(i);
		  	this.setProgress(i);
		      }
		    
		  } catch (InterruptedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		  }
		return 0;
	  }
	   
	  protected void process(List<Integer> chunks) {
	   // TODO 3.3 - print values received
		  System.out.println("process thread:"+Thread.currentThread());
		  int i;
		  System.out.println("Task 2:");
		  for(i=0;i<chunks.size();i++)
			  System.out.println(chunks.get(i));
		  System.out.println("Task 3");
		  System.out.println(chunks);
	  }

	  @Override
	  protected void done() {
		System.out.println("done thread:"+Thread.currentThread());
	    if (isCancelled())
	      System.out.println("Cancelled !");
	    else
	      System.out.println("Done !");
	  }
	}