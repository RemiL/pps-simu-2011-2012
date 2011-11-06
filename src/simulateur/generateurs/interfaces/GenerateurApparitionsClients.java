package simulateur.generateurs.interfaces;

/**
 * Interface définissant un générateur d'apparition de clients dans le temps.
 */
public interface GenerateurApparitionsClients {
	/**
	 * Génère une valeur correspondant au nombre de clients apparaissant dans la
	 * ville pendant le prochain pas de temps de la simulation.
	 * 
	 * @return une valeur correspondant au nombre de clients apparaissant dans
	 *         la ville pendant le prochain pas de temps de la simulation
	 */
	public int genererNombreApparitionsClients();
}
