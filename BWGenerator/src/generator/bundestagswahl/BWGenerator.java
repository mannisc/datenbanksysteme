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
			st = conn.createStatement();

			// Bundesland
			try {
				st.executeUpdate("CREATE TABLE \"Bundesland\"( \"Name\" text NOT NULL, CONSTRAINT \"Name\" PRIMARY KEY (\"Name\"))WITH (OIDS=FALSE);");
				st.executeUpdate("ALTER TABLE \"Bundesland\" OWNER TO postgres;");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Wahlkreis
			try {
				st.executeUpdate("CREATE TABLE \"Wahlkreis\"( \"Nummer\" integer , \"Name\" text , \"Population\" integer , \"Bundesland\" text  ,\"Jahr\" integer , CONSTRAINT \"Name\" PRIMARY KEY (\"Name\"))WITH (OIDS=FALSE);");
				st.executeUpdate("ALTER TABLE \"Wahlkreis\" OWNER TO postgres;");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Direktkandidat
			try {
				st.executeUpdate("CREATE TABLE \"Direktkandidat\"(\"Kandidatennummer\" integer NOT NULL,\"Name\" text, \"Partei\" text,\"Jahrgang\" integer, \"Jahr\" integer, CONSTRAINT \"Kandidatennummer\" PRIMARY KEY (\"Kandidatennummer\",\"Jahr\")) WITH ( OIDS=FALSE );");
				st.executeUpdate("ALTER TABLE \"Direktkandidat\" OWNER TO postgres;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("COPY \"Direktkandidat\" FROM 'C:\\Program Files\\PostgreSQL\\9.2\\Dateien\\csv\\wahlbewerber2009.csv' WITH  DELIMITER  ';' CSV; ");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Erststimmen
			try {
				st.executeUpdate("CREATE TABLE \"Erststimmen\"( \"Nummer\" integer ,\"Quantität\" integer , \"Kandidatennummer\" integer, \"Wahlkreis\" integer, \"Jahr\" integer, CONSTRAINT \"Nummer\" PRIMARY KEY (\"Nummer\"))WITH (OIDS=FALSE);");
				st.executeUpdate("ALTER TABLE \"Erststimmen\" OWNER TO postgres;");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Zweitstimmen
			try {
				st.executeUpdate("CREATE TABLE \"Zweitstimmen\"( \"Nummer\" integer ,\"Quantität\" integer , \"Partei\" text, \"Wahlkreis\" integer, \"Jahr\" integer, CONSTRAINT \"Nummer\" PRIMARY KEY (\"Nummer\"))WITH (OIDS=FALSE);");
				st.executeUpdate("ALTER TABLE \"Zweitstimmen\" OWNER TO postgres;");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Partei
			try {
				st.executeUpdate("CREATE TABLE \"Partei\"( \"Name\" text , \"Mitglieder\" integer  ,\"Jahr\" integer , CONSTRAINT \"Name\" PRIMARY KEY (\"Name\"))WITH (OIDS=FALSE);");
				st.executeUpdate("ALTER TABLE \"Partei\" OWNER TO postgres;");
			} catch (SQLException e) {
				e.printStackTrace();
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
