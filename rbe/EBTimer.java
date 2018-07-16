/*
 * rbe.EBTimer.java
 * Lei Zhang
 * 
 * TPC-W Timer
 *
 * This timer is used to send a message to the main thread to terminate all EBs.
*/

/*
Lei Zhang @McMaster: changing this TPC-W implementation from a closed queueing model 
	to an open queueing model.
*/

package rbe;

//public class EBTimer implements Runnable {
public class EBTimer extends Thread {

  private final RBE rbe;

// The following four times are absolute times (UTC), millis.
  //  They are corrected for the slow-down factor (rbe.slowDown).
  long start;    // When to start test
  //  WIRT and think times are only measured over the measurement interval (MI)
  long startMI;  // When measurement interval starts.
  long startRD;  // When measurement interval ends.
  long term;     // When to terminate test
  
    // Times are all expressed in milliseconds.
   public EBTimer(RBE rbe,
                   long start,    // When to start test.
                   long miWait,   // Length of ramp-up
                   long miDur,    // Duration of measurement interval
                   long rampDown) // Length of ramp-down
                   {
                   this.rbe = rbe;
       		this.start = start;
        		startMI = start + rbe.slow(miWait); //lei_open: miWait = RU + 52secs.
        		startRD = startMI + rbe.slow(miDur); //lei_open: miDur = MI
        		term    = startRD + rbe.slow(rampDown); //lei_open: rampDown = RD, term = start + miWait + MI + RD.

	 		System.out.println("lei: EBTimer - RU " + (startMI - start)/1000L + " MI " + (startRD- startMI)/1000L + 
                               " RD " + rbe.slow(rampDown)/1000L);
			}

    public void run() {
		System.out.println("lei: EBTimer - start timer!");
		long wait = term - System.currentTimeMillis();
            if (wait<0) return;
	     try {
	 	System.out.println("lei: wait for " + Math.ceil((double)(wait/1000)) + " seconds to terminate all EBs.");
		
             Thread.sleep(wait); //wait is ramp-up + ramp-down + measurement + 52 seconds.

	 } catch (InterruptedException ie) {
	     System.out.println("Interrupted in EBTimer sleep.");
	 }
		//rbe.timeFlag = false;
		/*synchronized(rbe) {
		rbe.notify();	//notify() method
			}*///lei_05, test purpose 
			//lei_05
			rbe.ebFlag = false;
			System.out.println("lei: ebFlag has been changed to " + rbe.ebFlag);
		System.out.println("lei: EBTimer - end timer!");
    }

}
