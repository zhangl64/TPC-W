/*-------------------------------------------------------------------------
 * rbe.EB.java
 * Timothy Heil
 * 10/5/99
 *
 * ECE902 Fall '99
 *
 * TPC-W emulated browser.
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

import java.net.*;
import java.io.*;

import java.util.Random;
import java.util.Vector;

import rbe.util.Debug;
import rbe.util.StringPattern;
import rbe.util.StrStrPattern;
import rbe.util.CharStrPattern;
import rbe.util.CharSetStrPattern;

public class EB implements Runnable {

    private Thread thread;

  // Terminate this EB.
  public volatile boolean terminate = false;

  int          [/*from*/][/*to*/] transProb;      // Transition probabilities.
  EBTransition [/*from*/][/*to*/] trans;          // EB transitions.
  EBTransition curTrans;
  int curState;                                   // Current state.
  String nextReq;            // Next HTTP request.
  public String cachedProdDetHTML;  // See EBWProdCURLTrans.
  int maxTrans;
  String name;
  int    cid;                // CUSTOMER_ID.  See TPC-W Spec.
  String sessionID;          // SESSION_ID.  See TPC-W Spec. 
  int    shopID;             // Shopping ID.  
  String fname = null;       // Customer first name.
  String lname = null;       // Customer last name.
  public RBE rbe;
  long usmd;
  boolean toHome;
  boolean stagger = true;

  // Wait for key-stroke between transisions.
  //   Does not do think-times.
  public boolean waitKey = true;
  
  public Random rand = new Random();

  // Think time-scaling.
  public double tt_scale = 1.0;

  // Set this higher to see more messages. 
  public static int DEBUG =0;

  public static final int NO_TRANS = 0;
  public static final int MIN_PROB = 1;
  public static final int MAX_PROB = 9999;
  public static final int ID_UNKNOWN = -1;

  private final StrStrPattern imgPat    = new StrStrPattern("<IMG");
  private final StrStrPattern inputPat  = 
      new StrStrPattern("<INPUT TYPE=\"IMAGE\"");
  private final StrStrPattern srcPat    = new StrStrPattern("SRC=\"");
  private final CharStrPattern quotePat = new CharStrPattern('\"');

  //lei_08 define a boolean flag
  public volatile boolean requestFlag = false;
  //public boolean nextFlag = false;

  public EB(RBE rbe,
	    int [][] prob, // Transition probabilities.  
	                   //   See TPC-W Spec. Section 5.2.2.
	    EBTransition [][] trans, // Actual transitions.
	    int max,     // Number of transitions. -1 implies continuous
	    String name  // String name.
	    )
  {
    int i,j;
    int s;
    int prev;

    // Make sure prob and trans are well-formed.
    s = prob.length;
    Debug.Assert(s>0, "No states in prob.");
    Debug.Assert(trans.length == s, "Number of states in prob (" + s + 
		 ") does not equal number of states in trans (" + 
		 trans.length + ")");

    for (j=0;j<s;j++) {
      Debug.Assert(trans[j].length==s, "Transition matrix is not square.");
      Debug.Assert(prob[j].length==s, "Transition matrix is not square.");

      prev = 0;
      for (i=0;i<s;i++) {
	if (prob[j][i]==NO_TRANS) {
	  Debug.Assert(trans[j][i] == null, "Transition method specified " + 
		       "for impossible transition." + i + ", " + j + " " + trans[j][i]);
	}
	else {
	  Debug.Assert(prob[j][i] <= MAX_PROB, 
		       "Transition probability for prob[" + 
		       j + "][" + i + "] (" + prob[j][i] + 
		       ") is larger than " + MAX_PROB);
	  Debug.Assert(prob[j][i] >= MIN_PROB, 
		       "Transition probability for prob[" + 
		       j + "][" + i + "] (" + prob[j][i] + 
		       ") is less than " + MIN_PROB);
	  Debug.Assert(trans[j][i]!=null, 
		       "No transition method for possible transition [" + 
		       j +"][" +i + "]");

	  Debug.Assert(prob[j][i] > prev, 
		       "Transition [" + j + "][" + i + "] has probability (" + 
		       prob[j][i] + " not greater than previous " + 
		       "probability (" + prev + ")");
	  prev = prob[j][i];
	}
      }
      Debug.Assert(prev==MAX_PROB, "Final probability for state [" + j + 
		   "] ( " + prev + ") is not " + MAX_PROB);
    }

    this.rbe       = rbe;
    this.transProb = prob;
    this.trans     = trans;
    this.name      = name;

    maxTrans = max;    
  }

  public final int states() 
  {
    return(transProb.length);
  }

  public void initialize() {
    curState = 0;
    nextReq = null;
    nextState(null);
    cachedProdDetHTML = null;
    cid    = ID_UNKNOWN;
    sessionID = null;
    shopID = ID_UNKNOWN;
    usmd = System.currentTimeMillis() + usmd();
    if (DEBUG>4) {
      log("usmd " + usmd);
    }
    fname = null;
    lname = null;
	//lei_03
	//log("initialize() - lei");
  }

    public void start() {
        if (isAlive()) {
            throw new IllegalStateException("EB already started");
        }
        terminate = false;
        stagger = true;
        thread = new Thread(this, name);
        thread.setDaemon(true);
        thread.start();

	  //lei_open
	  System.out.println("lei: EB " + name + " - Thread has been started.");
    }

    public boolean isAlive() {
        return thread != null && thread.isAlive();
    }

    public void interrupt() {
        thread.interrupt();
    }

	//lei_05, join method in EB class has "throw InterruptedException",
	//shall we add it to isAlive and interrupt methods as well?
	//especially interrupt() method, because isAlive() method already has null check.
    public void join(long millis) throws InterruptedException {
		//lei_05 add if-else condition 
		if (thread != null)
			{
			thread.join(millis);
			}
		else
			return;
		//lei_05 end
    }

    public String getStackTrace() {
        return java.util.Arrays.deepToString(thread.getStackTrace());
    }

  public void run() {
    long wirt_t1;  // WIRT.T1 in TPC_W.Spec.
    long wirt_t2;  // Same as TT.T1 in TPC-W Spec.
    long tt=0L;    // Think Time.

    initialize();

    //lei_open
    //System.out.println("lei: EB " + name + " - Thread is running at " + System.currentTimeMillis());

    try {
	if (waitKey) {
	    RBE.getKey();
	} else {

	      //lei_open, the first think time before even sending a request...but why?
		//long temp = System.currentTimeMillis();
		//System.out.println("lei: sleep starts at: " + temp);
		
	    //lei_01, no stagger at the beginning
	    //Thread.sleep(thinkTime());  // First thinkTime will stagger.
	    thinkTime();
	    Thread.sleep(0); //lei_01

		//lei_open
		//System.out.println("lei: sleep ends at: " + System.currentTimeMillis());
		//temp = System.currentTimeMillis() - temp;
		//System.out.println("lei: EB " + name + " sleeps for: " + temp + " mills as stagger.");
	}
    } catch (InterruptedException inte) {
	// Will exit if terminate is true.
    }

	//lei_open, while-loop until the end of this run() method.
	//maxTrans = 1,000,000 for all TPCW-1,2,3. 
	//The value of maxTrans minus one at the end of each loop, if and only if maxTrans>0.
	//Total 6 if-conditions in the while-loop body plus one block of the TT generator.
	//System.out.println("lei: maxTrans = " + maxTrans);	//lei_open
    while ((maxTrans == -1) || (maxTrans>0)) {	//lei_06, for testing purpose, change maxTrans>0 to maxTran>999,999

	//lei_open, 1st if-condition
	//an EB will be terminated by the main thread if the running time is over.
	if (terminate) { 
		//lei_open
		System.out.println("lei: EB " + name + " committing suicide at the beginning.");
		
	    if (DEBUG > 0) {
		log("Committing suicide");
	    }
	    return;
	}

	wirt_t1 = System.currentTimeMillis();

	// Check if user session is finished.
	//lei_open, 2nd if-condition
	//a message "...EB #...: Completed..." will show up when an EB is completed by itself.
	//the condition is only satisfied when both (1) AND (2) happened
	//(1) this EB has run more than an expected time interval (with min 15min or max 60min)
	//(2) the next HTTP request is "home'.
	if ((wirt_t1 >= usmd) && (toHome)) {
	    // Start new user session.
	    log("Completed user session");

		//lei_03
		//log("returns - lei");
		return;
	   // initialize(); //lei_03, remove 
	    //continue; //lei_03, remove 
	}

	//lei_open, 3rd if-condition
	//print a message "Restarting user session..." if the next request is null.
	if (nextReq.equals("")) {
	    rbe.stats.error(
                name, "Restarting user session (empty nextReq)", null);
	    // Start new user session.
	    log("Completed user session");
	    initialize();
	    continue;
	}

	// 2) Send HTTP request.
	// This is purely for testing purposes.
	//lei_open, 4th if-condition
	//the last debug message "lei: send http..." doesn't show up at all.
	if (nextReq.startsWith("file:")) {
		//lei_open
		System.out.println("lei: send http testing.");
		
	    int q = nextReq.indexOf('?');
	    if (q==-1) {
		q = nextReq.length();
	    }
	    nextReq = nextReq.substring(0,q);
	    // log("nextReq trimmed : " + nextReq);
	}
	
	URL httpReq;
	try {
	    httpReq = new URL(nextReq);
	}
	catch (MalformedURLException murl) {
	    murl.printStackTrace();
	    return;
	}
	
	// 3) Receive HTML response page.
	if (DEBUG > 0) {
	    log("Making request");
	}

	//lei_06 start, add if-else condition and "leitest"
	//String html;
	//if(maxTrans > 999999)
	//	html = "";
	//else
	//	html = getHTML(httpReq);
	
	//lei_06, return html page source.
	//System.out.println("lei: EB " + name + " send HTML reqest at " + System.currentTimeMillis());				
	String html = getHTML(httpReq);	//lei_open, getHTML() function, is the function to make request.

	//lei_open, 5th if-condition
	//check if terminate==true, return.
	if (terminate) {
		//lei_open
		System.out.println("lei: EB " + name + " committing suicide at the end.");
	    if (DEBUG > 0) {
		log("Committing suicide");
	    }
	    return;
	}
	// 4) Measure absolute response time, TT.T1 = WIRT.T2.  This is a
	// time stamp just following the reception of the last byte of the
	// HTML response page, which was provided by the SUT.
	wirt_t2 = System.currentTimeMillis();
	//lei_open, 6th if-condition
	//if the html that geted is null, restart session; 
	//otherwise, compute the absolute response time. Then, call nextState() function
	//to pick the next navigation option.
	if (html == null) {
            //rbe.stats.error(name, "Restarting user session due to error",
            //                httpReq.toString());
	    // Start new user session.
	    log("Restarting user session");		//lei_open, got this a lot when lambda is large, e.g., 200.
	    initialize();
	} else {
		//lei_open
		//log("lei: Loaded page in " + (wirt_t2 - wirt_t1) + "ms");	
            // 5) Compute and store Web Interaction Response Time (WIRT) 
            if (DEBUG > 1) {
                log("Loaded page in " + (wirt_t2 - wirt_t1) + "ms");
            }
            rbe.stats.interaction(curState, wirt_t1, wirt_t2, tt);	//lei_open, compute and store response time?
	
            if (DEBUG > 2) {
                log("Post process: " + curTrans.getClass().getSimpleName());
            }
            curTrans.postProcess(this, html);	//lei_open, 1st time to use html from getHTML(), post process?

            // 6) Pick the next navigation option.
            // 7) Compose HTTP request.
            nextState(html);		//lei_open, 2nd time ot use html from getHTML().
        }//lei_open: end of else.

	// 8) Pick think time (TT), and compute absolute request time
	// 9) Wait for absolute request time.
	//lei_open, the last block are the think time generator plus the maxTrans self operation.
	try {
	    if (waitKey) {
		RBE.getKey();
	    } else {
		long absReqTime = wirt_t2 + thinkTime();
		long remaining = absReqTime - System.currentTimeMillis(); 
		if (remaining > 0) {
			//lei_open	
			//System.out.println("lei: EB " + name + " TT remaining = " + remaining);
			
		    //Thread.sleep(remaining);	//lei_01, remove this for open queueing networks.
		    Thread.sleep(0);	//lei_01		
		}
	    }
	} catch (InterruptedException inte) {
	    if (DEBUG > 0) {
			//lei_open
			//System.out.println("lei: committing suicide because of inte.");
		log("Committing suicide");
	    }
	    return;
	}
	tt = System.currentTimeMillis() - wirt_t2;

	//lei_08, waiting for the signal from the master thread here
	//System.out.println("lei: EB " + name + " will wait.");
	//nextFlag = true;
	//lei_09 remove requestFlag = true;
	//lei_09 remove while(requestFlag){}
	//lei_09 start
	synchronized(this)
		{
		try{
			requestFlag = true;
			wait();
			//System.out.println("lei: EB " + name + " get notify() at " + System.currentTimeMillis());
			} catch(InterruptedException e) {
			System.out.println("lei: wait() interrupted.");
				}
		}
	requestFlag = false;
	//lei_09 end

	//nextFlag = false;
	//requestFlag = false;
	//System.out.println("lei: EB " + name + " will send the next reqeust, and requestFlag = " + requestFlag);
	//lei_08 end
	
	if (maxTrans > 0) maxTrans--;		//lei_open, if maxTrans>0, maxTrans --; otherwise, maxTrans = maxTrans.
      }
  }

    private void readStreamInto(InputStream is, StringBuilder buf)
        throws IOException
    {
        byte [] buffer = new byte[4096];
        BufferedInputStream in = new BufferedInputStream(is, 4096);
        try {
            int r;
            while ((r = in.read(buffer, 0, buffer.length)) != -1) {
                if (r > 0) {
                    buf.append(new String(buffer, 0, r));
                }
            }
        } finally {
            in.close();
        }
    }

    String getHTML(URL url) {
        StringBuilder html = new StringBuilder();
        // log("Begin reading HTML. " + name);
        //lei_open
        //log("lei: begin reading HTML. " + name);
        boolean retry = false;
        int retries = 0;
        Vector<ImageReader> imageRd = new Vector<ImageReader>(0);

	//lei_06, while-loop start here********************
        do {
            if (terminate) return null;	//lei_open, first time return null
            if (retry) {
                try {
                    if (waitKey) {
                        RBE.getKey();
                    } else {
                    //lei_open
                    System.out.println("lei: getHTML() sleep for a think time.");
					
                        Thread.sleep(thinkTime());
                    }
                } catch (InterruptedException inte) {
                    return null;		//lei_open, second time return null
                }	  
            }
            retry = false;

		//lei_open the rest of the do-while loop is a try-catch block,
		//open a connection and get stream into html.
            html.setLength(0);
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(120000); //lei may change the default timeout from 15000 (15sec.) to 60000
                conn.setReadTimeout(60000); //lei may change the default timeout from 15000 (15sec.) to 60000
                readStreamInto(conn.getInputStream(), html);
            } catch (SocketTimeoutException ste) {
                if (terminate) return null;		//lei_open, third time return null
                rbe.stats.error(name, ste.getMessage() + " - lei", url.toExternalForm()); //lei add some message here
                rbe.stats.serverError(System.currentTimeMillis());
                retry = true;
            } catch (IOException ioe) {
                if (terminate) return null;		//lei_open, fourth time return null
                rbe.stats.error(name, ioe.getMessage(), url.toExternalForm());
                if (conn != null) {
                    try {
                        InputStream es = conn.getErrorStream();
                        if (es != null) {
                            html.append("Error page:\n");
                            readStreamInto(es, html);
                        }
                        int status = conn.getResponseCode();
                        if (status >= 500) {
                            rbe.stats.serverError(System.currentTimeMillis());
                        }
                    } catch (IOException ioe2) {
                        if (terminate) return null;		//lei_open, fifth time return null
                        log("Error handling IO error: " + ioe2.getMessage());	//lei_open, have this error when lambda=350, "Too many open files".
                    }
                }
                if (DEBUG > 1 && html.length() > 0) {
                    log("Server output: " + html.toString());
                }
                retry = true;
            }
        } while (retry && --retries >= 0);
   	//lei_06, while-loop ended here**********************
   	
        if (retries < 0) {
            return null;	//lei_open, sixth time return null
        }

        String htmlStr = html.toString();
		//lei_open, this will print the whole source of the html page.
		//log("lei: htmlStr : " + htmlStr);
        if (DEBUG > 9) {
            log(htmlStr);
        }

        // Suppress image requests.
        if (!RBE.getImage){
			//lei_06
			//log("lei: getImage = " + RBE.getImage);
			return htmlStr;
        	}

        // Scan for image requests, and request those.
        findImg(htmlStr, url, imgPat, srcPat, quotePat, imageRd);
        findImg(htmlStr, url, inputPat, srcPat, quotePat, imageRd);

        long imgT0 = 0;
        if (DEBUG>2) {
            log("Found " + imageRd.size() + " images");
            imgT0 = System.currentTimeMillis();
        }
        int totImgSize = 0;
        while (imageRd.size()>0) {
            int max = imageRd.size();
            int min = Math.max(max - rbe.maxImageRd,0);
            try {
                for (int i=min; i<max; i++) {
                    ImageReader rd = imageRd.elementAt(i);
                    //	log("Reading image: " + rd.imgURLStr);
                    if (!rd.readImage()) {
                        if (DEBUG>3) {
                            log("[" + System.currentTimeMillis() + "] Read " +
                                rd.tot + " bytes from " + rd.imgURLStr);
                        }
                        totImgSize += rd.tot;
                        imageRd.removeElementAt(i);
                        i--;
                        max--;
                    }
                }
            }
            catch (InterruptedException inte) {
                return null;		//lei_open, last time (seventh) return null
            }
        }
        if (DEBUG>2) {
            long imgT1 = System.currentTimeMillis();
            log("Fetched all images (" + totImgSize + " B total) in " +
                (imgT1 - imgT0) + "ms");
        }

        return htmlStr;
    }
    
  private void findImg(String html, URL url, StringPattern imgPat, 
		    StringPattern srcPat, StringPattern quotePat,
		    Vector<ImageReader> imageRd)
  {
    int cur = 0;
    byte [] buffer = new byte[4096];
    
    while ((cur = imgPat.find(html, cur)) > -1) {
      cur = srcPat.find(html, imgPat.end()+1);
      quotePat.find(html, srcPat.end()+1);

      String imageURLString = html.substring(srcPat.end()+1, quotePat.start());
      
      if (DEBUG>3) {
	log("Found image " + imageURLString);
      }
      
      imageRd.addElement(new ImageReader(
          rbe, name, url, imageURLString, buffer));
      cur = quotePat.start()+1;
    }
  }


  long thinkTime()
  {
      if (stagger) {
          long r = RBE.nextInt(rand, 20000)+100; //lei_open, return a positive integer from 100 to 20099..
          stagger = false;

		  //lei_open, stagger is random r * tt_scale, where "tt_scale" is the input value of TT.
		  //System.out.println("lei: tt staggering " + r + " sec.");
		  
          if (DEBUG > 1) {
              log("Think time staggering to " + r + "ms");
          }
          return((long) (r*tt_scale));
      }
      else {
          long r = rbe.negExp(rand, 7000L, 0.36788, 70000L, 4.54e-5, 7000.0); //lei_open, 
          
          r = (long) (tt_scale*r);

	  //lei r = 50; //for testing
	   //lei_open, this is the real think time, return r.
         //System.out.println("lei: tt = " + r + " sec.");
          
          if (DEBUG > 1) {
              //r = 5000; // For testing...
              log("Think time of " + r + "ms");
          }
          
          return(r);
      }
  }

  long usmd()
  {
    return(rbe.negExp(rand, 0L, 1.0, 3600000L /*60 minutes*/, 0.0183156, 
		      900000.0 /* 15 minutes */));
  }

