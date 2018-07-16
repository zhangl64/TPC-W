/*-------------------------------------------------------------------------
 * rbe.ImageReader.java
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

public class ImageReader {

  protected String imgURLStr;
  protected URL srcURL;
  protected URL imgURL;
  protected byte [] buffer;
  protected BufferedInputStream imgIn;
  protected RBE rbe;
  protected String eb;
  protected int tot;

  public ImageReader(RBE rbe, String eb, URL srcURL, String url, byte [] buffer)
  {
    imgURLStr = url;
    this.buffer = buffer;
    this.srcURL = srcURL;
    this.rbe = rbe;
    this.eb = eb;
  }

  // Read as much of the image as we can without blocking.
  // Returns true if there is more image to read.
  // Returns false if the image has been read.
  public boolean readImage() throws InterruptedException
  {
    // Create the image URL if necessary.
    if (imgURL == null) {
      try {
	imgURL = new URL(srcURL, imgURLStr);
      }
      catch(MalformedURLException mue) {
	// This one means the received HTML is bad.
	rbe.stats.error(eb, "Malformed image URL", imgURLStr);
	return(false);
      }
    }

    // Open the image URL if necessary.
    if (imgIn == null) {
      try {
          HttpURLConnection conn = (HttpURLConnection) imgURL.openConnection();
          conn.setConnectTimeout(5000);
          conn.setReadTimeout(5000);
	  imgIn = new BufferedInputStream(conn.getInputStream());
	  //System.out.println("EB opened image: " + imgURLStr);
      }
      catch (IOException ioe) {
	  rbe.stats.error(eb, "Unable to open image: " + ioe.getMessage(),
                          imgURLStr);
	  //Thread.sleep(1000L);
	  return(true);
      }
      tot = 0;
    }
    
    try {
	int r = imgIn.read(buffer, 0, buffer.length);
	if (r == -1) {
	    close();
	    return(false);
	}
	else {
	    tot += r;
	    return(true);
	}
    }
    catch (IOException ioe) {
      rbe.stats.error(eb, "Unable to read image: " + ioe.getMessage(),
                      imgURLStr);
      close();
      return(true);
    }
  }

  private void close()
  {
    try {
      imgIn.close();
    }
    catch (IOException ioe) {
      rbe.stats.error(eb, "Unable to close image: " + ioe.getMessage(),
                      imgURLStr);
    }
    
    imgIn = null;
  }
}
