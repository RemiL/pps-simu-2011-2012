package simulateur;

import java.awt.geom.Point2D;

/**
 * Classe repr�sentant un client de la soci�t� de taxi.
 * 
 * Un client est caract�ris� par son �tat (attente d'un taxi, pris en charge par
 * un taxi ou abandon) et par son point de d�part et sa destination. S'il n'a
 * pas �t� pris en charge par un taxi au bout d'un certain temps, un client peut
 * se lasser et abandonner l'id�e d'attendre un taxi.
 */
public class Client {
	/**
	 * Le r�f�rentiel de temps partag� par tous les parties prenantes du
	 * probl�me (centrale des taxis, taxis et clients).
	 */
	private ReferentielTemps referentielTemps;
	/**
	 * Les points de d�part et d'arriv�e du client dans le r�p�re gradu� en
	 * m�tres ayant pour origine le centre de la ville
	 */
	private Point2D.Double depart, arrivee;
	/**
	 * L'horaire (dans le r�f�rentiel de temps partag�) apr�s lequel le client
	 * abandonne l'id�e de prendre un taxi s'il n'a pas �t� pris en charge
	 */
	private double horaireAbandon;

	/**
	 * Enum�ration d�crivant les �tats possibles du client.
	 */
	public enum Etat {
		ATTENTE_TAXI, PRIS_EN_CHARGE, ABANDON
	};

	/** Etat courant du client */
	private Etat etat;

	/**
	 * Construit un nouveau client poss�dant les caract�ristiques fournies.
	 * 
	 * @param referentielTemps
	 *            le r�f�rentiel de temps commun � toutes les parties prenantes
	 * @param depart
	 *            le point de d�part du client dans le r�p�re gradu� en m�tres
	 *            ayant pour origine le centre de la ville
	 * @param arrivee
	 *            le destination du client dans le r�p�re gradu� en m�tres ayant
	 *            pour origine le centre de la ville
	 * @param tempsAttente
	 *            le temps d'attente apr�s lequel le client abandonne l'id�e de
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
	 * Met � jour l'�tat du client en fonction de l'horaire courant dans le
	 * r�f�rentiel de temps commun.
	 */
	public void majEtat() {
		// Si on a d�pass� l'horaire d'abandon, on change l'�tat du client pour
		// refl�ter ce fait.
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
	 * Teste si le client a �t� pris en charge par un taxi.
	 * 
	 * @return vrai si et seulement si le client a �t� pris en charge par un
	 *         taxi et faux sinon
	 */
	public boolean estPrisEnCharge() {
		return etat == Etat.PRIS_EN_CHARGE;
	}

	/**
	 * Retourne le point de d�part du client dans le rep�re gradu� en m�tres
	 * ayant pour origine le centre de la ville.
	 * 
	 * @return le point de d�part du client dans le rep�re gradu� en m�tres
	 *         ayant pour origine le centre de la ville
	 */
	public Point2D.Double getDepart() {
		return depart;
	}

	/**
	 * Retourne la destination du client dans le rep�re gradu� en m�tres ayant
	 * pour origine le centre de la ville.
	 * 
	 * @return le point la destination du client dans le rep�re gradu� en m�tres
	 *         ayant pour origine le centre de la ville
	 */
	public Point2D.Double getArrivee() {
		return arrivee;
	}
}
