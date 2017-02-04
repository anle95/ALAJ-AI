
public class Time extends Thread {
	private AI monitor;
	
	public Time(AI ai) {
		monitor = ai;
	}
	
	public void run() {
		long t;
		t  = System.currentTimeMillis();
		while(true) {
			t += 10;
			long diff = t - System.currentTimeMillis();
			if(diff > 0) try {
				Thread.sleep(diff);
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			monitor.incrementTime();
		}
	}
}
