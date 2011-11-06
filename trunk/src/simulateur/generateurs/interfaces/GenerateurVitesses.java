package simulateur.generateurs.interfaces;

/**
 * Interface définissant un générateur de vitesse pour les taxis.
 */
public interface GenerateurVitesses {
	/**
	 * Génère une vitesse (exprimée en m/s) pour un taxi.
	 * 
	 * @return une vitesse (exprimée en m/s) pour un taxi
	 */
	public double genererVitesse();
}
