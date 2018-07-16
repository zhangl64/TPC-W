package edu.nyu.pdsg.tpcw.ejb.xact;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

/**
 * CC_XACT bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public abstract class Cc_xactBean implements EntityBean {

	private EntityContext ctx;
	
	// CMP field accessors -----------------------------------------------------

	public abstract String getCX_AUTH_ID();
	public abstract Integer getCX_CO_ID();
	public abstract Date getCX_EXPIRE();
	public abstract String getCX_NAME();
	public abstract String getCX_NUM();
	public abstract Integer getCX_O_ID();
	public abstract String getCX_TYPE();
	public abstract Double getCX_XACT_AMT();
	public abstract Date getCX_XACT_DATE();

	public abstract void setCX_AUTH_ID(String string);
	public abstract void setCX_CO_ID(Integer integer);
	public abstract void setCX_EXPIRE(Date date);
	public abstract void setCX_NAME(String string);
	public abstract void setCX_NUM(String string);
	public abstract void setCX_O_ID(Integer integer);
	public abstract void setCX_TYPE(String string);
	public abstract void setCX_XACT_AMT(Double double1);
	public abstract void setCX_XACT_DATE(Date date);

	// create methods ----------------------------------------------------------
    
	public Integer ejbCreate(Integer CX_O_ID, String CX_TYPE, String CX_NUM, String CX_NAME, Date CX_EXPIRE,
					String CX_AUTH_ID, Double CX_XACT_AMT, Date CX_XACT_DATE, Integer CX_CO_ID) throws CreateException {
		setCX_O_ID(CX_O_ID);
		setCX_CO_ID(CX_CO_ID);
		setCX_AUTH_ID(CX_AUTH_ID);
		setCX_EXPIRE(CX_EXPIRE);
		setCX_NAME(CX_NAME);
		setCX_NUM(CX_NUM);
		setCX_TYPE(CX_TYPE);
		setCX_XACT_AMT(CX_XACT_AMT);
		setCX_XACT_DATE(CX_XACT_DATE);
		return null;
	}

	public void ejbPostCreate(Integer CX_O_ID, String CX_TYPE, String CX_NUM, String CX_NAME, Date CX_EXPIRE,
							String CX_AUTH_ID, Double CX_XACT_AMT, Date CX_XACT_DATE, Integer CX_CO_ID) {}

	// model object accessors ------------------------------------------------------------

	/**
	 * Returns full Model Object.
	 */
	public Cc_xactModel getCc_xact() {
		return new Cc_xactModel(getCX_O_ID(), getCX_TYPE(), getCX_NUM(), getCX_NAME(), getCX_EXPIRE(),
					getCX_AUTH_ID(), getCX_XACT_AMT(), getCX_XACT_DATE(), getCX_CO_ID());
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
