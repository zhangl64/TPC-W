package edu.nyu.pdsg.tpcw.ejb.author;

import java.io.Serializable;
import java.util.Date;

/**
 * Author model class.
 * 
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:27 $   $Author: totok $
 */
public class AuthorModel implements Serializable {

	private Integer A_ID;
	private String A_FNAME;
	private String A_LNAME; 
	private String A_MNAME;
	private Date A_DOB;
	private String A_BIO;
	
	public AuthorModel(Integer A_ID, String A_FNAME, String A_LNAME, String A_MNAME, Date A_DOB, String A_BIO) {
		this.A_ID = A_ID;
		this.A_FNAME = A_FNAME;
		this.A_LNAME = A_LNAME; 
		this.A_MNAME = A_MNAME;
		this.A_DOB = A_DOB;
		this.A_BIO = A_BIO;
	}

	public AuthorModel(Integer A_ID) {
		this.A_ID = A_ID;
	}

	// getters and setters

	public String getA_BIO() {
		return A_BIO;
	}

	public Date getA_DOB() {
		return A_DOB;
	}

	public String getA_FNAME() {
		return A_FNAME;
	}

	public Integer getA_ID() {
		return A_ID;
	}

	public String getA_LNAME() {
		return A_LNAME;
	}

	public String getA_MNAME() {
		return A_MNAME;
	}

	public void setA_BIO(String string) {
		A_BIO = string;
	}

	public void setA_DOB(Date date) {
		A_DOB = date;
	}

	public void setA_FNAME(String string) {
		A_FNAME = string;
	}

	public void setA_LNAME(String string) {
		A_LNAME = string;
	}

	public void setA_MNAME(String string) {
		A_MNAME = string;
	}

}
