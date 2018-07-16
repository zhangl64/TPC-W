/*-------------------------------------------------------------------------
 * rbe.RBE.java
 * Timothy Heil
 * ECE902 Fall '99
 *
 * TPC-W Remote Browser Emulator.
 *
 * Main program.
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

/* ***********************************************************************
*  Tibor Horvath [tibor@cs.virginia.edu]                                 *
*    - Several enhancements, interoperability with our server code.      *
*    - Additional workload options (trace-based simulation).             *
*    - Better rampup options and implementation.                         *
*    - Compiles clean with Java 1.5.                                     *
*************************************************************************/

/*
Lei Zhang @McMaster: changing this TPC-W implementation from a closed queueing model 
	to a semi-open queueing model. 
Version 0.1
	base function, generate a hard-coding number of EBs, interarrival time is based on input arrival rate.
	Currently, inter-request think time is all zero.
	
Change logs:
lei_open - Feb 3rd, 2015
	this first round of changes, mainly for study purpose (put some comments) and adding some debug messages.
	
lei_01 - Feb 10, 2015
	adding a thread which will wait for (RU+MI+RD) seconds, and then send a notify() 
	back to the main thread to kill all EBs.

lei_02 - Feb 17, 2015
	adding think time function after each start of an EB (arrival rate control).

lei_03 - Feb 21, 2015
	if a session is completed, no new session will be initialized.

lei_04 - Feb 22, 2015
	1, release the time limitation of ramp-up, in other words, the time intveral for EBs' startup in rampEBs method.
	2, release the number limit of EBs. 

lei_05 - Feb 23, 2015
	verify lei_01's work, try to use a boolean (flag) to notify the main thread (rampEBs method) to kill all EBs.

lei_06 - Feb 24, 2015 
	modify TPCW-1,2,3, so that each session will only send one request, and then leave 
	(to be adjusted, because the first request is always home request).

lei_07 - March 1, 2015
	add arrival rate lambda as one of the user input arguments (TBA).

lei_08 - March 25, 2015
	revisit from pure-open TPC-W, debug.

lei_09 - March 30, 2015
	first release, features added:
	1. add three variables, maxUser, userCounter, queueCounter.
	2. in RBE.java, before a session starts, check userCounter.
	3. in EB.java, when a session completed, check queueCounter.

lei_10 - April 07, 2015
	second release, feature changed:
	instead of complete a session based on an exponentially distributted time interval, 
	finish every session if the next request is a "home" request (not including the first "home" request).
*/


package rbe;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Random;
import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;

//import java.util.Queue;	//lei_01

import rbe.args.Arg;
import rbe.args.ArgDB;
import rbe.args.IntArg;
import rbe.args.PrintStreamArg;
import rbe.args.DateArg;
import rbe.args.DoubleArg;
import rbe.args.StringArg;
import rbe.args.BooleanArg;

import rbe.util.StrStrPattern;

import rbe.util.Pad;

public class RBE {

  // URLs
  public static String www1 = "http://localhost:80/";
  public static String www;
  public static String homeURL;
  public static String shopCartURL;
  public static String orderInqURL;
  public static String orderDispURL;
  public static String searchReqURL;
  public static String searchResultURL;
  public static String newProdURL;
  public static String bestSellURL;
  public static String prodDetURL;
  public static String custRegURL;
  public static String buyReqURL;
  public static String buyConfURL;
  public static String adminReqURL;
  public static String adminConfURL;
  public static Date startTime;

  public static final StrStrPattern yourCID = 
    new StrStrPattern("C_ID");
  public static final StrStrPattern yourShopID = 
    new StrStrPattern("SHOPPING_ID=");
  public static final StrStrPattern yourSessionID = 
      //new StrStrPattern("JIGSAW-SESSION-ID=");
      //      new StrStrPattern("JServSessionIdroot=");
    new StrStrPattern(";jsessionid=");
  public static final StrStrPattern endSessionID =
    new StrStrPattern("?");

  static {
	 setURLs();
  }

  public static void setURLs()
  {
	 www = www1 + "tpcw/";
	 homeURL = www+"home";
	 shopCartURL = www+"cart";
	 orderInqURL = www+"order_status";
	 orderDispURL = www+"order_display";
	 searchReqURL = www+"search";
	 searchResultURL = www+"execute_search";
	 newProdURL = www+"new_products";
	 bestSellURL = www+"best_sellers";
	 prodDetURL = www+"item";
	 custRegURL = www+"register";
	 buyReqURL = www+"buy";
	 buyConfURL = www+"buy_confirm";
	 adminReqURL = www+"admin";
	 adminConfURL = www+"admin_process";
  }

  // FIELD NAMES
  public static final String field_cid = "C_ID";
    //public static final String field_sessionID = "JServSessionIdroot";
    
    //public static final String field_sessionID = "JIGSAW-SESSION-ID";
  public static final String field_sessionID = ";jsessionid=";
  public static final String field_shopID = "SHOPPING_ID";
  public static final String field_uname = "UNAME";
  public static final String field_passwd = "PASSWD";
  public static final String field_srchType = "search_type";
  public static final String   authorType = "author";
  public static final String   subjectType = "subject";
  public static final String   titleType = "title";
  public static final String field_srchStr = "search_string";
  public static final String field_addflag = "ACTION"; // = "ADD_FLAG";
  public static final String field_iid = "I_ID";
  public static final String field_qty = "QTY";
  public static final String field_subject = "subject";
  public static final String field_retflag = "RETURNING_FLAG";
  public static final String field_fname = "FNAME";
  public static final String field_lname = "LNAME";
  public static final String field_street1 = "STREET_1";
  public static final String field_street2 = "STREET_2";
  public static final String field_city = "CITY";
  public static final String field_state = "STATE";
  public static final String field_zip = "ZIP";
  public static final String field_country = "CO_ID";
  public static final String field_phone = "PHONE";
  public static final String field_email = "EMAIL";
  public static final String field_birthdate = "BIRTHDATE";
  public static final String field_data = "DATA";
  public static final String field_cctype = "CC_TYPE";
  public static final String field_ccnumber = "CC_NUMBER";
  public static final String field_ccname = "CC_NAME";
  public static final String field_ccexp = "CC_EXPIRY";
  public static final String field_shipping = "SHIPPING";
  public static final String field_newimage = "I_NEW_IMAGE";
  public static final String field_newthumb = "I_NEW_THUMBNAIL";
  public static final String field_newcost = "I_NEW_COST";
  public static final String field_newstock = "I_NEW_STOCK";

