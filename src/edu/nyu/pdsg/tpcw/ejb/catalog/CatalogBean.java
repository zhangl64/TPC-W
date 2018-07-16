package edu.nyu.pdsg.tpcw.ejb.catalog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import edu.nyu.pdsg.tpcw.ejb.author.Author;
import edu.nyu.pdsg.tpcw.ejb.author.AuthorHome;
import edu.nyu.pdsg.tpcw.ejb.author.AuthorModel;
import edu.nyu.pdsg.tpcw.ejb.item.Item;
import edu.nyu.pdsg.tpcw.ejb.item.ItemAuthorModel;
import edu.nyu.pdsg.tpcw.ejb.item.ItemHome;
import edu.nyu.pdsg.tpcw.ejb.item.ItemModel;
import edu.nyu.pdsg.tpcw.ejb.item.ShortItemAuthorModel;

/**
 * Catalog bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public class CatalogBean implements SessionBean {

	private SessionContext ctx;

	private ItemHome itemHome;
	private AuthorHome authorHome;
	private DataSource datasource;
	
	public void ejbCreate()  throws CreateException {
		try {
			InitialContext initial = new InitialContext();
			Object objref = initial.lookup("java:comp/env/ejb/Item");
			itemHome = (ItemHome)PortableRemoteObject.narrow(objref, ItemHome.class);
			
			objref = initial.lookup("java:comp/env/ejb/Author");
			authorHome = (AuthorHome)PortableRemoteObject.narrow(objref, AuthorHome.class);

			datasource = (DataSource)initial.lookup("java:comp/env/jdbc/TPCWDS");
		} catch (Exception ex) {
			throw new EJBException(ex.getMessage());
		}
	}
	
	//--------------------------- business methods ----------------------------

	public Vector itemFindRelated(Integer itemId) {
		Vector v = new Vector();
		Integer i;
		Item item;
		try {
			item = itemHome.findByPrimaryKey(itemId);

			i = item.getI_RELATED1();
			v.add(i);
			v.add(itemHome.findByPrimaryKey(i).getI_THUMBNAIL());

			i = item.getI_RELATED2();
			v.add(i);
			v.add(itemHome.findByPrimaryKey(i).getI_THUMBNAIL());

			i = item.getI_RELATED3();
			v.add(i);
			v.add(itemHome.findByPrimaryKey(i).getI_THUMBNAIL());

			i = item.getI_RELATED4();
			v.add(i);
			v.add(itemHome.findByPrimaryKey(i).getI_THUMBNAIL());

			i = item.getI_RELATED5();
			v.add(i);
			v.add(itemHome.findByPrimaryKey(i).getI_THUMBNAIL());

		} catch (Exception e) {
			throw new EJBException(e);
		}
		return v;
	}

	/**
	 * Returns the set (max `limit`) of items with the author's last name starting with the specified string.
	 * Returns collections of ShortItemModel's.
	 * 
	 * @param author  author'a last name
	 * @param limit  max number of results
	 */
	public Collection searchItemsByAuthor(String author, int limit) {
		ArrayList coll = new ArrayList();
		Connection con = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			con = datasource.getConnection();
			String s = "SELECT item.i_id FROM item,author WHERE item.i_a_id = author.a_id AND author.a_lname LIKE ? ORDER BY item.i_title ASC LIMIT ?";
			prepStmt = con.prepareStatement(s);
			prepStmt.setString(1, author + "%");
			prepStmt.setInt(2, limit);
			rs = prepStmt.executeQuery();

			while(rs.next()) {
				Integer i_id = new Integer(rs.getInt(1));
				ShortItemAuthorModel item = itemHome.findByPrimaryKey(i_id).getShortModel();
				Integer a_id = item.getI_A_ID();
				Author a = authorHome.findByPrimaryKey(a_id);
				item.setA_FNAME(a.getA_FNAME());
				item.setA_LNAME(a.getA_LNAME());
				coll.add(item);
			}
		} catch(Exception  ex) {
			throw new EJBException(ex);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {}
			try {
				prepStmt.close();
			} catch (Exception e) {}
			try {
				con.close();
			} catch (Exception e) {}
		}
		return coll;
	}

	/**
	 * Returns the set (max `limit`) of items with the title containing the specified string.
	 * Returns collections of ShortItemModel's.
	 * 
	 * @param title  title keyword
	 * @param limit  max number of results
	 */
	public Collection searchItemsByTitle(String title, int limit) {
		ArrayList coll = new ArrayList();
		try {
			Collection c = itemHome.findByTitle("%" + title + "%", limit);
			Author author;
			Integer _id;
			ShortItemAuthorModel model;
			Iterator it = c.iterator();
			while (it.hasNext()) {
				model = ((Item)it.next()).getShortModel();
				_id = model.getI_A_ID();
				author = authorHome.findByPrimaryKey(_id);
				model.setA_FNAME(author.getA_FNAME());
				model.setA_LNAME(author.getA_LNAME());
				coll.add(model);
			}
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return coll;
	}

	/**
	 * Returns the set (max `limit`) of items with the subject equal to the specified string. 
	 * Returns collections of ShortItemModel's.
	 * 
	 * @param subject  desired subject
	 * @param limit   max number of results
	 */
	public Collection searchItemsBySubject(String subject, int limit) {
		ArrayList coll = new ArrayList();
		try {
			Collection c = itemHome.findBySubject(subject, limit);
			Author author;
			Integer _id;
			ShortItemAuthorModel model;
			Iterator it = c.iterator();
			while (it.hasNext()) {
				model = ((Item)it.next()).getShortModel();
				_id = model.getI_A_ID();
				author = authorHome.findByPrimaryKey(_id);
				model.setA_FNAME(author.getA_FNAME());
				model.setA_LNAME(author.getA_LNAME());
				coll.add(model);
			}
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return coll;
	}

	/**
	 * Returns the set of (max `limit`) items with the latest publication date (I_PUB_DATE)
	 * and the specified subject. 
	 * Returns collections of ShortItemModel's.
	 * 
	 * @param subject  desired subject
	 * @param limit   max number of results
	 */
	public Collection searchNewItemsBySubject(String subject, int limit) {
		ArrayList coll = new ArrayList();
		try {
			Collection c = itemHome.findNewBySubject(subject, limit);
			Author author;
			Integer _id;
			ShortItemAuthorModel model;
			Iterator it = c.iterator();
			while (it.hasNext()) {
				model = ((Item)it.next()).getShortModel();
				_id = model.getI_A_ID();
				author = authorHome.findByPrimaryKey(_id);
				model.setA_FNAME(author.getA_FNAME());
				model.setA_LNAME(author.getA_LNAME());
				coll.add(model);
			}
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return coll;
	}

	/**
	 * Returns the set of (max 'limit') items with the specified subject, such that they were 
	 * bought in recent 'numRecentOrders' orders, ordered by the total quantity bought (DESC).
	 * Returns collections of ShortItemModel's.
	 * 
	 * @param subject  desired subject
	 * @param numRecentOrders  number of most recent orders to search through
	 * @param limit   max number of results
	 */
	public Collection searchRecentlyPurchasedItemsBySubject(String subject, int numRecentOrders, int limit) {
		ArrayList coll = new ArrayList();
		Connection con = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		String s;
		try {
			con = datasource.getConnection();
			//s = "SELECT i_id,SUM(ol_qty) AS val FROM (SELECT o_id FROM orders ORDER BY o_date DESC LIMIT ?) as recent_orders,order_line,item WHERE recent_orders.o_id = ol_o_id AND ol_i_id = i_id AND i_subject = ? GROUP BY i_id ORDER BY val DESC LIMIT ?";
            s = "SELECT i_id, SUM(ol_qty) AS val FROM recent_orders JOIN order_line ON (recent_orders.o_id = ol_o_id) JOIN item ON (ol_i_id = i_id) WHERE i_subject = ? GROUP BY i_id ORDER BY val DESC LIMIT ?";
			prepStmt = con.prepareStatement(s);
			//prepStmt.setInt(1, numRecentOrders);
			prepStmt.setString(1, subject);
			prepStmt.setInt(2, limit);
			rs = prepStmt.executeQuery();

			while(rs.next()) {
				Integer i_id = new Integer(rs.getInt(1));
				ShortItemAuthorModel item = itemHome.findByPrimaryKey(i_id).getShortModel();
				Integer a_id = item.getI_A_ID();
				Author a = authorHome.findByPrimaryKey(a_id);
				item.setA_FNAME(a.getA_FNAME());
				item.setA_LNAME(a.getA_LNAME());
				coll.add(item);
			}
		} catch(Exception  ex) {
			throw new EJBException(ex);
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {}
			try {
				if (prepStmt != null) prepStmt.close();
			} catch (Exception e) {}
			try {
				if (con != null) con.close();
			} catch (Exception e) {}
		}
		return coll;
	}

	/**
	 * 
	 * Returns full utem and author information.
	 * 
	 */
	public ItemAuthorModel getItemAuthor(Integer i) {
		try {
			ItemModel model = itemHome.findByPrimaryKey(i).getItem();
			ItemAuthorModel result = new ItemAuthorModel(model);
			Integer a_id = model.getI_A_ID();
			AuthorModel a = authorHome.findByPrimaryKey(a_id).getAuthor();
			result.setA_FNAME(a.getA_FNAME());
			result.setA_LNAME(a.getA_LNAME());
			return result;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	/**
	 * 
	 * Returns utem model for adding to the shopping cart, with only a limited number of fields filled:
	 * I_ID, I_COST, I_SRP, I_TITLE, I_BACKING.
	 * 
	 */
	public ItemModel getItemForCart(Integer i) {
		ItemModel itemModel = new ItemModel(i);
		try{
			Item item = itemHome.findByPrimaryKey(i);
			itemModel.setI_COST(item.getI_COST());
			itemModel.setI_SRP(item.getI_SRP());
			itemModel.setI_TITLE(item.getI_TITLE());
			itemModel.setI_BACKING(item.getI_BACKING());
		} catch (Exception re) {
			throw new EJBException(re);
		}
		return itemModel;
	}
	
	/**
	 * 
	 * Reduces the quantity of the item.
	 * 
	 * @param id   I_ID of the item
	 * @param qty  qty to reduce by
	 */
	public void reduceItemQty(Integer id, int qty) {
		try {
			Item item = itemHome.findByPrimaryKey(id);
			int q = item.getI_STOCK().intValue();
			item.setI_STOCK(new Integer(q - qty));
		} catch (Exception re) {
			throw new EJBException(re);
		}
	}

	//-------------------------- utility methods ------------------------------

	public void setSessionContext(SessionContext context) {
		this.ctx = context;
	}

	public void unsetSessionContext() {
		ctx = null;
	}
	
	public void ejbActivate() {
		try {
			InitialContext initial = new InitialContext();
			Object objref = initial.lookup("java:comp/env/ejb/Item");
			itemHome = (ItemHome)PortableRemoteObject.narrow(objref, ItemHome.class);
			
			objref = initial.lookup("java:comp/env/ejb/Author");
			authorHome = (AuthorHome)PortableRemoteObject.narrow(objref, AuthorHome.class);

			datasource = (DataSource)initial.lookup("java:comp/env/jdbc/TPCWDS");
		} catch (Exception ex) {
			throw new EJBException(ex.getMessage());
		}
	}
	
	public void ejbPassivate() {
		itemHome = null;
		authorHome = null;
		datasource = null;
	}
	
	public void ejbRemove() {}

}
