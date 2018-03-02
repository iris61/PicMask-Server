package webProject.testServlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webProject.model.CommentsInfo;
import webProject.model.ImageInfo;
import webProject.model.LikesInfo;
import webProject.model.UserInfo;

/**
 * Servlet implementation class UpdateImageInfo
 */
@WebServlet("/UpdateImageInfo")
public class UpdateImageInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateImageInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String action = request.getParameter("action");
		String username = request.getParameter("username");
		
		try {
			int imageId = Integer.parseInt(request.getParameter("imageId"));
			if (action.equals(new String("toggleLike"))) {
				LikesInfo.toggleLike(username, imageId);
			} else if (action.equals(new String("removeImage"))) {
				CommentsInfo.removeComments(imageId);
				LikesInfo.removeLikes(imageId);
				String path = "/Users/yangying/Documents/workspace/webProject/upload";
				File imageFile = new File(path + "/" + ImageInfo.findImageName(imageId));
				if (imageFile.exists() && imageFile.isFile()) imageFile.delete();
				ImageInfo.removeImage(imageId);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
//		PrintWriter pw = response.getWriter();
//		try {
//	            response.setContentType("text/html;charset=UTF-8");
//	            response.setCharacterEncoding("UTF-8");
//	            response.getWriter().write("1");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
