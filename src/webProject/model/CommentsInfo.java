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
	private int userId;
	private int imageId;
	private int replyToComment;
	private String content;
	private String time;
	private int hostComment = 0;
	
	public CommentsInfo(int commentId, int userId, int imageId, int replyToComment, String content, String time, int hostComment) {
		this.commentId = commentId;
		this.userId = userId;
		this.imageId = imageId;
		this.replyToComment = replyToComment;
		this.content = content;
		this.time = time;
		this.hostComment = hostComment;
	}
	
	public CommentsInfo(int userId, int imageId, int replyToComment, String content, String time, int hostComment) {
		this.userId = userId;
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
   	    PreparedStatement stmt = conn.prepareStatement("select * from comments where image_id = ? and host_comment = 0 order by time asc");
   	    stmt.setInt(1, imageId);
   	    ResultSet rs = stmt.executeQuery();
	    while(rs.next()) {
	    		CommentsInfo comment = new CommentsInfo();
	    		comment.setCommentId(rs.getInt("comment_id"));
	    		comment.setImageId(rs.getInt("image_id"));
	    		comment.setUserId(rs.getInt("user_id"));
	    		comment.setReplyToComment(rs.getInt("reply_to_comment"));
	    		comment.setContent(rs.getString("content"));
	    		comment.setTime(rs.getString("time"));
	    		comment.setHostComment(rs.getInt("host_comment"));
	    		result.add(comment);
	    		
	    		PreparedStatement stmtChild = conn.prepareStatement("select * from comments where host_comment = ? order by time asc");
	    		stmtChild.setInt(1, rs.getInt("comment_id"));
	    		ResultSet rsChild = stmtChild.executeQuery();
	    		while(rsChild.next()) {
		    		CommentsInfo commentChild = new CommentsInfo();
		    		commentChild.setCommentId(rsChild.getInt("comment_id"));
		    		commentChild.setImageId(rsChild.getInt("image_id"));
		    		commentChild.setUserId(rsChild.getInt("user_id"));
		    		commentChild.setReplyToComment(rsChild.getInt("reply_to_comment"));
		    		commentChild.setContent(rsChild.getString("content"));
		    		commentChild.setTime(rsChild.getString("time"));
		    		commentChild.setHostComment(rsChild.getInt("host_comment"));
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
   	    stmt.setInt(3, comment.userId);
   	    stmt.setInt(4, comment.replyToComment);
   	    stmt.setString(5, comment.content);
   	    stmt.setString(6, comment.time);
   	    stmt.setInt(7, comment.hostComment);
   	    stmt.executeUpdate();
   	    conn.close();
    }
	
	public void setCommentId (int commentId) {this.commentId = commentId; }
	public void setImageId (int imageId) {this.imageId = imageId; }
	public void setUserId (int userId) {this.userId = userId; }
	public void setReplyToComment (int replyToComment) {this.replyToComment = replyToComment; }
	public void setContent (String content) {this.content = content;}
	public void setTime (String time) {this.time = time;}
	public void setHostComment (int hostComment) {this.hostComment = hostComment;}
	public int getCommentId() {return this.commentId;}
	public int getImageId() {return this.imageId;}
	public int getUserId() {return this.userId;}
	public int getReplyToComment() {return this.replyToComment;}
	public String getContent() {return this.content;}
	public String getTime() {return this.time;}
	public int getHostComment() {return this.hostComment;}
}
