package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.BookBean;
import bean.SoldBean;

public class SoldDAO {
	
	DataSource ds;
	BookDAO b;

	public SoldDAO() throws ClassNotFoundException {
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			b = new BookDAO();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	
	public Map<String, SoldBean> retrieveAllBooksSold() throws SQLException {
		String category = "PURCHASE";
		String query = "select * from visitevent where eventtype like '" + category + "'";
		Map<String, SoldBean> rv = new HashMap<String, SoldBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while(r.next()){
			String bid = r.getString("bid");
			
			if(rv.containsKey(bid))
			{
				SoldBean old = rv.get(bid);
				old.setQuantity(old.getQuantity() + 1);
			}
			else {
				BookBean bean = b.retrieveBeanByBID(bid);
				SoldBean s = new SoldBean(bean, 1);
				rv.put(bid, s);
			}
		}
		r.close();
		p.close();
		con.close();
		
		return rv;
	}
	
	public Map<String, SoldBean> retrieveBooksSoldByMonth(String month) throws SQLException {
		String category = "PURCHASE";
		String query = "select * from VISITEVENT where eventtype like 'PURCHASE' and DAY like \'%" + month + "%\'";
		Map<String, SoldBean> rv = new HashMap<String, SoldBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while(r.next()){
			String bid = r.getString("bid");
			
			if(rv.containsKey(bid))
			{
				SoldBean old = rv.get(bid);
				old.setQuantity(old.getQuantity() + 1);
			}
			else {
				BookBean bean = b.retrieveBeanByBID(bid);
				SoldBean s = new SoldBean(bean, 1);
				rv.put(bid, s);
			}
		}
		r.close();
		p.close();
		con.close();
		
		return rv;
	}
	
	public Map<Integer, SoldBean> retrieveTopBooksSold() throws SQLException {
		int topSold = 10;
		String category = "PURCHASE";
		String query = "select * from visitevent where eventtype like '" + category + "'";
		Map<String, SoldBean> rv = new HashMap<String, SoldBean>();
		Map<Integer, SoldBean> rvs = new TreeMap<Integer, SoldBean>(Collections.reverseOrder());
		
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while(r.next()){
			String bid = r.getString("bid");
			
			if(rv.containsKey(bid))
			{
				SoldBean old = rv.get(bid);
				old.setQuantity(old.getQuantity() + 1);
			}
			else {
				BookBean bean = b.retrieveBeanByBID(bid);
				SoldBean s = new SoldBean(bean, 1);
				rv.put(bid, s);
			}
		}
		r.close();
		p.close();
		con.close();
		
		for(String s : rv.keySet())
		{
			rvs.put(rv.get(s).getQuantity(), rv.get(s));
			if(rvs.size() == topSold)
				break;
		}
		
		return rvs;
	}
	
}
