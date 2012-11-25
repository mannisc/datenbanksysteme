package generator.bundestagswahl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

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

		// 2009

		// Stimmen pro Wahlkreis aus wkumrechnung2009.csv auslesen
		// und in stimmen2099.csv Einzelstimmen schreiben

		try {

			CSVReader reader = new CSVReader(new BufferedReader(new FileReader(
					"csv\\wkumrechnung2009.csv")), ';');

			CSVWriter writer = new CSVWriter(new FileWriter(
					"csv\\stimmen2009.csv"), ';');

			String[] readLine;
			String[] writeLine; // = "first#second#third".split("#");

			// Einleseparameter

			int skipLines = 0; // zu überspringende Zeilen am Anfang
			int columnWahlkreisNummer = 0; // Spalte der WahlkreisNummer ab 0
			int columnParteienStart = 9; // Startspalte der Stimmen je Kandidat
			int columnParteienStop = 22; // Endspalte der Stimmen je Kandidat

			int WahlkreisNummer;
			String Partei;

			// Anfangszeilen überspringen
			for (int i = 0; i < skipLines; i++) {
				reader.readNext();
			}

			// writer.writeNext(writeLine);

			while ((readLine = reader.readNext()) != null) {

				if (readLine.length >= columnParteienStop)
					System.out.println(readLine[0] + "  " + readLine[1] + "  "
							+ readLine[2] + "  " + readLine[3]);

			}

			writer.close();
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// try {
		// conn = DriverManager.getConnection(url);
		// st = conn.createStatement();
		//
		// rs =
		// st.executeQuery("SELECT * FROM \"Bundesland\" ORDER BY \"Bundesland\" ASC;");
		//
		// while (rs.next()) {
		//
		// System.out.println(rs.getString(1));
		// }
		//
		// st.close();
		//
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		//
		// System.out.print(" ");

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
