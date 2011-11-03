package simulateur;

public class ReferentielTemps {

	private final double dt;
	private int tick;
	private double temps;

	public ReferentielTemps(double dt) {
		this.dt = dt;
		this.tick = 0;
		this.temps = 0.0;
	}

	public void incrementerTemps() {
		tick++;
		temps = tick * dt;
	}

	public double getDt() {
		return dt;
	}

	public double getTemps() {
		return temps;
	}
}
