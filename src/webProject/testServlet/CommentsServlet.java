package webProject.testServlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import webProject.model.CommentsInfo;
import webProject.model.ImageInfo;

/**
 * Servlet implementation class CommentsServlet
 */
@WebServlet("/CommentsServlet")
public class CommentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");  
		int imageId = Integer.parseInt(request.getParameter("imageId"));
		int userId = 0;
		int replyToComment = 0;
		String content = "";
		String time = "";
		int hostComment = 0;
		ArrayList<CommentsInfo> comments = new ArrayList<CommentsInfo>();
		try {
			userId = Integer.parseInt(request.getParameter("userId"));
			imageId = Integer.parseInt(request.getParameter("imageId"));
			replyToComment = Integer.parseInt(request.getParameter("replyToComment"));
			content = request.getParameter("content");
			time = request.getParameter("time");
			hostComment = Integer.parseInt(request.getParameter("hostComment"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			if(userId > 0) {
				try {
					CommentsInfo.insertCommentsInfo(new CommentsInfo(userId, imageId, replyToComment, content, time, hostComment));
					
				}catch(Exception e) {
					
				}
			}else {
				comments = (ArrayList<CommentsInfo>) CommentsInfo.getAllCommentsOfImage(imageId);
				JSONArray array=JSONArray.fromObject(comments);
		        response.setHeader("Content-type", "text/JavaScript;charset=UTF-8");
		        OutputStream out=response.getOutputStream();  
		        out.write(array.toString().getBytes("UTF-8")); 
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
        
    }
}
