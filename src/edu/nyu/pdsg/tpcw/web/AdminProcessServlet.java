package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nyu.pdsg.tpcw.ejb.adminportal.AdminPortal;
import edu.nyu.pdsg.tpcw.ejb.adminportal.AdminPortalHome;
import edu.nyu.pdsg.tpcw.ejb.item.ItemAuthorModel;

/**
 * Admin Process Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class AdminProcessServlet extends TPCWServlet {
    
	// reference to the AdminHome
	protected AdminPortal adminPortal;
	
	public void init() throws ServletException {
		Object objref;
		try {
			objref = (new InitialContext()).lookup("java:comp/env/ejb/AdminPortal");
			adminPortal = ((AdminPortalHome)PortableRemoteObject.narrow(objref, AdminPortalHome.class)).create();
		} catch (Exception ne) {
			throw new ServletException(ne.getMessage());
		}

		super.init();
	}
    
    public void serveRequest(HttpServletRequest request, HttpServletResponse  response, PrintWriter out) throws ServletException {

        Integer I_ID = Integer.valueOf(request.getParameter("I_ID"));
        String I_NEW_IMAGE = (String)request.getParameter("I_NEW_IMAGE");
        String I_NEW_THUMBNAIL = (String)request.getParameter("I_NEW_THUMBNAIL");
        String I_NEW_COSTstr = (String) request.getParameter("I_NEW_COST");
        double dbl = Double.parseDouble(I_NEW_COSTstr);
        Double I_NEW_COST = new Double(((double)Math.round(dbl*100))/100);
        Integer I_NEW_STOCK = new Integer(request.getParameter("I_NEW_STOCK"));

		ItemAuthorModel myItemAuthor = null;
		try	{
	        myItemAuthor = adminPortal.updateItem(I_ID, I_NEW_COST, I_NEW_STOCK, I_NEW_IMAGE, I_NEW_THUMBNAIL);
		} catch (Exception e) {
			throw new ServletException(e);
		}
            
		out.println("<H2> Title: " + myItemAuthor.getI_TITLE() + "</H2>");
		out.println("<P>Author: " + myItemAuthor.getA_FNAME() + " " + myItemAuthor.getA_LNAME() + "<BR>");
		out.println("<P><IMG SRC=\"" + myItemAuthor.getI_IMAGE() + "\" ALIGN=\"RIGHT\" BORDER=\"0\">");
		out.println("Decription: <I>" + myItemAuthor.getI_DESC() + "</I></P>");
		out.println("<BLOCKQUOTE><P><B>In Stock: " + myItemAuthor.getI_STOCK() + "</B><BR>");
		out.println("<B>Suggested Retail: $" + myItemAuthor.getI_SRP() + "</B>");
		out.println("<BR><B>Our Price:</B>");
		out.println("<FONT COLOR=\"#dd0000\"><B> $" + myItemAuthor.getI_COST() + "</B></FONT><BR>");
		double d = myItemAuthor.getI_SRP().doubleValue() - myItemAuthor.getI_COST().doubleValue();
		double d2 = ((double)Math.round(d*100))/100.0;
		out.println("<B>You Save:</B><FONT COLOR=\"#dd0000\"> $" + d2 + "</B></FONT></P>");
		out.println("</BLOCKQUOTE><DL><DT><FONT SIZE=\"2\">");
		out.println("Backing: " + myItemAuthor.getI_BACKING() + ", " + myItemAuthor.getI_PAGE() + " pages<BR>");
		out.println("Published by " + myItemAuthor.getI_PUBLISHER() +"<BR>");
		out.println("Publication date: " + myItemAuthor.getI_PUB_DATE() +"<BR>");
		out.println("Dimensions (in inches): " + myItemAuthor.getI_DIMENSIONS() + "<BR>");
		out.println("ISBN: " + myItemAuthor.getI_ISBN() +"</FONT></DT></DL><P>");
        
		out.print("<CENTER><A HREF=\"");
		out.print(response.encodeURL("search"));
		out.println("\">");
		out.println("<IMG SRC=\"images/search_B.gif\" ALT=\"Search\"></A>");
        
		out.print("<A HREF=\"");
		out.print(response.encodeURL("home"));
		out.println("\">");
		out.println("<IMG SRC=\"images/home_B.gif\" ALT=\"Home\"></A></CENTER>");
    }

	/**
	 * Returns the title of the servlet. Defines by subclasses.
	 */    
	protected String getTitle() {
		return "Admin Confirm Page";
	}
}
