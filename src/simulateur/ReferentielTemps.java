package simulateur;

/**
 * Classe repr�sentant le r�f�rentiel de temps commum � toutes les parties
 * prenantes de la simulation.
 * 
 * Le r�f�rentiel de temps est caract�ris� par le pas de temps minimal de la
 * simulation dt et par le temps courant.
 */
public class ReferentielTemps {
	/** Le pas de temps minimal de la simulation en secondes */
	private final double dt;
	/** Le nombre de pas de temps effectu�s depuis le d�but de la simulation */
	private int tick;
	/** Le temps courant de la simulation en secondes */
	private double temps;

	/**
	 * Construit un nouveau r�f�rentiel de temps initialis� au temps 0 poss�dant
	 * le pas de temps sp�cifi�.
	 * 
	 * @param dt
	 *            le pas de temps de la simulation
	 */
	public ReferentielTemps(double dt) {
		this.dt = dt;
		reset();
	}

	/**
	 * R�initialise le r�f�rentiel de temps au temps 0.
	 */
	public void reset() {
		tick = 0;
		temps = 0.0;
	}

	/**
	 * Incr�mente le temps courant d'un pas.
	 */
	public void incrementerTemps() {
		tick++;
		// Evite la d�rive li� aux arrondis cumul�s de temps += dt.
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
