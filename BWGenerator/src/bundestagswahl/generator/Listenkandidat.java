package bundestagswahl.generator;

public class Listenkandidat {
	public int partei;

	public int bundesland;
	public int politiker;
	public int listenplatz;

	public Listenkandidat(int partei, int bundesland, int listenplatz,
			int politiker) {
		super();
		this.partei = partei;
		this.bundesland = bundesland;
		this.listenplatz = listenplatz;

		this.politiker = politiker;
	}

	public String toString() {
		return this.partei + "," + bundesland + ", " + listenplatz + ",  "
				+ politiker;
	}
}
