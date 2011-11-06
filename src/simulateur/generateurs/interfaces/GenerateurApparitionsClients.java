package simulateur.generateurs.interfaces;

/**
 * Interface d�finissant un g�n�rateur d'apparition de clients dans le temps.
 */
public interface GenerateurApparitionsClients {
	/**
	 * G�n�re une valeur correspondant au nombre de clients apparaissant dans la
	 * ville pendant le prochain pas de temps de la simulation.
	 * 
	 * @return une valeur correspondant au nombre de clients apparaissant dans
	 *         la ville pendant le prochain pas de temps de la simulation
	 */
	public int genererNombreApparitionsClients();
}
