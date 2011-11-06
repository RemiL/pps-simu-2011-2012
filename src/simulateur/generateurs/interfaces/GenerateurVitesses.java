package simulateur.generateurs.interfaces;

/**
 * Interface d�finissant un g�n�rateur de vitesse pour les taxis.
 */
public interface GenerateurVitesses {
	/**
	 * G�n�re une vitesse (exprim�e en m/s) pour un taxi.
	 * 
	 * @return une vitesse (exprim�e en m/s) pour un taxi
	 */
	public double genererVitesse();
}
