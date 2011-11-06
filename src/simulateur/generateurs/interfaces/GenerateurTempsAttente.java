package simulateur.generateurs.interfaces;

/**
 * Interface définissant un générateur de temps d'attente pour les clients.
 */
public interface GenerateurTempsAttente {
	/**
	 * Génère un temps d'attente (exprimé en secondes) pour un client.
	 * 
	 * @return un temps d'attente (exprimé en secondes) pour un client
	 */
	public double genererTempsAttente();
}
