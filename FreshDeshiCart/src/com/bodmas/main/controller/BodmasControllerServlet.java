package com.bodmas.main.controller;
//------------------------------------------------------------------------------------
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;



import com.bodmas.main.dao.BodmasDbUtil;
import com.bodmas.main.model.BodmasAdmin;
import com.bodmas.main.model.BodmasStudent;
//------------------------------------------------------------------------------------



/**
 * Servlet implementation class BodmasControllerServlet
 */
@WebServlet("/BodmasControllerServlet")
public class BodmasControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	
	
	private BodmasDbUtil bodmasDbUtil;
	
	@Resource(name="jdbc/bodmas")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		
		try {


			bodmasDbUtil = new BodmasDbUtil(dataSource);
		
		}
		catch(Exception e) {

			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		try {
			
			String theCommand =  request.getParameter("command");
			
			if(theCommand==null) {
				
				theCommand = "List";
			}
			
			switch(theCommand) {
			
			case "LIST":
				
				fetchingData(request,response);
				break;
			
			case "S_ADD":
				addStudent(request,response);
				break;
			
			case "LOGIN":
				loginAdmin(request,response);
				break;
				
			case "S_LOGIN":
				loginStudent(request,response);
				break;	
				
			case "LOGOUT":
				logoutAdmin(request,response);
				break;
				
			case "LOAD":
				loadStudent(request,response);
				break;
			
			case "UPDATE":
				updateStudent(request,response);
				break;
			
			case "DELETE":
				deleteStudent(request,response);
				break;	
			
			default:
				fetchingData(request,response);
				
			}	
			

			
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String theCommand =  request.getParameter("command");
			
			if(theCommand==null) {
				
				theCommand = "List";
			}
			
			switch(theCommand) {
			
			case "LIST":
				
				fetchingData(request,response);
				break;
			
			case "S_ADD":
				addStudent(request,response);
				break;
			
			case "LOGIN":
				loginAdmin(request,response);
				break;
				
			case "S_LOGIN":
				loginStudent(request,response);
				break;	
				
			case "LOGOUT":
				logoutAdmin(request,response);
				break;
			
			case "LOAD":
				loadStudent(request,response);
				break;
			
			case "UPDATE":
				updateStudent(request,response);
				break;
			
			case "DELETE":
				deleteStudent(request,response);
				break;
			default:
				fetchingData(request,response);
				
			}	
			

			
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
		

		
		
		
	}
	
	
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//read the student id from data
		String theStudentId = request.getParameter("studentId");
		
		//delete data from the database 
		bodmasDbUtil.deleteStudent(theStudentId);
		
		//send back to the "list students" page 
		fetchingData(request, response);
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int id = Integer.parseInt(request.getParameter("studentId"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String mobile = request.getParameter("mobile");
		String password = request.getParameter("password");
		String confirm_password = request.getParameter("confirm_password");
		
		
		//create a new student object
		BodmasStudent theStudent = new BodmasStudent(id, name,email,mobile,password,confirm_password);
		
		//perform updte on databse
		bodmasDbUtil.updateStudent(theStudent);
		
		//send them back to the "list students" page
		fetchingData(request, response);
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//read the student id from data
		String theStudentId = request.getParameter("studentId");
		
		//get student from database (db util)
		BodmasStudent theStudent = bodmasDbUtil.getStudents(theStudentId);
		
		//place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		//send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update_student.jsp");
		dispatcher.forward(request, response);
		
	}

	private void loginStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String email               =          request.getParameter("email");
		String password            =          request.getParameter("password");
		
		BodmasStudent bs             =          new BodmasStudent(email, password);  
		System.out.println(bs);
		
		System.out.println(bodmasDbUtil.loginStudent(bs)); 
        if(bodmasDbUtil.loginStudent(bs)) {
			System.out.println("if loginstudent");
			HttpSession session = request.getSession();
			session.setAttribute("email", email);
			
			response.sendRedirect("student_dashboard.jsp");
			
		}
		
		else {
			
			PrintWriter out = response.getWriter();
			   out.println("<script type=\"text/javascript\">");
			   out.println("alert('email or password incorrect');");
			   out.println("location='index.html';");
			   out.println("</script>");
			//response.sendRedirect("index.html");
		}
		
	}

	/* Admin Logout */
	private void logoutAdmin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		session.removeAttribute("email");
		session.invalidate();
		response.sendRedirect("index.html");
		
		
	}

	/* Admin Login */
	
	private void loginAdmin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String email               =          request.getParameter("email");
		String password            =          request.getParameter("password");
		
		BodmasAdmin ba             =          new BodmasAdmin(email, password);  
		System.out.println(ba);
		
		System.out.println(bodmasDbUtil.loginAdmin(ba));
        if(bodmasDbUtil.loginAdmin(ba)) {
			
			HttpSession session = request.getSession();
			session.setAttribute("email", email);
			
			response.sendRedirect("admin_dashboard.jsp");
			
		}
		
		else {
			
			PrintWriter out = response.getWriter();
			   out.println("<script type=\"text/javascript\">");
			   out.println("alert('email or password incorrect');");
			   out.println("location='index.html';");
			   out.println("</script>");
		}
		
	}

	
	
	
	
	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("add method invoked");
		String name                 =              request.getParameter("name");
		String email                =              request.getParameter("email");
		String mobile               =              request.getParameter("mobile");
		String password             =              request.getParameter("password");
		String confirm_password     =              request.getParameter("confirm_password");
		
		BodmasStudent bs            =              new BodmasStudent(name, email, mobile, password, confirm_password);
		System.out.println(bs);

		bodmasDbUtil.addStudent(bs);
		
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		System.out.println("email "+session.getAttribute("email"));
		if(session.getAttribute("email")=="" || session.getAttribute("email")==null) {
			
			   out.println("<script type=\"text/javascript\">");
			   out.println("alert('Thank you for Registration now you can login');");
			   out.println("location='index.html';");
			   out.println("</script>");
		}
		else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("student_list.jsp");   
			dispatcher.forward(request, response);
		}
		

		
		
	}

	private void fetchingData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
				List<BodmasStudent> students = bodmasDbUtil.getStudents();
				
				//add student to the request
				request.setAttribute("STUDENT_LIST", students);
				
				//send to the JSP page (view )
				RequestDispatcher dispatcher = request.getRequestDispatcher("student_list.jsp");
				dispatcher.forward(request, response);
	}

}
