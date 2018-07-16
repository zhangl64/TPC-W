package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nyu.pdsg.tpcw.ejb.address.AddressModel;
import edu.nyu.pdsg.tpcw.ejb.order.BigOrderLineModel;
import edu.nyu.pdsg.tpcw.ejb.order.BigOrderModel;

/**
 * Order Display Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class OrderDisplayServlet extends BuyServlet {
    
	protected void serveRequest(HttpServletRequest req, HttpServletResponse  response, PrintWriter out) throws ServletException {
        
        out.println("<HR>");
        
        String C_UNAME = req.getParameter("UNAME");
        String C_PASSWD = req.getParameter("PASSWD");
        BigOrderModel order = null;

        if (C_UNAME == null || C_UNAME.equals("")) {
			out.println("<h2><font color=\"red\">You have not specified USERNAME ...</font></h2>");
			return;
        }
        
        try {
        	order = adminPortal.getLastOrder(C_UNAME, C_PASSWD);
        } catch (RemoteException re) {
        	throw new ServletException(re);
        }
        
        if (order == null) {
			out.println("<h2><font color=\"red\">Authentication failed ...</font></h2>");
			return;
        } else if (order.isEmpty()) {
			out.println("<h2>You have not placed any orders yet.</h2>");
			return;
        }

		// print out thr order
		out.println("<P>Order ID: " + order.getO_ID() + "<BR>");
		out.println("Authorization ID: " + order.getCX_AUTH_ID() + "<BR>");
		out.println("Order Placed on " + order.getO_DATE() + "<BR>");
		out.println("Shipping Type: " + order.getO_SHIP_TYPE() + "<BR>");
		out.println("Ship Date: " + order.getO_SHIP_DATE() + "<BR>");
		out.println("Order Subtotal: " + order.getO_SUB_TOTAL() + "<BR>");
		out.println("Order Tax: " + order.getO_TAX() + "<BR>");
		out.println("Order Total:" + order.getO_TOTAL() + "<BR></P>");
		
		AddressModel billAddress = order.getBILL_ADDR();
		AddressModel shipAddress = order.getSHIP_ADDR();
        
		out.println("<TABLE BORDER=\"0\" WIDTH=\"90%\">");
		out.println("<TR><TD><B>Bill To:</B></TD><TD><B>Ship To:</B></TD></TR>");
		out.println("<TR><TD COLSPAN=\"2\"><H4>"+ order.getC_FNAME() + " " + order.getC_LNAME() + "</H4></TD></TR>");
		out.println("<TR><TD WIDTH=\"50%\"><ADDRESS>" + billAddress.getADDR_STREET1() + "<BR>");
		out.println(billAddress.getADDR_STREET2() + "<BR>");
		out.println(billAddress.getADDR_STATE() + " " + billAddress.getADDR_ZIP() + "<BR>");
		out.println(order.getBILL_CO_NAME() + "<BR><BR>");
		out.println("Email: " + order.getC_EMAIL() + "<BR>");
		out.println("Phone: " + order.getC_PHONE() +"</ADDRESS><BR><P>");
		out.println("Credit Card Type: " + order.getCX_TYPE() + "<BR>");
		out.println("Order Status: " + order.getO_STATUS() +"</P></TD>");
		out.println("<TD VALIGN=\"TOP\" WIDTH=\"50%\"><ADDRESS>" + shipAddress.getADDR_STREET1() + "<BR>");
		out.println(shipAddress.getADDR_STREET2() + "<BR>");
		out.println(shipAddress.getADDR_STATE() + " " + shipAddress.getADDR_ZIP() + "<BR>");
		out.println(order.getSHIP_CO_NAME());
		out.println("</ADDRESS></TD></TR></TABLE><P>");
        
		//Print out the list of items
		out.println("<CENTER><TABLE BORDER=\"1\" CELLPADDING=\"5\" CELLSPACING=\"0\">");
		out.println("<TR><TD><H4>Item #</H4></TD>");
		out.println("<TD><H4>Title</H4></TD>");
		out.println("<TD> <H4>Cost</H4></TD>");
		out.println("<TD> <H4>Qty</H4></TD>");
		out.println("<TD> <H4>Discount</H4></TD>");
		out.println("<TD> <H4>Comment</H4></TD></TR>");
		
		Iterator orderLines = order.getLineItems().iterator();
        
		while (orderLines.hasNext()) {
			BigOrderLineModel line = (BigOrderLineModel)orderLines.next();
			out.println("<TR><TD><H4>" + line.getI_ID() + "</H4></TD>");
			out.println("<TD VALIGN=\"top\"><H4>" + line.getI_TITLE() + "<BR>Publisher: " + line.getI_PUBLISHER() + "</H4></TD>");
			out.println("<TD><H4>" + line.getI_COST() + "</H4></TD>");
			out.println("<TD><H4>" + line.getOL_QTY() + "</H4></TD>");
			out.println("<TD><H4>" + line.getOL_DISCOUNT() + "</H4></TD>");
			out.println("<TD><H4>" + line.getOL_COMMENTS() + "</H4></TD></TR>");
		}
		out.println("</TABLE><BR></CENTER>");
        
		out.print("<CENTER><A HREF=\"");
		out.print(response.encodeURL("search"));
		out.println("\"><IMG SRC=\"images/search_B.gif\" ALT=\"Search\"></A>");
		out.print("<A HREF=\"");
		out.print(response.encodeURL("home"));
		out.println("\"><IMG SRC=\"images/home_B.gif\" ALT=\"Home\"></A></P></CENTER>");
    }
    
	protected String getTitle() {
		return "Order Display Page";
	}
    
}
