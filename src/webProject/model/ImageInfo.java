package webProject.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ImageInfo implements Serializable{
	 private String createUser = null;
	 private String createTime = null;
	 private String imageName = null;
	 private int imageId = 0;
	 private String comment = null;
	 private int likes = 0;
	 private int selfLike = 0;
    public ImageInfo(String create_user, String comment, String image_name, String create_time){
    		this.comment = comment;
    		this.createTime = create_time;
    		this.createUser = create_user;
    		this.imageName = image_name;
    }
    
    public ImageInfo(){}
    
    static public void insertImageInfo(ImageInfo image) throws NamingException, SQLException, ClassNotFoundException {
   	 	Class.forName("com.mysql.jdbc.Driver");
   	 	Context ctx = new InitialContext();  
   	    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");  
   	    Connection conn = ds.getConnection(); 
   	    PreparedStatement stmt0 = conn.prepareStatement("select max(id) as maxid from images");
	    ResultSet rs0 = stmt0.executeQuery();
	    int image_id = 1;
	    if(rs0.next()) {
	    		String maxid = rs0.getString("maxid");
	    		if(maxid != null) image_id = Integer.parseInt(rs0.getString("maxid")) + 1;
	    }
   	    PreparedStatement stmt = conn.prepareStatement("insert into images values (?, ?, ?, ?, ?, ?, ?)");
   	    stmt.setInt(1, image_id);
   	    stmt.setString(2, image.createTime);
   	    stmt.setString(3, image.createUser);
   	    stmt.setString(4, image.comment);
   	    stmt.setString(5, image.imageName);
   	    stmt.setInt(6, 0);
   	    stmt.setInt(7, 0);
   	    stmt.executeUpdate();
   	    conn.close();
    }
    
    public static ArrayList<ImageInfo> getMostRecentImages(int num){
    		ArrayList<ImageInfo> recentImagesList = new ArrayList<ImageInfo>();
    		try {
	    		Class.forName("com.mysql.jdbc.Driver");
	    		Context ctx = new InitialContext();  
	    		DataSource ds2 = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");
	       	Connection conn2 = ds2.getConnection(); 
	       	PreparedStatement stmt2 = conn2.prepareStatement("select * from images order by create_time desc limit ?");
	    	    stmt2.setInt(1, num);
	       	ResultSet rs2 = stmt2.executeQuery();
	    	    while(rs2.next()) {
	    	    		ImageInfo imageInfo = new ImageInfo();
	    	    		imageInfo.setImageId(rs2.getInt("id"));
	    	    		imageInfo.setLikes(rs2.getInt("likes"));
	    	    		imageInfo.setCreateTime(rs2.getString("create_time"));
	    	    		imageInfo.setCreateUser(rs2.getString("create_user"));
	    	    		imageInfo.setComment(rs2.getString("comment"));
	    	    		imageInfo.setImageName(rs2.getString("image_name"));
	    	    		imageInfo.setSelfLike(rs2.getInt("self_like"));
	    	    		recentImagesList.add(imageInfo);
	    	    }
	    	    conn2.close();
    		}catch(Exception e) {e.printStackTrace();}
    		return recentImagesList;
    }

	public static ArrayList<ImageInfo> getMyRecentImages(int num, String username){
		ArrayList<ImageInfo> myImagesList = new ArrayList<ImageInfo>();
		try {
	    		Class.forName("com.mysql.jdbc.Driver");
	    		Context ctx = new InitialContext();  
	    		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");
	       	Connection conn = ds.getConnection(); 
	       	PreparedStatement stmt = conn.prepareStatement("select * from images where create_user = ? order by create_time desc limit ?");
	       	stmt.setString(1, username);
	       	stmt.setInt(2, num);
	       	ResultSet rs = stmt.executeQuery();
	    	    while(rs.next()) {
	    	    		ImageInfo imageInfo = new ImageInfo();
	    	    		imageInfo.setImageId(rs.getInt("id"));
	    	    		imageInfo.setLikes(rs.getInt("likes"));
	    	    		imageInfo.setCreateTime(rs.getString("create_time"));
	    	    		imageInfo.setCreateUser(rs.getString("create_user"));
	    	    		imageInfo.setComment(rs.getString("comment"));
	    	    		imageInfo.setImageName(rs.getString("image_name"));
	    	    		imageInfo.setSelfLike(rs.getInt("self_like"));
	    	    		myImagesList.add(imageInfo);
	    	    }
	    	    conn.close();
		}catch(Exception e) {e.printStackTrace();}
		return myImagesList;
    }
	
	public static boolean addLikeByOthers(int imageId) {
		try {
	    		Class.forName("com.mysql.jdbc.Driver");
	    		Context ctx = new InitialContext();  
	    		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");
	       	Connection conn = ds.getConnection(); 
	       	PreparedStatement stmt = conn.prepareStatement("update images set likes = likes + 1 where id = ?");
	       	stmt.setInt(1, imageId);
	       	stmt.executeUpdate();
	    	    conn.close();
	    	    return true;
		}catch(Exception e) {e.printStackTrace();}
		return false;
	}
	
	public static boolean addLikeBySelf(int imageId) {
		try {
	    		Class.forName("com.mysql.jdbc.Driver");
	    		Context ctx = new InitialContext();  
	    		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");
	       	Connection conn = ds.getConnection(); 
	       	PreparedStatement stmt = conn.prepareStatement("update images set likes = likes + 1, self_like = 1 where id = ?");
	       	stmt.setInt(1, imageId);
	       	stmt.executeUpdate();
	    	    conn.close();
	    	    return true;
		}catch(Exception e) {e.printStackTrace();}
		return false;
	}
	
	public static boolean removeLikeByOthers(int imageId) {
		try {
	    		Class.forName("com.mysql.jdbc.Driver");
	    		Context ctx = new InitialContext();  
	    		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");
	       	Connection conn = ds.getConnection(); 
	       	PreparedStatement stmt = conn.prepareStatement("update images set likes = likes - 1 where id = ?");
	       	stmt.setInt(1, imageId);
	       	stmt.executeUpdate();
	    	    conn.close();
	    	    return true;
		}catch(Exception e) {e.printStackTrace();}
		return false;
	}
	
	public static boolean removeLikeBySelf(int imageId) {
		try {
	    		Class.forName("com.mysql.jdbc.Driver");
	    		Context ctx = new InitialContext();  
	    		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");
	       	Connection conn = ds.getConnection(); 
	       	PreparedStatement stmt = conn.prepareStatement("update images set likes = likes - 1 and self_like = 0 where id = ?");
	       	stmt.setInt(1, imageId);
	       	stmt.executeUpdate();
	    	    conn.close();
	    	    return true;
		}catch(Exception e) {e.printStackTrace();}
		return false;
	}
	
	public static boolean removeImage(int imageId) {
		try {
	    		Class.forName("com.mysql.jdbc.Driver");
	    		Context ctx = new InitialContext();  
	    		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");
	       	Connection conn = ds.getConnection(); 
	       	PreparedStatement stmt = conn.prepareStatement("delete from images where id = ?");
	       	stmt.setInt(1, imageId);
	       	stmt.executeUpdate();
	    	    conn.close();
	    	    return true;
		}catch(Exception e) {e.printStackTrace();}
		return false;
	}
    
    private void setSelfLike(int selfLike) {
		this.selfLike = selfLike;
	}
	
    public void setCreateTime(String create_time) {
    		this.createTime = create_time;
    }
    
    public void setCreateUser(String create_user) {
		this.createUser = create_user;
    }
    
    public void setComment(String comment) {
    		this.comment = comment;
    }
    
    public void setImageName(String image_name) {
    		this.imageName = image_name;
    }
    
    public void setImageId(int image_id) {
		this.imageId = image_id;
    }
    
    public void setLikes(int likes) {
		this.likes = likes;
    }
    
    public String getImageName() {return this.imageName;}
    public String getCreateUser() {return this.createUser;}
    public String getCreateTime() {return this.createTime;}
    public String getComment() {return this.comment;}
    public int getImageId() {return this.imageId;}
    public int getLikes() {return this.likes;}
    public int getSelfLike() {return this.selfLike;}
}
