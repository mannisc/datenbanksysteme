package testbw.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import testbw.client.SetupStaticDBService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SetupStaticDBServiceImpl extends RemoteServiceServlet implements
		SetupStaticDBService {

	@Override
	public String setupStaticDB(String url) {

		System.out.println("jdbc:postgresql://localhost/" + url.split(";")[0]
				+ "?user=" + url.split(";")[1] + "&password="
				+ url.split(";")[2]);

		String postgresqlurl = "jdbc:postgresql://localhost/"
				+ url.split(";")[0] + "?user=" + url.split(";")[1]
				+ "&password=" + url.split(";")[2];
		Connection conn;
		Statement st;
		try {

			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(postgresqlurl);
			st = conn.createStatement();

			// Siehe: setup_static.sql

			// -- SitzeProJahr [Mock]
			// -- Typ: vordefiniert
			// -- Verweist auf: -
			st.executeUpdate("DROP TABLE IF EXISTS sitzeprojahr CASCADE;");
			st.executeUpdate("CREATE TABLE sitzeprojahr ( jahr INTEGER PRIMARY KEY, sitze INTEGER NOT NULL);");
			st.executeUpdate("INSERT INTO sitzeprojahr VALUES ('2013', '15');");

			// -- StimmenProPartei [Mock]
			// -- Typ: wird berechnet (mehrmals) -> Initialisiere mit 0
			// -- Verweist auf: -
			st.executeUpdate("DROP TABLE IF EXISTS sitzepropartei CASCADE;");
			st.executeUpdate("DROP TABLE IF EXISTS stimmenpropartei CASCADE;");
			st.executeUpdate("CREATE TABLE stimmenpropartei ( partei CHARACTER VARYING PRIMARY KEY, anzahl INTEGER);");
			st.executeUpdate("INSERT INTO stimmenpropartei VALUES ('X', '5200'), ('Y', '1700'), ('Z', '3100');");

			// -- Divisoren
			// -- Typ: vordefiniert
			// -- Verweist auf: -
			st.executeUpdate("DROP TABLE IF EXISTS divisoren CASCADE;");
			st.executeUpdate("DROP SEQUENCE IF EXISTS divisoren_div_seq CASCADE;");
			st.executeUpdate("CREATE TABLE divisoren ( div SERIAL PRIMARY KEY);");
			st.executeUpdate("ALTER SEQUENCE divisoren_div_seq RESTART WITH 1 INCREMENT BY 2;");

			// -- Trigger Divisoren -> Divisoren
			// -- Typ: vordefiniert
			// -- Verweist auf: sitzeprojahr
			st.executeUpdate("DROP TRIGGER IF EXISTS fill_divisoren ON divisoren CASCADE;");
			st.executeUpdate("DROP FUNCTION IF EXISTS trigfill() CASCADE;");

			st.executeUpdate("CREATE OR REPLACE FUNCTION trigfill() RETURNS trigger AS $$"
					+ "DECLARE divcount INTEGER; "
					+ "DECLARE sitzecount INTEGER; "
					+ "BEGIN "
					+ "  SELECT COUNT(*) INTO divcount FROM divisoren; "
					+ "  SELECT sitze INTO sitzecount FROM sitzeprojahr WHERE jahr = 2013; "
					+ "  IF divcount < sitzecount THEN "
					+ "  INSERT INTO divisoren VALUES(DEFAULT); "
					+ "  END IF; "
					+ "  RETURN NEW; "
					+ "END; "
					+ "$$ LANGUAGE plpgsql;");

			st.executeUpdate("CREATE TRIGGER fill_divisoren "
					+ "AFTER INSERT ON divisoren " + "FOR EACH ROW "
					+ "EXECUTE PROCEDURE trigfill();");

			// -- ItrErgebnisse
			// -- Typ: vordefiniert
			// -- Verweist auf: -
			st.executeUpdate("DROP TABLE IF EXISTS itrergebnisse CASCADE;");
			st.executeUpdate("CREATE TABLE itrergebnisse ( partei CHARACTER VARYING NOT NULL, anzahl NUMERIC NOT NULL, PRIMARY KEY(partei, anzahl));");

			// -- Trigger Divisoren -> ItrErgebnisse
			// -- Typ: vordefiniert
			// -- Verweist auf: stimmenpropartei
			st.executeUpdate("DROP TRIGGER IF EXISTS berechne_ItrErgebnisse ON divisoren CASCADE;");
			st.executeUpdate("DROP FUNCTION IF EXISTS berechneitr() CASCADE;");
			st.executeUpdate("CREATE OR REPLACE FUNCTION berechneitr() RETURNS trigger AS $$ "
					+ "BEGIN "
					+ "  INSERT INTO itrergebnisse (SELECT partei, (anzahl / NEW.div::float8) AS anzahl FROM stimmenpropartei); "
					+ "  RETURN NEW; " + "END; " + "$$ LANGUAGE plpgsql;");

			st.executeUpdate("CREATE TRIGGER berechne_ItrErgebnisse "
					+ "AFTER INSERT ON divisoren " + "FOR EACH ROW "
					+ "EXECUTE PROCEDURE berechneitr();");

			// -- Load: Divisoren
			// -- Typ: Vorberechnung
			// -- Verweist auf: divisoren
			st.executeUpdate("INSERT INTO divisoren VALUES (DEFAULT);");

			st.close();

			return "Setup successful.";

		} catch (SQLException e) {
			e.printStackTrace();
			return "Setup unsuccessful.";
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "Setup unsuccessful. Check JDBC Driver declaration on server side.";
		}
	}
}
