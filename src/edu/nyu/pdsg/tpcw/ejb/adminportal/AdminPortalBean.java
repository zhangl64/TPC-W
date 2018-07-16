package edu.nyu.pdsg.tpcw.ejb.adminportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import edu.nyu.pdsg.tpcw.ejb.address.Address;
import edu.nyu.pdsg.tpcw.ejb.address.AddressHome;
import edu.nyu.pdsg.tpcw.ejb.address.AddressModel;
import edu.nyu.pdsg.tpcw.ejb.author.Author;
import edu.nyu.pdsg.tpcw.ejb.author.AuthorHome;
import edu.nyu.pdsg.tpcw.ejb.cart.CartItem;
import edu.nyu.pdsg.tpcw.ejb.country.CountryHome;
import edu.nyu.pdsg.tpcw.ejb.country.CountryModel;
import edu.nyu.pdsg.tpcw.ejb.customer.BigCustomerModel;
import edu.nyu.pdsg.tpcw.ejb.customer.Customer;
import edu.nyu.pdsg.tpcw.ejb.customer.CustomerHome;
import edu.nyu.pdsg.tpcw.ejb.customer.CustomerModel;
import edu.nyu.pdsg.tpcw.ejb.item.Item;
import edu.nyu.pdsg.tpcw.ejb.item.ItemAuthorModel;
import edu.nyu.pdsg.tpcw.ejb.item.ItemHome;
import edu.nyu.pdsg.tpcw.ejb.item.ItemModel;
import edu.nyu.pdsg.tpcw.ejb.order.BigOrderLineModel;
import edu.nyu.pdsg.tpcw.ejb.order.BigOrderModel;
import edu.nyu.pdsg.tpcw.ejb.order.Order;
import edu.nyu.pdsg.tpcw.ejb.order.OrderHome;
import edu.nyu.pdsg.tpcw.ejb.order.OrderLine;
import edu.nyu.pdsg.tpcw.ejb.order.OrderLineHome;
import edu.nyu.pdsg.tpcw.ejb.order.OrderLineModel;
import edu.nyu.pdsg.tpcw.ejb.order.OrderModel;
import edu.nyu.pdsg.tpcw.ejb.xact.Cc_xactHome;
import edu.nyu.pdsg.tpcw.ejb.xact.Cc_xactModel;
import edu.nyu.pdsg.tpcw.util.Constants;
import edu.nyu.pdsg.tpcw.util.Util;

