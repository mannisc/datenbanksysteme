package bundestagswahl.setup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BWSetupDatabse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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

			// Bundesland
			try {
				st.executeUpdate("DROP TABLE \"Bundesland\";");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE \"Bundesland\"( \"Name\" text NOT NULL,  PRIMARY KEY (\"Name\"))WITH (OIDS=FALSE);");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Wahlkreis
			try {
				st.executeUpdate("DROP TABLE \"Wahlkreis\";");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE \"Wahlkreis\"( \"Nummer\" integer , \"Name\" text , \"Population\" integer , \"Bundesland\" text  ,\"Jahr\" integer , PRIMARY KEY (\"Name\"))WITH (OIDS=FALSE);");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Direktkandidat
			try {
				st.executeUpdate("DROP TABLE \"Direktkandidat\";");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE \"Direktkandidat\"(\"Kandidatennummer\" integer NOT NULL,\"Name\" text, \"Partei\" text,\"Jahrgang\" integer, \"Jahr\" integer, PRIMARY KEY (\"Kandidatennummer\",\"Jahr\")) WITH ( OIDS=FALSE );");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// try {
			// st.executeUpdate("COPY \"Direktkandidat\" FROM 'C:\\Program Files\\PostgreSQL\\9.2\\Dateien\\csv\\wahlbewerber2009.csv' WITH  DELIMITER  ';' CSV; ");
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }

			// Erststimmen
			try {
				st.executeUpdate("DROP TABLE \"Erststimmen\";");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE \"Erststimmen\"( \"Nummer\" integer ,\"Quantität\" integer , \"Kandidatennummer\" integer, \"Wahlkreis\" integer, \"Jahr\" integer, PRIMARY KEY (\"Nummer\"))WITH (OIDS=FALSE);");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Zweitstimmen
			try {
				st.executeUpdate("DROP TABLE \"Zweitstimmen\";");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE \"Zweitstimmen\"( \"Nummer\" integer ,\"Quantität\" integer , \"Partei\" text, \"Wahlkreis\" integer, \"Jahr\" integer, PRIMARY KEY (\"Nummer\"))WITH (OIDS=FALSE);");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Partei
			try {
				st.executeUpdate("DROP TABLE \"Partei\";");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE \"Partei\"( \"Name\" text , \"Mitglieder\" integer  ,\"Jahr\" integer , PRIMARY KEY (\"Name\"))WITH (OIDS=FALSE);");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.print(" ");

	}

}
