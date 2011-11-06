package simulateur.generateurs.implementations;

import simulateur.generateurs.interfaces.GenerateurTempsAttente;
import utils.GenerateurAleatoireGaussien;

/**
 * Classe repr�sentant un g�n�rateur de temps d'attente al�toire pour les
 * clients bas� sur une loi gaussienne (caract�ris�e par son esp�rance et son
 * �cart-type) dont on exclut les valeurs n�gatives.
 */
public class GenTempsAttenteGaussien implements GenerateurTempsAttente {
	/** Le temps d'attente moyen d'un client */
	private double tempsAttenteMoyen;
	/** L'�cart-type de la loi gaussienne utilis�e */
	private double ecartType;

	/**
	 * Construit un g�n�rateur de temps d'attente al�toire pour les clients bas�
	 * sur une loi gaussienne dont les param�tres sont fournis.
	 * 
	 * @param tempsAttenteMoyen
	 *            le temps d'attente moyen d'un client
	 * @param ecartType
	 *            l'�cart-type de la loi gaussienne utilis�e
	 */
	public GenTempsAttenteGaussien(double tempsAttenteMoyen, double ecartType) {
		if (tempsAttenteMoyen < 0) {
			throw new IllegalArgumentException("Le temps d'attente ne peut pas �tre n�gatif.");
		}

		this.tempsAttenteMoyen = tempsAttenteMoyen;
		this.ecartType = ecartType;
	}

	/**
	 * G�n�re un temps d'attente (exprim� en secondes) pour un client gr�ce �
	 * une loi gausienne dont on a exclu les valeurs n�gatives.
	 * 
	 * @return un temps d'attente (exprim� en secondes) pour un client
	 */
	public double genererTempsAttente() {
		double tempsAttente;

		do {
			tempsAttente = GenerateurAleatoireGaussien.generer(tempsAttenteMoyen, ecartType);
		} while (tempsAttente < 0);

		return tempsAttente;
	}
}
