package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nyu.pdsg.tpcw.ejb.cart.CartItem;
import edu.nyu.pdsg.tpcw.ejb.cart.ShoppingCart;
import edu.nyu.pdsg.tpcw.ejb.cart.ShoppingCartModel;

/**
 * Shopping Cart Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class ShoppingCartServlet extends HomeServlet {
    
	protected void serveRequest(HttpServletRequest req, HttpServletResponse  response, PrintWriter out)  throws ServletException {

		// get Shopping Cart
		ShoppingCart cart = getShoppingCart(cartHome, req.getSession());
		
		ShoppingCartModel cartModel = null;

		String action = req.getParameter("ACTION");
		if (action != null && action.equals("ADD")) {
			// add one item to the shopping cart
			Integer I_ID = new Integer(req.getParameter("I_ID"));
			int qty = Integer.parseInt(req.getParameter("QTY"));
			try {
				cartModel = cart.addItem(I_ID, qty);
			} catch (RemoteException re) {
				throw new ServletException(re);
			}
		} else if (action != null && action.equals("UPDATE")) {
			// update shopping cart
			// We need to parse an arbitrary number of I_ID/QTY pairs from the url
			Vector ids = new Vector();
			Vector quantities = new Vector();
			int i = 0;
			String curr_QTYstr;
			String curr_I_IDstr;
        
			curr_I_IDstr = req.getParameter("I_ID_" + i);
			curr_QTYstr = req.getParameter("QTY_" + i);
			while ((curr_I_IDstr != null) && (!curr_I_IDstr.equals(""))) {
				ids.addElement(new Integer(curr_I_IDstr));
				quantities.addElement(new Integer(curr_QTYstr));
				i++;
				curr_QTYstr = req.getParameter("QTY_" + i);
				curr_I_IDstr = req.getParameter("I_ID_" + i);
			}
			try {
				cartModel = cart.update(ids, quantities);
			} catch (RemoteException re) {
				throw new ServletException(re);
			}
		} else {
			// just obtain the shopping cart data
			try {
				cartModel = cart.getModel();
			} catch (RemoteException re) {
				throw new ServletException(re);
			}
		}
		
        //Print out the promotional processing stuff
        displayPromotions(req, response, out, rand);
        
        //Display the shopping cart contents
        out.print("<FORM ACTION=\"");
	out.print(response.encodeURL("cart"));
	out.println("\" METHOD=\"get\">");
		out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"ACTION\" value = \"UPDATE\">");
        out.println("<CENTER><P></P><TABLE BORDER=\"0\">");
        out.println("<TR><TD><B>Qty</B></TD><TD><B>Product</B></TD></TR>");

        //Print out the entries in the shopping cart
        Iterator lineItems = cartModel.lineItems();
        int i = 0; // auxiliary counter
        while (lineItems.hasNext()){
            CartItem line = (CartItem)lineItems.next();
            out.println("<TR><TD VALIGN=\"top\">");
            out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"I_ID_" + i + "\" value = \"" + line.getSCL_I_ID() + "\">");
            out.println("<INPUT NAME=\"QTY_" + i + "\" SIZE=\"3\" VALUE=\"" + line.getSCL_QTY() +"\"></TD>");
            out.println("<TD VALIGN=\"top\">ID: " + line.getSCL_I_ID() + ". Title:<I>" + line.getSCL_TITLE() + "</I> - Backing: " + line.getSCL_BACKING() + "<BR>");
            out.println("SRP. $" + ((double)line.getSCL_SRP())/100.0 + "</B>");
            out.println("<FONT COLOR=\"#aa0000\"><B>Your Price: $" + ((double)line.getSCL_COST())/100.0 + "</B></FONT></TD></TR>");
            i++;
        }

        out.println("</TABLE><B><I>Subtotal price: " + ((double)cartModel.getSC_SUB_TOTAL())/100.0 + "</I></B>");
        out.print("<P><BR><A HREF=\"");
	out.print(response.encodeURL("register"));
	out.println("\"><IMG SRC=\"images/checkout_B.gif\"></A>");
        out.print("<A HREF=\"");
	out.print(response.encodeURL("home"));
	out.println("\"><IMG SRC=\"images/home_B.gif\"></P></A>");
        out.println("<P>If you have changed the quantities and/or taken anything out<BR> of your shopping cart, click here to refresh your shopping cart:</P> ");
        out.println("<P><INPUT TYPE=\"IMAGE\" NAME=\"Refresh\" SRC=\"images/refresh_B.gif\"></P>");
        out.println("</CENTER></FORM>");
    }

	protected String getTitle() {
		return "Shopping Cart Page";
	}
}
