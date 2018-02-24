package webProject.model;

import java.sql.*;
import javax.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class UserInfo{
	
	 private String username;
	 private String pwd;
	 private boolean found;
     public UserInfo(String username, String pwd){
    	 	this.username = username;
    	 	this.pwd = pwd;
    	 	this.found = false;
     }
     
     static public boolean findUser(UserInfo user) throws NamingException, SQLException, ClassNotFoundException {
    	 	Class.forName("com.mysql.jdbc.Driver");
    	 	Context ctx = new InitialContext();  
    	    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");  
    	    Connection conn = ds.getConnection(); 
    	    PreparedStatement stmt = conn.prepareStatement("select * from users where username = ? and pwd = ?");
    	    stmt.setString(1, user.username);
    	    stmt.setString(2, user.pwd);
    	    ResultSet rs = stmt.executeQuery();
    	    if(rs.next()) {
    	    		user.found = true;
    	    }else {
    	    		user.found = false;
    	    }
    	    conn.close();
    	    return user.found;
     }
     
     static public boolean insertUser(UserInfo user)  throws NamingException, SQLException, ClassNotFoundException {
    	 	Class.forName("com.mysql.jdbc.Driver");
    	 	Context ctx = new InitialContext();  
    	    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");  
    	    Connection conn = ds.getConnection(); 
    	    
    	    PreparedStatement stmtFindExist = conn.prepareStatement("select * from users where username = ?");
    	    stmtFindExist.setString(1,  user.username);
    	    ResultSet rsFindExist = stmtFindExist.executeQuery();
    	    if(rsFindExist.next()) {
    	    		conn.close();
    	    		return false;
    	    }
    	    
    	    PreparedStatement stmt0 = conn.prepareStatement("select max(idusers) as maxid from users");
    	    ResultSet rs0 = stmt0.executeQuery();
    	    int user_id = 1;
    	    if(rs0.next()) {
    	    		String maxid = rs0.getString("maxid");
    	    		if(maxid != null) user_id = Integer.parseInt(rs0.getString("maxid")) + 1;
    	    }
    	    PreparedStatement stmt = conn.prepareStatement("insert into users values (?, ?, ?)");
    	    stmt.setInt(1, user_id);
       	stmt.setString(2, user.username);
       	stmt.setString(3, user.pwd);
       	stmt.executeUpdate();
   	    conn.close();
   	    return true;
     }
}
