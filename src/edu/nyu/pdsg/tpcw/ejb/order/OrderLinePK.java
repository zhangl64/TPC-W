package edu.nyu.pdsg.tpcw.ejb.order;

import java.io.Serializable;

/**
 * Order bean.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class OrderLinePK implements Serializable {

	public Integer OL_ID;
	public Integer OL_O_ID;

	public OrderLinePK() {}

	public OrderLinePK(Integer OL_ID, Integer OL_O_ID) {
		this.OL_ID = OL_ID;
		this.OL_O_ID = OL_O_ID;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof OrderLinePK) {
			OrderLinePK pk = (OrderLinePK)obj;
			if (OL_ID.equals(pk.OL_ID) && OL_O_ID.equals(pk.OL_O_ID)) return true;
			else return false;
		} 
		return false;
	}
	
	public int hashCode() {	
		return OL_ID.hashCode()*OL_O_ID.hashCode();
	}
}