  public static boolean getImage; // Whether to fetch images.
  public static boolean monitor; //Whether or not to do monitoring

  public int numCustomer = 1000;  // Number of initial customers
  public int cidA = 1023;         // Used for generating random CIDs
                                  //  See TPC-W spec Clause 2.3.2

  // TPC-W spec values for cidA.  See TPC-W spec Clause 2.3.2
  public static final int [][] stdCIDA = 
  // For NUM_CUSTOMERS in this range    Value for A
  {
    {          1,      9999,                     1023 },
    {      10000,     39999,                     4095 },
    {      40000,    159999,                    16383 },
    {     160000,    639999,                    65535 },
    {     640000,   2559999,                   262143 },
    {    2560000,  10239999,                  1048575 },
    {   10240000,  40959999,                  4194303 },
    {   40960000, 163839999,                 16777215 },
    {  163840000, 655359999,                 67108863 }
  };

  public static int numItem = 10000;     // Number of items for sale.
  public static int numItemA = 511;      // Used for search strings.  See
                                         //  TPC-W Spec. 2.10.5.1

  public static final int [][] stdNumItemA =
  // For NUM_ITEMS    Value for A
  {
    {      1000,               63 },
    {     10000,              511 },
    {    100000,             4095 },
    {   1000000,            32767 },
    {  10000000,           524287 }
  };

  public int maxImageRd=10;       // Maximum number of images to read at once.
  public EBStats stats;

//lei_02 add static
  public double slowDown;         // Time slow down factor.
  //  1000 means one thousand real seconds equals one simulated second.
  public double speedUp;          // 1/slowDown.

  public static final BufferedReader bin = 
    new BufferedReader(new InputStreamReader(System.in));

  //lei_01
  //public EBTimer ebTimeout;
  //public static boolean timeFlag; 
 // private final Queue shareQ;
 //lei_02
   public Random rand = new Random(); 
  // public static long lambda = 1;
//lei_05
  public static boolean ebFlag = true;
  //lei_09 start
  public static int maxUser = 1000;
  public static int userCounter = 0;
  public static int queueCounter = 0;
  //public volatile boolean requestFlag = false;
  //lei_09 end

