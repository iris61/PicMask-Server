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
    	    return user.found;
     }
}
