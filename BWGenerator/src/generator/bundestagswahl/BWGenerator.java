package generator.bundestagswahl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
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
					new InputStreamReader(new FileInputStream(
							"csv\\wahlbewerber2005.csv"), "UTF-8")), ';');

			// new FileReader("csv\\wahlbewerber2005.csv")

			CSVReader readerErgebnis05 = new CSVReader(new BufferedReader(
					new InputStreamReader(new FileInputStream(
							"csv\\wkumrechnung2009.csv"), "UTF-8")), ';');

			// new FileReader("csv\\wkumrechnung2009.csv")

			CSVWriter writerStimmen05 = new CSVWriter(new FileWriter(
					"csv\\stimmen2005.csv"), ';');

			String[] readLineErgebnis05;
			String[] readLineWahlbewerber05;

			// Wahlbewerber.csv-------------------------------------------

			HashSet<Partei> parteien = new HashSet<Partei>();
			HashSet<Politiker> politiker = new HashSet<Politiker>();
			HashSet<Direktkandidat> direktkandidaten = new HashSet<Direktkandidat>();
			HashSet<Listenkandidat> listenkandidaten = new HashSet<Listenkandidat>();

			int Politikernummer = 1;
			int Kandidatennummer = 1;

			// Anfangszeilen überspringen
			int skipLinesWahlbewerber = 1; // zu überspringende Zeilen am Anfang
			for (int i = 1; i <= skipLinesWahlbewerber; i++) {
				readerWahlbewerber05.readNext();
			}

			while ((readLineWahlbewerber05 = readerWahlbewerber05.readNext()) != null) {
				if (readLineWahlbewerber05.length >= 1) {

					int parteinummer = 0;

					// Politiker hinzufügen
					politiker.add(new Politiker(Politikernummer,
							readLineWahlbewerber05[0]));

					// Parteien hinzufügen
					if (!readLineWahlbewerber05[7].trim().equals("")) {
						Partei partei = new Partei(readLineWahlbewerber05[7]);
						parteien.add(partei);
						partei.parteinummer = parteien.size();
						parteinummer = partei.parteinummer;
					}

					// Direktkandidaten hinzufügen
					if (!readLineWahlbewerber05[8].trim().equals("")) {
						direktkandidaten.add(new Direktkandidat(
								Kandidatennummer, parteinummer,
								Politikernummer, Integer
										.parseInt(readLineWahlbewerber05[8])));
						Kandidatennummer++;
					}
					// Listenkandidaten hinzufügen
					if (!readLineWahlbewerber05[9].trim().equals("")) {
						listenkandidaten.add(new Listenkandidat(parteinummer,
								readLineWahlbewerber05[9], Integer
										.parseInt(readLineWahlbewerber05[10]),
								Politikernummer));
					}
					Politikernummer++;
				}
			}

			// Ergebnisse in Tabellen schreiben

			Iterator itrprint = parteien.iterator();
			System.out.println("Set : ");
			while (itrprint.hasNext())
				System.out.println(itrprint.next());
			System.out.println("------------");

			try {
				conn = DriverManager.getConnection(url);
				st = conn.createStatement();

				Iterator<Politiker> itrPolitiker = politiker.iterator();
				while (itrPolitiker.hasNext()) {
					st.executeUpdate("INSERT INTO politiker VALUES ("
							+ itrPolitiker.next() + ");");
				}

				Iterator<Partei> itrPartei = parteien.iterator();
				while (itrPartei.hasNext()) {
					st.executeUpdate("INSERT INTO partei VALUES ("
							+ itrPartei.next() + ");");
				}

				Iterator<Direktkandidat> itrDirektkandidat = direktkandidaten
						.iterator();
				while (itrDirektkandidat.hasNext()) {
					st.executeUpdate("INSERT INTO direktkandidat VALUES (2005,"
							+ itrDirektkandidat.next() + ");");
				}

				Iterator<Listenkandidat> itrListenkandidat = listenkandidaten
						.iterator();
				while (itrListenkandidat.hasNext()) {
					Listenkandidat tmp = itrListenkandidat.next();
					System.out.println(tmp);
					st.executeUpdate("INSERT INTO listenkandidat VALUES (2005,"
							+ tmp + ");");
				}

				st.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

			// WKUmrechnung.csv-------------------------------------------

			// HashSet<Stimme> stimme = new HashSet<Stimme>();

			// Anfangszeilen überspringen und Header auslesen

			readerErgebnis05.readNext();
			readerErgebnis05.readNext();
			readLineErgebnis05 = readerErgebnis05.readNext();
			readerErgebnis05.readNext();

			// Spaltenbeschriftung einlesen und Spalten Parteien zuordnen
			HashMap<Integer, String> parteienSpalte = new HashMap<Integer, String>();
			for (int i = 9; i < 73; i = i + 2) {
				parteienSpalte.put(i, readLineErgebnis05[i]);
			}

			// temporäre Variablen
			int stimmzettelnummer = 1;
			int aktuelleKandidatennummer = 0;

			while ((readLineErgebnis05 = readerErgebnis05.readNext()) != null) {

				if (!readLineErgebnis05[0].trim().equals("")
						&& !readLineErgebnis05[2].trim().equals("")
						&& !readLineErgebnis05[2].equals("Land")
						&& !readLineErgebnis05[2].equals("Insgesamt")) {

					// Neuen Wahlkreis hinzufügen

					int wahlkreisnummer = Integer
							.parseInt(readLineErgebnis05[0]);
					String wahlkreisname = readLineErgebnis05[2];
					String bundesland = readLineErgebnis05[1];
					int wahlberechtigte = Integer
							.parseInt(readLineErgebnis05[3]);

					wahlkreis.add(new Wahlkreis(wahlkreisnummer, wahlkreisname,
							bundesland, wahlberechtigte));

					// for (int i = 9; i < 73; i = i + 2) {
					int i = 9;

					int erststimmenAnzahl = Integer
							.parseInt(readLineErgebnis05[i]);
					// aktuelleKandidatennummer

					System.out.println("!!!!!!!!!!" + readLineErgebnis05[1]
							+ ", " + erststimmenAnzahl);

					for (int j = 0; j < erststimmenAnzahl; j++) {

						String[] writeLine05 = { "2005",
								Integer.toString(stimmzettelnummer),
								Integer.toString(aktuelleKandidatennummer),
								parteienSpalte.get(i),
								Integer.toString(erststimmenAnzahl) };

						writerStimmen05.writeNext(writeLine05);

						stimmzettelnummer++;
					}

					// }

				}
			}

			writerStimmen05.close();
			readerErgebnis05.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// // 2009
		//
		// // Stimmen pro Wahlkreis aus wkumrechnung2009.csv auslesen
		// // und in stimmen2005.csv Einzelstimmen schreiben
		//
		// try {
		//
		// CSVReader readerWahlbewerber09 = new CSVReader(new BufferedReader(
		// new FileReader("csv\\wahlbewerber2009.csv")), ';');
		//
		// CSVReader readerErgebnis09 = new CSVReader(new BufferedReader(
		// new FileReader("csv\\kerg.csv")), ';');
		//
		// CSVWriter writerStimmen09 = new CSVWriter(new FileWriter(
		// "csv\\stimmen2009.csv"), ';');
		//
		// String[] readLineErgebnis09;
		// String[] readLineWahlbewerber09;
		// String[] writeLine09; // = "first#second#third".split("#");
		//
		// // Wahlbewerber.csv-------------------------------------------
		//
		// HashSet<Partei> partei = new HashSet<Partei>();
		// HashSet<Politiker> politiker = new HashSet<Politiker>();
		// HashSet<Direktkandidat> direktkandidat = new
		// HashSet<Direktkandidat>();
		// HashSet<Listenkandidat> listenkandidat = new
		// HashSet<Listenkandidat>();
		//
		// int Politikernummer = 1;
		// int Kandidatennummer = 1;
		//
		// // Anfangszeilen überspringen
		// int skipLinesWahlbewerber = 1; // zu überspringende Zeilen am Anfang
		// for (int i = 1; i <= skipLinesWahlbewerber; i++) {
		// readerWahlbewerber09.readNext();
		// }
		//
		// while ((readLineWahlbewerber09 = readerWahlbewerber09.readNext()) !=
		// null) {
		// if (readLineWahlbewerber09.length >= 1) {
		// // Politiker
		// politiker.add(new Politiker(Politikernummer,
		// readLineWahlbewerber09[0] + " "
		// + readLineWahlbewerber09[1]));
		// // Parteien
		// if (!readLineWahlbewerber09[3].trim().equals("")) {
		// partei.add(new Partei(readLineWahlbewerber09[3]));
		// }
		// // Direktkandidaten
		// if (!readLineWahlbewerber09[4].trim().equals("")) {
		// direktkandidat.add(new Direktkandidat(Kandidatennummer,
		// readLineWahlbewerber09[3], Politikernummer,
		// Integer.parseInt(readLineWahlbewerber09[4])));
		// Kandidatennummer++;
		// }
		// // Listenkandidaten
		// if (!readLineWahlbewerber09[5].trim().equals("")) {
		// listenkandidat.add(new Listenkandidat(
		// readLineWahlbewerber09[3],
		// readLineWahlbewerber09[5], Integer
		// .parseInt(readLineWahlbewerber09[6]),
		// Politikernummer));
		// }
		// Politikernummer++;
		// }
		// }
		//
		// // Ergebnisse in Tabelle schreiben
		//
		// try {
		// conn = DriverManager.getConnection(url);
		// st = conn.createStatement();
		//
		// Iterator itr = politiker.iterator();
		// while (itr.hasNext())
		// st.executeUpdate("INSERT INTO \"Politiker\" VALUES (\"2009\",\""
		// + itr.next() + "\")");
		//
		// itr = partei.iterator();
		// while (itr.hasNext())
		// st.executeUpdate("INSERT INTO \"Partei\" VALUES (\"2009\",\""
		// + itr.next() + "\")");
		// st.close();
		//
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		//
		// // Iterator itr = partei.iterator();
		// // System.out.println("Set : ");
		// // while (itr.hasNext())
		// // System.out.println(itr.next());
		// // System.out.println("------------");
		//
		// // WKUmrechnung.csv-------------------------------------------
		//
		// // HashSet<Stimme> stimme = new HashSet<Stimme>();
		//
		// readerErgebnis09.readNext();
		// readerErgebnis09.readNext();
		// readerErgebnis09.readNext();
		// readerErgebnis09.readNext();
		//
		// while ((readLineErgebnis09 = readerErgebnis09.readNext()) != null) {
		//
		// // stimme.add(new Stimme(stimmzettelnummer,));
		// // stimmzettelnummer++;
		//
		// if (!readLineErgebnis09[0].trim().equals("")) {
		//
		// }
		// }
		//
		// // Iterator itr = wahlkreis.iterator();
		// // System.out.println("Set : ");
		// // while (itr.hasNext())
		// // System.out.println(itr.next());
		// // System.out.println("------------");
		//
		// writerStimmen09.close();
		// readerErgebnis09.close();
		//
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}

// ---nützlicher Code

// rs =
// st.executeQuery("SELECT * FROM \"Bundesland\" ORDER BY \"Bundesland\" ASC;");
// while (rs.next()) {
// System.out.println(rs.getString(1));
// }