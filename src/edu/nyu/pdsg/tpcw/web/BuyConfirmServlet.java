package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.nyu.pdsg.tpcw.ejb.cart.CartItem;
import edu.nyu.pdsg.tpcw.ejb.cart.ShoppingCart;
import edu.nyu.pdsg.tpcw.ejb.cart.ShoppingCartModel;
import edu.nyu.pdsg.tpcw.util.Constants;

/**
 * Buy Confirm Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class BuyConfirmServlet extends BuyServlet {
    
	protected void serveRequest(HttpServletRequest req, HttpServletResponse  response, PrintWriter out) throws ServletException {
		
		HttpSession session = req.getSession();
		ShoppingCart cart = getShoppingCart(cartHome, session);
		
		Object o = session.getAttribute(Constants.C_ID);
		Integer C_ID;
		if (o == null) {
			out.println("<h2><font color=\"red\">You are not signed in ...</font></h2>");
			return;
		}
		C_ID = (Integer)o;

        String CC_TYPE = Constants.CARD_TYPES[Integer.parseInt(req.getParameter("CC_TYPE"))-1];
        String CC_NUMBER = req.getParameter("CC_NUMBER");
        String CC_NAME = req.getParameter("CC_NAME");

        String CC_EXPIRYstr = req.getParameter("CC_EXPIRY");
        Date CC_EXPIRY = null;
        DateFormat myDF = DateFormat.getDateInstance(DateFormat.SHORT);
        try {
            CC_EXPIRY = myDF.parse(CC_EXPIRYstr);
        } catch(ParseException e) {
            out.println("<h2><font color=\"red\">Invalid Credit Card Expiration date ...</font></h2>");
            return;
        }
        
        String SHIPPING = Constants.SHIP_TYPES[Integer.parseInt(req.getParameter("SHIPPING"))-1];
        String STREET_1 = req.getParameter("STREET_1");
		String STREET_2 = null;
		String CITY = null;
		String STATE = null;
		String ZIP = null;
		Integer CO_ID = null;
        if (STREET_1 != null && !STREET_1.equals("")) {
            STREET_2 = req.getParameter("STREET_2");
            CITY = req.getParameter("CITY");
            STATE = req.getParameter("STATE");
            ZIP = req.getParameter("ZIP");
            CO_ID = new Integer(req.getParameter("CO_ID"));
        }
        
        // create the order
        ShoppingCartModel cartModel = null;
        try {
	        cartModel = cart.processOrder(CC_TYPE, CC_NUMBER, CC_NAME, CC_EXPIRY, SHIPPING, STREET_1, STREET_2, 
	        				CITY, STATE, ZIP, CO_ID);
        } catch (RemoteException re) {
        	throw new ServletException(re);
        }
        
        //Print out the HTML page
        out.println("<BLOCKQUOTE><BLOCKQUOTE>");
        out.println("<H2 ALIGN=\"LEFT\">Order Information:</H2>");
        out.println("<TABLE BORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
        out.println("<TR><TD><B>Qty</B></TD><TD><B>Product</B></TD></TR>");
        
        //For each item in the shopping cart, print out its contents
        Iterator iter = cartModel.lineItems();
        while (iter.hasNext()) {
            CartItem item = (CartItem)iter.next();
            out.println("<TR><TD VALIGN=\"TOP\">" + item.getSCL_QTY() + "</TD>");
            out.println("<TD VALIGN=\"TOP\">ID: " + item.getSCL_I_ID() + ". Title:<I>" + item.getSCL_TITLE() + "</I> - Backing: " + item.getSCL_BACKING() + "<BR>SRP. $" + ((double)item.getSCL_SRP())/100.0 + "<FONT COLOR=\"#aa0000\"><B>Your Price: $" + ((double)item.getSCL_COST())/100.0 + "</FONT> </TD></TR>");
        }
        
        out.println("</TABLE><H2 ALIGN=\"LEFT\">Your Order has been processed.</H2>");
        out.println("<TABLE BORDER=\"1\" CELLPADDING=\"5\" CELLSPACING=\"0\">");
        out.println("<TR><TD><H4>Subtotal with discount:</H4></TD>");
        out.println("<TD> <H4>$" + ((double)cartModel.getSC_SUB_TOTAL())/100.0 +"</H4></TD></TR>");
        out.println("<TR><TD><H4>Tax (8.25%):</H4></TD>");
        out.println("<TD><H4>$" + ((double)cartModel.getSC_TAX())/100.0 + "</H4></TD></TR>");
        out.println("<TR><TD><H4>Shipping &amp; Handling:</H4></TD>");
        out.println("<TD><H4>$"+ ((double)cartModel.getSC_SHIP_COST())/100.0 + "</H4></TD></TR>");
        out.println("<TR><TD> <H4>Total:</H4></TD>");
        out.println("<TD><H4>$" + ((double)cartModel.getSC_TOTAL())/100.0 + "</H4></TD></TR></TABLE>");
        out.println("<P><BR></P><H2>Order Number: " + cartModel.getO_ID() + "</H2>");
        out.println("<H1>Thank you for shopping at TPC-W</H1><P></P>");
        
        //Add the buttons
        out.print("<CENTER><P><A HREF=\"");
        out.print(response.encodeURL("search"));
        out.println("\"><IMG SRC=\"images/search_B.gif\" ALT=\"Search\"></A>");
        out.print("<A HREF=\"");
        out.print(response.encodeURL("home"));
        out.println("\"><IMG SRC=\"images/home_B.gif\" ALT=\"Home\"></A>");
        out.print("</CENTER></BLOCKQUOTE></BLOCKQUOTE>");
    }    

	protected String getTitle() {
		return "Buy Confirm Page";
	}
}
