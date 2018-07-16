package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Search Request Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class SearchRequestServlet extends TPCWServlet {
    
	// for promotion generation
	protected Random rand;

	public void init() throws ServletException {
		rand = new Random(System.currentTimeMillis());
		super.init();
	}
    
    protected void serveRequest(HttpServletRequest req, HttpServletResponse  response, PrintWriter out) throws ServletException {

        // Insert Promotional processing
        displayPromotions(req, response, out, rand);
        
        out.print("<FORM ACTION=\"");
	out.print(response.encodeURL("execute_search"));
	out.println("\" METHOD=\"GET\">");
        out.println("<TABLE ALIGN=\"center\"><TR><TD ALIGN=\"right\">");
        out.println("<H3>Search by:</H3></TD><TD WIDTH=\"100\"></TD></TR>");
        out.println("<TR><TD ALIGN=\"right\">");
        out.println("<SELECT NAME=\"search_type\" SIZE=\"1\">");
        out.println("<OPTION SELECTED=\"SELECTED\" VALUE=\"author\">Author</OPTION>");
        out.println("<OPTION VALUE=\"title\">Title</OPTION>");
        out.println("<OPTION VALUE=\"subject\">Subject</OPTION></SELECT></TD>");
        out.println("<TD><INPUT NAME=\"search_string\" SIZE=\"30\"></TD></TR>");
        out.println("</TABLE>");
        out.println("<P ALIGN=\"CENTER\"><CENTER>");
        out.println("<INPUT TYPE=\"IMAGE\" NAME=\"Search\" SRC=\"images/submit_B.gif\">");
        out.print("<A HREF=\"");
	out.print(response.encodeURL("home"));
	out.println("\"><IMG SRC=\"images/home_B.gif\" ALT=\"Home\"></A>");
        out.print("<A HREF=\"");
	out.print(response.encodeURL("cart"));
	out.println("\"><IMG SRC=\"images/shopping_cart_B.gif\" ALT=\"Shopping Cart\"></A>");
        out.println("</CENTER></P></FORM>");
    }

	protected String getTitle() {
		return "Search Request Page";
	}

}
