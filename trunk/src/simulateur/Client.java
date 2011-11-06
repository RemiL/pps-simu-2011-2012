package simulateur;

import java.awt.geom.Point2D;

/**
 * Classe représentant un client de la société de taxi.
 * 
 * Un client est caractérisé par son état (attente d'un taxi, pris en charge par
 * un taxi ou abandon) et par son point de départ et sa destination. S'il n'a
 * pas été pris en charge par un taxi au bout d'un certain temps, un client peut
 * se lasser et abandonner l'idée d'attendre un taxi.
 */
public class Client {
	/**
	 * Le référentiel de temps partagé par tous les parties prenantes du
	 * problème (centrale des taxis, taxis et clients).
	 */
	private ReferentielTemps referentielTemps;
	/**
	 * Les points de départ et d'arrivée du client dans le répère gradué en
	 * mètres ayant pour origine le centre de la ville
	 */
	private Point2D.Double depart, arrivee;
	/**
	 * L'horaire (dans le référentiel de temps partagé) après lequel le client
	 * abandonne l'idée de prendre un taxi s'il n'a pas été pris en charge
	 */
	private double horaireAbandon;

	/**
	 * Enumération décrivant les états possibles du client.
	 */
	public enum Etat {
		ATTENTE_TAXI, PRIS_EN_CHARGE, ABANDON
	};

	/** Etat courant du client */
	private Etat etat;

	/**
	 * Construit un nouveau client possédant les caractéristiques fournies.
	 * 
	 * @param referentielTemps
	 *            le référentiel de temps commun à toutes les parties prenantes
	 * @param depart
	 *            le point de départ du client dans le répère gradué en mètres
	 *            ayant pour origine le centre de la ville
	 * @param arrivee
	 *            le destination du client dans le répère gradué en mètres ayant
	 *            pour origine le centre de la ville
	 * @param tempsAttente
	 *            le temps d'attente après lequel le client abandonne l'idée de
	 *            prendre un taxi
	 */
	public Client(ReferentielTemps referentielTemps, Point2D.Double depart, Point2D.Double arrivee, double tempsAttente) {
		this.referentielTemps = referentielTemps;

		this.depart = depart;
		this.arrivee = arrivee;
		this.horaireAbandon = referentielTemps.getTemps() + tempsAttente;
		this.etat = Etat.ATTENTE_TAXI;
	}

	/**
	 * Met à jour l'état du client en fonction de l'horaire courant dans le
	 * référentiel de temps commun.
	 */
	public void majEtat() {
		// Si on a dépassé l'horaire d'abandon, on change l'état du client pour
		// refléter ce fait.
		if (etat == Etat.ATTENTE_TAXI && horaireAbandon < referentielTemps.getTemps()) {
			etat = Etat.ABANDON;
		}
	}

	/**
	 * Teste si le client est toujours en train d'attendre un taxi.
	 * 
	 * @return vrai si et seulement si le client est toujours en train
	 *         d'attendre un taxi et faux sinon
	 */
	public boolean estEnAttenteTaxi() {
		majEtat();

		return etat == Etat.ATTENTE_TAXI;
	}

	/**
	 * Marque le client comme pris en charge par un taxi.
	 */
	public void prendreEnCharge() {
		etat = Etat.PRIS_EN_CHARGE;
	}

	/**
	 * Teste si le client a été pris en charge par un taxi.
	 * 
	 * @return vrai si et seulement si le client a été pris en charge par un
	 *         taxi et faux sinon
	 */
	public boolean estPrisEnCharge() {
		return etat == Etat.PRIS_EN_CHARGE;
	}

	/**
	 * Retourne le point de départ du client dans le repère gradué en mètres
	 * ayant pour origine le centre de la ville.
	 * 
	 * @return le point de départ du client dans le repère gradué en mètres
	 *         ayant pour origine le centre de la ville
	 */
	public Point2D.Double getDepart() {
		return depart;
	}

	/**
	 * Retourne la destination du client dans le repère gradué en mètres ayant
	 * pour origine le centre de la ville.
	 * 
	 * @return le point la destination du client dans le repère gradué en mètres
	 *         ayant pour origine le centre de la ville
	 */
	public Point2D.Double getArrivee() {
		return arrivee;
	}
}
