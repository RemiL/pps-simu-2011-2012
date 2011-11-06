package simulateur.generateurs.implementations;

import simulateur.generateurs.interfaces.GenerateurApparitionsClients;
import utils.GenerateurAleatoirePoisson;

/**
 * Classe repr�sentant un g�n�rateur d'apparitions de clients al�atoire bas� sur
 * une loi de Poisson (caract�ris�e par son esp�rance et sa variance lambda).
 */
public class GenApparitionsClientsPoisson implements GenerateurApparitionsClients {
	/** L'esp�rance et la variance de la loi de poisson */
	private double lambda;

	/**
	 * Construit un nouveau g�n�rateur d'apparitions de clients al�atoires bas�
	 * sur une loi de Poisson de param�tre lambda.
	 * 
	 * @param lambda
	 *            le param�tre de la loi de Poisson
	 */
	public GenApparitionsClientsPoisson(double lambda) {
		this.lambda = lambda;
	}

	/**
	 * G�n�re une valeur g�n�r�e selon la loi de Poisson correspondant au nombre
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
