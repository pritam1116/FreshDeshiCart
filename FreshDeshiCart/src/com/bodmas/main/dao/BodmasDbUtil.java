/**
 * 
 */
package com.bodmas.main.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


import com.bodmas.main.model.BodmasAdmin;
import com.bodmas.main.model.BodmasStudent;


/**
 * @author Pritam Sinha
 *
 */
public class BodmasDbUtil {
	
	private DataSource      dataSource;
	
	Connection              conn            =     null;
	PreparedStatement       ps              =     null;
	Statement               stmt            =     null;
	ResultSet               rs              =     null;

	
	public BodmasDbUtil(DataSource theDataSource) {
			System.out.println("Datasource method invoked");
		  dataSource = theDataSource;
	}

	public void addStudent(BodmasStudent bodmasStudent) throws Exception {
		

			
		try {
				System.out.println("add student method invoked");
				conn           =        dataSource.getConnection();
				String    sql  =        "insert into bodmas_registration "
									+ "(name,email,mobile,password,confirm_password) "
									+ " values (?,?,?,?,?) "
									;
				ps             =        conn.prepareStatement(sql);
				
				
				ps.setString(1,bodmasStudent.getName());
				ps.setString(2,bodmasStudent.getEmail());
				ps.setString(3,bodmasStudent.getMobile());
				ps.setString(4,bodmasStudent.getPassword());
				ps.setString(5,bodmasStudent.getConfirm_password());
				
				ps.executeQuery();
				
			}
			finally {
				
				close(conn, ps, rs);
					
			}
		
		
	}

	private void close(Connection conn, PreparedStatement ps2, ResultSet rs) {
		
		try {
			
			if(rs != null) {
				rs.close();
			}
			if(ps2 != null) {
				ps2.close();
			}
			if(conn != null) {
				conn.close();
			}
			
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
		
	}

	public boolean loginAdmin(BodmasAdmin bodmasAdmin) throws Exception {
		
		try {
			System.out.println("Login Admin method invoked");
			conn           =        dataSource.getConnection();
			String    sql  =        "select * from bodmas_admin_login "
								+ " where email=? and password=? "
								;
			
			ps             =        conn.prepareStatement(sql);
			
			
			
			ps.setString(1,bodmasAdmin.getEmail());
			
			ps.setString(2,bodmasAdmin.getPassword());
			
			rs = ps.executeQuery();
			
			
			if(rs.next()) {
				System.out.println("if invoked "+rs.getString("email"));
				if(bodmasAdmin.getEmail().equals(rs.getString("email")) 
						&& bodmasAdmin.getPassword().equals(rs.getString("password")))
				return true; 
			}
			
			return false;   
			
		}
		finally {
			
			close(conn, ps, rs);
				
		}
		
		
		
	}

	public boolean loginStudent(BodmasStudent bodmasStudent) throws Exception {
		
		try {
			System.out.println("Login Student method invoked");
			conn           =        dataSource.getConnection();
			String    sql  =        "select * from bodmas_registration "
								+ " where email=? and password=? "
								;
			
			ps             =        conn.prepareStatement(sql);
			
			
			
			ps.setString(1,bodmasStudent.getEmail());
			
			ps.setString(2,bodmasStudent.getPassword());
			
			rs = ps.executeQuery();
			
			
			if(rs.next()) {
				System.out.println("if invoked "+rs.getString("email"));
				if(bodmasStudent.getEmail().equals(rs.getString("email")) 
						&& bodmasStudent.getPassword().equals(rs.getString("password")))
				return true; 
			}
			
			return false;   
			
		}
		finally {
			
			close(conn, ps, rs);
				
		}
	}

	public List<BodmasStudent> getStudents() throws Exception {
		
		List<BodmasStudent> students = new ArrayList<BodmasStudent>();
		

		
		try {
		//get a connection
		conn = dataSource.getConnection();
		
		//create SQL statement
		String sql = "select * from bodmas_registration ";
		stmt = conn.createStatement();
		
		//execute query
		rs = stmt.executeQuery(sql);
		
		//process result set
		while(rs.next()) {
			
			//retrieve data from result set row 
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String email = rs.getString("email");
			String mobile = rs.getString("mobile");
			String password = rs.getString("password");
			String confirm_password = rs.getString("confirm_password");
			//create new student object
			BodmasStudent tempStudent = new BodmasStudent(id, name, email, mobile,password,confirm_password);
			
			//add it to the list of students
			students.add(tempStudent);
					

		}
		
			return students;	
		}
		finally {
			
			//close JDBC objects
			close(conn,stmt,rs);
		}
	}

	private void close(Connection conn2, Statement stmt2, ResultSet rs2) {
		
try {
			
			if(rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			if(conn != null) {
				conn.close();
			}
			
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
		
	}

	public void deleteStudent(String theStudentId) throws Exception {
		
		try {
			
			//convert student id to int 
			int studentId = Integer.parseInt(theStudentId);
			
			//get connection to database 
			conn = dataSource.getConnection();
			
			//create statement to delete student
			String sql = "delete from bodmas_registration where id=? ";
			
			//prepared statement
			ps=conn.prepareStatement(sql);
			
			//set params
			ps.setInt(1, studentId);
			
			//execute SQL statement 
			ps.execute();
		}
		finally {
			close(conn, ps, rs);
		}
		
	}

	public BodmasStudent getStudents(String theStudentId) throws Exception {
		
        int studentId;
		BodmasStudent theStudent = null;
		try {
			//convert student id to int 
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			conn = dataSource.getConnection();
			
			//create sql to get selected student 
			String sql = "select * from bodmas_registration where id=? ";
			
			//create prepared statement 
			ps = conn.prepareStatement(sql);
			
			//set the param
			ps.setInt(1,studentId);
			
			//execute statement 
			rs = ps.executeQuery();
			
			//retrieve data from result set row
			if(rs.next()) {
				
				String name = rs.getString("name");
				String email = rs.getString("email");
				String mobile = rs.getString("mobile");
				String password = rs.getString("password");
				String confirm_password= rs.getString("confirm_password");
				
				//use the studentId during construction
				theStudent = new BodmasStudent(studentId, name, email,mobile,password,confirm_password);
			}
			
			else {
				throw new Exception("Could not find student id: " + studentId);
			}
		
		return theStudent;
		}
		finally {
			//clean up JDBC object
			close(conn, ps, rs);
		}
	}

	public void updateStudent(BodmasStudent theStudent) throws Exception {
		
		try {
			
			//get DB connection
			conn=dataSource.getConnection();
			
			//create SQL for update
			String sql = "update bodmas_registration "
						+" set name=?, email=?, mobile=?, password=?, confirm_password=? "
						+ " where id=? ";
			
			ps = conn.prepareStatement(sql);
			
			//set the param values for the student 
			ps.setString(1, theStudent.getName());
			ps.setString(2, theStudent.getEmail());
			ps.setString(3, theStudent.getMobile());
			ps.setString(4,theStudent.getPassword());
			ps.setString(5,theStudent.getConfirm_password());
			ps.setInt(6,theStudent.getId());
			
			//execute SQL  statements
			ps.execute();
			
		} 	 	
		finally {
		//clean up JDBC objects
		close(conn, ps, rs);
			
		}
		
	}

}
