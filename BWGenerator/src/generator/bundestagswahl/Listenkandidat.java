package generator.bundestagswahl;

public class Listenkandidat {
	public String partei;
	public String bundesland;
	public int politiker;
	public int listenplatz;

	public Listenkandidat(String partei, String bundesland, int listenplatz,
			int politiker) {
		super();
		this.partei = partei;
		this.bundesland = bundesland;
		this.listenplatz = listenplatz;

		this.politiker = politiker;
	}

	public String toString() {
		return this.partei + ",  " + bundesland + ",  " + listenplatz + ",  "
				+ politiker;
	}
}
