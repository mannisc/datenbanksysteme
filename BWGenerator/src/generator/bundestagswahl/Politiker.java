package generator.bundestagswahl;

public class Politiker {
	public String name;
	public int politikernummer;

	public Politiker(int politikernummer, String name) {
		super();
		// Name formattieren
		String[] temp;
		temp = name.split(",");
		this.name = temp[1] + " " + temp[0];
		this.politikernummer = politikernummer;
	}

	public String toString() {

		return this.politikernummer + ",'" + name + "'";
	}

}
