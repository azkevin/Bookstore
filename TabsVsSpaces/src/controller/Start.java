package controller;

import java.io.IOException;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ReviewBean;
import bean.UserBean;
import model.Model;
import model.ReviewUtil;

/**
 * Servlet implementation class Start
 */
@WebServlet(urlPatterns = {"/Start", "/LoginPage", "/RegisterUserPage"})
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Model sis;
	private UserBean currentUser;
	
	//used for login
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	
	//used for account creation
	private static final String ACC_EMAIL = "email";
	private static final String ACC_PASSWORD = "acc_password";
	private static final String ACC_C_PASSWORD = "acc_c_password";
	private static final String ACC_FNAME = "firstname";
	private static final String ACC_LNAME = "lastname";
	private static final String ACC_STREET = "streetname";
	private static final String ACC_PROVINCE = "province";
	private static final String ACC_COUNTRY = "country";
	private static final String ACC_ZIP = "zip";
	private static final String ACC_PHONE = "phone";
	private static final String ACC_CARD_TYPE = "card_type";
	private static final String ACC_CARD = "card_num";
	private static final String ACC_CVV = "cvv";
	private static final String ACC_MONTH = "month";
	private static final String ACC_YEAR = "year";
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Start() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException{
    	try {
			this.sis = new Model();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/plain");
		
		String target = "MainPage.jspx";
		String category = request.getParameter("category");
		String book = request.getParameter("book");
		
		//Don't understand the logic behind this, but it works!
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String email = request.getParameter(ACC_EMAIL);
		String acc_password = request.getParameter(ACC_PASSWORD);
		String acc_c_password = request.getParameter(ACC_C_PASSWORD);
		String fName = request.getParameter(ACC_FNAME);
		String lName = request.getParameter(ACC_LNAME);
		String street = request.getParameter(ACC_STREET);
		String province = request.getParameter(ACC_PROVINCE);
		String country = request.getParameter(ACC_COUNTRY);
		String zip = request.getParameter(ACC_ZIP);
		String phoneNumber = request.getParameter(ACC_PHONE);
		String cardtype = request.getParameter(ACC_CARD_TYPE);
		String cardnum = request.getParameter(ACC_CARD);
		String cvv = request.getParameter(ACC_CVV);
		String month = request.getParameter(ACC_MONTH);
		String year = request.getParameter(ACC_YEAR);
		
		request.setAttribute("category", category); //Set attribute for header on main page
		
		//<TODO> error checking on all control flow
		// User selects a book category to browse
		

		//If the login button is clicked
		if(request.getParameter("login") != null)
		{
			currentUser = null;
			try {
				currentUser = sis.retrieveUser(username, password);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(currentUser != null)
			{
				//Login Successful. Load main page
				request.getServletContext().setAttribute("username", currentUser);
				String url = request.getRequestURL().append("?").append("category=All").toString();
				response.sendRedirect(url);
			}
			else {
				//Failed Login. Re-load loginPage
				request.setAttribute("error", "Invalid username or password.");
				request.getRequestDispatcher("LoginPage.jspx").forward(request, response);
			}
		}
		
		//If the logout button is clicked
		else if(request.getParameter("logout") != null)
		{
			//remove attribute from servlet context
			//yet to implement error logging out case(not necessary though)
			currentUser = null;
			username = "";
			password = "";
			request.getServletContext().removeAttribute("username");
			
			if(request.getServletContext().getAttribute("username") == null)
			{
				String url = request.getRequestURL().append("?").append("category=All").toString();
				response.sendRedirect(url);
			}
			else {
				request.setAttribute("error", "Error logging out.");
				request.getRequestDispatcher("MainPage.jspx").forward(request, response);
			}
		}
		
		//if the user is creating an account
		else if (request.getParameter("register") != null)
		{
			String user_name = email.substring(0, email.indexOf('@'));
			try {
				currentUser = sis.addNewUser(user_name, acc_password, email, fName, lName, street, province, country, zip, phoneNumber);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(currentUser != null)
			{
				//Account Creation Successful. Load main page
				request.getServletContext().setAttribute("username", currentUser);
				String url = request.getRequestURL().append("?").append("category=All").toString();
				response.sendRedirect(url);
			}
			else {
				//Failed Login. Re-load loginPage
				request.setAttribute("error", "Account Creation Failed!");
				request.getRequestDispatcher("RegisterUserPage.jspx").forward(request, response);
			}
		}
		
		else if(category != null && !category.equals("")){
			try {
				request.setAttribute("books", sis.retrieveBookByCategory(category));
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.getRequestDispatcher(target).forward(request, response);
		} 
		// User selects a book for more details
		else if(book != null && !book.equals("")) {
			target = "Book.jspx";
			try {
				Map<String, ReviewBean> bookReviews = sis.retrieveReviewByBID(book);
				request.setAttribute("book", sis.retrieveBookByBID(book));
				request.setAttribute("review", bookReviews);
				request.setAttribute("reviewAvg", ReviewUtil.calculateAvgBookRating(bookReviews));
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.getRequestDispatcher(target).forward(request, response);
		}		
		//Default category should be "All" on the landing page
		else {
			String url = request.getRequestURL().append("?").append("category=All").toString();
			response.sendRedirect(url);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		doGet(request, response);
	}

}
