package webProject.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class LikesInfo {
	String username = null;
	int imageId = 0;
	int likeId = 0;
	
	public LikesInfo(String username, int imageId, int likeId) {
		this.username = username;
		this.imageId = imageId;
		this.likeId = likeId;
	}
	
	public LikesInfo(String username, int imageId) {
		this.username = username;
		this.imageId = imageId;
	}
	
	public static void toggleLike(String username, int imageId) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
	   	 	Context ctx = new InitialContext();  
	   	    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");  
	   	    Connection conn = ds.getConnection(); 
	   	    PreparedStatement stmt0 = conn.prepareStatement("select * from userLikeRecord where username = ? and user_like_image_id = ?");
	   	    stmt0.setString(1, username);
		    stmt0.setInt(2, imageId);
	   	    ResultSet rs0 = stmt0.executeQuery();
		    if(rs0.next()) {
		    		PreparedStatement stmt = conn.prepareStatement("delete from userLikeRecord where username = ? and user_like_image_id = ?");
		   	    stmt.setString(1, username);
			    stmt.setInt(2, imageId);
			    stmt.executeUpdate();
		   	    conn.close();
		    }else {
		    		PreparedStatement stmtForId = conn.prepareStatement("select max(id) as maxid from userLikeRecord");
			    ResultSet rsForId = stmtForId.executeQuery();
			    int like_id = 1;
			    if(rsForId.next()) {
			    		String maxid = rsForId.getString("maxid");
			    		if(maxid != null) like_id = Integer.parseInt(rsForId.getString("maxid")) + 1;
			    }
			    
		    	 	PreparedStatement stmt = conn.prepareStatement("insert into userLikeRecord values (?, ?, ?)");
		    	    stmt.setInt(1, like_id);
		    	    stmt.setString(2, username);
		    	    stmt.setInt(3, imageId);
		    	    stmt.executeUpdate();
		    	    conn.close();
		    }
		}catch(Exception e){e.printStackTrace();}
	}
	
	static public void removeLikes(int imageId) throws NamingException, SQLException, ClassNotFoundException {
   	 	Class.forName("com.mysql.jdbc.Driver");
   	 	Context ctx = new InitialContext();  
   	    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");  
   	    Connection conn = ds.getConnection(); 
   	    PreparedStatement stmt = conn.prepareStatement("delete from userLikeRecord where user_like_image_id = ?");
	   	stmt.setInt(1, imageId);
	    	stmt.executeUpdate();
 	    conn.close();
    }
}
