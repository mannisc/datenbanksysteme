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
		String url = "jdbc:postgresql://localhost/Bundestagswahl?user=user&password=1234";
		Connection conn;
		Statement st;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(url);
			st = conn.createStatement();

			rs = st.executeQuery("SELECT * FROM \"Bundesland\" ORDER BY \"Bundesland\" ASC;");

			while (rs.next()) {

				System.out.println(rs.getString(1));
			}

			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.print(" ");

		// try {
		// BufferedReader br = new BufferedReader(new FileReader(
		// "csv\\wahlbewerber2009.csv"));
		//
		// CSVReader reader = new CSVReader(br, ';');
		// String[] line;
		// while ((line = reader.readNext()) != null) {
		// // System.out.println(line[0] + "  " + line[1] + "  " + line[2]
		// // + "  " + line[3]);
		//
		// }
		//
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// String line;
		// while((line = br.readLine()) != null) {
		// StringTokenizer st = new StringTokenizer(line, ",");
		// String name = st.nextToken();
		// String email = st.nextToken();
		// Integer id = Integer.valueOf(st.nextToken());
		// }

	}
}
