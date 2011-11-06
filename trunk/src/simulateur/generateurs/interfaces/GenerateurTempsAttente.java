package simulateur.generateurs.interfaces;

/**
 * Interface d�finissant un g�n�rateur de temps d'attente pour les clients.
 */
public interface GenerateurTempsAttente {
	/**
	 * G�n�re un temps d'attente (exprim� en secondes) pour un client.
	 * 
	 * @return un temps d'attente (exprim� en secondes) pour un client
	 */
	public double genererTempsAttente();
}
