/*-------------------------------------------------------------------------
 * rbe.EBStats.java
 * Timothy Heil
 * 10/19/99
 *
 * ECE902 Fall '99
 *
 * Collects statistics for TPC-W.
 *------------------------------------------------------------------------
 *
 * This is part of the the Java TPC-W distribution,
 * written by Harold Cain, Tim Heil, Milo Martin, Eric Weglarz, and Todd
 * Bezenek.  University of Wisconsin - Madison, Computer Sciences
 * Dept. and Dept. of Electrical and Computer Engineering, as a part of
 * Prof. Mikko Lipasti's Fall 1999 ECE 902 course.
 *
 * Copyright (C) 1999, 2000 by Harold Cain, Timothy Heil, Milo Martin, 
 *                             Eric Weglarz, Todd Bezenek.
 *
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 * Everyone is granted permission to copy, modify and redistribute
 * this code under the following conditions:
 *
 * This code is distributed for non-commercial use only.
 * Please contact the maintainer for restrictions applying to 
 * commercial use of these tools.
 *
 * Permission is granted to anyone to make or distribute copies
 * of this code, either as received or modified, in any
 * medium, provided that all copyright notices, permission and
 * nonwarranty notices are preserved, and that the distributor
 * grants the recipient permission for further redistribution as
 * permitted by this document.
 *
 * Permission is granted to distribute this code in compiled
 * or executable form under the same conditions that apply for
 * source code, provided that either:
 *
 * A. it is accompanied by the corresponding machine-readable
 *    source code,
 * B. it is accompanied by a written offer, with no time limit,
 *    to give anyone a machine-readable copy of the corresponding
 *    source code in return for reimbursement of the cost of
 *    distribution.  This written offer must permit verbatim
 *    duplication by anyone, or
 * C. it is distributed by someone who received only the
 *    executable form, and is accompanied by a copy of the
 *    written offer of source code that they received concurrently.
 *
 * In other words, you are welcome to use, share and improve this codes.
 * You are forbidden to forbid anyone else to use, share and improve what
 * you give them.
 *
 ************************************************************************/

package rbe;

import java.io.PrintStream;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

import rbe.util.Histogram;
import rbe.util.Pad;

public class EBStats {

  public  static int maxError = 0;
  private static boolean DEBUG = false;

  private final Histogram [] wirt;     // WIRT (See TPC-W Spec.)
  private final Histogram tt;          // Think Time.
  private final int [][] trans;
  private final List<Long> start_times;
  private final List<Long> end_times;
  private final RBE rbe;

  // List of retries/errors encountered.
  private final Vector<EBError> errors = new Vector<EBError>(0);  

  // web-interaction throughput over time.
  //  Sampled continuously at 1 second intervals.
  private final int[] through;

  // Server error rate over time.
  //  Sampled continuously at 1 second intervals.
  private final int[] error_rate;

  // The following four times are absolute times (UTC), millis.
  //  They are corrected for the slow-down factor (rbe.slowDown).
  long start;    // When to start test
  //  WIRT and think times are only measured over the measurement interval (MI)
  long startMI;  // When measurement interval starts.
  long startRD;  // When measurement interval ends.
  long term;     // When to terminate test
  
    // Times are all expressed in milliseconds.
    public EBStats(RBE rbe,
                   int wirt_max, int wirt_binSize,
                   int tt_max, int tt_binSize,
                   int states,
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

		//lei_open: units are all milliseconds.
		//System.out.println("lei: miWait = " + miWait + " rbe.slow(miWait) = " + rbe.slow(miWait));
		System.out.println("lei: RU " + (startMI - start)/1000L + " MI " + (startRD- startMI)/1000L + 
                               " RD " + rbe.slow(rampDown)/1000L);

        if (DEBUG) {
            System.out.println("start " + start + " startMI " + startMI + 
                               " startRD " + startRD);
        }

        wirt = new Histogram[15];
        trans = new int[states][states];
        start_times = new ArrayList<Long>();
        end_times = new ArrayList<Long>();
        for (int h = 0; h < wirt.length; h++) {
            wirt[h] = new Histogram(wirt_max/wirt_binSize, wirt_binSize);
        }
        tt = new Histogram(tt_max/tt_binSize, tt_binSize);

        int lastBin = (int) (rbe.speed(term - start) / 1000L);
        through = new int[lastBin + 1];
        error_rate = new int[lastBin + 1];
    }

  public final synchronized void transition(int cur, int next) {
	 if (System.currentTimeMillis() < start) return;
    trans[cur][next]++;
  }

  public synchronized void
  interaction(int state, long wirt_t1, long wirt_t2, long itt)
  {
	 int b;  // Throughput bin.
	 // Discard interactions that completed before the start
	 //  of the ramp-up period.
	 if (wirt_t2 < start) return;
         //HWC
         assert start_times.size() == end_times.size();
	 start_times.add(new Long(wirt_t1));
	 end_times.add(new Long(wirt_t2));
	 //end HWC
	 
	 b= ((int) (rbe.speed(wirt_t2-start)/1000L));
	 if (b<through.length) {
	     through[b]++;
	 }
	 
	 if (DEBUG) {
	     System.out.println("t1 " + wirt_t1 + " t2 " + wirt_t2 + " startMI " + startMI + 
				" startRD " + startRD);
	     System.out.println("Interact " + ((wirt_t2-start)/1000L) + " b " + b);
	 }

	 if ((wirt_t2 >= startMI) && (wirt_t2 <= startRD)) {
	     if (DEBUG) {
		    System.out.println("adding...");
	     }
		
	      wirt[state].add((int) rbe.speed((wirt_t2-wirt_t1)));
	      tt.add((int) rbe.speed(itt));
	 }

  }

