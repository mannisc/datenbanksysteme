package generator.bundestagswahl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ProgressMonitorInputStream;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import bundestagswahl.setup.BWSetupDatabase;

public class BWGenerator {

	// public static String progPfad = System.getProperty("user.dir");
	// public static String homePfad = System.getProperty("user.home");

	public static String ergebnis05Pfad = "csv\\kerg2005.csv";
	public static String ergebnis09Pfad = "csv\\kerg2009.csv";

	public static String stimmen05Pfad = "csv\\stimmen2005.csv";
	public static String stimmen09Pfad = "csv\\stimmen2009.csv";

	public static boolean setupDatabase = true;
	public static boolean generateStimmen = true;

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

		// Datenbank neu aufsetzen
		if (setupDatabase) {
			String[] init = { "" };
			BWSetupDatabase.main(init);
		}

		// Stimmen pro Wahlkreis aus wkumrechnung200x.csv auslesen
		// und in stimmen200x.csv Einzelstimmen schreiben

		try {
			try {
				conn = DriverManager.getConnection(url);
				st = conn.createStatement();
				CSVReader readerErgebnis[] = new CSVReader[2];
				CSVWriter writerStimmen[] = new CSVWriter[2];

				if (generateStimmen) {
					readerErgebnis[0] = new CSVReader(new BufferedReader(
							new InputStreamReader(new FileInputStream(
									ergebnis05Pfad), "UTF-8")), ';');
					readerErgebnis[1] = new CSVReader(new BufferedReader(
							new InputStreamReader(new FileInputStream(
									ergebnis09Pfad), "UTF-8")), ';');
					writerStimmen[0] = new CSVWriter(new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(
									stimmen05Pfad), "UTF-8")), ';');
					writerStimmen[1] = new CSVWriter(new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(
									stimmen09Pfad), "UTF-8")), ';');
				}

				for (int jahr = 0; jahr < 2; jahr++) {
					String jahrName = Integer.toString(2005 + jahr * 4);

					if (generateStimmen) {
						System.out.println("\nGenerating started");
						String[] readLineErgebnis;

						// kerg.csv------------------------------------

						// Anfangszeilen überspringen und Header auslesen

						readerErgebnis[jahr].readNext();
						readerErgebnis[jahr].readNext();
						readLineErgebnis = readerErgebnis[jahr].readNext();
						readerErgebnis[jahr].readNext();
						readerErgebnis[jahr].readNext();

						// Beschriftung einlesen und Spalten Parteien zuordnen
						HashMap<Integer, String> parteienSpalte = new HashMap<Integer, String>();
						for (int i = 19; i < 132; i = i + 4) {
							parteienSpalte.put(i, readLineErgebnis[i]);
						}

						// Benötige Variablen
						int stimmzettelnummer = 1;
						int aktuelleKandidatennummer = 0;
						int aktuelleParteinummer = 0;

						while ((readLineErgebnis = readerErgebnis[jahr]
								.readNext()) != null) {

							if (!readLineErgebnis[0].trim().equals("")
									&& !readLineErgebnis[2].trim().equals("")
									&& !readLineErgebnis[2].equals("99")) {

								int wahlkreisnummer = Integer
										.parseInt(readLineErgebnis[0]);

								String wahlkreisname = readLineErgebnis[1];
								System.out.println("\n" + wahlkreisname);// Ladebalken

								int aktelleBundeslandnummer = Integer
										.parseInt(getQueryResult(
												st,
												rs,
												"SELECT bundesland FROM wahlkreis WHERE jahr = "
														+ jahrName
														+ " AND wahlkreisnummer = "
														+ wahlkreisnummer + ";"));

								for (int i = 19; i < 132; i = i + 4) {

									System.out.print(".");// Ladebalken

									String partei = parteienSpalte.get(i);

									int erststimmenAnzahl = 0;
									if (!readLineErgebnis[i].equals(""))
										erststimmenAnzahl = Integer
												.parseInt(readLineErgebnis[i]);

									if (erststimmenAnzahl > 0) {

										aktuelleParteinummer = Integer
												.parseInt(getQueryResult(st,
														rs,
														"SELECT parteinummer FROM partei WHERE name = '"
																+ partei + "';"));

										aktuelleKandidatennummer = Integer
												.parseInt(getQueryResult(
														st,
														rs,
														"SELECT kandidatennummer FROM direktkandidat WHERE jahr = "
																+ jahrName
																+ " AND wahlkreis = "
																+ wahlkreisnummer
																+ " AND partei = "
																+ aktuelleParteinummer
																+ ";"));

										for (int j = 0; j < erststimmenAnzahl; j++) {
											String[] writeLine = {
													jahrName,
													Integer.toString(stimmzettelnummer),
													Integer.toString(aktuelleKandidatennummer),
													Integer.toString(aktuelleParteinummer),
													Integer.toString(aktelleBundeslandnummer) };

											writerStimmen[jahr]
													.writeNext(writeLine);
											stimmzettelnummer++;
										}
									}
								}
							}
						}

						writerStimmen[jahr].close();
						readerErgebnis[jahr].close();

						System.out.println("\nGenerating finished");
					}

					SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
					System.out.println("\nCopying started: "
							+ format.format(new Date()));

					// Bulk Load der
					// Stimmen----------------------------------------
					CopyManager copyManager = new CopyManager(
							(BaseConnection) conn);
					FileReader fileReader = new FileReader(stimmen05Pfad);

					String actPfad;

					switch (jahr) {
					case 0:
						actPfad = stimmen05Pfad;
						break;
					default:
						actPfad = stimmen09Pfad;
						break;
					}

					ProgressMonitorInputStream progressMonitorInputStream = new ProgressMonitorInputStream(
							null, "Stimmen laden",
							new FileInputStream(actPfad) {

								private long gelesenByte = 0;
								private long diffGelesen = 0;
								private long zuLesen = 0;
								DecimalFormat fromat = new DecimalFormat(
										"#0.00");

								public int read() throws IOException {
									update(1);
									return super.read();
								}

								public int read(byte[] b) throws IOException {
									update(1);
									return super.read(b);
								}

								public int read(byte[] b, int off, int len)
										throws IOException {
									update(len);
									return super.read(b, off, len);
								}

								public void update(int len) throws IOException {

									if (gelesenByte == 0)
										zuLesen = super.available();

									gelesenByte = gelesenByte + len;
									diffGelesen = diffGelesen + len;
									if (diffGelesen > 1024 * 1024) {
										diffGelesen = 0;
										System.out.println(gelesenByte
												/ 1024
												/ 1024
												+ "MB von "
												+ zuLesen
												/ 1024
												/ 1024
												+ " MB - "
												+ fromat.format((double) gelesenByte
														/ zuLesen * 100.0)
												+ "%");
									}
								}

							});

					InputStream in = new BufferedInputStream(
							progressMonitorInputStream);

					copyManager
							.copyIn("COPY public.stimme FROM STDIN WITH DELIMITER ';' CSV",
									in);
					fileReader.close();
					System.out.println("\nCopying finished");
					System.out.println("\nAdding Constraints");

					st.executeUpdate("ALTER TABLE wahlberechtigte  ADD CONSTRAINT wahlkreis FOREIGN KEY (jahr,wahlkreis) REFERENCES wahlkreis(jahr,wahlkreisnummer);");
					st.executeUpdate("ALTER TABLE direktkandidat  ADD CONSTRAINT politiker FOREIGN KEY (politiker) REFERENCES politiker;");
					st.executeUpdate("ALTER TABLE direktkandidat  ADD CONSTRAINT partei FOREIGN KEY (partei) REFERENCES partei;");
					st.executeUpdate("ALTER TABLE direktkandidat  ADD CONSTRAINT wahlkreis FOREIGN KEY (jahr,wahlkreis) REFERENCES wahlkreis(jahr,wahlkreisnummer);");
					st.executeUpdate("ALTER TABLE listenkandidat  ADD CONSTRAINT partei FOREIGN KEY (partei) REFERENCES partei;");
					st.executeUpdate("ALTER TABLE listenkandidat  ADD CONSTRAINT bundesland FOREIGN KEY (bundesland) REFERENCES bundesland;");
					st.executeUpdate("ALTER TABLE listenkandidat  ADD CONSTRAINT politiker FOREIGN KEY (politiker) REFERENCES politiker;");
					st.executeUpdate("ALTER TABLE stimme  ADD CONSTRAINT bundesland FOREIGN  KEY (bundesland) REFERENCES bundesland;");
					st.executeUpdate("ALTER TABLE stimme  ADD CONSTRAINT kandidatennummer FOREIGN KEY (kandidatennummer) REFERENCES direktkandidat(kandidatennummer);");

					System.out.println("\nFinished");

				}

				st.close();
				conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getQueryResult(Statement st, ResultSet rs, String query)
			throws SQLException {
		String returnString = "";
		rs = st.executeQuery(query);
		if (rs.next()) {
			try {
				returnString = rs.getString(1);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return returnString;
	}

}
