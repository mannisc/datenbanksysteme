package generator.bundestagswahl;

public class Politiker {
	public String name;
	public int politikernummer;

	public Politiker(int politikernummer, String name) {
		super();
		this.name = name;
		this.politikernummer = politikernummer;
	}

	public String toString() {
		return this.politikernummer + ",  " + name;
	}

}
