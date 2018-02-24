package webProject.testServlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import net.sf.json.JSONArray;
import webProject.model.ImageInfo;

/**
 * Servlet implementation class FetchRecentImages
 */
@WebServlet("/FetchRecentImages")
public class FetchRecentImages extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchRecentImages() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int num = Integer.parseInt(request.getParameter("num"));
		ArrayList<ImageInfo> images = ImageInfo.getMostRecentImages(num);
		//PrintWriter pw = response.getWriter();
		//Gson gson = new Gson();
        
        JSONArray array=JSONArray.fromObject(images);
        String temp = new String(array.toString().getBytes());
        //request.setCharacterEncoding("utf-8");
        //response.setContentType("text/JavaScript;charset=utf-8");
        response.setHeader("Content-type", "text/JavaScript;charset=UTF-8");
        //response.setCharacterEncoding("UTF-8");
        OutputStream out=response.getOutputStream();  
        out.write(array.toString().getBytes("UTF-8")); 
        //response.getWriter().write(temp);
        //response.getWriter().write(new String(gson.toJson(images).getBytes("ISO-8859-1"), "UTF-8"));
	}

}
