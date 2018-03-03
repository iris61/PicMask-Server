package webProject.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.CommandInfo;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class CommentsInfo implements Serializable{
	private int commentId = 0;
	private String username;
	private int imageId;
	private int replyToComment;
	private String content;
	private String time;
	private int hostComment = 0;
	private String replyToCommentUser = null;
	
	public CommentsInfo(int commentId, String username, int imageId, int replyToComment, String content, String time, int hostComment) {
		this.commentId = commentId;
		this.username = username;
		this.imageId = imageId;
		this.replyToComment = replyToComment;
		this.content = content;
		this.time = time;
		this.hostComment = hostComment;
	}
	
	public CommentsInfo(String username, int imageId, int replyToComment, String content, String time, int hostComment) {
		this.username = username;
		this.imageId = imageId;
		this.replyToComment = replyToComment;
		this.content = content;
		this.time = time;
		this.hostComment = hostComment;
	}
	
	public CommentsInfo() {}
	
	static public List<CommentsInfo> getAllCommentsOfImage (int imageId) throws NamingException, SQLException, ClassNotFoundException {
		List<CommentsInfo> result = new ArrayList<CommentsInfo>();
		Class.forName("com.mysql.jdbc.Driver");
   	 	Context ctx = new InitialContext();  
   	    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");  
   	    Connection conn = ds.getConnection(); 
   	    PreparedStatement stmt = conn.prepareStatement("select * from view_comments_reply where image_id = ? and host_comment_id = 0 order by time asc");
   	    stmt.setInt(1, imageId);
   	    ResultSet rs = stmt.executeQuery();
	    while(rs.next()) {
	    		CommentsInfo comment = new CommentsInfo();
	    		comment.setCommentId(rs.getInt("comment_id"));
	    		comment.setImageId(rs.getInt("image_id"));
	    		comment.setUserName(rs.getString("user_name"));
	    		comment.setReplyToComment(rs.getInt("reply_to_comment_id"));
	    		comment.setContent(rs.getString("content"));
	    		comment.setTime(rs.getString("time"));
	    		comment.setHostComment(rs.getInt("host_comment_id"));
	    		comment.setReplyToCommentUser(rs.getString("reply_to_comment_user"));
	    		result.add(comment);
	    		
	    		PreparedStatement stmtChild = conn.prepareStatement("select * from view_comments_reply where host_comment_id = ? order by time asc");
	    		stmtChild.setInt(1, rs.getInt("comment_id"));
	    		ResultSet rsChild = stmtChild.executeQuery();
	    		while(rsChild.next()) {
		    		CommentsInfo commentChild = new CommentsInfo();
		    		commentChild.setCommentId(rsChild.getInt("comment_id"));
		    		commentChild.setImageId(rsChild.getInt("image_id"));
		    		commentChild.setUserName(rsChild.getString("user_name"));
		    		commentChild.setReplyToComment(rsChild.getInt("reply_to_comment_id"));
		    		commentChild.setContent(rsChild.getString("content"));
		    		commentChild.setTime(rsChild.getString("time"));
		    		commentChild.setHostComment(rsChild.getInt("host_comment_id"));
		    		commentChild.setReplyToCommentUser(rsChild.getString("reply_to_comment_user"));
		    		result.add(commentChild);
	    		}
	    }
   	    conn.close();
   	    return result;
	}
	
	static public void insertCommentsInfo(CommentsInfo comment) throws NamingException, SQLException, ClassNotFoundException {
   	 	Class.forName("com.mysql.jdbc.Driver");
   	 	Context ctx = new InitialContext();  
   	    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");  
   	    Connection conn = ds.getConnection(); 
   	    PreparedStatement stmt0 = conn.prepareStatement("select max(comment_id) as maxid from comments");
	    ResultSet rs0 = stmt0.executeQuery();
	    int comment_id = 1;
	    if(rs0.next()) {
	    		String maxid = rs0.getString("maxid");
	    		if(maxid != null) comment_id = Integer.parseInt(rs0.getString("maxid")) + 1;
	    }
   	    PreparedStatement stmt = conn.prepareStatement("insert into comments values (?, ?, ?, ?, ?, ?, ?)");
   	    stmt.setInt(1, comment_id);
   	    stmt.setInt(2, comment.imageId);
   	    stmt.setString(3, comment.username);
   	    stmt.setInt(4, comment.replyToComment);
   	    stmt.setString(5, comment.content);
   	    stmt.setString(6, comment.time);
   	    stmt.setInt(7, comment.hostComment);
   	    stmt.executeUpdate();
   	    conn.close();
    }
	
	static public void removeComments(int imageId) throws NamingException, SQLException, ClassNotFoundException {
   	 	Class.forName("com.mysql.jdbc.Driver");
   	 	Context ctx = new InitialContext();  
   	    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");  
   	    Connection conn = ds.getConnection(); 
   	    PreparedStatement stmt = conn.prepareStatement("delete from comments where image_id = ?");
	   	stmt.setInt(1, imageId);
	    	stmt.executeUpdate();
 	    conn.close();
    }
	
	static public void removeComment(int commentId) throws NamingException, SQLException, ClassNotFoundException {
   	 	Class.forName("com.mysql.jdbc.Driver");
   	 	Context ctx = new InitialContext();  
   	    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jndi/mydb");  
   	    Connection conn = ds.getConnection(); 
   	    PreparedStatement stmt = conn.prepareStatement("delete from comments where comment_id = ?");
	   	stmt.setInt(1, commentId);
	    	stmt.executeUpdate();
 	    conn.close();
    }
	
	public void setCommentId (int commentId) {this.commentId = commentId; }
	public void setImageId (int imageId) {this.imageId = imageId; }
	public void setUserName (String username) {this.username = username; }
	public void setReplyToComment (int replyToComment) {this.replyToComment = replyToComment; }
	public void setContent (String content) {this.content = content;}
	public void setTime (String time) {this.time = time;}
	public void setHostComment (int hostComment) {this.hostComment = hostComment;}
	public int getCommentId() {return this.commentId;}
	public int getImageId() {return this.imageId;}
	public String getUserName() {return this.username;}
	public int getReplyToComment() {return this.replyToComment;}
	public String getContent() {return this.content;}
	public String getTime() {return this.time;}
	public int getHostComment() {return this.hostComment;}
	public String getReplyToCommentUser() {return this.replyToCommentUser;}
	public void setReplyToCommentUser (String replyToCommentUser) {this.replyToCommentUser = replyToCommentUser; }
}
