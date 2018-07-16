package edu.nyu.pdsg.tpcw.populate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import edu.nyu.pdsg.tpcw.util.Constants;
import edu.nyu.pdsg.tpcw.util.Util;

/**
 * Populate.java - database population program.
 * 
 * @author Rice University ...
 * @author <a href="mailto:totok@cs.nyu.edu">Alexander Totok</a>
 * 
 * @version   $Revision: 1.4 $   $Date: 2005/02/05 21:26:28 $   $Author: totok $
 */
public class Populate {
    
    private static Connection con;
    private static Random rand;
    
    public static void main(String[] args) {
    	try {
			System.out.println("Beginning TPC-W database population...");
			rand = new Random(System.currentTimeMillis());
			getConnection();
			deleteTables();
			createTables();
			populateCountryTable();
			populateAddressTable();
			populateAuthorTable();
			populateCustomerTable();
			populateItemTable();
			populateOrdersAndCC_XACTSTable();
            populateRecentOrdersTable();
    		addIndexes();
			System.out.println("Population: done!");
    	} catch (Exception e) {
    		e.printStackTrace();
	    } finally {
			closeConnection();
	    }
    }

	private static void getConnection() throws Exception {
		System.out.println("Opening database connection...");
		Class.forName(Constants.driverName).newInstance();
		con = DriverManager.getConnection(Constants.dbName);
		con.setAutoCommit(false);
		System.out.println("Opening: done");
	}
  
