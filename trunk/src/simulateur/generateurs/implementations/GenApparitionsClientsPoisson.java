package simulateur.generateurs.implementations;

import simulateur.generateurs.interfaces.GenerateurApparitionsClients;
import utils.GenerateurAleatoirePoisson;

/**
 * Classe représentant un générateur d'apparitions de clients aléatoire basé sur
 * une loi de Poisson (caractérisée par son espérance et sa variance lambda).
 */
public class GenApparitionsClientsPoisson implements GenerateurApparitionsClients {
	/** L'espérance et la variance de la loi de poisson */
	private double lambda;

	/**
	 * Construit un nouveau générateur d'apparitions de clients aléatoires basé
	 * sur une loi de Poisson de paramètre lambda.
	 * 
	 * @param lambda
	 *            le paramètre de la loi de Poisson
	 */
	public GenApparitionsClientsPoisson(double lambda) {
		this.lambda = lambda;
	}

	/**
	 * Génère une valeur générée selon la loi de Poisson correspondant au nombre
	 * de clients apparaissant dans la ville pendant le prochain pas de temps de
	 * la simulation.
	 * 
	 * @return une valeur selon la loi de Poisson correspondant au nombre de
	 *         clients apparaissant dans la ville pendant le prochain pas de
	 *         temps de la simulation
	 */
	public int genererNombreApparitionsClients() {
		return GenerateurAleatoirePoisson.generer(lambda);
	}
}