  public static void main(String [] args) {
    RBE rbe = new RBE();
    Vector<EB> ebs = new Vector<EB>(0);

    System.out.println("Remote Browser Emulator for TPC-W.");
    System.out.println("  University of Virginia");
    System.out.println("  Version 1.7 (05/2007)");

    ArgDB db = new ArgDB();

    startTime = new Date();

	//lei_open, number of ebs.
    EBFactoryArg ebfArg =
      new EBFactoryArg("-EB", "EB Factory",
		       "% Factory class used to create EBs.  " + 
		       "<class> <#> <factory args...>.", 
		       rbe, ebs, db);

    PrintStreamArg oFile =
      new PrintStreamArg("-OUT", "Output file",
			 "% Name of matlab .m output file for results.", db);

    StringArg traceArg = 
        new StringArg("-TRACE", "Trace file",
                      "% Path to trace input data file. File has two " +
                      "columns: time(s) and load. Loads are normalized to " +
                      "the #EBs specified with -EB.",
                      db);

    DoubleArg traceSpeedArg = 
      new DoubleArg("-TRACESPEED", "Speed up the trace by this factor",
		    "% 60 means 1 hour of the trace will be simulated " + 
		    "in 1 minute.  Accepts factional values.",
                    1.0, db);

    DateArg st = 
      new DateArg("-ST", "Starting time for ramp-up",
		  "% Time (such as Nov 2, 1999 11:30:00 AM CST) " + 
		  "at which to start ramp-up." +
		  "  Useful for synchronizing multiple RBEs.", 
		  System.currentTimeMillis()+2000L, db);

    IntArg ru = new IntArg("-RU", "Ramp-up time", 
	       "% Seconds used to warm-up the simulator.", 10*60, db);

    IntArg mi = new IntArg("-MI", "Measurement interval",
	       "% Seconds used for measuring SUT performance.", 30*60, db);

    IntArg rd = new IntArg("-RD", "Ramp-down time", 
	       "% Seconds of steady-state operation following " + 
	       "measurment interval.", 5*60, db);

    DoubleArg slow = 
      new DoubleArg("-SLOW", "Slow-down factor",
		    "% 1000 means one thousand real seconds equals one " + 
		    "simulated second.  " + 
		    "Accepts factional values and E notation.", 1.0, db);

	//lei_open, think time scale
    DoubleArg tt_scale = 
      new DoubleArg("-TT", "Think time multiplication.",
		    "% Used to increase (>1.0) or decrease (<1.0) think " +
		    "time.  In addition to slow-down factor.", 1.0, db);
	//lei_07 start
	
	//lei_07 end

    BooleanArg key =
        new BooleanArg("-KEY", "Interactive control.",
                       "% Require user to hit RETURN before every " +
                       "interaction.  Overrides think time.",
                       false, db);

    BooleanArg getImage =
        new BooleanArg("-GETIM", "Request images.",
                       "% True will cause RBE to request images.  " +
                       "False suppresses image requests.",
                       true, db);

    IntArg img = new IntArg("-CON", "Image connections",
	       "% Maximum number of images downloaded at once.", 4, db);

    IntArg cust = 
      new IntArg("-CUST", "Number of customers",
		 "% Number of customers in the database.  " + 
		 "Used to generated random CIDs.", 1000, db);

    IntArg custa = 
      new IntArg("-CUSTA", "CID NURand A",
		 "% Used to generate random CIDs.  " + 
		 "See TPC-W Spec. Clause 2.3.2.  " +
		 "-1 means use TPC-W spec. value.", -1, db);

    IntArg item = 
      new IntArg("-ITEM", "Number of items",
		 "% Number of items in the database. " +
		 "Used to generate random searches.", 10000, db);

    IntArg itema = 
      new IntArg("-ITEMA", "Item NURand A",
		 "% Used to generate random searches.  " + 
		 "See TPC-W Spec. Clause 2.10.5.1.  " +
		 "-1 means use TPC-W spec. value.", -1, db);

    IntArg debug = 
      new IntArg("-DEBUG", "Debug message.",
		 "% Increase this to see more debug messages ~1 to 10.",
		 0, db);

    IntArg maxErr = 
        new IntArg("-MAXERROR", "Maximum errors allowed.",
                   "% RBE will terminate after this many errors.  " +
                   "0 implies no limit.",
                   1, db);

    StringArg wwwArg = 
        new StringArg("-WWW", "Base URL",
                      "% The root URL for the TPC-W pages.",
                      RBE.www1, db);

    BooleanArg monArg = 
        new BooleanArg("-MONITOR", "Do utilization monitoring", 
                       "% TRUE=do monitoring, FALSE=Don't do monitoring",
                       false, db);

    IntArg bipArg = 
        new IntArg("-BIP", "Browser increase period", 
                   "% Milliseconds to allocate to starting each EB. " +
                   "0 means start all EBs immediately, larger values " +
                   "are more incremental. Increases the ramp-up time.",
                   0, db);

    if (args.length==0) {
      Usage(args, db);
      return;
    }

    try {
      db.parse(args);
    }
    catch (Arg.Exception ae) {
      System.out.println("Error:");
      System.out.println(ae);
      Usage(args, db);
      return;
    }

	//lei_open
	//System.out.println("lei: Print all args.");
    Usage(args, db);	//lei_open, print all arguments and options.
	//lei_open
	//System.out.println("lei: All args printed.");

    // Copy in parameters.
    rbe.maxImageRd  = img.num;
    rbe.numCustomer = cust.num;
    rbe.cidA        = custa.num;
    RBE.numItem     = item.num;
    RBE.numItemA    = itema.num;
    RBE.www1        = wwwArg.s;
    RBE.getImage    = getImage.flag;
    RBE.monitor     = monArg.flag;
    RBE.setURLs();
    EB.DEBUG = debug.num;
    EBStats.maxError = maxErr.num;

    // Check for numCustomer valid. 
    if (rbe.numCustomer < 1) {
      System.out.println("Number of customers (" + rbe.numCustomer + 
			 ")  must be >= 1.");
      return;
    }

    if (rbe.numCustomer > stdCIDA[stdCIDA.length-1][1]) {
      System.out.println("Number of customers (" + rbe.numCustomer + 
			 ")  must be <= " +
			 stdCIDA[stdCIDA.length-1][1] + ".");
      return;
    }

    // Compute default CID A value, if needed.
    if (rbe.cidA == -1) {
		//lei_open
		//System.out.println("lei: CUSTA == -1");
      for (int i=0;i<stdCIDA.length;i++) {
	if ((rbe.numCustomer >= stdCIDA[i][0]) &&
	    (rbe.numCustomer <= stdCIDA[i][1])) {
	    	//lei_open
		//System.out.println("lei: i = " + i + ", numCustomer = " + rbe.numCustomer + ", and stdCIDA[i][0] = " + stdCIDA[i][0]);
	  rbe.cidA = stdCIDA[i][2];
	  System.out.println("Choose " + rbe.cidA + " for -CUSTA.");
	  break;
	}
      }
    }

    // Check num item is valid.
    for (int i=0; i<stdNumItemA.length; i++) {
	if (RBE.numItem == stdNumItemA[i][0]) {
	    // Compute standard item A value, if needed.
	    if (RBE.numItemA == -1) {
		RBE.numItemA = stdNumItemA[i][1];
		System.out.println("Choose " + RBE.numItemA + " for -ITEMA.");
	    }
	    break;
	}

	if (i == stdNumItemA.length - 1) {
	    System.out.println("Number of items (" + RBE.numItem + 
			       ") must be one of ");
	    for (int j=0;j<stdNumItemA.length;j++) {
		System.out.println("    " + stdNumItemA[j][0]);
	    }
	    return;
	}
    }

    // Check max image read parameter.
    if (rbe.maxImageRd < 1) {
      System.out.println("-CON must be >= 1.");
      return;
    } 

    // Correct ramp-up time by start time.
    long start = st.d.getTime();
    long addRU = start - System.currentTimeMillis();

	//lei_open
	//System.out.println("lei: addRU = " + addRU);

	//lei_10, when we set EB number is equal to 100,000, we observed the following warning.
	// it means that the time we get from Date.getTime() is less than System.currentTimeMillis().
    if (addRU < 0L) {
      System.out.println("Warning: start time " + (((double) addRU)/1000.0) + 
			 " seconds before current time.\n" +
			 "Resetting to current time.");
      start = System.currentTimeMillis();
    }

    rbe.slowDown = slow.num;
    rbe.speedUp  = 1.0/rbe.slowDown;

    String pidStr = null;
    if(monitor){
	try {
	    int j;
	    char c[] = new char[10];
	    Runtime r = Runtime.getRuntime();
	    Process proc = r.exec("/usr/local/bin/monitor -log /bigvol2/monitor_traces/java.mon -interval 10 -sample 10 -L -Toplog -F");
	    //	    InputStream i= proc.getInputStream();
	    InputStreamReader reader= new InputStreamReader(proc.getInputStream());
	    reader.skip(40);
	    
	    reader.read(c, 0, 10);
	    for(j = 0; c[j]!= '\n'; j++);
	    pidStr = new String(c,0,j);
	    System.out.println("Pid = " + pidStr);
	    
	} catch(java.lang.Exception ex){
	    System.out.println("Unable to monitor process");
	}
    }

    // Set up EB parameters.
    for (int i=0;i<ebs.size();i++) {
      EB e = ebs.elementAt(i);
      e.tt_scale = tt_scale.num;	//lei_open, set think time scale
      e.waitKey = key.flag;
    }

    int ebCount = ebs.size();
    TraceSim traceSim = null;
    if (traceArg.s != null) {
        try {
            traceSim = new TraceSim(traceArg.s, traceSpeedArg.num, ebs.size());
			//lei_open
			System.out.println("traceSim is an objective now."); //never show
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        ebCount = traceSim.getInitEBCount();
    }

    // Create statistics object.
    long timeToStartAll = ebCount * bipArg.num;
	//lei_open: if bip=0, rampupMillis = 52 secs. + ru.num.
    long rampupMillis = timeToStartAll
        + 32000  // Correction for maximum 99%-ile think time.
        + 20000  // Correction for maximum initial think time.
        + 1000L * ru.num;  // Server settling time (user-specified ramp-up).
        //lei_open: EBStates constructor, set up three times.
    rbe.stats = new EBStats(rbe,
			    60000, 50, 75000, 100, ebfArg.maxState, start,
			    rampupMillis, 1000L*mi.num, 1000L*rd.num);
    rbe.stats.waitForStart();

	//lei_01, start the timer thread
	//timeFlag = true;
	EBTimer ebTimer= null;
	ebTimer = new EBTimer(rbe, start, rampupMillis, 1000L*mi.num, 1000L*rd.num); //lei_05, -52sec. to adjust the running time.
	ebTimer.start();
	//lei_01 end

    // Start EBs...
    System.out.println("Starting " + ebCount + " EBs.");
    long startAllBy = System.currentTimeMillis() + timeToStartAll;
    try {
        rampEBs(rbe, ebs, 0, ebCount, startAllBy, rbe.stats.term);//lei_02 add rbe
    } catch(InterruptedException e) {
        System.out.println("Interrupted startup");
        return;
    }
    //lei_08 System.out.println("All of the EBs are alive!");

    if (traceSim != null) {
        // Trace simulation - manage EBs until the end.
        //lei_open
        System.out.println("traceSime is not null."); // never show
        try {
		System.out.println("lei: traceSime is not null, run() method is called."); 	//lei_open 
            traceSim.run(rbe, ebs, rbe.stats.startMI, rbe.stats.term);	//lei_02, add rbe
        } catch(InterruptedException e) {
            System.out.println("Interrupted trace simulation");
            return;
        }
    }

    //lei_open: the main thread will sleep for "w" interval and then terminate all threads.
    //rbe.stats.waitForRampDown();     //lei_01, remove it

	//lei_01 start, a while loop to test the flag from the timer thread.
    //System.out.println("lei: timeFlag = " + timeFlag);
    //while (timeFlag){
		//System.out.println("lei: timeFlag = " + timeFlag);
		//}
		/*System.out.println("lei: rbe.wait() starts.");
		synchronized(rbe){
			try{
				rbe.wait();
				}
			catch (InterruptedException e) {
				e.printStackTrace();
				}
			}
	//System.out.println("lei: timeFlag = " + timeFlag);
	System.out.println("lei: rbe.wait() finished.");*///lei_05 test purpose
	//lei_01 end.

	//lei_08 start
	if(rbe.ebFlag == false)
		{
		System.out.println("lei: rampEBs() is finished.");
		}
	else
		{
		System.out.println("lei: all EBs started, waiting for terminate signal.");
		while(rbe.ebFlag == true)
			{
			try {
				Thread.sleep(1000);
				}
			catch (InterruptedException ie) {
				System.out.println("lei: Interrupted before shutdown");
				return;
				}
			}
		System.out.println("lei: main thread is going to be finished.");
		}
	//lei_08 end

	//lei_open, if monitor is set to be true by the user.
    if (monitor){
	try {
	    Runtime r = Runtime.getRuntime();
	    r.exec("/bin/kill "+pidStr);
	}
	catch(java.lang.Exception ex){
	    System.out.println("Unable to destroy monitor process");
	}
    }
	//lei_open, set e.terminate to "true", and let running EB to suicide.
    System.out.println("Terminating EBs...");
    for (int i=0;i<ebs.size();i++) {
      EB e = ebs.elementAt(i);
      e.terminate = true;
    }

	//lei_open, for any session is already completed, continue, otherwise, call e.interrrupt.
    for (int i=0;i<ebs.size();i++) {
      EB e = ebs.elementAt(i);
      if (!e.isAlive()) continue;	//lei_open, for any session is already completed, continue, otherwise, call e.interrrupt.
	  //System.out.println("lei: Main thread: About to interrupt: " + i);	//lei_open
      if (EB.DEBUG > 0) {
	  System.out.println("Main thread: About to interrupt: " + i);
      }
      e.interrupt();
    }

	//lei_05, main thread sleeps for a certain time.
    try {
      Thread.sleep(15000L);
    }
    catch (InterruptedException ie) {
	System.out.println("Interrupted shutdown");
	return;
    }
	//System.out.println("lei: Main thread: ebs.size() = " + ebs.size());	//lei_open

	//lei_05, for-loop to let all  EBs to call join() method to finish together (wait 200millisec.)
    for(int i=0; i < ebs.size(); i++) {
		//System.out.println("lei: i = " + i);	//lei_05
	EB e = ebs.elementAt(i);
	//System.out.println("lei: EB e.name = " + e.name);	//lei_05
        try {
			//lei_05, add if condition, otherwise, return Java NullPointerException 
			// lei_05, remove if-else condition, because e.thread is a private variable in EB class.
			//if (e.thread != null)
			//	{
				//System.out.println("lei: EB " + e.name + " about to interrupt.");	//lei_open
	 		e.join(200); //lei_open: Waits at most 200 millis for this thread to die.
			//	}
			//else{
			//	System.out.println("lei: e == null.");
			//	continue;
			//	}
			//lei_05 end
        }
        catch (InterruptedException inte) {
	    System.out.println("Interrupted shutdown");
	    return;
        }
    }

	System.out.println("lei: Main thread: all EBs joined and finished.");	//lei_open
	
	//lei_open, check if any EBs are still alive.
    int aliveCount = 0;
    for(int i=0; i < ebs.size(); i++) {
        EB e = ebs.elementAt(i);
        if (e.isAlive()) {
            aliveCount++;
            if (EB.DEBUG > 0) {
                System.out.println(e.name + " alive at: " + e.getStackTrace());
            }
        }
    }
    if (aliveCount > 0) {
        System.out.println("Warning: " + aliveCount + " EBs still alive");
    }

    System.out.println("Writing results...");

    if (!oFile.set()) {
      oFile.s = System.out;
    }

    oFile.s.println("% Start time: " + startTime);
    oFile.s.println("% System under test: " + www);
    Date endTime = new Date();
    oFile.s.println("% End time: " + endTime);
    oFile.s.println("% Transaction Mix: " + ebfArg.className);
    db.print(oFile.s);
    //ofile.s.println("% Database parameters: num items=" + rbe.numItem + 
    //	    " num customers= " + rbe.numCustomer);
    //oFile.s.println("% Images On?: " + RBE.getImage + 
    //	    " Think Time Scale Factor: " + e.tt_scale); 

    rbe.stats.print(oFile.s);
    oFile.s.close();
    System.out.println("Finished RBE.");
  }

//lei_02, add one parameter "RBE rbe"
  static void rampEBs(RBE rbe, Vector<EB> ebs, int startCount, int endCount,
                      final long rampUntil, final long term)
  throws InterruptedException {
      startCount = Math.min(startCount, ebs.size());
      endCount = Math.min(endCount, ebs.size());

      long rampStart = System.currentTimeMillis();
      System.out.println("RAMP: " + startCount + " -> " + endCount + " (" +
                         (rampUntil - rampStart)/1000 + " s)");

	//lei_open: endCount is equal to ebCount, which is the number of EBs.
      boolean rampup = (startCount <= endCount);
      int lowCount = Math.min(startCount, endCount);
      int highCount = Math.max(startCount, endCount);

      List<EB> targetEBs = new ArrayList<EB>();

	  //lei_open
	  //System.out.println("lei: lowCount = " + lowCount + ", highCount = " + highCount);
	  System.out.println("lei: startCount = " + startCount + ", endCount = " + endCount);

	  //lei_open, main function calls rampEbs() with lowCount = 0. Consequently, this loop is skipped.
      for (int i = 0; i < lowCount; i++) {
          EB eb = ebs.elementAt(i);
          if (!eb.isAlive()) {
              System.out.println(eb.name + " should already be alive");
              eb.start();
          }
      }
      for (int i = lowCount; i < highCount; i++) {
          EB eb = ebs.elementAt(i);
          if (rampup) {
              if (!eb.isAlive()) {
                  targetEBs.add(eb);
		     //lei_open
		     //System.out.println("lei: targetEBs added.");			 
              } else {
                  System.out.println(eb.name + " should be dead");
              }
          } else {  // rampdown
              if (eb.isAlive()) {
                  targetEBs.add(eb);
              } else {
                  System.out.println(eb.name + " should be alive");
              }
          }
      }
	  //lei_open, highCount = ebs.size()? this loop is skipped.
      for (int i = highCount; i < ebs.size(); i++) {

		//lei_open
		//System.out.println("lei: highCount = " + highCount + " ebs.size = " + ebs.size());
		
          EB eb = ebs.elementAt(i);
          eb.terminate = true;
          if (eb.isAlive()) {
              System.out.println(eb.name + " should already be dead");
              eb.interrupt();
          }
      }

      final int BATCH = 1;
      long t0 = System.currentTimeMillis();

	  //lei_open: targetEBs.size() returns the number of EBs.
	  System.out.println("lei: targetEBs.size = " + targetEBs.size());
	//lei_open: this loop start all EBs  ***************************************************
	//RBE rbe = new RBE();	//lei_02
	double lambda = 60; //lei_02, arrival rate in the unit of arrival per second (not millisec.). 

	  //lei_open, for loop to start all EBs, two if conditions inside the loop.
      for (int i = 0; i < targetEBs.size(); i++) {
          EB eb = targetEBs.get(i);
          if (rampup) {

		//lei_05, add if condition to check ebFlag
		//System.out.println("lei: ebFlag = " + rbe.ebFlag);
		if(rbe.ebFlag == true){
			 //lei_02 start****
			 //add a sleep time which is exponentially distributed between two consecutive arrivals
			 //long temp = System.currentTimeMillis();
			 try{
			 long arrivalInterval = rbe.arrivalRate(eb, lambda);
			 //lei_09 System.out.println("lei: EB " + eb.name + " may start after " + arrivalInterval + " ms.");
			 Thread.sleep(arrivalInterval);
			 }catch(InterruptedException e){
			 	}
			//lei_02 end*****

			//lei_09 start***
			if (userCounter < maxUser)
				{
				if(queueCounter > 0)
					{
					queueCounter++;
					//System.out.println("lei: join the queue, queueCounter = " + queueCounter);
					i--;
					}
				else if(queueCounter == 0)
					{
					eb.start();
					userCounter++;
					//System.out.println("lei: new eb started, userCounter = " + userCounter);
					}
				else
					{
					System.out.println("lei: error, queueCounter < 1.");
					}
				}
			else{
				queueCounter++;
				//System.out.println("lei: maxUser limit, userCounter = " + userCounter + ", queueCounter = " + queueCounter);
				i--;
				}
			//lei_09 end***
			  	
		          //lei_09 remove eb.start();	//lei_open, start EBs.
		          
		          //lei_02 start
			//temp = System.currentTimeMillis() - temp;
			//System.out.println("lei: EB " + eb.name + " has started after " + temp + " ms.");
			//lei_02 end

			}
		else{
			System.out.println("lei: ebFlag = " + rbe.ebFlag + ", return rampEBs().");
			return;	//lei_05, if ebFlag == false, return the function
			}
              
          } else {
              eb.terminate = true;
          }

          int remaining = targetEBs.size() - (i+1);
		  
		//lei_open, what's the purpose of this if condition?	  
          if ((i+1) % BATCH == 0 || remaining == 0) {
              long now = System.currentTimeMillis();
              long sleep;
              if (remaining == 0) {
                  sleep = rampUntil - now;
              } else {
                  long unit = (rampUntil - t0) / (remaining + BATCH);
                  sleep = t0 + BATCH * unit - now;
              }

			  //lei_open, sleep = -1 ?
			  //System.out.println("lei: rampEBs: sleep=" + sleep + "ms");
			  
              if (EB.DEBUG > 3) {
                  System.out.println("rampEBs: sleep=" + sleep + "ms");
              }
              if (now >= term || now + sleep >= term) {
			  System.out.println("lei: in loop - now = " + now+ " sleep = " + sleep + " term = " + term);	//lei_open
                 // return; //lei_04, to avoid the time limit of ramp-up.
              }
              if (sleep > 0) {
                  Thread.sleep(sleep);
              }
              t0 = System.currentTimeMillis();
          }
		  
      }
	  //lei_open, end of the loop, all EBs started *******************************************

	  //lei_open 
	  System.out.println("lei: for loop in rampEBs ended.");
	  
      long now = System.currentTimeMillis();
      long sleep = rampUntil - now;
      if (EB.DEBUG > 3) {
          System.out.println("rampEBs: sleep=" + sleep + "ms");
      }
      if (now >= term || now + sleep >= term) {
	  	System.out.println("lei: out loop - now = " + now+ " sleep = " + sleep + " term = " + term);	//lei_open
          return;
      }
      if (sleep > 0) {
	  	//lei_open
	  	System.out.println("lei: Thread sleeps " + sleep + " millisec. in rampEBs.");
		
          Thread.sleep(sleep);
      }

      long rampEnd = System.currentTimeMillis();
      System.out.println("RAMP done in " + (rampEnd - rampStart)/1000 + " s");
  }


  //lei_02, function arrivalRate to calculate 
  public long arrivalRate(EB eb, double lambda) {
  	//System.out.println("lei: lambda = " + lambda + " /sec.");
  	double mu = 1000.0/lambda;		// expected number, or mean is in millisec.
	//System.out.println("lei: mu = " + mu + " millisec.");
  	 long r = negExp(rand, 7000L, 0.36788, (long)(mu), 4.54e-5, mu);	//lei_02, 70sec.? this value is to be adjusted.
          //System.out.println("lei: r = " + r + " millisec.");
          //System.out.println("lei: tt_scale = " + eb.tt_scale);

	   //r = (long) (eb.tt_scale*r); //we do not use tt_scale
          

         //System.out.println("lei: scaled r = " + r + " millisec.");
          
          if (EB.DEBUG > 1) {
              System.out.println("Think time of interarrivals is " + r + "ms");
          }
          
          return(r);
	  }
  //lei_02, function end.


  public static void getKey() {
	 System.out.println("Type RETURN to continue...");
	 try {
		bin.readLine();
	 }
	 catch (java.io.IOException e) {
		e.printStackTrace();		
	 }
  }

  public static void Arguments(String [] args) {
    int a;

    for (a=0;a<args.length;a++) {
      System.out.println("#" + Pad.l(3,""+(a+1)) + "  " + args[a]);
    }
  }

  public static void Usage(String [] args, ArgDB db) {
    System.out.println("Input command-line arguments");
    Arguments(args);

    System.out.println("\nOptions");
    db.print(System.out);
  }

  // Static random functions.

  // Defined in TPC-W Spec Clause 2.1.13
  public static final int NURand(Random rand, int A, int x, int y)
  {
    return((( (nextInt(rand, A+1)) | (nextInt(rand, y-x+1)+x)) % (y-x+1)) +x);
  }

  public final long slow(long t) { //System.out.println("lei: slowDown = " + slowDown); //lei_02
  							return((long) (slowDown*t+0.5)); }
  public final long speed(long t) { return((long) (speedUp*t+0.5)); }

  // Negative exponential distribution used by 
  //  TPC-W spec for Think Time (Clause 5.3.2.1) and USMD (Clause 6.1.9.2)
  public final long negExp(Random rand, long min, double lMin,
			   long max, double lMax,
			   double mu)
  {
    double r = rand.nextDouble();

	//lei_02
	//System.out.println("lei: get randome r =" + r);
    
    if (r < lMax) {
		//lei_open
		//lei_02, sometimes we have r < IMax, so how to set the value of max is a question.
		//System.out.println("lei: r is less than IMax, and r = " + r + ", and slow(max) = " + slow(max)); 
		return(slow(max));
    }

	//lei_02
	//long test1 = -(long)(mu * Math.log(r));
	//System.out.println("lei: test1 = " + test1);
	//long test2 = slow((long) (-mu*Math.log(r)));
	//System.out.println("lei: test2 = " + test2);

	 return(slow((long) (-mu*Math.log(r))));
  }

  // Defined in TPC-W Spec Clause 4.6.2.8
  private static final String [] digS = {
    "BA","OG","AL","RI","RE","SE","AT","UL","IN","NG"
  };
  
  public static String digSyl(int d, int n)
  {
    String s = "";

    if (n==0) return(digSyl(d));

    for (;n>0;n--) {
      int c = d % 10;
      s = digS[c]+s;
      d = d /10;
    }

    return(s);
  }

  public static String digSyl(int d)
  {
    String s = "";

    for (;d!=0;d=d/10) {
      int c = d % 10;
      s = digS[c]+s;      
    }

    return(s);
  }

  // Gets the username and password fields according to TPC-W Spec. 4.6.2.9 ff.
  public static String unameAndPass(int cid)
  {
    String un = digSyl(cid);
    return(field_uname + "=" + un +
	   "&" + field_passwd + "=" + un.toLowerCase());
  }

  // Subject list.  See TPC-W Spec. 4.6.2.11
  public static final String [] subjects = {
    "ARTS",
    "BIOGRAPHIES",
    "BUSINESS",
    "CHILDREN",
    "COMPUTERS",
    "COOKING",
    "HEALTH",
    "HISTORY",
    "HOME",
    "HUMOR",
    "LITERATURE",
    "MYSTERY",
    "NON-FICTION",
    "PARENTING",
    "POLITICS",
    "REFERENCE",
    "RELIGION",
    "ROMANCE",
    "SELF-HELP",
    "SCIENCE-NATURE",
    "SCIENCE-FICTION",
    "SPORTS",
    "YOUTH",
    "TRAVEL"
  };

  // Select a subject string randomly and uniformly from above list.
  //   See TPC-W Spec. 2.10.5.1
  public static String unifSubject(Random rand) {
    return(subjects[nextInt(rand, subjects.length)]);
  }

  // Select a subject string randomly and uniformly from above list.
  //   See TPC-W Spec. 2.10.5.1
  // NOTE:  The "YOUTH" and "TRAVEL" subjects are missing from the home page.
  //   I believe this to be an error, but cannot be sure.
  // Change this function if this is determined to not be an error.
  public static String unifHomeSubject(Random rand) 
    { return(unifSubject(rand)); }

  // Adds a field to a HTTP request.
  //  field name = f, value = v.
  public static String addField(String i, String f, String v)
  {
    if (i.indexOf((int) '?')==-1) {
      // First field
      i = i + '?';
    }
    else {
      // Another additional field.
      i = i + '&';
    }
    i = i + f + "=" + v;

    return(i);
  }

  public static String addSession(String i, String f, String v){
    StringTokenizer tok = new StringTokenizer(i, "?");
    String return_val = null;
    try {
      return_val = tok.nextToken();
      return_val = return_val + f + v;
      return_val = return_val + "?" + tok.nextToken();
    }
    catch (NoSuchElementException e) { 
    }

    return(return_val);
  }

  public static final String [] nchars = { 
	 "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
  };

  public static final String [] achars = { 
	 "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
	 "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
	 "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", 
	 "u", "v", "w", "x", "y", "z", 
	 "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
	 "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", 
	 "U", "V", "W", "X", "Y", "Z", 

	 "%21",  // "!"
	 "%40",  // "@"
	 "%23",  // "#"
	 "%24",  // "$"
	 "%25",  // "%"
	 "%5E",  // "^"
	 "%26",  // "&"
	 "*",    // "*"
	 "%28",  // "("
	 "%29",  // ")"
	 //	 "_",    // "_"
	 "%5F", 
	 "-",    // "-"
	 "%3D",  // "="
	 "%2B",  // "+"
	 "%7B",  // "{"
	 "%7D",  // "}"
	 "%5B",  // "["
	 "%5D",  // "]"
	 "%7C",  // "|"
	 "%3A",  // ":"
	 "%3B",  // ";"
	 "%2C",  // ","
	 ".",    // "."
	 "%3F",  // "?"
	 "%2F",  // "/"
	 "%7E",  // "~"
	 "+"     // " "
  };

  public static String astring(Random rand, int min, int max)
  {
	 return(rstring(rand, min,max,achars));
  }

  public static String nstring(Random rand, int min, int max)
  {
	 return(rstring(rand, min,max,nchars));
  }

  private static String rstring(Random rand, int min, int max, String [] cset)
  {
    int l = cset.length;
    int r = nextInt(rand, max-min+1)+min;
    String s;

    for (s="";s.length()<r;s = s + cset[nextInt(rand, l)]);

    return(s);
  }

  public static final String [] countries = {
	 "United+States",                    "United+Kingdom",
	 "Canada",                           "Germany",
	 "France",                           "Japan",
	 "Netherlands",                      "Italy",
	 "Switzerland",                      "Australia",
	 "Algeria",                          "Argentina",
	 "Armenia",                          "Austria",
	 "Azerbaijan",                       "Bahamas",
	 "Bahrain",                          "Bangla+Desh",
	 "Barbados",                         "Belarus",
	 "Belgium",                          "Bermuda",
	 "Bolivia",                          "Botswana",
	 "Brazil",                           "Bulgaria",
	 "Cayman+Islands",                   "Chad",
	 "Chile",                            "China",
	 "Christmas+Island",                 "Colombia",
	 "Croatia",                          "Cuba",
	 "Cyprus",                           "Czech+Republic",
	 "Denmark",                          "Dominican+Republic",
	 "Eastern+Caribbean",                "Ecuador",
	 "Egypt",                            "El+Salvador",
	 "Estonia",                          "Ethiopia",
	 "Falkland+Island",                  "Faroe+Island",
	 "Fiji",                             "Finland",
	 "Gabon",                            "Gibraltar",
	 "Greece",                           "Guam",
	 "Hong+Kong",                        "Hungary",
	 "Iceland",                          "India",
	 "Indonesia",                        "Iran",
	 "Iraq",                             "Ireland",
	 "Israel",                           "Jamaica",
	 "Jordan",                           "Kazakhstan",
	 "Kuwait",                           "Lebanon",
	 "Luxembourg",                       "Malaysia",
	 "Mexico",                           "Mauritius",
	 "New+Zealand",                      "Norway",
	 "Pakistan",                         "Philippines",
	 "Poland",                           "Portugal",
	 "Romania",                          "Russia",
	 "Saudi+Arabia",                     "Singapore",
	 "Slovakia",                         "South+Africa",
	 "South+Korea",                      "Spain",
	 "Sudan",                            "Sweden",
	 "Taiwan",                           "Thailand",
	 "Trinidad",                         "Turkey",
	 "Venezuela",                        "Zambia",
  };

  public static int unifCountry(Random rand) {
	 return nextInt(rand, countries.length) + 1;
  }

  public static final Calendar c = new GregorianCalendar(1880,1,1);
  public static final long dobStart = c.getTime().getTime();;
  public static final long dobEnd =  System.currentTimeMillis();;

    /*static {
	 Calendar c = new GregorianCalendar(1880,1,1);
	 dobStart = c.getTime().getTime();

	 // FIXME:  This has some drawbacks.  Assums the day does not change
	 //  during benchmark execution.  Also, does not give the current
	 //  date the same chance as all the other dates.
	 //  In the end, this simplification will not affect anything.
	 dobEnd   = System.currentTimeMillis();
	 
	 }*/

  public static String unifDOB(Random rand) {
	 long t = ((long) (rand.nextDouble()*(dobEnd-dobStart)))+dobStart;
	 Date d = new Date(t);
	 Calendar c = new GregorianCalendar();
	 c.setTime(d);

	 return("" + c.get(Calendar.MONTH) + "%2f" + 
			  c.get(Calendar.DAY_OF_MONTH) + "%2f" + c.get(Calendar.YEAR));
  }

  public static final String [] ccTypes = {
	 "VISA", "MASTERCARD", "DISCOVER", "DINERS", "AMEX"
  };

  public static int unifCCType(Random rand) {
	 return nextInt(rand, ccTypes.length) + 1;
  }

  public static String unifExpDate(Random rand) {
	 Date d = new Date(System.currentTimeMillis() + 
							 ((long) (nextInt(rand, 730))+1)*24L*60L*60L*1000L);
	 Calendar c = new GregorianCalendar();
	 c.setTime(d);

	 return("" + c.get(Calendar.MONTH) + "%2f" + 
			  c.get(Calendar.DAY_OF_MONTH) + "%2f" + c.get(Calendar.YEAR));

  }

  public static int unifDollars(Random rand) {
	 return (nextInt(rand, 9999)+1);
  }
  public static int unifCents(Random rand) {
	 return (nextInt(rand, 100));
  }

  public static final String [] shipTypes = {
	 "AIR",
	 "UPS",
	 "FEDEX",
	 "SHIP",
	 "COURIER",
	 "MAIL"
  };

  public static int unifShipping(Random rand) {
	 return nextInt(rand, shipTypes.length) + 1;
  }

  // FIXME:  These need to be implemented with specifics from
  //  the data-base/ web-server people.
  public static String unifImage(Random rand) {
	 int i = nextInt(rand, numItem)+1;
	 //int grp = i % 100;
	 //return(mungeURL("img" + grp + "/image_" + i + ".gif"));
	 return(mungeURL("images/items/item" + i + ".jpg"));
  }

  public static String unifThumbnail(Random rand) {
	 int i = nextInt(rand, numItem)+1;
	 //int grp = i % 100;
	 //return(mungeURL("img" + grp + "/thumb_" + i + ".gif"));
	 return(mungeURL("images/items/thumb" + i + ".jpg"));
  }

  public static int unifStock(Random rand) {
	 return nextInt(rand, 21) + 10;
  }

  // Needed, because Java 1.1 did not have Random.nextInt(int range)
  public static int nextInt(Random rand, int range) {
    int i = Math.abs(rand.nextInt());
    return (i % (range));
  }

  // Finds any non-URLable characters and converts them to URL form.
  //  All alpha numerics are left alone.
  //  <space> goes to '+'
  //  All others go to %xx (hexadecimal representation.)
  public static String mungeURL(String url) {
	 int i;
	 String mURL = "";

	 // System.out.println("Munging: " + url);
	 for (i=0;i<url.length();i++) {
		char ch = url.charAt(i);
		if ( ((ch >= '0') && (ch <='9')) ||
			  ((ch >= 'a') && (ch <='z')) ||
			  ((ch >= 'A') && (ch <='Z')) || 
		     ((ch == '.') || (ch =='/')) ) {
		  mURL = mURL + ch;
		}
		else if ( ch == ' ') {
		  mURL = mURL + '+';
		}
		else {
		  int d = ch;
		  int d1 = d>>4;
		  int d2 = d & 0xf;
		  char c1 = (char) ( (d1 > 9) ? ( 'A'+d1-10 ) : '0'+d1);
		  char c2 = (char) ( (d2 > 9) ? ( 'A'+d2-10 ) : '0'+d2);
		  mURL = mURL + "%" + c1 + c2;
		}
		// System.out.println("munging: " + mURL);
	 }
	 
	 return(mURL);
  }
}
