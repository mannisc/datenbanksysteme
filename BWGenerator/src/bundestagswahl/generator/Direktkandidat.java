package bundestagswahl.generator;

public class Direktkandidat {
	public int partei;
	public int kandidatennummer;
	public int politiker;
	public int wahlkreis;

	public Direktkandidat(int kandidatennummer, int partei, int politiker,
			int wahlkreis) {
		super();
		this.partei = partei;
		this.kandidatennummer = kandidatennummer;
		this.politiker = politiker;
		this.wahlkreis = wahlkreis;

	}

	public String toString() {
		return kandidatennummer + "," + politiker + "," + this.partei + ","
				+ this.wahlkreis;
	}

}
