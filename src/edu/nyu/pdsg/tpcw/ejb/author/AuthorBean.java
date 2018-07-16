package edu.nyu.pdsg.tpcw.ejb.author;

import java.util.Date;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

/**
 * Entity bean representing Author.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public abstract class AuthorBean implements EntityBean {

	private EntityContext ctx;
	
	// CMP field accessors -----------------------------------------------------

	public abstract String getA_BIO();
	public abstract Date getA_DOB();
	public abstract String getA_FNAME();
	public abstract Integer getA_ID();
	public abstract String getA_LNAME();
	public abstract String getA_MNAME();

	public abstract void setA_BIO(String string);
	public abstract void setA_DOB(Date date);
	public abstract void setA_FNAME(String string);
	public abstract void setA_ID(Integer integer);
	public abstract void setA_LNAME(String string);
	public abstract void setA_MNAME(String string);

	// create methods ----------------------------------------------------------
    
	// model object accessors ------------------------------------------------------------

	/**
	 * Returns full Model Object.
	 */
	public AuthorModel getAuthor() {
		return new AuthorModel(getA_ID(), getA_FNAME(), getA_LNAME(), getA_MNAME(), getA_DOB(), getA_BIO());
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
