package generator.bundestagswahl;

public class Direktkandidat {
	public String partei;
	public int kandidatennummer;
	public int politiker;
	public int wahlkreis;

	public Direktkandidat(int kandidatennummer, String partei, int politiker,
			int wahlkreis) {
		super();
		this.partei = partei;
		this.kandidatennummer = kandidatennummer;
		this.politiker = politiker;
		this.wahlkreis = wahlkreis;

	}

	public String toString() {
		return this.partei + ",  " + kandidatennummer + ",  " + politiker
				+ ",  " + this.wahlkreis;
	}
}