/**
 * Admin portal session bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public class AdminPortalBean implements SessionBean {

	private SessionContext ctx;

	private CustomerHome custHome;
	private AddressHome addressHome;
	private CountryHome countryHome;
	private OrderHome orderHome;
	private OrderLineHome orderLineHome;
	private Cc_xactHome xactHome;
	private ItemHome itemHome;
	private AuthorHome authorHome;
	private DataSource datasource;
	
	private Random rand;
	
	public void ejbCreate()  throws CreateException {
		try {
			InitialContext initial = new InitialContext();
			Object objref = initial.lookup("java:comp/env/ejb/Customer");
			custHome = (CustomerHome)PortableRemoteObject.narrow(objref, CustomerHome.class);

			objref = initial.lookup("java:comp/env/ejb/Address");
			addressHome = (AddressHome)PortableRemoteObject.narrow(objref, AddressHome.class);

			objref = initial.lookup("java:comp/env/ejb/Country");
			countryHome = (CountryHome)PortableRemoteObject.narrow(objref, CountryHome.class);
			
			objref = initial.lookup("java:comp/env/ejb/Order");
			orderHome = (OrderHome)PortableRemoteObject.narrow(objref, OrderHome.class);
			
			objref = initial.lookup("java:comp/env/ejb/OrderLine");
			orderLineHome = (OrderLineHome)PortableRemoteObject.narrow(objref, OrderLineHome.class);
			
			objref = initial.lookup("java:comp/env/ejb/Cc_xact");
			xactHome = (Cc_xactHome)PortableRemoteObject.narrow(objref, Cc_xactHome.class);

			objref = initial.lookup("java:comp/env/ejb/Item");
			itemHome = (ItemHome)PortableRemoteObject.narrow(objref, ItemHome.class);

			objref = initial.lookup("java:comp/env/ejb/Author");
			authorHome = (AuthorHome)PortableRemoteObject.narrow(objref, AuthorHome.class);

			datasource = (DataSource)initial.lookup("java:comp/env/jdbc/TPCWDS");
			
			rand = new Random(System.currentTimeMillis());
		} catch (Exception ex) {
			throw new EJBException(ex.getMessage());
		}
	}
	
	//--------------------------- business methods ----------------------------

	/**
	 * Authenticates a returning customer.
	 * @param uname UNAME of the customer
	 * @param passwd PASSWD of the customer
	 * @return  the Big Customer Model object (with address and country name fields), 
	 * if authentication is successful, null otherwise.
	 */
	public BigCustomerModel authenticate(String uname, String passwd) {
		BigCustomerModel bigModel = null;
		try {

			// find Collection of customers with this C_UNAME
			// it should be actually one or none of them
			Collection coll = custHome.findByC_UNAME(uname);
			if (coll.isEmpty()) return null; // fail if no such customers

			// get the CustomerModel object
			Customer customer = (Customer)coll.iterator().next();
			CustomerModel model = customer.getCustomer();

			// check the password
			if (!model.getC_PASSWD().equals(passwd)) return null; // fail if passwds do not match

			// update C_LOGIN and C_EXPIRATION
			Date now = new Date();
			customer.setC_LOGIN(now);
			customer.setC_EXPIRATION(new Date(now.getTime() + 7200000));
			
			// create BigCustomerModel object
			// get fields from the Customer table
			bigModel = new BigCustomerModel(model.getC_ID(), model.getC_UNAME(), model.getC_PASSWD(), 
								model.getC_FNAME(), model.getC_LNAME(), model.getC_ADDR_ID(), 
								model.getC_PHONE(), model.getC_EMAIL(), model.getC_DISCOUNT(), 
								model.getC_BIRTHDATE(), model.getC_DATA());

			// get fields from the ADDRESS table
			Integer C_ADDR_ID = model.getC_ADDR_ID();
			AddressModel address = addressHome.findByPrimaryKey(C_ADDR_ID).getAddress();
			bigModel.setADDR_STREET1(address.getADDR_STREET1());
			bigModel.setADDR_STREET2(address.getADDR_STREET2());
			bigModel.setADDR_CITY(address.getADDR_CITY());
			bigModel.setADDR_STATE(address.getADDR_STATE());
			bigModel.setADDR_ZIP(address.getADDR_ZIP());
			bigModel.setCO_ID(address.getADDR_CO_ID());

			// get fields from the Country table
			Integer CO_ID = address.getADDR_CO_ID();
			CountryModel country = countryHome.findByPrimaryKey(CO_ID).getCountry();
			bigModel.setCO_NAME(country.getCO_NAME());			
		} catch (Exception re) {
			throw new EJBException(re);
		}
		return bigModel;
	}
	
	/**
	 * Creates a new Customer according to the spec. clause 2.6.3.2
	 * @return  the Big Customer Model object (with adress and coubtry name fields)
	 */
	public BigCustomerModel createNewCustomer(String C_FNAME, String C_LNAME, String ADDR_STREET1, 
							String ADDR_STREET2, String ADDR_CITY, String ADDR_STATE, String ADDR_ZIP, Integer CO_ID, 
							String C_PHONE, String C_EMAIL, Date C_BIRTHDATE, String C_DATA) {
		BigCustomerModel bigModel = null;
		try {
			// create a new address entry in the database
			Address address = addressHome.create(ADDR_STREET1, ADDR_STREET2,
							ADDR_CITY, ADDR_STATE, ADDR_ZIP, CO_ID);
			AddressModel addressModel = address.getAddress();
			
			CountryModel countryModel = countryHome.findByPrimaryKey(CO_ID).getCountry();
						
			// create a new cutomer entry in the database;
			// note that we don't know C_UNAME and C_PASSWD yet, because
			// according to the spec they depend on the C_ID
			Date now = new Date();
			Customer customer = custHome.create("", "", C_FNAME, C_LNAME,
					addressModel.getADDR_ID(), C_PHONE, C_EMAIL, now, now,
					now, new Date(now.getTime() + 7200000), 
					new Double(((double)Util.getRandomInt(rand, 0, 50))/100.0), new Double(0.0), 
					new Double(0.0), C_BIRTHDATE, C_DATA);
			CustomerModel customerModel = customer.getCustomer();
			
			// fill if the missing C_UNAME and C_PASSWD
			String C_UNAME = Util.DigSyl(rand, customerModel.getC_ID().intValue(), 0);
			String C_PASSWD = C_UNAME.toLowerCase();
			customer.setC_UNAME(C_UNAME);
			customer.setC_PASSWD(C_PASSWD);
					
			// create BigCustomerModel and populate it
			bigModel = new BigCustomerModel(customerModel.getC_ID(), C_UNAME, C_PASSWD, 
								C_FNAME, C_LNAME, customerModel.getC_ADDR_ID(), 
								C_PHONE, C_EMAIL, customerModel.getC_DISCOUNT(), 
								C_BIRTHDATE, C_DATA);

			// get fields from the ADDRESS table
			bigModel.setADDR_STREET1(ADDR_STREET1);
			bigModel.setADDR_STREET2(ADDR_STREET2);
			bigModel.setADDR_CITY(ADDR_CITY);
			bigModel.setADDR_STATE(ADDR_STATE);
			bigModel.setADDR_ZIP(ADDR_ZIP);
			bigModel.setCO_ID(CO_ID);

			// get fields from the Country table
			bigModel.setCO_NAME(countryModel.getCO_NAME());			

		} catch (Exception e) {
			throw new EJBException(e);
		}
		return bigModel;
	}

	/**
	 * Creates order in the database.
	 */
	public Integer createOrder(Integer C_ID, Iterator lineItems, int SC_SUB_TOTAL, int SC_TAX, int SC_SHIP_COST, int SC_TOTAL, int SC_C_DISCOUNT,
							String CC_TYPE, String CC_NUMBER, String CC_NAME, Date CC_EXPIRY, String SHIPPING, String STREET_1, String STREET_2, 
							String CITY, String STATE, String ZIP, Integer CO_ID) {
		Integer O_ID = null;

		try {
			// get customer record
			Customer cust = custHome.findByPrimaryKey(C_ID);
			Integer C_ADDR_ID = cust.getC_ADDR_ID();
			
			// get shipping address_id
			Integer SHIP_ADDR_ID = null;
			if (STREET_1 == null || STREET_1.equals("")) {
				// there is no separate shipping address
				SHIP_ADDR_ID = C_ADDR_ID;
			} else {
				// there is a separate shipping address
				// don't try to match to an existing address, but create a new one
				Address address = addressHome.create(STREET_1, STREET_2, CITY, STATE, ZIP, CO_ID);
				SHIP_ADDR_ID = address.getADDR_ID();
			}
			
			// generate missing fields for the order record
			Date O_SHIP_DATE = new Date(System.currentTimeMillis() + Util.getRandomInt(rand, 1, 7)*3600000*24);

			// create order record
			Order order = orderHome.create(C_ID, new Date(), new Double(((double)SC_SUB_TOTAL)/100.0), new Double(((double)SC_TAX)/100.0), new Double(((double)SC_TOTAL)/100.0), 	
									SHIPPING, O_SHIP_DATE, C_ADDR_ID, SHIP_ADDR_ID,
									"PENDING");
			O_ID = order.getO_ID();
									
			// create oderLine records
			int i = 1;
			while (lineItems.hasNext()) {
				CartItem item = (CartItem)lineItems.next();
				orderLineHome.create(new Integer(i), O_ID, item.getSCL_I_ID(), new Integer(item.getSCL_QTY()), new Double(((double)SC_C_DISCOUNT)/100.0), Util.getRandomAString(rand, 20, 100));
				i++;
			}
			
			// create CC_XACTS record
			// Note that we don't send info to PGE (clause 2.7.3.3)
			// but rather create a record right away
			xactHome.create(O_ID, CC_TYPE, CC_NUMBER, CC_NAME, CC_EXPIRY,
							Util.getRandomAString(rand, 15), new Double(((double)SC_TOTAL)/100.0), new Date(), SHIP_ADDR_ID);

            // update recent_orders
            Connection con = null;
            PreparedStatement prepStmt = null;
            try {
                con = datasource.getConnection();
                String s = "UPDATE recent_orders SET o_id = ? ORDER BY o_id LIMIT 1";
                prepStmt = con.prepareStatement(s);
                prepStmt.setInt(1, O_ID.intValue());
                int rowcnt = prepStmt.executeUpdate();
                if (rowcnt != 1) {
                    throw new Exception(s + ": rowcnt=" + rowcnt);
                }
            } finally {
                try {
                    prepStmt.close();
                } catch (Exception e) {}
                try {
                    con.close();
                } catch (Exception e) {}
            }
		} catch (Exception re) {
			throw new EJBException(re);
		}
		return O_ID;
	}

	/**
	 * Returns the last order placed by the customer, by first authenticating him.
	 * 
	 * @param C_UNAME name of the customer
	 * @param S_PASSWD password supplied by the customer
	 * @return the BigOrderModel object, if successfully authenticated; null otherwise
	 */
	public BigOrderModel getLastOrder(String C_UNAME, String C_PASSWD) {
		BigOrderModel bigOrder = null;
		CustomerModel custModel = null;
		try {
			// find Collection of customers with this C_UNAME
			// it should be actually one or none of them
			Collection coll = custHome.findByC_UNAME(C_UNAME);
			if (coll.isEmpty()) return null; // fail, if no such customers

			// get the CustomerModel object
			Customer customer = (Customer)coll.iterator().next();
			custModel = customer.getCustomer();

			// check the password
			if (!custModel.getC_PASSWD().equals(C_PASSWD)) return null; // fail if passwds do not match
		} catch (Exception e) {
			throw new EJBException(e);
		}

		// find the latest order
		Integer O_ID = null;
		Connection con = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			con = datasource.getConnection();
			String s = "SELECT max(o_id) FROM orders WHERE o_c_id = ?";
			prepStmt = con.prepareStatement(s);
			prepStmt.setInt(1, custModel.getC_ID().intValue());
			rs = prepStmt.executeQuery();
			
			boolean exist = rs.next();
			if (exist) {
				int i = rs.getInt(1);
				if (rs.wasNull()) {
					O_ID = null;
				} else {
					O_ID = new Integer(i); 
				}
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

		if (O_ID == null) {
			// no orders from this customer
			// return empty model object, which means that authentication was successfull, but no orders ware found
			return new BigOrderModel();
		}

		OrderModel orderModel = null;
		try {
			orderModel = orderHome.findByPrimaryKey(O_ID).getOrder();
		} catch (Exception e) {
			throw new EJBException(e);
		}
		
		// create BigOrderModel object and fill it with right order related info
		bigOrder = new BigOrderModel(O_ID, orderModel.getO_C_ID(), orderModel.getO_DATE(), orderModel.getO_SUB_TOTAL(), 
						orderModel.getO_TAX(), orderModel.getO_TOTAL(), orderModel.getO_SHIP_TYPE(), 
						orderModel.getO_SHIP_DATE(), orderModel.getO_STATUS());

		// set customer information fields
		bigOrder.setC_FNAME(custModel.getC_FNAME());
		bigOrder.setC_LNAME(custModel.getC_LNAME());
		bigOrder.setC_PHONE(custModel.getC_PHONE());
		bigOrder.setC_EMAIL(custModel.getC_EMAIL());

		try {
			// set billing address related info
			Integer B_O_ID = orderModel.getO_BILL_ADDR_ID();
			AddressModel billAddr = addressHome.findByPrimaryKey(B_O_ID).getAddress();
			bigOrder.setBILL_ADDR(billAddr);
			bigOrder.setBILL_CO_NAME(countryHome.findByPrimaryKey(billAddr.getADDR_CO_ID()).getCountry().getCO_NAME());

			// set shipping address related info		
			Integer S_O_ID = orderModel.getO_SHIP_ADDR_ID();
			AddressModel shipAddr = addressHome.findByPrimaryKey(S_O_ID).getAddress();
			bigOrder.setSHIP_ADDR(shipAddr);
			bigOrder.setSHIP_CO_NAME(countryHome.findByPrimaryKey(shipAddr.getADDR_CO_ID()).getCountry().getCO_NAME());
			
			// set credit card transaction related info
			Cc_xactModel xact = xactHome.findByPrimaryKey(O_ID).getCc_xact();
			bigOrder.setCX_TYPE(xact.getCX_TYPE());
			bigOrder.setCX_AUTH_ID(xact.getCX_AUTH_ID());

			// set lineItems related info
			Iterator iter = orderLineHome.findByOrderId(O_ID).iterator();
			ArrayList list = new ArrayList();
			while (iter.hasNext()) {
				OrderLineModel olm = ((OrderLine)iter.next()).getOrderLine();
				BigOrderLineModel bigOrderLine = new BigOrderLineModel(olm.getOL_ID(), olm.getOL_O_ID(), 
								olm.getOL_I_ID(), olm.getOL_QTY(), olm.getOL_DISCOUNT(), olm.getOL_COMMENTS());
				ItemModel item = itemHome.findByPrimaryKey(olm.getOL_I_ID()).getItem();
				bigOrderLine.setI_TITLE(item.getI_TITLE());
				bigOrderLine.setI_PUBLISHER(item.getI_PUBLISHER());
				bigOrderLine.setI_COST(item.getI_COST());
				list.add(bigOrderLine);
			}
			bigOrder.setLineItems(list);
		} catch (Exception e) {
			throw new EJBException(e);
		}

		return bigOrder;
	}

	/**
	 * Performs update of the specified item, as defined in the processing definition of 
	 * the Admin Confirm Web Interaction. 
	 * It DOES NOT PERFORM updates of the I_RELATED* fields, as prescribed in the clause 2.16.3.3 of the
	 * spec (version 1.8). It rather updates these values with randomly selected IDs.
	 * 
	 * @param I_ID  id of the item to update
	 * @param I_NEW_COST  item's new cost
	 * @param I_NEW_IMAGE  item's new image
	 * @param I_NEW_THUMBNAIL  item's new thumbnail
	 * @return  updated item and author info
	 */
	public ItemAuthorModel updateItem(Integer I_ID, Double I_NEW_COST, Integer I_NEW_STOCK, String I_NEW_IMAGE, String I_NEW_THUMBNAIL) {
		ItemAuthorModel model = null;
		Random rand = new Random(System.currentTimeMillis());
		int I_RELATED1, I_RELATED2, I_RELATED3, I_RELATED4, I_RELATED5;
		int i = I_ID.intValue();
		Item item = null;

		// choose I_RELATED* randomly, not as specified in the spec (clause 2.16.3.3)
		do {
			I_RELATED1 = Util.getRandomInt(rand, 1, Constants.NUM_ITEMS);
		} while(I_RELATED1 == i);
		do {
			I_RELATED2 = Util.getRandomInt(rand, 1, Constants.NUM_ITEMS);
		} while(I_RELATED2 == I_RELATED1 || I_RELATED2 == i);
		do {
			I_RELATED3 = Util.getRandomInt(rand, 1, Constants.NUM_ITEMS);
		} while(I_RELATED3 == I_RELATED1 || I_RELATED3 == I_RELATED2 || I_RELATED3 == i);
		do {
			I_RELATED4 = Util.getRandomInt(rand, 1, Constants.NUM_ITEMS);
		} while(I_RELATED4 == I_RELATED1 || I_RELATED4 == I_RELATED2 || I_RELATED4 == I_RELATED3 || I_RELATED4 == i);
		do {
			I_RELATED5 = Util.getRandomInt(rand, 1, Constants.NUM_ITEMS);
		} while(I_RELATED5 == I_RELATED1 || I_RELATED5 == I_RELATED2 || I_RELATED5 == I_RELATED3 || I_RELATED5 == I_RELATED4 || I_RELATED5 == i);
		
		try {
			item = itemHome.findByPrimaryKey(I_ID);
			item.setI_COST(I_NEW_COST);
			item.setI_STOCK(I_NEW_STOCK);
			item.setI_IMAGE(I_NEW_IMAGE);
			item.setI_THUMBNAIL(I_NEW_THUMBNAIL);
			item.setI_RELATED1(new Integer(I_RELATED1));
			item.setI_RELATED2(new Integer(I_RELATED2));
			item.setI_RELATED3(new Integer(I_RELATED3));
			item.setI_RELATED4(new Integer(I_RELATED4));
			item.setI_RELATED5(new Integer(I_RELATED5));
			item.setI_PUB_DATE(new Date());
			ItemModel it = item.getItem();
			model = new ItemAuthorModel(it);
			Author author = authorHome.findByPrimaryKey(it.getI_A_ID());
			model.setA_FNAME(author.getA_FNAME());
			model.setA_LNAME(author.getA_LNAME());
		} catch (Exception re) {
			throw new EJBException(re);
		}
		return model;
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
			Object objref = initial.lookup("java:comp/env/ejb/Customer");
			custHome = (CustomerHome)PortableRemoteObject.narrow(objref, CustomerHome.class);

			objref = initial.lookup("java:comp/env/ejb/Address");
			addressHome = (AddressHome)PortableRemoteObject.narrow(objref, AddressHome.class);

			objref = initial.lookup("java:comp/env/ejb/Country");
			countryHome = (CountryHome)PortableRemoteObject.narrow(objref, CountryHome.class);
			
			objref = initial.lookup("java:comp/env/ejb/Order");
			orderHome = (OrderHome)PortableRemoteObject.narrow(objref, OrderHome.class);
			
			objref = initial.lookup("java:comp/env/ejb/OrderLine");
			orderLineHome = (OrderLineHome)PortableRemoteObject.narrow(objref, OrderLineHome.class);
			
			objref = initial.lookup("java:comp/env/ejb/Cc_xact");
			xactHome = (Cc_xactHome)PortableRemoteObject.narrow(objref, Cc_xactHome.class);

			objref = initial.lookup("java:comp/env/ejb/Item");
			itemHome = (ItemHome)PortableRemoteObject.narrow(objref, ItemHome.class);

			objref = initial.lookup("java:comp/env/ejb/Author");
			authorHome = (AuthorHome)PortableRemoteObject.narrow(objref, AuthorHome.class);

			datasource = (DataSource)initial.lookup("java:comp/env/jdbc/TPCWDS");
			
			rand = new Random(System.currentTimeMillis());
		} catch (Exception ex) {
			throw new EJBException(ex.getMessage());
		}
	}
	
	public void ejbPassivate() {
		custHome = null;
		addressHome = null;
		countryHome = null;
		orderHome = null;
		orderLineHome = null;
		xactHome = null;
		itemHome = null;
		authorHome = null;
		datasource = null;
		rand = null;
	}
	
	public void ejbRemove() {
		custHome = null;
	}

}
