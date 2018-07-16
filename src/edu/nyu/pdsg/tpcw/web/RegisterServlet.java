package edu.nyu.pdsg.tpcw.web;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nyu.pdsg.tpcw.util.Constants;

/**
 * Register Servlet.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class RegisterServlet extends HomeServlet {
    
	protected void serveRequest(HttpServletRequest req, HttpServletResponse  response, PrintWriter out) throws ServletException {

	    out.print("<FORM ACTION=\"");
	    out.print(response.encodeURL("buy"));
	    out.println("\" METHOD=\"POST\">");
        out.println("<BLOCKQUOTE><BLOCKQUOTE><HR><TABLE BORDER=\"0\"><TR>");
        out.println("<TD><INPUT CHECKED=\"CHECKED\" NAME=\"RETURNING_FLAG\" TYPE=\"radio\" VALUE=\"Y\">I am an existing customer");
        out.println("</TD></TR><TR><TD>\n");
        out.println("<INPUT NAME=\"RETURNING_FLAG\" TYPE=\"radio\" VALUE=\"N\">I am a first time customer</TD></TR></TABLE>");
        out.println("<HR><P><B>If you're an existing customer, enter your User ID and Password:</B><BR><BR></P>");
        out.println("<TABLE><TR ALIGN=\"left\">");
        out.println("<TD>User ID: <INPUT NAME=\"UNAME\" SIZE=\"23\"></TD></TR>");
        out.println("<TR ALIGN=\"left\">");
        out.println("<TD>Password: <INPUT SIZE=\"14\" NAME=\"PASSWD\" TYPE=\"password\"></TD></TR></TABLE>");
        out.println("<HR><P><B>If you are a first time customer, enter the details below:</B><BR></P>");
        out.println("<TABLE><TR><TD>Enter your birth date (mm/dd/yyyy):</TD>");
        out.println("<TD> <INPUT NAME=\"BIRTHDATE\" SIZE=\"10\"></TD></TR>");
        out.println("<TR><TD>Enter your First Name:</TD>");
        out.println("<TD> <INPUT NAME=\"FNAME\" SIZE=\"15\"></TD></TR>");
        out.println("<TR><TD>Enter your Last Name:</TD>");
        out.println("<TD><INPUT NAME=\"LNAME\" SIZE=\"15\"></TD></TR>");
        out.println("<TR><TD>Enter your Address 1:</TD>");
        out.println("<TD><INPUT NAME=\"STREET1\" SIZE=\"40\"></TD></TR>");
        out.println("<TR><TD>Enter your Address 2:</TD>");
        out.println("<TD> <INPUT NAME=\"STREET2\" SIZE=\"40\"></TD></TR>");
        out.println("<TR><TD>Enter your City, State, Zip:</TD>");
        out.println("<TD><INPUT NAME=\"CITY\" SIZE=\"30\"><INPUT NAME=\"STATE\"><INPUT NAME=\"ZIP\" SIZE=\"10\"></TD></TR>");
        out.println("<TR><TD>Enter your Country:</TD>");

        out.println("<TD><SELECT NAME=\"CO_ID\">");
        for (int i = 1; i <=Constants.NUM_COUNTRIES; i++) {
        	out.println("<OPTION VALUE=\"" + i + "\">" + Constants.COUNTRIES[i-1] + "</OPTION>");
        }
		out.println("</SELECT></TD></TR>");
        
        out.println("<TR><TD>Enter your Phone:</TD>");
        out.println("<TD><INPUT NAME=\"PHONE\" SIZE=\"16\"></TD></TR>");
        out.println("<TR><TD>Enter your E-mail:</TD>");
        out.println("<TD> <INPUT NAME=\"EMAIL\" SIZE=\"50\"></TD></TR></TABLE>");        
        out.println("<HR><TABLE><TR><TD COLSPAN=\"2\">Special Instructions:");
        out.println("<TEXTAREA COLS=\"65\" NAME=\"DATA\" ROWS=\"4\"></TEXTAREA></TD></TR></TABLE></BLOCKQUOTE></BLOCKQUOTE><CENTER>");
        out.println("<INPUT TYPE=\"IMAGE\" NAME=\"Enter Order\" SRC=\"images/submit_B.gif\">");
        out.print("<A HREF=\"");
	out.print(response.encodeURL("search"));
	out.println("\"><IMG SRC=\"images/search_B.gif\" ALT=\"Search Item\"></A>");        
        out.print("<A HREF=\"");
	out.print(response.encodeURL("home"));
	out.println("\"><IMG SRC=\"images/home_B.gif\" ALT=\"Home\"></A>");
        out.println("</CENTER></FORM>");
    }

	protected String getTitle() {
		return "Customer Registration Page";
	}
}
