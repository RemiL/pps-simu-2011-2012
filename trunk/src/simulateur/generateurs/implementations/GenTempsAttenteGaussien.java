package simulateur.generateurs.implementations;

import simulateur.generateurs.interfaces.GenerateurTempsAttente;
import utils.GenerateurAleatoireGaussien;

/**
 * Classe représentant un générateur de temps d'attente alétoire pour les
 * clients basé sur une loi gaussienne (caractérisée par son espérance et son
 * écart-type) dont on exclut les valeurs négatives.
 */
public class GenTempsAttenteGaussien implements GenerateurTempsAttente {
	/** Le temps d'attente moyen d'un client */
	private double tempsAttenteMoyen;
	/** L'écart-type de la loi gaussienne utilisée */
	private double ecartType;

	/**
	 * Construit un générateur de temps d'attente alétoire pour les clients basé
	 * sur une loi gaussienne dont les paramètres sont fournis.
	 * 
	 * @param tempsAttenteMoyen
	 *            le temps d'attente moyen d'un client
	 * @param ecartType
	 *            l'écart-type de la loi gaussienne utilisée
	 */
	public GenTempsAttenteGaussien(double tempsAttenteMoyen, double ecartType) {
		if (tempsAttenteMoyen < 0) {
			throw new IllegalArgumentException("Le temps d'attente ne peut pas être négatif.");
		}

		this.tempsAttenteMoyen = tempsAttenteMoyen;
		this.ecartType = ecartType;
	}

	/**
	 * Génère un temps d'attente (exprimé en secondes) pour un client grâce à
	 * une loi gausienne dont on a exclu les valeurs négatives.
	 * 
	 * @return un temps d'attente (exprimé en secondes) pour un client
	 */
	public double genererTempsAttente() {
		double tempsAttente;

		do {
			tempsAttente = GenerateurAleatoireGaussien.generer(tempsAttenteMoyen, ecartType);
		} while (tempsAttente < 0);

		return tempsAttente;
	}
}
