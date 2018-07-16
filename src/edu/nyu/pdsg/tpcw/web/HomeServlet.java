package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Random;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.nyu.pdsg.tpcw.ejb.cart.ShoppingCart;
import edu.nyu.pdsg.tpcw.ejb.cart.ShoppingCartHome;
import edu.nyu.pdsg.tpcw.util.Constants;

/**
 * Home Servlet.
 * Further extended by some servlets.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class HomeServlet extends TPCWServlet {

	// for promotion generation
	protected Random rand;

	// reference to the ShoppingCart home interface
	protected ShoppingCartHome cartHome;
	
	public void init() throws ServletException {
		rand = new Random(System.currentTimeMillis());

		Object objref;
		try {
			objref = (new InitialContext()).lookup("java:comp/env/ejb/ShoppingCart");
		} catch (NamingException ne) {
			throw new ServletException(ne.getMessage());
		}
		cartHome = (ShoppingCartHome)PortableRemoteObject.narrow(objref, ShoppingCartHome.class);

		super.init();
	}
    
    protected void serveRequest(HttpServletRequest req, HttpServletResponse  response, PrintWriter out)  throws ServletException {

		HttpSession session = req.getSession();
		ShoppingCart cart = getShoppingCart(cartHome, session);
	
        // Say Hello!
        String C_FNAME = null;
        String C_LNAME = null;
        try {
	        C_FNAME = cart.getSC_C_FNAME();
	        C_LNAME = cart.getSC_C_LNAME();
		} catch (RemoteException re) {
			throw new ServletException(re.getMessage());
		}

		String hello = null;
		if ((C_FNAME == null) && (C_LNAME == null)) hello = "Welcome, customer!";
		else hello = "Welcome back, " + C_FNAME + " " + C_LNAME + "!";
		out.println("<p><center><b>" + hello + "</b></center><p>");
        
        // Insert the promotional processing
        displayPromotions(req, response, out, rand);
        
        //Generate Table of What's New and BestSellersTable headings
        out.println("<TABLE ALIGN=\"center\" BGCOLOR=\"#c0c0c0\" BORDER=\"0\" CELLPADDING=\"6\" CELLSPACING=\"0\" WIDTH=\"700\">");
        out.println("<TR ALIGN=\"CENTER\" BGCOLOR=\"#ffffff\" VALIGN=\"top\">");
        out.println("<TD COLSPAN=\"2\" VALIGN=\"MIDDLE\" WIDTH=\"300\">");
        out.println("<IMG SRC=\"images/whats_new.gif\" ALT=\"New Product\">");
        out.println("</TD>");
        out.println("<TD BGCOLOR=\"#ffffff\" WIDTH=\"100\"></TD>");
        out.println("<TD COLSPAN=\"2\" WIDTH=\"300\">");
        out.println("<IMG SRC=\"images/best_sellers.gif\" ALT=\"Best Seller\"></TD></TR>");
        
        for (int i = 0; i < Constants.NUM_SUBJECTS/2; i++) {
	    out.print("<TR><TD><P ALIGN=\"center\"><A HREF=\"");
	    out.print(response.encodeURL("new_products"));
	    out.println("?subject=" + Constants.SUBJECTS[i] + "\">" + Constants.SUBJECTS[i] + "</A></P></TD>");
	    out.print("<TD><P ALIGN=\"center\"><A HREF=\"");
	    out.print(response.encodeURL("new_products"));
	    out.println("?subject=" + Constants.SUBJECTS[i + Constants.NUM_SUBJECTS/2] + "\">" + Constants.SUBJECTS[i + Constants.NUM_SUBJECTS/2] + "</A></P></TD>");
			out.println("<TD BGCOLOR=\"#ffffff\" WIDTH=\"50\"></TD>");
			out.print("<TD><P ALIGN=\"center\"><A HREF=\"");
			out.print(response.encodeURL("best_sellers"));
			out.println("?subject=" + Constants.SUBJECTS[i] + "\">" + Constants.SUBJECTS[i] + "</A></P></TD>");
			out.print("<TD><P ALIGN=\"center\"><A HREF=\"");
			out.print(response.encodeURL("best_sellers"));
			out.println("?subject=" + Constants.SUBJECTS[i + Constants.NUM_SUBJECTS/2] + "\">" + Constants.SUBJECTS[i + Constants.NUM_SUBJECTS/2] + "</A></P></TD></TR>");
        }
        out.println("</TABLE>");
        
        //Generate shopping cart, search, and order status buttons.
        out.println("<P ALIGN=\"CENTER\">");
        out.print("<A HREF=\"");
	out.print(response.encodeURL("cart"));
	out.println("\"><IMG SRC=\"images/shopping_cart_B.gif\" ALT=\"Shopping Cart\"></A>");
        out.print("<A HREF=\"");
	out.print(response.encodeURL("search"));
	out.println("\"><IMG SRC=\"images/search_B.gif\" ALT=\"Search\"></A>");
        out.print("<A HREF=\"");
	out.print(response.encodeURL("order_status"));
	out.println("\"><IMG SRC=\"images/order_status_B.gif\" ALT=\"Order Status\"></A>");
    }

	protected String getTitle() {
		return "Home Page";
	}
}