// lei_open, nextState() first called by initialize(), then by run().
  void nextState(String html)
  {
    int random = nextInt(MAX_PROB-MIN_PROB + 1) + MIN_PROB; //lei_06, random = 1...9999

    if (DEBUG>4) {		//lei_06, change it from 4 to -1 to print debug messages.
	StringBuilder sb = new StringBuilder("TransProb ");
	for (int j=0; j<transProb[curState].length; j++) {
	    sb.append(j);
	    sb.append(":");
	    sb.append(transProb[curState][j]);
	    sb.append(" ");
	}
	sb.append("; Random=");
	sb.append(random);
	log(sb.toString());
    }

    for (int j=0; j<transProb[curState].length; j++) {
		//lei_06, if-condition starts here***************
		//find the first state which has the probability larger than (or equal to) random, then go from curState to j.
      if (transProb[curState][j] >= random) {
	rbe.stats.transition(curState,j);
	curTrans = trans[curState][j];
	nextReq = curTrans.request(this, html);	//lei_06, nextReq is the url, e.g., String = "http://127.0.0.1/tpcw/home?c_id=65207. 
	if (nextReq == null) {
	    Debug.fail("FATAL: nextReq == null");
	}
	toHome = trans[curState][j].toHome();
	//lei_06
	//log("lei: From " + curState + " To " + j + " Via: " + nextReq);
	if (DEBUG>2) {
	    log("From " + curState + " To " + j + " Via: " + nextReq);
	}
	curState = j;
	return;
      }
	  //lei_06, if-condition ends here*******************
    }

    Debug.fail("Should not be here.");
  }

  // Needed, because Java 1.1 did not have Random.nextInt(int range)
  public int nextInt(int range) {
    int i = Math.abs(rand.nextInt());
    return (i % (range));
  }

  // Adds CUSTOMER_ID and SHOPPING_ID fields to HTTP request,
  //  if they are known.
  String addIDs(String i) {

    if (sessionID != null) {
      i = RBE.addSession(i,RBE.field_sessionID, ""+sessionID);
    }
    
    if (cid != ID_UNKNOWN) {
      i = RBE.addField(i,RBE.field_cid, ""+cid);
    }

    if (shopID != ID_UNKNOWN) {
      i = RBE.addField(i,RBE.field_shopID, ""+shopID);
    }

    return(i);
  }

  public int findID(String html, StrStrPattern tag) {
    int id;

    // NOTE: StringPattern.first/last are not thread-safe. 

    // Find the tag string.
    int i = tag.find(html);
    if (i==-1) {
      return(EB.ID_UNKNOWN);
    }
    i = i + tag.length();

    // Find the digits following the tag string.
    int j = CharSetStrPattern.digit.find(html.substring(i));
    if (j==-1) {
      return(EB.ID_UNKNOWN);
    }

    // Find the end of the digits.
    j = j + i;
    int k = CharSetStrPattern.notDigit.find(html.substring(j));
    if (k==-1) {
      k = html.length();
    }
    else {
      k = k + j;
    }	 
    
    id =  Integer.parseInt(html.substring(j, k));
    
    return(id);
  }

  public String findSessionID(String html, StrStrPattern tag, 
			   StrStrPattern etag) {
    // NOTE: StringPattern.first/last are not thread-safe. 

    // Find the tag string.
    int i = tag.find(html);
    if (i==-1) {
      return(null);
    }
    i = i + tag.length();

    // Find end of the digits.
    int j = etag.find(html, i);
    if (j==-1) {
      return(null);
    }
    
    return(html.substring(i,j));
  }

    private void log(String str) {
	System.out.println(name + ": " + str);
    }
}
