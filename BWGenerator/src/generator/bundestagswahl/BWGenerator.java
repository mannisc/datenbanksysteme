package generator.bundestagswahl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BWGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

			while (rs.next()) {
				System.out.print("");
				System.out.println(rs.getString(1));
			}

			rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// String line;
		// while((line = br.readLine()) != null) {
		// StringTokenizer st = new StringTokenizer(line, ",");
		// String name = st.nextToken();
		// String email = st.nextToken();
		// Integer id = Integer.valueOf(st.nextToken());
		// }

	}

}
