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
    }

    public boolean isAlive() {
        return thread != null && thread.isAlive();
    }

    public void interrupt() {
        thread.interrupt();
    }

    public void join(long millis) throws InterruptedException {
        thread.join(millis);
    }

    public String getStackTrace() {
        return java.util.Arrays.deepToString(thread.getStackTrace());
    }

  public void run() {
    long wirt_t1;  // WIRT.T1 in TPC_W.Spec.
    long wirt_t2;  // Same as TT.T1 in TPC-W Spec.
    long tt=0L;    // Think Time.

    initialize();

    try {
	if (waitKey) {
	    RBE.getKey();
	} else {
	    Thread.sleep(thinkTime());  // First thinkTime will stagger.
	}
    } catch (InterruptedException inte) {
	// Will exit if terminate is true.
    }

    while ((maxTrans == -1) || (maxTrans>0)) {
	if (terminate) { 
	    if (DEBUG > 0) {
		log("Committing suicide");
	    }
	    return;
	}

	wirt_t1 = System.currentTimeMillis();

	// Check if user session is finished.
	if ((wirt_t1 >= usmd) && (toHome)) {
	    // Start new user session.
	    log("Completed user session");
	    initialize();
	    continue;
	}

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
	if (nextReq.startsWith("file:")) {
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
	String html = getHTML(httpReq);
	if (terminate) {
	    if (DEBUG > 0) {
		log("Committing suicide");
	    }
	    return;
	}
	// 4) Measure absolute response time, TT.T1 = WIRT.T2.  This is a
	// time stamp just following the reception of the last byte of the
	// HTML response page, which was provided by the SUT.
	wirt_t2 = System.currentTimeMillis();

	if (html == null) {
            //rbe.stats.error(name, "Restarting user session due to error",
            //                httpReq.toString());
	    // Start new user session.
	    log("Restarting user session");
	    initialize();
	} else {
            // 5) Compute and store Web Interaction Response Time (WIRT) 
            if (DEBUG > 1) {
                log("Loaded page in " + (wirt_t2 - wirt_t1) + "ms");
            }
            rbe.stats.interaction(curState, wirt_t1, wirt_t2, tt);
	
            if (DEBUG > 2) {
                log("Post process: " + curTrans.getClass().getSimpleName());
            }
            curTrans.postProcess(this, html);

            // 6) Pick the next navigation option.
            // 7) Compose HTTP request.
            nextState(html);
        }

	// 8) Pick think time (TT), and compute absolute request time
	// 9) Wait for absolute request time.
	try {
	    if (waitKey) {
		RBE.getKey();
	    } else {
		long absReqTime = wirt_t2 + thinkTime();
		long remaining = absReqTime - System.currentTimeMillis();
		if (remaining > 0) {
		    Thread.sleep(remaining);
		}
	    }
	} catch (InterruptedException inte) {
	    if (DEBUG > 0) {
		log("Committing suicide");
	    }
	    return;
	}
	tt = System.currentTimeMillis() - wirt_t2;
	
	if (maxTrans > 0) maxTrans--;
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
        boolean retry = false;
        int retries = 0;
        Vector<ImageReader> imageRd = new Vector<ImageReader>(0);

        do {
            if (terminate) return null;
            if (retry) {
                try {
                    if (waitKey) {
                        RBE.getKey();
                    } else {
                        Thread.sleep(thinkTime());
                    }
                } catch (InterruptedException inte) {
                    return null;
                }	  
            }
            retry = false;

            html.setLength(0);
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                readStreamInto(conn.getInputStream(), html);
            } catch (SocketTimeoutException ste) {
                if (terminate) return null;
                rbe.stats.error(name, ste.getMessage(), url.toExternalForm());
                rbe.stats.serverError(System.currentTimeMillis());
                retry = true;
            } catch (IOException ioe) {
                if (terminate) return null;
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
                        if (terminate) return null;
                        log("Error handling IO error: " + ioe2.getMessage());
                    }
                }
                if (DEBUG > 1 && html.length() > 0) {
                    log("Server output: " + html.toString());
                }
                retry = true;
            }
        } while (retry && --retries >= 0);
   
        if (retries < 0) {
            return null;
        }

        String htmlStr = html.toString();
        if (DEBUG > 9) {
            log(htmlStr);
        }

        // Suppress image requests.
        if (!RBE.getImage) return htmlStr;

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
                return null;
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
          long r = RBE.nextInt(rand, 20000)+100;
          stagger = false;
          if (DEBUG > 1) {
              log("Think time staggering to " + r + "ms");
          }
          return((long) (r*tt_scale));
      }
      else {
          long r = rbe.negExp(rand, 7000L, 0.36788, 70000L, 4.54e-5, 7000.0);
          
          r = (long) (tt_scale*r);
          
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

  void nextState(String html)
  {
    int random = nextInt(MAX_PROB-MIN_PROB + 1) + MIN_PROB;

    if (DEBUG>4) {
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
      if (transProb[curState][j] >= random) {
	rbe.stats.transition(curState,j);
	curTrans = trans[curState][j];
	nextReq = curTrans.request(this, html);
	if (nextReq == null) {
	    Debug.fail("FATAL: nextReq == null");
	}
	toHome = trans[curState][j].toHome();
	if (DEBUG>2) {
	    log("From " + curState + " To " + j + " Via: " + nextReq);
	}
	curState = j;
	return;
      }
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
