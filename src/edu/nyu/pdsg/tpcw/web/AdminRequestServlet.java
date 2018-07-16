package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nyu.pdsg.tpcw.ejb.item.ItemAuthorModel;

/**
 * Admin Request Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class AdminRequestServlet extends TPCWServlet {

    public void serveRequest(HttpServletRequest request, HttpServletResponse  response, PrintWriter out) throws ServletException {

        Integer I_ID = new Integer(Integer.parseInt(request.getParameter("I_ID")));
        
        ItemAuthorModel book = null;
        try {
        	book = catalog.getItemAuthor(I_ID);
        } catch (RemoteException re) {
        	throw new ServletException(re);
        }

        out.println("<H2 ALIGN=\"center\">Title: " + book.getI_TITLE() + "</H2>");
        out.println("<P ALIGN=\"LEFT\">Author: " + book.getA_FNAME() + " " + book.getA_LNAME() + "<BR></P>");
		out.println("<IMG SRC=\"" + book.getI_IMAGE() + "\" ALIGN=\"RIGHT\" BORDER=\"0\">");
        out.println("<IMG SRC=\"" + book.getI_THUMBNAIL() +  "\" ALIGN=\"RIGHT\" BORDER=\"0\">");
        out.println("<P><BR></P>");
        out.print("<FORM ACTION=\"");
	out.print(response.encodeURL("admin_process"));
	out.println("\" METHOD=\"POST\">");
        out.println("<INPUT NAME=\"I_ID\" TYPE=\"hidden\" VALUE=\"" + I_ID + "\">");
        out.println("<TABLE BORDER=\"0\">");
        out.println("<TR><TD><B>Suggested Retail:</B></TD><TD><B>$ " + book.getI_SRP() + "</B></TD></TR>");
        out.println("<TR><TD><B>Our Current Price: </B></TD><TD><FONT COLOR=\"#dd0000\"><B>$ " + book.getI_COST() + "</B></FONT></TD></TR>");
        out.println("<TR><TD><B>Enter New Price</B></TD><TD ALIGN=\"right\">$ <INPUT TYPE=\"TEXT\" SIZE=\"30\" NAME=\"I_NEW_COST\" VALUE=\"" + book.getI_COST() + "\"></TD></TR>");
		out.println("<TR><TD><B>Enter New Qty In Stock</B></TD><TD ALIGN=\"right\">$ <INPUT TYPE=\"TEXT\" SIZE=\"30\" NAME=\"I_NEW_STOCK\" VALUE=\"" + book.getI_STOCK() + "\"></TD></TR>");
        out.println("<TR><TD><B>Enter New Picture</B></TD><TD ALIGN=\"right\"><INPUT TYPE=\"TEXT\" SIZE=\"30\" NAME=\"I_NEW_IMAGE\" VALUE=\"" + book.getI_IMAGE() + "\"></TD></TR>");
        out.println("<TR><TD><B>Enter New Thumbnail</B></TD><TD ALIGN=\"RIGHT\"><INPUT TYPE=\"TEXT\" SIZE=\"30\" NAME=\"I_NEW_THUMBNAIL\" VALUE=\"" + book.getI_THUMBNAIL() + "\"></TD></TR>");
        out.println("</TABLE>");
        out.println("<INPUT TYPE=\"IMAGE\" NAME=\"Submit\" SRC=\"images/submit_B.gif\">");        

	out.print("<A HREF=\"");
	out.print(response.encodeURL("search"));
	out.println("\">");
        out.println("<IMG SRC=\"images/search_B.gif\" ALT=\"Search\"></A>");        
	out.print("<A HREF=\"");
	out.print(response.encodeURL("home"));
	out.println("\">");
        out.println("<IMG SRC=\"images/home_B.gif\" ALT=\"Home\"></A></P>"); 
    }

	/**
	 * Returns the title of the servlet. Defines by subclasses.
	 */    
	protected String getTitle() {
		return "Admin Request Page";
	}

}
