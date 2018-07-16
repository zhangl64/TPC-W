package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.nyu.pdsg.tpcw.util.Constants;

/**
 * Order Inquiry Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class OrderInquiryServlet extends BuyServlet {
    
	protected void serveRequest(HttpServletRequest req, HttpServletResponse  response, PrintWriter out) throws ServletException {
		
		HttpSession session = req.getSession();
		Object obj = session.getAttribute(Constants.C_UNAME);
		String C_UNAME = null;
		if (obj != null) {
			C_UNAME = (String)obj;
		}
      
		out.print("<FORM ACTION=\"");
		out.print(response.encodeURL("order_display"));
		out.println("\" METHOD=\"POST\">");
      	out.println("<TABLE ALIGN=\"CENTER\">"); 
      	out.println("<TR><TD><H4>Username:</H4></TD>"); 
      	out.println("<TD><INPUT NAME=\"UNAME\" VALUE=\"" + (C_UNAME == null? "" : C_UNAME) + "\" SIZE=\"23\"></TD></TR>"); 
      	out.println("<TR><TD><H4>Password:</H4></TD>"); 
      	out.println("<TD><INPUT NAME=\"PASSWD\" SIZE=\"14\" TYPE=\"password\"></TD>");
      	out.println("</TR></TABLE> <P ALIGN=\"CENTER\"><CENTER>");

      	out.println("<INPUT TYPE=\"IMAGE\" NAME=\"Display Last Order\" SRC=\"images/display_last_order_B.gif\">");
    	out.print("<A HREF=\"");
	out.print(response.encodeURL("search"));
	out.println("\"><IMG SRC=\"images/search_B.gif\" ALT=\"Search\"></A>");
      	out.print("<A HREF=\"");
	out.print(response.encodeURL("home"));
	out.println("\"><IMG SRC=\"images/home_B.gif\" ALT=\"Home\"></A></P></CENTER>");
      	out.println("</CENTER></FORM>");		
    }

	protected String getTitle() {
		return "Order Inquiry Page";
	}
}
