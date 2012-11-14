package servlet.bundestagswahl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	public void init() throws ServletException {
		// Do required initialization
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doGet(HttpServletRequest requ, HttpServletResponse resp)
			throws ServletException, IOException {

		// Datenbankverbindung aufbauen
		String url = "jdbc:postgresql://localhost/Bundestagswahl?user=postgres&password=1234";
		Connection conn;
		Statement st;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(url);

			// PreparedStatement pst = conn
			// .prepareStatement("INSERT INTO \"Bundesland\" values('Testland',1);");
			// pst.executeUpdate();
			//
			// int foovalue = 1;
			// pst = conn
			// .prepareStatement("DELETE FROM \"Bundesland\" WHERE \"Population\" = ?");
			// pst.setInt(1, foovalue);
			// int rowsDeleted = pst.executeUpdate();

			st = conn.createStatement();

			rs = st.executeQuery("SELECT * FROM \"Bundesland\" ORDER BY \"Bundesland\" ASC;");

			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<html>");
			out.println("<h3> Bundestagswahl Auswertung </h3>");

			while (rs.next()) {
				out.print("<br>");
				out.println(rs.getString(1));
			}
			out.print("<br><br>");
			out.println("<a href='/Bundestagswahl/'>zur&uuml;ck</a>");
			out.println("</html>");
			out.close();

			rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}
