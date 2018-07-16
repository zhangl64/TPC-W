University of Virginia
Department of Computer Science
Author: Tibor Horvath (tibor@cs.virginia.edu)
May 2008

J2EE-based Implementation of the TPC-W Benchmark (Modified Version)
-------------------------------------------------------------------

This implementation is based on the implementation from New York University.
The modifications include significant performance and feature improvements.

The original README file is included below.



=============================================================================

New York University
Courant Institute of Mathematical Sciences
Computer Science Department
Parallel and Distributed Systems Research Group (PDSG)
Developer: Alexander Totok (Ph.D. student): totok@cs.nyu.edu
February 2004

J2EE-based Implementation of the TPC-W Benchmark
------------------------------------------------

This application is a J2EE-based implementation of the TPC-W
benchmark (version 1.8): http://www.tpc.org/tpcw/default.asp.

Features:
-- Utilizes classical three tier architecture (Web, EJB, Data tiers).
-- Web tier relies on the session handling provided by the Servlet Container.
-- EJB tier conforms to the EJB spec version 2.0. Entity beans implemented using CMP 2.0.
-- The most simple searches (queries) are implemented using EJB QL (EJB 2.0). Several more
   difficult queries are implemented using JBoss QL (JBoss extension of EJB QL). 
   A few most difficult queries are implemented as direct SQL JDBC commands through 
   stateless session EJBs.
-- Fully J2EE-compliant and packaged (use provided ANT script). Additional 
   application server - specific deployment descriptors (DD) are provided for 
   the JBoss application server.
-- Database population utility is provided. SQL (JDBS) code is tested to work with 
   the MySQL database (v. 3.2.x and later).
-- Immediately deployable on the JBoss-MySQL bundle (was tested on JBoss 3.2.x and 
   MySQL 4.0.x).


Deviations of this TPC-W implementation from the TPC-W spec
-----------------------------------------------------------

0. This version implements TPC-W spec version 1.8.

1. All VARCHAR database rows are limited to size <=255, because
   of MySQL databse requirement. Notes in the code and other files 
   are put where this is done.

2. In the ITEMS table, I_THUMBNAIL and I_IMAGE are varchars, keeping the
   name of the image file (which themselves are distributed with the WAR archive).
   
3. There are only 100 pregenerated item images and thumbnails. This is done
   to reduce the size of the WAR file (images are stored there, rather than 
   in the database). See ImageGen\USAGE.TXT file for further instructions.

4. In teh CC_XACTS table, CX_NUM (credit card number) is VARCHAR(16),
   but not numeric, 16 digits. Keeps credit card number in text format.

5. During execution of the Admin Confirm Web Interaction, for a specified item, 
   I_RELATED* fields must be updated using a specific algorithm (see clause 2.16.3.3, 
   spec version 1.8), involving massive database scan. In the current implementation 
   it IS NOT PERFORMED as prescribed in the clause 2.16.3.3 of the
   spec (version 1.8). It rather updates these values with rabdomly selected IDs.

6. When a new Customer created in the Buy Request WI, a new (customer) address
   is created in the database, rather than trying to match an existing address
   and reuse the ADDR_ID (as specified in the clause 2.6.3.2).
   
7. In the Buy Confirm WI, when a separate shipping address is passed (clause 2.7.3.2),
   a new address is created in the database, rather than trying to match an
   existing address.
   
8. No Payment Getaway Emulator is used (clause 2.7.3.3), so the CC_XACTS credit
   card processing record is created right away during the order creation.
   
9. No SSL is used, all connections are unsecure.


To compile, deploy, and run the application:
--------------------------------------------

1. If you use MySQL database server: 

   -- it should have logical database "tpcw" created.
   
   -- deploy the MySQL datasource MBEAN file "tpcw-mysql-ds.xml" (from the root dir.
      of the package) in the deploy dir. of the JBoss server. If you are using another 
      database, make sure that the datasource is bound to java:/TPCWDS.
   
   -- to populate the database:
      -- set the desired values for NUM_ITEMS and NUM_EBS (as well as NUM_IMAGES)
         in the  edu.nyu.pdsg.tpcw.util.Constants  Java file;
      -- for the I_TITLE field in the ITEMS table, and the A_LNAME fields in the AUTHOR
         table, specific values are used, which should be pregenerated using the 'wgen' 
         utility. Read the USAGE.TXT file in the 'wgen\wgen_v1_exe' directory;
      -- after database population, the I_THUMBNAIL and I_IMAGE fields in the ITEMS table 
         will contain the names of the image files, that should be packaged in the WAR 
         archive. The number of images is set in the NUM_IMAGES field of the 
         edu.nyu.pdsg.tpcw.util.Constants  Java class. Read the USAGE.TXT file in the 
         'ImageGen' directory on how to use the ImageGen utility to generate image files,
         and where to put them;
      -- rebuild the project;
      -- run the populate.bat script (in the root directory of the package), this will
         populate the database.
   
2. Application uses ANT build and packaging sripts, you should provide correct references
   to the ANT class files in the build.bat file.

3. To compile the application, you should provide the path to the J2EE class files (we point
   it to the JBoss class files) - see build.bat batch file and make appropriate changes 
   is necessary.

4. Compile TPC-W by running the "build.bat ear" script. This will create the EAR archive.

5. Deploy the application by copyng build/tpcw.ear file to the deploy directory of
   the JBoss server.

6. Access application through the URL:

      http://localhost:8080/tpcw/

   and follow the instructions.
