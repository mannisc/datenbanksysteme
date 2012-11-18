package generator.bundestagswahl;

import java.io.File;
import java.security.CodeSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

			PreparedStatement pst = conn
					.prepareStatement("INSERT INTO \"Direktkandidat\" values('0','ungültig','ungültig',0);");
			// pst.executeUpdate();
			//
			// int foovalue = 1;
			// pst = conn
			// .prepareStatement("DELETE FROM \"Bundesland\" WHERE \"Population\" = ?");
			// pst.setInt(1, foovalue);
			// int rowsDeleted = pst.executeUpdate();

			//
			// rs =
			// st.executeQuery("SELECT * FROM \"Bundesland\" ORDER BY \"Bundesland\" ASC;");
			//
			// while (rs.next()) {
			// System.out.print("");
			// System.out.println(rs.getString(1));
			// }

			String propertiesFilePath = "client.properties";
			File propertiesFile = new File(propertiesFilePath);

			if (!propertiesFile.exists()) {
				try {
					CodeSource codeSource = BWGenerator.class
							.getProtectionDomain().getCodeSource();
					File jarFile = new File(codeSource.getLocation().toURI()
							.getPath());
					String jarDir = jarFile.getParentFile().getPath();
					propertiesFile = new File(jarDir
							+ System.getProperty("file.separator")
							+ propertiesFilePath);
				} catch (Exception ex) {
				}
			}

			System.out.println(propertiesFile.getParent());

			rs = st.executeQuery("COPY \"Direktkandidat\" FROM 'C:\\Program Files\\PostgreSQL\\9.2\\Dateien\\csv\\wahlbewerber2009.csv' WITH  DELIMITER  ';' CSV; ");
			rs.close();
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
