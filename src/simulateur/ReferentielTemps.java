package simulateur;

/**
 * Classe représentant le référentiel de temps commum à toutes les parties
 * prenantes de la simulation.
 * 
 * Le référentiel de temps est caractérisé par le pas de temps minimal de la
 * simulation dt et par le temps courant.
 */
public class ReferentielTemps {
	/** Le pas de temps minimal de la simulation en secondes */
	private final double dt;
	/** Le nombre de pas de temps effectués depuis le début de la simulation */
	private int tick;
	/** Le temps courant de la simulation en secondes */
	private double temps;

	/**
	 * Construit un nouveau référentiel de temps initialisé au temps 0 possédant
	 * le pas de temps spécifié.
	 * 
	 * @param dt
	 *            le pas de temps de la simulation
	 */
	public ReferentielTemps(double dt) {
		this.dt = dt;
		reset();
	}

	/**
	 * Réinitialise le référentiel de temps au temps 0.
	 */
	public void reset() {
		tick = 0;
		temps = 0.0;
	}

	/**
	 * Incrémente le temps courant d'un pas.
	 */
	public void incrementerTemps() {
		tick++;
		// Evite la dérive lié aux arrondis cumulés de temps += dt.
		temps = tick * dt;
	}

	/**
	 * Retourne le pas de temps de la simulation.
	 * 
	 * @return le pas de temps de la simulation
	 */
	public double getDt() {
		return dt;
	}

	/**
	 * Retourne le temps courant de la simulation.
	 * 
	 * @return le temps courant de la simulation
	 */
	public double getTemps() {
		return temps;
	}
}
