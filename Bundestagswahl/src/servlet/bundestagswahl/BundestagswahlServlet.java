
package servlet.bundestagswahl;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/Servlet")
public class BundestagswahlServlet extends HttpServlet {

   static final long serialVersionUID = 1L;

   /**
    * @see HttpServlet#HttpServlet()
    */
   public BundestagswahlServlet() {
       super();
       // TODO Auto-generated constructor stub
   }
   
   
   public void init() throws ServletException
   {
       // Do required initialization
	   try {
		Class.forName("org.postgresql.Driver");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   
   
   @Override
   public void doGet( HttpServletRequest requ, HttpServletResponse resp )
   throws ServletException, IOException
   {
      resp.setContentType( "text/html" );
      PrintWriter out = resp.getWriter();
      out.println( "<html>" );
      out.println( "<h3> Bundestagswahl Auswertung </h3>" );
      out.println( "<a href='/Bundestagswahl/'>zur&uuml;ck</a>" );
      out.println( "</html>" );
      out.close();
   }
   
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}
