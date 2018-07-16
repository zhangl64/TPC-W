package edu.nyu.pdsg.tpcw.ejb.customer;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

/**
 * Customer bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public abstract class CustomerBean implements EntityBean {

	private EntityContext ctx;
	
	// CMP field accessors -----------------------------------------------------

	public abstract Integer getC_ADDR_ID();
	public abstract Double getC_BALANCE();
	public abstract Date getC_BIRTHDATE();
	public abstract String getC_DATA();
	public abstract Double getC_DISCOUNT();
	public abstract String getC_EMAIL();
	public abstract Date getC_EXPIRATION();
	public abstract String getC_FNAME();
	public abstract Integer getC_ID();
	public abstract Date getC_LAST_VISIT();
	public abstract String getC_LNAME();
	public abstract Date getC_LOGIN();
	public abstract String getC_PASSWD();
	public abstract String getC_PHONE();
	public abstract Date getC_SINCE();
	public abstract String getC_UNAME();
	public abstract Double getC_YTD_PMT();

	public abstract void setC_ADDR_ID(Integer integer);
	public abstract void setC_BALANCE(Double double1);
	public abstract void setC_BIRTHDATE(Date date);
	public abstract void setC_DATA(String string);
	public abstract void setC_DISCOUNT(Double double1);
	public abstract void setC_EMAIL(String string);
	public abstract void setC_EXPIRATION(Date date);
	public abstract void setC_FNAME(String string);
	public abstract void setC_ID(Integer integer);
	public abstract void setC_LAST_VISIT(Date date);
	public abstract void setC_LNAME(String string);
	public abstract void setC_LOGIN(Date date);
	public abstract void setC_PASSWD(String string);
	public abstract void setC_PHONE(String string);
	public abstract void setC_SINCE(Date date);
	public abstract void setC_UNAME(String string);
	public abstract void setC_YTD_PMT(Double double1);

	// create methods ----------------------------------------------------------
    
	public Integer ejbCreate(String C_UNAME, String C_PASSWD, String C_FNAME, String C_LNAME,
			Integer C_ADDR_ID, String C_PHONE, String C_EMAIL, Date C_SINCE, Date C_LAST_VISIT,
			Date C_LOGIN, Date C_EXPIRATION, Double C_DISCOUNT, Double C_BALANCE, 
			Double C_YTD_PMT, Date C_BIRTHDATE, String C_DATA) throws CreateException {
				
		setC_ADDR_ID(C_ADDR_ID);
		setC_BALANCE(C_BALANCE);
		setC_BIRTHDATE(C_BIRTHDATE);
		setC_DATA(C_DATA);
		setC_DISCOUNT(C_DISCOUNT);
		setC_EMAIL(C_EMAIL);
		setC_EXPIRATION(C_EXPIRATION);
		setC_FNAME(C_FNAME);
		setC_LAST_VISIT(C_LAST_VISIT);
		setC_LNAME(C_LNAME);
		setC_LOGIN(C_LOGIN);
		setC_PASSWD(C_PASSWD);
		setC_PHONE(C_PHONE);
		setC_SINCE(C_SINCE);
		setC_UNAME(C_UNAME);
		setC_YTD_PMT(C_YTD_PMT);
		return null;
	}

	public void ejbPostCreate(String C_UNAME, String C_PASSWD, String C_FNAME, String C_LNAME,
		Integer C_ADDR_ID, String C_PHONE, String C_EMAIL, Date C_SINCE, Date C_LAST_VISIT,
		Date C_LOGIN, Date C_EXPIRATION, Double C_DISCOUNT, Double C_BALANCE, 
		Double C_YTD_PMT, Date C_BIRTHDATE, String C_DATA) {}

	// model object accessors ------------------------------------------------------------

	/**
	 * Returns full Model Object.
	 */
	public CustomerModel getCustomer() {
		return new CustomerModel(getC_ID(), getC_UNAME(), getC_PASSWD(), getC_FNAME(), getC_LNAME(),
				getC_ADDR_ID(), getC_PHONE(), getC_EMAIL(), getC_SINCE(), getC_LAST_VISIT(),
				getC_LOGIN(), getC_EXPIRATION(), getC_DISCOUNT(), getC_BALANCE(), 
				getC_YTD_PMT(), getC_BIRTHDATE(), getC_DATA());
	}
	
	// EJB callbacks -----------------------------------------------------------
   
	public void setEntityContext(EntityContext context) {
		ctx = context;
	}

	public void unsetEntityContext() {
		ctx = null;
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void ejbRemove() {
	}

	public void ejbStore() {
	}

	public void ejbLoad() {
	}
}