    public void error(String eb, String message, String url) 
    {
        EBError error = new EBError(eb, message, url);
        errors.addElement(error);
        System.out.println(error.toShortString());
        if ((errors.size() >= maxError) && (maxError > 0)) {
            System.exit(-1);
        }
    }

    public synchronized void serverError(long time)
    {
        if (time < start)
            return;
        int bin = ((int) (rbe.speed(time - start) / 1000));
        if (bin < error_rate.length) {
            error_rate[bin]++;
        }
    }

  public void print(PrintStream out) {
	 int h;

	 out.println("function [dat] = tpcw()");

	 for (h=0;h<wirt.length;h++) {
		wirt[h].printMFile(out, "dat.wirt{" + (h+1) + "}");
	 }

	 tt.printMFile(out, "dat.tt");

    int i,j,tot;

    out.println("dat.trans = [");
    for (i=0;i<trans.length;i++) {
      for (j=0;j<trans.length;j++) {
		  out.print(" " + Pad.l(6, "" + trans[i][j]));
      }
		out.println();
    }
	 out.println("];\n");

	 out.print("dat.interact = [");
    for (j=0;j<trans.length;j++) {
      for (i=0,tot=0;i<trans.length;tot=tot + trans[i][j],i++);
		out.print(" " + Pad.l(6, "" + tot));
	 }
    out.println("];\n");

	 out.println("dat.wips = [");
	 for (j=0;j<through.length;j++) {
		out.println(""+through[j]);
	 }
    out.println("];\n");

	 out.println("dat.seps = [");
	 for (j=0;j<error_rate.length;j++) {
		out.println(""+error_rate[j]);
	 }
    out.println("];\n");

    out.println("dat.starttimes = [");
    long start_time0 = start_times.get(0).longValue();
    for (j = 0; j < start_times.size(); j++) {
	out.println(""+(start_times.get(j).longValue() - start_time0));
    }
    out.println("];\n");
    out.println("dat.endtimes = [");
    for (j = 0; j < start_times.size(); j++) {
	out.println(""+(end_times.get(j).longValue() - start_time0));
    }
    out.println("];\n");
    out.println("dat.numinteractions = " + start_times.size() + ";");
	 // Absolute UTC time (millis).
	 out.println("dat.startRU =  " + start + ";");

	 // Slowdown corrected UTC time (millis).
	 out.println("dat.startMI =  " + (start + rbe.speed(startMI-start)) + ";");
	 out.println("dat.startRD =  " + (start + rbe.speed(startRD-start)) + ";");
	 out.println("dat.term    =  " + (start + rbe.speed(term-start)) + ";");

	 // slow down factor.
	 out.println("dat.slowDown = " + rbe.slowDown + ";");

    out.println("% Errors");
    for (i=0;i<errors.size();i++) {
      out.println("%" + errors.elementAt(i));
    }
    out.println("% Total Errors: " + errors.size());
  }

  public void waitForRampDown()
  {
         waitForStart();
         long w = term - System.currentTimeMillis();
         if (w<0) return;
	 try {
	 	//lei_open: if w>0, the thread will sleep for w millis.
	 	////System.out.printf("lei: w = %d\n",w); // no difference between these two methods.
	 	//System.out.println("lei: w = " + w);
		//long temp = System.currentTimeMillis();
		//System.out.println("lei: sleep starts at: " + temp);
		
             Thread.sleep(w); //lei_open: w is ramp-up + ramp-down + measurement + 52 seconds.

		//lei_open
		//System.out.println("lei: sleep ends at: " + System.currentTimeMillis());
		//temp = System.currentTimeMillis() - temp;
		//System.out.println("lei: sleep for: " + temp + "mills.");
		
	 } catch (InterruptedException ie) {
	     System.out.println("Interrupted in waitForRampDown");
	 }
  }

  public void waitForStart()
  {
	 long w = start - System.currentTimeMillis();
	 if (w<0) return;

         System.out.println("Waiting for " + (w / 1000) + " sec before start");
	 try {
             Thread.sleep(w);
	 } catch (InterruptedException ie) {
	     System.out.println("Interrupted in waitForStart");
	 }
  }
}

class EBError {
    private String eb;
    private String message;
    private String url;

    public EBError(String eb, String message, String url) {
        this.eb = eb;
        this.message = message;
        this.url = url;
    }

    public String toShortString() {
        String msg = eb + " Error: " + message;
        return msg;
    }

    public String toString() {
        String msg = eb + " Error: " + message;
        if (url != null && message.indexOf(url) == -1) {
            msg += " (" + url + ")";
        }
        return msg;
    }
}
