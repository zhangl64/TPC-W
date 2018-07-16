package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nyu.pdsg.tpcw.ejb.item.ShortItemAuthorModel;
import edu.nyu.pdsg.tpcw.util.Constants;

/**
 * New Products Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class NewProductsServlet extends TPCWServlet {
    
	// for promotion generation
	protected Random rand;

	public void init() throws ServletException {
		rand = new Random(System.currentTimeMillis());
		super.init();
	}
    
	public void serveRequest(HttpServletRequest req, HttpServletResponse  response, PrintWriter out) throws ServletException {
        
		String subject  = req.getParameter("subject");

		out.println("<H2 ALIGN=\"center\">SUBJECT: " + subject + "</H2>");
        
		// Display promotions
		displayPromotions(req, response, out, rand);
        
		Collection books = null;
		// Display new products
		try {
			books = catalog.searchNewItemsBySubject(subject, Constants.SEARCH_LIMIT);
		} catch (RemoteException re) {
			throw new ServletException(re);
		}
            
		Iterator it = books.iterator();
        
		out.println("<TABLE BORDER=\"1\" CELLPADDING=\"1\" CELLSPACING=\"1\">");
		out.println("<TR> <TD WIDTH=\"30\"></TD>");
		out.println("<TD><FONT SIZE=\"+1\">Author</FONT></TD>");
		out.println("<TD><FONT SIZE=\"+1\">Title</FONT></TD></TR>");
        
		//Print out a line for each item returned by the DB
		int i = 1;
		while (it.hasNext()) {
			ShortItemAuthorModel myBook = (ShortItemAuthorModel)it.next();
			out.print("<TR><TD>" + i + "</TD>\n");
			out.print("<TD><I>"+ myBook.getA_FNAME() + " " + myBook.getA_LNAME() +"</I></TD>");
            
			out.print("<TD><A HREF=\"");
			out.print(response.encodeURL("item"));
			out.print("?I_ID=" + myBook.getI_ID());
			out.println("\">" + myBook.getI_TITLE() + "</A></TD></TR>");
			i++;
		}
        
		out.println("</TABLE><P><CENTER>");
        
		//Generate shopping cart, search, and home buttons.
		out.println("<P ALIGN=\"CENTER\">");
		out.print("<A HREF=\"");
		out.print(response.encodeURL("cart"));
		out.println("\"><IMG SRC=\"images/shopping_cart_B.gif\" ALT=\"Shopping Cart\"></A>");
		out.print("<A HREF=\"");
		out.print(response.encodeURL("search"));
		out.println("\"><IMG SRC=\"images/search_B.gif\" ALT=\"Search\"></A>");
		out.print("<A HREF=\"");
		out.print(response.encodeURL("home"));
		out.println("\"><IMG SRC=\"images/home_B.gif\" ALT=\"Home\"></A>");
	}

	/**
	 * Returns the title of the servlet.
	 */
	protected String getTitle() {
		return "New Products Page";
	}
}
