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
				st.executeUpdate("DROP TABLE bundesland;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE bundesland(bundeslandnummer integer, name text NOT NULL,  PRIMARY KEY (bundeslandnummer))WITH (OIDS=FALSE);");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Wahlkreis
			try {
				st.executeUpdate("DROP TABLE wahlkreis;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {

				st.executeUpdate("CREATE TABLE wahlkreis( jahr integer , wahlkreisnummer integer, name text, wahlberechtigte integer, bundesland text , PRIMARY KEY (jahr,wahlkreisnummer))WITH (OIDS=FALSE);");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Direktkandidat
			try {
				st.executeUpdate("DROP TABLE direktkandidat;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE direktkandidat(jahr integer,kandidatennummer integer, politiker integer, partei int, wahlkreis integer, PRIMARY KEY (jahr,kandidatennummer)) WITH ( OIDS=FALSE );");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Listenkandidat
			try {
				st.executeUpdate("DROP TABLE listenkandidat;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE listenkandidat(jahr integer,partei int, bundesland text, listenplatz integer, politiker integer, PRIMARY KEY (jahr,partei,bundesland,listenplatz)) WITH ( OIDS=FALSE );");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Partei
			try {
				st.executeUpdate("DROP TABLE partei;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE partei( parteinummer integer,  name text , PRIMARY KEY (parteinummer))WITH (OIDS=FALSE);");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Politiker
			try {
				st.executeUpdate("DROP TABLE politiker;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeUpdate("CREATE TABLE politiker(  politikernummer integer, name text , PRIMARY KEY (politikernummer))WITH (OIDS=FALSE);");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Stimmen
			try {
				st.executeUpdate("DROP TABLE stimme;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {

				st.executeUpdate("CREATE TABLE stimme( jahr integer, stimmzettelnummer integer, kandidatennummer integer, partei int, bundesland text, PRIMARY KEY (Jahr,Stimmzettelnummer))WITH (OIDS=FALSE);");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Erststimmen
			// try {
			// st.executeUpdate("DROP TABLE Erststimmen;");
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			// try {
			// st.executeUpdate("CREATE TABLE Erststimmen( Nummer integer ,Quantität integer , Kandidatennummer integer, Wahlkreis integer, Jahr integer, PRIMARY KEY (Nummer))WITH (OIDS=FALSE);");
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			//
			// // Zweitstimmen
			// try {
			// st.executeUpdate("DROP TABLE Zweitstimmen;");
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			// try {
			// st.executeUpdate("CREATE TABLE Zweitstimmen( Nummer integer ,Quantität integer , partei int, Wahlkreis integer, Jahr integer, PRIMARY KEY (Nummer))WITH (OIDS=FALSE);");
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }

			// try {
			// st.executeUpdate("COPY Direktkandidat FROM 'C:\\Program Files\\PostgreSQL\\9.2\\Dateien\\csv\\wahlbewerber2009.csv' WITH  DELIMITER  ';' CSV; ");
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }

			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.print(" ");

	}
}
