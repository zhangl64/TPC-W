package edu.nyu.pdsg.tpcw.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.nyu.pdsg.tpcw.ejb.cart.ShoppingCart;
import edu.nyu.pdsg.tpcw.ejb.cart.ShoppingCartHome;
import edu.nyu.pdsg.tpcw.ejb.catalog.Catalog;
import edu.nyu.pdsg.tpcw.ejb.catalog.CatalogHome;
import edu.nyu.pdsg.tpcw.util.Constants;
import edu.nyu.pdsg.tpcw.util.Util;

/**
 * Generic TPC-W servlet.
 * Extended by all TPC-W servlets.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public abstract class TPCWServlet extends HttpServlet {

	// reference to the Catalog
	protected Catalog catalog;
	
	public void init() throws ServletException {
		Object objref = null;
		try {
			objref = (new InitialContext()).lookup("java:comp/env/ejb/Catalog");
			catalog = ((CatalogHome)PortableRemoteObject.narrow(objref, CatalogHome.class)).create();
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}
	}
    
    public void doGet(HttpServletRequest request, HttpServletResponse  response)
                                                         throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
    	PrintWriter out = response.getWriter();

		//Generate Page Head
		out.println("<HTML><HEAD><TITLE>TPC-W " + getTitle() + "</TITLE></HEAD>");
		out.println("<BODY BGCOLOR=\"#ffffff\">");
		out.println("<H1 ALIGN=\"center\">TPC Web Commerce Benchmark (TPC-W)</H1>");
		out.println("<P ALIGN=\"CENTER\">");
		out.println("<IMG SRC=\"images/tpclogo.gif\" ALIGN=\"BOTTOM\" BORDER=\"0\" WIDTH=\"288\" HEIGHT=\"67\"></P>");
		out.println("<H2 ALIGN=\"center\">" + getTitle() + "</H2>");

		serveRequest(request, response, out);

		//Generate Trailer
		out.println("<p><hr>");
		out.println("<a href=\"http://www.tpc.org/tpcw/default.asp\">TPC-W Benchmark</a>,&nbsp;");
		out.println("<a href=\"http://www.cs.wisc.edu/~arch/uwarch\">University of Wisconsin Computer Architecture</a>,&nbsp;");
		out.println("<a href=\"http://www.cs.rice.edu/CS/Systems/DynaServer/\">Rice University DynaServer Project</a><br>");
		out.println("<a href=\"http://www.cs.nyu.edu/pdsg/\">New York University Parallel and Distributed Systems Group</a>,&nbsp;");
		out.println("<a href=\"http://www.cs.nyu.edu/totok/\">Alexander Totok</a>,&nbsp;");
		out.println("February 2004.");
		out.println("</BODY> </HTML>");
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse  response)
                                                         throws ServletException, IOException {
		doGet(request, response);
    }
    
    protected ShoppingCart getShoppingCart(ShoppingCartHome scHome, HttpSession session) throws ServletException {
    	Object obj = session.getAttribute(Constants.CART);
    	if (obj != null) return (ShoppingCart)obj;
    	else {
    		try {
    			ShoppingCart cart = scHome.create();
    			session.setAttribute(Constants.CART, cart);
    			return cart;
    		} catch (Exception ex) {
    			throw new ServletException(ex.getMessage());
    		}
    	}
    }

	protected void displayPromotions(HttpServletRequest request, HttpServletResponse  response, PrintWriter out, Random rand) throws ServletException {

		Vector v = null;
		Integer I_ID = new Integer(Util.getRandomInt(rand, 1, Constants.NUM_ITEMS));
		try {
			v = catalog.itemFindRelated(I_ID);
		} catch(RemoteException re) {
			throw new ServletException(re);
		}
        
		//Create table and "Click on our latest books..." row
		out.println("<TABLE ALIGN=CENTER BORDER=0 WIDTH=660>");
		out.println("<TR ALIGN=CENTER VALIGN=top>");
		out.println("<TD COLSPAN=5><B><FONT COLOR=#ff0000 SIZE=+1>Click on one of our latest books to find out more! ... " + I_ID + "</FONT></B></TD></TR>");
		out.println("<TR ALIGN=CENTER VALIGN=top>");
        
		//Create links and references to book images
		for (int i = 0; i < v.size(); i+=2) {
		        out.print("<TD><A HREF=\"");
			out.print(response.encodeURL("item?I_ID=" + v.elementAt(i)));
			out.println("\"><IMG SRC=\"" + v.elementAt(i+1) + "\"></A></TD>");
		}
		out.println("</TR></TABLE>");
	}

	/**
	 * Main method of the servlet. Does all the job and prints output except for header and footer.
	 */    
    protected abstract void serveRequest(HttpServletRequest request, HttpServletResponse  response, PrintWriter out) throws ServletException;

	/**
	 * Returns the title of the servlet. Defines by subclasses.
	 */    
    protected abstract String getTitle();

}
