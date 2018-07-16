package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.nyu.pdsg.tpcw.ejb.adminportal.AdminPortal;
import edu.nyu.pdsg.tpcw.ejb.adminportal.AdminPortalHome;
import edu.nyu.pdsg.tpcw.ejb.cart.CartItem;
import edu.nyu.pdsg.tpcw.ejb.cart.ShoppingCart;
import edu.nyu.pdsg.tpcw.ejb.cart.ShoppingCartModel;
import edu.nyu.pdsg.tpcw.ejb.customer.BigCustomerModel;
import edu.nyu.pdsg.tpcw.util.Constants;

/**
 * Buy Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class BuyServlet extends HomeServlet {

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
    
	protected void serveRequest(HttpServletRequest req, HttpServletResponse  response, PrintWriter out) throws ServletException {
        
        String RETURNING_FLAG = req.getParameter("RETURNING_FLAG");

		HttpSession session = req.getSession();
		ShoppingCart cart = getShoppingCart(cartHome, session);
        BigCustomerModel cust = null;
        
        if (RETURNING_FLAG.equals("Y")) {
            String UNAME = req.getParameter("UNAME");
            String PASSWD = req.getParameter("PASSWD");

			// authenticate the customer and get his info            
            try {
            	cust = adminPortal.authenticate(UNAME, PASSWD);
            	if (cust == null) {
            		// authentication failed
            		out.println("<h2><font color=\"red\">Authentication failed ...</font></h2>");
            		return;
            	}
            } catch (RemoteException re) {
            	throw new ServletException(re);
            }
        } else {
        	// RETURNING_FLAG = "N" -- new customer
            String C_FNAME = req.getParameter("FNAME");
            String C_LNAME = req.getParameter("LNAME"); 
            String ADDR_STREET1 = req.getParameter("STREET1");
            String ADDR_STREET2 = req.getParameter("STREET2");
            String ADDR_CITY = req.getParameter("CITY");
            String ADDR_STATE = req.getParameter("STATE");
            String ADDR_ZIP = req.getParameter("ZIP");
            Integer CO_ID = new Integer(req.getParameter("CO_ID"));
            String C_PHONE = req.getParameter("PHONE");
            String C_EMAIL = req.getParameter("EMAIL");

            Date C_BIRTHDATE = null;
            DateFormat myDF = DateFormat.getDateInstance(DateFormat.SHORT);
            try {
                C_BIRTHDATE = myDF.parse(req.getParameter("BIRTHDATE"));
            } catch(ParseException e) {
                out.println("<h2><font color=\"red\">Invalid birthdate format</font></h2>");
                return;
            }
            
            String C_DATA = req.getParameter("DATA");

			// create new customer
            try {
            	cust = adminPortal.createNewCustomer(C_FNAME, C_LNAME, ADDR_STREET1, 
							ADDR_STREET2, ADDR_CITY, ADDR_STATE, ADDR_ZIP, CO_ID, 
							C_PHONE, C_EMAIL, C_BIRTHDATE, C_DATA);
				cart.setSC_C_FNAME(cust.getC_FNAME());
				cart.setSC_C_LNAME(cust.getC_LNAME());
				cart.setSC_C_DISCOUNT((int)(cust.getC_DISCOUNT().doubleValue()*100));
            } catch (Exception e) {
            	throw new ServletException(e);
            }
        }
		// mark session that customer has checked in
		session.setAttribute(Constants.C_ID, cust.getC_ID());
		session.setAttribute(Constants.C_UNAME, cust.getC_UNAME());

        // update shopping cart and prepare shopping cart to checkout and get its data
		ShoppingCartModel cartModel = null;
		try {
			cart.setC_ID(cust.getC_ID());
			cart.setSC_C_FNAME(cust.getC_FNAME());
			cart.setSC_C_LNAME(cust.getC_LNAME());
			cart.setSC_C_DISCOUNT((int)(cust.getC_DISCOUNT().doubleValue()*100));
        	cartModel = cart.prepareToBuy();
		} catch (Exception e) {
			throw new ServletException(e);
		}

        //Print out the web page
        out.print("<HR><FORM ACTION=\"");
	out.print(response.encodeURL("buy_confirm"));
	out.println("\" METHOD=\"POST\">");
        out.println("<TABLE BORDER=\"0\" WIDTH=\"100%\">");
        out.println("<TR ALIGN=\"LEFT\" VALIGN=\"TOP\">");
        out.println("<TD VALIGN=\"TOP\" WIDTH=\"50%\">");
        out.println("<H2>Billing Information:</H2>");
        out.println("<TABLE WIDTH=\"100%\" BORDER=\"0\"><TR>");
        
        out.println("<TR><TD>Firstname:</TD><TD>" + cust.getC_FNAME() + "</TD></TR>");
        out.println("<TR><TD>Lastname:</TD><TD>" + cust.getC_LNAME() + "</TD></TR>");
        out.println("<TR><TD>Addr_street_1:</TD><TD>" + cust.getADDR_STREET1() + "</TD></TR>");
        out.println("<TR><TD>Addr_street_2:</TD><TD>" + cust.getADDR_STREET2() + "</TD></TR>");
        out.println("<TR><TD>City:</TD><TD>" + cust.getADDR_CITY() + "</TD></TR>");
        out.println("<TR><TD>State:</TD><TD>" + cust.getADDR_STATE() + "</TD></TR>");
        out.println("<TR><TD>Zip:</TD><TD>" + cust.getADDR_ZIP() + "</TD></TR>");
        out.println("<TR><TD>Country:</TD><TD>" + cust.getCO_NAME() + "</TD></TR>");
        out.println("<TR><TD>Email:</TD><TD>" + cust.getC_EMAIL() + "</TD></TR>");
        out.println("<TR><TD>Phone:</TD><TD>" + cust.getC_PHONE() + "</TD></TR>");

        if (RETURNING_FLAG.equals("N")){
            out.println("<TR><TD>C_ID:</TD><TD>" + cust.getC_ID() + "</TD></TR>");
            out.println("<TR><TD>USERNAME:</TD><TD>" + cust.getC_UNAME() + "</TD></TR>");
            out.println("<TR><TD>PASSWORD:</TD><TD>" + cust.getC_PASSWD() + "</TD></TR>");
        }
        out.println("</TABLE></TD>");
        
        //
        //The Shipping Info Form
        out.println("<TD VALIGN=\"TOP\" WIDTH=\"50%\">");
        out.println("<H2>Shipping Information:</H2>");
        out.println("<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\" WIDTH=\"100%\">");
        out.println("<TR><TD WIDTH=\"50%\">Addr_street_1:</TD>");
        out.println("<TD><INPUT NAME=\"STREET_1\" SIZE=\"40\" VALUE=\"\"></TD></TR>");
        out.println("<TR><TD>Addr_street_ 2:</TD>");
        out.println("<TD><INPUT NAME=\"STREET_2\" SIZE=\"40\" VALUE=\"\"></TD></TR>");
        out.println("<TR><TD>City:</TD><TD><INPUT NAME=\"CITY\" SIZE=\"30\" VALUE=\"\"></TD></TR>");
        out.println("<TR><TD>State:</TD><TD><INPUT NAME=\"STATE\" SIZE=\"20\" VALUE=\"\"></TD></TR>");
        out.println("<TR><TD>Zip:</TD><TD><INPUT NAME=\"ZIP\" SIZE=\"10\" VALUE=\"\"></TD></TR>");

		out.println("<TR><TD>Country:</TD>");
		out.println("<TD><SELECT NAME=\"CO_ID\">");
		for (int i = 1; i <=Constants.NUM_COUNTRIES; i++) {
			out.println("<OPTION VALUE=\"" + i + "\">" + Constants.COUNTRIES[i-1] + "</OPTION>");
		}
		out.println("</SELECT></TD></TR>");
        
        //
        //Order Information Section
        out.println("</TABLE></TD></TR></TABLE>");
        out.println("<HR><H2>Order Information:</H2>");
        out.println("<TABLE BORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
        out.println("<TR><TD><B>Qty</B></TD><TD><B>Product</B></TD></TR>");
        
        //Insert Shopping Cart Contents Here!
        Iterator iter = cartModel.lineItems();
        while (iter.hasNext()) {
        	CartItem item = (CartItem)iter.next();
            out.println("<TR><TD VALIGN=\"TOP\">" + item.getSCL_QTY() + "</TD>");
            out.println("<TD VALIGN=\"TOP\">ID: " + item.getSCL_I_ID() + ". Title:<I>" + item.getSCL_TITLE() + "</I> - Backing: " + item.getSCL_BACKING());
            out.println("<BR>SRP. $" + ((double)item.getSCL_SRP())/100.0);
            out.println("<FONT COLOR=\"#aa0000\">");
            out.println("<B>Your Price:" + ((double)item.getSCL_COST())/100.0 + "</B>");
            out.println("</FONT></TD></TR>");
        }
        
        out.println("</TABLE>");
        out.println("<P><BR></P><TABLE BORDER=\"0\">");
        out.println("<TR><TD><B>Subtotal with discount (" + cartModel.getSC_C_DISCOUNT() + "%):</B></TD><TD ALIGN=\"RIGHT\"><B>$" + ((double)cartModel.getSC_SUB_TOTAL())/100.0 +"</B></TD></TR>");
        out.println("<TR><TD><B>Tax</B></TD><TD ALIGN=\"RIGHT\"><B>$" + ((double)cartModel.getSC_TAX())/100.0 +"</B></TD></TR>");
        out.println("<TR><TD><B>Shipping &amp; Handling</B></TD><TD ALIGN=\"RIGHT\"><B>$" + ((double)cartModel.getSC_SHIP_COST())/100.0 + "</B></TD></TR>");
        out.println("<TR><TD><B>Total</B></TD><TD ALIGN=\"RIGHT\"><B>$" + ((double)cartModel.getSC_TOTAL())/100.0 +"</B></TD></TR></TABLE>");
        
        //
        //Credit Card Stuff
        out.println("<HR><P><BR></P>");
        
        out.println("<TABLE BORDER=\"1\" CELLPADDING=\"5\" "+ "CELLSPACING=\"0\"><TR>");
        out.println("<TD>Credit Card Type</TD><TD>");
		out.println("<INPUT TYPE=\"RADIO\" NAME=\"CC_TYPE\" VALUE=\"1\" CHECKED=\"CHECKED\">" + Constants.CARD_TYPES[0]);
        for (int i = 2; i <= Constants.NUM_CARD_TYPES; i++) {
        	out.println("<INPUT TYPE=\"RADIO\" NAME=\"CC_TYPE\" VALUE=\"" + i + "\">" + Constants.CARD_TYPES[i-1]);
        }
        out.println("</TD></TR>");
        
        out.println("<TR><TD>Name on Credit Card</TD>");
        out.println("<TD><INPUT NAME=\"CC_NAME\" SIZE=\"30\" VALUE=\"\"></TD></TR>");
        out.println("<TR><TD>Credit Card Number</TD>");
        out.println("<TD><INPUT NAME=\"CC_NUMBER\" SIZE=\"16\" VALUE=\"\"></TD></TR>");
        out.println("<TR><TD>Card Expiration Date (mm/dd/yyyy)</TD>");
        out.println("<TD><INPUT NAME=\"CC_EXPIRY\" SIZE=\"10\" VALUE=\"\"></TD></TR>");
        
        out.println("<TR><TD>Shipping Method</TD><TD>");
        out.println("<INPUT TYPE=\"RADIO\" NAME=\"SHIPPING\" VALUE=\"1\" CHECKED=\"CHECKED\">" + Constants.SHIP_TYPES[0]);
		for (int i = 2; i <= Constants.NUM_SHIP_TYPES; i++) {
			out.println("<INPUT TYPE=\"RADIO\" NAME=\"SHIPPING\" VALUE=\"" + i + "\">" + Constants.SHIP_TYPES[i-1]);
		}
        out.println("</TD></TR></TABLE><P><CENTER>");
        
        out.println("<INPUT TYPE=\"IMAGE\" NAME=\"Submit\" SRC=\"images/submit_B.gif\">");
        out.print("<A HREF=\"");
	out.print(response.encodeURL("cart"));
	out.println("\"><IMG SRC=\"images/shopping_cart_B.gif\" ALT=\"Shopping Cart\"></A>");
	out.print("<A HREF=\"");
	out.print(response.encodeURL("home"));
	out.println("\"><IMG SRC=\"images/home_B.gif\" ALT=\"Home\"></A>");
        out.println("</P></CENTER>");
    }

	protected String getTitle() {
		return "Buy Request Page";
	}
}
