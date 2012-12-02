package bundestagswahl.servlet;

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
		String url = "jdbc:postgresql://localhost/Bundestagswahl?user=user&password=1234";
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

			// Stimmenverteilung abfragen--------

			st.executeUpdate("CREATE TABLE SitzeProJahr ( Jahr INTEGER PRIMARY KEY, Sitze INTEGER NOT NULL);");

			st.executeUpdate("INSERT INTO SitzeProJahr VALUES (�2005�, �598�), (�2009�, �598�), (�2013�, �700�);");

			st.executeUpdate("CREATE OR REPLACE TABLE Divisoren ( div SERIAL PRIMARY KEY);");

			st.executeUpdate("ALTER SEQUENCE Divisoren_div_seq RESTART WITH 1 INCREMENT BY 2;");

			st.executeUpdate("CREATE TRIGGER fill_divisoren AFTER INSERT ON Divisoren FOR EACH ROW WHEN ( (SELECT COUNT(*) FROM Divisoren) < (SELECT Sitze FROM SitzeProJahr WHERE Jahr = #JahrInput)) BEGIN INSERT INTO Divisoren VALUES (DEFAULT) END;");

			st.executeUpdate("CREATE OR REPLACE VIEW StimmenProPartei as ( SELECT Partei, COUNT(*) as AnzahlStimmen FROM Stimme WHERE Jahr = #InputJahr AND Partei IS NOT NULL GROUP BY Partei HAVING  COUNT(*) >= (0.05 * (SELECT COUNT(*) FROM Stimme S WHERE s.Jahr = #JahrInput AND s.Partei IS NOT NULL)));");

			// Aggregierte Ergebnisse verwenden

			// CREATE OR REPLACE VIEW StimmenProPartei as (
			// SELECT Partei, SUM(Quantitaet) as AnzahlStimmen
			// FROM Zweitstimmen
			// WHERE Jahr = #InputJahr AND Partei IS NOT NULL
			// GROUP BY Partei
			// HAVING SUM(Quantitaet) >= (0.05 * (SELECT SUM(Quantitaet)
			// FROM Zweitstimme z
			// WHERE z.Jahr = #JahrInput AND
			// z.Partei IS NOT NULL)))

			st.executeUpdate("CREATE OR REPLACE TABLE ItrErgebnisse ( Partei STRING,  Anzahl NUMERIC, PRIMARY KEY(Partei, Anzahl));");

			st.executeUpdate("CREATE OR REPLACE TRIGGER berechne_ItrErgebnisse AFTER INSERT ON Divisoren FOR EACH ROW BEGIN INSERT INTO ItrErgebnisse (SELECT Name, AnzahlStimmen / (NEW.div::float8) AS Anzahl FROM StimmenProPartei)END;");

			st.executeUpdate("CREATE OR REPLACE TABLE Divisoren ( div SERIAL PRIMARY KEY);");

			st.executeUpdate("ALTER SEQUENCE Divisoren_div_seq RESTART WITH 1 INCREMENT BY 2;");

			st.executeUpdate("INSERT INTO Divisoren VALUES (DEFAULT);");

			st.executeUpdate("CREATE VIEW Ergebnis AS (WITH T AS (SELECT * FROM ItrErgebnisse ORDER BY Anzahl DESC LIMIT (SELECT Sitze FROM SitzeProJahr WHERE Jahr = #JahrInput));");

			// Abfrage des Endergebnisses
			rs = st.executeQuery("SELECT Partei, COUNT(*) as AnzahlSitze FROM T GROUP BY Partei);");

			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<html>");
			out.println("<h3> Bundestagswahl Auswertung </h3>");

			while (rs.next()) {
				out.print("<br>");
				out.println(rs.getString(1) + " " + rs.getString(2));
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