	private static void closeConnection() {
		try {
			if (con != null) {
				con.commit();
				con.close();
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (SQLException ex) {}
		}
		System.out.println("Closing database connection: done");
	}
  
	private static void deleteTables() throws Exception {
		System.out.println("Deleting tables...");

		for (int i = 0; i < Constants.tables.length; i++){
			try {
				PreparedStatement statement = con.prepareStatement("DROP TABLE " + Constants.tables[i]);
				statement.executeUpdate();
				System.out.println("Dropped table: " + Constants.tables[i]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		con.commit();
		System.out.println("Deleting tables: done");
	}

	private static void createTables() throws Exception {
		System.out.println("Creating tables...");
		PreparedStatement statement;
	
		statement = con.prepareStatement(Constants.CREATE_ADDRESS_TABLE);
		statement.executeUpdate();
		System.out.println("Created table ADDRESS");

		statement = con.prepareStatement(Constants.CREATE_AUTHOR_TABLE);
		statement.executeUpdate();
		System.out.println("Created table AUTHOR");
	
		statement = con.prepareStatement(Constants.CREATE_COUNTRY_TABLE);
		statement.executeUpdate();
		System.out.println("Created table COUNTRY");

		statement = con.prepareStatement(Constants.CREATE_CUSTOMER_TABLE);
		statement.executeUpdate();
		System.out.println("Created table CUSTOMER");

		statement = con.prepareStatement(Constants.CREATE_ITEM_TABLE);
		statement.executeUpdate();
		System.out.println("Created table ITEM");

		statement = con.prepareStatement(Constants.CREATE_ORDER_LINE_TABLE);
		statement.executeUpdate();
		System.out.println("Created table ORDER_LINE");

		statement = con.prepareStatement(Constants.CREATE_ORDERS_TABLE);
		statement.executeUpdate();
		System.out.println("Created table ORDERS");

		statement = con.prepareStatement(Constants.CREATE_CC_XACTS_TABLE);
		statement.executeUpdate();
		System.out.println("Created table CC_XACTS");
		
		statement = con.prepareStatement(Constants.CREATE_RECENT_ORDERS_TABLE);
		statement.executeUpdate();
		System.out.println("Created table RECENT_ORDERS");
		
		con.commit();

		System.out.println("Creating tables: done");
	}
	
	private static void populateCountryTable() throws Exception {
		System.out.println("Populating COUNTRY TABLE with " + Constants.NUM_COUNTRIES + " countries...");
	
		PreparedStatement statement = con.prepareStatement
		("INSERT INTO country (CO_ID, CO_NAME, CO_EXCHANGE, CO_CURRENCY) VALUES (?,?,?,?)");
		for(int i = 1; i <= Constants.NUM_COUNTRIES; i++){
			// Set parameter
			statement.setInt(1, i);
			statement.setString(2, Constants.COUNTRIES[i-1]);
			statement.setDouble(3, Constants.EXCHANGES[i-1]);
			statement.setString(4, Constants.CURRENCIES[i-1]);
			statement.executeUpdate();
		}
		System.out.println("COUNTRY table: done");
	}

	private static void populateAddressTable() throws Exception {
		String ADDR_STREET1, ADDR_STREET2, ADDR_CITY, ADDR_STATE, ADDR_ZIP;
		int ADDR_CO_ID;

		System.out.println("Populating ADDRESS table with " + Constants.NUM_ADDRESSES + " addresses...");
		System.out.print("Complete (in 1000's): ");

		PreparedStatement statement = con.prepareStatement
		("INSERT INTO address (ADDR_STREET1, ADDR_STREET2, ADDR_CITY, ADDR_STATE, ADDR_ZIP, ADDR_CO_ID) VALUES (?, ?, ?, ?, ?, ?)",
		 Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i <= Constants.NUM_ADDRESSES; i++) {
			if (i % 1000 == 0) System.out.print(i/1000 + " ");
			ADDR_STREET1 = Util.getRandomAString(rand, 15, 40);
			ADDR_STREET2 = Util.getRandomAString(rand, 15, 40);
			ADDR_CITY    = Util.getRandomAString(rand, 4, 30);
			ADDR_STATE   = Util.getRandomAString(rand, 2, 20);
			ADDR_ZIP     = Util.getRandomNString(rand, 5, 10);
			ADDR_CO_ID   = Util.getRandomInt(rand, 1, 92);
		
			// Set parameter
			statement.setString(1, ADDR_STREET1);
			statement.setString(2, ADDR_STREET2);
			statement.setString(3, ADDR_CITY);
			statement.setString(4, ADDR_STATE);
			statement.setString(5, ADDR_ZIP);
			statement.setInt(6, ADDR_CO_ID);
			statement.executeUpdate();
			checkGeneratedKey(statement, i, "ADDRESS");
			if (i % 1000 == 0) con.commit();
		}
		System.out.println();
		con.commit();
		System.out.println("ADDRESS table: done");
	}

	private static void populateAuthorTable() throws Exception {
		String A_FNAME, A_MNAME, A_LNAME, A_BIO;
		Date A_DOB;
		GregorianCalendar cal;

		System.out.println("Populating AUTHOR table with " + Constants.NUM_AUTHORS + " authors...");
		System.out.print("Complete (in 1000's): ");
		
		// open A_LNAME.TXT file
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(Constants.A_LNAME_FILE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	
		PreparedStatement statement = con.prepareStatement
		("INSERT INTO author (A_ID, A_FNAME, A_LNAME, A_MNAME, A_DOB, A_BIO) VALUES (?, ?, ?, ?, ?, ?)");
		for (int i = 1; i <= Constants.NUM_AUTHORS; i++) {
			int month, day, year, maxday;
			if (i % 1000 == 0) System.out.print(i/1000 + " ");
			A_FNAME = Util.getRandomAString(rand, 3,20);

			//A_LNAME = Util.getRandomAString(rand, 1,20);
			do {
				A_LNAME = reader.readLine().trim();
			} while (A_LNAME.equals(""));

			A_MNAME = Util.getRandomAString(rand, 1,20);
			year = Util.getRandomInt(rand, 1800, 1990);
			month = Util.getRandomInt(rand, 0, 11);
			maxday = 31;
			if (month == 3 | month ==5 | month == 8 | month == 10)
				maxday = 30;
			else if (month == 1)
				maxday = 28;
			day = Util.getRandomInt(rand, 1, maxday);
			cal = new GregorianCalendar(year, month, day);
			A_DOB = new Date(cal.getTime().getTime());
			A_BIO = Util.getRandomAString(rand, 125, 255); //  (125, 500)
			// Set parameter
			statement.setInt(1, i);
			statement.setString(2, A_FNAME);
			statement.setString(3, A_LNAME);
			statement.setString(4, A_MNAME);
			statement.setDate(5, A_DOB);
			statement.setString(6, A_BIO);
			statement.executeUpdate();
			if(i % 1000 == 0) con.commit();
		}
		reader.close();
		System.out.println();
		con.commit();
		System.out.println("AUTHOR table: done");
	}

	private static void populateCustomerTable() throws Exception {
		String C_UNAME, C_PASSWD, C_LNAME, C_FNAME;
		int C_ADDR_ID;
		String C_PHONE, C_EMAIL;
		Date C_SINCE, C_LAST_VISIT;
		Date C_LOGIN, C_EXPIRATION;
		double C_DISCOUNT, C_BALANCE, C_YTD_PMT;
		Date C_BIRTHDATE;
		String C_DATA;

		System.out.println("Populating CUSTOMER table with " + Constants.NUM_CUSTOMERS + " customers...");
		System.out.print("Complete (in 1000's): ");
		
		PreparedStatement statement = con.prepareStatement
		("INSERT INTO customer (C_UNAME, C_PASSWD, C_FNAME, C_LNAME, C_ADDR_ID, C_PHONE, C_EMAIL, C_SINCE, C_LAST_VISIT, C_LOGIN, C_EXPIRATION, C_DISCOUNT, C_BALANCE, C_YTD_PMT, C_BIRTHDATE, C_DATA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)",
		 Statement.RETURN_GENERATED_KEYS);

		for (int i = 1; i <= Constants.NUM_CUSTOMERS; i++) {
			if (i%1000 == 0) System.out.print(i/1000 + " ");

		    C_UNAME = Util.DigSyl(rand, i, 0);
			C_PASSWD = C_UNAME.toLowerCase();
			C_LNAME = Util.getRandomAString(rand, 8, 15);
			C_FNAME = Util.getRandomAString(rand, 8, 15);
			C_ADDR_ID = Util.getRandomInt(rand, 1, Constants.NUM_ADDRESSES);
			C_PHONE = Util.getRandomNString(rand, 9, 16);
			C_EMAIL = C_UNAME + "@" + Util.getRandomAString(rand, 2, 9) + ".com";

			GregorianCalendar cal = new GregorianCalendar();
			cal.add(Calendar.DAY_OF_YEAR, -1*Util.getRandomInt(rand, 1, 730));
			C_SINCE = new Date(cal.getTime().getTime());
			cal.add(Calendar.DAY_OF_YEAR, Util.getRandomInt(rand, 0, 60)); 
			if (cal.after(new GregorianCalendar())) cal = new GregorianCalendar();		
			C_LAST_VISIT = new Date(cal.getTime().getTime());

			C_LOGIN = new Date(System.currentTimeMillis());
			cal = new GregorianCalendar();
			cal.add(Calendar.HOUR, 2);
			C_EXPIRATION = new Date(cal.getTime().getTime());

			C_DISCOUNT = ((double)Util.getRandomInt(rand, 0, 50))/100.0;
			C_BALANCE = 0.00;
			C_YTD_PMT = ((double) Util.getRandomInt(rand, 0, 99999))/100.0;

			int year = Util.getRandomInt(rand, 1880, 2000);
			int month = Util.getRandomInt(rand, 0, 11);
			int maxday = 31;
			int day;
			if (month == 3 | month ==5 | month == 8 | month == 10) {
				maxday = 30;
			} else if (month == 1) {
				maxday = 28;
			}
			day = Util.getRandomInt(rand, 1, maxday);
			cal = new GregorianCalendar(year, month, day);
			C_BIRTHDATE = new Date(cal.getTime().getTime());

			C_DATA = Util.getRandomAString(rand, 100, 255); // (100, 500)

			statement.setString(1, C_UNAME);
			statement.setString(2, C_PASSWD);
			statement.setString(3, C_FNAME);
			statement.setString(4, C_LNAME);
			statement.setInt(5, C_ADDR_ID);
			statement.setString(6, C_PHONE);
			statement.setString(7, C_EMAIL);
			statement.setDate(8, C_SINCE);
			statement.setDate(9, C_LAST_VISIT);
			statement.setDate(10, C_LOGIN);
			statement.setDate(11, C_EXPIRATION);
			statement.setDouble(12, C_DISCOUNT);
			statement.setDouble(13, C_BALANCE);
			statement.setDouble(14, C_YTD_PMT);
			statement.setDate(15, C_BIRTHDATE);
			statement.setString(16, C_DATA);
			statement.executeUpdate();
			checkGeneratedKey(statement, i, "CUSTOMER");
			if(i % 1000 == 0) con.commit();
		}
		System.out.println();
		con.commit();
		System.out.println("CUSTOMER table done");
	}

	private static void populateItemTable() throws Exception {
		String I_TITLE;
		int I_A_ID;
		Date I_PUB_DATE;
		String I_PUBLISHER, I_SUBJECT, I_DESC;
		int I_RELATED1, I_RELATED2, I_RELATED3, I_RELATED4, I_RELATED5;
		String I_THUMBNAIL, I_IMAGE;
		double I_SRP, I_COST;
		Date I_AVAIL;
		int I_STOCK;
		String I_ISBN;
		int I_PAGE;
		String I_BACKING, I_DIMENSIONS;

		GregorianCalendar cal;

		System.out.println("Populating ITEM table with " + Constants.NUM_ITEMS + " items...");
		System.out.print("Complete (in 1000's): ");

		// open I_TITLE.TXT file
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(Constants.I_TITLE_FILE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));		

		PreparedStatement statement = con.prepareStatement
		("INSERT INTO item (I_ID, I_TITLE , I_A_ID, I_PUB_DATE, I_PUBLISHER, I_SUBJECT, I_DESC, I_RELATED1, I_RELATED2, I_RELATED3, I_RELATED4, I_RELATED5, I_THUMBNAIL, I_IMAGE, I_SRP, I_COST, I_AVAIL, I_STOCK, I_ISBN, I_PAGE, I_BACKING, I_DIMENSIONS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		for (int i = 1; i <= Constants.NUM_ITEMS; i++) {
			int month,day,year,maxday;

			// I_TITLE= Util.getRandomAString(rand, 14,60);
			do {
				I_TITLE = reader.readLine().trim();
			} while (I_TITLE.equals(""));
			
			if (i <= (Constants.NUM_AUTHORS))
				I_A_ID = i;
			else 
				I_A_ID = Util.getRandomInt(rand, 1, Constants.NUM_AUTHORS);

			year = Util.getRandomInt(rand, 1930, 2000);
			month = Util.getRandomInt(rand, 0,11);
			maxday = 31;
			if (month == 3 | month ==5 | month == 8 | month == 10)
				maxday = 30;
			else if (month == 1)
				maxday = 28;
			day = Util.getRandomInt(rand, 1, maxday);
			cal = new GregorianCalendar(year,month,day);
			I_PUB_DATE = new Date(cal.getTime().getTime());

			I_PUBLISHER = Util.getRandomAString(rand, 14, 60);
			I_SUBJECT = Constants.SUBJECTS[Util.getRandomInt(rand, 0, Constants.NUM_SUBJECTS-1)];
			I_DESC = Util.getRandomAString(rand, 100, 255); // (100, 500)

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
	       
			I_THUMBNAIL = "images/items/thumb" + ((i-1)%Constants.NUM_IMAGES + 1) + ".jpg";
			I_IMAGE = "images/items/item" + ((i-1)%Constants.NUM_IMAGES + 1) + ".jpg";
			
			int srp = Util.getRandomInt(rand, 100, 999999);
			int cost = Util.getRandomInt(rand, srp/2, srp);
			I_SRP  = ((double)srp)/100.0;
			I_COST = ((double)cost)/100.0;

			cal.add(Calendar.DAY_OF_YEAR, Util.getRandomInt(rand, 1, 30));
			I_AVAIL = new Date(cal.getTime().getTime());

			I_STOCK = Util.getRandomInt(rand, 10, 30);
			I_ISBN = Util.getRandomAString(rand, 13);
			I_PAGE = Util.getRandomInt(rand, 20, 9999);
			I_BACKING = Constants.BACKINGS[Util.getRandomInt(rand, 0, Constants.NUM_BACKINGS-1)];

			I_DIMENSIONS = (((double)Util.getRandomInt(rand, 1, 9999))/100.0) + "x" +
			               (((double)Util.getRandomInt(rand, 1, 9999))/100.0) + "x" +
				           (((double)Util.getRandomInt(rand, 1, 9999))/100.0);

			// Set parameter
			statement.setInt(1, i);
			statement.setString(2, I_TITLE);
			statement.setInt(3, I_A_ID);
			statement.setDate(4, I_PUB_DATE);
			statement.setString(5, I_PUBLISHER);
			statement.setString(6, I_SUBJECT);
			statement.setString(7, I_DESC);
			statement.setInt(8, I_RELATED1);
			statement.setInt(9, I_RELATED2);
			statement.setInt(10, I_RELATED3);
			statement.setInt(11, I_RELATED4);
			statement.setInt(12, I_RELATED5);
			statement.setString(13, I_THUMBNAIL);
			statement.setString(14, I_IMAGE);
			statement.setDouble(15, I_SRP);
			statement.setDouble(16, I_COST);
			statement.setDate(17, I_AVAIL);
			statement.setInt(18, I_STOCK);
			statement.setString(19, I_ISBN);
			statement.setInt(20, I_PAGE);
			statement.setString(21, I_BACKING);
			statement.setString(22, I_DIMENSIONS);

			statement.executeUpdate();
			if(i % 1000 == 0) con.commit();
		}
		reader.close();
		System.out.println();
		con.commit();
		System.out.println("ITEMS table done");
	}

	private static void populateOrdersAndCC_XACTSTable() throws Exception {
		GregorianCalendar cal;
		
		int O_C_ID;
		Date O_DATE;
		double O_SUB_TOTAL;
		double O_TAX;
		double O_TOTAL;
		String O_SHIP_TYPE;
		Date O_SHIP_DATE;
		int O_BILL_ADDR_ID, O_SHIP_ADDR_ID;
		String O_STATUS;

		String CX_TYPE;
		String CX_NUM;
		String CX_NAME;
		Date CX_EXPIRY;
		String CX_AUTH_ID;
		int CX_CO_ID;
	
		System.out.println("Populating ORDERS, ORDER_LINE, CC_XACTS with " + Constants.NUM_ORDERS + " orders");	
		System.out.print("Complete (in 1000's): ");

		PreparedStatement statement = con.prepareStatement
			("INSERT INTO orders(O_C_ID, O_DATE, O_SUB_TOTAL, O_TAX, O_TOTAL, O_SHIP_TYPE, O_SHIP_DATE, O_BILL_ADDR_ID, O_SHIP_ADDR_ID, O_STATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
			Statement.RETURN_GENERATED_KEYS);
		PreparedStatement statement2 = con.prepareStatement
			("INSERT INTO order_line(OL_ID, OL_O_ID, OL_I_ID, OL_QTY, OL_DISCOUNT, OL_COMMENTS) VALUES (?, ?, ?, ?, ?, ?)");
		PreparedStatement statement3 = con.prepareStatement
			("INSERT INTO cc_xacts(CX_O_ID, CX_TYPE, CX_NUM, CX_NAME, CX_EXPIRE, CX_AUTH_ID, CX_XACT_AMT, CX_XACT_DATE, CX_CO_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		for (int i = 1; i <= Constants.NUM_ORDERS; i++){
			if(i%1000 == 0) System.out.print(i/1000 + " ");
			int num_items = Util.getRandomInt(rand, 1, 5);

			O_C_ID = Util.getRandomInt(rand, 1, Constants.NUM_CUSTOMERS);

			cal = new GregorianCalendar();
			cal.add(Calendar.DAY_OF_YEAR, -1*Util.getRandomInt(rand, 1, 60));
			O_DATE = new Date(cal.getTime().getTime());
			
			O_SUB_TOTAL = ((double)Util.getRandomInt(rand, 1000, 999999))/100;
			O_TAX = O_SUB_TOTAL * 0.0825;
			O_TOTAL = O_SUB_TOTAL + O_TAX + 3.00 + (double)num_items;

			O_SHIP_TYPE = Constants.SHIP_TYPES[Util.getRandomInt(rand, 0, Constants.NUM_SHIP_TYPES-1)];

			cal.add(Calendar.DAY_OF_YEAR, Util.getRandomInt(rand, 0, 7));
			O_SHIP_DATE = new Date(cal.getTime().getTime());

			O_BILL_ADDR_ID = Util.getRandomInt(rand, 1, Constants.NUM_ADDRESSES);
			O_SHIP_ADDR_ID = Util.getRandomInt(rand, 1, Constants.NUM_ADDRESSES);
			O_STATUS = Constants.STATUS_TYPES[Util.getRandomInt(rand, 0, Constants.NUM_STATUS_TYPES-1)];
		
			// Set parameter
			statement.setInt(1, O_C_ID);
			statement.setDate(2, O_DATE);
			statement.setDouble(3, O_SUB_TOTAL);
			statement.setDouble(4, O_TAX);
			statement.setDouble(5, O_TOTAL);
			statement.setString(6, O_SHIP_TYPE);
			statement.setDate(7, O_SHIP_DATE);
			statement.setInt(8, O_BILL_ADDR_ID);
			statement.setInt(9, O_SHIP_ADDR_ID);
			statement.setString(10, O_STATUS);
			statement.executeUpdate();
			checkGeneratedKey(statement, i, "ORDERS");

			for(int j = 1; j <= num_items; j++){
				int OL_ID = j;
				int OL_O_ID = i;
				int OL_I_ID = Util.getRandomInt(rand, 1, Constants.NUM_ITEMS);
				int OL_QTY = Util.getRandomInt(rand, 1, 300);
				double OL_DISCOUNT = ((double)Util.getRandomInt(rand, 0, 30))/100;
				String OL_COMMENTS = Util.getRandomAString(rand, 20, 100);
				statement2.setInt(1, OL_ID);
				statement2.setInt(2, OL_O_ID);
				statement2.setInt(3, OL_I_ID);
				statement2.setInt(4, OL_QTY);
				statement2.setDouble(5, OL_DISCOUNT);
				statement2.setString(6, OL_COMMENTS);
				statement2.executeUpdate();
			}

			CX_TYPE = Constants.CARD_TYPES[Util.getRandomInt(rand, 0, Constants.NUM_CARD_TYPES-1)];
			CX_NUM = Util.getRandomNString(rand, 16);
			CX_NAME = Util.getRandomAString(rand, 14, 30);

			cal = new GregorianCalendar();
			cal.add(Calendar.DAY_OF_YEAR, Util.getRandomInt(rand, 10, 730));
			CX_EXPIRY = new Date(cal.getTime().getTime());

			CX_AUTH_ID = Util.getRandomAString(rand, 15);
			CX_CO_ID = Util.getRandomInt(rand, 1, 92);
			statement3.setInt(1, i);
			statement3.setString(2, CX_TYPE);
			statement3.setString(3, CX_NUM);
			statement3.setString(4, CX_NAME);
			statement3.setDate(5, CX_EXPIRY);
			statement3.setString(6, CX_AUTH_ID);
			statement3.setDouble(7, O_TOTAL);
			statement3.setDate(8, O_SHIP_DATE);
			statement3.setInt(9, CX_CO_ID);
			statement3.executeUpdate();
		
			if(i % 1000 == 0) con.commit();
		}
		con.commit();
		System.out.println();
		System.out.println("ORDERS, ORDER_LINE, CC_XACTS tables done");
	}

    private static void populateRecentOrdersTable() throws Exception {
        PreparedStatement statement = con.prepareStatement(
            "INSERT INTO recent_orders (SELECT o_id FROM orders " +
            "ORDER BY o_date DESC LIMIT "+Constants.RECENT_ORDERS_LIMIT+")");
        statement.executeUpdate();
        System.out.println("RECENT_ORDERS table: done");
    }


    private static void addIndexes() throws Exception {
		System.out.println("Adding indexes...");
		PreparedStatement st;

		st = con.prepareStatement
		("create index customer_c_uname on customer(c_uname)");
		st.executeUpdate();
		st.close();
		
		st = con.prepareStatement
		("create index item_i_title on item(i_title)");
		st.executeUpdate();
		st.close();
		
		st = con.prepareStatement
		("create index item_i_subject on item(i_subject)");
		st.executeUpdate();
		st.close();
		
		st = con.prepareStatement
		("create index item_i_subject_i_title on item(i_subject,i_title)");
		st.executeUpdate();
		st.close();
		
		st = con.prepareStatement
		("create index item_i_subject_i_pub_date on item(i_subject,i_pub_date)");
		st.executeUpdate();
		st.close();
		
		st = con.prepareStatement
		("create index order_line_o_id on order_line(ol_o_id)");
		st.executeUpdate();
		st.close();
		
		st = con.prepareStatement
		("create index item_i_a_id on item(i_a_id)");
		st.executeUpdate();
		st.close();
				
		st = con.prepareStatement
		("create index author_a_lname on author(a_lname)");
		st.executeUpdate();
		st.close();

		st = con.prepareStatement
		("create index orders_date on orders(o_date)");
		st.executeUpdate();
		st.close();

		st = con.prepareStatement
		("create index order_line_i_id on order_line(ol_i_id)");
		st.executeUpdate();
		st.close();

	    con.commit();
	    System.out.println("Adding indexes: done");
    }

	private static void checkGeneratedKey(PreparedStatement stmt, int i, String table)
	throws SQLException
	{
		ResultSet rs = stmt.getGeneratedKeys();
		if (!rs.next()) {
			throw new RuntimeException(
				"Unable to retrieve autoincrement key for "+
				table);
		}
		if (i != rs.getInt(1)) {
			throw new RuntimeException(
				"Autoincrement key ["+rs.getInt(1)+"] for "+
				table+" is not as expected ["+i+"]");
		}
	        rs.close();
	}
}

