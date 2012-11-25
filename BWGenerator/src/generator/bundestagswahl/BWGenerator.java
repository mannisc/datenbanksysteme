package generator.bundestagswahl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;

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

		// Wahlkreiszuordnung\-nummer bleiben 05/09 gleich
		HashSet<Wahlkreis> wahlkreis = new HashSet<Wahlkreis>();

		// 2005

		// Stimmen pro Wahlkreis aus wkumrechnung2005.csv auslesen
		// und in stimmen2005.csv Einzelstimmen schreiben

		try {

			CSVReader readerWahlbewerber05 = new CSVReader(new BufferedReader(
					new FileReader("csv\\wahlbewerber2005.csv")), ';');

			CSVReader readerErgebnis05 = new CSVReader(new BufferedReader(
					new FileReader("csv\\kerg.csv")), ';');

			CSVWriter writerStimmen05 = new CSVWriter(new FileWriter(
					"csv\\wkumrechnung2009.csv"), ';');

			String[] readLineErgebnis05;
			String[] readLineWahlbewerber05;
			String[] writeLine05; // = "first#second#third".split("#");

			// Wahlbewerber.csv-------------------------------------------

			HashSet<Partei> partei = new HashSet<Partei>();
			HashSet<Politiker> politiker = new HashSet<Politiker>();
			HashSet<Direktkandidat> direktkandidat = new HashSet<Direktkandidat>();
			HashSet<Listenkandidat> listenkandidat = new HashSet<Listenkandidat>();

			int Politikernummer = 1;
			int Kandidatennummer = 1;

			// Anfangszeilen überspringen
			int skipLinesWahlbewerber = 1; // zu überspringende Zeilen am Anfang
			for (int i = 1; i <= skipLinesWahlbewerber; i++) {
				readerWahlbewerber05.readNext();
			}

			while ((readLineWahlbewerber05 = readerWahlbewerber05.readNext()) != null) {
				if (readLineWahlbewerber05.length >= 1) {
					// Politiker
					politiker.add(new Politiker(Politikernummer,
							readLineWahlbewerber05[0]));

					// Parteien
					if (!readLineWahlbewerber05[7].trim().equals("")) {
						partei.add(new Partei(readLineWahlbewerber05[7]));
					}
					// Direktkandidaten
					if (!readLineWahlbewerber05[8].trim().equals("")) {
						direktkandidat.add(new Direktkandidat(Kandidatennummer,
								readLineWahlbewerber05[7], Politikernummer,
								Integer.parseInt(readLineWahlbewerber05[8])));
						Kandidatennummer++;
					}
					// Listenkandidaten
					if (!readLineWahlbewerber05[9].trim().equals("")) {
						listenkandidat.add(new Listenkandidat(
								readLineWahlbewerber05[7],
								readLineWahlbewerber05[9], Integer
										.parseInt(readLineWahlbewerber05[10]),
								Politikernummer));
					}
					Politikernummer++;
				}
			}

			Iterator itr = listenkandidat.iterator();
			System.out.println("Set : ");
			while (itr.hasNext())
				System.out.println(itr.next());
			System.out.println("------------");

			// WKUmrechnung.csv-------------------------------------------

			// HashSet<Stimme> stimme = new HashSet<Stimme>();

			int stimmzettelnummer = 1;

			// Einleseparameter
			int columnWahlkreisNummer = 0; // Spalte der WahlkreisNummer ab 0
			int columnParteienStart = 9; // Startspalte der Stimmen je Kandidat
			int columnParteienStop = 22; // Endspalte der Stimmen je Kandidat
			// writer.writeNext(writeLine);

			// Anfangszeilen überspringen
			int skipLinesWKUmrechnung = 4; // zu überspringende Zeilen am Anfang
			for (int i = 1; i <= skipLinesWKUmrechnung; i++) {
				readerErgebnis05.readNext();
			}

			while ((readLineErgebnis05 = readerErgebnis05.readNext()) != null) {

				// stimme.add(new Stimme(stimmzettelnummer,));
				// stimmzettelnummer++;

				if (!readLineErgebnis05[0].trim().equals("")) {
					wahlkreis.add(new Wahlkreis(Integer
							.parseInt(readLineErgebnis05[0]),
							readLineErgebnis05[2], readLineErgebnis05[1],
							Integer.parseInt(readLineErgebnis05[3])));

				}
			}

			// Iterator itr = wahlkreis.iterator();
			// System.out.println("Set : ");
			// while (itr.hasNext())
			// System.out.println(itr.next());
			// System.out.println("------------");

			writerStimmen05.close();
			readerErgebnis05.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 2009

		// Stimmen pro Wahlkreis aus wkumrechnung2009.csv auslesen
		// und in stimmen2005.csv Einzelstimmen schreiben

		try {

			CSVReader readerWahlbewerber09 = new CSVReader(new BufferedReader(
					new FileReader("csv\\wahlbewerber2009.csv")), ';');

			CSVReader readerErgebnis09 = new CSVReader(new BufferedReader(
					new FileReader("csv\\kerg.csv")), ';');

			CSVWriter writerStimmen09 = new CSVWriter(new FileWriter(
					"csv\\stimmen2009.csv"), ';');

			String[] readLineErgebnis09;
			String[] readLineWahlbewerber09;
			String[] writeLine09; // = "first#second#third".split("#");

			// Wahlbewerber.csv-------------------------------------------

			HashSet<Partei> partei = new HashSet<Partei>();
			HashSet<Politiker> politiker = new HashSet<Politiker>();
			HashSet<Direktkandidat> direktkandidat = new HashSet<Direktkandidat>();
			HashSet<Listenkandidat> listenkandidat = new HashSet<Listenkandidat>();

			int Politikernummer = 1;
			int Kandidatennummer = 1;

			// Anfangszeilen überspringen
			int skipLinesWahlbewerber = 1; // zu überspringende Zeilen am Anfang
			for (int i = 1; i <= skipLinesWahlbewerber; i++) {
				readerWahlbewerber09.readNext();
			}

			while ((readLineWahlbewerber09 = readerWahlbewerber09.readNext()) != null) {
				if (readLineWahlbewerber09.length >= 1) {
					// Politiker
					politiker.add(new Politiker(Politikernummer,
							readLineWahlbewerber09[0] + " "
									+ readLineWahlbewerber09[1]));
					// Parteien
					if (!readLineWahlbewerber09[3].trim().equals("")) {
						partei.add(new Partei(readLineWahlbewerber09[3]));
					}
					// Direktkandidaten
					if (!readLineWahlbewerber09[4].trim().equals("")) {
						direktkandidat.add(new Direktkandidat(Kandidatennummer,
								readLineWahlbewerber09[3], Politikernummer,
								Integer.parseInt(readLineWahlbewerber09[4])));
						Kandidatennummer++;
					}
					// Listenkandidaten
					if (!readLineWahlbewerber09[5].trim().equals("")) {
						listenkandidat.add(new Listenkandidat(
								readLineWahlbewerber09[3],
								readLineWahlbewerber09[5], Integer
										.parseInt(readLineWahlbewerber09[6]),
								Politikernummer));
					}
					Politikernummer++;
				}
			}

			// Iterator itr = partei.iterator();
			// System.out.println("Set : ");
			// while (itr.hasNext())
			// System.out.println(itr.next());
			// System.out.println("------------");

			// WKUmrechnung.csv-------------------------------------------

			// HashSet<Stimme> stimme = new HashSet<Stimme>();

			int stimmzettelnummer = 1;

			// Einleseparameter
			int columnWahlkreisNummer = 0; // Spalte der WahlkreisNummer ab 0
			int columnParteienStart = 9; // Startspalte der Stimmen je Kandidat
			int columnParteienStop = 22; // Endspalte der Stimmen je Kandidat
			// writer.writeNext(writeLine);

			// Anfangszeilen überspringen
			int skipLinesWKUmrechnung = 4; // zu überspringende Zeilen am Anfang
			for (int i = 1; i <= skipLinesWKUmrechnung; i++) {
				readerErgebnis09.readNext();
			}

			while ((readLineErgebnis09 = readerErgebnis09.readNext()) != null) {

				// stimme.add(new Stimme(stimmzettelnummer,));
				// stimmzettelnummer++;

				if (!readLineErgebnis09[0].trim().equals("")) {

				}
			}

			// Iterator itr = wahlkreis.iterator();
			// System.out.println("Set : ");
			// while (itr.hasNext())
			// System.out.println(itr.next());
			// System.out.println("------------");

			writerStimmen09.close();
			readerErgebnis09.close();

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
