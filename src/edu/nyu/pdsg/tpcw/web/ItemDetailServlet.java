package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nyu.pdsg.tpcw.ejb.item.ItemAuthorModel;

/**
 * Item Detail Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class ItemDetailServlet extends TPCWServlet {

    public void serveRequest(HttpServletRequest request, HttpServletResponse  response, PrintWriter out) throws ServletException {
    	
    	Integer I_ID = new Integer(Integer.parseInt(request.getParameter("I_ID")));
        
        ItemAuthorModel myItemAuthor = null;
        try {
        	myItemAuthor = catalog.getItemAuthor(I_ID);
        } catch (RemoteException re) {
        	throw new ServletException(re);
        }
        
        out.println("<H2> Title: " + myItemAuthor.getI_TITLE() + "</H2>");
        out.println("<P>Author: " + myItemAuthor.getA_FNAME() + " " + myItemAuthor.getA_LNAME() + "<BR>");
        out.println("Subject: " + myItemAuthor.getI_SUBJECT());
        out.println("<P><IMG SRC=\"" + myItemAuthor.getI_IMAGE() + "\" ALIGN=\"RIGHT\" BORDER=\"0\">");
        out.println("Decription: <I>" + myItemAuthor.getI_DESC() + "</I></P>");
        out.println("<BLOCKQUOTE><P><B>Item ID: " + myItemAuthor.getI_ID() + "</B><BR>");
        out.println("<B>In Stock: " + myItemAuthor.getI_STOCK() + "</B><BR>");        out.println("<B>Suggested Retail: $" + myItemAuthor.getI_SRP() + "</B>");
        out.println("<BR><B>Our Price:</B>");
        out.println("<FONT COLOR=\"#dd0000\"><B> $" + myItemAuthor.getI_COST() + "</B></FONT><BR>");
        double d = myItemAuthor.getI_SRP().doubleValue() - myItemAuthor.getI_COST().doubleValue();
        double d2 = ((double)Math.round(d*100))/100.0;
        out.println("<B>You Save:</B><FONT COLOR=\"#dd0000\"> $" + d2 + "</B></FONT></P>");
        out.println("</BLOCKQUOTE><DL><DT><FONT SIZE=\"2\">");
        out.println("Backing: " + myItemAuthor.getI_BACKING() + ", " + myItemAuthor.getI_PAGE() + " pages<BR>");
        out.println("Published by " + myItemAuthor.getI_PUBLISHER() +"<BR>");
        out.println("Publication date: " + myItemAuthor.getI_PUB_DATE() +"<BR>");
        out.println("Avail date: " + myItemAuthor.getI_AVAIL() + "<BR>");
        out.println("Dimensions (in inches): " + myItemAuthor.getI_DIMENSIONS() + "<BR>");
        out.println("ISBN: " + myItemAuthor.getI_ISBN() +"</FONT></DT></DL><P>");
        
        out.print("<CENTER><A HREF=\"");
	out.print(response.encodeURL("cart"));
	out.println("?ACTION=ADD&I_ID=" + I_ID + "&QTY=1\">");
        out.println("<IMG SRC=\"images/add_B.gif\" ALT=\"Add to Basket\"></A>");
        
        out.print("<A HREF=\"");
	out.print(response.encodeURL("search"));
        out.println("\"><IMG SRC=\"images/search_B.gif\" ALT=\"Search\"></A>");
        
        out.print("<A HREF=\"");
	out.print(response.encodeURL("home"));
        out.println("\"><IMG SRC=\"images/home_B.gif\" ALT=\"Home\"></A>");
        
        out.print("<A HREF=\"");
	out.print(response.encodeURL("admin"));
	out.println("?I_ID=" + I_ID + "\">");
        out.println("<IMG SRC=\"images/update_B.gif\" ALT=\"Update\"></A></CENTER>");
    }

	/**
	 * Returns the title of the servlet. Defines by subclasses.
	 */    
	protected String getTitle() {
		return "Product Detail Page";
	}
}
