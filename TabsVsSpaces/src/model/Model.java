package model;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import DAO.AddressDAO;
import DAO.BookDAO;
import DAO.CartDAO;
import DAO.CreditCardDAO;
import DAO.ReviewDAO;
import DAO.SoldDAO;
import DAO.LoginDAO;
import DAO.OrderDAO;
import DAO.PODAO;
import DAO.RegisterDAO;
import bean.AddressBean;
import bean.BookBean;
import bean.CartBean;
import bean.CreditCardBean;
import bean.POBean;
import bean.ReviewBean;
import bean.SoldBean;
import bean.UserBean;

public class Model {
	private BookDAO bookDAO;
	private ReviewDAO reviewDAO;
	private LoginDAO loginDAO;
	private RegisterDAO registerDAO;
	private AddressDAO addressDAO;
	private OrderDAO orderDAO;
	private SoldDAO soldDAO;
	private CartDAO cartDAO;
	private CreditCardDAO creditcardDAO;
	private PODAO poDAO;
	
	public Model() throws ClassNotFoundException {
		this.bookDAO = new BookDAO();
		this.reviewDAO = new ReviewDAO();
		this.loginDAO = new LoginDAO();
		this.registerDAO = new RegisterDAO();
		this.addressDAO = new AddressDAO();
		this.orderDAO = new OrderDAO();
		this.soldDAO = new SoldDAO();
		this.cartDAO = new CartDAO();
		this.creditcardDAO = new CreditCardDAO();
		this.poDAO = new PODAO();
	}
	
	// PO/Orders
	public void addToPO(int a_userid, int a_addressid) throws Exception {
		this.poDAO.addNewPO(a_userid, a_addressid);
	}
	public void addDeniedToPO(int a_userid, int a_addressid) throws Exception {
		this.poDAO.addDeniedPO(a_userid, a_addressid);
	}
	public Map<Integer, POBean> retrieveByPOId(int a_poid) throws Exception {
		return this.poDAO.retrieveByPOId(a_poid);
	}

	// Shopping Cart
	public Map<Integer, CartBean> retrieveCartByUserId(int a_userid) throws Exception {
		return this.cartDAO.retrieveByUserId(a_userid);
	}
	public void removeCartByCartId(int a_cartid) throws Exception {
		this.cartDAO.removeCartItem(a_cartid);
	}
	public void addToCart(int a_userid, String a_bid) throws Exception {
		this.cartDAO.addNewCartItem(a_userid, a_bid);
	}
	
	// Books
	public Map<String, BookBean> retrieveBookByCategory(String category) throws Exception {
		if(category.equalsIgnoreCase("All")) {
			return this.bookDAO.retrieveAll();
		}
		return this.bookDAO.retrieveAllByCategory(category);
	}
	
	public Map<String, BookBean> retrieveBookByBID(String bid) throws Exception {
		return this.bookDAO.retrieveByBID(bid);
	}
	
	// Reviews
	public Map<Integer, ReviewBean> retrieveReviewByBID(String bid) throws Exception {
		return this.reviewDAO.retrieveByBID(bid);
	}
	
	public ReviewBean addNewReview(String bid, int userid, String username, int rating, String reviewdesc) throws Exception {
		return this.reviewDAO.addNewReview(bid, userid, username, rating, reviewdesc);
	}
	
	// Users
	public UserBean retrieveUser(String username, String password) throws Exception {
		return this.loginDAO.loginUser(username, password);
	}
	
	public UserBean addNewUser(String username, String pwd, String email, String fName, String lName, String street, String province,
			String country, String zip, String phoneNumber, String type, String number, String cvv, String month, String year) throws Exception {
		return registerDAO.addNewUser(username, pwd, email, fName, lName, street, province, country, zip, phoneNumber, type, number, cvv, month, year);
	}
	
	public AddressBean getAddress(int userID) throws Exception{
		return addressDAO.getAddress(userID);
	}
	
	public CreditCardBean getCreditCard(int ccid) throws Exception {
		return creditcardDAO.getCreditCard(ccid);
	}
	
	// Analytics
	public Map<String, SoldBean> retrieveBooksSold() throws Exception {
		return this.soldDAO.retrieveAllBooksSold();
	}
	
	public Map<Integer, SoldBean> retrieveTopBooksSold() throws Exception {
		return this.soldDAO.retrieveTopBooksSold();
	}
	
	public Map<String, SoldBean> retrieveBooksSoldByMonth(String month) throws Exception {
		return this.soldDAO.retrieveBooksSoldByMonth(month);
	}
	
	// VisitEvents
	public void addVisitEvent(String day, String bid, String event) throws Exception {
		bookDAO.addVisitEvent(day, bid, event);
	}
	
	
//	public String submitOrder(int uid, String status, ArrayList<CartBean> list) throws Exception{
//		return orderDAO.submitOrder(uid, status, list);
//	}
//	
}
